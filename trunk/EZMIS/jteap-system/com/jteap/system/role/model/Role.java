package com.jteap.system.role.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

import com.jteap.system.person.model.P2Role;

@Entity
@Table(name = "tb_sys_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role {
	// 编号
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private Serializable id;

	// 角色名称
	@Column(name = "ROLENAME")
	private String rolename;
	// 说明
	@Column(name = "remark")
	private String remark;
	// 是否继承权限
	@Column(name = "inheritable")
	private boolean inheritable;
	// 排序
	@Column(name = "sortno")
	private int sortNo;

	// 创建者
	@Column(name = "creator")
	private String creator;

	// 父角色
	@ManyToOne()
	@JoinColumn(name = "parentid")
	@LazyToOne(LazyToOneOption.PROXY)
	private Role parentRole;

	// 子角色
	@OneToMany(mappedBy = "parentRole")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "sortno")
	private Set<Role> childRoles = new HashSet<Role>();

	// 这个角色所拥有的人员
	@OneToMany(mappedBy = "role")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<P2Role> persons;

	// 角色拥有的资源
	@OneToMany(mappedBy = "role")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<R2R> resources;

	// 角色内码
	@Column(name="ROLE_SN")
	private String roleSn;

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isInheritable() {

		return inheritable;
	}

	public void setInheritable(boolean inheritable) {

		this.inheritable = inheritable;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public Role getParentRole() {
		return parentRole;
	}

	public void setParentRole(Role parentRole) {
		this.parentRole = parentRole;
	}

	public Set<Role> getChildRoles() {
		return childRoles;
	}

	public void setChildRoles(Set<Role> childRoles) {
		this.childRoles = childRoles;
	}

	public Set<P2Role> getPersons() {
		return persons;
	}

	public void setPersons(Set<P2Role> persons) {
		this.persons = persons;
	}

	public Set<R2R> getResources() {
		return resources;
	}

	public void setResources(Set<R2R> resources) {
		this.resources = resources;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getRoleSn() {
		return roleSn;
	}

	public void setRoleSn(String roleSn) {
		this.roleSn = roleSn;
	}

	/**
	 * 
	 * 方法功能描述 :取得角色名
	 * 
	 * @author 唐剑钢
	 * @return 2008-1-29 返回类型：String
	 */
	public String getPathWithText() {
		String parentPath = "";
		if (this.getParentRole() != null) {
			parentPath = this.getParentRole().getPathWithText();
		}
		String path = parentPath + "/" + this.getRolename();
		return path;
	}

}
