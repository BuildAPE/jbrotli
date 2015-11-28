package de.bitkings.jbrotli;

public class BrotliErrorChecker {

  /**
   * @param errorCode errorCode
   */
  public static boolean isBrotliOk(int errorCode) {
    return errorCode >= 0;
  }

  /**
   * @param errorCode errorCode
   * @throws BrotliException in case of errors
   */
  public static int assertBrotliOk(int errorCode) throws BrotliException {
    String msg = resolveErrorCode2Message(errorCode);
    if (msg != null) {
      throw new BrotliException(msg);
    }
    return errorCode;
  }

  /**
   * @param errorCode errorCode
   * @return message or null if there was no error
   */
  public static String resolveErrorCode2Message(int errorCode) {
    if (isBrotliOk(errorCode)) return null;
    String msg = " (Error code: " + errorCode + ")";
    switch (errorCode) {
      case -1:
        return "An error happened inside JNI function call. Maybe OOME or other issues." + msg;
      case BrotliError.DECOMPRESS_BROTLI_RESULT_ERROR:
      case BrotliError.DECOMPRESS_ByteBuffer_BROTLI_RESULT_ERROR:
        return "Decoding error, e.g. corrupt input or no memory left." + msg;
      case BrotliError.DECOMPRESS_BROTLI_RESULT_NEEDS_MORE_INPUT:
      case BrotliError.DECOMPRESS_ByteBuffer_BROTLI_RESULT_NEEDS_MORE_INPUT:
        return "Decompression partially done, but must be invoked again with more input." + msg;
      case BrotliError.DECOMPRESS_BROTLI_RESULT_NEEDS_MORE_OUTPUT:
      case BrotliError.DECOMPRESS_ByteBuffer_BROTLI_RESULT_NEEDS_MORE_OUTPUT:
        return "Decompression partially done, but must be invoked again with more output." + msg;
      default:
        return "Error in native Brotli library." + msg;
    }
  }

}
