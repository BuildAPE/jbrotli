
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

```bash
mkdir build
cd build
cmake ..
```

### Prepare JNI header files

This is only needed when native method signatures change.

```bash
mvn compile
javah -v -d src/main/cpp -classpath target/classes de.bitkings.jbrotli.BrotliCompressor
```

