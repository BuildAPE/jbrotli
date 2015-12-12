
#ifndef _Included_param_converter
#define _Included_param_converter

brotli::BrotliParams mapToBrotliParams(JNIEnv *env, jint mode, jint quality, jint lgwin, jint lgblock);

#endif