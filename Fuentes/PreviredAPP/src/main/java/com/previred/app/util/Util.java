package com.previred.app.util;

import org.apache.tomcat.util.codec.binary.Base64;

public class Util {

	public static String identificador(String rs,String key) {

		String codigo = rs+key;
		byte[] bytesEncoded = Base64.encodeBase64(codigo.getBytes());
		return new String(bytesEncoded);
	}

	
	
}
