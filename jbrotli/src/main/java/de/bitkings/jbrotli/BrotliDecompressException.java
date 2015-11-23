package de.bitkings.jbrotli;

public class BrotliDecompressException extends RuntimeException {
  public BrotliDecompressException() {
  }

  public BrotliDecompressException(String message) {
    super(message);
  }

  public BrotliDecompressException(String message, Throwable cause) {
    super(message, cause);
  }

  public BrotliDecompressException(Throwable cause) {
    super(cause);
  }

  public BrotliDecompressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
