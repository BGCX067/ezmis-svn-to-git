package com.jteap.system.dataperm.model;

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

import com.jteap.system.role.model.Role;

@Entity
@Table(name = "TB_SYS_DATAPERM_ROLE")
public class Perm2Role {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="ROLEID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Role role;
	
	@ManyToOne()
	@JoinColumn(name="DATAPERMID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DataPerm perm;

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

	public DataPerm getPerm() {
		return perm;
	}

	public void setPerm(DataPerm perm) {
		this.perm = perm;
	}
	
	
}
