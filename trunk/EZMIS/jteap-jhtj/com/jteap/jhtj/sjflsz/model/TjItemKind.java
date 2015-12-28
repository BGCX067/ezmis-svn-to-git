package com.jteap.jhtj.sjflsz.model;

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
 * 
 *描述：数据分类
 *时间：2010-4-7
 *作者：童贝
 *
 */
@Entity
@Table(name="TJ_ITEMKIND")
public class TjItemKind {
	public final static String FLFLAG_RI="1"; 
	public final static String FLFLAG_YUE="2"; 
	public final static String FLFLAG_NIAN="3"; 
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="kid")
	private String kid;//分类编码
	@Column(name="kname")
	private String kname;//分类名称
	@Column(name="flflag")
	private Long flflag;//分类标志：0-节点数据，1-日数据，2-月数据，3-年数据，4-机组数据
	@Column(name="uflag")
	private Long uflag;//计算项修改标志
	@Column(name="dflag")
	private Long dflag=new Long(1);//操作标识
	@Column(name="sortno")
	private Long sortno;
	
	@ManyToOne()
	@JoinColumn(name = "K_PRIOR")
	@LazyToOne(LazyToOneOption.PROXY)
	private TjItemKind parentKind;       //父节点
	
	@OneToMany(mappedBy = "parentKind")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "sortno")
	private Set<TjItemKind> childKind;  //子节点 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKname() {
		return kname;
	}
	public void setKname(String kname) {
		this.kname = kname;
	}
	public Long getFlflag() {
		return flflag;
	}
	public void setFlflag(Long flflag) {
		this.flflag = flflag;
	}
	public Long getUflag() {
		return uflag;
	}
	public void setUflag(Long uflag) {
		this.uflag = uflag;
	}
	public Long getDflag() {
		return dflag;
	}
	public void setDflag(Long dflag) {
		this.dflag = dflag;
	}
	public TjItemKind getParentKind() {
		return parentKind;
	}
	public void setParentKind(TjItemKind parentKind) {
		this.parentKind = parentKind;
	}
	public Set<TjItemKind> getChildKind() {
		return childKind;
	}
	public void setChildKind(Set<TjItemKind> childKind) {
		this.childKind = childKind;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public Long getSortno() {
		return sortno;
	}
	public void setSortno(Long sortno) {
		this.sortno = sortno;
	}

}
