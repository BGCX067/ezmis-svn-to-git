package com.jteap.yx.aqyxfx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
/**
 * 被考核电量月统计  
 * @author lvchao
 *
 */
@Entity
@Table(name = "TB_FORM_SJBZB_BKHDLYTJ")
public class BkhdlTj {
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
	@Column(name = "TJR")
	private String tjr;

	// 统计时间
	@Column(name = "TJSJ")
	private Date tjsj;

	// 原因名称
	@Column(name = "YYMC")
	private String yymc;
	
	//原因编号
	@Column(name = "YYBH")
	private String yybh;
	
	//考核值别
	@Column(name = "KHZB")
	private String khzb;
	
	//值别编号
	@Column(name = "ZBBH")
	private int zbbh;
	
	//考核电量
	@Column(name = "KHDL")
	private String khdl;

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

	public String getTjr() {
		return tjr;
	}

	public void setTjr(String tjr) {
		this.tjr = tjr;
	}

	public Date getTjsj() {
		return tjsj;
	}

	public void setTjsj(Date tjsj) {
		this.tjsj = tjsj;
	}

	public String getYymc() {
		return yymc;
	}

	public void setYymc(String yymc) {
		this.yymc = yymc;
	}

	public String getYybh() {
		return yybh;
	}

	public void setYybh(String yybh) {
		this.yybh = yybh;
	}

	public String getKhzb() {
		return khzb;
	}

	public void setKhzb(String khzb) {
		this.khzb = khzb;
	}


	public int getZbbh() {
		return zbbh;
	}

	public void setZbbh(int zbbh) {
		this.zbbh = zbbh;
	}

	public String getKhdl() {
		return khdl;
	}

	public void setKhdl(String khdl) {
		this.khdl = khdl;
	}

	
}
