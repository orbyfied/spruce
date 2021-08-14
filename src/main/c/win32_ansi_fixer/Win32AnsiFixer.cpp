#include "Windows.h"
#include "io_spruce_Spruce_Win32AnsiFixer.h"

#define _M_FIX Java_io_spruce_Spruce_00024Win32AnsiFixer_fix

JNIEXPORT void JNICALL _M_FIX(JNIEnv* env, jclass klass) {
    // get console handle
    HANDLE handle = GetStdHandle(STDOUT);

    // set console mode
    SetConsoleMode(handle, ENABLE_VIRTUAL_TERMINAL_PROCESSING);
}