package org.heima.lib.callback;

public interface HMChatCallBack {

	void onSuccess();

	void onProgress(int progress);

	void onError(int error, String msg);

}
