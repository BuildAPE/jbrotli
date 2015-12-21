package de.bitkings.jbrotli;

import java.io.Closeable;
import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliErrorChecker.assertBrotliOk;

public final class BrotliStreamDeCompressor implements Closeable {

  static {
    assertBrotliOk(initJavaFieldIdCache());
  }

  public BrotliStreamDeCompressor() {
    assertBrotliOk(initBrotliDeCompressor());
  }

  // will be used from native code to store native decompressor state object
  private final long brotliDeCompressorState = 0;

  /**
   * @param in  input byte array
   * @param out output byte array
   */
  public final int deCompress(byte[] in, byte[] out) {
    return deCompress(in, 0, in.length, out, 0, out.length);
  }

  /**
   * @param in         input byte array
   * @param inPosition position to start decompress from
   * @param inLength   length in byte to decompress
   * @return decompressed byte array
   */
  public final int deCompress(byte[] in, int inPosition, int inLength, byte[] out, int outPosition, int outLength) throws BrotliException {
    if (inPosition + inLength > in.length) {
      throw new IllegalArgumentException("The source position + length must me smaller then the source byte array's length.");
    }
    return assertBrotliOk(deCompressBytes(in, inPosition, inLength, out, outPosition, outLength));
  }

  /**
   * One may use {@link ByteBuffer#position(int)} and {@link ByteBuffer#limit(int)} to adjust
   * how the buffers are used for reading and writing.
   *
   * @param in  input buffer
   * @param out output buffer
   */
  public final int deCompress(ByteBuffer in, ByteBuffer out) throws BrotliException {
    int inPosition = in.position();
    int inLimit = in.limit();
    int inRemain = inLimit - inPosition;
    if (inRemain < 0)
      throw new IllegalArgumentException("The source (in) position must me smaller then the source ByteBuffer's limit.");

//    ByteBuffer out;
    if (in.isDirect()) {
//      out = deCompressByteBuffer(in, inPosition, inRemain, doFlush);
    } else if (in.hasArray()) {
//      out = ByteBuffer.wrap(deCompressBytes(in.array(), inPosition + in.arrayOffset(), inRemain, doFlush));
    } else {
      throw new UnsupportedOperationException("Not supported ByteBuffer implementation. Use either direct BB or wrapped byte arrays. You may raise an issue on Github too ;-)");
    }
    in.position(inLimit);
//    return out;
    return 0;
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    close();
  }

  @Override
  public void close() throws BrotliException {
    assertBrotliOk(freeNativeResources());
  }

  private native static int initJavaFieldIdCache();

  private native int initBrotliDeCompressor();

  private native int freeNativeResources();

  private native int deCompressBytes(byte[] inArray, int inPosition, int inLength, byte[] outArray, int outPosition, int outLength);

  private native int deCompressByteBuffer(ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer, int outPosition, int outLength);

}
