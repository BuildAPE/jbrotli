/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_bitkings_jbrotli_BrotliCompressor */

#ifndef _Included_de_bitkings_jbrotli_BrotliCompressor
#define _Included_de_bitkings_jbrotli_BrotliCompressor
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_bitkings_jbrotli_BrotliCompressor
 * Method:    compressBytes
 * Signature: (Lde/bitkings/jbrotli/Brotli/Parameter;[BII[B)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliCompressor_compressBytes
  (JNIEnv *, jclass, jobject, jbyteArray, jint, jint, jbyteArray);

/*
 * Class:     de_bitkings_jbrotli_BrotliCompressor
 * Method:    compressByteBuffer
 * Signature: (Lde/bitkings/jbrotli/Brotli/Parameter;Ljava/nio/ByteBuffer;IILjava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_bitkings_jbrotli_BrotliCompressor_compressByteBuffer
  (JNIEnv *, jclass, jobject, jobject, jint, jint, jobject);

#ifdef __cplusplus
}
#endif
#endif
