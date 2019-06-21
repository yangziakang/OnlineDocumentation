package com.icip.framework.file.resolve.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.icip.framework.constants.Field;
import com.icip.framework.exception.CustomException;
import com.icip.framework.file.resolve.FileResolver;
import com.icip.framework.util.StringUtil;

@SuppressWarnings("rawtypes")
public class JsonResolver implements FileResolver{

	@SuppressWarnings("unchecked")
	@Override
	public Map resolveDocument(Map json) throws CustomException {
		Set keys = json.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			Map column = (Map)json.get(key);
			String type = (String)column.get("type");
			if(StringUtil.isEmpty(type))
				throw new CustomException(key + "：类型不能为空");
			type = type.toLowerCase();
			if(type.indexOf(Field.TYPE_ARRAY) > -1) {//是否为数组，数组只取第一个
				if(Field.TYPE_ARRAY_OBJECT.equals(type)) {
					List list = new ArrayList();
					List value = (List)column.get(Field.VALUE);
					if(value == null || value.isEmpty())
						throw new CustomException(key + "：值不能为空");
					Map value_0 = (Map)value.get(0);
					list.add(value_0);
					resolveDocument(value_0);
					column.put("value", list);
				}
			}
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map resolveResponse(Map json) throws CustomException {
		Map result = new HashMap();
		Set keys = json.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			if(StringUtil.isEmpty(key))
				throw new CustomException("字段名称不能为空");
			Map column = (Map)json.get(key);
			String type = (String) column.get(Field.TYPE);
			if(StringUtil.isEmpty(type))
				throw new CustomException(key + "：类型不能为空");
			type = type.toLowerCase();
			if(Field.TYPE_ARRAY_OBJECT.equals(type)) {
				List values = (List)column.get(Field.VALUE);
				if(values != null && !values.isEmpty()) {
					List list = new ArrayList();
					for(Object o : values) {
						list.add(resolveResponse((Map)o));
					}
					result.put(key, list);
				} else {
					result.put(key, null);
				}
			} else {
				result.put(key, column.get(Field.VALUE));
			}
		}
		
		return result;
	}

}
