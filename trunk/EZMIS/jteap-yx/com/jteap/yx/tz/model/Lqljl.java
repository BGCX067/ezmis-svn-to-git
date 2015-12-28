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
 * 漏氢量记录实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_LQLJL")
public class Lqljl {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "KSSJ")
	private java.util.Date kssj;

	@Column(name = "KSQY")
	private Double ksqy;

	@Column(name = "KSQW")
	private Double ksqw;

	@Column(name = "JSSJ")
	private java.util.Date jssj;

	@Column(name = "JSQY")
	private Double jsqy;

	@Column(name = "JSQW")
	private Double jsqw;

	@Column(name = "YXSJ")
	private Double yxsj;

	@Column(name = "LQL")
	private Double lql;

	@Column(name = "LQLV")
	private Double lqlv;

	@Column(name = "TXR1")
	private java.lang.String txr1;

	@Column(name = "TXR2")
	private java.lang.String txr2;
	
	@Column(name = "JZ")
	private java.lang.String jz;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.util.Date getKssj() {
		return kssj;
	}

	public void setKssj(java.util.Date kssj) {
		this.kssj = kssj;
	}

	public Double getKsqy() {
		return ksqy;
	}

	public void setKsqy(Double ksqy) {
		this.ksqy = ksqy;
	}

	public Double getKsqw() {
		return ksqw;
	}

	public void setKsqw(Double ksqw) {
		this.ksqw = ksqw;
	}

	public java.util.Date getJssj() {
		return jssj;
	}

	public void setJssj(java.util.Date jssj) {
		this.jssj = jssj;
	}

	public Double getJsqy() {
		return jsqy;
	}

	public void setJsqy(Double jsqy) {
		this.jsqy = jsqy;
	}

	public Double getJsqw() {
		return jsqw;
	}

	public void setJsqw(Double jsqw) {
		this.jsqw = jsqw;
	}

	public Double getYxsj() {
		return yxsj;
	}

	public void setYxsj(Double yxsj) {
		this.yxsj = yxsj;
	}

	public Double getLql() {
		return lql;
	}

	public void setLql(Double lql) {
		this.lql = lql;
	}

	public Double getLqlv() {
		return lqlv;
	}

	public void setLqlv(Double lqlv) {
		this.lqlv = lqlv;
	}

	public java.lang.String getTxr1() {
		return txr1;
	}

	public void setTxr1(java.lang.String txr1) {
		this.txr1 = txr1;
	}

	public java.lang.String getTxr2() {
		return txr2;
	}

	public void setTxr2(java.lang.String txr2) {
		this.txr2 = txr2;
	}

	public java.lang.String getJz() {
		return jz;
	}

	public void setJz(java.lang.String jz) {
		this.jz = jz;
	}

}
