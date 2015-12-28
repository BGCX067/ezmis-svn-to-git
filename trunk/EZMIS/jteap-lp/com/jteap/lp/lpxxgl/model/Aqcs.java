/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.lp.lpxxgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * 安全措施实体表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_LP_XXPZ_AQCS")
public class Aqcs {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	// 安全措施分类
	@ManyToOne
	@JoinColumn(name = "FLID")
	@LazyToOne(LazyToOneOption.PROXY)
	private AqcsCatalog aqcsCatalog;

	// 措施名称
	@Column(name = "CSMC")
	private java.lang.String csmc;

	// 措施内容
	@Column(name = "CSNR")
	private java.lang.String csnr;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public AqcsCatalog getAqcsCatalog() {
		return aqcsCatalog;
	}

	public void setAqcsCatalog(AqcsCatalog aqcsCatalog) {
		this.aqcsCatalog = aqcsCatalog;
	}

	public java.lang.String getCsmc() {
		return this.csmc;
	}

	public void setCsmc(java.lang.String value) {
		this.csmc = value;
	}

	public java.lang.String getCsnr() {
		return csnr;
	}

	public void setCsnr(java.lang.String csnr) {
		this.csnr = csnr;
	}

}
