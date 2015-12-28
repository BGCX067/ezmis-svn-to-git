/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.fjcldgl.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "TB_WZ_YFJWZCLD")
public class Fjcld{
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@OneToMany(mappedBy="fjcld")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("xh asc")
	private Set<Fjcldmx> fjhsdmx;
	
	@Column(name = "BH")	
	private java.lang.String bh;
	
	@Column(name = "BM")	
	private java.lang.String bm;
	
	@Column(name = "CLRQ")	
	private java.util.Date clrq;
	
	@Column(name = "ZWY")	
	private java.lang.String zwy;
	
	@Column(name = "ZG")	
	private java.lang.String zg;
	
	@Column(name = "CZR")	
	private java.lang.String czr;
	
	@Column(name = "ZT")	
	private java.lang.String zt;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Set<Fjcldmx> getFjhsdmx() {
		return fjhsdmx;
	}

	public void setFjhsdmx(Set<Fjcldmx> fjhsdmx) {
		this.fjhsdmx = fjhsdmx;
	}

	public java.lang.String getBh() {
		return bh;
	}

	public void setBh(java.lang.String bh) {
		this.bh = bh;
	}

	public java.lang.String getBm() {
		return bm;
	}

	public void setBm(java.lang.String bm) {
		this.bm = bm;
	}

	public java.util.Date getClrq() {
		return clrq;
	}

	public void setClrq(java.util.Date clrq) {
		this.clrq = clrq;
	}

	public java.lang.String getZwy() {
		return zwy;
	}

	public void setZwy(java.lang.String zwy) {
		this.zwy = zwy;
	}

	public java.lang.String getZg() {
		return zg;
	}

	public void setZg(java.lang.String zg) {
		this.zg = zg;
	}

	public java.lang.String getCzr() {
		return czr;
	}

	public void setCzr(java.lang.String czr) {
		this.czr = czr;
	}

	public java.lang.String getZt() {
		return zt;
	}

	public void setZt(java.lang.String zt) {
		this.zt = zt;
	}
	
}

