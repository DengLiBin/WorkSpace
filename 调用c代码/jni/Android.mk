#在该文件所在文件路径下运行cmd命令:ndk-build.cmd（先配置NDK的环境变量）
LOCAL_PATH := $(call my-dir)

    include $(CLEAR_VARS)
	#编译生成的文件的类库叫什么名字
    LOCAL_MODULE    := hello
    #要编译的c文件
    LOCAL_SRC_FILES := Hello.c

    include $(BUILD_SHARED_LIBRARY)