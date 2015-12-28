/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htzx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 合同备忘
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_HT_HTBW")
public class HeTongBeiWang{
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;
	
	@Column(name = "HTID")
	private String htid;
	
	@Column(name = "HTMC")
	private String htmc;
	
	@Column(name = "HTBH")
	private String htbh;
	
	@Column(name = "BWSJ")
	private Date bwsj;
	
	@Column(name = "BWNR")
	private String bwnr;
	
	@Column(name = "BWR")
	private String bwr;
	
	@Column(name = "BWRID")
	private String bwrId;
	
	public String getHtmc() {
		return htmc;
	}

	public void setHtmc(String htmc) {
		this.htmc = htmc;
	}

	public String getHtbh() {
		return htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public String getBwrId() {
		return bwrId;
	}

	public void setBwrId(String bwrId) {
		this.bwrId = bwrId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public Date getBwsj() {
		return bwsj;
	}

	public void setBwsj(Date bwsj) {
		this.bwsj = bwsj;
	}

	public String getBwnr() {
		return bwnr;
	}

	public void setBwnr(String bwnr) {
		this.bwnr = bwnr;
	}

	public String getBwr() {
		return bwr;
	}

	public void setBwr(String bwr) {
		this.bwr = bwr;
	}
	
}
