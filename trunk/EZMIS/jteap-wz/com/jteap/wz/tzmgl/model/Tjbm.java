package com.jteap.wz.tzmgl.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_WZ_TJBM")
public class Tjbm {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name="WZBM")
	private String wzbm;
	
	@Column(name="WZBMLR")
	private String wzbmlr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWzbm() {
		return wzbm;
	}

	public void setWzbm(String wzbm) {
		this.wzbm = wzbm;
	}

	public String getWzbmlr() {
		return wzbmlr;
	}

	public void setWzbmlr(String wzbmlr) {
		this.wzbmlr = wzbmlr;
	}
	

}
