/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.wz.wzda.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.kwwh.model.Kw;
import com.jteap.wz.util.ExcelSign;
import com.jteap.wz.wzlb.model.Wzlb;

@Entity
@Table(name = "TB_WZ_SWZDA")
@SuppressWarnings("unused")
public class Wzda {

	// columns START

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "WZMC")
	@ExcelSign(column = "物资名称")
	private java.lang.String wzmc;// 物资名称

	@ManyToOne()
	@JoinColumn(name = "wzlbbm")
	@LazyToOne(LazyToOneOption.PROXY)
	@ExcelSign(column = "物资类别名称", index = "wzlb.wzlbmc")
	private Wzlb wzlb;// 物资类别

	@Column(name = "ZJM")
	@ExcelSign(column = "助记码")
	private java.lang.String zjm;// 助记码

	@Column(name = "XHGG")
	@ExcelSign(column = "型号规格")
	private java.lang.String xhgg;// 型号规格

	@Column(name = "JLDW")
	@ExcelSign(column = "计量单位")
	private java.lang.String jldw;// 计量单位

	@Column(name = "JHDJ")
	@ExcelSign(column = "计划单价")
	private double jhdj;// 计划单价

	@Column(name = "PJJ")
	@ExcelSign(column = "平均价")
	private double pjj;// 平均价

	@Transient
	@ExcelSign(column = "金额", script = "#set($je=${dqkc}*${jhdj}) $!{je}")
	private double je;// 金额

	@Column(name = "ZGCBDE")
	@ExcelSign(column = "最高储备")
	private double zgcbde;// 最高储备

	@Column(name = "ZDCBDE")
	@ExcelSign(column = "最低储备")
	private double zdcbde;// 最低储备

	@Column(name = "DQKC")
	@ExcelSign(column = "当前库存")
	private double dqkc;// 当前库存

	@Column(name = "YFPSL")
	@ExcelSign(column = "已分配数量")
	private double yfpsl;// 已分配数量

	@Column(name = "ABCFL")
	@ExcelSign(column = "abc分类")
	private java.lang.String abcfl;// abc分类

	@Column(name = "TSFL")
	@ExcelSign(column = "特殊分类")
	private java.lang.String tsfl;// 特殊分类

	@Column(name = "ZYF")
	@ExcelSign(column = "再用否")
	private java.lang.String zyf;// 在用否

	@ManyToOne()
	@JoinColumn(name = "kwbm")
	@LazyToOne(LazyToOneOption.PROXY)
	@ExcelSign(column = "库位", index = "kw.cwmc")
	private Kw kw;

	@Column(name = "CZY")
	private java.lang.String czy;//

	@Column(name = "CSKC")
	@ExcelSign(column = "初始库存")
	private double cskc;// 初始库存

	@Column(name = "CSJG")
	@ExcelSign(column = "初始价格")
	private double csjg;// 初始价格

	@Column(name = "BZ")
	@ExcelSign(column = "备注")
	private java.lang.String bz;// 备注

	@Column(name = "CFWZ")
	private java.lang.String cfwz;// 存放位置，不需要了

	@Column(name = "DYX")
	@ExcelSign(column = "代用型")
	private java.lang.String dyx;// 代用型

	@Column(name = "TJM")
	@ExcelSign(column = "统计码")
	private java.lang.String tjm;// 统计码
	
	@Column(name = "WZBH")
	@ExcelSign(column = "物资编号")
	private java.lang.String wzbh;//物资编号
	
	@Column(name = "SJDJ")
	private double sjdj;          //实际单价（导入老物资）
	
	@Column(name = "SJJE")
	private double sjje;          //实际金额（导入老物资）
	
	@Column(name = "OLDWZKC")
	private double oldwzkc;       //老物资库存
	
	@Column(name = "OLDKC")
	private double oldkc;       //老物资库存 用做出库处理
	
	@Transient
	private String wzmcxh;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Wzlb getWzlb() {
		return wzlb;
	}

	public void setWzlb(Wzlb wzlb) {
		this.wzlb = wzlb;
	}

	public java.lang.String getWzmc() {
		return this.wzmc;
	}

	public void setWzmc(java.lang.String value) {
		this.wzmc = value;
	}

	public java.lang.String getZjm() {
		return this.zjm;
	}

	public void setZjm(java.lang.String value) {
		this.zjm = value;
	}

	public java.lang.String getXhgg() {
		return this.xhgg;
	}

	public void setXhgg(java.lang.String value) {
		this.xhgg = value;
	}

	public java.lang.String getJldw() {
		return this.jldw;
	}

	public void setJldw(java.lang.String value) {
		this.jldw = value;
	}

	public double getJhdj() {
		return this.jhdj;
	}

	public void setJhdj(double value) {
		this.jhdj = value;
	}

	public double getPjj() {
		return this.pjj;
	}

	public void setPjj(double value) {
		this.pjj = value;
	}

	public double getZgcbde() {
		return this.zgcbde;
	}

	public void setZgcbde(double value) {
		this.zgcbde = value;
	}

	public double getZdcbde() {
		return this.zdcbde;
	}

	public void setZdcbde(double value) {
		this.zdcbde = value;
	}

	public double getDqkc() {
		return dqkc;
	}

	public void setDqkc(double dqkc) {
		this.dqkc = dqkc;
	}

	public void setDqkc(int value) {
		this.dqkc = value;
	}

	public double getYfpsl() {
		return this.yfpsl;
	}

	public void setYfpsl(double value) {
		this.yfpsl = value;
	}

	public java.lang.String getAbcfl() {
		return this.abcfl;
	}

	public void setAbcfl(java.lang.String value) {
		this.abcfl = value;
	}

	public java.lang.String getTsfl() {
		return this.tsfl;
	}

	public void setTsfl(java.lang.String value) {
		this.tsfl = value;
	}

	public java.lang.String getZyf() {
		return this.zyf;
	}

	public void setZyf(java.lang.String value) {
		this.zyf = value;
	}

	public Kw getKw() {
		return kw;
	}

	public void setKw(Kw kw) {
		this.kw = kw;
	}

	public java.lang.String getCzy() {
		return this.czy;
	}

	public void setCzy(java.lang.String value) {
		this.czy = value;
	}

	public double getCskc() {
		return this.cskc;
	}

	public void setCskc(double value) {
		this.cskc = value;
	}

	public double getCsjg() {
		return this.csjg;
	}

	public void setCsjg(double value) {
		this.csjg = value;
	}

	public java.lang.String getBz() {
		return this.bz;
	}

	public void setBz(java.lang.String value) {
		this.bz = value;
	}

	public java.lang.String getCfwz() {
		return this.cfwz;
	}

	public void setCfwz(java.lang.String value) {
		this.cfwz = value;
	}

	public java.lang.String getDyx() {
		return this.dyx;
	}

	public void setDyx(java.lang.String value) {
		this.dyx = value;
	}

	public java.lang.String getTjm() {
		return this.tjm;
	}

	public void setTjm(java.lang.String value) {
		this.tjm = value;
	}

	public double getJe() {
		return je;
	}

	public void setJe(double je) {
		this.je = je;
	}

	public java.lang.String getWzbh() {
		return wzbh;
	}

	public void setWzbh(java.lang.String wzbh) {
		this.wzbh = wzbh;
	}

	public String getWzmcxh() {
		String xhgg="";
		if(this.xhgg!=null){
			xhgg = this.xhgg;
		}
		return this.wzmc+"("+xhgg+")";
	}

	public void setWzmcxh(String wzmcxh) {
		this.wzmcxh = wzmcxh;
	}

	public double getSjdj() {
		return sjdj;
	}

	public void setSjdj(double sjdj) {
		this.sjdj = sjdj;
	}

	public double getSjje() {
		return sjje;
	}

	public void setSjje(double sjje) {
		this.sjje = sjje;
	}

	public double getOldwzkc() {
		return oldwzkc;
	}

	public void setOldwzkc(double oldwzkc) {
		this.oldwzkc = oldwzkc;
	}

	public double getOldkc() {
		return oldkc;
	}

	public void setOldkc(double oldkc) {
		this.oldkc = oldkc;
	}
	
}
