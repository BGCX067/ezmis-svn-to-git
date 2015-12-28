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
 * 用油情况分析
 * @author lvchao
 *
 */
@Entity
@Table(name = "TB_FORM_SJBZB_YYFX")
public class Yyqkfx {
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

	// 用油量
	@Column(name = "YYL")
	private String yyl;
	
	//次数
	@Column(name = "CS")
	private String cs;
	 
	//项目名称
	@Column(name = "XMMC")
	private String xmmc;

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

	public String getYyl() {
		return yyl;
	}

	public void setYyl(String yyl) {
		this.yyl = yyl;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	
	
}
