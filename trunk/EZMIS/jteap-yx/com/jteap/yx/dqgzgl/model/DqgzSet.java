/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 定期工作设置
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_DQGZ_SET")
public class DqgzSet{

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")	
	private java.lang.String id;
	
	@Column(name = "DQGZ_CATALOG_ID")	
	private java.lang.String dqgzCatalogId;
	
	@Column(name = "FZBM")	
	private java.lang.String fzbm;
	
	@Column(name = "FZGW")
	private java.lang.String fzgw;
	
	@Column(name = "GZGL")	
	private java.lang.String gzgl;
	
	@Column(name = "DQGZ_ZY")	
	private java.lang.String dqgzzy;
	
	@Column(name = "BC")	
	private java.lang.String bc;
	
	@Column(name = "DQGZ_MC")	
	private java.lang.String dqgzMc;
	
	@Column(name = "DQGZ_NR")	
	private java.lang.String dqgzNr;
	
	@Column(name = "DQGZ_FL")
	private java.lang.String dqgzFl;
	
	@Column(name = "DQGZ_CREATE_DT")
	private java.util.Date dqgzCreateDt;
	
	public java.util.Date getDqgzCreateDt() {
		return dqgzCreateDt;
	}

	public void setDqgzCreateDt(java.util.Date dqgzCreateDt) {
		this.dqgzCreateDt = dqgzCreateDt;
	}

	public java.lang.String getDqgzFl() {
		return dqgzFl;
	}

	public void setDqgzFl(java.lang.String dqgzFl) {
		this.dqgzFl = dqgzFl;
	}

	public java.lang.String getId() {
		return this.id;
	}
	
	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getDqgzCatalogId() {
		return this.dqgzCatalogId;
	}
	
	public void setDqgzCatalogId(java.lang.String value) {
		this.dqgzCatalogId = value;
	}
	public java.lang.String getFzbm() {
		return this.fzbm;
	}
	
	public void setFzbm(java.lang.String value) {
		this.fzbm = value;
	}
	public java.lang.String getGzgl() {
		return this.gzgl;
	}
	
	public void setGzgl(java.lang.String value) {
		this.gzgl = value;
	}
	public java.lang.String getDqgzzy() {
		return this.dqgzzy;
	}
	
	public void setDqgzzy(java.lang.String value) {
		this.dqgzzy = value;
	}
	public java.lang.String getBc() {
		return this.bc;
	}
	
	public void setBc(java.lang.String value) {
		this.bc = value;
	}
	public java.lang.String getDqgzMc() {
		return this.dqgzMc;
	}
	
	public void setDqgzMc(java.lang.String value) {
		this.dqgzMc = value;
	}
	public java.lang.String getDqgzNr() {
		return this.dqgzNr;
	}
	
	public void setDqgzNr(java.lang.String value) {
		this.dqgzNr = value;
	}

	public java.lang.String getFzgw() {
		return fzgw;
	}

	public void setFzgw(java.lang.String fzgw) {
		this.fzgw = fzgw;
	}
	
}
