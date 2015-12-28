/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.gqjtz.model;

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
@Table(name = "TB_WZ_YGQJTZ")
public class Gqjtz{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "BH")	   //编号
	private String bh;
	
	@Column(name = "BM")	     //部门编码
	private java.lang.String bm;
	
	@Column(name = "ZGXM")	     //职工编码
	private java.lang.String zgxm;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzbm;
	
	@Column(name = "FFSJ")	     //发放时间
	private java.util.Date ffsj;
	 
	@Column(name = "DQSJ")	      //到期时间
	private java.util.Date dqsj;
	
	@Column(name = "ZYF")	   //在用否
	private java.lang.String zyf;
	
	@Column(name = "FFSL")	    //发放数量
	private double ffsl;
	
 	@Column(name = "DJ")	   //单价
	private double dj;
 	
	@Transient
	private double zje;         //总金额
 	
 	@Column(name = "ZG")	   //主管
	private java.lang.String zg;
	
 	@Column(name = "BMZG")	  //部门主管
	private java.lang.String bmzg;

 	@Column(name = "SJC")	      //生计处
	private java.lang.String sjc;
 	
	@Column(name = "FLR")	    //发料人
	private java.lang.String flr;
	
	@Column(name = "CZR")	   //操作人
	private java.lang.String czr;
	
	@Column(name = "GRGYQF")	  //个人公用区分
	private java.lang.String grgyqf;
	

	public double getDj() {
		return this.dj;
	}
	
	public void setDj(double value) {
		this.dj = value;
	}
	public java.lang.String getFlr() {
		return this.flr;
	}
	
	public void setFlr(java.lang.String value) {
		this.flr = value;
	}

	public Wzda getWzbm() {
		return wzbm;
	}

	public void setWzbm(Wzda wzbm) {
		this.wzbm = wzbm;
	}

	public java.lang.String getZyf() {
		return this.zyf;
	}
	
	public void setZyf(java.lang.String value) {
		this.zyf = value;
	}
	public java.lang.String getCzr() {
		return this.czr;
	}
	
	public void setCzr(java.lang.String value) {
		this.czr = value;
	}
	public java.lang.String getBmzg() {
		return this.bmzg;
	}
	
	public void setBmzg(java.lang.String value) {
		this.bmzg = value;
	}
	public java.lang.String getZg() {
		return this.zg;
	}
	
	public void setZg(java.lang.String value) {
		this.zg = value;
	}
	
	public void setFfsl(double value) {
		this.ffsl = value;
	}
	public java.lang.String getSjc() {
		return this.sjc;
	}
	
	public void setSjc(java.lang.String value) {
		this.sjc = value;
	}
	public java.util.Date getFfsj() {
		return this.ffsj;
	}
	
	public void setFfsj(java.util.Date value) {
		this.ffsj = value;
	}
	public java.util.Date getDqsj() {
		return this.dqsj;
	}
	
	public void setDqsj(java.util.Date value) {
		this.dqsj = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getGrgyqf() {
		return this.grgyqf;
	}
	
	public void setGrgyqf(java.lang.String value) {
		this.grgyqf = value;
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

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public java.lang.String getBm() {
		return bm;
	}

	public void setBm(java.lang.String bm) {
		this.bm = bm;
	}

	public java.lang.String getZgxm() {
		return zgxm;
	}

	public void setZgxm(java.lang.String zgxm) {
		this.zgxm = zgxm;
	}

	public double getFfsl() {
		return ffsl;
	}
	
	
}

