package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliCompressorTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliDeCompressorTest {

  private BrotliDeCompressor decompressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
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
    byte[] in = createFilledByteArray(100, 'x');
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

    ByteBuffer outBuf = ByteBuffer.allocateDirect(100);

    // when
    int outLen = decompressor.deCompress(inBuf, outBuf);

    // then
    assertThat(outLen).isEqualTo(A_BYTES.length);
    // then
    byte[] out = new byte[A_BYTES.length];
    outBuf.get(out);
    assertThat(out).isEqualTo(A_BYTES);
  }

  @Test
  public void decompress_with_ByteBuffer_using_position_and_length_on_input() throws Exception {
    // setup
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(100);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(100);
    inBuffer.put(createFilledByteArray(100, 'x'));

    // given
    int testPosition = 23;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES_COMPRESSED);
    inBuffer.position(testPosition);
    inBuffer.limit(testPosition + A_BYTES_COMPRESSED.length);

    // when
    int outLen = decompressor.deCompress(inBuffer, outBuffer);

    // then
    assertThat(outLen).isEqualTo(A_BYTES.length);
    assertThat(outBuffer.position()).isEqualTo(0);
    assertThat(outBuffer.limit()).isEqualTo(outLen);
    assertThat(inBuffer.position()).isEqualTo(testPosition + A_BYTES_COMPRESSED.length);
    // then
    byte[] buf = new byte[A_BYTES.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES);
  }

  @Test
  public void decompress_with_ByteBuffer_using_position_and_length_on_output() throws Exception {
    // setup
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(100);
    ByteBuffer outBuffer = ByteBuffer.allocateDirect(100);
    inBuffer.put(A_BYTES_COMPRESSED);
    inBuffer.limit(A_BYTES_COMPRESSED.length);
    inBuffer.position(0);

    // given
    int testPosition = 23;
    outBuffer.position(testPosition);
    outBuffer.limit(testPosition + A_BYTES.length);

    // when
    int outLen = decompressor.deCompress(inBuffer, outBuffer);

    // then
    assertThat(outLen).isEqualTo(A_BYTES.length);
    assertThat(outBuffer.position()).isEqualTo(testPosition);
    assertThat(outBuffer.limit()).isEqualTo(testPosition + outLen);
    assertThat(inBuffer.position()).isEqualTo(A_BYTES_COMPRESSED.length);
    // then
    byte[] buf = new byte[A_BYTES.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES);
  }
}