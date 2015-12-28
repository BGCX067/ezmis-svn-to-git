package com.jteap.sb.sbpjgl.model;

import java.util.Date;

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

/**
 * 设备评级实体
 * @author caofei
 *
 */
@Entity
@Table(name = "TB_SB_SBPJGL_PJXX")
public class Sbpj {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="PJFL_CATALOG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private SbpjCatalog sbpjCatalog;
	
	@Column(name = "pjfl")
	private String pjfl;
	
	@Column(name = "sbbm")
	private String sbbm;
	
	@Column(name = "sbmc")
	private String sbmc;
	
	@Column(name = "sbgg")
	private String sbgg;
	
	@Column(name = "scpjrq")
	private Date scpjrq;
	
	@Column(name = "scpjjb")
	private String scpjjb;
	
	@Column(name = "scpjr")
	private String scpjr;
	
	@Column(name = "bcpjrq")
	private Date bcpjrq;
	
	@Column(name = "bcpjjb")
	private String bcpjjb;
	
	@Column(name = "bcpjr")
	private String bcpjr;
	
	@Column(name = "remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SbpjCatalog getSbpjCatalog() {
		return sbpjCatalog;
	}

	public void setSbpjCatalog(SbpjCatalog sbpjCatalog) {
		this.sbpjCatalog = sbpjCatalog;
	}

	public String getSbbm() {
		return sbbm;
	}

	public void setSbbm(String sbbm) {
		this.sbbm = sbbm;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getSbgg() {
		return sbgg;
	}

	public void setSbgg(String sbgg) {
		this.sbgg = sbgg;
	}

	public Date getScpjrq() {
		return scpjrq;
	}

	public void setScpjrq(Date scpjrq) {
		this.scpjrq = scpjrq;
	}

	public String getScpjjb() {
		return scpjjb;
	}

	public void setScpjjb(String scpjjb) {
		this.scpjjb = scpjjb;
	}

	public String getScpjr() {
		return scpjr;
	}

	public void setScpjr(String scpjr) {
		this.scpjr = scpjr;
	}

	public Date getBcpjrq() {
		return bcpjrq;
	}

	public void setBcpjrq(Date bcpjrq) {
		this.bcpjrq = bcpjrq;
	}

	public String getBcpjjb() {
		return bcpjjb;
	}

	public void setBcpjjb(String bcpjjb) {
		this.bcpjjb = bcpjjb;
	}

	public String getBcpjr() {
		return bcpjr;
	}

	public void setBcpjr(String bcpjr) {
		this.bcpjr = bcpjr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPjfl() {
		return pjfl;
	}

	public void setPjfl(String pjfl) {
		this.pjfl = pjfl;
	}
	
}
