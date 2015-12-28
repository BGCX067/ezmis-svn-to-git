package com.jteap.jhtj.ljydy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * 
 *描述：应用连接字段信息
 *时间：2010-3-31
 *作者：童贝
 *
 */
 @Entity
 @Table(name="TJ_APPSYSTEM_SJZD")
public class AppSystemField{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	@Column(name="vname")
	private String vname;//数据表名
	@Column(name="fname")
	private String fname;//数据字段名
	@Column(name="cvname")
	private String cvname;//数据表中文名
	@Column(name="cfname")
	private String cfname;//数据字段中文名
	@Column(name="ftype")
	private String ftype;//数据类型
	@Column(name="forder")
	private Long forder;//顺序

	//资源
	@ManyToOne()
	@JoinColumn(name="sid")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private AppSystemConn system;

	

	public AppSystemConn getSystem() {
		return system;
	}

	public void setSystem(AppSystemConn system) {
		this.system = system;
	}

	/** default constructor */
	public AppSystemField() {
	}

	public String getCvname() {
		return this.cvname;
	}

	public void setCvname(String cvname) {
		this.cvname = cvname;
	}

	public String getCfname() {
		return this.cfname;
	}

	public void setCfname(String cfname) {
		this.cfname = cfname;
	}

	public String getFtype() {
		return this.ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public Long getForder() {
		return this.forder;
	}

	public void setForder(Long forder) {
		this.forder = forder;
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

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

}