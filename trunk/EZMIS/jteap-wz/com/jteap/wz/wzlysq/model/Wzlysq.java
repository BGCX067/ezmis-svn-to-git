package com.jteap.wz.wzlysq.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_WZ_YLYSQ")
public class Wzlysq {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "BH")
	private String bh;
	
	@Column(name = "CZR")
	private String czr;
	
	@Column(name = "LYBM")
	private String lybm;
	
	@Column(name = "GCLB")
	private String gclb;
	
	@Column(name = "SQSJ")
	private java.util.Date sqsj;
	
	@Column(name = "GCXM")
	private String gcxm;
	
	@Column(name = "ZT")
	private String zt;
	
	@Column(name = "XQJHSQBH")
	private String xqjhsqbh;
	
	@Column(name = "SQBMMC")
	private String sqbmmc;
	
	@Column(name = "CZYXM")
	private String czyxm;
	
	@Column(name = "CZY")
	private String czy;
	
	@Column(name = "FLOW_STATUS")
	private String flow_status;
	
	@Column(name = "LYDQF")
	private String lydqf;
	
	// 所有条目明细
	@OneToMany(mappedBy="wzlysq")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<WzlysqDetail> xqjhsqDetail;//物资档案

	@Column(name = "XQJHMXINFO")
	private String xqjhmxinfo;;     //需求计划信息明细（wzbm，xqjhmxid）获取物资在流程中的状态（可 申领）
	
	public String getXqjhmxinfo() {
		return xqjhmxinfo;
	}

	public void setXqjhmxinfo(String xqjhmxinfo) {
		this.xqjhmxinfo = xqjhmxinfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getLybm() {
		return lybm;
	}

	public void setLybm(String lybm) {
		this.lybm = lybm;
	}

	public String getGclb() {
		return gclb;
	}

	public void setGclb(String gclb) {
		this.gclb = gclb;
	}

	public java.util.Date getSqsj() {
		return sqsj;
	}

	public void setSqsj(java.util.Date sqsj) {
		this.sqsj = sqsj;
	}

	public String getGcxm() {
		return gcxm;
	}

	public void setGcxm(String gcxm) {
		this.gcxm = gcxm;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getXqjhsqbh() {
		return xqjhsqbh;
	}

	public void setXqjhsqbh(String xqjhsqbh) {
		this.xqjhsqbh = xqjhsqbh;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getCzyxm() {
		return czyxm;
	}

	public void setCzyxm(String czyxm) {
		this.czyxm = czyxm;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.czy = czy;
	}

	public String getFlow_status() {
		return flow_status;
	}

	public void setFlow_status(String flow_status) {
		this.flow_status = flow_status;
	}

	public Set<WzlysqDetail> getXqjhsqDetail() {
		return xqjhsqDetail;
	}

	public void setXqjhsqDetail(Set<WzlysqDetail> xqjhsqDetail) {
		this.xqjhsqDetail = xqjhsqDetail;
	}

	public String getLydqf() {
		return lydqf;
	}

	public void setLydqf(String lydqf) {
		this.lydqf = lydqf;
	}

}
