package com.icip.framework.model;

import com.icip.framework.util.StringUtil;

public class ServiceInfo {
	
	private String description;
	private String version;
	private String group;
	private String interfaceName;
	
	public ServiceInfo(String version, String group, String description, String interfaceName) {
		this.version = version;
		this.group = group;
		this.description = description;
		this.interfaceName = interfaceName;
	}
	public String getDescription() {
		return description;
	}
	public String getVersion() {
		return version;
	}
	public String getGroup() {
		return group;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	public boolean isMatch(ServiceInfo si) {
		if(si == null || StringUtil.isEmpty(si.getVersion()) || StringUtil.isEmpty(si.getGroup()))
		 return false;
		if(si.getVersion().equals(this.version) && si.getGroup().equals(this.group)) {
			return true;
		}
		
		return false;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

}
