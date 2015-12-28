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
 * 电气设备保护记录实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_DQSBBHJL")
public class Dqsbbhjl{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "JZ")	
	private java.lang.String jz;
	
	
	@Column(name = "BHMC")	
	private java.lang.String bhmc;
	
	
	@Column(name = "TYSJ")	
	private java.util.Date tysj;
	
	
	@Column(name = "TYYY")	
	private java.lang.String tyyy;
	
	
	@Column(name = "TYZXR")	
	private java.lang.String tyzxr;
	
	
	@Column(name = "TYZBY")	
	private java.lang.String tyzby;
	
	
	@Column(name = "JYSJ")	
	private java.util.Date jysj;
	
	
	@Column(name = "JYYY")	
	private java.lang.String jyyy;
	
	
	@Column(name = "JYZXR")	
	private java.lang.String jyzxr;
	
	
	@Column(name = "JYZBY")	
	private java.lang.String jyzby;
	

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getJz() {
		return this.jz;
	}
	
	public void setJz(java.lang.String value) {
		this.jz = value;
	}
	public java.lang.String getBhmc() {
		return this.bhmc;
	}
	
	public void setBhmc(java.lang.String value) {
		this.bhmc = value;
	}
	public java.util.Date getTysj() {
		return this.tysj;
	}
	
	public void setTysj(java.util.Date value) {
		this.tysj = value;
	}
	public java.lang.String getTyyy() {
		return this.tyyy;
	}
	
	public void setTyyy(java.lang.String value) {
		this.tyyy = value;
	}
	public java.lang.String getTyzxr() {
		return this.tyzxr;
	}
	
	public void setTyzxr(java.lang.String value) {
		this.tyzxr = value;
	}
	public java.lang.String getTyzby() {
		return this.tyzby;
	}
	
	public void setTyzby(java.lang.String value) {
		this.tyzby = value;
	}
	public java.util.Date getJysj() {
		return this.jysj;
	}
	
	public void setJysj(java.util.Date value) {
		this.jysj = value;
	}
	public java.lang.String getJyyy() {
		return this.jyyy;
	}
	
	public void setJyyy(java.lang.String value) {
		this.jyyy = value;
	}
	public java.lang.String getJyzxr() {
		return this.jyzxr;
	}
	
	public void setJyzxr(java.lang.String value) {
		this.jyzxr = value;
	}
	public java.lang.String getJyzby() {
		return this.jyzby;
	}
	
	public void setJyzby(java.lang.String value) {
		this.jyzby = value;
	}
	
	
}

