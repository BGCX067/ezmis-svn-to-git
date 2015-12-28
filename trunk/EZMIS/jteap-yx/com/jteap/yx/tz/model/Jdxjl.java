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
 * 接地线记录实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_JDXJL")
public class Jdxjl {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "JDXBH")
	private java.lang.String jdxbh;

	@Column(name = "JZ")
	private java.lang.String jz;

	@Column(name = "ZSDD")
	private java.lang.String zsdd;

	@Column(name = "ZSSJ")
	private java.util.Date zssj;

	@Column(name = "ZSR")
	private java.lang.String zsr;

	@Column(name = "CCSJ")
	private java.util.Date ccsj;

	@Column(name = "CCR")
	private java.lang.String ccr;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getJdxbh() {
		return this.jdxbh;
	}

	public void setJdxbh(java.lang.String value) {
		this.jdxbh = value;
	}

	public java.lang.String getJz() {
		return this.jz;
	}

	public void setJz(java.lang.String value) {
		this.jz = value;
	}

	public java.lang.String getZsdd() {
		return this.zsdd;
	}

	public void setZsdd(java.lang.String value) {
		this.zsdd = value;
	}

	public java.util.Date getZssj() {
		return this.zssj;
	}

	public void setZssj(java.util.Date value) {
		this.zssj = value;
	}

	public java.lang.String getZsr() {
		return this.zsr;
	}

	public void setZsr(java.lang.String value) {
		this.zsr = value;
	}

	public java.util.Date getCcsj() {
		return this.ccsj;
	}

	public void setCcsj(java.util.Date value) {
		this.ccsj = value;
	}

	public java.lang.String getCcr() {
		return this.ccr;
	}

	public void setCcr(java.lang.String value) {
		this.ccr = value;
	}

}
