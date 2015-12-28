/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.model;

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
 * 定期工作分类
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_DQGZ_CATALOG")
public class DqgzCatalog{

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DqgzCatalog parent;
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<DqgzCatalog> children;
	
	@Column(name = "CATALOGNAME")
	private java.lang.String catalogName;
	
	@Column(name = "SORTNO")
	private int sortNo;
	
	public java.lang.String getId() {
		return this.id;
	}
	
	public void setId(java.lang.String value) {
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

	public Set<DqgzCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<DqgzCatalog> children) {
		this.children = children;
	}

	public DqgzCatalog getParent() {
		return parent;
	}

	public void setParent(DqgzCatalog parent) {
		this.parent = parent;
	}
	
}
