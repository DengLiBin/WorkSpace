#include <stdio.h>
#include <stdlib.h>
#include <jni.h>

jstring Java_com_bin_c_MainActivity_helloFromC(JNIEnv* env, jobject obj){//运行环境的指针（env是一个二级指针）和MainActivity对象
	//c语言的字符串(指针存的是第一个字符的地址)
	char* cstr = "hello from c";
	//把C语言的字符串转换成java的字符串
	// jstring     (*NewStringUTF)(JNIEnv*, const char*);
//	jstring jstr = (*(*env)).NewStringUTF(env, cstr);
	jstring jstr = (*env)->NewStringUTF(env, cstr);
	return jstr;
}
jint Java_com_bin_c_MainActivity_add(JNIEnv* env, jobject obj, jint i,jint j){
	return i+j;
}
