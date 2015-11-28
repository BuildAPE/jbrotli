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
    return assertBrotliOk(compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, inPosition, inLength, out));
  }

  /**
   * @param parameter parameter
   * @param in        input
   * @param out       output
   * @return output buffer length
   * @throws BrotliException
   */
  public final int compress(Brotli.Parameter parameter, ByteBuffer in, ByteBuffer out) throws BrotliException {
    return compress(parameter, in, 0, in.limit(), out);
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
  public final int compress(Brotli.Parameter parameter, ByteBuffer in, int inPosition, int inLength, ByteBuffer out) throws BrotliException {
    int pos = in.position();
    int limit = in.limit();
    assert (pos <= limit);
    int rem = limit - pos;
    int outLength;
    if (rem <= 0)
      throw new IllegalArgumentException("The source position and length must me smaller then the source ByteBuffer's length.");
    if (in.isDirect() && out.isDirect()) {
      outLength = assertBrotliOk(compressByteBuffer(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, in.position(), inLength, out));
    } else if (in.hasArray() && out.hasArray()) {
      outLength = assertBrotliOk(compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in.array(), pos + in.arrayOffset(), rem, out.array()));
      out.limit(pos + outLength);
    } else {
      byte[] b = new byte[rem];
      in.get(b);
      outLength = assertBrotliOk(compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), b, 0, b.length, out.array()));
    }
    in.position(limit);
    return outLength;
  }

  private native static int compressBytes(int mode, int quality, int lgwin, int lgblock, byte[] inArray, int inPosition, int inLength, byte[] outArray);

  private native static int compressByteBuffer(int mode, int quality, int lgwin, int lgblock, ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer);

}
