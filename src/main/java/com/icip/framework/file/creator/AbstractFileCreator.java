package com.icip.framework.file.creator;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icip.framework.exception.CustomException;
import com.icip.framework.file.validate.ValidateResult;
import com.icip.framework.file.validate.Validator;
import com.icip.framework.file.validate.ValidatorFactory;
import com.icip.framework.model.Information;

@SuppressWarnings("rawtypes")
public abstract class AbstractFileCreator implements FileCreator {

	@Override
	public Map createRequestDocument(Information requestInfo) throws CustomException {
		return getDocument(requestInfo);
	}
	
	protected abstract Map createDocument(Map body) throws CustomException;

	@Override
	public Map createResponseDocument(Information responseInfo) throws CustomException {
		return getDocument(responseInfo);
	}

	@Override
	public Map createResponseMap(Information responseInfo) throws CustomException {
		String jsonStr = JSONObject.toJSONString(responseInfo.getBody());
		JSONObject body = JSONObject.parseObject(jsonStr);
		return createResponse(body);
	}
	
	private Map getDocument(Information info) throws CustomException {
		String jsonStr = JSONObject.toJSONString(info.getBody());
		JSONObject body = JSONObject.parseObject(jsonStr);
		
		ValidateResult result = validate(info);
		if(!result.isSuccess()) {
			return (Map)JSON.toJSON(result);
		}
		
		return createDocument(body);
	}
	
	protected abstract Map createResponse(Map body) throws CustomException;
	
	public ValidateResult validate(Information info) {
		Map body = info.getBody();
		Map header = info.getHeader();
		
		Validator validate = ValidatorFactory.getDocumentInstance();
		boolean r1 = validate.validateHeader(header);
		
		if(!r1)
			return validate.getResult();
		
		boolean r2 = validate.validateBody(body);
		if(!r2) 
			return validate.getResult();
		
		return new ValidateResult(true, null);
	}

}
