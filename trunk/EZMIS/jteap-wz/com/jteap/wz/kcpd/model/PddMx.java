package com.jteap.wz.kcpd.model;

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
@Table(name = "TB_WZ_YPDDMX")
public class PddMx {
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
	@JoinColumn(name="PDDBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Pdd pdd;
	
	@Column(name = "PQSL")
	private Double pqsl;
	
	@Column(name = "PQJE")
	private Double pqje;
	
	@Column(name = "PJJ")
	private Double pjj;
	
	@Column(name = "PDSL")
	private Double pdsl;
	
	@Column(name = "ZKSJ")
	private Double zksj;
	
	@Column(name = "SLCY")
	private Double slcy;
	
	@Column(name = "JECY")
	private Double jecy;
	
	@Column(name = "CYYY")
	private String cyyy;
	
	@Column(name = "ZT")
	private String zt;

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

	public Pdd getPdd() {
		return pdd;
	}

	public void setPdd(Pdd pdd) {
		this.pdd = pdd;
	}

 
	public Double getPqje() {
		return pqje;
	}

	public void setPqje(Double pqje) {
		this.pqje = pqje;
	}

	public Double getZksj() {
		return zksj;
	}

	public void setZksj(Double zksj) {
		this.zksj = zksj;
	}

	public Double getSlcy() {
		return slcy;
	}

	public void setSlcy(Double slcy) {
		this.slcy = slcy;
	}

	public Double getJecy() {
		return jecy;
	}

	public void setJecy(Double jecy) {
		this.jecy = jecy;
	}

	public String getCyyy() {
		return cyyy;
	}

	public void setCyyy(String cyyy) {
		this.cyyy = cyyy;
	}

	public Double getPqsl() {
		return pqsl;
	}

	public void setPqsl(Double pqsl) {
		this.pqsl = pqsl;
	}

	public Double getPdsl() {
		return pdsl;
	}

	public void setPdsl(Double pdsl) {
		this.pdsl = pdsl;
	}

	public Double getPjj() {
		return pjj;
	}

	public void setPjj(Double pjj) {
		this.pjj = pjj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
	
}
