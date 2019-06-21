package com.icip.framework.file.holder;

import java.util.Map;

import com.icip.framework.exception.CustomException;
import com.icip.framework.model.RequestInfo;

@SuppressWarnings("rawtypes")
public interface FileHolder {
	Map findResponse(String serviceCode, RequestInfo requestInfo) throws CustomException;
}
