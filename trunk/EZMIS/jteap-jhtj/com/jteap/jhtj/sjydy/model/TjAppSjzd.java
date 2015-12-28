package com.jteap.jhtj.sjydy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * 数据源数据字段模型
 */
@Entity
@Table(name="TJ_APPSJZD")
public class TjAppSjzd{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="vname")
	private String vname;
	@Column(name="fname")
	private String fname;
	@Column(name="sid")
	private String sid;
	@Column(name="tname")
	private String tname;
	@Column(name="cfname")
	private String cfname;
	@Column(name="ftype")
	private String ftype;
	@Column(name="forder")
	private Long forder;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getCfname() {
		return cfname;
	}
	public void setCfname(String cfname) {
		this.cfname = cfname;
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

	
}