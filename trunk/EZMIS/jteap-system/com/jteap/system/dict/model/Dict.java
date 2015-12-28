package com.jteap.system.dict.model;

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
 * 数据字典实体
 * @author Jeery
 * 2008-1-31
 */
@Entity
@Table(name = "tb_sys_dict")
public class Dict {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type="string")
	private Serializable id; // 编号
	
	//类别	
	@ManyToOne()
	@JoinColumn(name="catalog")
	@LazyToOne(LazyToOneOption.PROXY)
	private DictCatalog catalog;
	
	@Column(name="sortno")
	private int sortNo;
	
	@SuppressWarnings("unused")
	@Transient
	private String catalogName;
	
	//键
	@Column(name="DICTKEY")
	private String key;
	
	//值
	@Column(name="DICTVALUE")
	private String value;
	
	//备注
	@Column(name="remark")
	private String remark;

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}


	public DictCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(DictCatalog catalog) {
		this.catalog = catalog;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getCatalogName() {
		return catalog.getCatalogName();
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	
	

}
