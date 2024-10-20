#include "jni.h"

JNIEXPORT jobject JNICALL
Java_org_cef_misc_DataPointer_bufferForMemory(JNIEnv* env, jclass cls, jlong addr, jlong capacity);
