/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.jjb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_YX_JJB_PAIBAN")
/**
 * 排班实体
 */
public class PaiBan{

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;
	
	@Column(name = "BC")	
	private java.lang.String bc;
	
	@Column(name = "ZB")	
	private java.lang.String zb;
	
	@Column(name = "CSSJ")	
	private java.util.Date cssj;
	
	public java.lang.String getId() {
		return this.id;
	}
	
	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getBc() {
		return this.bc;
	}
	
	public void setBc(java.lang.String value) {
		this.bc = value;
	}
	public java.lang.String getZb() {
		return this.zb;
	}
	
	public void setZb(java.lang.String value) {
		this.zb = value;
	}
	public java.util.Date getCssj() {
		return this.cssj;
	}
	
	public void setCssj(java.util.Date value) {
		this.cssj = value;
	}
	
}
