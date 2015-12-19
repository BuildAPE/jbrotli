package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.Test;

public class BrotliExampleTest {

  /**
   * intended to be used in README ... to make sure it will compile ;-)
   *
   * @throws Exception
   */
  @Test
  public void compress_with_byte_array() throws Exception {

    NativeLoader.loadLibrary("brotli");

    byte[] inBuf = "Brotli: a new compression algorithm for the internet. Now available for Java!".getBytes();
    byte[] compressedBuf = new byte[2048];
    BrotliCompressor compressor = new BrotliCompressor();
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuf, compressedBuf);

  }

  /**
   * intended to be used in README ... to make sure it will compile ;-)
   *
   * @throws Exception
   */
  @Test
  public void compress_with_stream_compressor() throws Exception {

    NativeLoader.loadLibrary("brotli");

    byte[] inBuf = "Brotli: a new compression algorithm for the internet. Now available for Java!".getBytes();
    boolean doFlush = true;
    BrotliStreamCompressor streamCompressor = new BrotliStreamCompressor(Brotli.DEFAULT_PARAMETER);
    byte[] compressed = streamCompressor.compress(inBuf, doFlush);
  }

}