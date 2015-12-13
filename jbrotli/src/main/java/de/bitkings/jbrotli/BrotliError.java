package de.bitkings.jbrotli;

public class BrotliError {

  public static final int NATIVE_ERROR = -1;

  public static final int COMPRESS_GetPrimitiveArrayCritical_INBUF = -10;
  public static final int COMPRESS_GetPrimitiveArrayCritical_OUTBUF = -11;
  public static final int COMPRESS_ReleasePrimitiveArrayCritical_OUTBUF = -12;
  public static final int COMPRESS_ReleasePrimitiveArrayCritical_INBUF = -13;
  public static final int COMPRESS_BrotliCompressBuffer = -14;

  public static final int COMPRESS_ByteBuffer_GetDirectBufferAddress_INBUF = -20;
  public static final int COMPRESS_ByteBuffer_GetDirectBufferAddress_OUTBUF = -21;
  public static final int COMPRESS_ByteBuffer_BrotliCompressBuffer = -22;

  public static final int DECOMPRESS_GetPrimitiveArrayCritical_INBUF = -30;
  public static final int DECOMPRESS_GetPrimitiveArrayCritical_OUTBUF = -31;
  public static final int DECOMPRESS_BROTLI_RESULT_ERROR = -32;
  public static final int DECOMPRESS_BROTLI_RESULT_NEEDS_MORE_INPUT = -33;
  public static final int DECOMPRESS_BROTLI_RESULT_NEEDS_MORE_OUTPUT = -34;
  public static final int DECOMPRESS_ReleasePrimitiveArrayCritical_OUTBUF = -35;
  public static final int DECOMPRESS_ReleasePrimitiveArrayCritical_INBUF = -36;

  public static final int DECOMPRESS_ByteBuffer_GetDirectBufferAddress_INBUF = -40;
  public static final int DECOMPRESS_ByteBuffer_GetDirectBufferAddress_OUTBUF = -41;
  public static final int DECOMPRESS_ByteBuffer_BROTLI_RESULT_ERROR = -42;
  public static final int DECOMPRESS_ByteBuffer_BROTLI_RESULT_NEEDS_MORE_INPUT = -43;
  public static final int DECOMPRESS_ByteBuffer_BROTLI_RESULT_NEEDS_MORE_OUTPUT = -44;

  public static final int STREAM_COMPRESS_INIT_BrotliCompressor = -50;

  public static final int STREAM_COMPRESS_GetPrimitiveArrayCritical_INBUF = -60;
  public static final int STREAM_COMPRESS_GetPrimitiveArrayCritical_OUTBUF = -61;
  public static final int STREAM_COMPRESS_ReleasePrimitiveArrayCritical_OUTBUF = -63;
  public static final int STREAM_COMPRESS_ReleasePrimitiveArrayCritical_INBUF = -64;
  public static final int STREAM_COMPRESS_WriteBrotliData = -62;

  public static final int STREAM_COMPRESS_ByteBuffer_GetDirectBufferAddress_INBUF = -70;
  public static final int STREAM_COMPRESS_ByteBuffer_GetDirectBufferAddress_OUTBUF = -71;
  public static final int STREAM_COMPRESS_ByteBuffer_WriteBrotliData = -72;

}
