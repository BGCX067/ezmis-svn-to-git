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
 * 开关分合闸记录实体Bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_KGFHZ")
public class Kgfhzjl {

	// columns START
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "JZLX")
	private java.lang.String jzlx;

	@Column(name = "KGMC")
	private java.lang.String kgmc;

	@Column(name = "HZSJ")
	private java.util.Date hzsj;

	@Column(name = "HZYY")
	private java.lang.String hzyy;

	@Column(name = "HZJLR")
	private java.lang.String hzjlr;

	@Column(name = "FZSJ")
	private java.util.Date fzsj;

	@Column(name = "FZYY")
	private java.lang.String fzyy;

	@Column(name = "FZJLR")
	private java.lang.String fzjlr;

	@Column(name = "YXSJ")
	private Integer yxsj;

	@Column(name = "DZCS")
	private Integer dzcs;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getJzlx() {
		return this.jzlx;
	}

	public void setJzlx(java.lang.String value) {
		this.jzlx = value;
	}

	public java.lang.String getKgmc() {
		return this.kgmc;
	}

	public void setKgmc(java.lang.String value) {
		this.kgmc = value;
	}

	public java.util.Date getHzsj() {
		return this.hzsj;
	}

	public void setHzsj(java.util.Date value) {
		this.hzsj = value;
	}

	public java.lang.String getHzyy() {
		return this.hzyy;
	}

	public void setHzyy(java.lang.String value) {
		this.hzyy = value;
	}

	public java.lang.String getHzjlr() {
		return this.hzjlr;
	}

	public void setHzjlr(java.lang.String value) {
		this.hzjlr = value;
	}

	public java.util.Date getFzsj() {
		return this.fzsj;
	}

	public void setFzsj(java.util.Date value) {
		this.fzsj = value;
	}

	public java.lang.String getFzyy() {
		return this.fzyy;
	}

	public void setFzyy(java.lang.String value) {
		this.fzyy = value;
	}

	public java.lang.String getFzjlr() {
		return this.fzjlr;
	}

	public void setFzjlr(java.lang.String value) {
		this.fzjlr = value;
	}

	public Integer getYxsj() {
		return yxsj;
	}

	public void setYxsj(Integer yxsj) {
		this.yxsj = yxsj;
	}

	public Integer getDzcs() {
		return dzcs;
	}

	public void setDzcs(Integer dzcs) {
		this.dzcs = dzcs;
	}

}
