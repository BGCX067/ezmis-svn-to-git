package com.jteap.dgt.fjdgztj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
/**
 * 活动剪影实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_FORM_JYFC")
@SuppressWarnings("unchecked")
public class Hdjy {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="BT")
	private String BT;
	
	@Column(name="BTTP")
	private String BTTP;
	
	@Column(name="HDNR")
	private String HDNR;
	
	@Column(name="HDFJ")
	private String HDFJ;

	@Column(name="SCR")
	private String SCR;
	
	@JoinColumn(name="SCSJ")
	private String SCSJ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBT() {
		return BT;
	}

	public void setBT(String bt) {
		BT = bt;
	}

	public String getBTTP() {
		return BTTP;
	}

	public void setBTTP(String bttp) {
		BTTP = bttp;
	}

	public String getHDNR() {
		return HDNR;
	}

	public void setHDNR(String hdnr) {
		HDNR = hdnr;
	}

	public String getHDFJ() {
		return HDFJ;
	}

	public void setHDFJ(String hdfj) {
		HDFJ = hdfj;
	}

	public String getSCR() {
		return SCR;
	}

	public void setSCR(String scr) {
		SCR = scr;
	}

	public String getSCSJ() {
		return SCSJ;
	}

	public void setSCSJ(String scsj) {
		SCSJ = scsj;
	}

	//返回标题
	public String getTitle(){
		return this.BT;
	}
    //返回活动内容
	public String getContent(){
		return this.HDNR;
	}
	public String getCreateDtStr() {
		return this.SCSJ.replace("年", "-").replace("月","-").replace("日","");
	}
}
