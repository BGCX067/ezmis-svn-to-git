/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.ztbgl.model;

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
 * 招投标分类
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_HT_ZTB_CATALOG")
public class ZtbCatalog {

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
	private ZtbCatalog parentZtb;

	@OneToMany(mappedBy = "parentZtb")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "sortno")
	private List<ZtbCatalog> childZtbs = new ArrayList<ZtbCatalog>();

	@Column(name = "SORTNO")
	private long sortno;

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

	public ZtbCatalog getParentZtb() {
		return parentZtb;
	}

	public void setParentZtb(ZtbCatalog parentZtb) {
		this.parentZtb = parentZtb;
	}

	public List<ZtbCatalog> getChildZtbs() {
		return childZtbs;
	}

	public void setChildZtbs(List<ZtbCatalog> childZtbs) {
		this.childZtbs = childZtbs;
	}

	public long getSortno() {
		return sortno;
	}

	public void setSortno(long sortno) {
		this.sortno = sortno;
	}

}
