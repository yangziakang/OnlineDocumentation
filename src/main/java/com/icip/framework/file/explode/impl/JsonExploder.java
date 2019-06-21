package com.icip.framework.file.explode.impl;

import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.icip.framework.exception.CustomException;
import com.icip.framework.file.explode.AbstractExploder;
import com.icip.framework.model.RequestInfo;
import com.icip.framework.model.ResponseInfo;
import com.icip.framework.model.ServiceInfo;
import com.icip.framework.util.StringUtil;

@SuppressWarnings("rawtypes")
public class JsonExploder extends AbstractExploder {
	
	private RequestInfo requestInfo;
	private ResponseInfo responseInfo;
	private ServiceInfo serviceInfo;
	
	public JsonExploder(RequestInfo requestInfo, ResponseInfo responseInfo, ServiceInfo serviceInfo) {
		this.requestInfo = requestInfo;
		this.responseInfo = responseInfo;
		this.serviceInfo = serviceInfo;
	}
	
	public JsonExploder(JSONObject json) throws CustomException {
		String interfaceName = null;
		Iterator it = json.keySet().iterator();
		while(it.hasNext()) {
			String s = (String)it.next();
			if(StringUtil.isNotEmpty(s) && !"description".equals(s) && !"group".equals(s) && !"version".equals(s)) {
				interfaceName = s;
				break;
			}
		}
		
		if(StringUtil.isEmpty(interfaceName))
			throw new CustomException("接口名称不能为空");
		
		Map jsonMap = (Map)json.get(interfaceName);
		
		Map requestBody = null;
		Map responseBody = null;
		Map requestMap = (Map)jsonMap.get("request");
		if(requestMap != null) 
			requestBody = (Map)requestMap.get("requestBody");
		Map responseMap = (Map)jsonMap.get("response");
		if(responseMap != null) 
			responseBody = (Map)responseMap.get("responseBody");
		String description = (String)json.get("description");
		String group = (String)json.get("group");
		String version = (String)json.get("version");
		
		interfaceName = interfaceName + "[" + description + "]";
		this.requestInfo = new RequestInfo(requestBody);
		this.responseInfo = new ResponseInfo(responseBody);
		this.serviceInfo = new ServiceInfo(version, group, description, interfaceName);
	}

	@Override
	public RequestInfo getRequest() {
		return requestInfo;
	}

	@Override
	public ResponseInfo getResponse() {
		return responseInfo;
	}

	@Override
	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	
}
