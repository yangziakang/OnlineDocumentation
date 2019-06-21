package com.icip.framework.file.holder.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.icip.framework.file.holder.AbstractFileHolder;
import com.icip.framework.util.FileUtil;
import com.icip.framework.util.StringUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JsonFileHodler extends AbstractFileHolder {
	
	private static final Logger log = LoggerFactory.getLogger(JsonFileHodler.class);

	@Override
	public Map getResponse(String filePath) {
		Map result = new HashMap();
		File file = new File(filePath);
	  try {
			String content = FileUtil.read(file);
			if(StringUtil.isEmpty(content)) {
				result.put("result", "json文件内容为空");
				return result;
			}
			return JSONObject.parseObject(content);
		} catch (FileNotFoundException e) {
			log.debug(e.getMessage(), e);
			result.put("result", "访问的接口文件不存在，请先生成");
			return result;
		} catch (IOException e) {
			log.debug(e.getMessage(), e);
			result.put("result", "文件读取错误");
			return result;
		}
	}


}
