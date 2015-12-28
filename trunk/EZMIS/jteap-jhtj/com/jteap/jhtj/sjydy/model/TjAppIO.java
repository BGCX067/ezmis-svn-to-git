package com.jteap.jhtj.sjydy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 数据源模型
 */
@Entity
@Table(name="TJ_APPIO")
public class TjAppIO{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="vname")
	private String vname;  //数据源名称
	@Column(name="sid")
	private String sid;    //系统ID
	@Column(name="cvname")
	private String cvname; //数据源中文名
	@Column(name="sqlstr")
	private String sqlstr; //sql语句
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
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getCvname() {
		return cvname;
	}
	public void setCvname(String cvname) {
		this.cvname = cvname;
	}
	public String getSqlstr() {
		return sqlstr;
	}
	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}

	

}