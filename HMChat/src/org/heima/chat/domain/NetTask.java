package org.heima.chat.domain;

import java.io.Serializable;
import java.util.HashMap;

public class NetTask implements Serializable {

	private static final long serialVersionUID = 6231882517017053073L;

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_UPLOAD = 1;
	public static final int TYPE_DOWNLOAD = 2;

	private long id;
	private String path;
	private HashMap<String, String> params;
	private HashMap<String, String> files;
	private String method = "POST";
	private String protocol;
	private int type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashMap<String, String> getFiles() {
		return files;
	}

	public void setFiles(HashMap<String, String> files) {
		this.files = files;
	}

}
