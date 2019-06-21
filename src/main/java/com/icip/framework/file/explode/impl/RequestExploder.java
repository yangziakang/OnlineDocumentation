package com.icip.framework.file.explode.impl;

import com.icip.framework.file.explode.AbstractExploder;
import com.icip.framework.model.ExtRequestInfo;
import com.icip.framework.model.RequestInfo;

public class RequestExploder extends AbstractExploder {

	private ExtRequestInfo requestInfo;

	public RequestExploder(ExtRequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	@Override
	public RequestInfo getRequest() {
		return requestInfo.getRequest();
	}

}
