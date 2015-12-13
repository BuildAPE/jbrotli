/* exporting methods */
#if (__GNUC__ >= 4) || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4)
#  ifndef GCC_HASCLASSVISIBILITY
#    define GCC_HASCLASSVISIBILITY
#  endif
#endif

/* Deal with Apple's deprecated 'AssertMacros.h' from Carbon-framework */
#if defined(__APPLE__) && !defined(__ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES)
# define __ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES 0
#endif

/* Intel's compiler complains if a variable which was never initialised is
 * cast to void, which is a common idiom which we use to indicate that we
 * are aware a variable isn't used.  So we just silence that warning.
 * See: https://github.com/swig/swig/issues/192 for more discussion.
 */
#ifdef __INTEL_COMPILER
# pragma warning disable 592
#endif

/* Fix for jlong on some versions of gcc on Windows */
#if defined(__GNUC__) && !defined(__INTEL_COMPILER)
typedef long long __int64;
#endif

/* Fix for jlong on 64-bit x86 Solaris */
#if defined(__x86_64)
# ifdef _LP64
#   undef _LP64
# endif
#endif

#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "../../../../brotli/enc/encode.h"
#include "./type_converters.h"
#include "./param_converter.h"
#include "./de_bitkings_jbrotli_BrotliCompressor.h"
#include "./de_bitkings_jbrotli_BrotliError.h"


#ifdef __cplusplus
extern "C" {
#endif

static jfieldID brotliCompressorInstanceRefID;

/*
 * Class:     de_bitkings_jbrotli_BrotliStreamCompressor
 * Method:    initJavaFieldIdCache
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_bitkings_jbrotli_BrotliStreamCompressor_initJavaFieldIdCache(JNIEnv *env,
                                                                                            jclass cls) {
  brotliCompressorInstanceRefID = env->GetFieldID(cls, "brotliCompressorInstanceRef", "J");
}

/*
 * Class:     de_bitkings_jbrotli_BrotliStreamCompressor
 * Method:    initBrotliCompressor
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliStreamCompressor_initBrotliCompressor(JNIEnv *env,
                                                                                            jobject thisObj,
                                                                                            jint mode,
                                                                                            jint quality,
                                                                                            jint lgwin,
                                                                                            jint lgblock) {
  brotli::BrotliParams params = mapToBrotliParams(env, mode, quality, lgwin, lgblock);

  brotli::BrotliCompressor *compressor = new brotli::BrotliCompressor(params);
  JNU_SetLongFieldFromPtr(env, thisObj, brotliCompressorInstanceRefID, compressor);

  size_t brotli_ring;
  brotli_ring = compressor->input_block_size();

  return 0;
}

/*
 * Class:     de_bitkings_jbrotli_BrotliStreamCompressor
 * Method:    compressBytes
 * Signature: ([BII[B)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliStreamCompressor_compressBytes(JNIEnv *env,
                                                                                     jobject thisObj,
                                                                                     jbyteArray inByteArray,
                                                                                     jint inPos,
                                                                                     jint inLen,
                                                                                     jbyteArray outByteArray) {
  if (inPos < 0 || inLen < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: input array position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }

  if (inLen == 0) return 0;

  uint8_t *inBufCritArray = (uint8_t *) env->GetPrimitiveArrayCritical(inByteArray, 0);
  if (inBufCritArray == NULL || env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_GetPrimitiveArrayCritical_INBUF;
  uint8_t *outBufCritArray = (uint8_t *) env->GetPrimitiveArrayCritical(outByteArray, 0);
  if (outBufCritArray == NULL || env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_GetPrimitiveArrayCritical_OUTBUF;

  brotli::BrotliCompressor *compressor = (brotli::BrotliCompressor*) JNU_GetLongFieldAsPtr(env, thisObj, brotliCompressorInstanceRefID);

  size_t output_length;
  uint8_t *brotliOutBufferPtr;

  inBufCritArray += inPos;
  compressor->CopyInputToRingBuffer(inLen, inBufCritArray);
  bool writeResult = compressor->WriteBrotliData(true, false, &output_length, &brotliOutBufferPtr);
  if (!writeResult) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_WriteBrotliData;

  if (output_length > 0) {
    memcpy(outBufCritArray, brotliOutBufferPtr, output_length);
  }

  env->ReleasePrimitiveArrayCritical(outByteArray, outBufCritArray, 0);
  if (env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_ReleasePrimitiveArrayCritical_OUTBUF;
  env->ReleasePrimitiveArrayCritical(inByteArray, inBufCritArray, 0);
  if (env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_ReleasePrimitiveArrayCritical_INBUF;

  return output_length;
}

/*
 * Class:     de_bitkings_jbrotli_BrotliStreamCompressor
 * Method:    compressByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;IILjava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliStreamCompressor_compressByteBuffer(JNIEnv *env,
                                                                                          jobject thisObj,
                                                                                          jobject inBuf,
                                                                                          jint inPos,
                                                                                          jint inLen,
                                                                                          jobject outBuf) {

  if (inPos < 0 || inLen < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: input ByteBuffer position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }

  if (inLen == 0) return 0;

  uint8_t *inBufPtr = (uint8_t *) env->GetDirectBufferAddress(inBuf);
  if (inBufPtr == NULL) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_ByteBuffer_GetDirectBufferAddress_INBUF;

  uint8_t *outBufPtr = (uint8_t *) env->GetDirectBufferAddress(outBuf);
  if (outBufPtr == NULL) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_ByteBuffer_GetDirectBufferAddress_OUTBUF;

  brotli::BrotliCompressor *compressor = (brotli::BrotliCompressor *) JNU_GetLongFieldAsPtr(env, thisObj, brotliCompressorInstanceRefID);

  size_t output_length;
  uint8_t *brotliOutBufferPtr;

  inBufPtr += inPos;
  compressor->CopyInputToRingBuffer(inLen, inBufPtr);
  bool writeResult = compressor->WriteBrotliData(true, false, &output_length, &brotliOutBufferPtr);
  if (!writeResult) return de_bitkings_jbrotli_BrotliError_STREAM_COMPRESS_ByteBuffer_WriteBrotliData;

  if (output_length > 0) {
    memcpy(outBufPtr, brotliOutBufferPtr, output_length);
  }

  return output_length;
}

#ifdef __cplusplus
}
#endif

