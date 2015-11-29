package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliErrorChecker.assertBrotliOk;

public final class BrotliStreamCompressor {

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
    throw new UnsupportedOperationException("not yet implemented");
  }

  public final int compress(ByteBuffer in, int inLength, ByteBuffer out) {
    throw new UnsupportedOperationException("not yet implemented");
  }

  static {
    initIDs();
  }

  private native static void initIDs();

  private native int initBrotliCompressor(int mode, int quality, int lgwin, int lgblock);

  private native int compressBytes(byte[] inArray, int inPosition, int inLength, byte[] outArray);

  private native int compressByteBuffer(ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer);

}
