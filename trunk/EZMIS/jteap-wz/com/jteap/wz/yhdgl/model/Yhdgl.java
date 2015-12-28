/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.wz.yhdgl.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.wz.yhdmx.model.Yhdmx;

@Entity
@Table(name = "TB_WZ_YYHD")
public class Yhdgl {

	// columns START

	@Column(name = "YSRQ")
	private java.util.Date ysrq;

	@Column(name = "GHDW")
	private java.lang.String ghdw;

	@Column(name = "ZT")
	private java.lang.String zt;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@OneToMany(mappedBy = "yhdgl")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("xh asc")
	private Set<Yhdmx> yhdmxs;

	@Column(name = "BGY")
	private java.lang.String bgy;

	@Column(name = "CGY")
	private java.lang.String cgy;

	@Column(name = "BH")
	private java.lang.String bh;

	@Column(name = "HTBH")
	private java.lang.String htbh;

	@Column(name = "DHRQ")
	private java.util.Date dhrq;

	@Column(name = "FLAG")
	private java.lang.String flag; // 区分是正常入库，还是自由入库（正常入库：1；自由入库：2）

	@Column(name = "GCXM")
	private java.lang.String gcxm;

	@Column(name = "GCLB")
	private java.lang.String gclb;

	@Column(name = "BZ")
	private java.lang.String bz;
	
	@Column(name = "BL_ZT")
	private java.lang.String blzt; 
	
	@Column(name = "JL_ZT")
	private java.lang.String jlzt;

	public java.lang.String getFlag() {
		return flag;
	}

	public void setFlag(java.lang.String flag) {
		this.flag = flag;
	}

	public Set<Yhdmx> getYhdmxs() {
		return yhdmxs;
	}

	public void setYhdmxs(Set<Yhdmx> yhdmxs) {
		this.yhdmxs = yhdmxs;
	}

	public java.util.Date getYsrq() {
		return this.ysrq;
	}

	public void setYsrq(java.util.Date value) {
		this.ysrq = value;
	}

	public java.lang.String getGhdw() {
		return this.ghdw;
	}

	public void setGhdw(java.lang.String value) {
		this.ghdw = value;
	}

	public java.lang.String getZt() {
		return this.zt == null ? "0" : this.zt;
	}

	public void setZt(java.lang.String value) {
		this.zt = value;
	}

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getBgy() {
		return bgy;
	}

	public void setBgy(java.lang.String bgy) {
		this.bgy = bgy;
	}

	public java.lang.String getCgy() {
		return cgy;
	}

	public void setCgy(java.lang.String cgy) {
		this.cgy = cgy;
	}

	public java.lang.String getBh() {
		return this.bh;
	}

	public void setBh(java.lang.String value) {
		this.bh = value;
	}

	public java.lang.String getHtbh() {
		return this.htbh;
	}

	public void setHtbh(java.lang.String value) {
		this.htbh = value;
	}

	public java.util.Date getDhrq() {
		return this.dhrq;
	}

	public void setDhrq(java.util.Date value) {
		this.dhrq = value;
	}
	@Transient
	public Person getPersonCgy() {
		PersonManager pm = (PersonManager) SpringContextUtil
				.getBean("personManager");
		return pm.findUniqueBy("userLoginName", this.cgy);
	}
	@Transient
	public Person getPersonBgy() {
		PersonManager pm = (PersonManager) SpringContextUtil
				.getBean("personManager");
		return pm.findUniqueBy("userLoginName", this.bgy);
	}

	public java.lang.String getGcxm() {
		return gcxm;
	}

	public void setGcxm(java.lang.String gcxm) {
		this.gcxm = gcxm;
	}

	public java.lang.String getGclb() {
		return gclb;
	}

	public void setGclb(java.lang.String gclb) {
		this.gclb = gclb;
	}

	public java.lang.String getBz() {
		return bz;
	}

	public void setBz(java.lang.String bz) {
		this.bz = bz;
	}

	public java.lang.String getBlzt() {
		return blzt;
	}

	public void setBlzt(java.lang.String blzt) {
		this.blzt = blzt;
	}

	public java.lang.String getJlzt() {
		return jlzt;
	}

	public void setJlzt(java.lang.String jlzt) {
		this.jlzt = jlzt;
	}
}
