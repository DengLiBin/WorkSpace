#默认不会生成x86架构的类库，需要添加该文件，编译时就会同时生成支持arm和x86架构的类库
APP_ABI := armeabi armeabi-v7a x86
# APP_ABI := all 生成支持所有架构的类库（编译时间长）