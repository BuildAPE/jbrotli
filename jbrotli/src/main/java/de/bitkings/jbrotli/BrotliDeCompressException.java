package de.bitkings.jbrotli;

public class BrotliDeCompressException extends RuntimeException {
  public BrotliDeCompressException() {
  }

  public BrotliDeCompressException(String message) {
    super(message);
  }

  public BrotliDeCompressException(String message, Throwable cause) {
    super(message, cause);
  }

  public BrotliDeCompressException(Throwable cause) {
    super(cause);
  }

  public BrotliDeCompressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
