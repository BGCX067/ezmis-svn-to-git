/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.kwwh.model;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.wzda.model.Wzda;

import java.util.*;


@Entity
@Table(name = "TB_WZ_SKWGL")
public class Kw{//库位

	//columns START
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "CWMC")
	private java.lang.String cwmc;//仓位名称
	@Column(name = "WZZJM")
	private java.lang.String wzzjm;//物资
	@Column(name = "CKID")
	private java.lang.String ckid;//仓库ID
	@Column(name = "SXXJBBM")
	private java.lang.String sxxjbbm;//树信息
	@Column(name = "BZ")
	private java.lang.String bz;//备注

	@ManyToOne()
	@JoinColumn(name="SJID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Kw pkw;// 父亲组织
	
	@OneToMany(mappedBy="pkw")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Kw> ckw = new ArrayList<Kw>();	//子组织
	
	//@Column(name = "SJID")
	//private java.lang.String sjid;//上级ID

	// 所有内容
	@OneToMany(mappedBy="kw")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Wzda> wzda;
	
	public Set<Wzda> getWzda() {
		return wzda;
	}
	public void setWzda(Set<Wzda> wzda) {
		this.wzda = wzda;
	}
	public Kw getPkw() {
		return pkw;
	}
	public void setPkw(Kw pkw) {
		this.pkw = pkw;
	}
	public List<Kw> getCkw() {
		return ckw;
	}
	public void setCkw(List<Kw> ckw) {
		this.ckw = ckw;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getCwmc() {
		return this.cwmc;
	}
	
	public void setCwmc(java.lang.String value) {
		this.cwmc = value;
	}
	
	public java.lang.String getWzzjm() {
		return this.wzzjm;
	}
	
	public void setWzzjm(java.lang.String value) {
		this.wzzjm = value;
	}
	
	public java.lang.String getSxxjbbm() {
		return this.sxxjbbm;
	}
	
	public java.lang.String getCkid() {
		return ckid;
	}
	public void setCkid(java.lang.String ckid) {
		this.ckid = ckid;
	}
	public void setSxxjbbm(java.lang.String value) {
		this.sxxjbbm = value;
	}
	
	public java.lang.String getBz() {
		return this.bz;
	}
	
	public void setBz(java.lang.String value) {
		this.bz = value;
	}
	
	public Ckgl getCk(){
		CkglManager ckglManager = (CkglManager)SpringContextUtil.getBean("ckglManager");
		return ckglManager.findUniqueBy("id", this.ckid);
	}
	
}

