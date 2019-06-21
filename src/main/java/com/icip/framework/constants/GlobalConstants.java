package com.icip.framework.constants;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstants {

	public static final String FILE_ROOT_PATH = "file.root.path";
	
	public static final String SUCCESS = "1";
	public static final String FAILED = "0";

	public static final String DOCUMENT_FILE_NAME = "document.json";
	
	public static final String RESPONSE_FILE_NAME = "response.json";
	
	public static final Map PUB_HEADER = new HashMap();
	static {
		PUB_HEADER.put("returnCode", "000000");
		PUB_HEADER.put("returnStatus", "1");
		PUB_HEADER.put("serviceCode", "USQueryUserInfo");
	}

}
