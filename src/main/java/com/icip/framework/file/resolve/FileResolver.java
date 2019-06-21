package com.icip.framework.file.resolve;

import java.util.Map;

import com.icip.framework.exception.CustomException;

@SuppressWarnings("rawtypes")
public interface FileResolver {

	Map resolveDocument(Map json) throws CustomException;

	Map resolveResponse(Map json) throws CustomException;
}
