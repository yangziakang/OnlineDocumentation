package com.icip.framework.file.creator.adapter;

import com.icip.framework.file.creator.FileCreator;
import com.icip.framework.file.creator.impl.JsonFileCreator;

public class FileCreatorAdapter {

	public static FileCreator getFileCreator() {
		return new JsonFileCreator();
	}

}
