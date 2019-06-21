package com.icip.framework.file.holder.adapter;

import com.icip.framework.file.holder.FileHolder;
import com.icip.framework.file.holder.impl.JsonFileHodler;

public class FileHolderAdapter {

	public static FileHolder getHolder() {
		return new JsonFileHodler();
	}

}
