package com.jteap.form.eform.model;

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

/**
 * 自定义表单分类
 * 
 * @author tanchang
 * 
 */
@Entity  
@Table(name="tb_form_eform_catalog")
public class EFormCatalog {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	private String id;
	
	@Column(name="CATALOGNAME")
	private String catalogName;
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private EFormCatalog parent;
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<EFormCatalog> children;
	
	
	@OneToMany(mappedBy="catalog")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<EForm> eforms;
	
	
	@Column(name="SORTNO")
	private int sortNo;
	
	
	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public Set<EFormCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<EFormCatalog> children) {
		this.children = children;
	}



	public EFormCatalog getParent() {
		return parent;
	}

	public void setParent(EFormCatalog parent) {
		this.parent = parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<EForm> getEforms() {
		return eforms;
	}

	public void setEforms(Set<EForm> eforms) {
		this.eforms = eforms;
	}

}
