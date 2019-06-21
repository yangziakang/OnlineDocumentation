package com.icip.framework.file.resolve;

import com.icip.framework.file.resolve.adapter.FileResolverAdapter;

public class FileResolverFactory {

	public static FileResolver getInstance() {
		return FileResolverAdapter.getResolver();
	}

}
