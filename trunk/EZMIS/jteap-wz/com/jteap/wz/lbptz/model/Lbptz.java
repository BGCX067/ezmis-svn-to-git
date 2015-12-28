/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.lbptz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.wzda.model.Wzda;


@Entity
@Table(name = "TB_WZ_YLBTZ")
public class Lbptz{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "BH")	
	private java.lang.String bh;
	
	@Column(name = "BM")	
	private java.lang.String bm;
	
	@Column(name = "ZGBM")	
	private java.lang.String zgbm;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzbm;
	
	@Column(name = "FFSJ")	
	private java.util.Date ffsj;
	
	@Column(name = "DQSJ")	
	private java.util.Date dqsj;
	
	@Column(name = "ZYF")	
	private java.lang.String zyf;
	
	@Column(name = "FFSL")	
	private double ffsl;
	
	@Column(name = "DJ")	
	private double dj;
	
	@Transient
	private double zje;
	
	@Column(name = "ZG")	
	private java.lang.String zg;
	
	@Column(name = "BMZG")	
	private java.lang.String bmzg;

	@Column(name = "SJC")	
	private java.lang.String sjc;
	
	@Column(name = "FLR")	
	private java.lang.String flr;
	
	@Column(name = "LLR")	
	private java.lang.String llr;
	
	@Column(name = "CZR")	
	private java.lang.String czr;
	
	@Column(name = "GRGYQF")	
	private java.lang.String grgyqf;

	public double getFfsl() {
		return this.ffsl;
	}
	
	public void setFfsl(double value) {
		this.ffsl = value;
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

	public java.lang.String getGrgyqf() {
		return this.grgyqf;
	}
	
	public void setGrgyqf(java.lang.String value) {
		this.grgyqf = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getZyf() {
		return this.zyf;
	}
	
	public void setZyf(java.lang.String value) {
		this.zyf = value;
	}
	public java.lang.String getLlr() {
		return this.llr;
	}
	
	public void setLlr(java.lang.String value) {
		this.llr = value;
	}
	public java.util.Date getDqsj() {
		return this.dqsj;
	}
	
	public void setDqsj(java.util.Date value) {
		this.dqsj = value;
	}
	public java.util.Date getFfsj() {
		return this.ffsj;
	}
	
	public void setFfsj(java.util.Date value) {
		this.ffsj = value;
	}
	public Wzda getWzbm() {
		return wzbm;
	}

	public void setWzbm(Wzda wzbm) {
		this.wzbm = wzbm;
	}

	public java.lang.String getFlr() {
		return this.flr;
	}
	
	public void setFlr(java.lang.String value) {
		this.flr = value;
	}
	public double getDj() {
		return this.dj;
	}
	
	public void setDj(double value) {
		this.dj = value;
	}
	public java.lang.String getZgbm() {
		return this.zgbm;
	}
	
	public void setZgbm(java.lang.String value) {
		this.zgbm = value;
	}
	public java.lang.String getBmzg() {
		return this.bmzg;
	}
	
	public void setBmzg(java.lang.String value) {
		this.bmzg = value;
	}
	public java.lang.String getCzr() {
		return this.czr;
	}
	
	public void setCzr(java.lang.String value) {
		this.czr = value;
	}
	public java.lang.String getSjc() {
		return this.sjc;
	}
	
	public void setSjc(java.lang.String value) {
		this.sjc = value;
	}
	public java.lang.String getZg() {
		return this.zg;
	}
	
	public void setZg(java.lang.String value) {
		this.zg = value;
	}
	
	public double getZje() {
		try {
			this.zje = dj*ffsl;
		} catch (Exception e) {
			this.zje=0d;
		}
		return this.zje;
	}

	public void setZje(double zje) {
		this.zje = zje;
	}
	
}

