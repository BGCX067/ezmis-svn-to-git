package com.jteap.system.doclib.model;

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

/**
 * TbDoclibFv entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_DOCLIB_FV")
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibFv {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private Serializable id; // 编号

	@ManyToOne()
	@JoinColumn(name = "DOCLIB_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Doclib doclib;

	@Column(name = "FIELD_ID")
	private String fieldId;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private String value;

	
	// Property accessors

	
	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public Doclib getDoclib() {
		return doclib;
	}

	public void setDoclib(Doclib doclib) {
		this.doclib = doclib;
	}

}