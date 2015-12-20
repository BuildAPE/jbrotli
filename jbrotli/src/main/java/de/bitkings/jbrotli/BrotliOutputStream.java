package de.bitkings.jbrotli;

import java.io.IOException;
import java.io.OutputStream;

public class BrotliOutputStream extends OutputStream {

  private final BrotliStreamCompressor brotliStreamCompressor;
  private OutputStream outputStream;

  public BrotliOutputStream(OutputStream outputStream) {
    this(outputStream, Brotli.DEFAULT_PARAMETER);
  }

  public BrotliOutputStream(OutputStream outputStream, Brotli.Parameter parameter) {
    this.outputStream = outputStream;
    brotliStreamCompressor = new BrotliStreamCompressor(parameter);
  }

  @Override
  public void write(int i) throws IOException {
    byte[] buf = new byte[]{(byte) (i & 0xff)};
    outputStream.write(brotliStreamCompressor.compress(buf, false));
  }

  @Override
  public void write(byte[] b) throws IOException {
    outputStream.write(brotliStreamCompressor.compress(b, false));
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    outputStream.write(brotliStreamCompressor.compress(b, off, len, false));
  }

  @Override
  public void flush() throws IOException {
    outputStream.write(brotliStreamCompressor.compress(new byte[0], true));
    outputStream.flush();
  }

  @Override
  public void close() throws IOException {
    flush();
    outputStream.close();
  }
}
