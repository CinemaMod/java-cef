#include "DataPointer.h"
#include "jni.h"
#include "jni_util.h"
#include "jni_scoped_helpers.h"

JNIEXPORT jobject JNICALL
Java_org_cef_misc_DataPointer_bufferForMemory(JNIEnv* env, jclass cls, jlong addr, jlong capacity) {
  // type information is irrelevant here
  ScopedJNIObjectLocal jdirectBuffer(
      env,
      env->NewDirectByteBuffer((void*) addr, capacity));
  return jdirectBuffer.get();
}
