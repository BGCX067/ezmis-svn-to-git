/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htsp.model;

import java.util.Set;

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
 * 合同分类
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_HT_HT_CATALOG")
public class HeTongCatalog{
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private HeTongCatalog parent;
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<HeTongCatalog> children;
	
	@Column(name = "CATALOGNAME")
	private String catalogName;
	
	@Column(name = "FLBM")
	private String flbm;
	
	@Column(name = "SORTNO")
	private int sortNo;
	
	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String value) {
		this.id = value;
	}
	
	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public java.lang.String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(java.lang.String catalogName) {
		this.catalogName = catalogName;
	}

	public HeTongCatalog getParent() {
		return parent;
	}

	public void setParent(HeTongCatalog parent) {
		this.parent = parent;
	}

	public Set<HeTongCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<HeTongCatalog> children) {
		this.children = children;
	}
}
