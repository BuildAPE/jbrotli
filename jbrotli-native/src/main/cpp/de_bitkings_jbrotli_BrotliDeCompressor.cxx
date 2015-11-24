

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

#include "de_bitkings_jbrotli_BrotliDeCompressor.h"
#include "../../../../brotli/dec/decode.h"



#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     de_bitkings_jbrotli_BrotliDeCompressor
 * Method:    deCompressBytes
 * Signature: ([BII[B)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliDeCompressor_deCompressBytes(JNIEnv *env, jclass thisObj, jbyteArray encodedByteArray, jint inPos, jint inLen, jbyteArray outByteArray) {
  size_t output_length;

  uint8_t *encodedBuffer = (uint8_t*)env->GetPrimitiveArrayCritical(encodedByteArray, 0);
  if (encodedBuffer == NULL || env->ExceptionCheck()) return -1;
  uint8_t *outBuffer = (uint8_t*)env->GetPrimitiveArrayCritical(outByteArray, 0);
  if (outBuffer == NULL || env->ExceptionCheck()) return -1;

  BrotliResult brotliResult = BrotliDecompressBuffer(inLen, encodedBuffer, &output_length, outBuffer);

  if (brotliResult == BROTLI_RESULT_ERROR) return -10;
  if (brotliResult == BROTLI_RESULT_NEEDS_MORE_INPUT) return -12;
  if (brotliResult == BROTLI_RESULT_NEEDS_MORE_OUTPUT) return -13;

  env->ReleasePrimitiveArrayCritical(outByteArray, outBuffer, 0);
  if (env->ExceptionCheck()) return -1;
  env->ReleasePrimitiveArrayCritical(encodedByteArray, encodedBuffer, 0);
  if (env->ExceptionCheck()) return -1;

  return output_length;
}

/*
 * Class:     de_bitkings_jbrotli_BrotliDeCompressor
 * Method:    deCompressByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;IILjava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliDeCompressor_deCompressByteBuffer(JNIEnv *env, jclass thisObj, jobject inBuf, jint inPos, jint inLen, jobject outBuf) {
  // size_t output_length;
  // brotli::BrotliParams params;

  // uint8_t *inBufPtr = (uint8_t *)env->GetDirectBufferAddress(inBuf);
  // if (inBufPtr==NULL) return -1;

  // uint8_t *outBufPtr = (uint8_t *)env->GetDirectBufferAddress(outBuf);
  // if (outBufPtr==NULL) return -1;

  // int ok = brotli::BrotliCompressBuffer(params, inLen, inBufPtr, &output_length, outBufPtr);
  // if (!ok) {
  //   return -1;
  // }

  // return output_length;
  return -1;
}

#ifdef __cplusplus
}
#endif

