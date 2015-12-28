package com.jteap.jx.qxgl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 缺陷统计表
 * @author wangyun
 *
 */
@Entity
@Table(name = "TB_JX_QXGL_QXTJ")
public class Qxtj {

	// 编号
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 电厂统计期
	@Column(name = "DCTJQ")
	private String dctjq;

	// 填报人
	@Column(name = "TBR")
	private String tbr;

	// 统计时间
	@Column(name = "TJSJ")
	private Date tjsj;

	// 统计时间
	@Column(name = "TJKSSJ")
	private Date tjkssj;
	
	// 统计时间
	@Column(name = "TJJSSJ")
	private Date tjjssj;
	
	// 项目名称
	@Column(name = "XMMC")
	private String xmmc;

	// 锅炉
	@Column(name = "gl")
	private String gl;

	// 汽机
	@Column(name = "qj")
	private String qj;
	// 电气
	@Column(name = "dq")
	private String dq;
	// 燃料
	//@Column(name = "rl")
	//private String rl;
	// 热工
	@Column(name = "rg")
	private String rg;
	// 合计
	@Column(name = "hj")
	private String hj;
	// 年总计
	@Column(name = "nzj")
	private String nzj;

	// 本月消缺情况
	@Column(name = "BYXQQK")
	private String byxqqk;

	// 本月转接缺陷情况
	@Column(name = "BYZJQXQK")
	private String byzjqxqk;

	// 以往转接缺陷消除情况
	@Column(name = "YWZJQXXQQK")
	private String ywzjqxxqqk;
	
	//是厂内统计或报集团统计
	@Column(name = "FL")
	private String fl;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDctjq() {
		return dctjq;
	}

	public void setDctjq(String dctjq) {
		this.dctjq = dctjq;
	}

	public String getTbr() {
		return tbr;
	}

	public void setTbr(String tbr) {
		this.tbr = tbr;
	}

	public Date getTjsj() {
		return tjsj;
	}

	public void setTjsj(Date tjsj) {
		this.tjsj = tjsj;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getGl() {
		return gl;
	}

	public void setGl(String gl) {
		this.gl = gl;
	}

	public String getQj() {
		return qj;
	}

	public void setQj(String qj) {
		this.qj = qj;
	}

	public String getDq() {
		return dq;
	}

	public void setDq(String dq) {
		this.dq = dq;
	}

//	public String getRl() {
//		return rl;
//	}
//
//	public void setRl(String rl) {
//		this.rl = rl;
//	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getHj() {
		return hj;
	}

	public void setHj(String hj) {
		this.hj = hj;
	}

	public String getNzj() {
		return nzj;
	}

	public void setNzj(String nzj) {
		this.nzj = nzj;
	}

	public String getByxqqk() {
		return byxqqk;
	}

	public void setByxqqk(String byxqqk) {
		this.byxqqk = byxqqk;
	}

	public String getByzjqxqk() {
		return byzjqxqk;
	}

	public void setByzjqxqk(String byzjqxqk) {
		this.byzjqxqk = byzjqxqk;
	}

	public String getYwzjqxxqqk() {
		return ywzjqxxqqk;
	}

	public void setYwzjqxxqqk(String ywzjqxxqqk) {
		this.ywzjqxxqqk = ywzjqxxqqk;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public Date getTjkssj() {
		return tjkssj;
	}

	public void setTjkssj(Date tjkssj) {
		this.tjkssj = tjkssj;
	}

	public Date getTjjssj() {
		return tjjssj;
	}

	public void setTjjssj(Date tjjssj) {
		this.tjjssj = tjjssj;
	}
	
}
