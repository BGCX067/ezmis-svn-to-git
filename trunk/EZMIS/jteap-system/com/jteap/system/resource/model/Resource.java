package com.jteap.system.resource.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

import com.jteap.system.person.model.P2Res;
import com.jteap.system.role.model.R2R;

/**
 * 资源
 * 
 * @author tantyou
 * @date 2008-1-17
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_sys_resources")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resource {
	/**
	 * 资源类型 操作和模块
	 */
	public static final String RES_TYPE_MODULE = "模块";
	public static final String RES_TYPE_OPERATION = "操作";

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private Serializable id;

	// 资源名称
	@Column(name = "RESNAME")
	private String resName;

	// 排序
	@Column(name = "sortno")
	private int sortNo;

	// 是否可见
	@Column(name = "visiabled")
	private boolean visiabled;
	
	//是否做后一级模块
	@Column(name = "ISLEAFMODULE")
	private Boolean leafModule;

	// 说明
	@Column(name = "remark")
	private String remark;

	// 父亲资源
	@ManyToOne()
	@JoinColumn(name="parentid")
	@LazyToOne(LazyToOneOption.PROXY)
	private Resource parentRes;

	// 儿子资源
	@OneToMany(mappedBy="parentRes")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy(clause="sortno")
	private Set<Resource> childRes = new HashSet<Resource>(); // 子组织

	
	
	
	// 资源类型
	@Column(name = "RESTYPE")
	private String type;

	// 2008-2-19 tantyou 一般情况下是不会使用该映射的，主要作用是用于删除资源时，将附带的关联对象也级联删除

	@OneToMany(mappedBy = "resource")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<P2Res> p2ress;

	// 2008-2-19 tantyou 一般情况下是不会使用该映射的，主要作用是用于删除资源时，将附带的关联对象也级联删除
	@OneToMany(mappedBy = "resource")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<R2R> r2rs;

	// 资源种类（自定义、系统定义）
	@Column(name = "resstyle")
	private char resStyle;

	public char getResStyle() {
		return resStyle;
	}

	public void setResStyle(char resStyle) {
		this.resStyle = resStyle;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Resource getParentRes() {
		return parentRes;
	}

	public void setParentRes(Resource parentRes) {
		this.parentRes = parentRes;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public boolean isVisiabled() {
		return visiabled;
	}

	public void setVisiabled(boolean visiabled) {
		this.visiabled = visiabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Resource> getChildRes() {
		return childRes;
	}

	public void setChildRes(Set<Resource> childRes) {
		this.childRes = childRes;
	}

	public Boolean getLeafModule() {
		return leafModule;
	}

	public void setLeafModule(Boolean leafModule) {
		this.leafModule = leafModule;
	}

	/**
	 * 取得当前资源的路径
	 * 
	 * @return
	 */
	public String getPathWithText() {
		String parentPath = "";
		if (this.getParentRes() != null) {
			parentPath = this.getParentRes().getPathWithText();
		}
		String path = parentPath + "/" + this.getResName();
		return path;
	}

	public Set<P2Res> getP2ress() {
		return p2ress;
	}

	public void setP2ress(Set<P2Res> p2ress) {
		this.p2ress = p2ress;
	}

	public Set<R2R> getR2rs() {
		return r2rs;
	}

	public void setR2rs(Set<R2R> r2rs) {
		this.r2rs = r2rs;
	}

	@Override
	public String toString() {
		return "资源【" + this.getPathWithText() + "】";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Resource) {
			Resource res = (Resource) obj;
			return res.getId().toString().equals(this.getId().toString());
		} else
			return false;
	}
}
