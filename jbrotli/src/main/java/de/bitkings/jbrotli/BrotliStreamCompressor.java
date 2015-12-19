package de.bitkings.jbrotli;

import java.io.Closeable;
import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliErrorChecker.assertBrotliOk;
import static de.bitkings.jbrotli.BrotliErrorChecker.isBrotliOk;

public final class BrotliStreamCompressor implements Closeable {

  static {
    assertBrotliOk(initJavaFieldIdCache());
  }

  // will be used from native code to store native compressor object
  private final long brotliCompressorInstanceRef = 0;

  public BrotliStreamCompressor() {
    this(Brotli.DEFAULT_PARAMETER);
  }

  public BrotliStreamCompressor(Brotli.Parameter parameter) {
    assertBrotliOk(initBrotliCompressor(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock()));
  }

  public final int compress(byte[] in, byte[] out) {
    return compress(in, 0, in.length, out);
  }

  public final int compress(byte[] in, int inPosition, int inLength, byte[] out) {
    if (inPosition + inLength > in.length || inPosition < 0 || inLength < 0) {
      throw new IllegalArgumentException("The source position + length must me smaller then the source byte array's length.");
    }
    return assertBrotliOk(compressBytes(in, inPosition, inLength, out, out.length));
  }

  /**
   * One may use {@link ByteBuffer#position(int)} and {@link ByteBuffer#limit(int)} to adjust
   * how the buffers are used for reading and writing.
   *
   * @param in      input buffer
   * @param doFlush do flush
   * @return a direct baked {@link ByteBuffer} containing the compressed output OR null, if input was 0 bytes
   */
  public final ByteBuffer compress(ByteBuffer in, boolean doFlush) {
    int inPosition = in.position();
    int inLimit = in.limit();
    int inRemain = inLimit - inPosition;
    if (inRemain < 0)
      throw new IllegalArgumentException("The source (in) position must me smaller then the source ByteBuffer's limit.");

    ByteBuffer out;
    if (in.isDirect()) {
      out = compressByteBuffer(in, inPosition, inRemain, doFlush);
    } else if (in.hasArray()) {
//      outLength = assertBrotliOk(compressBytes(in.array(), inPosition + in.arrayOffset(), inRemain, out.array()));
//      out.inLimit(inPosition + outLength);
      throw new UnsupportedOperationException("Not yet implemented");
    } else {
//      byte[] b = new byte[inRemain];
//      in.get(b);
//      outLength = assertBrotliOk(compressBytes(b, 0, b.length, out.array()));
      throw new UnsupportedOperationException("Not yet implemented");
    }
    in.position(inLimit);
    return out;
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    close();
  }

  @Override
  public void close() throws BrotliException {
    isBrotliOk(freeNativeResources());
  }

  public final int getMaxInputBufferSize() {
    return assertBrotliOk(getInputBlockSize());
  }

  private native static int initJavaFieldIdCache();

  private native int getInputBlockSize();

  private native int initBrotliCompressor(int mode, int quality, int lgwin, int lgblock);

  private native int freeNativeResources();

  private native int compressBytes(byte[] inArray, int inPosition, int inLength, byte[] outArray, int outLength);

  private native ByteBuffer compressByteBuffer(ByteBuffer inByteBuffer, int inPosition, int inLength, boolean doFlush);

}
