package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliCompressorTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamCompressorInitAndCloseTest {

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

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The source position \\+ length must me smaller then the source byte array's length.")
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {

    compressor.compress(A_BYTES, -1, 0, true);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The source position \\+ length must me smaller then the source byte array's length.")
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {

    compressor.compress(A_BYTES, 0, -1, true);

    // expect exception
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