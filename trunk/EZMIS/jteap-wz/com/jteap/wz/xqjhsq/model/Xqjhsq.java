package com.jteap.wz.xqjhsq.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_WZ_XQJHSQ")
public class Xqjhsq {

	// columns START

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "XQJHSQBH")
	private java.lang.String xqjhsqbh;

	@Column(name = "GCLB")
	private java.lang.String gclb;

	@Column(name = "GCXM")
	private java.lang.String gcxm;

	@Column(name = "SQBM")
	private java.lang.String sqbm;
	
	@Column(name = "SQSJ")
	private java.util.Date sqsj;

	@Column(name = "CZY")
	private java.lang.String czy;

	@Column(name = "CZYXM")
	private java.lang.String czyxm;

	@Column(name = "FLOW_STATUS")
	private String flowStatus;

	@Column(name = "IS_BACK")
	private String isBack;

	@Column(name = "IS_UPDATE")
	private String isUpdate;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "XJSJSZ", columnDefinition = "CLOB", nullable = true)
	private String xjsjsz;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "XGSJSZ", columnDefinition = "CLOB", nullable = true)
	private String xgsjsz;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "SCSJSZ", columnDefinition = "CLOB", nullable = true)
	private String scsjsz;

	@Column(name = "XQJHQF")
	private String xqjhqf;

	@Column(name = "LYDID")
	private String lydid;

	@Column(name = "QMZT")
	private String qmzt;

	@Column(name = "DYSZT")
	private String dyszt; 
	
	@Column(name = "DYSCZR")
	private String dysczr;
	
	@Column(name = "YHDID")
	private String yhdid;

	// 所有条目明细
	@OneToMany(mappedBy = "xqjhsq")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<XqjhsqDetail> xqjhsqDetail;// 物资档案
	
	@Column(name = "GDZCID")
	private String  gdzc;
	
	public Set<XqjhsqDetail> getXqjhsqDetail() {
		return xqjhsqDetail;
	}

	public void setXqjhsqDetail(Set<XqjhsqDetail> xqjhsqDetail) {
		this.xqjhsqDetail = xqjhsqDetail;
	}

	public java.lang.String getCzy() {
		return czy;
	}

	public void setCzy(java.lang.String czy) {
		this.czy = czy;
	}

	public java.lang.String getCzyxm() {
		return czyxm;
	}

	public void setCzyxm(java.lang.String czyxm) {
		this.czyxm = czyxm;
	}

	public java.lang.String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(java.lang.String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	@Column(name = "SQBMMC")
	private java.lang.String sqbmmc;

	@Column(name = "STATUS")
	private java.lang.String status;

	@Column(name = "C_LCBH")
	private java.lang.String clcbh;

	@Column(name = "GCXMBH")
	private java.lang.String gcxmbh;

	@Column(name = "C_FLAG")
	private java.lang.String cflag;

	@Column(name = "FPSJ")
	private java.util.Date fpsj;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getGclb() {
		return this.gclb;
	}

	public void setGclb(java.lang.String value) {
		this.gclb = value;
	}

	public java.lang.String getGcxm() {
		return this.gcxm;
	}

	public void setGcxm(java.lang.String value) {
		this.gcxm = value;
	}

	public java.lang.String getSqbm() {
		return this.sqbm;
	}

	public void setSqbm(java.lang.String value) {
		this.sqbm = value;
	}

	public java.util.Date getSqsj() {
		return this.sqsj;
	}

	public void setSqsj(java.util.Date value) {
		this.sqsj = value;
	}

	public java.lang.String getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.String value) {
		this.status = value;
	}

	public java.lang.String getClcbh() {
		return this.clcbh;
	}

	public void setClcbh(java.lang.String value) {
		this.clcbh = value;
	}

	public java.lang.String getGcxmbh() {
		return this.gcxmbh;
	}

	public void setGcxmbh(java.lang.String value) {
		this.gcxmbh = value;
	}

	public java.lang.String getCflag() {
		return this.cflag;
	}

	public void setCflag(java.lang.String value) {
		this.cflag = value;
	}

	public java.util.Date getFpsj() {
		return this.fpsj;
	}

	public void setFpsj(java.util.Date value) {
		this.fpsj = value;
	}

	public java.lang.String getXqjhsqbh() {
		return xqjhsqbh;
	}

	public void setXqjhsqbh(java.lang.String xqjhsqbh) {
		this.xqjhsqbh = xqjhsqbh;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getIsBack() {
		return isBack;
	}

	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getLydid() {
		return lydid;
	}

	public void setLydid(String lydid) {
		this.lydid = lydid;
	}

	public String getQmzt() {
		return qmzt;
	}

	public void setQmzt(String qmzt) {
		this.qmzt = qmzt;
	}

	public String getXqjhqf() {
		return xqjhqf;
	}

	public void setXqjhqf(String xqjhqf) {
		this.xqjhqf = xqjhqf;
	}

	public String getYhdid() {
		return yhdid;
	}

	public void setYhdid(String yhdid) {
		this.yhdid = yhdid;
	}

	public String getXjsjsz() {
		return xjsjsz;
	}

	public void setXjsjsz(String xjsjsz) {
		this.xjsjsz = xjsjsz;
	}

	public String getXgsjsz() {
		return xgsjsz;
	}

	public void setXgsjsz(String xgsjsz) {
		this.xgsjsz = xgsjsz;
	}

	public String getScsjsz() {
		return scsjsz;
	}

	public void setScsjsz(String scsjsz) {
		this.scsjsz = scsjsz;
	}

	public String getDyszt() {
		return dyszt;
	}

	public void setDyszt(String dyszt) {
		this.dyszt = dyszt;
	}

	public String getDysczr() {
		return dysczr;
	}

	public void setDysczr(String dysczr) {
		this.dysczr = dysczr;
	}

	public String getGdzc() {
		return gdzc;
	}

	public void setGdzc(String gdzc) {
		this.gdzc = gdzc;
	}
	
}
