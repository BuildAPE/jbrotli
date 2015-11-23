package de.bitkings.jbrotli;

import java.nio.ByteBuffer;

public final class BrotliDeCompressor {

  public final int deCompress(byte[] in, byte[] out) throws BrotliDecompressException {
    return deCompress(in, 0, in.length, out);
  }

  public final int deCompress(byte[] in, int inPosition, int inLength, byte[] out) throws BrotliDecompressException {
    return checkDecompressionResult(deCompressBytes(in, inPosition, inLength, out));
  }

  public final int deCompress(ByteBuffer in, ByteBuffer out) throws BrotliDecompressException {
    return deCompress(in, 0, in.limit(), out);
  }

  public final int deCompress(ByteBuffer in, int inPosition, int inLength, ByteBuffer out) throws BrotliDecompressException {
    int pos = in.position();
    int limit = in.limit();
    assert (pos <= limit);
    int rem = limit - pos;
    int outLength;
    if (rem <= 0)
      return -1;
    if (in.isDirect() && out.isDirect()) {
      outLength = deCompressByteBuffer(in, in.position(), inLength, out);
    } else if (in.hasArray() && out.hasArray()) {
      outLength = checkDecompressionResult(deCompressBytes(in.array(), pos + in.arrayOffset(), rem, out.array()));
      out.limit(pos + outLength);
    } else {
      byte[] b = new byte[rem];
      in.get(b);
      outLength = checkDecompressionResult(deCompressBytes(b, 0, b.length, out.array()));
    }
    in.position(limit);
    return outLength;
  }

  private int checkDecompressionResult(int brotliResult) {
    if (brotliResult == -1) throw new BrotliDecompressException("An error happened inside JNI call. Maybe OOME or other issues.");
    if (brotliResult == -10) throw new BrotliDecompressException("Decoding error, e.g. corrupt input or no memory.");
    if (brotliResult == -12)
      throw new BrotliDecompressException("Partially done, but must be called again with more input.");
    if (brotliResult == -13)
      throw new BrotliDecompressException("Partially done, but must be called again with more output.");
    return brotliResult;
  }

  private native static int deCompressBytes(byte[] in, int inPosition, int inLength, byte[] out);

  private native static int deCompressByteBuffer(ByteBuffer inBuf, int inPosition, int inLength, ByteBuffer outBuf);

}
