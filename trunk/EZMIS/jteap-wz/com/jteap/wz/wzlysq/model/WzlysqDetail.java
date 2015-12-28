package com.jteap.wz.wzlysq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.xqjh.model.XqjhDetail;

@Entity
@Table(name = "TB_WZ_YLYSQMX")
public class WzlysqDetail {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "JLDW")
	private String jldw;
	
	@Column(name = "JG")
	private Double jg;
	
	@Column(name = "WZBM")
	private String wzbm;
	
	@Column(name = "LYSL")
	private Double lysl;

	@Column(name = "SQSL")
	private Double sqsl;
	
	@ManyToOne()
	@JoinColumn(name="LYSQBH")
	@LazyToOne(LazyToOneOption.PROXY)
	private Wzlysq wzlysq;
	
	@Column(name = "XH")
	private String xh;
	
	@OneToOne()
	@JoinColumn(name="XQJHMXBM")
	@LazyToOne(LazyToOneOption.PROXY)
	private XqjhDetail xqjhDetail;
	
	public double getJe() {
		if(sqsl==null||jg==null)
			return 0;
		return this.sqsl*this.jg;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getWzbm() {
		return wzbm;
	}
	public void setWzbm(String wzbm) {
		this.wzbm = wzbm;
	}
	public Double getJg() {
		return jg;
	}

	public void setJg(Double jg) {
		this.jg = jg;
	}

	public Double getLysl() {
		return lysl;
	}

	public void setLysl(Double lysl) {
		this.lysl = lysl;
	}

	public Double getSqsl() {
		return sqsl;
	}

	public void setSqsl(Double sqsl) {
		this.sqsl = sqsl;
	}

	public Wzlysq getWzlysq() {
		return wzlysq;
	}
	public void setWzlysq(Wzlysq wzlysq) {
		this.wzlysq = wzlysq;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public XqjhDetail getXqjhDetail() {
		return xqjhDetail;
	}
	public void setXqjhDetail(XqjhDetail xqjhDetail) {
		this.xqjhDetail = xqjhDetail;
	}
}
