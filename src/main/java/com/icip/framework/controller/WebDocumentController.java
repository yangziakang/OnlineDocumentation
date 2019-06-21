package com.icip.framework.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.icip.framework.constants.GlobalConstants;
import com.icip.framework.constants.Setting;
import com.icip.framework.exception.CustomException;
import com.icip.framework.file.creator.FileCreator;
import com.icip.framework.file.creator.FileCreatorFactory;
import com.icip.framework.file.explode.Exploder;
import com.icip.framework.file.explode.ExploderFactory;
import com.icip.framework.file.holder.FileHolder;
import com.icip.framework.file.holder.FileHolderFactory;
import com.icip.framework.file.holder.impl.JsonFileHodler;
import com.icip.framework.model.ExtRequestInfo;
import com.icip.framework.model.RequestInfo;
import com.icip.framework.model.ResponseInfo;
import com.icip.framework.model.ServiceInfo;
import com.icip.framework.util.Encrypt;
import com.icip.framework.util.FileUtil;
import com.icip.framework.util.ParamUtils;
import com.icip.framework.util.PropertyConfigure;
import com.icip.framework.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping
public class WebDocumentController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(WebDocumentController.class);

	@RequestMapping("/welcome")
	public ModelAndView showEditor(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("editor");
		mav.addObject("groupList", FileUtil.getGroupList());
		mav.addObject("isEncode", Setting.isEncode());
		return mav;
	}
	
	@RequestMapping("/showResponse")
	public ModelAndView showResponse(HttpServletRequest request, HttpServletResponse response) {
		String version = ParamUtils.getParameter(request, "version");
		String group = ParamUtils.getParameter(request, "group");
		String interfaceName = ParamUtils.getParameter(request, "interfaceName");
		String description = ParamUtils.getParameter(request, "description");
	/*	
		try {
			description = new String(description.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}*/
		
		String folder = interfaceName + "[" + description + "]";
		
		StringBuffer filePath = new StringBuffer();
		filePath.append(PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH)).append(version).append(File.separator)
				.append(group).append(File.separator).append(folder).append(File.separator).append(GlobalConstants.RESPONSE_FILE_NAME);
		
		log.debug("---------------------------------- 查看response路径:" + filePath + " ------------------------------");
		
		JsonFileHodler hodler = new JsonFileHodler();
		Map result = null;
		try {
			result = hodler.getResponse(new String(filePath.toString().getBytes("iso8859-1"),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("respDialog");
		mav.addObject("version", version);
		mav.addObject("group", group);
		mav.addObject("jsonStr", JSONObject.toJSONString(result));
		mav.addObject("interfaceName", interfaceName);
		mav.addObject("description", description);
		return mav;
	}

	@ResponseBody
	@RequestMapping("/createDocument")
	public Object createDocument(HttpServletRequest request, HttpServletResponse resp) {
		String jsonStr = ParamUtils.getParameter(request, "jsonStr");
		Map result = new HashMap();
		log.debug("---------------------------------- 文档生成json字符串:" + jsonStr + " ------------------------------");

		if (StringUtil.isEmpty(jsonStr)) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "jsonStr 不能为空");
		}

		Exploder exploder = null;
		try {
			exploder = ExploderFactory.getDocumentInstance(jsonStr);
		} catch (CustomException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", e.getMessage());
			return result;
		}
		RequestInfo requestInfo = exploder.getRequest();
		ResponseInfo responseInfo = exploder.getResponse();
		ServiceInfo serviceInfo = exploder.getServiceInfo();
		// 文件生成的路径
		String basePath = new StringBuffer(PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH)).append(serviceInfo.getVersion()).append(File.separator)
				.append(serviceInfo.getGroup()).append(File.separator).append(serviceInfo.getInterfaceName()).append(File.separator).toString();

		FileCreator fileCreator = FileCreatorFactory.getInstance();
		Map requestDocument = null;
		Map responseDocument = null;
