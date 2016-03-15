#include <jni.h>

JNIEXPORT jint JNICALL Java_com_bin_c2_MainActivity_encodePass
  (JNIEnv * env, jobject obj, jint pass){
	return pass + 2;
}
