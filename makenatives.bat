@echo off

echo ...........................
echo ...........................
echo Spruce natives build script
echo ...........................
echo ...........................

:: Create natives output folder
cd build
mkdir natives
cd ..

:: CD into C/native code folder
cd src/main/c

:: Win32AnsiFixer

echo ...........................
echo Win32
echo ...........................

cd win32

echo .
echo . x86
echo .
make -f Makefile32
make -f Makefile32 clean
echo .
echo . x64
echo .
make -f Makefile64
make -f Makefile64 clean

cd ..

:: Exit folder
cd ../../..