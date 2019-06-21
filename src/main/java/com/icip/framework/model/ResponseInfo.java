package com.icip.framework.model;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ResponseInfo extends Information {

	private Map responseBody = new HashMap();
	
	private Map responseHeader  = new HashMap();

	public ResponseInfo(){}

	public ResponseInfo(Map responseBody) {
		this.responseBody = responseBody;
	}
	
	public ResponseInfo(Map responseBody, Map responseHeader) {
		this.responseBody = responseBody;
		this.responseHeader = responseHeader;
	}

	@Override
	public Map getHeader() {
		return responseHeader;
	}

	@Override
	public Map getBody() {
		return responseBody;
	}

}
