/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.model;

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
 * 运行日志分类
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_LOGS_CATALOG")
public class LogsCatalog{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "CATALOGNAME")
	private java.lang.String catalogName;
	
	@Column(name = "SORTNO")
	private long sortNo;

	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private LogsCatalog parent;
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<LogsCatalog> children;
	
	public java.lang.String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(java.lang.String catalogName) {
		this.catalogName = catalogName;
	}
	public long getSortNo() {
		return sortNo;
	}
	public void setSortNo(long sortNo) {
		this.sortNo = sortNo;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public LogsCatalog getParent() {
		return parent;
	}

	public void setParent(LogsCatalog parent) {
		this.parent = parent;
	}
	
	public Set<LogsCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<LogsCatalog> children) {
		this.children = children;
	}
	
}
