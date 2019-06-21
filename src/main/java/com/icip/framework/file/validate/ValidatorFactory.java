package com.icip.framework.file.validate;

import com.icip.framework.file.validate.adapter.ValidatorAdapter;

public class ValidatorFactory {

	public static Validator getInstance() {
		return ValidatorAdapter.getValidator();
	}
	
	public static Validator getDocumentInstance() {
		return ValidatorAdapter.getDocumentValidator();
	}

}
