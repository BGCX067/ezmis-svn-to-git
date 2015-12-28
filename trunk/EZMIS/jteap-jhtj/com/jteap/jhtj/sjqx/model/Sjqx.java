package com.jteap.jhtj.sjqx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="TJ_QX2ROLE")
public class Sjqx {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="qxid")
	private String qxid;
	@Column(name="roleid")
	private String roleid;
	@Column(name="qxlx")
	private String qxlx;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQxid() {
		return qxid;
	}
	public void setQxid(String qxid) {
		this.qxid = qxid;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getQxlx() {
		return qxlx;
	}
	public void setQxlx(String qxlx) {
		this.qxlx = qxlx;
	}
	
}
