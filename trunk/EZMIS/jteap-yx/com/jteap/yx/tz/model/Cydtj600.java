/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.tz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 厂用电统计(600MW)
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_TZ_CYDTJ600")
public class Cydtj600{
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")	
	private String id;
	
	//填写人
	@Column(name = "TIANXIEREN")	
	private String tianXieRen;
	
	//填写时间
	@Column(name = "TIANXIESHIJIAN")	
	private String tianXieShiJian;
	
	//值别
	@Column(name = "ZB")	
	private String zb;
	
	//抄表人
	@Column(name = "CBR")	
	private String cbr;
	
	//抄表时间
	@Column(name = "CBSJ")	
	private String cbsj;
	
	//#3发电机有功
	@Column(name = "FDJYG_3")	
	private String fdjyg_3;
	
	//#3高厂变
	@Column(name = "GCB_3")	
	private String gcb_3;
	
	//#4发电机有功
	@Column(name = "FDJYG_4")	
	private String fdjyg_4;
	
	//#4高厂变
	@Column(name = "GCB_4")	
	private String gcb_4;
	
	//#3励磁变
	@Column(name = "LCB_3")	
	private String lcb_3;
	
	//#4励磁变
	@Column(name = "LCB_4")	
	private String lcb_4;
	
	//#02启备变
	@Column(name = "QBB_2")	
	private String qbb_2;

	public String getCbsj() {
		return cbsj;
	}

	public void setCbsj(String cbsj) {
		this.cbsj = cbsj;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTianXieRen() {
		return tianXieRen;
	}

	public void setTianXieRen(String tianXieRen) {
		this.tianXieRen = tianXieRen;
	}

	public String getTianXieShiJian() {
		return tianXieShiJian;
	}

	public void setTianXieShiJian(String tianXieShiJian) {
		this.tianXieShiJian = tianXieShiJian;
	}

	public String getZb() {
		return zb;
	}

	public void setZb(String zb) {
		this.zb = zb;
	}

	public String getCbr() {
		return cbr;
	}

	public void setCbr(String cbr) {
		this.cbr = cbr;
	}

	public String getFdjyg_3() {
		return fdjyg_3;
	}

	public void setFdjyg_3(String fdjyg_3) {
		this.fdjyg_3 = fdjyg_3;
	}

	public String getGcb_3() {
		return gcb_3;
	}

	public void setGcb_3(String gcb_3) {
		this.gcb_3 = gcb_3;
	}

	public String getFdjyg_4() {
		return fdjyg_4;
	}

	public void setFdjyg_4(String fdjyg_4) {
		this.fdjyg_4 = fdjyg_4;
	}

	public String getGcb_4() {
		return gcb_4;
	}

	public void setGcb_4(String gcb_4) {
		this.gcb_4 = gcb_4;
	}

	public String getLcb_3() {
		return lcb_3;
	}

	public void setLcb_3(String lcb_3) {
		this.lcb_3 = lcb_3;
	}

	public String getLcb_4() {
		return lcb_4;
	}

	public void setLcb_4(String lcb_4) {
		this.lcb_4 = lcb_4;
	}

	public String getQbb_2() {
		return qbb_2;
	}

	public void setQbb_2(String qbb_2) {
		this.qbb_2 = qbb_2;
	}
	
}
