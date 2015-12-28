package com.jteap.wz.sfjctj.model;

import java.util.Date;

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
import com.jteap.wz.wzda.model.Wzda;

@Entity
@Table(name = "TB_WZ_TWZSFJCB")
public class Sfjc {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;//id
	
	@Column(name = "NF")
	private String nf;  		//年份
	
	@Column(name = "YF")
	private String yf;			//月份
	
	@ManyToOne()
	@JoinColumn(name="CK")
	@LazyToOne(LazyToOneOption.PROXY)
	private Ckgl ck;			//仓库
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	private Wzda wz;			//物资编码
	
	@Column(name = "SQJCSL")
	private Double sqjcsl;		//上期结存数量
	
	@Column(name = "SQJCJE")	//上期结存金额
	private Double sqjcje;
	
	@Column(name = "BQSRSL")	//本期收入数量
	private Double bqsrsl;
	
	@Column(name = "BQSRJE")	//本期收入金额
	private Double bqsrje;
	
	@Column(name = "BQZCSL")	//本期支出数量
	private Double bqzcsl;
	
	@Column(name = "BQZCJE")	//本期支出金额
	private Double bqzcje;
	
	@Column(name = "BQZRSL")	//本期转入数量
	private Double bqzrsl;
	
	@Column(name = "BQZRJE")	//本期转入金额
	private Double bqzrje;
	
	@Column(name = "BQJYSL")	//本期结余数量
	private Double bqjysl;
	
	@Column(name = "BQJYJE")	//本期结余金额
	private Double bqjyje;
	
	@Column(name = "ZJLLSJ")	//资金领用时间
	private Date  zjllsj;
	
	@Column(name = "zjllbz")	//资金领用班组
	private String zjllbz;
	
	@Column(name = "zjllgclb")	//资金领用工程类别
	private String zjllgclb;
	
	@Column(name = "zjllgcxm")	//资金领用工程项目
	private String zjllgcxm;
	
	@Column(name = "tjsj")	//统计时间
	private String tjsj;
	
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

	public Ckgl getCk() {
		return ck;
	}

	public void setCk(Ckgl ck) {
		this.ck = ck;
	}

	public Wzda getWz() {
		return wz;
	}

	public void setWz(Wzda wz) {
		this.wz = wz;
	}

	public Double getSqjcsl() {
		return sqjcsl;
	}

	public void setSqjcsl(Double sqjcsl) {
		this.sqjcsl = sqjcsl;
	}

	public Double getSqjcje() {
		return sqjcje;
	}

	public void setSqjcje(Double sqjcje) {
		this.sqjcje = sqjcje;
	}

	public Double getBqsrsl() {
		return bqsrsl;
	}

	public void setBqsrsl(Double bqsrsl) {
		this.bqsrsl = bqsrsl;
	}

	public Double getBqsrje() {
		return bqsrje;
	}

	public void setBqsrje(Double bqsrje) {
		this.bqsrje = bqsrje;
	}

	public Double getBqzcsl() {
		return bqzcsl;
	}

	public void setBqzcsl(Double bqzcsl) {
		this.bqzcsl = bqzcsl;
	}

	public Double getBqzcje() {
		return bqzcje;
	}

	public void setBqzcje(Double bqzcje) {
		this.bqzcje = bqzcje;
	}

	public Double getBqjysl() {
		return bqjysl;
	}

	public void setBqjysl(Double bqjysl) {
		this.bqjysl = bqjysl;
	}

	public Double getBqjyje() {
		return bqjyje;
	}

	public void setBqjyje(Double bqjyje) {
		this.bqjyje = bqjyje;
	}

	public Date getZjllsj() {
		return zjllsj;
	}

	public void setZjllsj(Date zjllsj) {
		this.zjllsj = zjllsj;
	}

	public String getZjllbz() {
		return zjllbz;
	}

	public void setZjllbz(String zjllbz) {
		this.zjllbz = zjllbz;
	}

	public String getZjllgclb() {
		return zjllgclb;
	}

	public void setZjllgclb(String zjllgclb) {
		this.zjllgclb = zjllgclb;
	}

	public String getZjllgcxm() {
		return zjllgcxm;
	}

	public void setZjllgcxm(String zjllgcxm) {
		this.zjllgcxm = zjllgcxm;
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

	public Double getBqzrsl() {
		return bqzrsl;
	}

	public void setBqzrsl(Double bqzrsl) {
		this.bqzrsl = bqzrsl;
	}

	public Double getBqzrje() {
		return bqzrje;
	}

	public void setBqzrje(Double bqzrje) {
		this.bqzrje = bqzrje;
	}
	
}
