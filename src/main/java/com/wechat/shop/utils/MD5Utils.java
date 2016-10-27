package com.wechat.shop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class MD5Utils {
	public static final String ALGORITHM = "MD5";
	public static String EncodeByMd5(String str){
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(ALGORITHM);
			Base64 base64 = new Base64();
			String encodedStr = base64.encodeAsString(md5.digest(str.getBytes()));
			return encodedStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}
}
