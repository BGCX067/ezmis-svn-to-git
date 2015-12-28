/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 系统通知
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_SYS_TONGZHI")
public class XiTongTongZhi {
	
	// 编号
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;

	// 序号
	@Column(name = "XH")
	private int xh;
	
	// 内容
	@Column(name = "NR")
	private String nr;
	
	//发出人
	@Column(name = "FCR")
	private String fcr;
	
	//时间
	@Column(name = "SJ")
	private String sj;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getFcr() {
		return fcr;
	}

	public void setFcr(String fcr) {
		this.fcr = fcr;
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}
	
}
