package com.icip.framework.file.validate.adapter;

import com.icip.framework.file.validate.Validator;
import com.icip.framework.file.validate.impl.NullValidator;

public class ValidatorAdapter {

	public static Validator getValidator() {
		return new NullValidator();
	}

	public static Validator getDocumentValidator() {
		return new NullValidator();
	}

}
