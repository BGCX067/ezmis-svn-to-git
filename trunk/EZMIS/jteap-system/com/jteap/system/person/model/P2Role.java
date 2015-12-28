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

import com.jteap.system.role.model.Role;
/**
 * Person to Role 中间对象
 * 
 * @author tantyou
 * @date 2008-1-30
 */
@Entity
@Table(name="tb_sys_person2role")
public class P2Role {
	//编号
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	private String id;
	
	//角色
	@ManyToOne()
	@JoinColumn(name="roleid")
	@LazyToOne(LazyToOneOption.PROXY)
    @Cascade(value={CascadeType.SAVE_UPDATE})
	private Role role;  //多对一映射
	
	//人员
	@ManyToOne()
	@JoinColumn(name="personid")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Person person;
	
	//指定人员应该是必须存在的，所以没有必要使用级联保存和级联更新
	@Column(name="indicator")
	private String indicator;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}




}
