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

#include "../../../../brotli/dec/decode.h"
#include "./de_bitkings_jbrotli_BrotliDeCompressor.h"
#include "./de_bitkings_jbrotli_BrotliError.h"


#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     de_bitkings_jbrotli_BrotliDeCompressor
 * Method:    deCompressBytes
 * Signature: ([BII[BII)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliDeCompressor_deCompressBytes(JNIEnv *env,
                                                                                   jclass thisObj,
                                                                                   jbyteArray encodedByteArray,
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

  uint8_t *encodedBuffer = (uint8_t *) env->GetPrimitiveArrayCritical(encodedByteArray, 0);
  if (encodedBuffer == NULL || env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_GetPrimitiveArrayCritical_INBUF;
  uint8_t *outBuffer = (uint8_t *) env->GetPrimitiveArrayCritical(outByteArray, 0);
  if (outBuffer == NULL || env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_GetPrimitiveArrayCritical_OUTBUF;

  size_t computedOutLength = outLength;
  BrotliResult brotliResult = BrotliDecompressBuffer(inLength, encodedBuffer + inPosition, &computedOutLength, outBuffer + outPosition);

  env->ReleasePrimitiveArrayCritical(outByteArray, outBuffer, 0);
  if (env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ReleasePrimitiveArrayCritical_OUTBUF;
  env->ReleasePrimitiveArrayCritical(encodedByteArray, encodedBuffer, 0);
  if (env->ExceptionCheck()) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ReleasePrimitiveArrayCritical_INBUF;

  if (brotliResult == BROTLI_RESULT_ERROR) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_BROTLI_RESULT_ERROR;
  if (brotliResult == BROTLI_RESULT_NEEDS_MORE_INPUT) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_BROTLI_RESULT_NEEDS_MORE_INPUT;
  if (brotliResult == BROTLI_RESULT_NEEDS_MORE_OUTPUT) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_BROTLI_RESULT_NEEDS_MORE_OUTPUT;

  return computedOutLength;
}

/*
 * Class:     de_bitkings_jbrotli_BrotliDeCompressor
 * Method:    deCompressByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;IILjava/nio/ByteBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliDeCompressor_deCompressByteBuffer(JNIEnv *env,
                                                                                        jclass thisObj,
                                                                                        jobject inBuf,
                                                                                        jint inPosition,
                                                                                        jint inLength,
                                                                                        jobject outBuf,
                                                                                        jint outPosition,
                                                                                        jint outLength) {

  if (inPosition < 0 || inLength < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: input ByteBuffer position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }
  if (outPosition < 0 || outLength < 0) {
    env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Brotli: output ByteBuffer position and length must be greater than zero.");
    return de_bitkings_jbrotli_BrotliError_NATIVE_ERROR;
  }

  if (inLength == 0) return 0;

  uint8_t *inBufPtr = (uint8_t *)env->GetDirectBufferAddress(inBuf);
  if (inBufPtr==NULL) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ByteBuffer_GetDirectBufferAddress_INBUF;

  uint8_t *outBufPtr = (uint8_t *)env->GetDirectBufferAddress(outBuf);
  if (outBufPtr==NULL) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ByteBuffer_GetDirectBufferAddress_OUTBUF;

  size_t computedOutLength = outLength;
  BrotliResult brotliResult = BrotliDecompressBuffer(inLength, inBufPtr + inPosition, &computedOutLength, outBufPtr + outPosition);

  if (brotliResult == BROTLI_RESULT_ERROR) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ByteBuffer_BROTLI_RESULT_ERROR;
  if (brotliResult == BROTLI_RESULT_NEEDS_MORE_INPUT) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ByteBuffer_BROTLI_RESULT_NEEDS_MORE_INPUT;
  if (brotliResult == BROTLI_RESULT_NEEDS_MORE_OUTPUT) return de_bitkings_jbrotli_BrotliError_DECOMPRESS_ByteBuffer_BROTLI_RESULT_NEEDS_MORE_OUTPUT;

  return computedOutLength;
}

#ifdef __cplusplus
}
#endif

