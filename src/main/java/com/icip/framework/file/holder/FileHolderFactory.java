package com.icip.framework.file.holder;

import com.icip.framework.file.holder.adapter.FileHolderAdapter;

public class FileHolderFactory {

	public static FileHolder getInstance() {
		return FileHolderAdapter.getHolder();
	}
}
