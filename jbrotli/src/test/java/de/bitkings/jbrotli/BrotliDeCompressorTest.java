package de.bitkings.jbrotli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static de.bitkings.jbrotli.BrotliCompressorTest.A_BUFFER;
import static org.assertj.core.api.Assertions.assertThat;

public class BrotliDeCompressorTest {

  public static final byte[] A_BYTES_COMPRESSED = new byte[]{27, 54, 0, 0, 36, -126, -30, -103, 64, 0};

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

    assertThat(outLen).isEqualTo(A_BUFFER.length);
    assertThat(out).startsWith(A_BUFFER);
  }

  @Test
  public void decompress_with_ByteBuffer() throws Exception {

    ByteBuffer inBuf = ByteBuffer.allocateDirect(A_BYTES_COMPRESSED.length);
    inBuf.put(A_BYTES_COMPRESSED);
    inBuf.position(0);

    byte[] out = new byte[100];
    ByteBuffer outBuf = ByteBuffer.allocateDirect(out.length);

    int outLen = decompressor.deCompress(inBuf, outBuf);

    assertThat(outLen).isEqualTo(A_BUFFER.length);

    outBuf.get(out);
    assertThat(out).startsWith(A_BUFFER);
  }
}