package com.jteap.jhtj.bbsjydy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="TJ_BBSJZD")
public class BbSjzd {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="bbioid")
	private String bbioid;
	@Column(name="bbindexid")
	private String bbindexid; 
	@Column(name="fname")
	private String fname; //字段名
	@Column(name="cfname")
	private String cfname;//字段中文名
	@Column(name="tname")
	private String tname; //表名
	@Column(name="ftype")
	private String ftype; //字段类型
	@Column(name="forder")
	private Long forder;  //排序
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getCfname() {
		return cfname;
	}
	public void setCfname(String cfname) {
		this.cfname = cfname;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	public Long getForder() {
		return forder;
	}
	public void setForder(Long forder) {
		this.forder = forder;
	}
	public String getBbioid() {
		return bbioid;
	}
	public void setBbioid(String bbioid) {
		this.bbioid = bbioid;
	}
	public String getBbindexid() {
		return bbindexid;
	}
	public void setBbindexid(String bbindexid) {
		this.bbindexid = bbindexid;
	}
}
