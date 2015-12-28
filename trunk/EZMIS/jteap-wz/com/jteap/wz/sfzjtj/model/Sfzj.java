/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.sfzjtj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.tjny.model.Tjny;
/**
 * 物资资金收发统计
 * @author lvchao
 *
 */

@Entity
@Table(name = "TB_WZ_TWZSFZJDTB")
public class Sfzj{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@ManyToOne()
	@JoinColumn(name="CK")
	@LazyToOne(LazyToOneOption.FALSE)
	private Ckgl ck;// 仓库名称
	
	@Column(name = "Nf")
	private String nf;
	
	@Column(name = "Yf")
	private String yf;
	
	@Column(name = "YCKC")
	private Double yckc;//月初库存
	
	@Column(name = "RKDS")
	private Double rkd;//入库单
	
	@Column(name = "RKJE")
	private Double rkje;	//入库金额
	
	@Column(name = "CKDS")
	private Double ckd;	//出库单
	
	@Column(name = "CKJE")
	private Double ckje;	//出库金额
	
	@Column(name = "ZRJE")
	private Double zrje;//入库单
	
	@Column(name = "YMKC")
	private Double ymkc;	//月末库存
	
	@Column(name = "TJSJ")
	private String tjsj;	//统计时间
	
	@ManyToOne()
	@JoinColumn(name="TJNY")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Tjny tjny;	//统计年月

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Ckgl getCk() {
		return ck;
	}

	public void setCk(Ckgl ck) {
		this.ck = ck;
	}

	public String getNf() {
		return nf;
	}

	public void setNf(String nf) {
		this.nf = nf;
	}

	public String getYf() {
		return yf;
	}

	public void setYf(String yf) {
		this.yf = yf;
	}

	public Double getYckc() {
		return yckc;
	}

	public void setYckc(Double yckc) {
		this.yckc = yckc;
	}

	public Double getRkd() {
		return rkd;
	}

	public void setRkd(Double rkd) {
		this.rkd = rkd;
	}

	public Double getRkje() {
		return rkje;
	}

	public void setRkje(Double rkje) {
		this.rkje = rkje;
	}

	public Double getCkd() {
		return ckd;
	}

	public void setCkd(Double ckd) {
		this.ckd = ckd;
	}

	public Double getCkje() {
		return ckje;
	}

	public void setCkje(Double ckje) {
		this.ckje = ckje;
	}

	public Double getYmkc() {
		return ymkc;
	}

	public void setYmkc(Double ymkc) {
		this.ymkc = ymkc;
	}

	public Tjny getTjny() {
		return tjny;
	}

	public void setTjny(Tjny tjny) {
		this.tjny = tjny;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}

	public Double getZrje() {
		return zrje;
	}

	public void setZrje(Double zrje) {
		this.zrje = zrje;
	}
	
}

