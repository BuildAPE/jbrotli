package de.bitkings.jbrotli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BrotliCompressorTest {

  static final byte[] A_BUFFER = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();

  private BrotliCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    System.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliCompressor();
  }

  @Test
  public void compress_with_byte_array() throws Exception {
    byte[] out = new byte[2048];
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, A_BUFFER, 0, A_BUFFER.length, out);

    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0});
  }

  @Test
  public void compress_with_byte_array_using_position_and_length() throws Exception {
    // setup
    byte[] in = new byte[100];
    Arrays.fill(in, (byte) 'x');
    byte[] out = new byte[2048];

    // given
    int testPosition = 23;
    int testLength = A_BUFFER.length;
    System.arraycopy(A_BUFFER, 0, in, testPosition, testLength);

    // when
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, in, testPosition, testLength, out);

    // then
    assertThat(outLength).isEqualTo(10);
    assertThat(out).startsWith(new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0});
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(Brotli.DEFAULT_PARAMETER, A_BUFFER, -1, A_BUFFER.length, out);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {
    byte[] out = new byte[2048];

    compressor.compress(Brotli.DEFAULT_PARAMETER, A_BUFFER, 0, -1, out);

    // expect exception
  }

  @Test
  public void compress_with_ByteBuffer() throws Exception {
    ByteBuffer aDirectBuffer = ByteBuffer.allocateDirect(A_BUFFER.length);
    aDirectBuffer.put(A_BUFFER);
    aDirectBuffer.position(0);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(10);
    int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, aDirectBuffer, outBuffer);

    assertThat(outLength).isEqualTo(10);
    byte[] buf = new byte[10];
    outBuffer.get(buf);
    assertThat(buf).startsWith(new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0});
  }
}