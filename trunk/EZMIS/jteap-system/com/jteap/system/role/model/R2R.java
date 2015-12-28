package com.jteap.system.role.model;

import java.io.Serializable;

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
import org.hibernate.annotations.Type;

import com.jteap.system.resource.model.Resource;

@Entity
@Table(name="tb_sys_role2res")
public class R2R {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private Serializable id;		// 编号
	
	@ManyToOne()
	@JoinColumn(name="roleid")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Role role;
	
	@ManyToOne()                   
	@JoinColumn(name="resourcesid")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Resource resource;
	
	@Column(name="communicable")  //是否可以传播
	private boolean communicable;
	
	@Column(name="inheritflag") 	//是否是继承的资源
	private boolean inherit;

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public boolean isCommunicable() {
		return communicable;
	}

	public void setCommunicable(boolean communicable) {
		this.communicable = communicable;
	}

	public boolean isInherit() {
		return inherit;
	}

	public void setInherit(boolean inherit) {
		this.inherit = inherit;
	}
	
}
