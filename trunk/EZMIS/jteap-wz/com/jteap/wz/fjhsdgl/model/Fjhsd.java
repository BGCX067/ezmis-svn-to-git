/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.fjhsdgl.model;

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
@Table(name = "TB_WZ_YFJWZHSD")
public class Fjhsd{
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@OneToMany(mappedBy="fjhsd")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("xh asc")
	private Set<Fjhsdmx> fjhsdmx;
	
	
	@Column(name = "HSRQ")	
	private java.util.Date hsrq;
	
	
	@Column(name = "CZR")	
	private java.lang.String czr;
	
	
	@Column(name = "BH")	
	private java.lang.String hsdh;
	
	
	@Column(name = "SJBM")	
	private java.lang.String sjbm;
	
	
	@Column(name = "SJR")	
	private java.lang.String sjr;
	
	
	@Column(name = "ZT")	
	private java.lang.String zt;
	
	
	@Column(name = "JSR")	
	private java.lang.String jsr;
	

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.util.Date getHsrq() {
		return this.hsrq;
	}
	
	public void setHsrq(java.util.Date value) {
		this.hsrq = value;
	}
	public java.lang.String getCzr() {
		return this.czr;
	}
	
	public void setCzr(java.lang.String value) {
		this.czr = value;
	}
	public java.lang.String getHsdh() {
		return this.hsdh;
	}
	
	public void setHsdh(java.lang.String value) {
		this.hsdh = value;
	}
	public java.lang.String getSjbm() {
		return this.sjbm;
	}
	
	public void setSjbm(java.lang.String value) {
		this.sjbm = value;
	}
	public java.lang.String getSjr() {
		return this.sjr;
	}
	
	public void setSjr(java.lang.String value) {
		this.sjr = value;
	}
	public java.lang.String getZt() {
		return this.zt;
	}
	
	public void setZt(java.lang.String value) {
		this.zt = value;
	}
	public java.lang.String getJsr() {
		return this.jsr;
	}
	
	public void setJsr(java.lang.String value) {
		this.jsr = value;
	}
	public Set<Fjhsdmx> getFjhsdmx() {
		return fjhsdmx;
	}
	public void setFjhsdmx(Set<Fjhsdmx> fjhsdmx) {
		this.fjhsdmx = fjhsdmx;
	}
	
}

