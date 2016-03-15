package org.heima.lib.core;

import java.util.HashMap;
import java.util.Map;

import org.heima.lib.callback.HMChatCallBack;
import org.heima.lib.msg.ChatMessage;

import android.util.Log;

import com.google.gson.Gson;

public class ChatRequest {
	private HMChatCallBack callBack;
	private ChatMessage message;

	private String sequence;
	private Map<String, Object> map;

	public ChatRequest(HMChatCallBack callBack, ChatMessage message) {
		super();
		this.callBack = callBack;
		this.message = message;

		map = new HashMap<String, Object>();
		if (message != null) {
			map.putAll(this.message.getMap());
			sequence = (String) map.get("sequence");
		}
	}

	public String getSequence() {
		return sequence;
	}

	public String getTransport() {
		Log.d("", "" + map.toString());
		
		return new Gson().toJson(map);
	}

	public HMChatCallBack getCallBack() {
		return callBack;
	}

	public void setAccount(String account) {
		if (map != null) {
			map.put("account", account);
		}
	}

	public void setToken(String token) {
		if (map != null) {
			map.put("token", token);
		}
	}
}
