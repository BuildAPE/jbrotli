package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliCompressorExtremeTest {

  private BrotliCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliCompressor();
  }

  @Test(expectedExceptions = BrotliException.class,
      expectedExceptionsMessageRegExp = ".*\\(Error code: -14\\)")
  public void compress_huge_buffer() throws Exception {
    byte[] inBuf = createArrayWithNumbers(100000);
    assertThat(inBuf.length).isEqualTo(488890);

    byte[] compressedBuf = new byte[2048];

    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuf, compressedBuf);

    // expected Exception, because output buffer too small
  }

  private byte[] createArrayWithNumbers(int numberCount) {
    StringBuilder bigData = new StringBuilder();
    for (int i = 0; i < numberCount; i++) {
      bigData.append(i);
    }
    return bigData.toString().getBytes();
  }

}
