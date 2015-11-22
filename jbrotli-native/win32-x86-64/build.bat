@ECHO OFF

REM
REM Minimal script to compile the native resources
REM
REM Requirements
REM --------------
REM  o  Java 1.8 JDK installed, needs JAVA_HOME set
REM  o  cmake 3.0 + installed and available via PATH
REM  o  nmake installed (comes e.g. with Visual Studio), call "vcvarsall.bat x64" before to activate 64bit tools
REM

:ENSURE_WORKING_DIRECTORY
cd %~dp0

:PREPARE_FOLDERS
mkdir target
mkdir target\classes

:PREPARE_MAKEFILES
cd %~dp0target
cmake -G "NMake Makefiles" ..\..\..\

:MAKE_ALL
cd %~dp0target
nmake

:COPY_DLL_FOR_MAVEN_PACKAGING
copy /Y brotli.dll ..\target\classes\

:ENSURE_WORKING_DIRECTORY
cd %~dp0