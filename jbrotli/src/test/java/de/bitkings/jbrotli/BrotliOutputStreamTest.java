package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliOutputStreamTest {

  private byte[] testBytes;
  private BrotliOutputStream brotliOutputStream;
  private ByteArrayOutputStream baos;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    baos = new ByteArrayOutputStream();
    brotliOutputStream = new BrotliOutputStream(baos);
    createTestBytes();
  }

  @Test
  public void byte_wise_compression_works() throws Exception {

    // when
    for (int i : testBytes) {
      brotliOutputStream.write(i);
    }
    brotliOutputStream.flush();

    // then
    assertThat(decompress(baos.toByteArray(), testBytes.length)).isEqualTo(testBytes);
  }

  @Test
  public void byte_array_wise_compression_works() throws Exception {

    // when
    brotliOutputStream.write(testBytes);
    brotliOutputStream.flush();

    // then
    assertThat(decompress(baos.toByteArray(), testBytes.length)).isEqualTo(testBytes);
  }

  @Test
  public void byte_array_length_and_offset_wise_compression_works() throws Exception {

    // when
    brotliOutputStream.write(testBytes, 10, 100);
    brotliOutputStream.flush();

    // then
    byte[] decompressed = decompress(baos.toByteArray(), testBytes.length);
    for (int i = 10; i < 100; i++)
      assertThat(decompressed[i-10]).describedAs("Byte at offset=" + i).isEqualTo(testBytes[i]);
  }

  private byte[] decompress(byte[] compressed, int upackedLength) {
    BrotliDeCompressor brotliDeCompressor = new BrotliDeCompressor();
    byte[] decompressed = new byte[upackedLength];
    brotliDeCompressor.deCompress(compressed, decompressed);
    return decompressed;
  }

  private void createTestBytes() {
    testBytes = new byte[256];
    int idx = 0;
    for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
      testBytes[idx++] = b;
    }
  }
}