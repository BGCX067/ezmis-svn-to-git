package com.jteap.system.resource.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 操作
 * @author tantyou
 * @date 2008-1-17
 */
@Entity
@Table(name="tb_sys_operation")
public class Operation extends Resource{
	//按钮的文本
	@Column(name="sn")
	private String sn;
	
	//图标
	@Column(name="icon")
	private String icon;
	
	//按钮提示
	@Column(name="tip")
	private String tip;
	
	//操作按钮是否显示文本
	@Column(name="show_text")
	private boolean showText;

	//是否为管理员操作
	@Column(name="admin_op")
	private boolean adminOp;

	
	public boolean isAdminOp() {
		return adminOp;
	}

	public void setAdminOp(boolean adminOp) {
		this.adminOp = adminOp;
	}

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	

	
}
