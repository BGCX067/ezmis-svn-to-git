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
 * 避雷器动作记录(600MW)
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_TZ_BLQDZJL600")
public class Blqdzjl600{
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	//填写人
	@Column(name = "TIANXIEREN")	
	private String tianXieRen;
	
	//填写时间
	@Column(name = "TIANXIESHIJIAN")	
	private String tianXieShiJian;
	
	//检查人
	@Column(name = "CBR")	
	private String cbr;
	
	//检查时间
	@Column(name = "CBSJ")	
	private String cbsj;
	
	@Column(name = "ZBGYC_A3")	
	private java.lang.String zbgycA3;
	
	
	@Column(name = "ZBGYC_B3")	
	private java.lang.String zbgycB3;
	
	
	@Column(name = "ZBGYC_C3")	
	private java.lang.String zbgycC3;
	
	
	@Column(name = "ZBGYCZX_3")	
	private java.lang.String zbgyczx3;
	
	
	@Column(name = "ZBGYC_A4")	
	private java.lang.String zbgycA4;
	
	
	@Column(name = "ZBGYC_B4")	
	private java.lang.String zbgycB4;
	
	
	@Column(name = "ZBGYC_C4")	
	private java.lang.String zbgycC4;
	
	
	@Column(name = "ZBGYCZX_4")	
	private java.lang.String zbgyczx4;
	
	
	@Column(name = "QBBGYC_A02")	
	private java.lang.String qbbgycA02;
	
	
	@Column(name = "QBBGYC_B02")	
	private java.lang.String qbbgycB02;
	
	
	@Column(name = "QBBGYC_C02")	
	private java.lang.String qbbgycC02;
	
	
	@Column(name = "MX_A4")	
	private java.lang.String mxA4;
	
	
	@Column(name = "MX_B4")	
	private java.lang.String mxB4;
	
	
	@Column(name = "MX_C4")	
	private java.lang.String mxC4;
	
	
	@Column(name = "MX_A5")	
	private java.lang.String mxA5;
	
	
	@Column(name = "MX_B5")	
	private java.lang.String mxB5;
	
	
	@Column(name = "MX_C5")	
	private java.lang.String mxC5;
	
	
	@Column(name = "EGX_A")	
	private java.lang.String egxA;
	
	
	@Column(name = "EGX_B")	
	private java.lang.String egxB;
	
	
	@Column(name = "EGX_C")	
	private java.lang.String egxC;
	
	
	@Column(name = "EFX_A")	
	private java.lang.String efxA;
	
	
	@Column(name = "EFX_B")	
	private java.lang.String efxB;
	
	
	@Column(name = "EFX_C")	
	private java.lang.String efxC;
	
	
	@Column(name = "TSYH_A")	
	private java.lang.String tsyhA;
	
	
	@Column(name = "TSYH_B")	
	private java.lang.String tsyhB;
	
	
	@Column(name = "TSYH_C")	
	private java.lang.String tsyhC;
	
	
	@Column(name = "TSEH_A")	
	private java.lang.String tsehA;
	
	
	@Column(name = "TSEH_B")	
	private java.lang.String tsehB;
	
	
	@Column(name = "TSEH_C")	
	private java.lang.String tsehC;
	

