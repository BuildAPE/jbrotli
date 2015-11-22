package de.bitkings.jbrotli;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliExampleTest {

  /**
   * intended to be used in README ... to make sure it will compile ;-)
   * @throws Exception
   */
  @Test
  public void compress_with_byte_array() throws Exception {

    System.loadLibrary("brotli");

    byte[] inBuf = "Brotli: a new compression algorithm for the internet. Now available for Java!".getBytes("ASCII");
    byte[] compressedBuf = new byte[2048];
    BrotliCompressor compressor = new BrotliCompressor();
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuf, compressedBuf);

  }

}