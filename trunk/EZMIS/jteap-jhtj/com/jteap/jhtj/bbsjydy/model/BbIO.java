package com.jteap.jhtj.bbsjydy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="TJ_BBIO")
public class BbIO {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="vname")
	private String vname; //视图名称
	@Column(name="bbindexid")
	private String bbindexid; //报表模板ID
	@Column(name="cvname")
	private String cvname;//视图中文名
	@Column(name="sqlstr")
	private String sqlstr;//sql语句
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
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
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
	public String getBbindexid() {
		return bbindexid;
	}
	public void setBbindexid(String bbindexid) {
		this.bbindexid = bbindexid;
	}
}