	public String getTianXieRen() {
		return tianXieRen;
	}
	public void setTianXieRen(String tianXieRen) {
		this.tianXieRen = tianXieRen;
	}
	public String getTianXieShiJian() {
		return tianXieShiJian;
	}
	public void setTianXieShiJian(String tianXieShiJian) {
		this.tianXieShiJian = tianXieShiJian;
	}
	public String getCbr() {
		return cbr;
	}
	public void setCbr(String cbr) {
		this.cbr = cbr;
	}
	public String getCbsj() {
		return cbsj;
	}
	public void setCbsj(String cbsj) {
		this.cbsj = cbsj;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getZbgycA3() {
		return this.zbgycA3;
	}
	
	public void setZbgycA3(java.lang.String value) {
		this.zbgycA3 = value;
	}
	public java.lang.String getZbgycB3() {
		return this.zbgycB3;
	}
	
	public void setZbgycB3(java.lang.String value) {
		this.zbgycB3 = value;
	}
	public java.lang.String getZbgycC3() {
		return this.zbgycC3;
	}
	
	public void setZbgycC3(java.lang.String value) {
		this.zbgycC3 = value;
	}
	public java.lang.String getZbgyczx3() {
		return this.zbgyczx3;
	}
	
	public void setZbgyczx3(java.lang.String value) {
		this.zbgyczx3 = value;
	}
	public java.lang.String getZbgycA4() {
		return this.zbgycA4;
	}
	
	public void setZbgycA4(java.lang.String value) {
		this.zbgycA4 = value;
	}
	public java.lang.String getZbgycB4() {
		return this.zbgycB4;
	}
	
	public void setZbgycB4(java.lang.String value) {
		this.zbgycB4 = value;
	}
	public java.lang.String getZbgycC4() {
		return this.zbgycC4;
	}
	
	public void setZbgycC4(java.lang.String value) {
		this.zbgycC4 = value;
	}
	public java.lang.String getZbgyczx4() {
		return this.zbgyczx4;
	}
	
	public void setZbgyczx4(java.lang.String value) {
		this.zbgyczx4 = value;
	}
	public java.lang.String getQbbgycA02() {
		return this.qbbgycA02;
	}
	
	public void setQbbgycA02(java.lang.String value) {
		this.qbbgycA02 = value;
	}
	public java.lang.String getQbbgycB02() {
		return this.qbbgycB02;
	}
	
	public void setQbbgycB02(java.lang.String value) {
		this.qbbgycB02 = value;
	}
	public java.lang.String getQbbgycC02() {
		return this.qbbgycC02;
	}
	
	public void setQbbgycC02(java.lang.String value) {
		this.qbbgycC02 = value;
	}
	public java.lang.String getMxA4() {
		return this.mxA4;
	}
	
	public void setMxA4(java.lang.String value) {
		this.mxA4 = value;
	}
	public java.lang.String getMxB4() {
		return this.mxB4;
	}
	
	public void setMxB4(java.lang.String value) {
		this.mxB4 = value;
	}
	public java.lang.String getMxC4() {
		return this.mxC4;
	}
	
	public void setMxC4(java.lang.String value) {
		this.mxC4 = value;
	}
	public java.lang.String getMxA5() {
		return this.mxA5;
	}
	
	public void setMxA5(java.lang.String value) {
		this.mxA5 = value;
	}
	public java.lang.String getMxB5() {
		return this.mxB5;
	}
	
	public void setMxB5(java.lang.String value) {
		this.mxB5 = value;
	}
	public java.lang.String getMxC5() {
		return this.mxC5;
	}
	
	public void setMxC5(java.lang.String value) {
		this.mxC5 = value;
	}
	public java.lang.String getEgxA() {
		return this.egxA;
	}
	
	public void setEgxA(java.lang.String value) {
		this.egxA = value;
	}
	public java.lang.String getEgxB() {
		return this.egxB;
	}
	
	public void setEgxB(java.lang.String value) {
		this.egxB = value;
	}
	public java.lang.String getEgxC() {
		return this.egxC;
	}
	
	public void setEgxC(java.lang.String value) {
		this.egxC = value;
	}
	public java.lang.String getEfxA() {
		return this.efxA;
	}
	
	public void setEfxA(java.lang.String value) {
		this.efxA = value;
	}
	public java.lang.String getEfxB() {
		return this.efxB;
	}
	
	public void setEfxB(java.lang.String value) {
		this.efxB = value;
	}
	public java.lang.String getEfxC() {
		return this.efxC;
	}
	
	public void setEfxC(java.lang.String value) {
		this.efxC = value;
	}
	public java.lang.String getTsyhA() {
		return this.tsyhA;
	}
	
	public void setTsyhA(java.lang.String value) {
		this.tsyhA = value;
	}
	public java.lang.String getTsyhB() {
		return this.tsyhB;
	}
	
	public void setTsyhB(java.lang.String value) {
		this.tsyhB = value;
	}
	public java.lang.String getTsyhC() {
		return this.tsyhC;
	}
	
	public void setTsyhC(java.lang.String value) {
		this.tsyhC = value;
	}
	public java.lang.String getTsehA() {
		return this.tsehA;
	}
	
	public void setTsehA(java.lang.String value) {
		this.tsehA = value;
	}
	public java.lang.String getTsehB() {
		return this.tsehB;
	}
	
	public void setTsehB(java.lang.String value) {
		this.tsehB = value;
	}
	public java.lang.String getTsehC() {
		return this.tsehC;
	}
	
	public void setTsehC(java.lang.String value) {
		this.tsehC = value;
	}
	
}
