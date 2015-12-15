package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

    StringBuilder bigData = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      bigData.append(i);
    }
    byte[] inBuf = bigData.toString().getBytes();
    System.out.println(inBuf.length);
    byte[] compressedBuf = new byte[2048];

    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuf, compressedBuf);

    // expected Exception, because output buffer too small
  }

}