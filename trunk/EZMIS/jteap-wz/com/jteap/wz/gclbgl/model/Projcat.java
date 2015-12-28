package com.jteap.wz.gclbgl.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import com.jteap.wz.gcxmgl.model.Proj;

@Entity
@Table(name = "TB_WZ_PROJCAT")
public class Projcat {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name="PROJCATCODE")
	private String projcatcode;
	
	@Column(name="PROJCATNAME")
	private String projcatname;
	
	@Column(name="MEMO")
	private String memo;
	
	@Column(name="PREDEFINED")
	private String predefined;
	
	@OneToMany(mappedBy="projcat")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("projcode asc")
	private Set<Proj> projs ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjcatcode() {
		return projcatcode;
	}

	public void setProjcatcode(String projcatcode) {
		this.projcatcode = projcatcode;
	}

	public String getProjcatname() {
		return projcatname;
	}

	public void setProjcatname(String projcatname) {
		this.projcatname = projcatname;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPredefined() {
		return predefined;
	}

	public void setPredefined(String predefined) {
		this.predefined = predefined;
	}

	public Set<Proj> getProjs() {
		return projs;
	}

	public void setProjs(Set<Proj> projs) {
		this.projs = projs;
	}

}
