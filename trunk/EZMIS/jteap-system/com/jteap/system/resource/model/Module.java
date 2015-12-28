package com.jteap.system.resource.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 模块实体
 * @author tantyou
 * @date 2008-1-17
 */
@Entity
@Table(name = "tb_sys_module")
public class Module extends Resource {

	
	//模块链接
	@Column(name = "link")
	private String link;

	//图标
	@Column(name = "icon")
	private String icon;

	//模块类型
	@Column(name = "MTYPE")
	private String moduleType;

	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

}