//		Map responseMap = null;

		try {
			requestDocument = fileCreator.createRequestDocument(requestInfo);
			responseDocument = fileCreator.createResponseDocument(responseInfo);
//			responseMap = fileCreator.createResponseMap(responseInfo);
		} catch (CustomException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", e.getMessage());
			return result;
		}

		Map document = new HashMap();
		document.put("request", requestDocument);
		document.put("response", responseDocument);
		document.put("info", serviceInfo);
		try {
			FileUtil.upload(basePath, GlobalConstants.DOCUMENT_FILE_NAME, document);// 写入文档
//			FileUtil.upload(basePath, GlobalConstants.RESPONSE_FILE_NAME, responseMap);// 写入responseJson
		} catch (FileNotFoundException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "文件不存在 ：" + e.getMessage());
			return result;
		} catch (UnsupportedEncodingException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "格式化字符编码失败 ：" + e.getMessage());
			return result;
		} catch (IOException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "文件写入失败 ：" + e.getMessage());
			return result;
		}
		result.put("document", document);// 返回文档格式
		result.put("code", GlobalConstants.SUCCESS);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/createResponse")
	public Object createResponse(HttpServletRequest request, HttpServletResponse resp) {
		String jsonStr = ParamUtils.getParameter(request, "jsonStr");
		String version = ParamUtils.getParameter(request, "version");
		String group = ParamUtils.getParameter(request, "group");
		String interfaceName = ParamUtils.getParameter(request, "interfaceName");
		String description = ParamUtils.getParameter(request, "description");
		
		String folder = interfaceName + "[" + description + "]";
		
		Map result = new HashMap();
		log.debug("---------------------- response json字符串:" + jsonStr + " ------------------------------");

		if (StringUtil.isEmpty(jsonStr)) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "jsonStr 不能为空");
		}
		// 文件生成的路径
		String basePath = new StringBuffer(PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH)).append(version).append(File.separator)
				.append(group).append(File.separator).append(folder).append(File.separator).toString();
		
		try {
			FileUtil.upload(basePath, GlobalConstants.RESPONSE_FILE_NAME, jsonStr);// 写入文档
		} catch (FileNotFoundException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "文件不存在 ：" + e.getMessage());
			return result;
		} catch (UnsupportedEncodingException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "格式化字符编码失败 ：" + e.getMessage());
			return result;
		} catch (IOException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "文件写入失败 ：" + e.getMessage());
			return result;
		}
		result.put("code", GlobalConstants.SUCCESS);
		return result;
	}

	@ResponseBody
	@RequestMapping("/findDoc")
	public Object findDocument(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
		Map result = new HashMap();

		String version = ParamUtils.getParameter(req, "version");
		String group = ParamUtils.getParameter(req, "group");
//		String interfaceName = ParamUtils.getParameter(req, "interfaceName");
		String interfaceName = URLDecoder.decode(req.getParameter("interfaceName"),"utf-8");
		// 文件生成的路径
		String filePath = new StringBuffer(PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH))
														.append(version).append(File.separator)
														.append(group).append(File.separator)
														.append(interfaceName).append(File.separator)
														.append(GlobalConstants.DOCUMENT_FILE_NAME).toString();
		log.debug("----------------------- 查找的文档文件路径：" + filePath + " --------------------------");
		try {
			String jsonStr = FileUtil.read(filePath);
			Map document = JSONObject.parseObject(jsonStr);
			Map all = backToAll(document);
			result.put("document", document);
			result.put("all", all);
			result.put("code", GlobalConstants.SUCCESS);
		} catch (FileNotFoundException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "文件不存在 ：" + e.getMessage());
			return result;
		} catch (IOException e) {
			result.put("code", GlobalConstants.FAILED);
			result.put("message", "文件读取失败 ：" + e.getMessage());
			return result;
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/deleteDoc")
	public Object deleteDocument(HttpServletRequest req, HttpServletResponse resp) {
		Map result = new HashMap();

		String version = ParamUtils.getParameter(req, "version");
		String group = ParamUtils.getParameter(req, "group");
		String interfaceName = ParamUtils.getParameter(req, "interfaceName");

		// 文件生成的路径
		String filePath = new StringBuffer(PropertyConfigure.getString(GlobalConstants.FILE_ROOT_PATH))
														.append(version).append(File.separator)
														.append(group).append(File.separator)
														.append(interfaceName).append(File.separator).toString();
		log.debug("----------------------- 删除的文档文件路径：" + filePath + " --------------------------");
		FileUtil.delete(filePath);
		result.put("code", GlobalConstants.SUCCESS);
		return result;
	}
	
	private Map backToAll(Map document) {
		Object responseBody = document.get("response");
		Object requestBody = document.get("request");
		Map info = (Map)document.get("info");
		
		String description = info.get("description") == null ? null : info.get("description").toString();
		String group = info.get("group") == null ? null : info.get("group").toString();
		String version = info.get("version") == null ? null : info.get("version").toString();
		String interfaceName = info.get("interfaceName") == null ? null : info.get("interfaceName").toString();
		
		if(StringUtil.isNotEmpty(interfaceName)) {
			interfaceName = interfaceName.substring(0, interfaceName.indexOf("["));
		}
		
		Map reqMap = new HashMap();
		Map respMap = new HashMap();
		Map interfaceMap = new HashMap();
		reqMap.put("requestBody", requestBody);
		respMap.put("responseBody", responseBody);
		
		interfaceMap.put("request", reqMap);
		interfaceMap.put("response", respMap);
		
		Map all = new HashMap();
		all.put(interfaceName, interfaceMap);
		all.put("description", description);
		all.put("group", group);
		all.put("version", version);
		
		return all;
	}
	
	@ResponseBody
	@RequestMapping("/*")
	public Object getJson(@RequestBody ExtRequestInfo info, HttpServletRequest request, HttpServletResponse resp) {
		Map result = new HashMap();
		String uri = request.getRequestURI();
		if (uri.indexOf("/") < 0) {
			result.put("result", "请输入正确的访问路径");
			result.put("code", GlobalConstants.FAILED);
			return result;
		}

		String urlServiceCode = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
		if (StringUtil.isEmpty(urlServiceCode)) {
			result.put("result", "请输入正确的访问路径");
			result.put("code", GlobalConstants.FAILED);
			return result;
		}

		if (info == null) {
			result.put("result", "请求内容不能为空");
			result.put("code", GlobalConstants.FAILED);
			return result;
		}

		Exploder exploder = ExploderFactory.getRequestInstance(info);

		RequestInfo requestInfo = exploder.getRequest();

		FileHolder fileHodler = FileHolderFactory.getInstance();

		try {
			result = fileHodler.findResponse(urlServiceCode, requestInfo);
			
			if(Setting.isEncode()) {
				String jsonStr = JSONObject.toJSONString(result);
				jsonStr = Encrypt.encryption(jsonStr);
				return jsonStr;
			}
		} catch (CustomException e) {
			result.put("result", e.getMessage());
			result.put("code", GlobalConstants.FAILED);
			return result;
		}

		return result;
	}
	
	@ResponseBody
	@RequestMapping("/setting")
	public Object setting(HttpServletRequest request, HttpServletResponse resp) {
		Map result = new HashMap();
		boolean isEncode = ParamUtils.getBooleanParameter(request, "isEncode", false);
		Setting.setEncode(isEncode);
		result.put("code", "0");
		return result;
	}
}
