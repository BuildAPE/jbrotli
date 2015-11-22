package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

public final class BrotliCompressor {

  public final int compress(Brotli.Parameter parameter, byte[] in, byte[] out) {
    return compress(parameter, in, 0, in.length, out);
  }

  public final int compress(Brotli.Parameter parameter, byte[] in, int inPosition, int inLength, byte[] out) {
    return compressBytes(parameter, in, inPosition, inLength, out);
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
      outLength = compressByteBuffer(parameter, in, in.position(), inLength, out);
    } else if (in.hasArray() && out.hasArray()) {
      outLength = compressBytes(parameter, in.array(), pos + in.arrayOffset(), rem, out.array());
      out.limit(pos + outLength);
    } else {
      byte[] b = new byte[rem];
      in.get(b);
      outLength = compressBytes(parameter, b, 0, b.length, out.array());
    }
    in.position(limit);
    return outLength;
  }

  private native static int compressBytes(Brotli.Parameter parameter, byte[] in, int inPosition, int inLength, byte[] out);

  private native static int compressByteBuffer(Brotli.Parameter parameter, ByteBuffer inBuf, int inPosition, int inLength, ByteBuffer outBuf);

}
