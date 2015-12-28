package com.jteap.system.resource.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.system.person.model.Person;

/**
 * 个性化资源表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_sys_RESOURCES_USERS")
public class ResourcesUsers {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private Serializable id;

	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Resource resource;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Person user;

	@Column(name = "RESOURCE_NEWNAME")
	private String newName;

	@Column(name = "IS_QUICK_LINK")
	private char isQuickLink;

	public char getIsQuickLink() {
		return isQuickLink;
	}

	public void setIsQuickLink(char isQuickLink) {
		this.isQuickLink = isQuickLink;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Person getUser() {
		return user;
	}

	public void setUser(Person user) {
		this.user = user;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
}
