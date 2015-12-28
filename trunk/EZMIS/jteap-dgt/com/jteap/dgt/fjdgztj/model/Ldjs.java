package com.jteap.dgt.fjdgztj.model;

import java.util.Date;

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
 * 劳动竞赛实体类
 * @author lvchao
 *
 */

@Entity  
@Table(name="TB_DGT_LDJS")
@SuppressWarnings("unchecked")
public class Ldjs {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="PEPOLE_COUNT")
	private Integer pepole_count;
	
	@Column(name="RIQI")
	private Date riqi;
	
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

	public Integer getPepole_count() {
		return pepole_count;
	}

	public void setPepole_count(Integer pepole_count) {
		this.pepole_count = pepole_count;
	}

	public Date getRiqi() {
		return riqi;
	}

	public void setRiqi(Date riqi) {
		this.riqi = riqi;
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
