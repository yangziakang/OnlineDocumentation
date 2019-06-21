package com.icip.framework.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.icip.framework.constants.GlobalConstants;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FileUtil {

	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	public static void upload(String basePath, String fileName, Map params) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		String jsonStr = JSONObject.toJSONString(params);
		upload(basePath, fileName, jsonStr);
	}

	public static void upload(String basePath, String fileName, String jsonStr) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		Assert.hasLength(jsonStr, "待写入参数不能为空");

		log.debug("-------------------------------- 写入的字符串 ： " + jsonStr + " -------------------------");
		File dir = new File(basePath);
		if (!dir.exists())
			dir.mkdirs();

		String filePath = basePath + fileName;
		log.debug("-------------------------------- 待写入文件路径 ： " + filePath + " -------------------------");
		File file = new File(filePath);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file);
		// Save as File
		ByteArrayInputStream bis = new ByteArrayInputStream(jsonStr.getBytes());
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		byte[] bb = new byte[1024];
		int n;
		while ((n = bis.read(bb)) != -1) {
			bos.write(bb, 0, n);
		}

		bos.close();
		bis.close();
		fos.close();
	}

	public static String read(String filePath) throws FileNotFoundException, IOException {
		File file = new File(filePath);
		return read(file);
	}

	public static String read(File file) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer content = new StringBuffer();
		while (br.ready()) {
			content.append((char) br.read());
		}
		br.close();
		fr.close();
		return content.toString();
	}

	public static Map getGroupList() {
		Map groupList = new HashMap();
		String filePath = PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH);

		File root = new File(filePath);

		if (!root.exists())
			return groupList;

		File[] versions = root.listFiles();// 版本
		for (File version : versions) {
			List list = new ArrayList();
			File[] groups = version.listFiles();// 分组
			for (File group : groups) {
				Map gm = new HashMap();
				File[] files = group.listFiles();
				gm.put("id", group.getName());
				gm.put("pId", "0");
				gm.put("name", group.getName());
				list.add(gm);
				for (File interfaceFile : files) {
					Map m = new HashMap();
					String name = interfaceFile.getName();
					m.put("id", name);
					m.put("pId", group.getName());
					m.put("name", name);
					list.add(m);
				}
			}
			groupList.put(version.getName(), list);
		}

		return groupList;
	}

	public static void delete(String filePath) {
		File file = new File(filePath);
		delete(file);
	}

	public static void delete(File file) {
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();
				return;
			}
			for (File f : files) {
				delete(f);
			}
			file.delete();
			File f = file.getParentFile();
			File[] temps = f.listFiles();
			if (temps == null || temps.length == 0)// 如果父级文件为空删除父级文件夹
				f.delete();
		}
	}

	public static String findInterface(String versionPath, String urlServiceCode) {
		File version = new File(versionPath);
		File[] groups = version.listFiles();
		if (groups == null)
			return null;
		for (File g : groups) {
			File[] it = g.listFiles();
			if (it == null)
				continue;
			for (File itName : it) {
				if (itName.getName().indexOf("[") < 0)
					continue;
				String temp = itName.getName().substring(0, itName.getName().indexOf("["));
				if (temp.equals(urlServiceCode)) {
					return itName.getPath();
				}
			}
		}

		return null;
	}

}
