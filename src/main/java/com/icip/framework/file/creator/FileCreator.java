package com.icip.framework.file.creator;

import java.util.Map;

import com.icip.framework.exception.CustomException;
import com.icip.framework.model.Information;

@SuppressWarnings("rawtypes")
public interface FileCreator {

	Map createRequestDocument(Information requestInfo) throws CustomException ;

	Map createResponseDocument(Information responseInfo) throws CustomException ;
	
	Map createResponseMap(Information responseInfo) throws CustomException ;

}
