/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.ckgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;


@Entity
@Table(name = "TB_WZ_CKGL")
public class Ckgl{

	@Column(name = "CKGLY")	
	private java.lang.String ckgly;
	
	
	@Column(name = "CKBM")	
	private java.lang.String ckbm;
	
	
	@Column(name = "BZ")	
	private java.lang.String bz;
	
	
	@Column(name = "CKMC")	
	private java.lang.String ckmc;
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	


	public java.lang.String getCkgly() {
		return ckgly;
	}

	public void setCkgly(java.lang.String ckgly) {
		this.ckgly = ckgly;
	}

	public java.lang.String getCkbm() {
		return this.ckbm;
	}
	
	public void setCkbm(java.lang.String value) {
		this.ckbm = value;
	}
	public java.lang.String getBz() {
		return this.bz;
	}
	
	public void setBz(java.lang.String value) {
		this.bz = value;
	}
	public java.lang.String getCkmc() {
		return this.ckmc;
	}
	
	public void setCkmc(java.lang.String value) {
		this.ckmc = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	

	public Person getPerson() {
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
		return pm.findUniqueBy("userLoginName", this.ckgly);
	}

}

