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
 * 漏氢量记录(600MW)实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_LQLJL600")
public class Lqljl600 {

	// columns START
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "KSSJ")
	private java.util.Date kssj;

	@Column(name = "KSQYP1")
	private double ksqyp1;

	@Column(name = "QWXT1")
	private double qwxt1;

	@Column(name = "QWXT2")
	private double qwxt2;

	@Column(name = "QWDT1")
	private double qwdt1;

	@Column(name = "JSSJ")
	private java.util.Date jssj;

	@Column(name = "JSQYP2")
	private double jsqyp2;

	@Column(name = "QWDT2")
	private double qwdt2;

	@Column(name = "YXSJ")
	private double yxsj;

	@Column(name = "LQL")
	private double lql;

	@Column(name = "LQLV")
	private double lqlv;

	@Column(name = "TXR1")
	private java.lang.String txr1;

	@Column(name = "TXR2")
	private java.lang.String txr2;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.util.Date getKssj() {
		return this.kssj;
	}

	public void setKssj(java.util.Date value) {
		this.kssj = value;
	}

	public double getKsqyp1() {
		return this.ksqyp1;
	}

	public void setKsqyp1(double value) {
		this.ksqyp1 = value;
	}

	public double getQwxt1() {
		return this.qwxt1;
	}

	public void setQwxt1(double value) {
		this.qwxt1 = value;
	}

	public double getQwxt2() {
		return this.qwxt2;
	}

	public void setQwxt2(double value) {
		this.qwxt2 = value;
	}

	public double getQwdt1() {
		return this.qwdt1;
	}

	public void setQwdt1(double value) {
		this.qwdt1 = value;
	}

	public java.util.Date getJssj() {
		return this.jssj;
	}

	public void setJssj(java.util.Date value) {
		this.jssj = value;
	}

	public double getJsqyp2() {
		return this.jsqyp2;
	}

	public void setJsqyp2(double value) {
		this.jsqyp2 = value;
	}

	public double getQwdt2() {
		return this.qwdt2;
	}

	public void setQwdt2(double value) {
		this.qwdt2 = value;
	}

	public double getYxsj() {
		return this.yxsj;
	}

	public void setYxsj(double value) {
		this.yxsj = value;
	}

	public double getLql() {
		return this.lql;
	}

	public void setLql(double value) {
		this.lql = value;
	}

	public double getLqlv() {
		return this.lqlv;
	}

	public void setLqlv(double value) {
		this.lqlv = value;
	}

	public java.lang.String getTxr1() {
		return this.txr1;
	}

	public void setTxr1(java.lang.String value) {
		this.txr1 = value;
	}

	public java.lang.String getTxr2() {
		return this.txr2;
	}

	public void setTxr2(java.lang.String value) {
		this.txr2 = value;
	}

}
