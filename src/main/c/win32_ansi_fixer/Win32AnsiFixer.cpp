#include "Windows.h"
#include "io_spruce_Spruce_Win32AnsiFixer.h"

#define _M_FIX Java_io_spruce_Spruce_00024Win32AnsiFixer_fix

#define ENABLE_PROCESSED_OUTPUT            0x0001
#define ENABLE_VIRTUAL_TERMINAL_PROCESSING 0x0004

JNIEXPORT void JNICALL _M_FIX(JNIEnv* env, jclass klass) {
    // get console handle
    HANDLE handle = GetStdHandle(STD_OUTPUT_HANDLE);

    // set console mode
    SetConsoleMode(handle, ENABLE_VIRTUAL_TERMINAL_PROCESSING | ENABLE_PROCESSED_OUTPUT);
}