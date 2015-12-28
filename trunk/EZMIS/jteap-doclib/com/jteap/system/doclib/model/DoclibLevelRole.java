package com.jteap.system.doclib.model;

import java.util.Date;
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

import com.jteap.system.role.model.Role;

/**
 * AbstractTbDoclib entity provides the base persistence definition of the
 * TbDoclib entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_DOCLIB_LEVEL_ROLE")
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibLevelRole {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id; // 编号

	@ManyToOne()
	@JoinColumn(name="DOCLIB__LEVEL_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DoclibLevel doclibLevel;
	
	@ManyToOne()
	@JoinColumn(name="ROLE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Role role ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DoclibLevel getDoclibLevel() {
		return doclibLevel;
	}

	public void setDoclibLevel(DoclibLevel doclibLevel) {
		this.doclibLevel = doclibLevel;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	

}