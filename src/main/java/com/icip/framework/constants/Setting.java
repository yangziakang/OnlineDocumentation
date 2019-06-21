package com.icip.framework.constants;

public class Setting {

	private static boolean encode = false;

	public static boolean isEncode() {
		return encode;
	}

	public synchronized static void setEncode(boolean encode) {
		Setting.encode = encode;
	}
	
	
}
