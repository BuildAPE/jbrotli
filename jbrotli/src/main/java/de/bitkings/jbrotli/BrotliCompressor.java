package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliErrorChecker.assertBrotliOk;

public final class BrotliCompressor {

  /**
   * @param parameter parameter
   * @param in        input
   * @param out       output
   * @return output buffer length
   * @throws BrotliException
   */
  public final int compress(Brotli.Parameter parameter, byte[] in, byte[] out) throws BrotliException {
    return compress(parameter, in, 0, in.length, out);
  }

  /**
   * @param parameter  parameter
   * @param in         input
   * @param inPosition input position
   * @param inLength   input length
   * @param out        output
   * @return output buffer length
   * @throws BrotliException
   */
  public final int compress(Brotli.Parameter parameter, byte[] in, int inPosition, int inLength, byte[] out) throws BrotliException {
    if (inPosition + inLength > in.length) {
      throw new IllegalArgumentException("The source position and length must me smaller then the source byte array's length.");
    }
    return assertBrotliOk(compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, inPosition, inLength, out, out.length));
  }

  /**
   * One may use {@link ByteBuffer#position(int)} and {@link ByteBuffer#limit(int)} to adjust
   * how the buffers are used for reading and writing.
   *
   * @param parameter parameter
   * @param in        input
   * @param out       output
   * @return output buffer length
   * @throws BrotliException
   */
  public final int compress(Brotli.Parameter parameter, ByteBuffer in, ByteBuffer out) throws BrotliException {
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

    int computedOutLength;
    if (in.isDirect() && out.isDirect()) {
      computedOutLength = assertBrotliOk(compressByteBuffer(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, inPosition, inRemain, out, outPosition, outRemain));
    } else if (in.hasArray() && out.hasArray()) {
//      computedOutLength = assertBrotliOk(compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in.array(), inPosition + in.arrayOffset(), inRemain, out.array(), outRemain));
//      out.limit(inPosition + computedOutLength);
      throw new UnsupportedOperationException("Not yet implemented");
    } else {
//      byte[] b = new byte[inRemain];
//      in.get(b);
//      computedOutLength = assertBrotliOk(compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), b, 0, b.length, out.array(), outRemain));
      throw new UnsupportedOperationException("Not yet implemented");
    }
    in.position(inLimit);
    out.limit(outPosition + computedOutLength);
    return computedOutLength;
  }

  private native static int compressBytes(int mode, int quality, int lgwin, int lgblock, byte[] inArray, int inPosition, int inLength, byte[] outArray, int outLength);

  private native static int compressByteBuffer(int mode, int quality, int lgwin, int lgblock, ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer, int outPosition, int outLength);

}
