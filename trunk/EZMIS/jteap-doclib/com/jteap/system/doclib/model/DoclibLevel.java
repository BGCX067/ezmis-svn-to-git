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
@Table(name = "TB_DOCLIB_LEVEL")
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibLevel {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id; // 编号

	
	@Column(name = "DOCLIB_LELVE_NAME")
	private String levelName;
	
	@Column(name = "DOCLIB_LEVEL_DESC")
	private String levelDesc;
	
	
	
	@OneToMany(mappedBy = "doclibLevel")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<DoclibLevelRole> levelRoles ;
	
	@OrderBy(clause = "SORTNO")
	@Column(name = "SORTNO")
	private int sortNo;
	

	public Set<DoclibLevelRole> getLevelRoles() {
		return levelRoles;
	}

	public void setLevelRoles(Set<DoclibLevelRole> levelRoles) {
		this.levelRoles = levelRoles;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}
}