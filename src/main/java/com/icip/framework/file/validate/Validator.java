package com.icip.framework.file.validate;

import java.util.Map;

public interface Validator {
	boolean validateBody(Map body);
	boolean validateHeader(Map header);
	ValidateResult getResult();
}
