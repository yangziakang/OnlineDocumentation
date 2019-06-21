package com.icip.framework.file.creator;

import com.icip.framework.file.creator.adapter.FileCreatorAdapter;

public class FileCreatorFactory {

	public static FileCreator getInstance() {
		return FileCreatorAdapter.getFileCreator();
	}

}
