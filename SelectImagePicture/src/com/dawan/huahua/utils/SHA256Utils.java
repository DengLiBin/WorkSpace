package com.dawan.huahua.utils;

import java.security.MessageDigest;

public class SHA256Utils {

	/**
	 * sha256加密
	 * 
	 * @param paramsString
	 *            要加密的字符�?
	 * @return temp 返回加密�?
	 */
	public static String sha256(String paramsString) {
		String temp = null;
		paramsString = paramsString.replace("null", "");
		
		try {
//			paramsString = URLEncoder.encode(paramsString, "utf-8");
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(paramsString.getBytes("utf-8"));
			temp = bytes2Hex(md.digest()); // to HexString

			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * hex字符串与byte数组互转
	 * */
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
