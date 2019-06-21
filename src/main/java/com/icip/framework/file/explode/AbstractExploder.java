package com.icip.framework.file.explode;

import com.icip.framework.model.RequestInfo;
import com.icip.framework.model.ResponseInfo;
import com.icip.framework.model.ServiceInfo;

public abstract class AbstractExploder implements Exploder {

	@Override
	public RequestInfo getRequest() {
		return null;
	}

	@Override
	public ResponseInfo getResponse() {
		return null;
	}

	@Override
	public ServiceInfo getServiceInfo() {
		return null;
	}

}
