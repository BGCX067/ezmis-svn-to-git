package com.jteap.wz.gdzc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_WZ_GDZCDJMX")
public class GdzcdjMx {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;

	@Column(name = "XH")
	private String XH;
	
	@ManyToOne()
	@JoinColumn(name="GDZCBM")
	@LazyToOne(LazyToOneOption.PROXY)
	private Gdzcdj gdzcdj;
	
	@Column(name = "GDZCMC")
	private String gdzcmc;
	
	@Column(name = "GGXH")
	private String ggxh;
	
	@Column(name = "FSSB")
	private String fssb;
	
	@Column(name = "ZZCJ")
	private String zzcj;
	
	@Column(name = "DW")
	private String dw;
	
	@Column(name = "SL")
	private Double sl;
	
	@Column(name = "DJ")
	private Double dj;
	
	@Column(name = "FJYZ")
	private Double fjyz;
	
	@Column(name = "FPH")
	private String fph;
	
	@Column(name = "JK")
	private Double jk;
	
	@Column(name = "SK")
	private Double sk;
	
	@Column(name = "ZF")
	private Double zf;

	@Column(name = "WZBM")
	private String wzbm;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXH() {
		return XH;
	}

	public void setXH(String xh) {
		XH = xh;
	}

	public Gdzcdj getGdzcdj() {
		return gdzcdj;
	}

	public void setGdzcdj(Gdzcdj gdzcdj) {
		this.gdzcdj = gdzcdj;
	}

	public String getGdzcmc() {
		return gdzcmc;
	}

	public void setGdzcmc(String gdzcmc) {
		this.gdzcmc = gdzcmc;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	public String getFssb() {
		return fssb;
	}

	public void setFssb(String fssb) {
		this.fssb = fssb;
	}

	public String getZzcj() {
		return zzcj;
	}

	public void setZzcj(String zzcj) {
		this.zzcj = zzcj;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public Double getSl() {
		return sl;
	}

	public void setSl(Double sl) {
		this.sl = sl;
	}

	public Double getDj() {
		return dj;
	}

	public void setDj(Double dj) {
		this.dj = dj;
	}

	public Double getFjyz() {
		return fjyz;
	}

	public void setFjyz(Double fjyz) {
		this.fjyz = fjyz;
	}

	public String getFph() {
		return fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
	}

	public Double getJk() {
		return jk;
	}

	public void setJk(Double jk) {
		this.jk = jk;
	}

	public Double getSk() {
		return sk;
	}

	public void setSk(Double sk) {
		this.sk = sk;
	}

	public Double getZf() {
		return zf;
	}

	public void setZf(Double zf) {
		this.zf = zf;
	}

	public String getWzbm() {
		return wzbm;
	}

	public void setWzbm(String wzbm) {
		this.wzbm = wzbm;
	}
	
}
