package de.bitkings.jbrotli.performance;

import de.bitkings.jbrotli.Brotli;
import de.bitkings.jbrotli.BrotliCompressor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

@State(Scope.Benchmark)
public class Brotli_vs_Gzip_Benchmark {


  private BrotliCompressor brotliCompressor;
  private byte[] cpHtmlData;
  private byte[] out;
  private ByteArrayOutputStream arrayOutputStream;
  private Brotli.Parameter brotliParameter;

  @Setup
  public void init() throws IOException {
    System.loadLibrary("brotli");
    brotliCompressor = new BrotliCompressor();
    out = new byte[24603];
    cpHtmlData = loadCanterburyCorpusHtmlFile();
    brotliParameter = new Brotli.Parameter(Brotli.Mode.GENERIC, 5, Brotli.DEFAULT_PARAMETER_LGWIN, Brotli.DEFAULT_PARAMETER_LGBLOCK);
  }

  private byte[] loadCanterburyCorpusHtmlFile() throws IOException {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("cp.html");
    byte[] bytes = new byte[24603];
    inputStream.read(bytes);
    return bytes;
  }

  @Benchmark
  public void brotli_compression() {
    brotliCompressor.compress(brotliParameter, cpHtmlData, 0, cpHtmlData.length, out);
  }

  @Benchmark
  public void gzip_compression() throws IOException {
    arrayOutputStream = new ByteArrayOutputStream(24603);
    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
    gzipOutputStream.write(cpHtmlData, 0, cpHtmlData.length);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(Brotli_vs_Gzip_Benchmark.class.getSimpleName())
        .forks(1)
        .warmupIterations(3)
        .measurementIterations(5)
        .build();

    new Runner(opt).run();
  }
}
