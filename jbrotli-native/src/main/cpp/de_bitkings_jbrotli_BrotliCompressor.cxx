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
#include "./param_converter.h"
#include "./de_bitkings_jbrotli_BrotliCompressor.h"
#include "./de_bitkings_jbrotli_BrotliError.h"


#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     de_bitkings_jbrotli_BrotliCompressor
 * Method:    compressBytes
 * Signature: (IIII[BII[BII)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliCompressor_compressBytes(JNIEnv *env,
                                                                               jclass thisObj,
                                                                               jint mode,
                                                                               jint quality,
                                                                               jint lgwin,
                                                                               jint lgblock,
                                                                               jbyteArray inByteArray,
                                                                               jint inPosition,
                                                                               jint inLength,
                                                                               jbyteArray outByteArray,
                                                                               jint outPosition,
                                                                               jint outLength) {

  if (inPosition < 0 || inLength < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: input array position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }

  if (outPosition < 0 || outLength < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: output array position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }

  if (inLength == 0) return 0;

  size_t computedOutLength = outLength;
  brotli::BrotliParams params = mapToBrotliParams(env, mode, quality, lgwin, lgblock);

  uint8_t *inBufCritArray = (uint8_t *) env->GetPrimitiveArrayCritical(inByteArray, 0);
  if (inBufCritArray == NULL || env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_COMPRESS_GetPrimitiveArrayCritical_INBUF;
  uint8_t *outBufCritArray = (uint8_t *) env->GetPrimitiveArrayCritical(outByteArray, 0);
  if (outBufCritArray == NULL || env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_COMPRESS_GetPrimitiveArrayCritical_OUTBUF;

  int ok = brotli::BrotliCompressBuffer(params, inLength, inBufCritArray + inPosition, &computedOutLength, outBufCritArray + outPosition);

  env->ReleasePrimitiveArrayCritical(outByteArray, outBufCritArray, 0);
  if (env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_COMPRESS_ReleasePrimitiveArrayCritical_OUTBUF;
  env->ReleasePrimitiveArrayCritical(inByteArray, inBufCritArray, 0);
  if (env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_COMPRESS_ReleasePrimitiveArrayCritical_INBUF;

  if (!ok) {
    return de_bitkings_jbrotli_BrotliError_COMPRESS_BrotliCompressBuffer;
  }

  return computedOutLength;
}

/*
 * Class:     de_bitkings_jbrotli_BrotliCompressor
 * Method:    compressByteBuffer
 * Signature: (IIIILjava/nio/ByteBuffer;IILjava/nio/ByteBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliCompressor_compressByteBuffer(JNIEnv *env,
                                                                                    jclass thisObj,
                                                                                    jint mode,
                                                                                    jint quality,
                                                                                    jint lgwin,
                                                                                    jint lgblock,
                                                                                    jobject inBuf,
                                                                                    jint inPosition,
                                                                                    jint inLength,
                                                                                    jobject outBuf,
                                                                                    jint outPosition,
                                                                                    jint outLength) {

  if (inPosition < 0 || inLength < 0 ) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: input ByteBuffer position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }
  if (outPosition < 0 || outLength < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: output ByteBuffer position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }

  if (inLength == 0) return 0;

  brotli::BrotliParams params = mapToBrotliParams(env, mode, quality, lgwin, lgblock);

  uint8_t *inBufPtr = (uint8_t *) env->GetDirectBufferAddress(inBuf);
  if (inBufPtr == NULL) return de_bitkings_jbrotli_BrotliError_COMPRESS_ByteBuffer_GetDirectBufferAddress_INBUF;

  uint8_t *outBufPtr = (uint8_t *) env->GetDirectBufferAddress(outBuf);
  if (outBufPtr == NULL) return de_bitkings_jbrotli_BrotliError_COMPRESS_ByteBuffer_GetDirectBufferAddress_OUTBUF;

  size_t computedOutLength = outLength;
  int ok = brotli::BrotliCompressBuffer(params, inLength, inBufPtr + inPosition, &computedOutLength, outBufPtr + outPosition);
  if (!ok) {
    return de_bitkings_jbrotli_BrotliError_COMPRESS_ByteBuffer_BrotliCompressBuffer;
  }

  return computedOutLength;
}

#ifdef __cplusplus
}
#endif

