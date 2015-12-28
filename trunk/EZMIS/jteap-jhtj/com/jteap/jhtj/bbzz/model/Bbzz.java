package com.jteap.jhtj.bbzz.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity  
@Table(name="tj_bbbsdj")
public class Bbzz {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	@Column(name="bbindexid")
	private String bbindexid;
	@Column(name="cname")
	private String cname;
	@Column(name="key")
	private String key;
	@Column(name="zbrq")
	private Date zbrq;
	@Column(name="zzrname")
	private String zzrname;
	@Column(name="zzrcname")
	private String zzrcname;
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "bbnr", columnDefinition = "BLOB",nullable=true) 
	private byte[] bbnr;
	@Column(name="status")
	private String status="2";//1是发布,2是生成
	@Column(name="sortno")
	private Long sortno;
	public Long getSortno() {
		return sortno;
	}
	public void setSortno(Long sortno) {
		this.sortno = sortno;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBbindexid() {
		return bbindexid;
	}
	public void setBbindexid(String bbindexid) {
		this.bbindexid = bbindexid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Date getZbrq() {
		return zbrq;
	}
	public void setZbrq(Date zbrq) {
		this.zbrq = zbrq;
	}
	public String getZzrname() {
		return zzrname;
	}
	public void setZzrname(String zzrname) {
		this.zzrname = zzrname;
	}
	public String getZzrcname() {
		return zzrcname;
	}
	public void setZzrcname(String zzrcname) {
		this.zzrcname = zzrcname;
	}
	public byte[] getBbnr() {
		return bbnr;
	}
	public void setBbnr(byte[] bbnr) {
		this.bbnr = bbnr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
