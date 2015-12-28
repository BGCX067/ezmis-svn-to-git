package com.jteap.wz.tjny.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.fytj.model.Fytj;
import com.jteap.wz.sfjctj.model.Sfjc;
import com.jteap.wz.sfzjtj.model.Sfzj;


/**
 * 统计年月实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="DATA_WZ_NY")
@SuppressWarnings("unchecked")
public class Tjny {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="NY")
	private String ny;
	
	@Column(name="BBLB")
	private String bblb;
	
	@Column(name="SORTNO")
	private String sortno;
	
	@OneToMany(mappedBy="tjny")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="TJNY")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Sfzj> Sfzjs;
	
	@OneToMany(mappedBy="tjny")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="TJNY")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Sfjc> Sfjcs;
	
	@OneToMany(mappedBy="tjny")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="TJNY")
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Fytj> Fytjs;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getNy() {
		return ny;
	}

	public void setNy(String ny) {
		this.ny = ny;
	}

	public String getBblb() {
		return bblb;
	}

	public void setBblb(String bblb) {
		this.bblb = bblb;
	}

	public String getSortno() {
		return sortno;
	}

	public void setSortno(String sortno) {
		this.sortno = sortno;
	}

	public Set<Sfzj> getSfzjs() {
		return Sfzjs;
	}

	public void setSfzjs(Set<Sfzj> sfzjs) {
		Sfzjs = sfzjs;
	}

	public Set<Sfjc> getSfjcs() {
		return Sfjcs;
	}

	public void setSfjcs(Set<Sfjc> sfjcs) {
		Sfjcs = sfjcs;
	}

	public Set<Fytj> getFytjs() {
		return Fytjs;
	}

	public void setFytjs(Set<Fytj> fytjs) {
		Fytjs = fytjs;
	}

}
