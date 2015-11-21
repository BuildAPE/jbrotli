
jbrotli
=========================================

Java bindings for [Brotli](https://github.com/google/brotli.git): a new compression algorithm for the internet

## About Brotli

Brotli is a generic-purpose lossless compression algorithm that compresses data using a combination of a modern variant of the LZ77 algorithm,
Huffman coding and 2nd order context modeling, with a compression ratio comparable to the best currently available general-purpose compression methods.
It is similar in speed with deflate but offers more dense compression.

It was developed by Google and released in September 2015 via this blog post:
[Introducing Brotli: a new compression algorithm for the internet](http://google-opensource.blogspot.de/2015/09/introducing-brotli-new-compression.html)

## Building this library

### Requirements

* Java JDK 8
* C++ compiler tool chain
   * depends on platform
   * currently tested with MS Visual Studio 2010
* [CMake v3.0+](https://cmake.org/)

### Prepare JNI Header

This is only needed when native method signatures change.

```
$> mvn compile
$> javah -v -d src/main/cpp -classpath target/classes de.bitkings.jbrotli.BrotliCompressor
```

