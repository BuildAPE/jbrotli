package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliErrorChecker.assertBrotliOk;

public final class BrotliStreamCompressor {

  static {
    initIDs();
  }

  private final long brotliCompressorAddr = 0;

  public final void init(Brotli.Parameter parameter) {
    assertBrotliOk(initBrotliCompressor(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock()));
  }

  public final int compress(byte[] in, byte[] out) {
    return compress(in, 0, in.length, out);
  }

  public final int compress(byte[] in, int inPosition, int inLength, byte[] out) {
    if (inPosition + inLength > in.length || inPosition < 0 || inLength < 0) {
      throw new IllegalArgumentException("The source position + length must me smaller then the source byte array's length.");
    }
    return assertBrotliOk(compressBytes(in, inPosition, inLength, out));
  }

  public final int compress(ByteBuffer in, ByteBuffer out) {
    return compress(in, in.limit(), out);
  }

  public final int compress(ByteBuffer in, int inLength, ByteBuffer out) {
    int pos = in.position();
    int limit = in.limit();
    assert (pos <= limit);
    int rem = limit - pos;
    int outLength;
    if (rem <= 0)
      throw new IllegalArgumentException("The source position and length must me smaller then the source ByteBuffer's length.");
    if (in.isDirect() && out.isDirect()) {
      outLength = assertBrotliOk(compressByteBuffer(in, in.position(), inLength, out));
    } else if (in.hasArray() && out.hasArray()) {
      outLength = assertBrotliOk(compressBytes(in.array(), pos + in.arrayOffset(), rem, out.array()));
      out.limit(pos + outLength);
    } else {
      byte[] b = new byte[rem];
      in.get(b);
      outLength = assertBrotliOk(compressBytes(b, 0, b.length, out.array()));
    }
    in.position(limit);
    return outLength;
  }

  private native static void initIDs();

  private native int initBrotliCompressor(int mode, int quality, int lgwin, int lgblock);

  private native int compressBytes(byte[] inArray, int inPosition, int inLength, byte[] outArray);

  private native int compressByteBuffer(ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer);

}
