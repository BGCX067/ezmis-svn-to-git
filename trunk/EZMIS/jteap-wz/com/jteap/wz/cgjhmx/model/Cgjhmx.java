/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.cgjhmx.model;
import java.util.HashSet;
import java.util.Set;

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

import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;


@Entity
@Table(name = "TB_WZ_YCGJHMX")
@SuppressWarnings("unused")
public class Cgjhmx{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
//	@Column(name = "CGJHBH")	
//	private java.lang.String cgjhbh;
	
//	@Column(name = "WZBM")	
//	private java.lang.String wzbm;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzdagl;  //物资档案
	
	@ManyToOne()
	@JoinColumn(name="CGJHBH")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Cgjhgl cgjhgl; //采购计划
	
	
	@Transient
	private java.lang.Double je;  //金额
	
	@Column(name = "XH")	
	private java.lang.String xh;  //序号
	
	
	@Column(name = "JHDJ")	
	private java.lang.Double jhdj;  //计划单价
	
	
	@Column(name = "CGJLDW")	
	private java.lang.String cgjldw;  //采购计量单位
	
	
	@Column(name = "CGSL")	
	private double cgsl=0d;   //采购数量
	
	
	@Column(name = "HSXS")	
	private double hsxs=0d;     //换算系数
	
	
	@Column(name = "JHDHRQ")	
	private java.util.Date jhdhrq;     //计划到货日期
	
	
	@Column(name = "DHSL")	
	private double dhsl;        // 到货数量
	
	
	@Column(name = "CGFX")	
	private java.lang.String cgfx;    //采购方向
	
	@Column(name="CGY")
	private  java.lang.String cgy;     // 采购员
	
	
	@Column(name = "ZT")	
	private java.lang.String zt="0";    //状态
	
	@Column(name = "BZ")
	private java.lang.String bz = "";   //备注 
	
	@Column(name = "XQJHMX")	
	private java.lang.String xqjhmx;
	
	@Column(name="DYSZT")
	private String dyszt;
	
	@Column(name="DYSCZR")
	private String dysczr;

	public java.lang.String getXqjhmx() {
		return xqjhmx;
	}
	public void setXqjhmx(java.lang.String xqjhmx) {
		this.xqjhmx = xqjhmx;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Cgjhgl getCgjhgl() {
		return cgjhgl;
	}
	public void setCgjhgl(Cgjhgl cgjhgl) {
		this.cgjhgl = cgjhgl;
	}

	public Wzda getWzdagl() {
		return wzdagl;
	}
	public void setWzdagl(Wzda wzdagl) {
		this.wzdagl = wzdagl;
	}
	public java.lang.String getXh() {
		return this.xh;
	}
	
	public void setXh(java.lang.String value) {
		this.xh = value;
	}
	public java.lang.Double getJhdj() {
		return jhdj;
	}
	public void setJhdj(java.lang.Double jhdj) {
		this.jhdj = jhdj;
	}
	public java.lang.String getCgjldw() {
		return this.cgjldw;
	}
	
	public void setCgjldw(java.lang.String value) {
		this.cgjldw = value;
	}
	public double getCgsl() {
		return this.cgsl;
	}
	
	public void setCgsl(double value) {
		this.cgsl = value;
	}
	public double getHsxs() {
		return this.hsxs;
	}
	
	public void setHsxs(double value) {
		this.hsxs = value;
	}
	public java.util.Date getJhdhrq() {
		return this.jhdhrq;
	}
	
	public void setJhdhrq(java.util.Date value) {
		this.jhdhrq = value;
	}
	public double getDhsl() {
		return this.dhsl;
	}
	
	public void setDhsl(double value) {
		this.dhsl = value;
	}
	public java.lang.String getCgfx() {
		return this.cgfx;
	}
	
	public void setCgfx(java.lang.String value) {
		this.cgfx = value;
	}
	
	public void setZt(java.lang.String value) {
		this.zt = value;
	}
	public Double getJe() {
		Double retJe = 0.00;
		if(this.jhdj!=null){
			retJe=this.jhdj*this.cgsl;
		}
		return retJe;
	}
	public void setJe(Double je) {
		this.je = je;
	}
	public java.lang.String getCgy() {
		return cgy;
	}
	public void setCgy(java.lang.String cgy) {
		this.cgy = cgy;
	}
	public java.lang.String getZt() {
		return zt;
	}
	
	public Person getPerson() {
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
		return pm.findUniqueBy("userLoginName", this.cgy);
	}
	
	public XqjhDetail getXqjhDetail() {
		if(this.xqjhmx==null || this.xqjhmx.equals("")){
			XqjhDetail p = new XqjhDetail();
			p.setId("");
			p.setWzbm("");
			p.setXqjh(new Xqjh());
			return p;
		}
		XqjhDetailManager pm = (XqjhDetailManager)SpringContextUtil.getBean("xqjhDetailManager");
		return pm.findUniqueBy("id", this.xqjhmx);
	}
	
	public java.lang.String getBz() {
		return bz;
	}
	public void setBz(java.lang.String bz) {
		this.bz = bz;
	}
	
	public String getDyszt() {
		return dyszt;
	}

	public void setDyszt(String dyszt) {
		this.dyszt = dyszt;
	}

	public String getDysczr() {
		return dysczr;
	}

	public void setDysczr(String dysczr) {
		this.dysczr = dysczr;
	}
	
}

