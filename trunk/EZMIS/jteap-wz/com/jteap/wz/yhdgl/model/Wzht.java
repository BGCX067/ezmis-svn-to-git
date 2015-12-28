package com.jteap.wz.yhdgl.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_HT_WZHT")
public class Wzht {
//columns START
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "HTBH")	
	private String htbh ;
	
	@Column(name = "HTMC")	
	private String htmc ;
	
	@Column(name = "HTLX")	
	private String htlx ;
	
	@Column(name = "STATUS")	
	private String status ;
	
	@Column(name = "CJSJ")	
	private String cjsj ;
	
	@Column(name = "SQBM")	
	private String sqbm ;
	
	@Column(name = "SQR")	
	private String sqr ;
	
	@Column(name = "JHSQDH")	
	private String jhsqdh ;
	
	@Column(name = "GFDW")	
	private String gfdw ;
	
	@Column(name = "QDDD")	
	private String qddd ;
	
	@Column(name = "XFDW")	
	private String xfdw ;
	
	@Column(name = "QTSM")	
	private String qtsm ;
	
	@Column(name = "HTJE")	
	private Double htje ;
	
	@Column(name = "HTJBRYJ")	
	private String htjbryj ;
	
	@Column(name = "HTJBR")	
	private String htjbr ;
	
	@Column(name = "HTJBRQMSJ")	
	private String htjbrqmsj ;
	
	@Column(name = "JHJYBFZRYJ")	
	private String jhjybfzryj ;
	
	@Column(name = "JHJYBFZR")	
	private String jhjybfzr ;
	
	@Column(name = "JHJYBFZRQMSJ")	
	private String jhjybfzrqmsj ;
	
	@Column(name = "JHJYBZRYJ")	
	private String jhjybzryj ;
	
	@Column(name = "JHJYBZR")	
	private String jhjybzr ;
	
	@Column(name = "JHJYBZRQMSJ")	
	private String jhjybzrqmsj ;
	
	@Column(name = "ZGFZJLYJ")	
	private String zgfzjlyj ;
	
	@Column(name = "ZGFZJL")	
	private String zgfzjl ;
	
	@Column(name = "ZGFZJLQMSJ")	
	private String zgfzjlqmsj ;
	
	@Column(name = "HTQDR")	
	private String htqdr ;
	
	@Column(name = "ZJL")	
	private String zjl ;
	
	@Column(name = "ZJLQMRQ")	
	private String zjlqmrq ;
	
	@Column(name = "HTXH")	
	private String htxh ;
	
	@Column(name = "HTCJSJ")	
	private Date htcjsj ;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public String getHtbh() {
		return htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public String getHtmc() {
		return htmc;
	}

	public void setHtmc(String htmc) {
		this.htmc = htmc;
	}

	public String getHtlx() {
		return htlx;
	}

	public void setHtlx(String htlx) {
		this.htlx = htlx;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getJhsqdh() {
		return jhsqdh;
	}

	public void setJhsqdh(String jhsqdh) {
		this.jhsqdh = jhsqdh;
	}

	public String getGfdw() {
		return gfdw;
	}

	public void setGfdw(String gfdw) {
		this.gfdw = gfdw;
	}

	public String getQddd() {
		return qddd;
	}

	public void setQddd(String qddd) {
		this.qddd = qddd;
	}

	public String getXfdw() {
		return xfdw;
	}

	public void setXfdw(String xfdw) {
		this.xfdw = xfdw;
	}

	public String getQtsm() {
		return qtsm;
	}

	public void setQtsm(String qtsm) {
		this.qtsm = qtsm;
	}

	public Double getHtje() {
		return htje;
	}

	public void setHtje(Double htje) {
		this.htje = htje;
	}

	public String getHtjbryj() {
		return htjbryj;
	}

	public void setHtjbryj(String htjbryj) {
		this.htjbryj = htjbryj;
	}

	public String getHtjbr() {
		return htjbr;
	}

	public void setHtjbr(String htjbr) {
		this.htjbr = htjbr;
	}

	public String getHtjbrqmsj() {
		return htjbrqmsj;
	}

	public void setHtjbrqmsj(String htjbrqmsj) {
		this.htjbrqmsj = htjbrqmsj;
	}

	public String getJhjybfzryj() {
		return jhjybfzryj;
	}

	public void setJhjybfzryj(String jhjybfzryj) {
		this.jhjybfzryj = jhjybfzryj;
	}

	public String getJhjybfzr() {
		return jhjybfzr;
	}

	public void setJhjybfzr(String jhjybfzr) {
		this.jhjybfzr = jhjybfzr;
	}

	public String getJhjybfzrqmsj() {
		return jhjybfzrqmsj;
	}

	public void setJhjybfzrqmsj(String jhjybfzrqmsj) {
		this.jhjybfzrqmsj = jhjybfzrqmsj;
	}

	public String getJhjybzryj() {
		return jhjybzryj;
	}

	public void setJhjybzryj(String jhjybzryj) {
		this.jhjybzryj = jhjybzryj;
	}

	public String getJhjybzr() {
		return jhjybzr;
	}

	public void setJhjybzr(String jhjybzr) {
		this.jhjybzr = jhjybzr;
	}

	public String getJhjybzrqmsj() {
		return jhjybzrqmsj;
	}

	public void setJhjybzrqmsj(String jhjybzrqmsj) {
		this.jhjybzrqmsj = jhjybzrqmsj;
	}

	public String getZgfzjlyj() {
		return zgfzjlyj;
	}

	public void setZgfzjlyj(String zgfzjlyj) {
		this.zgfzjlyj = zgfzjlyj;
	}

	public String getZgfzjl() {
		return zgfzjl;
	}

	public void setZgfzjl(String zgfzjl) {
		this.zgfzjl = zgfzjl;
	}

	public String getZgfzjlqmsj() {
		return zgfzjlqmsj;
	}

	public void setZgfzjlqmsj(String zgfzjlqmsj) {
		this.zgfzjlqmsj = zgfzjlqmsj;
	}

	public String getHtqdr() {
		return htqdr;
	}

	public void setHtqdr(String htqdr) {
		this.htqdr = htqdr;
	}

	public String getZjl() {
		return zjl;
	}

	public void setZjl(String zjl) {
		this.zjl = zjl;
	}

	public String getZjlqmrq() {
		return zjlqmrq;
	}

	public void setZjlqmrq(String zjlqmrq) {
		this.zjlqmrq = zjlqmrq;
	}

	public String getHtxh() {
		return htxh;
	}

	public void setHtxh(String htxh) {
		this.htxh = htxh;
	}

	public Date getHtcjsj() {
		return htcjsj;
	}

	public void setHtcjsj(Date htcjsj) {
		this.htcjsj = htcjsj;
	}
	
}
