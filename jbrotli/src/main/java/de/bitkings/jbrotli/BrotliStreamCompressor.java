package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

public final class BrotliStreamCompressor {

  private long brotliCompressorAddr;

  public final void init(Brotli.Parameter parameter) {
    initBrotliCompressor(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock());
  }

  public final int compress(Brotli.Parameter parameter, byte[] in, byte[] out) {
    return compress(parameter, in, 0, in.length, out);
  }

  public final int compress(Brotli.Parameter parameter, byte[] in, int inPosition, int inLength, byte[] out) {
    return compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, inPosition, inLength, out);
  }

  public final int compress(Brotli.Parameter parameter, ByteBuffer in, ByteBuffer out) {
    throw new UnsupportedOperationException("not yet implemented");
  }

  public final int compress(Brotli.Parameter parameter, ByteBuffer in, int inPosition, int inLength, ByteBuffer out) {
    throw new UnsupportedOperationException("not yet implemented");
  }

  private native int initBrotliCompressor(int mode, int quality, int lgwin, int lgblock);

  private native int compressBytes(int mode, int quality, int lgwin, int lgblock, byte[] inArray, int inPosition, int inLength, byte[] outArray);

  private native int compressByteBuffer(int mode, int quality, int lgwin, int lgblock, ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer);

}
