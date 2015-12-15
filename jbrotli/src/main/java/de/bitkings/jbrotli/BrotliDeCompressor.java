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
    return assertBrotliOk(deCompressBytes(in, inPosition, inLength, out));
  }

  /**
   * @param in  compressed input
   * @param out output buffer
   * @return output buffer length
   * @throws BrotliException
   */
  public final int deCompress(ByteBuffer in, ByteBuffer out) throws BrotliException {
    return deCompress(in, in.limit(), out);
  }

  /**
   * @param in         compressed input
   * @param inLength   input length
   * @param out        output buffer
   * @return output buffer length
   * @throws BrotliException
   */
  public final int deCompress(ByteBuffer in, int inLength, ByteBuffer out) throws BrotliException {
    int pos = in.position();
    int limit = in.limit();
    assert (pos <= limit);
    int rem = limit - pos;
    int outLength;
    if (rem <= 0)
      return -1;
    if (in.isDirect() && out.isDirect()) {
      outLength = assertBrotliOk(deCompressByteBuffer(in, in.position(), inLength, out));
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
    in.position(limit);
    return outLength;
  }

  private native static int deCompressBytes(byte[] in, int inPosition, int inLength, byte[] out);

  private native static int deCompressByteBuffer(ByteBuffer inBuf, int inPosition, int inLength, ByteBuffer outBuf);

}
