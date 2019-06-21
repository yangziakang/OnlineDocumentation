package com.icip.framework.file.explode;

import com.icip.framework.model.RequestInfo;
import com.icip.framework.model.ResponseInfo;
import com.icip.framework.model.ServiceInfo;

public interface Exploder {

	RequestInfo getRequest(); 
	
	ResponseInfo getResponse(); 
	
	ServiceInfo getServiceInfo();

}
