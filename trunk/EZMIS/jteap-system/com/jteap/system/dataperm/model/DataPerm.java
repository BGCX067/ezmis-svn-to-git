/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.system.dataperm.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

/**
 * 
 * 功能说明:数据权限模块
 * 
 * @author 童贝
 * @version Nov 27, 2009
 */
@Entity
@Table(name = "TB_SYS_DATAPERM")
public class DataPerm {

	// columns START

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "DATAPERMNAME")
	private java.lang.String datapermname;

	@Column(name = "DATAPERMCNAME")
	private java.lang.String datapermcname;

	@Column(name = "SQL")
	private java.lang.String sql;

	@Column(name = "QUALIFICATION")
	private java.lang.String qualification;

	@Column(name = "TABLENAME")
	private java.lang.String tablename;

	@Column(name = "DORDER")
	private long dorder;

	// 权限所对应的人员列表,把控制权交给用户
	@ManyToMany(mappedBy = "perm")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Perm2Person> persons;
	//	
	// 权限所对应的人员列表,把控制权交给角色
	@ManyToMany(mappedBy = "perm")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Perm2Role> roles;

	public Set<Perm2Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Perm2Person> persons) {
		this.persons = persons;
	}

	public Set<Perm2Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Perm2Role> roles) {
		this.roles = roles;
	}

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getDatapermname() {
		return this.datapermname;
	}

	public void setDatapermname(java.lang.String value) {
		this.datapermname = value;
	}

	public java.lang.String getDatapermcname() {
		return this.datapermcname;
	}

	public void setDatapermcname(java.lang.String value) {
		this.datapermcname = value;
	}

	public java.lang.String getSql() {
		return this.sql;
	}

	public void setSql(java.lang.String value) {
		this.sql = value;
	}

	public java.lang.String getQualification() {
		return this.qualification;
	}

	public void setQualification(java.lang.String value) {
		this.qualification = value;
	}

	public long getDorder() {
		return this.dorder;
	}

	public void setDorder(long value) {
		this.dorder = value;
	}

	public java.lang.String getTablename() {
		return tablename;
	}

	public void setTablename(java.lang.String tablename) {
		this.tablename = tablename;
	}

}
