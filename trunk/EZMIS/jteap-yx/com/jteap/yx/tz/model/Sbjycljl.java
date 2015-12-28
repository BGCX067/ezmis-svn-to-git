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
 * 设备绝缘测量记录实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_SBJYCLJL")
public class Sbjycljl {

	// columns START

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "SJ")
	private java.util.Date sj;

	@Column(name = "SBMC")
	private java.lang.String sbmc;

	@Column(name = "CLXM")
	private java.lang.String clxm;

	@Column(name = "R15")
	private Double r15;

	@Column(name = "R60")
	private Double r60;

	@Column(name = "R1560")
	private Double r1560;

	@Column(name = "SYYB")
	private java.lang.String syyb;

	@Column(name = "TQ")
	private java.lang.String tq;

	@Column(name = "CLR")
	private java.lang.String clr;

	@Column(name = "JHR")
	private java.lang.String jhr;

	@Column(name = "BZ")
	private java.lang.String bz;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.util.Date getSj() {
		return this.sj;
	}

	public void setSj(java.util.Date value) {
		this.sj = value;
	}

	public java.lang.String getSbmc() {
		return this.sbmc;
	}

	public void setSbmc(java.lang.String value) {
		this.sbmc = value;
	}

	public java.lang.String getClxm() {
		return this.clxm;
	}

	public void setClxm(java.lang.String value) {
		this.clxm = value;
	}

	public Double getR15() {
		return r15;
	}

	public void setR15(Double r15) {
		this.r15 = r15;
	}

	public Double getR60() {
		return r60;
	}

	public void setR60(Double r60) {
		this.r60 = r60;
	}

	public Double getR1560() {
		return r1560;
	}

	public void setR1560(Double r1560) {
		this.r1560 = r1560;
	}

	public java.lang.String getSyyb() {
		return this.syyb;
	}

	public void setSyyb(java.lang.String value) {
		this.syyb = value;
	}

	public java.lang.String getTq() {
		return this.tq;
	}

	public void setTq(java.lang.String value) {
		this.tq = value;
	}

	public java.lang.String getClr() {
		return this.clr;
	}

	public void setClr(java.lang.String value) {
		this.clr = value;
	}

	public java.lang.String getJhr() {
		return this.jhr;
	}

	public void setJhr(java.lang.String value) {
		this.jhr = value;
	}

	public java.lang.String getBz() {
		return this.bz;
	}

	public void setBz(java.lang.String value) {
		this.bz = value;
	}

}
