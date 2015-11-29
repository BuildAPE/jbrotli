package de.bitkings.jbrotli.jni;

import org.testng.annotations.Test;

public class LoadLibraryTest {

  @Test
  public void the_library_can_be_loaded() throws Exception {
    NativeLoader.loadLibrary("brotli");
  }
}
