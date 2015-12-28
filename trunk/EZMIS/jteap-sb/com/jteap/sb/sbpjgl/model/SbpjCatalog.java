package com.jteap.sb.sbpjgl.model;

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
 * 设备评级分类
 * @author caofei
 *
 */
@Entity
@Table(name = "TB_SB_SBPJXX_CATALOG")
public class SbpjCatalog {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;                    //编码
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private SbpjCatalog parent;           //评级分类父节点
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="SORTNO")
	private Set<SbpjCatalog> children;    //评级分类子节点
	
	@Column(name = "flbm")
	private String flbm;                  //评级分类编码
	
	@Column(name = "flmc")
	private String flmc;                  //评级分类名称
	
	@Column(name = "fljb")
	private String fljb;                  //评级分类级别
	
	@Column(name = "sortno")
	private String sortNo;                //序列

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SbpjCatalog getParent() {
		return parent;
	}

	public void setParent(SbpjCatalog parent) {
		this.parent = parent;
	}

	public Set<SbpjCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<SbpjCatalog> children) {
		this.children = children;
	}

	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}

	public String getFlmc() {
		return flmc;
	}

	public void setFlmc(String flmc) {
		this.flmc = flmc;
	}

	public String getFljb() {
		return fljb;
	}

	public void setFljb(String fljb) {
		this.fljb = fljb;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	
	
	
}
