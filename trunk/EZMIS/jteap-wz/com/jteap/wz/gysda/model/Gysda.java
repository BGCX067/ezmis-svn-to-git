/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.gysda.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "TB_WZ_SGYSDA")
@SuppressWarnings("unchecked")
public class Gysda{

	//columns START
	
	@Column(name = "BM")	
	private java.lang.String bm;
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "GYSMC")	
	private java.lang.String gysmc;
	
	
	@Column(name = "DZ")	
	private java.lang.String dz;
	
	
	@Column(name = "DH")	
	private java.lang.String dh;
	
	
	@Column(name = "CZH")	
	private java.lang.String czh;
	
	
	@Column(name = "YZBM")	
	private java.lang.String yzbm;
	
	
	@Column(name = "LXR")	
	private java.lang.String lxr;
	
	
	@Column(name = "YXDZ")	
	private java.lang.String yxdz;
	
	
	@Column(name = "ZYWZ")	
	private java.lang.String zywz;
	
	
	@Column(name = "FRDB")	
	private java.lang.String frdb;
	
	
	@Column(name = "KHYH")	
	private java.lang.String khyh;
	
	
	@Column(name = "ZH")	
	private java.lang.String zh;
	
	
	@Column(name = "SWDJH")	
	private java.lang.String swdjh;
	
	
	@Column(name = "QTXX")	
	private java.lang.String qtxx;
	
	
	@Column(name = "SFXYDW")	
	private java.lang.String sfxydw;
	
	
	@Column(name = "SFSNDW")	
	private java.lang.String sfsndw;
	
	
	@Column(name = "ZJM")	
	private java.lang.String zjm;
	

	public java.lang.String getBm() {
		return this.bm;
	}
	
	public void setBm(java.lang.String value) {
		this.bm = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getGysmc() {
		return this.gysmc;
	}
	
	public void setGysmc(java.lang.String value) {
		this.gysmc = value;
	}
	public java.lang.String getDz() {
		return this.dz;
	}
	
	public void setDz(java.lang.String value) {
		this.dz = value;
	}
	public java.lang.String getDh() {
		return this.dh;
	}
	
	public void setDh(java.lang.String value) {
		this.dh = value;
	}
	public java.lang.String getCzh() {
		return this.czh;
	}
	
	public void setCzh(java.lang.String value) {
		this.czh = value;
	}
	public java.lang.String getYzbm() {
		return this.yzbm;
	}
	
	public void setYzbm(java.lang.String value) {
		this.yzbm = value;
	}
	public java.lang.String getLxr() {
		return this.lxr;
	}
	
	public void setLxr(java.lang.String value) {
		this.lxr = value;
	}
	public java.lang.String getYxdz() {
		return this.yxdz;
	}
	
	public void setYxdz(java.lang.String value) {
		this.yxdz = value;
	}
	public java.lang.String getZywz() {
		return this.zywz;
	}
	
	public void setZywz(java.lang.String value) {
		this.zywz = value;
	}
	public java.lang.String getFrdb() {
		return this.frdb;
	}
	
	public void setFrdb(java.lang.String value) {
		this.frdb = value;
	}
	public java.lang.String getKhyh() {
		return this.khyh;
	}
	
	public void setKhyh(java.lang.String value) {
		this.khyh = value;
	}
	public java.lang.String getZh() {
		return this.zh;
	}
	
	public void setZh(java.lang.String value) {
		this.zh = value;
	}
	public java.lang.String getSwdjh() {
		return this.swdjh;
	}
	
	public void setSwdjh(java.lang.String value) {
		this.swdjh = value;
	}
	public java.lang.String getQtxx() {
		return this.qtxx;
	}
	
	public void setQtxx(java.lang.String value) {
		this.qtxx = value;
	}
	public java.lang.String getSfxydw() {
		return this.sfxydw;
	}
	
	public void setSfxydw(java.lang.String value) {
		this.sfxydw = value;
	}
	public java.lang.String getSfsndw() {
		return this.sfsndw;
	}
	
	public void setSfsndw(java.lang.String value) {
		this.sfsndw = value;
	}
	public java.lang.String getZjm() {
		return this.zjm;
	}
	
	public void setZjm(java.lang.String value) {
		this.zjm = value;
	}
	
	
}

