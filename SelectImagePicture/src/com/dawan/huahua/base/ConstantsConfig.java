package com.dawan.huahua.base;

import android.annotation.SuppressLint;

public class ConstantsConfig {
	/******************************************** 参数设置信息开始 *****************************************/
	// 屏幕高度
	public static int SCREEN_HEIGHT = 800;
	// 屏幕宽度
	public static int SCREEN_WIDTH = 480;
	// 屏幕密度
	public static float SCREEN_DENSITY = 1.5f;
	// 客服电话
	public static String CALL_PHONE = "57285601";
	// APP所在城市ID
	public static String APP_CITY_NUM = "1";
	// APP对接唯一标示
	public static String APP_KEY = "KwXLJI6oRDt9";
	// APP加密秘钥
	public static String APP_SECRET = "9swwW2BanR4CY993RDrwSND0CmaNXN0u63jpYN5VRTmvHWo2kaIuGqePEExJN1KSgyoRP19MZ8Wpu9S9L3Kzfyimt7uSATS8kudtWXzMX8Md6mJHCqOnCA0UIuSeaGcLAWruB55iCHqUk10tzz31TdkdmlrQ5hfQzcSqoGHwQdqPVBCjPhJDO7qnVWPWK1bvWOi4HiwtqYwvzNTfSg7PEdsIE0cnHM8cuhNzqMYwllKqJKEH1FUuRxCRXWtmHlmo";
	// 头像保存地址
	@SuppressLint("SdCardPath")
	public static String SDPATH = "/data/data/com.zfw.zhaofang/images/";
	// 是否开启BUG日志删除(false::可以看手机SD文本日志[ZFWCrash])
	public static boolean IS_BUG = true;
	// 是否提交崩溃日志(正式的APP:true|测试的APP:false)
	public static boolean IS_LOG_CRASH = true;
	// 是否打印日志(正式的APP:false|测试的APP:true)
	public static boolean IS_LOG_DEBUG = true;

	/** 本地测试接口 254测试 ***************************************************************/
	// // APP城市数据接口地址
	// public static String APP_CITY_API = "http://192.168.1.250:803/";
	// // APP公共数据接口地址
	// public static String APP_COMMON_API = "http://192.168.1.250:802/";
	// // APP图片网关接口地址
	// public static String APP_IMG_API = "http://192.168.1.250:801/";
	/** 本地测试接口 254测试 ***************************************************************/
	// // APP城市数据接口地址
	// public static String APP_CITY_API = "http://192.168.1.254:803/";
	// // APP公共数据接口地址
	// public static String APP_COMMON_API = "http://192.168.1.254:802/";
	// // APP图片网关接口地址
	// public static String APP_IMG_API = "http://192.168.1.254:801/";
	/** 外网接口 正式版本发布 ***************************************************************/
	// //APP城市数据接口地址
	// public static String APP_CITY_API = "http://d1.api.zhaofang.com/";
	// // APP公共数据接口地址
	// public static String APP_COMMON_API = "http://comm.api.zhaofang.com/";
	// // APP图片网关接口地址
	// public static String APP_IMG_API = "http://img.zhaofang.com/";
	/*------------------------------------------------------------------------------*/
	// APP城市数据接口地址
//	public static String APP_CITY_API = "http://v3d1.api.zhaofang.com/";
//	// APP公共数据接口地址
//	public static String APP_COMMON_API = "http://v3comm.api.zhaofang.com/";
//	// APP图片网关接口地址
//	public static String APP_IMG_API = "http://img.zhaofang.com/";
	/** 外网接口 正式版本发布 ***************************************************************/
	/** 外网测试接口 只对内部版本测试 **********************************************************/
	// // APP城市数据接口地址
	// public static String APP_CITY_API =
	// "http://112.126.64.184:600/city/api.ashx/";
	// // APP公共数据接口地址
	// public static String APP_COMMON_API =
	// "http://112.126.64.184:600/comm/api.ashx/";
	// // APP图片网关接口地址
	// public static String APP_IMG_API = "http://img.zhaofang.com/";
	/*------------------------------------------------------------------------------*/
	// APP城市数据接口地址
	public static String APP_CITY_API = "http://112.126.64.184:500/city/api.ashx/";
	// APP公共数据接口地址
	public static String APP_COMMON_API = "http://112.126.64.184:500/comm/api.ashx/";
	// APP图片网关接口地址
	public static String APP_IMG_API = "http://img.zhaofang.com/";
	/** 外网接口 正式版本发布 ***************************************************************/

	// 版本号
	public static String VERSION = "http://m.zhaofang.com/updata/version.xml";
	/******************************************** 参数设置信息结束 *****************************************/
}
