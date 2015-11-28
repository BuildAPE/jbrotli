package de.bitkings.jbrotli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamCompressorTest {

  static final byte[] A_BUFFER = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();

  private BrotliStreamCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    System.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliStreamCompressor();
    compressor.init(Brotli.DEFAULT_PARAMETER);
  }

  @Test
  public void compress_with_byte_array() throws Exception {
    byte[] out = new byte[100];
    int outLength = compressor.compress(A_BUFFER, out);

    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0});
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(A_BUFFER, -1, 0, out);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(A_BUFFER, 0, -1, out);

    // expect exception
  }
}