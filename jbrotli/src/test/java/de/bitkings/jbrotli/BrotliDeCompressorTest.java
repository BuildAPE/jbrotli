package de.bitkings.jbrotli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static de.bitkings.jbrotli.BrotliCompressorTest.A_BYTES;
import static de.bitkings.jbrotli.BrotliCompressorTest.A_BYTES_COMPRESSED;
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
    byte[] in = A_BYTES_COMPRESSED;
    byte[] out = new byte[100];

    int outLen = decompressor.deCompress(in, out);

    assertThat(outLen).isEqualTo(A_BYTES.length);
    assertThat(out).startsWith(A_BYTES);
  }

  @Test
  public void decompress_with_byte_array_using_position_and_length() throws Exception {
    // setup
    byte[] in = new byte[100];
    Arrays.fill(in, (byte) 'x');
    byte[] out = new byte[100];

    // when
    int testPosition = 23;
    int testLength = A_BYTES_COMPRESSED.length;
    System.arraycopy(A_BYTES_COMPRESSED, 0, in, testPosition, testLength);

    int outLen = decompressor.deCompress(in, testPosition, testLength, out);

    assertThat(outLen).isEqualTo(A_BYTES.length);
    assertThat(out).startsWith(A_BYTES);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {
    byte[] in = A_BYTES_COMPRESSED;
    byte[] out = new byte[2048];

    decompressor.deCompress(in, -1, in.length, out);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {
    byte[] in = A_BYTES_COMPRESSED;
    byte[] out = new byte[2048];

    decompressor.deCompress(in, 0, -1, out);

    // expect exception
  }

  @Test
  public void decompress_with_ByteBuffer() throws Exception {

    ByteBuffer inBuf = ByteBuffer.allocateDirect(A_BYTES_COMPRESSED.length);
    inBuf.put(A_BYTES_COMPRESSED);
    inBuf.position(0);

    byte[] out = new byte[100];
    ByteBuffer outBuf = ByteBuffer.allocateDirect(out.length);

    int outLen = decompressor.deCompress(inBuf, outBuf);

    assertThat(outLen).isEqualTo(A_BYTES.length);

    outBuf.get(out);
    assertThat(out).startsWith(A_BYTES);
  }

  @Test
  public void decompress_with_ByteBuffer_using_position_and_length() throws Exception {
    // setup
    byte[] tmpXXX = new byte[100];
    Arrays.fill(tmpXXX, (byte) 'x');
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(tmpXXX.length);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(100);
    inBuffer.put(tmpXXX);

    // given
    int testPosition = 23;
    int testLength = A_BYTES_COMPRESSED.length;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES_COMPRESSED);
    inBuffer.position(testPosition);

    // when
    int outLen = decompressor.deCompress(inBuffer, testPosition, testLength, outBuffer);

    // then
    assertThat(outLen).isEqualTo(A_BYTES.length);
    byte[] buf = new byte[A_BYTES.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES);
  }
}