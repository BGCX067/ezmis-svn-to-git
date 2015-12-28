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
 * 费用性质
 * @author 童贝
 *
 */
@Entity
@Table(name = "TB_HT_HT_FYXZ")
public class HeTongFyxz{
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private HeTongFyxz parent;
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<HeTongFyxz> children;
	
	@Column(name = "FYXZNAME")
	private String fyxzName;
	
	@Column(name = "FLBM")
	private String flbm;
	
	@Column(name = "SORTNO")
	private int sortNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HeTongFyxz getParent() {
		return parent;
	}

	public void setParent(HeTongFyxz parent) {
		this.parent = parent;
	}

	public Set<HeTongFyxz> getChildren() {
		return children;
	}

	public void setChildren(Set<HeTongFyxz> children) {
		this.children = children;
	}

	public String getFyxzName() {
		return fyxzName;
	}

	public void setFyxzName(String fyxzName) {
		this.fyxzName = fyxzName;
	}

	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	
	
}
