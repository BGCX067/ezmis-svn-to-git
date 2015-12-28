/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.fjhsdgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "TB_WZ_YFJWZHSDMX")
public class Fjhsdmx{
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
//	@Column(name = "HSDH")	
//	private java.lang.String hsdh;
	@OneToOne()
	@JoinColumn(name="HSDH")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Fjhsd fjhsd;
	
	@Column(name = "WZLB")	
	private java.lang.String wzlb;

	
	@Column(name = "SL")	
	private double sl;
	
	
	@Column(name = "WZMC")	
	private java.lang.String wzmc;
	
	
	@Column(name = "DW")	
	private java.lang.String dw;
	
	
	@Column(name = "XH")	
	private java.lang.String xh;
	


	public java.lang.String getWzlb() {
		return wzlb;
	}
	public void setWzlb(java.lang.String wzlb) {
		this.wzlb = wzlb;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public double getSl() {
		return this.sl;
	}
	
	public void setSl(double value) {
		this.sl = value;
	}
	public java.lang.String getWzmc() {
		return this.wzmc;
	}
	
	public void setWzmc(java.lang.String value) {
		this.wzmc = value;
	}
	public java.lang.String getDw() {
		return this.dw;
	}
	
	public void setDw(java.lang.String value) {
		this.dw = value;
	}
	public java.lang.String getXh() {
		return this.xh;
	}
	
	public void setXh(java.lang.String value) {
		this.xh = value;
	}

	public Fjhsd getFjhsd() {
		return fjhsd;
	}

	public void setFjhsd(Fjhsd fjhsd) {
		this.fjhsd = fjhsd;
	}
	
	
}

