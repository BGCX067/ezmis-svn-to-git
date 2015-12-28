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
 * 避雷器动作记录(300MW)
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_TZ_BLQDZJL300")
public class Blqdzjl300{
	
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
	
	//检查人
	@Column(name = "CBR")	
	private String cbr;
	
	//检查时间
	@Column(name = "CBSJ")	
	private String cbsj;
	
	//#1主变高压侧A相
	@Column(name = "ZBGYC_A1")	
	private String zbgyc_a1;
	
	//#1主变高压侧B相
	@Column(name = "ZBGYC_B1")	
	private String zbgyc_b1;
	
	//#1主变高压侧C相
	@Column(name = "ZBGYC_C1")	
	private String zbgyc_c1;
	
	//#1主变高压侧中性点
	@Column(name = "ZBGYCZXD_1")	
	private String zbgyczxd_1;
	
	//#2主变高压侧A相
	@Column(name = "ZBGYC_A2")	
	private String zbgyc_a2;
	
	//#2主变高压侧B相
	@Column(name = "ZBGYC_B2")	
	private String zbgyc_b2;
	
	//#2主变高压侧C相
	@Column(name = "ZBGYC_C2")	
	private String zbgyc_c2;
	
	//#2主变高压侧中性点
	@Column(name = "ZBGYCZXD_2")	
	private String zbgyczxd_2;
	
	//#01启备变高压侧A相
	@Column(name = "QBBGYC_A01")	
	private String qbbgyc_a01;
	
	//#01启备变高压侧B相
	@Column(name = "QBBGYC_B01")	
	private String qbbgyc_b01;
	
	//#01启备变高压侧C相
	@Column(name = "QBBGYC_C01")	
	private String qbbgyc_c01;
	
	//220KV #1母线A相
	@Column(name = "MX_A1")	
	private String mx_a1;
	
	//220KV #1母线B相
	@Column(name = "MX_B1")	
	private String mx_b1;
	
	//220KV #1母线C相
	@Column(name = "MX_C1")	
	private String mx_c1;
	
	//220KV #2母线A相
	@Column(name = "MX_A2")	
	private String mx_a2;
	
	//220KV #2母线B相
	@Column(name = "MX_B2")	
	private String mx_b2;
	
	//220KV #2母线C相
	@Column(name = "MX_C2")	
	private String mx_c2;
	
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
	
	public String getCbr() {
		return cbr;
	}

	public void setCbr(String cbr) {
		this.cbr = cbr;
	}

	public String getCbsj() {
		return cbsj;
	}

	public void setCbsj(String cbsj) {
		this.cbsj = cbsj;
	}

	public String getZbgyc_a1() {
		return zbgyc_a1;
	}

	public void setZbgyc_a1(String zbgyc_a1) {
		this.zbgyc_a1 = zbgyc_a1;
	}

	public String getZbgyc_b1() {
		return zbgyc_b1;
	}

	public void setZbgyc_b1(String zbgyc_b1) {
		this.zbgyc_b1 = zbgyc_b1;
	}

	public String getZbgyc_c1() {
		return zbgyc_c1;
	}

	public void setZbgyc_c1(String zbgyc_c1) {
		this.zbgyc_c1 = zbgyc_c1;
	}

	public String getZbgyczxd_1() {
		return zbgyczxd_1;
	}

	public void setZbgyczxd_1(String zbgyczxd_1) {
		this.zbgyczxd_1 = zbgyczxd_1;
	}

	public String getZbgyc_a2() {
		return zbgyc_a2;
	}

	public void setZbgyc_a2(String zbgyc_a2) {
		this.zbgyc_a2 = zbgyc_a2;
	}

	public String getZbgyc_b2() {
		return zbgyc_b2;
	}

	public void setZbgyc_b2(String zbgyc_b2) {
		this.zbgyc_b2 = zbgyc_b2;
	}

	public String getZbgyc_c2() {
		return zbgyc_c2;
	}

	public void setZbgyc_c2(String zbgyc_c2) {
		this.zbgyc_c2 = zbgyc_c2;
	}

	public String getZbgyczxd_2() {
		return zbgyczxd_2;
	}

	public void setZbgyczxd_2(String zbgyczxd_2) {
		this.zbgyczxd_2 = zbgyczxd_2;
	}

	public String getQbbgyc_a01() {
		return qbbgyc_a01;
	}

	public void setQbbgyc_a01(String qbbgyc_a01) {
		this.qbbgyc_a01 = qbbgyc_a01;
	}

	public String getQbbgyc_b01() {
		return qbbgyc_b01;
	}

	public void setQbbgyc_b01(String qbbgyc_b01) {
		this.qbbgyc_b01 = qbbgyc_b01;
	}

	public String getQbbgyc_c01() {
		return qbbgyc_c01;
	}

	public void setQbbgyc_c01(String qbbgyc_c01) {
		this.qbbgyc_c01 = qbbgyc_c01;
	}

	public String getMx_a1() {
		return mx_a1;
	}

	public void setMx_a1(String mx_a1) {
		this.mx_a1 = mx_a1;
	}

	public String getMx_b1() {
		return mx_b1;
	}

	public void setMx_b1(String mx_b1) {
		this.mx_b1 = mx_b1;
	}

	public String getMx_c1() {
		return mx_c1;
	}

	public void setMx_c1(String mx_c1) {
		this.mx_c1 = mx_c1;
	}

	public String getMx_a2() {
		return mx_a2;
	}

	public void setMx_a2(String mx_a2) {
		this.mx_a2 = mx_a2;
	}

	public String getMx_b2() {
		return mx_b2;
	}

	public void setMx_b2(String mx_b2) {
		this.mx_b2 = mx_b2;
	}

	public String getMx_c2() {
		return mx_c2;
	}

	public void setMx_c2(String mx_c2) {
		this.mx_c2 = mx_c2;
	}
}
