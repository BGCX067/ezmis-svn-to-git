package com.jteap.system.doclib.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * TbDoclibCatalogFiled entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_DOCLIB_CATALOG_FIELD")
@SuppressWarnings("unchecked")
public class DoclibCatalogField {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private Serializable id; // 编号

	@ManyToOne()
	@JoinColumn(name = "CATALOG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DoclibCatalog doclibCatalog;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "EMUN_VALUE")
	private String emunValue;

	@Column(name = "FORMAT")
	private String format;

	@Column(name = "SORTNO")
	private int sortno;
	
	@Column(name = "length")
	private Integer len;
	@Transient
	private String code;

	// Property accessor

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmunValue() {
		return this.emunValue;
	}

	public void setEmunValue(String emunValue) {
		this.emunValue = emunValue;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public DoclibCatalog getDoclibCatalog() {
		return doclibCatalog;
	}

	public void setDoclibCatalog(DoclibCatalog doclibCatalog) {
		this.doclibCatalog = doclibCatalog;
	}

	public Integer getLen() {
		return len;
	}

	public void setLen(Integer len) {
		this.len = len;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}