/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.wzlb.model;

import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.jteap.wz.wzda.model.Wzda;

import java.util.*;


@Entity
@Table(name = "TB_WZ_SWZLB")
public class Wzlb{

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@ManyToOne()
	@JoinColumn(name="FLBBM")
	@LazyToOne(LazyToOneOption.PROXY)
	private Wzlb flbbm;// 父亲组织
	
	@OneToMany(mappedBy="flbbm")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="pxm")
	private List<Wzlb> childWzlb = new ArrayList<Wzlb>();	//子组织

	@Column(name = "WZLBMC")
	private java.lang.String wzlbmc;//物资类别名称
	
	@Column(name = "BZ")
	private java.lang.String bz;	//备注
	
	@Column(name = "PXM")
	private java.lang.String pxm;	//排序码
	
	// 所有内容
	@OneToMany(mappedBy="wzlb")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Wzda> wzda;//物资档案

	public Set<Wzda> getWzda() {
		return wzda;
	}
	public void setWzda(Set<Wzda> wzda) {
		this.wzda = wzda;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public Wzlb getFlbbm() {
		return flbbm;
	}
	public void setFlbbm(Wzlb flbbm) {
		this.flbbm = flbbm;
	}
	public List<Wzlb> getChildWzlb() {
		return childWzlb;
	}
	public void setChildWzlb(List<Wzlb> childWzlb) {
		this.childWzlb = childWzlb;
	}
	public java.lang.String getWzlbmc() {
		return this.wzlbmc;
	}
	
	public void setWzlbmc(java.lang.String value) {
		this.wzlbmc = value;
	}
	
	public java.lang.String getBz() {
		return this.bz;
	}
	
	public void setBz(java.lang.String value) {
		this.bz = value;
	}
	
	public java.lang.String getPxm() {
		return this.pxm;
	}
	
	public void setPxm(java.lang.String value) {
		this.pxm = value;
	}
	
	
	
}

