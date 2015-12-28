package com.jteap.jhtj.yyjkwh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 应用接口维护
 */
@Entity
@Table(name="tj_dllio")
public class TjDllIO{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	@Column(name="dname")
	private String dname;
	@Column(name="dcname")
	private String dcname;
	@Column(name="dms")
	private String dms;
	@Column(name="dorder")
	private Integer dorder;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDcname() {
		return dcname;
	}
	public void setDcname(String dcname) {
		this.dcname = dcname;
	}
	public String getDms() {
		return dms;
	}
	public void setDms(String dms) {
		this.dms = dms;
	}
	public Integer getDorder() {
		return dorder;
	}
	public void setDorder(Integer dorder) {
		this.dorder = dorder;
	}
	
}