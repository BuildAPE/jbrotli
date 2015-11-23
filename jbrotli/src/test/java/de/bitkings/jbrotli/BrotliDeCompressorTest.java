package de.bitkings.jbrotli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static de.bitkings.jbrotli.BrotliCompressorTest.A_BUFFER;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliDeCompressorTest {

  private BrotliDeCompressor decompressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    System.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    decompressor = new BrotliDeCompressor();
  }

  @Test
  public void decompress_with_byte_array() throws Exception {
    byte[] in = new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0};
    byte[] out = new byte[100];

    int outLen = decompressor.deCompress(in, out);

    assertThat(outLen).isEqualTo(A_BUFFER.length);
    assertThat(out).startsWith(A_BUFFER);
  }
}