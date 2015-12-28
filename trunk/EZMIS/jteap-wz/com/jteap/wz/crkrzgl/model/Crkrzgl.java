/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.crkrzgl.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;


@Entity
@Table(name = "TB_WZ_YCRKRZ")
public class Crkrzgl{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name="CZR")
	private java.lang.String czr;
	
	@OneToMany(mappedBy="crkrzgl")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Crkrzmx> crkrzmxs = new HashSet<Crkrzmx>();
	
	@Column(name = "CRKSJ")	
	private java.util.Date crksj;
	
	
	@Column(name = "CRKQF")	
	private java.lang.String crkqf;
	
	
	@Column(name = "CRKDH")	
	private java.lang.String crkdh;
	
	
	@Column(name = "CKBH")	
	private java.lang.String ckbh;
	
	
	@Column(name = "CRKLB")	
	private java.lang.String crklb;
	
	
	@Column(name = "XGDJBH")	
	private java.lang.String xgdjbh;
	
	@Column(name = "CZRXM")	
	private java.lang.String czrxm;
	
	@SuppressWarnings("unused")
	@Transient
	private Person person;

	public void setPerson(Person person) {
		this.person = person;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getCzr() {
		return czr;
	}
	public void setCzr(java.lang.String czr) {
		this.czr = czr;
	}
	public java.util.Date getCrksj() {
		return this.crksj;
	}
	
	public void setCrksj(java.util.Date value) {
		this.crksj = value;
	}
	public java.lang.String getCrkqf() {
		return this.crkqf;
	}
	
	public void setCrkqf(java.lang.String value) {
		this.crkqf = value;
	}
	public java.lang.String getCrkdh() {
		return this.crkdh;
	}
	
	public void setCrkdh(java.lang.String value) {
		this.crkdh = value;
	}
	public java.lang.String getCkbh() {
		return this.ckbh;
	}
	
	public void setCkbh(java.lang.String value) {
		this.ckbh = value;
	}
	public java.lang.String getCrklb() {
		return this.crklb;
	}
	
	public void setCrklb(java.lang.String value) {
		this.crklb = value;
	}
	public java.lang.String getXgdjbh() {
		return this.xgdjbh;
	}
	
	public void setXgdjbh(java.lang.String value) {
		this.xgdjbh = value;
	}
	public Set<Crkrzmx> getCrkrzmxs() {
		return crkrzmxs;
	}
	public void setCrkrzmxs(Set<Crkrzmx> crkrzmxs) {
		this.crkrzmxs = crkrzmxs;
	}
	
	public Person getPerson(){
		Person p = null;
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
		p = pm.findUniqueBy("userLoginName", this.czr);
		if(p==null){
			p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
		}
		return p;
	}
	
	public Ckgl getCk(){
		Ckgl c = null;
		CkglManager cm = (CkglManager)SpringContextUtil.getBean("ckglManager");
		c = cm.findUniqueBy("id", this.ckbh);
		if(c==null){
			c = new Ckgl();
		}
		return c;
	}
	public java.lang.String getCzrxm() {
		return czrxm;
	}
	public void setCzrxm(java.lang.String czrxm) {
		this.czrxm = czrxm;
	}
	
}

