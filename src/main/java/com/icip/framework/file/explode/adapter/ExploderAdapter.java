package com.icip.framework.file.explode.adapter;

import com.alibaba.fastjson.JSONObject;
import com.icip.framework.exception.CustomException;
import com.icip.framework.file.explode.Exploder;
import com.icip.framework.file.explode.impl.JsonExploder;
import com.icip.framework.file.explode.impl.RequestExploder;
import com.icip.framework.model.ExtRequestInfo;

@SuppressWarnings("rawtypes")
public class ExploderAdapter {

	public static Exploder getDocumentExploder(String jsonStr) throws CustomException {
		return new JsonExploder(JSONObject.parseObject(jsonStr));
	}

	public static Exploder getRequestExploder(ExtRequestInfo requestInfo) {
		return new RequestExploder(requestInfo);
	}

}
