/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.yx.tz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 保护定值修改记录实体bean
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
@Entity
@Table(name = "TB_YX_TZ_BHDZXGJL")
public class Bhdzxgjl{

	//columns START
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "BHMC")	
	private java.lang.String bhmc;
	
	
	@Column(name = "GGSJ")	
	private java.util.Date ggsj;
	
	
	@Column(name = "BHMCGQ1")	
	private java.lang.String bhmcgq1;
	
	
	@Column(name = "BHMCGQ2")	
	private java.lang.String bhmcgq2;
	
	
	@Column(name = "BHMCGQ3")	
	private java.lang.String bhmcgq3;
	
	
	@Column(name = "BHMCGQ4")	
	private java.lang.String bhmcgq4;
	
	
	@Column(name = "BHMCGQ5")	
	private java.lang.String bhmcgq5;
	
	
	@Column(name = "ZDZGQ1")	
	private java.lang.String zdzgq1;
	
	
	@Column(name = "ZDZGQ2")	
	private java.lang.String zdzgq2;
	
	
	@Column(name = "ZDZGQ3")	
	private java.lang.String zdzgq3;
	
	
	@Column(name = "ZDZGQ4")	
	private java.lang.String zdzgq4;
	
	
	@Column(name = "ZDZGQ5")	
	private java.lang.String zdzgq5;
	
	
	@Column(name = "BHMCGH1")	
	private java.lang.String bhmcgh1;
	
	
	@Column(name = "BHMCGH2")	
	private java.lang.String bhmcgh2;
	
	
	@Column(name = "BHMCGH3")	
	private java.lang.String bhmcgh3;
	
	
	@Column(name = "BHMCGH4")	
	private java.lang.String bhmcgh4;
	
	
	@Column(name = "BHMCGH5")	
	private java.lang.String bhmcgh5;
	
	
	@Column(name = "ZDZGH1")	
	private java.lang.String zdzgh1;
	
	
	@Column(name = "ZDZGH2")	
	private java.lang.String zdzgh2;
	
	
	@Column(name = "ZDZGH3")	
	private java.lang.String zdzgh3;
	
	
	@Column(name = "ZDZGH4")	
	private java.lang.String zdzgh4;
	
	
	@Column(name = "ZDZGH5")	
	private java.lang.String zdzgh5;
	
	
	@Column(name = "GGYY")	
	private java.lang.String ggyy;
	
	
	@Column(name = "GGFLR")	
	private java.lang.String ggflr;
	
	
	@Column(name = "GGZHR")	
	private java.lang.String ggzhr;
	
	
	@Column(name = "GGYHJC")	
	private java.lang.String ggyhjc;
	
	
	@Column(name = "BHMCHFQ1")	
	private java.lang.String bhmchfq1;
	
	
	@Column(name = "BHMCHFQ2")	
	private java.lang.String bhmchfq2;
	
	
	@Column(name = "BHMCHFQ3")	
	private java.lang.String bhmchfq3;
	
	
	@Column(name = "BHMCHFQ4")	
	private java.lang.String bhmchfq4;
	
	
	@Column(name = "BHMCHFQ5")	
	private java.lang.String bhmchfq5;
	
	
	@Column(name = "ZDZHFQ1")	
	private java.lang.String zdzhfq1;
	
	
	@Column(name = "ZDZHFQ2")	
	private java.lang.String zdzhfq2;
	
	
	@Column(name = "ZDZHFQ3")	
	private java.lang.String zdzhfq3;
	
	
	@Column(name = "ZDZHFQ4")	
	private java.lang.String zdzhfq4;
	
	
	@Column(name = "ZDZHFQ5")	
	private java.lang.String zdzhfq5;
	
	
	@Column(name = "BHMCHFH1")	
	private java.lang.String bhmchfh1;
	
	
	@Column(name = "BHMCHFH2")	
	private java.lang.String bhmchfh2;
	
	
	@Column(name = "BHMCHFH3")	
	private java.lang.String bhmchfh3;
	
	
	@Column(name = "BHMCHFH4")	
	private java.lang.String bhmchfh4;
	
	
	@Column(name = "BHMCHFH5")	
	private java.lang.String bhmchfh5;
	
	
	@Column(name = "ZDZHFH1")	
	private java.lang.String zdzhfh1;
	
	
	@Column(name = "ZDZHFH2")	
	private java.lang.String zdzhfh2;
	
	
	@Column(name = "ZDZHFH3")	
	private java.lang.String zdzhfh3;
	
	
	@Column(name = "ZDZHFH4")	
	private java.lang.String zdzhfh4;
	
	
	@Column(name = "ZDZHFH5")	
	private java.lang.String zdzhfh5;
	
	
	@Column(name = "HFYY")	
	private java.lang.String hfyy;
	
	
	@Column(name = "HFFLR")	
	private java.lang.String hfflr;
	
	
	@Column(name = "HFZHR")	
	private java.lang.String hfzhr;
	
	
	@Column(name = "HFYHJC")	
	private java.lang.String hfyhjc;
	
	
	@Column(name = "BZ")	
	private java.lang.String bz;
	
	
	@Column(name = "HFSJ")	
	private java.util.Date hfsj;
	

	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getBhmc() {
		return this.bhmc;
	}
	
	public void setBhmc(java.lang.String value) {
		this.bhmc = value;
	}
	public java.util.Date getGgsj() {
		return this.ggsj;
	}
	
	public void setGgsj(java.util.Date value) {
		this.ggsj = value;
	}
	public java.lang.String getBhmcgq1() {
		return this.bhmcgq1;
	}
	
	public void setBhmcgq1(java.lang.String value) {
		this.bhmcgq1 = value;
	}
	public java.lang.String getBhmcgq2() {
		return this.bhmcgq2;
	}
	
	public void setBhmcgq2(java.lang.String value) {
		this.bhmcgq2 = value;
	}
	public java.lang.String getBhmcgq3() {
		return this.bhmcgq3;
	}
	
	public void setBhmcgq3(java.lang.String value) {
		this.bhmcgq3 = value;
	}
	public java.lang.String getBhmcgq4() {
		return this.bhmcgq4;
	}
	
	public void setBhmcgq4(java.lang.String value) {
		this.bhmcgq4 = value;
	}
	public java.lang.String getBhmcgq5() {
		return this.bhmcgq5;
	}
	
	public void setBhmcgq5(java.lang.String value) {
		this.bhmcgq5 = value;
	}
	public java.lang.String getZdzgq1() {
		return this.zdzgq1;
	}
	
	public void setZdzgq1(java.lang.String value) {
		this.zdzgq1 = value;
	}
	public java.lang.String getZdzgq2() {
		return this.zdzgq2;
	}
	
	public void setZdzgq2(java.lang.String value) {
		this.zdzgq2 = value;
	}
	public java.lang.String getZdzgq3() {
		return this.zdzgq3;
	}
	
	public void setZdzgq3(java.lang.String value) {
		this.zdzgq3 = value;
	}
	public java.lang.String getZdzgq4() {
		return this.zdzgq4;
	}
	
	public void setZdzgq4(java.lang.String value) {
		this.zdzgq4 = value;
	}
	public java.lang.String getZdzgq5() {
		return this.zdzgq5;
	}
	
	public void setZdzgq5(java.lang.String value) {
		this.zdzgq5 = value;
	}
	public java.lang.String getBhmcgh1() {
		return this.bhmcgh1;
	}
	
	public void setBhmcgh1(java.lang.String value) {
		this.bhmcgh1 = value;
	}
	public java.lang.String getBhmcgh2() {
		return this.bhmcgh2;
	}
	
	public void setBhmcgh2(java.lang.String value) {
		this.bhmcgh2 = value;
	}
	public java.lang.String getBhmcgh3() {
		return this.bhmcgh3;
	}
	
	public void setBhmcgh3(java.lang.String value) {
		this.bhmcgh3 = value;
	}
	public java.lang.String getBhmcgh4() {
		return this.bhmcgh4;
	}
	
	public void setBhmcgh4(java.lang.String value) {
		this.bhmcgh4 = value;
	}
	public java.lang.String getBhmcgh5() {
		return this.bhmcgh5;
	}
	
	public void setBhmcgh5(java.lang.String value) {
		this.bhmcgh5 = value;
	}
	public java.lang.String getZdzgh1() {
		return this.zdzgh1;
	}
	
	public void setZdzgh1(java.lang.String value) {
		this.zdzgh1 = value;
	}
	public java.lang.String getZdzgh2() {
		return this.zdzgh2;
	}
	
	public void setZdzgh2(java.lang.String value) {
		this.zdzgh2 = value;
	}
	public java.lang.String getZdzgh3() {
		return this.zdzgh3;
	}
	
	public void setZdzgh3(java.lang.String value) {
		this.zdzgh3 = value;
	}
	public java.lang.String getZdzgh4() {
		return this.zdzgh4;
	}
	
	public void setZdzgh4(java.lang.String value) {
		this.zdzgh4 = value;
	}
	public java.lang.String getZdzgh5() {
		return this.zdzgh5;
	}
	
	public void setZdzgh5(java.lang.String value) {
		this.zdzgh5 = value;
	}
	public java.lang.String getGgyy() {
		return this.ggyy;
	}
	
	public void setGgyy(java.lang.String value) {
		this.ggyy = value;
	}
	public java.lang.String getGgflr() {
		return this.ggflr;
	}
	
	public void setGgflr(java.lang.String value) {
		this.ggflr = value;
	}
	public java.lang.String getGgzhr() {
		return this.ggzhr;
	}
	
	public void setGgzhr(java.lang.String value) {
		this.ggzhr = value;
	}
	public java.lang.String getGgyhjc() {
		return this.ggyhjc;
	}
	
	public void setGgyhjc(java.lang.String value) {
		this.ggyhjc = value;
	}
	public java.lang.String getBhmchfq1() {
		return this.bhmchfq1;
	}
	
	public void setBhmchfq1(java.lang.String value) {
		this.bhmchfq1 = value;
	}
	public java.lang.String getBhmchfq2() {
		return this.bhmchfq2;
	}
	
	public void setBhmchfq2(java.lang.String value) {
		this.bhmchfq2 = value;
	}
	public java.lang.String getBhmchfq3() {
		return this.bhmchfq3;
	}
	
	public void setBhmchfq3(java.lang.String value) {
		this.bhmchfq3 = value;
	}
	public java.lang.String getBhmchfq4() {
		return this.bhmchfq4;
	}
	
	public void setBhmchfq4(java.lang.String value) {
		this.bhmchfq4 = value;
	}
	public java.lang.String getBhmchfq5() {
		return this.bhmchfq5;
	}
	
	public void setBhmchfq5(java.lang.String value) {
		this.bhmchfq5 = value;
	}
	public java.lang.String getZdzhfq1() {
		return this.zdzhfq1;
	}
	
	public void setZdzhfq1(java.lang.String value) {
		this.zdzhfq1 = value;
	}
	public java.lang.String getZdzhfq2() {
		return this.zdzhfq2;
	}
	
	public void setZdzhfq2(java.lang.String value) {
		this.zdzhfq2 = value;
	}
	public java.lang.String getZdzhfq3() {
		return this.zdzhfq3;
	}
	
	public void setZdzhfq3(java.lang.String value) {
		this.zdzhfq3 = value;
	}
	public java.lang.String getZdzhfq4() {
		return this.zdzhfq4;
	}
	
	public void setZdzhfq4(java.lang.String value) {
		this.zdzhfq4 = value;
	}
	public java.lang.String getZdzhfq5() {
		return this.zdzhfq5;
	}
	
	public void setZdzhfq5(java.lang.String value) {
		this.zdzhfq5 = value;
	}
	public java.lang.String getBhmchfh1() {
		return this.bhmchfh1;
	}
	
	public void setBhmchfh1(java.lang.String value) {
		this.bhmchfh1 = value;
	}
	public java.lang.String getBhmchfh2() {
		return this.bhmchfh2;
	}
	
	public void setBhmchfh2(java.lang.String value) {
		this.bhmchfh2 = value;
	}
	public java.lang.String getBhmchfh3() {
		return this.bhmchfh3;
	}
	
	public void setBhmchfh3(java.lang.String value) {
		this.bhmchfh3 = value;
	}
	public java.lang.String getBhmchfh4() {
		return this.bhmchfh4;
	}
	
	public void setBhmchfh4(java.lang.String value) {
		this.bhmchfh4 = value;
	}
	public java.lang.String getBhmchfh5() {
		return this.bhmchfh5;
	}
	
	public void setBhmchfh5(java.lang.String value) {
		this.bhmchfh5 = value;
	}
	public java.lang.String getZdzhfh1() {
		return this.zdzhfh1;
	}
	
	public void setZdzhfh1(java.lang.String value) {
		this.zdzhfh1 = value;
	}
	public java.lang.String getZdzhfh2() {
		return this.zdzhfh2;
	}
	
	public void setZdzhfh2(java.lang.String value) {
		this.zdzhfh2 = value;
	}
	public java.lang.String getZdzhfh3() {
		return this.zdzhfh3;
	}
	
	public void setZdzhfh3(java.lang.String value) {
		this.zdzhfh3 = value;
	}
	public java.lang.String getZdzhfh4() {
		return this.zdzhfh4;
	}
	
	public void setZdzhfh4(java.lang.String value) {
		this.zdzhfh4 = value;
	}
	public java.lang.String getZdzhfh5() {
		return this.zdzhfh5;
	}
	
	public void setZdzhfh5(java.lang.String value) {
		this.zdzhfh5 = value;
	}
	public java.lang.String getHfyy() {
		return this.hfyy;
	}
	
	public void setHfyy(java.lang.String value) {
		this.hfyy = value;
	}
	public java.lang.String getHfflr() {
		return this.hfflr;
	}
	
	public void setHfflr(java.lang.String value) {
		this.hfflr = value;
	}
	public java.lang.String getHfzhr() {
		return this.hfzhr;
	}
	
	public void setHfzhr(java.lang.String value) {
		this.hfzhr = value;
	}
	public java.lang.String getHfyhjc() {
		return this.hfyhjc;
	}
	
	public void setHfyhjc(java.lang.String value) {
		this.hfyhjc = value;
	}
	public java.lang.String getBz() {
		return this.bz;
	}
	
	public void setBz(java.lang.String value) {
		this.bz = value;
	}
	public java.util.Date getHfsj() {
		return this.hfsj;
	}
	
	public void setHfsj(java.util.Date value) {
		this.hfsj = value;
	}
	
	
}

