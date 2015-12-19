package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliCompressorTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamCompressorTest {

  private BrotliStreamCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliStreamCompressor(Brotli.DEFAULT_PARAMETER);
  }

  @Test
  public void max_input_buffer_size_can_be_retrieved() throws Exception {
    Brotli.Parameter parameter = Brotli.DEFAULT_PARAMETER;
    parameter.setLgblock(16);

    compressor = new BrotliStreamCompressor(parameter);

    int maxInputBufferSize = compressor.getMaxInputBufferSize();

    int computed_input_block_size_as_in_botli_encode_header_file = 1 << 16;
    assertThat(maxInputBufferSize).isEqualTo(computed_input_block_size_as_in_botli_encode_header_file);
  }

  @Test
  public void compress_with_byte_array() throws Exception {
    byte[] out = new byte[100];
    int outLength = compressor.compress(A_BYTES, out);

    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(A_BYTES_COMPRESSED);
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The source position \\+ length must me smaller then the source byte array's length.")
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(A_BYTES, -1, 0, out);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The source position \\+ length must me smaller then the source byte array's length.")
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(A_BYTES, 0, -1, out);

    // expect exception
  }

  @Test
  public void compress_with_byte_array_using_position_and_length() throws Exception {
    // setup
    byte[] in = createFilledByteArray(100, 'x');
    byte[] out = new byte[2048];

    // given
    int testPosition = 23;
    int testLength = A_BYTES.length;
    System.arraycopy(A_BYTES, 0, in, testPosition, testLength);

    // when
    int outLength = compressor.compress(in, testPosition, testLength, out);

    // then
    assertThat(outLength).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(out).startsWith(A_BYTES_COMPRESSED);
  }

  //
  // *** ByteBuffer **********

  @Test
  public void compress_with_ByteBuffer_and_flushing() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(A_BYTES.length);
    inBuffer.put(A_BYTES);
    inBuffer.position(0);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_ByteBuffer_without_flushing() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(A_BYTES.length);
    inBuffer.put(A_BYTES);
    inBuffer.position(0);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, false);
    // then
    assertThat(outBuffer.capacity()).isEqualTo(0);

    // when
    outBuffer = compressor.compress(ByteBuffer.allocateDirect(0), true);
    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);

    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_ByteBuffer_using_position_and_length() throws Exception {
    // setup
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(100);
    inBuffer.put(createFilledByteArray(100, 'x'));

    // given
    int testPosition = 23;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES);
    inBuffer.position(testPosition);
    inBuffer.limit(testPosition + A_BYTES.length);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.limit()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.position()).isEqualTo(0);
    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test(expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "^BrotliStreamCompressor was already closed.*")
  public void auto_close_frees_resources() throws Exception {
    // given
    BrotliStreamCompressor brotliStreamCompressor = new BrotliStreamCompressor();

    // when
    brotliStreamCompressor.close();

    // then exception
    brotliStreamCompressor.getMaxInputBufferSize();
  }

  @Test
  public void close_is_idempotent() throws Exception {
    // given
    BrotliStreamCompressor brotliStreamCompressor = new BrotliStreamCompressor();

    // when
    brotliStreamCompressor.close();
    brotliStreamCompressor.close();
    brotliStreamCompressor.close();

    // no exception ;-)
  }
}