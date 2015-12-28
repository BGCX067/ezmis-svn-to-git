package com.jteap.jx.bpbjgl.model;

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
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

/**
 * 备品备件-专业分类
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_BPBJGL_ZYFL")
public class BpbjZyfl {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 专业名称
	@Column(name = "ZYMC")
	private String zymc;

	@ManyToOne
	@JoinColumn(name = "parentid")
	@LazyToOne(LazyToOneOption.PROXY)
	private BpbjZyfl parentZyfl;

	@OneToMany(mappedBy = "parentZyfl")
	@Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "sortno")
	private Set<BpbjZyfl> bpbjZyfls;

	// 备品备件信息
	@OneToMany(mappedBy = "bpbjZyfl")
	@Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Bpbjxx> bpbjxxs;

	@Column(name = "sortno")
	private int sortNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public BpbjZyfl getParentZyfl() {
		return parentZyfl;
	}

	public void setParentZyfl(BpbjZyfl parentZyfl) {
		this.parentZyfl = parentZyfl;
	}

	public Set<BpbjZyfl> getBpbjZyfls() {
		return bpbjZyfls;
	}

	public void setBpbjZyfls(Set<BpbjZyfl> bpbjZyfls) {
		this.bpbjZyfls = bpbjZyfls;
	}

	public Set<Bpbjxx> getBpbjxxs() {
		return bpbjxxs;
	}

	public void setBpbjxxs(Set<Bpbjxx> bpbjxxs) {
		this.bpbjxxs = bpbjxxs;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

}
