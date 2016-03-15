package com.dawan.huahua.utils;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class SDPathUtils {

	private static Context mAppContext;

	public static void setAppContext(Context context) {
		mAppContext = context;
	}

	public static Context getAppContext() {
		return mAppContext;
	}

	/**
	 * ��ȡSD����ַ
	 */
	public static String getCrashPath() {
		String filePath = getAppRootPath() + "/crash/";
		File file = new File(filePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		file = null;
		return filePath;
	}
	
	/**
	 * ��ȡSDͼƬ�����ַ
	 */
	public static String getCachePath() {
		String filePath = getAppRootPath() + "/cache/";
		File file = new File(filePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		file = null;
		return filePath;
	}

	/**
	 * ��ȡ��SD����Ŀ¼
	 */
	public static String getAppRootPath() {
		String filePath = "/ZFWCrash";
		// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// ��ȡSD��·��
			filePath = Environment.getExternalStorageDirectory() + filePath;
		} else {
			// ��ȡ�ֻ�·��
			filePath = getAppContext().getCacheDir() + filePath;
		}

		File folder = new File(filePath);
		// �ж��ļ�Ŀ¼�Ƿ����
		if (folder.exists() && folder.isDirectory()) {
			// do nothing
		} else {
			folder.mkdirs();
		}
		folder = null;

		File nomedia = new File(filePath + "/.nomedia");
		if (!nomedia.exists())
			try {
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return filePath;
	}

	/**
	 * ɾ���ļ�
	 * */
	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		try {
			if (file.exists() && file.isFile()) {
				Log.i("DelFile�ļ�ɾ��", "�ļ�ɾ���ɹ�");
				return file.delete();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
