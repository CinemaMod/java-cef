// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

#include "audio_handler.h"

#include "jni_util.h"

//#include <iostream>

AudioHandler::AudioHandler(JNIEnv* env, jobject handler)
    : handle_(env, handler) {}

jobject jniParams(ScopedJNIEnv env, jclass clsProps, const CefAudioParameters& params) {
  jclass cls = env->FindClass("org/cef/misc/CefChannelLayout");
  if (cls == nullptr) {
//    std::cout << "Could not find class 0";
    return nullptr;
  }
  jmethodID getLayout = env->GetStaticMethodID(cls, "forId", "(I)Lorg/cef/misc/CefChannelLayout;");
  if (getLayout == 0) {
//    std::cout << "Could not find method 0";
    return nullptr;
  }
  jobject layout = env->CallStaticObjectMethod(cls, getLayout, (int) params.channel_layout);

  cls = clsProps;
  if (cls == nullptr) {
//    std::cout << "Could not find class 1";
    return nullptr;
  }
  jmethodID constructor = env->GetMethodID(cls, "<init>", "(Lorg/cef/misc/CefChannelLayout;II)V");
  if (constructor == 0) {
//    std::cout << "Could not find constructor 1";
    return nullptr;
  }
  jobject parameters = env->NewObject(cls, constructor, layout, params.sample_rate, params.frames_per_buffer);

  return parameters;
}

jobject jniParams(ScopedJNIEnv env, const CefAudioParameters& params) {
  jclass cls = env->FindClass("org/cef/misc/CefAudioParameters");
  return jniParams(env, cls, params);
}

bool AudioHandler::GetAudioParameters(CefRefPtr<CefBrowser> browser,
                                      CefAudioParameters& params) {
  ScopedJNIEnv env;
  if (!env)
    return true;

  ScopedJNIBrowser jbrowser(env, browser);

  jboolean jreturn = JNI_FALSE;
  jclass cls = env->FindClass("org/cef/misc/CefAudioParameters");
  jobject paramsJni = jniParams(env, cls, params);

  JNI_CALL_METHOD(env, handle_, "getAudioParameters",
                       "(Lorg/cef/browser/CefBrowser;Lorg/cef/misc/CefAudioParameters;)Z", Boolean,
                       jreturn, jbrowser.get(), paramsJni);

  return (jreturn != JNI_FALSE);
}

void AudioHandler::OnAudioStreamStarted(CefRefPtr<CefBrowser> browser,
                                        const CefAudioParameters& params, int channels) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamStarted",
                       "(Lorg/cef/browser/CefBrowser;Lorg/cef/misc/CefAudioParameters;I)V",
                       jbrowser.get(), nullptr, channels);
}

void AudioHandler::OnAudioStreamPacket(CefRefPtr<CefBrowser> browser, const float** data, int frames, int64_t pts) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamPacket",
                  "(Lorg/cef/browser/CefBrowser;JIJ)V",
                  jbrowser.get(), (jlong) data, frames, (long long) pts);
}

void AudioHandler::OnAudioStreamStopped(CefRefPtr<CefBrowser> browser) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamStopped",
                       "(Lorg/cef/browser/CefBrowser;)V",
                       jbrowser.get());
}

void AudioHandler::OnAudioStreamError(CefRefPtr<CefBrowser> browser,
                                      const CefString& text) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);
  ScopedJNIString jtext(env, text);

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamError",
                       "(Lorg/cef/browser/CefBrowser;Ljava/lang/String;)V",
                       jbrowser.get(), jtext.get());
}
