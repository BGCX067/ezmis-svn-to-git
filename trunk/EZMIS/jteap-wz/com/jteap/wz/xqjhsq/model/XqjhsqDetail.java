/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.xqjhsq.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_WZ_XQJHSQ_DETAIL")
public class XqjhsqDetail{
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@ManyToOne()
	@JoinColumn(name = "XQJHSQID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Xqjhsq xqjhsq;
	
	@Column(name = "WZBM")	
	private java.lang.String wzbm;
	
	
	@Column(name = "XH",nullable=true)	
	private Long xh;
	
	
	@Column(name = "WZMC")	
	private java.lang.String wzmc;
	
	
	@Column(name = "XHGG")	
	private java.lang.String xhgg;
	
	
	@Column(name = "SQSL")	
	private Double sqsl;
	
	
	@Column(name = "JLDW")	
	private java.lang.String jldw;
	
	
	@Column(name = "GJDJ")	
	private Double gjdj;
	
	
	@Column(name = "PROVIDER")	
	private java.lang.String provider;
	
	
	@Column(name = "XYSJ")	
	private java.util.Date xysj;
	
	
	@Column(name = "DONE")	
	private java.lang.String done;
	
	
	@Column(name = "ISNEW")	
	private java.lang.String isnew;
	
	
	@Column(name = "JHY")	
	private java.lang.String jhy;
	
	@Transient
	private String jhyGh;
	
	@Column(name = "SFDH")	
	private java.lang.String sfdh;
	
	
	@Column(name = "IS_CANCEL")	
	private java.lang.String isCancel;
	
	
	@Column(name = "C_FLAG")	
	private java.lang.String cflag;
	
	@Column(name = "XGR")
	private String xgr;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "IS_MOD")
	private String isMod;
	
	@Column(name = "JLMXIDANDSL")
	private String jlmxidandsl;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CGJHMXID")
	private String cgjhmxid;
	
	@Column(name = "DYSZT")
	private String dyszt;
	
	@Column(name = "DYSCZR")
	private String dysczr;
	
	@Column(name = "GDZCMXID")
	private String gdzcmxid;
	
	@Column(name ="NEW_GOODS_MATCH")
	private String new_goods_match;
	
	public double getJe() {
		if(sqsl==null||gjdj==null)
			return 0;
		return this.sqsl*this.gjdj;
	}

	public Xqjhsq getXqjhsq() {
		return xqjhsq;
	}

	public void setXqjhsq(Xqjhsq xqjhsq) {
		this.xqjhsq = xqjhsq;
	}
	
	public java.lang.String getWzbm() {
		return this.wzbm;
	}
	
	public void setWzbm(java.lang.String value) {
		this.wzbm = value;
	}
	public Long getXh() {
		return this.xh;
	}
	
	public Double getGjdj() {
		return gjdj;
	}

	public void setGjdj(Double gjdj) {
		this.gjdj = gjdj;
	}

	public void setXh(Long value) {
		this.xh = value;
	}
	public java.lang.String getWzmc() {
		return this.wzmc;
	}
	
	public void setWzmc(java.lang.String value) {
		this.wzmc = value;
	}
	public java.lang.String getXhgg() {
		return this.xhgg;
	}
	
	public void setXhgg(java.lang.String value) {
		this.xhgg = value;
	}
	public Double getSqsl() {
		return sqsl;
	}

	public void setSqsl(Double sqsl) {
		this.sqsl = sqsl;
	}

	public java.lang.String getJldw() {
		return this.jldw;
	}
	
	public void setJldw(java.lang.String value) {
		this.jldw = value;
	}
	public java.lang.String getProvider() {
		return this.provider;
	}
	
	public void setProvider(java.lang.String value) {
		this.provider = value;
	}
	public java.util.Date getXysj() {
		return this.xysj;
	}
	
	public void setXysj(java.util.Date value) {
		this.xysj = value;
	}
	public java.lang.String getDone() {
		return this.done;
	}
	
	public void setDone(java.lang.String value) {
		this.done = value;
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

	public java.lang.String getIsnew() {
		return this.isnew;
	}
	
	public void setIsnew(java.lang.String value) {
		this.isnew = value;
	}
	public java.lang.String getJhy() {
		return this.jhy;
	}
	
	public void setJhy(java.lang.String value) {
		this.jhy = value;
	}
	public java.lang.String getSfdh() {
		return this.sfdh;
	}
	
	public void setSfdh(java.lang.String value) {
		this.sfdh = value;
	}
	public java.lang.String getIsCancel() {
		return this.isCancel;
	}
	
	public void setIsCancel(java.lang.String value) {
		this.isCancel = value;
	}
	public java.lang.String getCflag() {
		return this.cflag;
	}
	
	public void setCflag(java.lang.String value) {
		this.cflag = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}

	public String getJhyGh() {
		return jhyGh;
	}

	public void setJhyGh(String jhyGh) {
		this.jhyGh = jhyGh;
	}

	public String getXgr() {
		return xgr;
	}

	public void setXgr(String xgr) {
		this.xgr = xgr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsMod() {
		return isMod;
	}

	public void setIsMod(String isMod) {
		this.isMod = isMod;
	}

	public String getJlmxidandsl() {
		return jlmxidandsl;
	}

	public void setJlmxidandsl(String jlmxidandsl) {
		this.jlmxidandsl = jlmxidandsl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCgjhmxid() {
		return cgjhmxid;
	}

	public void setCgjhmxid(String cgjhmxid) {
		this.cgjhmxid = cgjhmxid;
	}

	public String getGdzcmxid() {
		return gdzcmxid;
	}

	public void setGdzcmxid(String gdzcmxid) {
		this.gdzcmxid = gdzcmxid;
	}

	public void setNew_goods_match(String new_goods_match) {
		this.new_goods_match = new_goods_match;
	}

	public String getNew_goods_match() {
		return new_goods_match;
	}
}

