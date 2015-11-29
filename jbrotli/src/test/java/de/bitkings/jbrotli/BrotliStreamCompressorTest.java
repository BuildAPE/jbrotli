package de.bitkings.jbrotli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static de.bitkings.jbrotli.BrotliCompressorTest.A_BYTES;
import static de.bitkings.jbrotli.BrotliCompressorTest.A_BYTES_COMPRESSED;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamCompressorTest {

  private BrotliStreamCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    System.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliStreamCompressor();
    compressor.init(Brotli.DEFAULT_PARAMETER);
    System.out.println(compressor);
  }

  @Test
  public void compress_with_byte_array() throws Exception {
    byte[] out = new byte[100];
    int outLength = compressor.compress(A_BYTES, out);

    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(A_BYTES_COMPRESSED);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(A_BYTES, -1, 0, out);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(A_BYTES, 0, -1, out);

    // expect exception
  }

  @Test
  public void compress_with_byte_array_using_position_and_length() throws Exception {
    // setup
    byte[] in = new byte[100];
    Arrays.fill(in, (byte) 'x');
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
}