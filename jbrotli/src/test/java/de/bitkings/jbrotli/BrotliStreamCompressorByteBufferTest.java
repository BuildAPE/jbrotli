package de.bitkings.jbrotli;

import org.scijava.nativelib.NativeLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliCompressorTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliStreamCompressorByteBufferTest {

  private BrotliStreamCompressor compressor;

  @BeforeClass
  public void loadLibrary() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }

  @BeforeMethod
  public void setUp() throws Exception {
    compressor = new BrotliStreamCompressor(Brotli.DEFAULT_PARAMETER);
  }

  //
  // *** direct ByteBuffer **********

  @Test
  public void compress_with_direct_ByteBuffer_and_flushing() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(A_BYTES.length);
    inBuffer.put(A_BYTES);
    inBuffer.position(0);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_direct_ByteBuffer_without_flushing() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(A_BYTES.length);
    inBuffer.put(A_BYTES);
    inBuffer.position(0);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, false);
    // then
    assertThat(outBuffer.capacity()).isEqualTo(0);

    // when
    outBuffer = compressor.compress(ByteBuffer.allocateDirect(0), true);
    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);

    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_direct_ByteBuffer_using_position_and_length() throws Exception {
    // setup
    ByteBuffer inBuffer = ByteBuffer.allocateDirect(100);
    inBuffer.put(createFilledByteArray(100, 'x'));

    // given
    int testPosition = 23;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES);
    inBuffer.position(testPosition);
    inBuffer.limit(testPosition + A_BYTES.length);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.limit()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.position()).isEqualTo(0);
    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  //
  // *** array wrapped ByteBuffer **********

  @Test
  public void compress_with_array_wrapped_ByteBuffer_and_flushing() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.wrap(A_BYTES);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    byte[] buf = new byte[outBuffer.capacity()];
    outBuffer.get(buf);
    assertThat(buf).isEqualTo(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_array_wrapped_ByteBuffer_without_flushing() throws Exception {
    ByteBuffer inBuffer = ByteBuffer.wrap(A_BYTES);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, false);
    // then
    assertThat(outBuffer.capacity()).isEqualTo(0);

    // when
    outBuffer = compressor.compress(ByteBuffer.wrap(new byte[0]), true);
    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);

    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_array_wrapped_ByteBuffer_using_position_and_length() throws Exception {
    // setup
    ByteBuffer inBuffer = ByteBuffer.wrap(createFilledByteArray(100, 'x'));

    // given
    int testPosition = 23;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES);
    inBuffer.position(testPosition);
    inBuffer.limit(testPosition + A_BYTES.length);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.limit()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.position()).isEqualTo(0);
    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

  @Test
  public void compress_with_array_wrapped_ByteBuffer_using_arrayOffset_and_length() throws Exception {
    // setup
    ByteBuffer inBuffer = ByteBuffer.wrap(createFilledByteArray(100, 'x'));

    // given
    int testPosition = 23;
    inBuffer.position(testPosition);
    inBuffer.put(A_BYTES);
    inBuffer.position(testPosition);
    inBuffer = inBuffer.slice();
    inBuffer.limit(A_BYTES.length);

    // when
    ByteBuffer outBuffer = compressor.compress(inBuffer, true);

    // then
    assertThat(outBuffer.capacity()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.limit()).isEqualTo(A_BYTES_COMPRESSED.length);
    assertThat(outBuffer.position()).isEqualTo(0);
    // then
    byte[] buf = new byte[A_BYTES_COMPRESSED.length];
    outBuffer.get(buf);
    assertThat(buf).startsWith(A_BYTES_COMPRESSED);
  }

}