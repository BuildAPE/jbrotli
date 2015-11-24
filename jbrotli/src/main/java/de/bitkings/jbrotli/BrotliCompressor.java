package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

public final class BrotliCompressor {

  public final int compress(Brotli.Parameter parameter, byte[] in, byte[] out) {
    return compress(parameter, in, 0, in.length, out);
  }

  public final int compress(Brotli.Parameter parameter, byte[] in, int inPosition, int inLength, byte[] out) {
    return compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, inPosition, inLength, out);
  }

  public final int compress(Brotli.Parameter parameter, ByteBuffer in, ByteBuffer out) {
    return compress(parameter, in, 0, in.limit(), out);
  }

  public final int compress(Brotli.Parameter parameter, ByteBuffer in, int inPosition, int inLength, ByteBuffer out) {
    int pos = in.position();
    int limit = in.limit();
    assert (pos <= limit);
    int rem = limit - pos;
    int outLength;
    if (rem <= 0)
      return -1;
    if (in.isDirect() && out.isDirect()) {
      outLength = compressByteBuffer(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in, in.position(), inLength, out);
    } else if (in.hasArray() && out.hasArray()) {
      outLength = compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), in.array(), pos + in.arrayOffset(), rem, out.array());
      out.limit(pos + outLength);
    } else {
      byte[] b = new byte[rem];
      in.get(b);
      outLength = compressBytes(parameter.getMode().mode, parameter.getQuality(), parameter.getLgwin(), parameter.getLgblock(), b, 0, b.length, out.array());
    }
    in.position(limit);
    return outLength;
  }

  private native static int compressBytes(int mode, int quality, int lgwin, int lgblock, byte[] inArray, int inPosition, int inLength, byte[] outArray);

  private native static int compressByteBuffer(int mode, int quality, int lgwin, int lgblock, ByteBuffer inByteBuffer, int inPosition, int inLength, ByteBuffer outByteBuffer);

}
