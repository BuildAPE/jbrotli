package de.bitkings.jbrotli;

import java.io.IOException;
import java.io.UncheckedIOException;

public class BrotliErrorChecker {

  /**
   * @param errorCode errorCode
   */
  public static boolean isBrotliOk(int errorCode) {
    return errorCode >= 0;
  }

  /**
   * @param errorCode errorCode
   * @throws UncheckedIOException in case of errors
   */
  public static void assertBrotliOk(int errorCode) throws UncheckedIOException {
    String msg = resolveErrorCode2Message(errorCode);
    if (msg != null) {
      throw new UncheckedIOException(new IOException(msg));
    }
  }

  /**
   * @param errorCode errorCode
   * @return message or null if there was no error
   */
  public static String resolveErrorCode2Message(int errorCode) {
    if (isBrotliOk(errorCode)) return null;
    switch (errorCode) {
      default:
        return "Error in native Brotli library. Error code: " + errorCode;
    }
  }

}
