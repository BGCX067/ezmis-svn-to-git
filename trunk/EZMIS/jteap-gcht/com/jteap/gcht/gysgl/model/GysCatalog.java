/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.gysgl.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

/**
 * 供应商分类
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_HT_GYS_CATALOG")
public class GysCatalog {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "CATALOGNAME")
	private java.lang.String catalogName;

	@ManyToOne()
	@JoinColumn(name = "PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private GysCatalog parentGys;

	@OneToMany(mappedBy = "parentGys")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "sortno")
	private List<GysCatalog> childGyss = new ArrayList<GysCatalog>();

	@Column(name = "SORTNO")
	private long sortno;
	
	@Column(name = "FLBM")
	private String flbm;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(java.lang.String catalogName) {
		this.catalogName = catalogName;
	}

	public GysCatalog getParentGys() {
		return parentGys;
	}

	public void setParentGys(GysCatalog parentGys) {
		this.parentGys = parentGys;
	}

	public List<GysCatalog> getChildGyss() {
		return childGyss;
	}

	public void setChildGyss(List<GysCatalog> childGyss) {
		this.childGyss = childGyss;
	}

	public long getSortno() {
		return sortno;
	}

	public void setSortno(long sortno) {
		this.sortno = sortno;
	}

	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}

}
