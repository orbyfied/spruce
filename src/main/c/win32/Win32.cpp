#include <jni.h>
#include "Windows.h"
#include "io_spruce_Spruce_Win32.h"

#include "const.hpp"

#define _M_FIX_ANSI Java_io_spruce_Spruce_00024Win32_fixAnsi
#define _M_INIT_NTV Java_io_spruce_Spruce_00024Win32_initNative
#define _M_SET_OFLG Java_io_spruce_Spruce_00024Win32_setOutFlag
#define _M_SET_IFLG Java_io_spruce_Spruce_00024Win32_setInFlag

static HANDLE stdout_handle;
static HANDLE stdin_handle;

JNIEXPORT void JNICALL _M_FIX_ANSI(JNIEnv* env, jobject _o) {
    // set console mode
    SetConsoleMode(stdout_handle, ENABLE_VIRTUAL_TERMINAL_PROCESSING | ENABLE_PROCESSED_OUTPUT);
}

JNIEXPORT void JNICALL _M_INIT_NTV(JNIEnv* env, jobject _o) {
    // get out handle
    stdout_handle = GetStdHandle(STD_OUTPUT_HANLDE);

    // get in handle
    stdin_handle = GetStdHandle(STD_INPUT_HANLDE);
}

JNIEXPORT void JNICALL _M_SET_OFLG(JNIEnv* env, jobject _o, jlong f, jboolean b) {
    // get flags
    LPDWORD tmp;
    GetConsoleMode(stdout_handle, tmp);
    unsigned int flags = *((unsigned int*) tmp);

    // set flag
    flags |= f;

    // set mode
    SetConsoleMode(stdout_handle, flags);
}

JNIEXPORT void JNICALL _M_SET_IFLG(JNIEnv* env, jobject _o, jlong f, jboolean b) {
    // get flags
    LPDWORD tmp;
    GetConsoleMode(stdin_handle, tmp);
    unsigned int flags = *((unsigned int*) tmp);

    // set flag
    flags |= f;

    // set mode
    SetConsoleMode(stdin_handle, flags);
}