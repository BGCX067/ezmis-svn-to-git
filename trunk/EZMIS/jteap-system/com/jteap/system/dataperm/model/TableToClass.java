/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.system.dataperm.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * 
 * 功能说明:表和类对应模块
 * @author 童贝		
 * @version Nov 27, 2009
 */
@Entity
@Table(name = "TB_SYS_DATAPERM_TABLETOCLASS")
public class TableToClass{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;

	
	@Column(name = "TABLENAME")
	private java.lang.String tablename;

	
	@Column(name = "TABLECNAME")
	private java.lang.String tablecname;

	
	
	
	
	@Column(name = "CLASSNAME")
	private java.lang.String classname;

	
	
	
	
	@Column(name = "CLASSCNAME")
	private java.lang.String classcname;

	
	
	
	
	@Column(name = "TORDER")
	private long torder;

	
	
	
	
	@Column(name = "CLASSPATH")
	private java.lang.String classpath;

	
	
	

	public java.lang.String getId() {
		return this.id;
	}
	
	public void setId(java.lang.String value) {
		this.id = value;
	}
	
	public java.lang.String getTablename() {
		return this.tablename;
	}
	
	public void setTablename(java.lang.String value) {
		this.tablename = value;
	}
	
	public java.lang.String getTablecname() {
		return this.tablecname;
	}
	
	public void setTablecname(java.lang.String value) {
		this.tablecname = value;
	}
	
	public java.lang.String getClassname() {
		return this.classname;
	}
	
	public void setClassname(java.lang.String value) {
		this.classname = value;
	}
	
	public java.lang.String getClasscname() {
		return this.classcname;
	}
	
	public void setClasscname(java.lang.String value) {
		this.classcname = value;
	}
	
	public long getTorder() {
		return this.torder;
	}
	
	public void setTorder(long value) {
		this.torder = value;
	}
	
	public java.lang.String getClasspath() {
		return this.classpath;
	}
	
	public void setClasspath(java.lang.String value) {
		this.classpath = value;
	}
	
	
	
}

