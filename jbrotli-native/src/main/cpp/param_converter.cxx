
#include <jni.h>
#include "../../../../brotli/enc/encode.h"
#include "./param_converter.h"

brotli::BrotliParams mapToBrotliParams(JNIEnv *env, jint mode, jint quality, jint lgwin, jint lgblock) {
  brotli::BrotliParams params;
  switch (mode) {
    case 0:
      params.mode = brotli::BrotliParams::MODE_GENERIC;
      break;
    case 1:
      params.mode = brotli::BrotliParams::MODE_TEXT;
      break;
    case 2:
      params.mode = brotli::BrotliParams::MODE_FONT;
      break;
    default:
      params.mode = brotli::BrotliParams::MODE_GENERIC;
  }
  params.quality = quality;
  params.lgwin = lgwin;
  params.lgblock = lgblock;
  return params;
}
