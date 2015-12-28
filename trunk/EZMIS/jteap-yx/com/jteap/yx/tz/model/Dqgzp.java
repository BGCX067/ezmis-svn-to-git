/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.tz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 电气工作票实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_YX_TZ_DQGZP")
public class Dqgzp {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;

	@Column(name = "GZPBH")
	private String gzpbh;

	@Column(name = "GZPZT")
	private String gzpzt;

	@Column(name = "DDJRW")
	private String ddjrw;

	@Column(name = "GZFZR")
	private String gzfzr;

	@Column(name = "GZBRY")
	private String gzbry;

	@Column(name = "GZPDJR")
	private String gzpdjr;

	@Column(name = "SPSJ")
	private Date spsj;

	@Column(name = "XKSJ")
	private Date xksj;

	@Column(name = "GZPZJR")
	private String gzpzjr;

	@Column(name = "ZJSJ")
	private Date zjsj;

	@Column(name = "GZPZFR")
	private String gzpzfr;

	@Column(name = "ZFSJ")
	private Date zfsj;

	@Column(name = "ZJJD")
	private String zjjd;

	@Column(name = "SPR")
	private String spr;

	@Column(name = "PZGZQX")
	private Date pzgzqx;

	@Column(name = "PZZZ")
	private String pzzz;

	@Column(name = "XKR")
	private String xkr;

	@Column(name = "YQSJ")
	private Date yqsj;

	@Column(name = "PZYQZZ")
	private String pzyqzz;

	@Column(name = "YQSXSJ")
	private Date yqsxsj;

	@Column(name = "ZFYY")
	private String zfyy;

	@Column(name = "ZJJCQK")
	private String zjjcqk;

	@Column(name = "PZZZMC")
	private String pzzzmc;

	@Column(name = "XKRMC")
	private String xkrmc;

	@Column(name = "ZJRMC")
	private String zjrmc;

	@Column(name = "PZYQZZMC")
	private String pzyqzzmc;

	@Column(name = "ZFRMC")
	private String zfrmc;

	@Column(name = "type")
	private String type;

	public String getId() {
		return this.id;
	}

	public String getGzpbh() {
		return gzpbh;
	}

	public void setGzpbh(String gzpbh) {
		this.gzpbh = gzpbh;
	}

	public String getGzpzt() {
		return gzpzt;
	}

	public void setGzpzt(String gzpzt) {
		this.gzpzt = gzpzt;
	}

	public String getDdjrw() {
		return ddjrw;
	}

	public void setDdjrw(String ddjrw) {
		this.ddjrw = ddjrw;
	}

	public String getGzfzr() {
		return gzfzr;
	}

	public void setGzfzr(String gzfzr) {
		this.gzfzr = gzfzr;
	}

	public String getGzbry() {
		return gzbry;
	}

	public void setGzbry(String gzbry) {
		this.gzbry = gzbry;
	}

	public String getGzpdjr() {
		return gzpdjr;
	}

	public void setGzpdjr(String gzpdjr) {
		this.gzpdjr = gzpdjr;
	}

	public Date getSpsj() {
		return spsj;
	}

	public void setSpsj(Date spsj) {
		this.spsj = spsj;
	}

	public Date getXksj() {
		return xksj;
	}

	public void setXksj(Date xksj) {
		this.xksj = xksj;
	}

	public String getGzpzjr() {
		return gzpzjr;
	}

	public void setGzpzjr(String gzpzjr) {
		this.gzpzjr = gzpzjr;
	}

	public Date getZjsj() {
		return zjsj;
	}

	public void setZjsj(Date zjsj) {
		this.zjsj = zjsj;
	}

	public String getGzpzfr() {
		return gzpzfr;
	}

	public void setGzpzfr(String gzpzfr) {
		this.gzpzfr = gzpzfr;
	}

	public Date getZfsj() {
		return zfsj;
	}

	public void setZfsj(Date zfsj) {
		this.zfsj = zfsj;
	}

	public String getZjjd() {
		return zjjd;
	}

	public void setZjjd(String zjjd) {
		this.zjjd = zjjd;
	}

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public Date getPzgzqx() {
		return pzgzqx;
	}

	public void setPzgzqx(Date pzgzqx) {
		this.pzgzqx = pzgzqx;
	}

	public String getPzzz() {
		return pzzz;
	}

	public void setPzzz(String pzzz) {
		this.pzzz = pzzz;
	}

	public String getXkr() {
		return xkr;
	}

	public void setXkr(String xkr) {
		this.xkr = xkr;
	}

	public Date getYqsj() {
		return yqsj;
	}

	public void setYqsj(Date yqsj) {
		this.yqsj = yqsj;
	}

	public String getPzyqzz() {
		return pzyqzz;
	}

	public void setPzyqzz(String pzyqzz) {
		this.pzyqzz = pzyqzz;
	}

	public Date getYqsxsj() {
		return yqsxsj;
	}

	public void setYqsxsj(Date yqsxsj) {
		this.yqsxsj = yqsxsj;
	}

	public String getZfyy() {
		return zfyy;
	}

	public void setZfyy(String zfyy) {
		this.zfyy = zfyy;
	}

	public String getZjjcqk() {
		return zjjcqk;
	}

	public void setZjjcqk(String zjjcqk) {
		this.zjjcqk = zjjcqk;
	}

	public String getPzzzmc() {
		return pzzzmc;
	}

	public void setPzzzmc(String pzzzmc) {
		this.pzzzmc = pzzzmc;
	}

	public String getXkrmc() {
		return xkrmc;
	}

	public void setXkrmc(String xkrmc) {
		this.xkrmc = xkrmc;
	}

	public String getZjrmc() {
		return zjrmc;
	}

	public void setZjrmc(String zjrmc) {
		this.zjrmc = zjrmc;
	}

	public String getPzyqzzmc() {
		return pzyqzzmc;
	}

	public void setPzyqzzmc(String pzyqzzmc) {
		this.pzyqzzmc = pzyqzzmc;
	}

	public String getZfrmc() {
		return zfrmc;
	}

	public void setZfrmc(String zfrmc) {
		this.zfrmc = zfrmc;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
