package com.icip.framework.file.creator.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icip.framework.exception.CustomException;
import com.icip.framework.file.creator.AbstractFileCreator;
import com.icip.framework.file.resolve.FileResolver;
import com.icip.framework.file.resolve.FileResolverFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JsonFileCreator extends AbstractFileCreator {
	
	private static final Logger log = LoggerFactory.getLogger(JsonFileCreator.class);

	private Map createHeader() {
		return new HashMap();
	}

	@Override
	protected Map createDocument(Map body) throws CustomException {
		return body;
	}

	@Override
	protected Map createResponse(Map body) throws CustomException {
		FileResolver resolver = FileResolverFactory.getInstance();
		Map result = new HashMap();
		result.put("responseBody", resolver.resolveResponse(body));
		result.put("responseHeader", createHeader());
		return result;
	}

}
