package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliErrorChecker.assertBrotliOk;

public final class BrotliDeCompressor {

  /**
   * @param in  compressed input
   * @param out output buffer
   * @return output buffer length
   * @throws BrotliException
   */
  public final int deCompress(byte[] in, byte[] out) throws BrotliException {
    return deCompress(in, 0, in.length, out);
  }

  /**
   * @param in         compressed input
   * @param inPosition input position
   * @param inLength   input length
   * @param out        output buffer
   * @return output buffer length
   * @throws BrotliException
   */
  public final int deCompress(byte[] in, int inPosition, int inLength, byte[] out) throws BrotliException {
    if (inPosition + inLength > in.length) {
      throw new IllegalArgumentException("The source position and length must me smaller then the source byte array's length.");
    }
    return assertBrotliOk(deCompressBytes(in, inPosition, inLength, out, out.length));
  }

  /**
   * One may use {@link ByteBuffer#position(int)} and {@link ByteBuffer#limit(int)} to adjust
   * how the buffers are used for reading and writing.
   *
   * @param in  compressed input
   * @param out output buffer
   * @return output buffer length
   * @throws BrotliException
   */
  public final int deCompress(ByteBuffer in, ByteBuffer out) throws BrotliException {
    int inPosition = in.position();
    int inLimit = in.limit();
    int inRemain = inLimit - inPosition;
    if (inRemain <= 0)
      throw new IllegalArgumentException("The source (in) position must me smaller then the source ByteBuffer's limit.");

    int outPosition = out.position();
    int outLimit = out.limit();
    int outRemain = outLimit - outPosition;
    if (outRemain <= 0)
      throw new IllegalArgumentException("The destination (out) position must me smaller then the source ByteBuffer's limit.");

    int outLength;
    if (in.isDirect() && out.isDirect()) {
      outLength = assertBrotliOk(deCompressByteBuffer(in, inPosition, inRemain, out, outPosition, outRemain));
    } else if (in.hasArray() && out.hasArray()) {
//      outLength = assertBrotliOk(deCompressBytes(in.array(), pos + in.arrayOffset(), rem, out.array()));
//      out.limit(pos + outLength);
      throw new UnsupportedOperationException("Not yet implemented");
    } else {
//      byte[] b = new byte[rem];
//      in.get(b);
//      outLength = assertBrotliOk(deCompressBytes(b, 0, b.length, out.array()));
      throw new UnsupportedOperationException("Not yet implemented");
    }
    in.position(inLimit);
    out.limit(outPosition + outLength);
    return outLength;
  }

  private native static int deCompressBytes(byte[] in, int inPosition, int inLength, byte[] out, int outLength);

  private native static int deCompressByteBuffer(ByteBuffer inBuf, int inPosition, int inLength, ByteBuffer outBuf, int outPosition, int outLength);

}
