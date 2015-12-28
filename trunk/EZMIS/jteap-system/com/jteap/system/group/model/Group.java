package com.jteap.system.group.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

import com.jteap.system.person.model.P2G;

/**
 * 组织对象
 * @author tantyou
 * @date 2008-1-12
 */
@Entity
@Table(name="tb_sys_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Group {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private Serializable id;		// 编号
	
	@Column(name="GROUPNAME")
	private String groupName;	// 组织名称
	
	@Column(name="remark")
	private String remark;	// 说明
	
	@ManyToOne()
	@JoinColumn(name="parentid")
	@LazyToOne(LazyToOneOption.PROXY)
	private Group parentGroup;// 父亲组织
	
	@OneToMany(mappedBy="parentGroup")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="sortno")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Group> childGroups = new ArrayList<Group>();	//子组织
	
	//所拥有的人员
	@OneToMany(mappedBy="group")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private Set<P2G> persons;

	@Column(name="sortno")
	private int sortNo;

	//创建人
	@Column(name="createby")
	private String creator;
	
	//用于在树型结构中显示当前组织中有多少人员的功能
	@SuppressWarnings("unused")
	@Transient
	private String groupNameWithCount;
	
	// 组织唯一标识
	@Column(name="GROUP_SN")
	private String groupSn;

	public String getGroupNameWithCount() {
		int count = this.getPersonCount();
		return this.groupName+"("+count+")";
	}
	
	/**
	 * 取得当前组织所拥有的人员的个数，包括子组织
	 * @return
	 */
	private int getPersonCount(){
		int result = this.persons == null?0:this.persons.size();
		if(this.childGroups!=null){
			for (Group child : this.childGroups) {
				result += child.getPersonCount();
			}
		}
		return result;
	}

	public void setGroupNameWithCount(String groupNameWithCount) {
		this.groupNameWithCount = groupNameWithCount;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Group getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(Group parentGroup) {
		this.parentGroup = parentGroup;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}


	public List<Group> getChildGroups() {
		return childGroups;
	}

	public void setChildGroups(List<Group> childGroups) {
		this.childGroups = childGroups;
	}

	public Set<P2G> getPersons() {
		return persons;
	}

	public void setPersons(Set<P2G> persons) {
		this.persons = persons;
	}

	/**
	 * 
	 * 方法功能描述 :取得组织
	 * @author 唐剑钢
	 * @return
	 * 2008-1-29
	 * 返回类型：String
	 */
	public String getPathWithText(){
		String parentPath="";
		if(this.getParentGroup()!=null){
			parentPath=this.getParentGroup().getPathWithText();
		}
		String path=parentPath+"/"+this.getGroupName();
		return path;
	}

	@Override
	public String toString() {
		return "组织【"+this.getPathWithText()+"】";
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getGroupSn() {
		return groupSn;
	}

	public void setGroupSn(String groupSn) {
		this.groupSn = groupSn;
	}

}
