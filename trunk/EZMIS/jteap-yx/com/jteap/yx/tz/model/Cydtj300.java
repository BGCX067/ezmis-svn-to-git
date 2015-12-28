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
 * 厂用电统计(300MW)
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_TZ_CYDTJ300")
public class Cydtj300{
	
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
	
	//#1发电机有功
	@Column(name = "FDJYG_1")	
	private String fdjyg_1;
	
	//#1高厂变
	@Column(name = "GCB_1")	
	private String gcb_1;
	
	//#2发电机有功
	@Column(name = "FDJYG_2")	
	private String fdjyg_2;
	
	//#2高厂变
	@Column(name = "GCB_2")	
	private String gcb_2;
	
	//#01启备变
	@Column(name = "QBB_1")	
	private String qbb_1;
	
	//A翻车机变
	@Column(name = "FCJB_A")	
	private String fcjb_a;
	
	//B翻车机变
	@Column(name = "FCJB_B")	
	private String fcjb_b;
	
	//A斗轮机变
	@Column(name = "DLJB_A")	
	private String dljb_a;
	
	//B斗轮机变
	@Column(name = "DLJB_B")	
	private String dljb_b;
	
	//1A输煤皮带
	@Column(name = "SMPD_A1")	
	private String smpd_a1;
	
	//1B输煤皮带
	@Column(name = "SMPD_B1")	
	private String smpd_b1;
	
	//2A输煤皮带
	@Column(name = "SMPD_A2")	
	private String smpd_a2;
	
	//2B输煤皮带
	@Column(name = "SMPD_B2")	
	private String smpd_b2;
	
	//3A输煤皮带
	@Column(name = "SMPD_A3")	
	private String smpd_a3;
	
	//3B输煤皮带
	@Column(name = "SMPD_B3")	
	private String smpd_b3;
	
	//4A输煤皮带
	@Column(name = "SMPD_A4")	
	private String smpd_a4;
	
	//4B输煤皮带
	@Column(name = "SMPD_B4")	
	private String smpd_b4;
	
	//6A输煤皮带
	@Column(name = "SMPD_A6")	
	private String smpd_a6;
	
	//6B输煤皮带
	@Column(name = "SMPD_B6")	
	private String smpd_b6;
	
	//#1主变(峰值)
	@Column(name = "ZBF_1")	
	private String zbf_1;
	
	//#1主变(平值)
	@Column(name = "ZBP_1")	
	private String zbp_1;
	
	//#1主变(谷值)
	@Column(name = "ZBG_1")	
	private String zbg_1;
	
	//#2主变(峰值)
	@Column(name = "ZBF_2")	
	private String zbf_2;
	
	//#2主变(平值)
	@Column(name = "ZBP_2")	
	private String zbp_2;
	
	//#2主变(谷值)
	@Column(name = "ZBG_2")	
	private String zbg_2;
	
	//启备变E06（峰）
	@Column(name = "QBBF")	
	private String qbbf;
	
	//启备变E06（平）
	@Column(name = "QBBP")	
	private String qbbp;
	
	//启备变E06（谷）
	@Column(name = "QBBG")	
	private String qbbg;
	
	//#1尾水发电
	@Column(name = "WSFD_1")	
	private String wsfd_1;
	
	//#2尾水发电
	@Column(name = "WSFD_2")	
	private String wsfd_2;
	
	//集控启备变
	@Column(name = "JKQBB")	
	private String jkqbb;
	
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

	public String getFdjyg_1() {
		return fdjyg_1;
	}

	public void setFdjyg_1(String fdjyg_1) {
		this.fdjyg_1 = fdjyg_1;
	}

	public String getGcb_1() {
		return gcb_1;
	}

	public void setGcb_1(String gcb_1) {
		this.gcb_1 = gcb_1;
	}

	public String getFdjyg_2() {
		return fdjyg_2;
	}

	public void setFdjyg_2(String fdjyg_2) {
		this.fdjyg_2 = fdjyg_2;
	}

	public String getGcb_2() {
		return gcb_2;
	}

	public void setGcb_2(String gcb_2) {
		this.gcb_2 = gcb_2;
	}

	public String getQbb_1() {
		return qbb_1;
	}

	public void setQbb_1(String qbb_1) {
		this.qbb_1 = qbb_1;
	}

