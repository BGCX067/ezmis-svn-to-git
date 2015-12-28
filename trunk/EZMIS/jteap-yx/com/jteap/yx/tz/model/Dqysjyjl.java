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
 * 电气钥匙借用记录实体bean
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
@Entity
@Table(name = "TB_YX_TZ_DQYSJYJL")
public class Dqysjyjl{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "JYSJ")	
	private java.util.Date jysj;
	
	
	@Column(name = "JYYS")	
	private java.lang.String jyys;
	
	
	@Column(name = "JYYY")	
	private java.lang.String jyyy;
	
	
	@Column(name = "JYR")	
	private java.lang.String jyr;
	
	
	@Column(name = "JCZBY")	
	private java.lang.String jczby;
	
	
	@Column(name = "JCZZ")	
	private java.lang.String jczz;
	
	
	@Column(name = "GHSJ")	
	private java.util.Date ghsj;
	
	
	@Column(name = "GHR")	
	private java.lang.String ghr;
	
	
	@Column(name = "SHZBY")	
	private java.lang.String shzby;
	

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.util.Date getJysj() {
		return this.jysj;
	}
	
	public void setJysj(java.util.Date value) {
		this.jysj = value;
	}
	public java.lang.String getJyys() {
		return this.jyys;
	}
	
	public void setJyys(java.lang.String value) {
		this.jyys = value;
	}
	public java.lang.String getJyyy() {
		return this.jyyy;
	}
	
	public void setJyyy(java.lang.String value) {
		this.jyyy = value;
	}
	public java.lang.String getJyr() {
		return this.jyr;
	}
	
	public void setJyr(java.lang.String value) {
		this.jyr = value;
	}
	public java.lang.String getJczby() {
		return this.jczby;
	}
	
	public void setJczby(java.lang.String value) {
		this.jczby = value;
	}
	public java.lang.String getJczz() {
		return this.jczz;
	}
	
	public void setJczz(java.lang.String value) {
		this.jczz = value;
	}
	public java.util.Date getGhsj() {
		return this.ghsj;
	}
	
	public void setGhsj(java.util.Date value) {
		this.ghsj = value;
	}
	public java.lang.String getGhr() {
		return this.ghr;
	}
	
	public void setGhr(java.lang.String value) {
		this.ghr = value;
	}
	public java.lang.String getShzby() {
		return this.shzby;
	}
	
	public void setShzby(java.lang.String value) {
		this.shzby = value;
	}
	
	
}

