/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.crkrzgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.wzda.model.Wzda;


@Entity
@Table(name = "TB_WZ_YCRKRZMX")
public class Crkrzmx{

	//columns START
	@OneToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzda;
	
	
	@Column(name = "CRKSL")	
	private double crksl;
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "CRKJG")	
	private double crkjg;
	
	@Column(name = "crkdh")	
	private java.lang.String crkdh;
	
	
	@Column(name = "XH")	
	private java.lang.String xh;
	
	@ManyToOne()
	@JoinColumn(name="CRKRZID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Crkrzgl crkrzgl;

	@Transient
	private Double sysl;
	
	@Transient
	private Double syje;
	
	public Double getSysl() {
		try {
			this.sysl = this.wzda.getDqkc();
		} catch (Exception e) {
			sysl = 0d;
		}
		return sysl;
	}

	public void setSysl(Double sysl) {
		this.sysl = sysl;
	}

	public Double getSyje() {
		try {
			this.syje = this.wzda.getDqkc()*this.wzda.getJhdj();
		} catch (Exception e) {
			this.syje = 0d;
		}
		return syje;
	}

	public void setSyje(Double syje) {
		this.syje = syje;
	}

	public Wzda getWzda() {
		return wzda;
	}

	public void setWzda(Wzda wzda) {
		this.wzda = wzda;
	}

	public double getCrksl() {
		return this.crksl;
	}
	
	public void setCrksl(double value) {
		this.crksl = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public double getCrkjg() {
		return this.crkjg;
	}
	
	public void setCrkjg(double value) {
		this.crkjg = value;
	}
	public Crkrzgl getCrkrzgl() {
		return crkrzgl;
	}

	public void setCrkrzgl(Crkrzgl crkrzgl) {
		this.crkrzgl = crkrzgl;
	}

	public java.lang.String getXh() {
		return this.xh;
	}
	
	public void setXh(java.lang.String value) {
		this.xh = value;
	}

	public java.lang.String getCrkdh() {
		return crkdh;
	}

	public void setCrkdh(java.lang.String crkdh) {
		this.crkdh = crkdh;
	}
	
	
}

