package org.heima.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.mina.core.session.IoSession;
import org.heima.lib.callback.HMChatCallBack;
import org.heima.lib.callback.HMFileCallBack;
import org.heima.lib.callback.HMObjectCallBack;
import org.heima.lib.core.AuthRequest;
import org.heima.lib.core.ChatRequest;
import org.heima.lib.core.PacketConnector;
import org.heima.lib.core.PacketConnector.ConnectListener;
import org.heima.lib.core.PacketConnector.IOListener;
import org.heima.lib.future.HttpFuture;
import org.heima.lib.msg.ChatMessage;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class HMChatManager {
	private static HMChatManager instance;
	private Context context;
	private Map<String, String> headers = new HashMap<String, String>();

	private String authSequence;
	private PacketConnector connector;

	private List<HMConnectListener> connectListeners = new LinkedList<HMConnectListener>();

	private OnPushListener pushListener;

	private Map<String, ChatRequest> requests = new LinkedHashMap<String, ChatRequest>();

	private Thread mainThread;
	private Handler handler = new Handler();

	public static HMChatManager getInstance() {
		if (instance == null) {
			synchronized (HMChatManager.class) {
				if (instance == null) {
					instance = new HMChatManager();
				}
			}
		}
		return instance;
	}

	private HMChatManager() {
		context = HMChat.getContext();
		mainThread = Thread.currentThread();
	}

	/**
	 * 初始化连接用户的安全信息
	 * 
	 * @param account
	 * @param token
	 */
	public void initAccount(String account, String token) {
		headers.put("account", account);
		headers.put("token", token);
	}

	/**
	 * 登录
	 * 
	 * @param account
	 * @param password
	 * @param callBack
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HMFuture login(String account, String password,
			final HMObjectCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		client.setResponseTimeout(30 * 1000);
		String url = HMURL.URL_HTTP_LOGIN;
		RequestParams params = new RequestParams();
		params.put("account", account);
		params.put("password", password);

		return new HttpFuture(client.post(context, url, params,
				newObjectResponseHandler(callBack)));
	}

	/**
	 * 注册
	 * 
	 * @param account
	 * @param password
	 * @param callBack
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HttpFuture register(String account, String password,
			final HMObjectCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		client.setResponseTimeout(30 * 1000);
		String url = HMURL.URL_HTTP_REGISTER;
		RequestParams params = new RequestParams();
		params.put("account", account);
		params.put("password", password);

		return new HttpFuture(client.post(context, url, params,
				newObjectResponseHandler(callBack)));
	}

	/**
	 * 搜索用户
	 * 
	 * @param account
	 * @param callBack
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HttpFuture searchContact(String search,
			final HMObjectCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		client.setResponseTimeout(30 * 1000);
		String url = HMURL.URL_HTTP_SEARCH_CONTACT;

		for (Map.Entry<String, String> me : headers.entrySet()) {
			client.addHeader(me.getKey(), me.getValue());
		}

		RequestParams params = new RequestParams();
		params.put("search", search);
		return new HttpFuture(client.post(context, url, params,
				newObjectResponseHandler(callBack)));
	}

	/**
	 * post 方式 请求服务器
	 * 
	 * @param path
	 *            分支节点路径
	 * @param parameters
	 *            参数键值对
	 * @param callBack
	 *            网络回调
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HttpFuture postByPath(String path, Map<String, String> parameters,
			final HMObjectCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		client.setResponseTimeout(30 * 1000);
		String url = HMURL.BASE_HTTP + path;

		for (Map.Entry<String, String> me : headers.entrySet()) {
			client.addHeader(me.getKey(), me.getValue());
		}

		RequestParams params = new RequestParams();
		if (parameters != null) {
			for (Map.Entry<String, String> me : parameters.entrySet()) {
				String key = me.getKey();
				String value = me.getValue();
				params.put(key, value);
			}
		}
		return new HttpFuture(client.post(context, url, params,
				newObjectResponseHandler(callBack)));
	}

	/**
	 * post 方式 请求服务器
	 * 
	 * @param url
	 *            服务器url
	 * @param parameters
	 *            参数键值对
	 * @param callBack
	 *            网络回调
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HttpFuture postByURL(String url, Map<String, String> parameters,
			final HMObjectCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		client.setResponseTimeout(30 * 1000);

		for (Map.Entry<String, String> me : headers.entrySet()) {
			client.addHeader(me.getKey(), me.getValue());
		}

		RequestParams params = new RequestParams();
		if (parameters != null) {
			for (Map.Entry<String, String> me : parameters.entrySet()) {
				String key = me.getKey();
				String value = me.getValue();
				params.put(key, value);
			}
		}
		return new HttpFuture(client.post(context, url, params,
				newObjectResponseHandler(callBack)));
	}

	/**
	 * 上传文件
	 * 
	 * @param path
	 * @param account
	 * @param token
	 * @param files
	 * @param parameters
	 * @param callBack
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HttpFuture uploadFile(String path, Map<String, File> files,
			Map<String, String> parameters, final HMObjectCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		//client.setResponseTimeout(30 * 1000);
		String url = HMURL.BASE_HTTP + path;

		for (Map.Entry<String, String> me : headers.entrySet()) {
			client.addHeader(me.getKey(), me.getValue());
		}

		RequestParams params = new RequestParams();
		if (files != null) {
			for (Map.Entry<String, File> me : files.entrySet()) {
				try {
					params.put(me.getKey(), me.getValue());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		if (parameters != null) {
			for (Map.Entry<String, String> me : parameters.entrySet()) {
				String key = me.getKey();
				String value = me.getValue();
				params.put(key, value);
			}
		}

		
		return new HttpFuture(client.post(context, url, params,
				newObjectResponseHandler(callBack)));
	}

	@SuppressWarnings("rawtypes")
	public HttpFuture downloadFile(String path, File file,
			final HMFileCallBack callBack) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30 * 1000);
		client.setMaxRetriesAndTimeout(5, 30 * 1000);
		client.setResponseTimeout(30 * 1000);
		String url = HMURL.BASE_HTTP + path;

		for (Map.Entry<String, String> me : headers.entrySet()) {
			client.addHeader(me.getKey(), me.getValue());
		}

		return new HttpFuture(client.get(url, new FileAsyncHttpResponseHandler(
				file) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, File file) {
				callBack.onSuccess(file);
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);

				callBack.onProgress(bytesWritten, totalSize);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, File file) {
				callBack.onError(HMError.ERROR_SERVER,
						"服务器异常 : " + throwable.getMessage());
			}
		}));
	}

	@SuppressWarnings("rawtypes")
	private TextHttpResponseHandler newObjectResponseHandler(
			final HMObjectCallBack callBack) {
		return new TextHttpResponseHandler("utf-8") {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				Log.d("###", "" + responseString);

				if (statusCode == 200) {
					JsonParser parser = new JsonParser();
					JsonObject root = parser.parse(responseString)
							.getAsJsonObject();
					if (root == null) {
						if (callBack != null) {
							callBack.onError(HMError.ERROR_SERVER, "服务器异常");
						}
					} else {
						if (callBack != null) {
							JsonPrimitive flagObj = root
									.getAsJsonPrimitive("flag");
							boolean flag = flagObj.getAsBoolean();
							if (flag) {
								JsonObject dataObj = root
										.getAsJsonObject("data");

								if (dataObj == null) {
									callBack.onSuccess(null);
								} else {
									Object data = new Gson().fromJson(dataObj,
											callBack.getClazz());
									callBack.onSuccess(data);
								}

							} else {
								// 如果返回错误
								// 获得错误code
								JsonPrimitive errorCodeObj = root
										.getAsJsonPrimitive("errorCode");
								// 获得错误string
								JsonPrimitive errorStringObj = root
										.getAsJsonPrimitive("errorString");

								int errorCode = errorCodeObj.getAsInt();
								String errorString = errorStringObj
										.getAsString();

								callBack.onError(errorCode, errorString);
							}
						}
					}
				} else {
					if (callBack != null) {
						callBack.onError(HMError.ERROR_SERVER, "服务器异常");
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (callBack != null) {
					callBack.onError(HMError.ERROR_SERVER, "服务器异常 : "
							+ throwable.getMessage());
				}
			}
		};
	}

	/**
	 * socket 连接认证
	 * 
	 * @param account
	 * @param token
	 */
	public void auth(final String account, final String token) {
		headers.put("account", account);
		headers.put("token", token);

		new Thread() {
			public void run() {
				AuthRequest request = new AuthRequest(account, token);

				if (connector == null) {
					connector = new PacketConnector(HMURL.BASE_HM_HOST,
							HMURL.BASE_HM_PORT, 3);
				}

				conncectChatServer();

				authSequence = request.getSequence();

				connector.addRequest(request);

			};
		}.start();
	}

	public void closeSocket() {
		if (connector != null && connector.isConnected()) {
			connector.disconnect();
			connector = null;
		}
	}

	private void conncectChatServer() {
		if (connector != null) {
			connector.connect();
			// 设置输入输出监听
			connector.setIOListener(ioListener);
			// 设置连接监听
			connector.setConnectListener(connectListener);
		}
	}

	/**
	 * 添加连接监听
	 * 
	 * @param listener
	 */
	public void addConnectionListener(HMConnectListener listener) {
		if (!connectListeners.contains(listener)) {
			connectListeners.add(listener);
		}
	}

	/**
	 * 移除连接监听
	 * 
	 * @param listener
	 */
	public void removeConnectionListener(HMConnectListener listener) {
		if (connectListeners.contains(listener)) {
			connectListeners.remove(listener);
		}
	}

	/**
	 * 添加消息推送监听
	 * 
	 * @param listener
	 */
	public void setPushListener(OnPushListener listener) {
		this.pushListener = listener;
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @param callBack
	 */
	public void sendMessage(final ChatMessage message,
			final HMChatCallBack callBack) {
		new Thread() {
			public void run() {
				if (connector == null) {
					connector = new PacketConnector(HMURL.BASE_HM_HOST,
							HMURL.BASE_HM_PORT, 3);
				}

				conncectChatServer();

				addRequest(message, callBack);
			}
		}.start();
	}

	private void addRequest(final ChatMessage message,
			final HMChatCallBack callBack) {
		// 加入到请求map中 为以后的response做处理
		ChatRequest request = new ChatRequest(callBack, message);
		requests.put(request.getSequence(), request);

		connector.addRequest(request);
	}

	private IOListener ioListener = new IOListener() {

		@Override
		public void onOutputFailed(ChatRequest request, Exception e) {
			// 消息发送失败，通知回调，让显示层做处理
			HMChatCallBack callBack = request.getCallBack();
			if (callBack != null) {
				callBack.onError(HMError.ERROR_CLIENT_NET, "客户端网络未连接");
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onInputComed(IoSession session, Object message) {
			if (message instanceof String) {
				String json = ((String) message).trim();

				JsonParser parser = new JsonParser();
				JsonObject root = parser.parse(json).getAsJsonObject();

				// 获得方向:是请求还是response
				JsonPrimitive typeJson = root.getAsJsonPrimitive("type");
				String type = typeJson.getAsString();

				// 获得序列号
				JsonPrimitive sequenceJson = root
						.getAsJsonPrimitive("sequence");
				String sequence = sequenceJson.getAsString();

				if ("request".equalsIgnoreCase(type)) {
					// 服务器推送消息
					JsonPrimitive actionJson = root
							.getAsJsonPrimitive("action");
					String action = actionJson.getAsString();
					if (pushListener != null) {
						boolean pushed = pushListener.onPush(action,
								(Map<String, Object>) new Gson().fromJson(root,
										new TypeToken<Map<String, Object>>() {
										}.getType()));
						if (pushed) {
							session.write("{type:'response',sequence:'"
									+ sequence + "',flag:" + true + "}");
						} else {
							session.write("{type:'response',sequence:'"
									+ sequence + "',flag:" + false
									+ ",errorCode:1,errorString:'客户端未处理成功!'}");
						}
					}
				} else if ("response".equalsIgnoreCase(type)) {
					// 请求返回response
					JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
					boolean flag = flagJson.getAsBoolean();
					// 消息发送结果只有 成功或者 失败,不需要返回对象
					if (flag) {
						if (sequence.equals(authSequence)) {
							Log.d("#####", "认证成功");
							return;
						}

						// 消息成功发送
						ChatRequest request = requests.remove(sequence);
						if (request != null) {
							final HMChatCallBack callBack = request
									.getCallBack();
							if (callBack != null) {
								// 在主线程中调用
								if (Thread.currentThread() != mainThread) {
									handler.post(new Runnable() {
										@Override
										public void run() {
											callBack.onSuccess();
										}
									});
								} else {
									callBack.onSuccess();
								}
							}
						}
					} else {
						if (sequence.equals(authSequence)) {
							Log.d("#####", "认证失败");
							return;
						}

						// 认证失败
						ListIterator<HMConnectListener> iterator = connectListeners
								.listIterator();
						while (iterator.hasNext()) {
							final HMConnectListener listener = iterator.next();
							// 在主线程中调用
							if (Thread.currentThread() != mainThread) {
								handler.post(new Runnable() {
									@Override
									public void run() {
										listener.onAuthFailed();
									}
								});
							} else {
								listener.onAuthFailed();
							}
						}
					}
				}
			}
		}
	};

	private ConnectListener connectListener = new ConnectListener() {

		@Override
		public void onReConnecting() {
			ListIterator<HMConnectListener> iterator = connectListeners
					.listIterator();
			while (iterator.hasNext()) {
				HMConnectListener listener = iterator.next();
				listener.onReconnecting();
			}
		}

		@Override
		public void onDisconnected() {
			Log.d("HM", "onDisconnected");

			ListIterator<HMConnectListener> iterator = connectListeners
					.listIterator();
			while (iterator.hasNext()) {
				HMConnectListener listener = iterator.next();
				listener.onDisconnected();
			}

			authSequence = null;
			connector = null;
		}

		@Override
		public void onConnecting() {
			ListIterator<HMConnectListener> iterator = connectListeners
					.listIterator();
			while (iterator.hasNext()) {
				HMConnectListener listener = iterator.next();
				listener.onReconnecting();
			}
		}

		@Override
		public void onConnected() {
			ListIterator<HMConnectListener> iterator = connectListeners
					.listIterator();
			while (iterator.hasNext()) {
				HMConnectListener listener = iterator.next();
				listener.onConnected();
			}
		}
	};

	public interface HMConnectListener {
		/**
		 * 正在连接
		 */
		void onConnecting();

		/**
		 * 已经连接
		 */
		void onConnected();

		/**
		 * 已经断开连接
		 */
		void onDisconnected();

		/**
		 * 正在重试连接
		 */
		void onReconnecting();

		/**
		 * 用户认证失败
		 */
		void onAuthFailed();
	}

	public interface OnPushListener {
		boolean onPush(String type, Map<String, Object> data);
	}
}
