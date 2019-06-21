package com.icip.framework.file.validate.impl;

import java.util.Map;

import com.icip.framework.file.validate.ValidateResult;
import com.icip.framework.file.validate.Validator;

@SuppressWarnings("rawtypes")
public class RequestValidator implements Validator {

	@Override
	public boolean validateBody(Map body) {
		return true;
	}

	@Override
	public boolean validateHeader(Map header) {
		return true;
	}

	@Override
	public ValidateResult getResult() {
		return null;
	}

}
