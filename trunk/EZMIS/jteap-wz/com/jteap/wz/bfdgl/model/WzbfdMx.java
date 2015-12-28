package com.jteap.wz.bfdgl.model;

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

import com.jteap.wz.wzda.model.Wzda;

@Entity
@Table(name = "TB_WZ_YKCBFDMX")
public class WzbfdMx {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "XH")
	private String xh;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzda;
	
	@ManyToOne()
	@JoinColumn(name="BFDBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzbfd wzbfd;
	
	@Column(name = "JLDW")
	private String jldw;
	
	@Column(name = "KCJ")
	private Double kcj;
	
	@Column(name = "BFSL")
	private Double bfsl;
	
	@Column(name = "CLJ")
	private Double clj;
	
	@Column(name = "ZZSL")
	private Double zzsl;
	
	@Column(name = "GLF")
	private Double glf;
	
	@Column(name = "DDYZF")
	private Double ddyzf;
	
	@Column(name = "CLJE")
	private Double clje;
	
	@Column(name = "BFJE")
	private Double bfje;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public Wzda getWzda() {
		return wzda;
	}

	public void setWzda(Wzda wzda) {
		this.wzda = wzda;
	}

	public Wzbfd getWzbfd() {
		return wzbfd;
	}

	public void setWzbfd(Wzbfd wzbfd) {
		this.wzbfd = wzbfd;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public Double getKcj() {
		return kcj;
	}

	public void setKcj(Double kcj) {
		this.kcj = kcj;
	}

	public Double getBfsl() {
		return bfsl;
	}

	public void setBfsl(Double bfsl) {
		this.bfsl = bfsl;
	}

	public Double getClj() {
		return clj;
	}

	public void setClj(Double clj) {
		this.clj = clj;
	}

	public Double getZzsl() {
		return zzsl;
	}

	public void setZzsl(Double zzsl) {
		this.zzsl = zzsl;
	}

	public Double getGlf() {
		return glf;
	}

	public void setGlf(Double glf) {
		this.glf = glf;
	}

	public Double getDdyzf() {
		return ddyzf;
	}

	public void setDdyzf(Double ddyzf) {
		this.ddyzf = ddyzf;
	}

	public Double getClje() {
		return clje;
	}

	public void setClje(Double clje) {
		this.clje = clje;
	}

	public Double getBfje() {
		return bfje;
	}

	public void setBfje(Double bfje) {
		this.bfje = bfje;
	}
	
}
