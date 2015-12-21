package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static de.bitkings.jbrotli.BrotliCompressorTest.A_BYTES;
import static de.bitkings.jbrotli.BrotliCompressorTest.A_BYTES_COMPRESSED;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamDeCompressorInitAndCloseTest {
  private BrotliStreamDeCompressor decompressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    decompressor = new BrotliStreamDeCompressor();
  }

  @Test
  public void happy_path_decompress_a_buffer_completly() throws Exception {
    byte out[] = new byte[A_BYTES.length];

    int outLength = decompressor.deCompress(A_BYTES_COMPRESSED, out);

    assertThat(outLength).isEqualTo(A_BYTES.length);
    assertThat(out).isEqualTo(A_BYTES);
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Brotli: input array position and length must be greater than zero.")
  public void using_negative_position_on_input_throws_IllegalArgumentException() throws Exception {

    decompressor.deCompress(A_BYTES_COMPRESSED, -1, 0, new byte[100], 0, 100);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Brotli: output array position and length must be greater than zero.")
  public void using_negative_position_on_output_throws_IllegalArgumentException() throws Exception {

    decompressor.deCompress(A_BYTES_COMPRESSED, 0, 10, new byte[100], -1, 100);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Brotli: input array position and length must be greater than zero.")
  public void using_negative_length_on_input_throws_IllegalArgumentException() throws Exception {

    decompressor.deCompress(A_BYTES_COMPRESSED, 0, -1, new byte[100], 0, 100);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Brotli: output array position and length must be greater than zero.")
  public void using_negative_length_on_output_throws_IllegalArgumentException() throws Exception {

    decompressor.deCompress(A_BYTES_COMPRESSED, 0, 10, new byte[100], 0, -1);

    // expect exception
  }

  @Test(expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "^BrotliStreamDeCompressor wasn't initialised.*")
  public void auto_close_frees_resources() throws Exception {
    // given
    BrotliStreamDeCompressor brotliStreamDeCompressor = new BrotliStreamDeCompressor();

    // when
    brotliStreamDeCompressor.close();

    // then exception
    brotliStreamDeCompressor.deCompress(A_BYTES_COMPRESSED, new byte[100]);
  }

  @Test
  public void close_is_idempotent() throws Exception {
    // given
    BrotliStreamDeCompressor brotliStreamDeCompressor = new BrotliStreamDeCompressor();

    // when
    brotliStreamDeCompressor.close();
    brotliStreamDeCompressor.close();
    brotliStreamDeCompressor.close();

    // no exception ;-)
  }
}