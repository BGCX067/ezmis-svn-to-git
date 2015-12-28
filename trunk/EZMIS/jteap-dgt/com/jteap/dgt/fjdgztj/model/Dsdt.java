package com.jteap.dgt.fjdgztj.model;

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
 * 导师带徒实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_DGT_DSDT")
@SuppressWarnings("unchecked")
public class Dsdt {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="MONTH")
	private Integer month;
	
	@Column(name="RESULT")
	private String result;
	
	@Column(name="TUDI")
	private String tudi;
	
	@Column(name="SHIFU")
	private String shifu;

	@Column(name="CONTENT")
	private String content;
	
	@ManyToOne
	@JoinColumn(name="tongji_id")
	@LazyToOne(LazyToOneOption.PROXY)
	private Tongji tongji;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTudi() {
		return tudi;
	}

	public void setTudi(String tudi) {
		this.tudi = tudi;
	}

	public String getShifu() {
		return shifu;
	}

	public void setShifu(String shifu) {
		this.shifu = shifu;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Tongji getTongji() {
		return tongji;
	}

	public void setTongji(Tongji tongji) {
		this.tongji = tongji;
	}


	
}
