cmake_minimum_required(VERSION 3.0.0)

project (brotli)

find_package(Java REQUIRED COMPONENTS Development)
find_package(JNI REQUIRED)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()
include_directories(${JNI_INCLUDE_DIRS})
include_directories(${JNI_LIBRARIES})

# Allow the developer to select if Dynamic or Static libraries are built
#OPTION (BUILD_SHARED_LIBS "Build Shared Libraries" OFF)
# Set the LIB_TYPE variable to STATIC
#SET (LIB_TYPE STATIC)
SET (LIB_TYPE SHARED)

add_library (brotli ${LIB_TYPE} "brotli/enc/backward_references.cc"
				"brotli/enc/block_splitter.cc"
				"brotli/enc/brotli_bit_stream.cc"
				"brotli/enc/encode.cc"
				"brotli/enc/entropy_encode.cc"
				"brotli/enc/histogram.cc"
				"brotli/enc/literal_cost.cc"
				"brotli/enc/metablock.cc"
				"brotli/enc/static_dict.cc"
				"brotli/enc/streams.cc"
				"brotli/dec/bit_reader.c"
				"brotli/dec/decode.c"
				"brotli/dec/huffman.c"
				"brotli/dec/streams.c"
				"brotli/dec/state.c"
				"jbrotli-native/src/main/cpp/de_bitkings_jbrotli_BrotliCompressor.cxx")

SET_TARGET_PROPERTIES(brotli PROPERTIES LINKER_LANGUAGE CXX)

#SET_SOURCE_FILES_PROPERTIES(example.i PROPERTIES CPLUSPLUS 1)
#SET_SOURCE_FILES_PROPERTIES(example.i PROPERTIES SWIG_FLAGS "-includeall")

set(CMAKE_JNI_TARGET TRUE)

SET(CMAKE_CXX_FLAGS "-std=c++11 -Wall -O3")

if(WIN32)
	SET(STATIC_LIBRARY_CXX_FLAGS /MDd)
	add_compile_options(/EHsc)
else(WIN32)
	SET(STATIC_LIBRARY_CXX_FLAGS)
endif()