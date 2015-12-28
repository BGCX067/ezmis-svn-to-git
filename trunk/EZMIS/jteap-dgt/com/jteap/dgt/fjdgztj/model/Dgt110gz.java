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
 * 党工团110实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_DGT_110GZ")
@SuppressWarnings("unchecked")
public class Dgt110gz {

	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	
	@Column(name="RIQI")
	private Date riqi;
	
	@Column(name="ZGNAME")
	private String zgname;
	
	@Column(name="GZLX")
	private String gzlx;
	
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

 
	public Date getRiqi() {
		return riqi;
	}

	public void setRiqi(Date riqi) {
		this.riqi = riqi;
	}
	 
	public String getZgname() {
		return zgname;
	}

	public void setZgname(String zgname) {
		this.zgname = zgname;
	}

	public Tongji getTongji() {
		return tongji;
	}

	public void setTongji(Tongji tongji) {
		this.tongji = tongji;
	}

	public String getGzlx() {
		return gzlx;
	}

	public void setGzlx(String gzlx) {
		this.gzlx = gzlx;
	}
	
}
