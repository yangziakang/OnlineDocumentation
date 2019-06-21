package com.icip.framework.file.resolve.adapter;

import com.icip.framework.file.resolve.FileResolver;
import com.icip.framework.file.resolve.impl.JsonResolver;

public class FileResolverAdapter {

	public static FileResolver getResolver() {
		return new JsonResolver();
	}

}
