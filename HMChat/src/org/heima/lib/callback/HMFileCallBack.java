package org.heima.lib.callback;

import java.io.File;

public abstract class HMFileCallBack {

	public abstract void onSuccess(File file);

	public abstract void onProgress(int writen, int total);

	public abstract void onError(int error, String msg);

}
