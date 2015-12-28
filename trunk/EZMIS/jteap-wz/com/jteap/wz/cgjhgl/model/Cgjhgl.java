/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.cgjhgl.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.wz.cgjhmx.model.Cgjhmx;




@Entity
@Table(name = "TB_WZ_YCGJH")
public class Cgjhgl{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "BH")	
	private java.lang.String bh;
	
	@OneToMany(mappedBy="cgjhgl")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("xh asc")
	private Set<Cgjhmx> cgjhmxs=new HashSet<Cgjhmx>();
	
	@Column(name = "ZDSJ")	
	private java.util.Date zdsj;
	
	
	@Column(name = "SXSJ")	
	private java.util.Date sxsj;
	
	
	@Column(name = "BZ")	
	private java.lang.String bz;
	
	
	@Column(name = "ZT")	
	private java.lang.String zt="0";
	
	
	@Column(name="JHY")
	private java.lang.String jhy;
	
	@Column(name="DYSZT")
	private String dyszt;
	
	@Column(name="DYSCZR")
	private String dysczr;

	public Person getPerson() {
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
		return pm.findUniqueBy("userLoginName", this.jhy);
	}


	public java.lang.String getBh() {
		return this.bh;
	}
	
	public void setBh(java.lang.String value) {
		this.bh = value;
	}
	public java.util.Date getZdsj() {
		return this.zdsj;
	}
	
	public void setZdsj(java.util.Date value) {
		this.zdsj = value;
	}
	public java.util.Date getSxsj() {
		return this.sxsj;
	}
	
	public void setSxsj(java.util.Date value) {
		this.sxsj = value;
	}
	
	public java.lang.String getBz() {
		return this.bz;
	}
	
	public void setBz(java.lang.String value) {
		this.bz = value;
	}
	public java.lang.String getZt() {
		return this.zt;
	}
	
	public void setZt(java.lang.String value) {
		this.zt = value;
	}
	
	public java.lang.String getJhy() {
		return jhy;
	}

	public void setJhy(java.lang.String jhy) {
		this.jhy = jhy;
	}

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Set<Cgjhmx> getCgjhmxs() {
		return cgjhmxs;
	}

	public void setCgjhmxs(Set<Cgjhmx> cgjhmxs) {
		this.cgjhmxs = cgjhmxs;
	}
	public String getDyszt() {
		return dyszt;
	}

	public void setDyszt(String dyszt) {
		this.dyszt = dyszt;
	}

	public String getDysczr() {
		return dysczr;
	}

	public void setDysczr(String dysczr) {
		this.dysczr = dysczr;
	}
}

