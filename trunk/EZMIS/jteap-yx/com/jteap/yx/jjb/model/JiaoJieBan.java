/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.jjb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 交接班实体
 */
@Entity
@Table(name = "TB_YX_JJB_JIAOJIEBAN")
public class JiaoJieBan{

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;
	
	@Column(name = "JJBSJ")	
	private java.lang.String jjbsj;
	
	@Column(name = "JIAOBANBC")	
	private java.lang.String jiaobanbc;
	
	@Column(name = "JIAOBANZB")	
	private java.lang.String jiaobanzb;
	
	@Column(name = "JIAOBANR")	
	private java.lang.String jiaobanr;
	
	@Column(name = "JIEBANBC")	
	private java.lang.String jiebanbc;
	
	@Column(name = "JIEBANZB")	
	private java.lang.String jiebanzb;
	
	@Column(name = "JIEBANR")	
	private java.lang.String jiebanr;
	
	@Column(name = "DODATE")
	private java.util.Date doDate;
	
	@Column(name = "GWLB")	
	private java.lang.String gwlb;
	
	@Column(name = "JIAOBANR_ID")	
	private java.lang.String jiaobanrId;
	
	@Column(name = "JIEBANR_ID")	
	private java.lang.String jiebanrId;
	
	public java.lang.String getJiaobanrId() {
		return jiaobanrId;
	}

	public void setJiaobanrId(java.lang.String jiaobanrId) {
		this.jiaobanrId = jiaobanrId;
	}

	public java.lang.String getJiebanrId() {
		return jiebanrId;
	}

	public void setJiebanrId(java.lang.String jiebanrId) {
		this.jiebanrId = jiebanrId;
	}

	public java.lang.String getJjbsj() {
		return jjbsj;
	}

	public void setJjbsj(java.lang.String jjbsj) {
		this.jjbsj = jjbsj;
	}

	public java.lang.String getGwlb() {
		return gwlb;
	}

	public void setGwlb(java.lang.String gwlb) {
		this.gwlb = gwlb;
	}

	public java.util.Date getDoDate() {
		return doDate;
	}

	public void setDoDate(java.util.Date doDate) {
		this.doDate = doDate;
	}

	public java.lang.String getId() {
		return this.id;
	}
	
	public void setId(java.lang.String value) {
		this.id = value;
	}
	
	public java.lang.String getJiaobanbc() {
		return this.jiaobanbc;
	}
	
	public void setJiaobanbc(java.lang.String value) {
		this.jiaobanbc = value;
	}
	public java.lang.String getJiaobanzb() {
		return this.jiaobanzb;
	}
	
	public void setJiaobanzb(java.lang.String value) {
		this.jiaobanzb = value;
	}
	public java.lang.String getJiaobanr() {
		return this.jiaobanr;
	}
	
	public void setJiaobanr(java.lang.String value) {
		this.jiaobanr = value;
	}
	public java.lang.String getJiebanbc() {
		return this.jiebanbc;
	}
	
	public void setJiebanbc(java.lang.String value) {
		this.jiebanbc = value;
	}
	public java.lang.String getJiebanzb() {
		return this.jiebanzb;
	}
	
	public void setJiebanzb(java.lang.String value) {
		this.jiebanzb = value;
	}
	public java.lang.String getJiebanr() {
		return this.jiebanr;
	}
	
	public void setJiebanr(java.lang.String value) {
		this.jiebanr = value;
	}
	
}
