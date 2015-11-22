
jBrotli
=========================================

## About jBrotli

Java bindings for [Brotli](https://github.com/google/brotli.git): a new compression algorithm for the internet

##### License

[![License](https://img.shields.io/:license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## About Brotli

Brotli is a generic-purpose lossless compression algorithm that compresses data using a combination of a modern variant of the LZ77 algorithm,
Huffman coding and 2nd order context modeling, with a compression ratio comparable to the best currently available general-purpose compression methods.
It is similar in speed with deflate but offers more dense compression.

It was developed by Google and released in September 2015 via this blog post:
[Introducing Brotli: a new compression algorithm for the internet](http://google-opensource.blogspot.de/2015/09/introducing-brotli-new-compression.html)

## Example compression code snippet

```Java
System.loadLibrary("brotli");

byte[] inBuf = "Brotli: a new compression algorithm for the internet. Now available for Java!".getBytes();
byte[] compressedBuf = new byte[2048];
BrotliCompressor compressor = new BrotliCompressor();
int outLength = compressor.compress(Brotli.DEFAULT_PARAMETER, inBuf, compressedBuf);
```

## Building this library

### Requirements

* Java JDK 8
* C++ compiler tool chain
   * depends on platform
   * successful tested with
      * MS Visual Studio 2010 (on Windows 10, 64bit)
      * NMake (on Windows 10, 64bit)
      * Xcode (on OSX El Capitan 10.11.1)
   * not working with (currently)
      * minggw 64bit doesn't work
* [CMake v3.0+](https://cmake.org/)


### Build native libs

The jbrotli-native Maven modules are configured to automatically be activated on your platform.
E.g. on Windows with a 64bit JDK the module 'win32-x86-64' will be picked up.
If you want to build the 32bit version on Windows, you also need the 32bit JDK installed
and to setup different ENV variables for your Windows SDK (or Visual Studio).
See build.bat files for more details.


#### automatically for your platform

```bash
cd jbrotli-native
mvn package
```


#### manual

Each native module contains a small build script.
E.g. for Windows 64bit, you may use this ...

```bash
cd jbrotli-native/win32-x86-64
build.bat
```


### Prepare JNI header files

This is only needed when native method signatures change.

```bash
mvn -pl jbrotli compile
javah -v -d jbrotli-native/src/main/cpp -classpath jbrotli/target/classes de.bitkings.jbrotli.BrotliCompressor
```

