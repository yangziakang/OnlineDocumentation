package com.icip.framework.file.explode;

import com.icip.framework.exception.CustomException;
import com.icip.framework.file.explode.adapter.ExploderAdapter;
import com.icip.framework.model.ExtRequestInfo;

@SuppressWarnings("rawtypes")
public class ExploderFactory {

	public static Exploder getDocumentInstance(String jsonStr) throws CustomException {
		return ExploderAdapter.getDocumentExploder(jsonStr);
	}

	public static Exploder getRequestInstance(ExtRequestInfo requestInfo) {
		return ExploderAdapter.getRequestExploder(requestInfo);
	}

}
