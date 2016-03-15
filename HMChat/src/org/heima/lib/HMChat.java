package org.heima.lib;

import android.content.Context;
import android.util.Log;

public class HMChat {
	private static HMChat instance;
	private static Context context;

	public static HMChat getInstance() {
		if (instance == null) {
			synchronized (HMChat.class) {
				instance = new HMChat();
			}
		}
		return instance;
	}

	protected static Context getContext() {
		if (HMChat.context == null) {
			throw new RuntimeException(
					"请在Application的onCreate方法中调用HMChat.getInstance().init(context)初始化聊天引擎.");
		}
		return HMChat.context;
	}

	public void init(Context context) {
		HMChat.context = context;
		Log.d("HMChat", "init");
	}

}