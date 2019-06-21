package com.icip.framework.file.holder;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.icip.framework.constants.Field;
import com.icip.framework.constants.GlobalConstants;
import com.icip.framework.exception.CustomException;
import com.icip.framework.file.validate.ValidateResult;
import com.icip.framework.file.validate.Validator;
import com.icip.framework.file.validate.ValidatorFactory;
import com.icip.framework.model.RequestInfo;
import com.icip.framework.util.FileUtil;
import com.icip.framework.util.PropertyConfigure;
import com.icip.framework.util.StringUtil;

@SuppressWarnings("rawtypes")
public abstract class AbstractFileHolder implements FileHolder{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractFileHolder.class);

	@Override
	public Map findResponse(String urlServiceCode, RequestInfo requestInfo) throws CustomException {
		String requestServiceCode = getServiceCode(requestInfo);
		
		if(!urlServiceCode.equals(requestServiceCode)) {
			throw new CustomException("请求中的服务码[" + urlServiceCode + "]与接口[" + requestServiceCode + "]不一致，请检查");
		}
		
		String version = getVersion(requestInfo);
		if(StringUtil.isEmpty(version)) {
			throw new CustomException("请求中的版本号不能为空");
		}
		
		ValidateResult result = validateRequest(requestInfo);
		if(!result.isSuccess()) {
			return (Map)JSON.toJSON(result);
		}
		
		String versionPath = new StringBuffer(PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH))
											.append(version).append(File.separator).toString();
		String filePath = FileUtil.findInterface(versionPath, urlServiceCode);
		log.debug("----------------------- 查找的response文件路径：" + filePath + " --------------------------");
		
		if(StringUtil.isEmpty(filePath)) {
			throw new CustomException("接口文件不存在，请生成");
		}
		
		return getResponse(filePath + File.separator + GlobalConstants.RESPONSE_FILE_NAME);
	}
	
	public abstract Map getResponse(String filePath);
	
	public ValidateResult validateRequest(RequestInfo requestInfo) {
		Map body = requestInfo.getBody();
		Map header = requestInfo.getHeader();
		
		Validator validate = ValidatorFactory.getInstance();
		boolean r1 = validate.validateHeader(header);
		
		if(!r1)
			return validate.getResult();
		
		boolean r2 = validate.validateBody(body);
		if(!r2) 
			return validate.getResult();
		
		return new ValidateResult(true, null);
	}
	
	protected String getVersion(RequestInfo requestInfo) {
		Map map = requestInfo.getHeader();
		return (String)map.get(Field.INTERFACE_VERSION);
	}
	
	protected String getServiceCode(RequestInfo requestInfo) {
		Map map = requestInfo.getHeader();
		return (String)map.get(Field.SERVICE_CODE);
	}

}
