/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.yx.tz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 保护及自动装置动作记录实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_BHZDDZJL")
public class Bhzddzjl{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "SBMC")	
	private java.lang.String sbmc;
	
	
	@Column(name = "DZSJ")	
	private java.util.Date dzsj;
	
	
	@Column(name = "BHMC")	
	private java.lang.String bhmc;
	
	
	@Column(name = "GZPXH")	
	private java.lang.String gzpxh;
	
	
	@Column(name = "JCR")	
	private java.lang.String jcr;
	
	
	@Column(name = "FGR")	
	private java.lang.String fgr;
	
	
	@Column(name = "BZ")	
	private java.lang.String bz;
	

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getSbmc() {
		return this.sbmc;
	}
	
	public void setSbmc(java.lang.String value) {
		this.sbmc = value;
	}
	public java.util.Date getDzsj() {
		return this.dzsj;
	}
	
	public void setDzsj(java.util.Date value) {
		this.dzsj = value;
	}
	public java.lang.String getBhmc() {
		return this.bhmc;
	}
	
	public void setBhmc(java.lang.String value) {
		this.bhmc = value;
	}
	public java.lang.String getGzpxh() {
		return this.gzpxh;
	}
	
	public void setGzpxh(java.lang.String value) {
		this.gzpxh = value;
	}
	public java.lang.String getJcr() {
		return this.jcr;
	}
	
	public void setJcr(java.lang.String value) {
		this.jcr = value;
	}
	public java.lang.String getFgr() {
		return this.fgr;
	}
	
	public void setFgr(java.lang.String value) {
		this.fgr = value;
	}
	public java.lang.String getBz() {
		return this.bz;
	}
	
	public void setBz(java.lang.String value) {
		this.bz = value;
	}
	
	
}

