/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.fjcldgl.model;

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
@Table(name = "TB_WZ_YFJWZCLDMX")
public class Fjcldmx{
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
//	@Column(name = "HSDH")	
//	private java.lang.String hsdh;
	@OneToOne()
	@JoinColumn(name="CLDBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Fjcld fjcld;
	
	@Column(name = "XH")	
	private java.lang.String xh;
	
	@Column(name = "WZLB")	
	private java.lang.String wzlb;

	@Column(name = "WZMC")	
	private java.lang.String wzmc;
	
	@Column(name = "DW")	
	private java.lang.String dw;
	
	@Column(name = "SL")	
	private double sl;
	
	@Column(name = "CLDJ")	
	private double cldj;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Fjcld getFjcld() {
		return fjcld;
	}

	public void setFjcld(Fjcld fjcld) {
		this.fjcld = fjcld;
	}

	public java.lang.String getXh() {
		return xh;
	}

	public void setXh(java.lang.String xh) {
		this.xh = xh;
	}

	public java.lang.String getWzlb() {
		return wzlb;
	}

	public void setWzlb(java.lang.String wzlb) {
		this.wzlb = wzlb;
	}

	public java.lang.String getWzmc() {
		return wzmc;
	}

	public void setWzmc(java.lang.String wzmc) {
		this.wzmc = wzmc;
	}

	public java.lang.String getDw() {
		return dw;
	}

	public void setDw(java.lang.String dw) {
		this.dw = dw;
	}

	public double getSl() {
		return sl;
	}

	public void setSl(double sl) {
		this.sl = sl;
	}

	public double getCldj() {
		return cldj;
	}

	public void setCldj(double cldj) {
		this.cldj = cldj;
	}
}

