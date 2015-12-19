package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliCompressorTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamCompressorByteArrayTest {

  private BrotliStreamCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliStreamCompressor(Brotli.DEFAULT_PARAMETER);
  }

  @Test
  public void compress_with_byte_array_and_flushing() throws Exception {
    byte[] out = compressor.compress(A_BYTES, true);

    assertThat(out).hasSize(10);
    assertThat(out).isEqualTo(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_byte_array_without_flushing() throws Exception {

    // when
    byte[] out = compressor.compress(A_BYTES, false);

    // then
    assertThat(out).hasSize(0);

    // when
    out = compressor.compress(new byte[0], true);

    // then
    assertThat(out).isEqualTo(A_BYTES_COMPRESSED);
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The source position \\+ length must me smaller then the source byte array's length.")
  public void using_negative_position_throws_IllegalArgumentException() throws Exception {

    compressor.compress(A_BYTES, -1, 0, true);

    // expect exception
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The source position \\+ length must me smaller then the source byte array's length.")
  public void using_negative_length_throws_IllegalArgumentException() throws Exception {

    compressor.compress(A_BYTES, 0, -1, true);

    // expect exception
  }

  @Test
  public void compress_with_byte_array_using_position_and_length() throws Exception {
    // setup
    byte[] in = createFilledByteArray(100, 'x');

    // given
    int testPosition = 23;
    int testLength = A_BYTES.length;
    System.arraycopy(A_BYTES, 0, in, testPosition, testLength);

    // when
    byte[] out = compressor.compress(in, testPosition, testLength, true);

    // then
    assertThat(out).isEqualTo(A_BYTES_COMPRESSED);
  }

}