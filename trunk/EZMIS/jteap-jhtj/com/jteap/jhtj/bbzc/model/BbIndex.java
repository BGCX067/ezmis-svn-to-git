package com.jteap.jhtj.bbzc.model;

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
@Table(name="tj_bbindex")
public class BbIndex {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="bbbm")
	private String bbbm; //报表编码
	@Column(name="bbmc")
	private String bbmc; //报表名称
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "bbmb", columnDefinition = "BLOB",nullable=true) 
	private byte[] bbmb; //报表模板
	@Column(name="flid")
	private String flid; //分类ID
	@Column(name="bz")
	private String bz;
	@Column(name="sortno")
	private Long sortno;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBbbm() {
		return bbbm;
	}
	public void setBbbm(String bbbm) {
		this.bbbm = bbbm;
	}
	public String getBbmc() {
		return bbmc;
	}
	public void setBbmc(String bbmc) {
		this.bbmc = bbmc;
	}
	public byte[] getBbmb() {
		return bbmb;
	}
	public void setBbmb(byte[] bbmb) {
		this.bbmb = bbmb;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public Long getSortno() {
		return sortno;
	}
	public void setSortno(Long sortno) {
		this.sortno = sortno;
	}
	public String getFlid() {
		return flid;
	}
	public void setFlid(String flid) {
		this.flid = flid;
	}
	
}
