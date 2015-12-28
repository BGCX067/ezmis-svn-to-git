/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.component.livegrid.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "TB_TEST_BIGDT")
public class BigDt{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "FD1")	
	private java.lang.String fd1;
	
	
	@Column(name = "FD2")	
	private java.lang.String fd2;
	
	
	@Column(name = "FD3")	
	private java.lang.String fd3;
	
	
	@Column(name = "FD4")	
	private java.lang.String fd4;
	
	
	@Column(name = "FD5")	
	private java.lang.String fd5;
	
	
	@Column(name = "FD6")	
	private java.lang.String fd6;
	
	
	@Column(name = "FD7")	
	private java.lang.String fd7;
	
	
	@Column(name = "FD8")	
	private java.lang.String fd8;
	
	
	@Column(name = "FD9")	
	private java.lang.String fd9;
	
	
	@Column(name = "FD0")	
	private java.lang.String fd0;
	

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getFd1() {
		return this.fd1;
	}
	
	public void setFd1(java.lang.String value) {
		this.fd1 = value;
	}
	public java.lang.String getFd2() {
		return this.fd2;
	}
	
	public void setFd2(java.lang.String value) {
		this.fd2 = value;
	}
	public java.lang.String getFd3() {
		return this.fd3;
	}
	
	public void setFd3(java.lang.String value) {
		this.fd3 = value;
	}
	public java.lang.String getFd4() {
		return this.fd4;
	}
	
	public void setFd4(java.lang.String value) {
		this.fd4 = value;
	}
	public java.lang.String getFd5() {
		return this.fd5;
	}
	
	public void setFd5(java.lang.String value) {
		this.fd5 = value;
	}
	public java.lang.String getFd6() {
		return this.fd6;
	}
	
	public void setFd6(java.lang.String value) {
		this.fd6 = value;
	}
	public java.lang.String getFd7() {
		return this.fd7;
	}
	
	public void setFd7(java.lang.String value) {
		this.fd7 = value;
	}
	public java.lang.String getFd8() {
		return this.fd8;
	}
	
	public void setFd8(java.lang.String value) {
		this.fd8 = value;
	}
	public java.lang.String getFd9() {
		return this.fd9;
	}
	
	public void setFd9(java.lang.String value) {
		this.fd9 = value;
	}
	public java.lang.String getFd0() {
		return this.fd0;
	}
	
	public void setFd0(java.lang.String value) {
		this.fd0 = value;
	}
	
	
}

