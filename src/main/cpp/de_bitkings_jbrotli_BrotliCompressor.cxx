

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

#include "de_bitkings_jbrotli_BrotliCompressor.h"
#include "../../../brotli/enc/encode.h"


#ifdef _WIN64
#define jlong2ptr(x) ((void*)(x))
#else
#define jlong2ptr(x) ((void*)(int)(x))
#endif


#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     de_bitkings_jbrotli_BrotliCompressor
 * Method:    compressBytes
 * Signature: (Lde/bitkings/jbrotli/Brotli/Parameter;[BII[B)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliCompressor_compressBytes(JNIEnv* env, jclass thisObj, jobject brotliParams, jbyteArray inBuf, jint inPos, jint inLen, jbyteArray outBuf) {
  size_t output_length;
  brotli::BrotliParams params;
  
  uint8_t *inBufCritArray = (uint8_t*)env->GetPrimitiveArrayCritical(inBuf, 0);
  uint8_t *outBufCritArray = (uint8_t*)env->GetPrimitiveArrayCritical(outBuf, 0);
  int ok = brotli::BrotliCompressBuffer(params, inLen, inBufCritArray, &output_length, outBufCritArray);
  env->ReleasePrimitiveArrayCritical(inBuf, inBufCritArray, 0);
  env->ReleasePrimitiveArrayCritical(outBuf, outBufCritArray, 0);
  if (!ok) {
    return -1;
  }
  return output_length;
}

/*
 * Class:     de_bitkings_jbrotli_BrotliCompressor
 * Method:    compressByteBuffer
 * Signature: (Lde/bitkings/jbrotli/Brotli/Parameter;JIIJ)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliCompressor_compressByteBuffer(JNIEnv* env, jclass thisObj, jobject brotliParams, jlong inBufLong, jint inPos, jint inLen, jlong outBufLong) {
  size_t output_length;
  brotli::BrotliParams params;
  
  uint8_t *inBufPtr = (uint8_t *)jlong2ptr(inBufLong);
  uint8_t *outBufPtr = (uint8_t *)jlong2ptr(outBufLong);
  int ok = brotli::BrotliCompressBuffer(params, inLen, inBufPtr, &output_length, outBufPtr);
  if (!ok) {
    return -1;
  }
  return output_length;
}

#ifdef __cplusplus
}
#endif

