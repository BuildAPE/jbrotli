@ECHO OFF

REM
REM Minimal script to compile the native resources
REM
REM Requirements
REM --------------
REM  o  Java 1.8 JDK installed, needs JAVA_HOME set
REM  o  cmake 3.0 + installed and available via PATH
REM  o  nmake installed (comes e.g. with Visual Studio), call "vcvarsall.bat x86" before to activate 64bit tools
REM

:ENSURE_WORKING_DIRECTORY
cd "%~dp0"

:PREPARE_FOLDERS
if not exist "%~dp0target" mkdir "%~dp0target"
if not exist "%~dp0target\classes" mkdir "%~dp0target\classes"
SET TARGET_CLASSES_PATH=%~dp0target\classes

:PREPARE_MAKEFILES
cd "%~dp0target"
cmake -G "NMake Makefiles" ..\..\..\ || goto ERROR

:MAKE_ALL
cd "%~dp0target"
nmake || goto ERROR

:COPY_DLL_FOR_MAVEN_PACKAGING
copy /Y "%~dp0target\brotli.dll" "%TARGET_CLASSES_PATH%" || goto ERROR 

:ENSURE_WORKING_DIRECTORY
cd %~dp0
goto :EOF

:ERROR
cd %~dp0
echo "*** An error occured. Please check log messages. ***"
exit /b -1