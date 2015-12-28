/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.lp.lpxxgl.model;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.*;

/**
 * 安全措施分类实体类
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_LP_XXPZ_AQCSCATALOG")
public class AqcsCatalog {

	// 主键
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@ManyToOne()
	@JoinColumn(name = "PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private AqcsCatalog parent;

	@OneToMany(mappedBy = "parent")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<AqcsCatalog> children;

	@OneToMany(mappedBy = "aqcsCatalog")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Aqcs> aqcss;

	// 分类名称
	@Column(name = "FLMC")
	private java.lang.String flmc;

	// 排序号
	@Column(name = "SORTNO")
	private Integer sortNo;

	public java.lang.String getId() {
		return this.id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public AqcsCatalog getParent() {
		return parent;
	}

	public void setParent(AqcsCatalog parent) {
		this.parent = parent;
	}

	public Set<AqcsCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<AqcsCatalog> children) {
		this.children = children;
	}

	public java.lang.String getFlmc() {
		return this.flmc;
	}

	public void setFlmc(java.lang.String value) {
		this.flmc = value;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Set<Aqcs> getAqcss() {
		return aqcss;
	}

	public void setAqcss(Set<Aqcs> aqcss) {
		this.aqcss = aqcss;
	}

}
