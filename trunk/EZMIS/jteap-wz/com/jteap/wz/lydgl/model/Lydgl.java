/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.lydgl.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.wzlysq.manager.WzlysqManager;
import com.jteap.wz.wzlysq.model.Wzlysq;


@Entity
@Table(name = "TB_WZ_YLYD")
public class Lydgl{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@OneToMany(mappedBy="lydgl")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("xh asc")
	private Set<Lydmx> lydmxs;
	
	@Column(name = "LYSQBH")	
	private java.lang.String lysqbh;
	
	@Column(name = "CZR")	
	private java.lang.String czr;
	
	@Column(name = "LLR")	
	private java.lang.String llr;
	
	@Column(name = "GCLB")	
	private java.lang.String gclb;
	
	@Column(name = "GCXM")	
	private java.lang.String gcxm;
	
	@Column(name = "LYSJ")	
	private java.util.Date lysj;
	
	@Column(name = "LYBM")	
	private java.lang.String lybm;
	
	@Column(name = "ZT")	
	private java.lang.String zt;
	
	@Column(name = "BH")	
	private java.lang.String bh;
	
	@Column(name = "LYDQF")
	private String lydqf;
	
	@Column(name = "LCZT")
	private String lczt;     //流程状态
	
	@Column(name = "ZFZT")
	private String zfzt;     //流程状态
	
	public String getLydqf() {
		return lydqf;
	}
	public void setLydqf(String lydqf) {
		this.lydqf = lydqf;
	}
	public java.util.Date getLysj() {
		return lysj;
	}
	public void setLysj(java.util.Date lysj) {
		this.lysj = lysj;
	}
	public java.lang.String getLybm() {
		return lybm;
	}
	public void setLybm(java.lang.String lybm) {
		this.lybm = lybm;
	}
	public java.lang.String getLlr() {
		return llr;
	}
	public void setLlr(java.lang.String llr) {
		this.llr = llr;
	}
	public Set<Lydmx> getLydmxs() {
		return lydmxs;
	}
	public void setLydmxs(Set<Lydmx> lydmxs) {
		this.lydmxs = lydmxs;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getLysqbh() {
		return lysqbh;
	}
	public void setLysqbh(java.lang.String lysqbh) {
		this.lysqbh = lysqbh;
	}
	public java.lang.String getCzr() {
		return this.czr;
	}
	
	public void setCzr(java.lang.String value) {
		this.czr = value;
	}

	public java.lang.String getGclb() {
		return this.gclb;
	}
	
	public void setGclb(java.lang.String value) {
		this.gclb = value;
	}
	
	public java.lang.String getGcxm() {
		return this.gcxm;
	}
	
	public void setGcxm(java.lang.String value) {
		this.gcxm = value;
	}
	public java.lang.String getZt() {
		return this.zt;
	}
	
	public void setZt(java.lang.String value) {
		this.zt = value;
	}
	public java.lang.String getBh() {
		return bh;
	}
	public void setBh(java.lang.String bh) {
		this.bh = bh;
	}
	
	public Wzlysq getWzlysq(){
		WzlysqManager manager = (WzlysqManager)SpringContextUtil.getBean("wzlysqManager");
		
		return manager.findUniqueBy("id", this.lysqbh);
	}
	public String getLczt() {
		return lczt;
	}
	public void setLczt(String lczt) {
		this.lczt = lczt;
	}
	public String getZfzt() {
		return zfzt;
	}
	public void setZfzt(String zfzt) {
		this.zfzt = zfzt;
	}
	
}

