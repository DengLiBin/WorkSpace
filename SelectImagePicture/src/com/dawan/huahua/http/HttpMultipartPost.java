package com.dawan.huahua.http;

import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dawan.huahua.base.ConstantsConfig;
import com.dawan.huahua.http.CustomMultipartEntity.ProgressListener;
import com.dawan.huahua.utils.SHA256Utils;

public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;

	private String httpUrl;// 请求地址
	private String filePath;// 文件保存地址
	private FileCallback fileCallback;// 回调

	private ProgressDialog pd;
	private static boolean isShowProgress = false;// 配置是否显示弹窗
	private long totalSize;

	// public HttpMultipartPost(Context context, String filePath) {
	// this.context = context;
	// this.filePath = filePath;
	// }

	public HttpMultipartPost(Context context, String httpUrl, String filePath,
			FileCallback fileCallback) {
		this.context = context;
		this.filePath = filePath;

		this.httpUrl = httpUrl;

		this.fileCallback = fileCallback;
	}

	/**
	 * 配置是否显示进度条弹�?
	 * */
	public static void setShowProgress(boolean booleanShowProgress) {
		isShowProgress = booleanShowProgress;
	}

	@Override
	protected void onPreExecute() {
		if (isShowProgress) {
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("正在上传..");
			pd.setCancelable(false);
			pd.show();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(this.httpUrl);

		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					public int compare(String obj1, String obj2) {
						// 升序排序
						return -obj2.compareTo(obj1);
					}
				});

		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					});
			
			map.put("apiname", "comm.debug");
			map.put("timestamp", System.currentTimeMillis() + "");
			map.put("app_key", ConstantsConfig.APP_KEY);
			map.put("cityid", "1");
			
			map.put("platform", "2"); // 2-Android 3-ios

			SharedPreferences mSharedPreferences = context
					.getSharedPreferences("USER", Activity.MODE_PRIVATE);
			if (mSharedPreferences.getString("uid", "") != null
					&& !"".equals(mSharedPreferences.getString("uid", ""))) {
				
				map.put("uid", mSharedPreferences.getString("uid", ""));
				map.put("ukey", mSharedPreferences.getString("code", ""));
			}

			map.put("model", android.os.Build.MODEL + "-"
					+ android.os.Build.VERSION.RELEASE);

			String paramsString = "";
			Set<String> keySet = map.keySet();
			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				Log.i("key-->:", "" + key + ":" + map.get(key));
				paramsString = paramsString + map.get(key);
				
				multipartContent.addPart(key,new StringBody(map.get(key)));
			}

			paramsString = paramsString + ConstantsConfig.APP_SECRET;
			String signString = SHA256Utils.sha256(paramsString);

			multipartContent.addPart("sign", new StringBody(signString));

			// We use FileBody to transfer an image
			multipartContent.addPart("data", new FileBody(new File(filePath)));
			totalSize = multipartContent.getContentLength();

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		if (isShowProgress) {
			pd.setProgress((int) (progress[0]));
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i("S::HttpMime_Success>>>", "HttpMime->" + result + "");
		Log.i("上传完成", "BUG上传完成");

		if (null != this.fileCallback) {
			this.fileCallback.getHttpResult(result);
		}

		if (isShowProgress) {
			pd.dismiss();
		}
	}

	@Override
	protected void onCancelled() {
		System.out.println("cancle");
	}

	/**
	 * 获取返回�?定义�?��公开的接口，用于执行回调操作)
	 * */
	public interface FileCallback {
		public void getHttpResult(String result);
	}
}
