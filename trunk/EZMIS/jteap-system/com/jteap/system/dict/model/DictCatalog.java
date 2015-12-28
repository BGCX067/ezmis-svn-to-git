package com.jteap.system.dict.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

import com.jteap.system.dict.model.Dict;

/**
 * 数据字典类型
 * @author Jeery
 *
 */
@Entity
@Table(name = "tb_sys_dict_catalog")
public class DictCatalog {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type="string")	
	private Serializable id; // 编号
	
	@Column(name="sortno")
	private int sortNo;      //排序号
		
	@Column(name="catalogname")
	private String catalogName; //类别
	
	@Column(name="UNIQUENAME")
	private String uniqueName;	//唯一名称代号
	
	@ManyToOne()
	@JoinColumn(name="parentid")
	@LazyToOne(LazyToOneOption.PROXY)
	private DictCatalog parentDictCatalog; //父类别
	
	@OneToMany(mappedBy="catalog")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Dict> dicts; //所拥有的字典条目
	
	@OneToMany(mappedBy="parentDictCatalog")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="sortno")
	private Set<DictCatalog> childDictCatalog; //子类别

	@Column(name="remark")
	private String remark;  //备注

	//用于在树型结构中显示该字典类型中有多少字典条目的功能
	@Transient
	private String dictCatalogWithCount;


	public void setDictCatalogWithCount(String dictCatalogWithCount) {
		this.dictCatalogWithCount = dictCatalogWithCount;
	}

	public Serializable getId() {
		return id;
	}


	public void setId(Serializable id) {
		this.id = id;
	}


	public DictCatalog getParentDictCatalog() {
		return parentDictCatalog;
	}

	public void setParentDictCatalog(DictCatalog parentDictCatalog) {
		this.parentDictCatalog = parentDictCatalog;
	}

	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getCatalogName() {
		return catalogName;
	}


	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}


	public Set<DictCatalog> getChildDictCatalog() {
		return childDictCatalog;
	}


	public void setChildDictCatalog(Set<DictCatalog> childDictCatalog) {
		this.childDictCatalog = childDictCatalog;
	}

	public Set<Dict> getDicts() {
		return dicts;
	}

	public void setDicts(Set<Dict> dicts) {
		this.dicts = dicts;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getDictCatalogWithCount() {
		return dictCatalogWithCount;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
}
