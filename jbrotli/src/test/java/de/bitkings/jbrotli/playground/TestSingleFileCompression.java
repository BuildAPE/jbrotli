package de.bitkings.jbrotli.playground;

import de.bitkings.jbrotli.Brotli;
import de.bitkings.jbrotli.BrotliStreamCompressor;
import org.scijava.nativelib.NativeLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestSingleFileCompression {

  public static void main(String[] args) {
    try {
      new TestSingleFileCompression().run();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void run() throws Exception {
    NativeLoader.loadLibrary("brotli");

    byte[] data = loadDemoData();
    byte[] compressedBuffer = new byte[data.length * 2];

    BrotliStreamCompressor brotliStreamCompressor = new BrotliStreamCompressor(Brotli.DEFAULT_PARAMETER);
    int compressedSize = brotliStreamCompressor.compress(data, compressedBuffer);

    writeDemoDataCompressed(compressedBuffer, compressedSize);
  }

  private void writeDemoDataCompressed(byte[] compressedBuffer, int compressedSize) throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream("cp.html.brotli");) {
      fileOutputStream.write(compressedBuffer, 0, compressedSize);
    }
  }

  private byte[] loadDemoData() throws IOException {
    InputStream inputStream = this.getClass().getResourceAsStream("/cp.html");
    return readAll(inputStream);
  }

  private byte[] readAll(InputStream inputStream) throws IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
      byte[] buf = new byte[16 * 1024];
      for (int read; (read = inputStream.read(buf)) > 0; ) {
        baos.write(buf, 0, read);
      }
      return baos.toByteArray();
    }
  }

}
