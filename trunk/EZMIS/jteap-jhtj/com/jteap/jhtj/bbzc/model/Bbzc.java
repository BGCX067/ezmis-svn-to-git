package com.jteap.jhtj.bbzc.model;

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

@Entity
@Table(name="TJ_BBFL")
public class Bbzc {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name="flbm")
	private String flbm;
	
	@Column(name="flmc")
	private String flmc;
	
	@ManyToOne()
	@JoinColumn(name = "KPrior")
	@LazyToOne(LazyToOneOption.PROXY)
	private Bbzc parentBbzc;       //父节点
	
	@OneToMany(mappedBy = "parentBbzc")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "sortno")
	private Set<Bbzc> childBbzc;  //子节点 
	@Column(name="sortno")
	private Long sortno;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlmc() {
		return flmc;
	}

	public void setFlmc(String flmc) {
		this.flmc = flmc;
	}

	public Bbzc getParentBbzc() {
		return parentBbzc;
	}

	public void setParentBbzc(Bbzc parentBbzc) {
		this.parentBbzc = parentBbzc;
	}

	public Set<Bbzc> getChildBbzc() {
		return childBbzc;
	}

	public void setChildBbzc(Set<Bbzc> childBbzc) {
		this.childBbzc = childBbzc;
	}

	public Long getSortno() {
		return sortno;
	}

	public void setSortno(Long sortno) {
		this.sortno = sortno;
	}

	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}
	
	
}
