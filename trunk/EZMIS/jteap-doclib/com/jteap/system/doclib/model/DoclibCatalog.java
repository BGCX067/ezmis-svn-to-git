package com.jteap.system.doclib.model;

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
 * TbDoclibCatalog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_DOCLIB_CATALOG")
@SuppressWarnings("unchecked")
public class DoclibCatalog {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id; // 编号\

	@ManyToOne()
	@JoinColumn(name = "PARENT_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DoclibCatalog parent; // 父类别

	@OneToMany(mappedBy = "parent")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "SORTNO")
	private Set<DoclibCatalog> children; // 子类别

	@OneToMany(mappedBy = "doclibCatalog")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "SORTNO")
	private Set<DoclibCatalogField> fields;
	
	@OneToMany(mappedBy = "doclibCatalog")
	@Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Doclib> doclib;

	@Column(name = "TITLE")
	private String title;

	@ManyToOne()
	@JoinColumn(name = "CATALOG_PERM")
	@LazyToOne(LazyToOneOption.PROXY)
	private DoclibLevel catalogPerm;

	@Column(name = "CATALOG_CODE")
	private String catalogCode;

	@Column(name = "TEMPLATE_FILE")
	private String templateFile;

	@Column(name = "SORTNO")
	private Long sortno;

	// Property accessors

	public Long getSortno() {
		return sortno;
	}

	public void setSortno(Long sortno) {
		this.sortno = sortno;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DoclibCatalog getParent() {
		return parent;
	}
	
	
	public DoclibLevel getCatalogPerm() {
		return catalogPerm;
	}

	public void setCatalogPerm(DoclibLevel catalogPerm) {
		this.catalogPerm = catalogPerm;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	
	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public void setParent(DoclibCatalog parent) {
		this.parent = parent;
	}

	public Set<DoclibCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<DoclibCatalog> children) {
		this.children = children;
	}

	public Set<DoclibCatalogField> getFields() {
		return fields;
	}

	public void setFields(Set<DoclibCatalogField> fields) {
		this.fields = fields;
	}

	public Set<Doclib> getDoclib() {
		return doclib;
	}

	public void setDoclib(Set<Doclib> doclib) {
		this.doclib = doclib;
	}
	
}