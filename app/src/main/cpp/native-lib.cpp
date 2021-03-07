//
// Created by Willi on 01/03/2021.
//

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_app_1td3_1william_1fernandes_1ios2_DisplayDataActivity_fetchURL(JNIEnv *env, jclass clazz) {
    std::string api_url = "aHR0cHM6Ly82MDA3ZjFhNDMwOWY4YjAwMTdlZTUwMjIubW9ja2FwaS5pby9hcGkvbTEv"; // url's hash
    return env->NewStringUTF(api_url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_app_1td3_1william_1fernandes_1ios2_LoginActivity_fetchPswd(JNIEnv *env, jclass clazz) {
    std::string user_pswd = "a290bGlu"; // password's hash
    return env->NewStringUTF(user_pswd.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_app_1td3_1william_1fernandes_1ios2_DisplayDataActivity_fetchKey(JNIEnv *env, jclass clazz) {
    std::string db_secret_key = "WkoIQidCPiSgG+OwfQaVYg=="; // secret key for db encryption AES-256
    return env->NewStringUTF(db_secret_key.c_str());
}