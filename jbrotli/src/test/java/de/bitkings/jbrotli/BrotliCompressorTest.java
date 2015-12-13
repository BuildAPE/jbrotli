package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliCompressorTest {

  static final byte[] A_BYTES = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();
  static final byte[] A_BYTES_COMPRESSED = new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0};

  private BrotliCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliCompressor();
  }

  @Test
  public void compress_with_byte_array() throws Exception {
    byte[] out = new byte[2048];
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, A_BYTES, 0, A_BYTES.length, out);

    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(A_BYTES_COMPRESSED);
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
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, in, testPosition, testLength, out);

    // then
    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(A_BYTES_COMPRESSED);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void compress_with_byte_array_using_negative_position_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(Brotli.DEFAULT_PARAMETER, A_BYTES, -1, A_BYTES.length, out);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Brotli: input array position and length must be greater than zero.")
  public void compress_with_byte_array_using_negative_length_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(Brotli.DEFAULT_PARAMETER, A_BYTES, 0, -1, out);

    // expect exception
  }

  @Test
  public void compress_with_ByteBuffer() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(A_BYTES.length);
    inBuffer.put(A_BYTES);
    inBuffer.position(0);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(10);
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuffer, outBuffer);

    assertThat(outLength).isEqualTo(10);
    byte[] buf = new byte[10];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Brotli: input ByteBuffer position and length must be greater than zero.")
  public void compress_with_ByteBuffer_using_negative_length_throws_IllegalArgumentException() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(A_BYTES.length);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(A_BYTES_COMPRESSED.length);

    compressor.compress(Brotli.DEFAULT_PARAMETER, inBuffer, -1, outBuffer);

    // expect exception
  }

  @Test
  public void compress_with_ByteBuffer_using_position_and_length() throws Exception {
    // setup
    byte[] tmpXXX = new byte[100];
    Arrays.fill(tmpXXX, (byte) 'x');
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(tmpXXX.length);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(A_BYTES_COMPRESSED.length);
    inBuffer.put(tmpXXX);

    // given
    int testPosition = 23;
    int testLength = A_BYTES.length;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES);
    inBuffer.position(testPosition);

    // when
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuffer, testLength, outBuffer);

    // then
    assertThat(outLength).isEqualTo(A_BYTES_COMPRESSED.length);
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }
}