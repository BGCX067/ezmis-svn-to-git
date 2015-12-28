package com.jteap.jhtj.dnb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 描述:电能表模型
 * 时间:2010 11 9
 * 作者:tngbei
 * 参数:
 * 返回值:
 * 抛出异常:
 */
@Entity
@Table(name="elec.elec_param")
public class Dnb {
	@Id
	@Column(name="ELECID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="name")
	private String name;
	@Column(name="bv")
	private Integer bv;
	@Column(name="pz")
	private String pz;
	@Column(name="pf")
	private String pf;
	@Column(name="qz")
	private String qz;
	@Column(name="qf")
	private String qf;
	@Column(name="zyf")
	private String zyf;
	@Column(name="zyp")
	private String zyp;
	@Column(name="zyg")
	private String zyg;
	@Column(name="fyf")
	private String fyf;
	@Column(name="fyp")
	private String fyp;
	@Column(name="fyg")
	private String fyg;
	@Column(name="zwf")
	private String zwf;
	@Column(name="zwp")
	private String zwp;
	@Column(name="zwg")
	private String zwg;
	@Column(name="fwf")
	private String fwf;
	@Column(name="fwp")
	private String fwp;
	@Column(name="fwg")
	private String fwg;
	@Column(name="t1")
	private String t1;
	@Column(name="t1n")
	private String t1n;
	@Column(name="t2")
	private String t2;
	@Column(name="t2n")
	private String t2n;
	@Column(name="t3")
	private String t3;
	@Column(name="t3n")
	private String t3n;
	@Column(name="pt")
	private Integer pt;
	@Column(name="ct")
	private Integer ct;
	@Column(name="pts")
	private String pts;
	@Column(name="cts")
	private String cts;
	@Column(name="elecbh")
	private String elecbh;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getBv() {
		return bv;
	}
	public void setBv(Integer bv) {
		this.bv = bv;
	}
	public String getPz() {
		return pz;
	}
	public void setPz(String pz) {
		this.pz = pz;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getQz() {
		return qz;
	}
	public void setQz(String qz) {
		this.qz = qz;
	}
	public String getQf() {
		return qf;
	}
	public void setQf(String qf) {
		this.qf = qf;
	}
	public String getZyf() {
		return zyf;
	}
	public void setZyf(String zyf) {
		this.zyf = zyf;
	}
	public String getZyp() {
		return zyp;
	}
	public void setZyp(String zyp) {
		this.zyp = zyp;
	}
	public String getZyg() {
		return zyg;
	}
	public void setZyg(String zyg) {
		this.zyg = zyg;
	}
	public String getFyf() {
		return fyf;
	}
	public void setFyf(String fyf) {
		this.fyf = fyf;
	}
	public String getFyp() {
		return fyp;
	}
	public void setFyp(String fyp) {
		this.fyp = fyp;
	}
	public String getFyg() {
		return fyg;
	}
	public void setFyg(String fyg) {
		this.fyg = fyg;
	}
	public String getZwf() {
		return zwf;
	}
	public void setZwf(String zwf) {
		this.zwf = zwf;
	}
	public String getZwp() {
		return zwp;
	}
	public void setZwp(String zwp) {
		this.zwp = zwp;
	}
	public String getZwg() {
		return zwg;
	}
	public void setZwg(String zwg) {
		this.zwg = zwg;
	}
	public String getFwf() {
		return fwf;
	}
	public void setFwf(String fwf) {
		this.fwf = fwf;
	}
	public String getFwp() {
		return fwp;
	}
	public void setFwp(String fwp) {
		this.fwp = fwp;
	}
	public String getFwg() {
		return fwg;
	}
	public void setFwg(String fwg) {
		this.fwg = fwg;
	}
	public String getT1() {
		return t1;
	}
	public void setT1(String t1) {
		this.t1 = t1;
	}
	public String getT1n() {
		return t1n;
	}
	public void setT1n(String t1n) {
		this.t1n = t1n;
	}
	public String getT2() {
		return t2;
	}
	public void setT2(String t2) {
		this.t2 = t2;
	}
	public String getT2n() {
		return t2n;
	}
	public void setT2n(String t2n) {
		this.t2n = t2n;
	}
	public String getT3() {
		return t3;
	}
	public void setT3(String t3) {
		this.t3 = t3;
	}
	public String getT3n() {
		return t3n;
	}
	public void setT3n(String t3n) {
		this.t3n = t3n;
	}
	public Integer getPt() {
		return pt;
	}
	public void setPt(Integer pt) {
		this.pt = pt;
	}
	public Integer getCt() {
		return ct;
	}
	public void setCt(Integer ct) {
		this.ct = ct;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPts() {
		return pts;
	}
	public void setPts(String pts) {
		this.pts = pts;
	}
	public String getCts() {
		return cts;
	}
	public void setCts(String cts) {
		this.cts = cts;
	}
	public String getElecbh() {
		return elecbh;
	}
	public void setElecbh(String elecbh) {
		this.elecbh = elecbh;
	}
	
}
