package com.jteap.sb.sbtzgl.model;

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
 * 设备台帐分类
 * @author caofei
 */
@Entity
@Table(name = "TB_SB_SBTZGL_CATALOG")
public class SbtzCatalog {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;                               //编号
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private SbtzCatalog parent;                      //设备台帐分类父节点
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<SbtzCatalog> children;               //设备台帐分类子节点
	
	@Column(name = "flbm")
	private String flbm;                             //设备编码
	 
	@Column(name = "FLJB")
	private String fljb;                             //分类级别
	
	@Column(name = "FLMC")
	private String flmc;                             //分类名称
	
	@Column(name = "SORTNO")
	private String sortNo;                           //序列

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SbtzCatalog getParent() {
		return parent;
	}

	public void setParent(SbtzCatalog parent) {
		this.parent = parent;
	}

	public Set<SbtzCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<SbtzCatalog> children) {
		this.children = children;
	}

	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}

	public String getFljb() {
		return fljb;
	}

	public void setFljb(String fljb) {
		this.fljb = fljb;
	}

	public String getFlmc() {
		return flmc;
	}

	public void setFlmc(String flmc) {
		this.flmc = flmc;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	
	

}