	public String getFcjb_a() {
		return fcjb_a;
	}

	public void setFcjb_a(String fcjb_a) {
		this.fcjb_a = fcjb_a;
	}

	public String getFcjb_b() {
		return fcjb_b;
	}

	public void setFcjb_b(String fcjb_b) {
		this.fcjb_b = fcjb_b;
	}

	public String getDljb_a() {
		return dljb_a;
	}

	public void setDljb_a(String dljb_a) {
		this.dljb_a = dljb_a;
	}

	public String getDljb_b() {
		return dljb_b;
	}

	public void setDljb_b(String dljb_b) {
		this.dljb_b = dljb_b;
	}

	public String getSmpd_a1() {
		return smpd_a1;
	}

	public void setSmpd_a1(String smpd_a1) {
		this.smpd_a1 = smpd_a1;
	}

	public String getSmpd_b1() {
		return smpd_b1;
	}

	public void setSmpd_b1(String smpd_b1) {
		this.smpd_b1 = smpd_b1;
	}

	public String getSmpd_a2() {
		return smpd_a2;
	}

	public void setSmpd_a2(String smpd_a2) {
		this.smpd_a2 = smpd_a2;
	}

	public String getSmpd_b2() {
		return smpd_b2;
	}

	public void setSmpd_b2(String smpd_b2) {
		this.smpd_b2 = smpd_b2;
	}

	public String getSmpd_a3() {
		return smpd_a3;
	}

	public void setSmpd_a3(String smpd_a3) {
		this.smpd_a3 = smpd_a3;
	}

	public String getSmpd_b3() {
		return smpd_b3;
	}

	public void setSmpd_b3(String smpd_b3) {
		this.smpd_b3 = smpd_b3;
	}

	public String getSmpd_a4() {
		return smpd_a4;
	}

	public void setSmpd_a4(String smpd_a4) {
		this.smpd_a4 = smpd_a4;
	}

	public String getSmpd_b4() {
		return smpd_b4;
	}

	public void setSmpd_b4(String smpd_b4) {
		this.smpd_b4 = smpd_b4;
	}

	public String getSmpd_a6() {
		return smpd_a6;
	}

	public void setSmpd_a6(String smpd_a6) {
		this.smpd_a6 = smpd_a6;
	}

	public String getSmpd_b6() {
		return smpd_b6;
	}

	public void setSmpd_b6(String smpd_b6) {
		this.smpd_b6 = smpd_b6;
	}

	public String getZbf_1() {
		return zbf_1;
	}

	public void setZbf_1(String zbf_1) {
		this.zbf_1 = zbf_1;
	}

	public String getZbp_1() {
		return zbp_1;
	}

	public void setZbp_1(String zbp_1) {
		this.zbp_1 = zbp_1;
	}

	public String getZbg_1() {
		return zbg_1;
	}

	public void setZbg_1(String zbg_1) {
		this.zbg_1 = zbg_1;
	}

	public String getZbf_2() {
		return zbf_2;
	}

	public void setZbf_2(String zbf_2) {
		this.zbf_2 = zbf_2;
	}

	public String getZbp_2() {
		return zbp_2;
	}

	public void setZbp_2(String zbp_2) {
		this.zbp_2 = zbp_2;
	}

	public String getZbg_2() {
		return zbg_2;
	}

	public void setZbg_2(String zbg_2) {
		this.zbg_2 = zbg_2;
	}

	public String getQbbf() {
		return qbbf;
	}

	public void setQbbf(String qbbf) {
		this.qbbf = qbbf;
	}

	public String getQbbp() {
		return qbbp;
	}

	public void setQbbp(String qbbp) {
		this.qbbp = qbbp;
	}

	public String getQbbg() {
		return qbbg;
	}

	public void setQbbg(String qbbg) {
		this.qbbg = qbbg;
	}

	public String getWsfd_1() {
		return wsfd_1;
	}

	public void setWsfd_1(String wsfd_1) {
		this.wsfd_1 = wsfd_1;
	}

	public String getWsfd_2() {
		return wsfd_2;
	}

	public void setWsfd_2(String wsfd_2) {
		this.wsfd_2 = wsfd_2;
	}

	public String getJkqbb() {
		return jkqbb;
	}

	public void setJkqbb(String jkqbb) {
		this.jkqbb = jkqbb;
	}
	
	
}
