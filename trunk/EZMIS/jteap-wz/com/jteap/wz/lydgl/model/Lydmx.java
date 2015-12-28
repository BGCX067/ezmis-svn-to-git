/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.lydgl.model;

import java.text.DecimalFormat;

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

import com.jteap.core.utils.DateUtils;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.wzlysq.model.WzlysqDetail;


@Entity
@Table(name = "TB_WZ_YLYDMX")
public class Lydmx{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
//	@Column(name = "LYDBH")	
//	private java.lang.String lydbh;
	@OneToOne()
	@JoinColumn(name="LYDBH")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Lydgl lydgl;
	
	@Column(name = "XH")	
	private java.lang.String xh;
	
	@OneToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzbm;
	
	@Column(name = "JLDW")	
	private java.lang.String jldw;
	
	@Column(name = "JHDJ")	
	private double jhdj;
	
	@Column(name = "PZLYSL")	
	private double pzlysl;
	
	
	@Column(name = "SJLYSL")	
	private double sjlysl;
	
	@Column(name = "LYSJ")	
	private java.util.Date lysj;
	
	@ManyToOne()
	@JoinColumn(name="LYSQMX")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private WzlysqDetail wzlysqDetail;
	
	@Column(name = "ZT")	
	private String zt;
	
	@Column(name = "ZFZT")	
	private String zfzt;
	
	@Column(name = "GCLB")	
	private String gclb;
	
	@Column(name = "GCXM")	
	private String gcxm;
	
	@Column(name = "SJJE")	
	private double sjje;
	
	@Transient
	private double zje;
	
	@Transient
	private double sjdj;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.util.Date getLysj() {
		return lysj;
	}

	public void setLysj(java.util.Date lysj) {
		this.lysj = lysj;
	}

	public Lydgl getLydgl() {
		return lydgl;
	}

	public void setLydgl(Lydgl lydgl) {
		this.lydgl = lydgl;
	}

	public java.lang.String getXh() {
		return xh;
	}

	public void setXh(java.lang.String xh) {
		this.xh = xh;
	}


	public Wzda getWzbm() {
		return wzbm;
	}

	public void setWzbm(Wzda wzbm) {
		this.wzbm = wzbm;
	}

	public java.lang.String getJldw() {
		return jldw;
	}

	public void setJldw(java.lang.String jldw) {
		this.jldw = jldw;
	}

	public double getJhdj() {
		return jhdj;
	}

	public void setJhdj(double jhdj) {
		this.jhdj = jhdj;
	}

	public double getPzlysl() {
		return pzlysl;
	}

	public void setPzlysl(double pzlysl) {
		this.pzlysl = pzlysl;
	}

	public double getSjlysl() {
		return sjlysl;
	}

	public void setSjlysl(double sjlysl) {
		this.sjlysl = sjlysl;
	}

	public double getZje() {
		try {
			this.zje = jhdj*sjlysl;
		} catch (Exception e) {
			this.zje=0d;
		}
		return this.zje;
	}
 
	public double getSjdj() {
		try {
			DecimalFormat decimalFormat = new DecimalFormat("###.00");
			this.sjdj = Double.valueOf(decimalFormat.format(sjje/sjlysl));
		} catch (Exception e) {
			this.sjdj=0d;
		}
		return this.sjdj;
	}

	public void setSjdj(double sjdj) {
		this.sjdj = sjdj;
	}

	public void setZje(double zje) {
		this.zje = zje;
	}

	public WzlysqDetail getWzlysqDetail() {
		return wzlysqDetail;
	}

	public void setWzlysqDetail(WzlysqDetail wzlysqDetail) {
		this.wzlysqDetail = wzlysqDetail;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getZfzt() {
		return zfzt;
	}

	public void setZfzt(String zfzt) {
		this.zfzt = zfzt;
	}

	public double getSjje() {
		try {
			DecimalFormat decimalFormat = new DecimalFormat("###.00");
			this.sjje = Double.valueOf(decimalFormat.format(sjje));
		} catch (Exception e) {
			this.sjje=0d;
		}
		return sjje;
	}

	public void setSjje(double sjje) {
		this.sjje = sjje;
	}
	
	public String getNowDate(){
		return DateUtils.getDate("yyyy-MM-dd");
	}

	public String getGclb() {
		return gclb;
	}

	public void setGclb(String gclb) {
		this.gclb = gclb;
	}

	public String getGcxm() {
		return gcxm;
	}

	public void setGcxm(String gcxm) {
		this.gcxm = gcxm;
	}
}

