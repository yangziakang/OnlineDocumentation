package com.icip.framework.model;

import java.util.Map;
@SuppressWarnings("rawtypes")
public class RequestInfo extends Information{
	private Map requestBody;
	
	private Map requestHeader;
	
	public RequestInfo() {}
	
	public RequestInfo(Map requestBody) {
		this.requestBody = requestBody;
	}
	
	public RequestInfo(Map requestBody, Map requestHeader) {
		this.requestBody = requestBody;
		this.requestHeader = requestHeader;
	}

	@Override
	public Map getHeader() {
		return requestHeader;
	}

	@Override
	public Map getBody() {
		return requestBody;
	}

	public void setRequestBody(Map requestBody) {
		this.requestBody = requestBody;
	}

	public void setRequestHeader(Map requestHeader) {
		this.requestHeader = requestHeader;
	}
	
}
