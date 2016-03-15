package org.heima.lib.core;

import java.util.HashMap;
import java.util.Map;

import org.heima.lib.msg.SequenceCreater;

import com.google.gson.Gson;

public class AuthRequest extends ChatRequest {
	private String account;
	private String token;

	private String sequence;

	public AuthRequest(String account, String token) {
		super(null, null);

		this.account = account;
		this.token = token;

		this.sequence = SequenceCreater.createSequence();
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	@Override
	public String getTransport() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("action", "auth");
		map.put("type", "request");
		map.put("sequence", sequence);
		map.put("sender", account);
		map.put("token", token);
		return new Gson().toJson(map);
	}

}
