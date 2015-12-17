package de.bitkings.jbrotli;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocateDirect;

public class BrotliOutputStream extends OutputStream {

  private final BrotliStreamCompressor brotliStreamCompressor;
  private OutputStream outputStream;

  public BrotliOutputStream(OutputStream outputStream) {
    this(outputStream, Brotli.DEFAULT_PARAMETER);
  }

  public BrotliOutputStream(OutputStream outputStream, Brotli.Parameter parameter) {
    this.outputStream = outputStream;
    brotliStreamCompressor = new BrotliStreamCompressor();
    brotliStreamCompressor.init(parameter);
  }

  @Override
  public void write(int i) throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1);
    byteBuffer.put((byte) (i & 0xff));
    byteBuffer.position(0);
    outputStream.write(asByteArray(brotliStreamCompressor.compress(byteBuffer, false)));
  }

  @Override
  public void write(byte[] b) throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(b.length);
    byteBuffer.put(b);
    byteBuffer.position(0);
    outputStream.write(asByteArray(brotliStreamCompressor.compress(byteBuffer, false)));
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(b.length);
    byteBuffer.put(b);
    byteBuffer.position(off);
    byteBuffer.limit(len);
    outputStream.write(asByteArray(brotliStreamCompressor.compress(byteBuffer, false)));
  }

  @Override
  public void flush() throws IOException {
    outputStream.write(asByteArray(brotliStreamCompressor.compress(allocateDirect(0), true)));
    outputStream.flush();
  }

  private byte[] asByteArray(ByteBuffer compressed) {
    byte[] buffer = new byte[compressed.capacity()];
    compressed.get(buffer);
    return buffer;
  }

  @Override
  public void close() throws IOException {
    flush();
    outputStream.close();
  }
}
