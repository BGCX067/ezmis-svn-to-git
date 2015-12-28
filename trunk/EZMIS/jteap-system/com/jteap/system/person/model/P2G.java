package com.jteap.system.person.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.jteap.system.group.model.Group;

/**
 * 
 * 人员组织关联对象，这是在JTEAP2.0升级后新出现的概念
 * 人员和组织是多对多的关系，但是关系过程当中还附加有
 * 其他性质，所以将其独立进行映射
 * @author tantyou
 * @date 2008-1-30
 */
@Entity
@Table(name="tb_sys_person2group")
public class P2G {
	//编号
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	private String id;
	
	//人员
	@ManyToOne()
	@JoinColumn(name="personid")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Person person;
	
	
	//组织
	@ManyToOne()
	@JoinColumn(name="groupid")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Group group;
	
	//是否管理员
	@Column(name="isadmin")
	private boolean isAdmin;
	
	
	
	@Column(name="indicator")
	private String indicator;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	
	
	
	
	
	
}
