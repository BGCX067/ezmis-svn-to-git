package com.jteap.jhtj.jkbldy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
/**
 * 接口变量定义
 */
@Entity
@Table(name="tj_interface")
public class TjInterFace{
	public static final String NIAN="NIAN";
	public static final String SNIAN="SNIAN";
	public static final String ENIAN="ENIAN";
	public static final String YUE="YUE";
	public static final String SYUE="SYUE";
	public static final String EYUE="EYUE";
	public static final String RI="RI";
	public static final String SRI="SRI";
	public static final String ERI="ERI";
	public static final String SRQ="SRQ";
	public static final String ERQ="ERQ";
	public static final String JZ="JZ";
	public static final String DESC="desc";
	public static final String ASC="asc";
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="vname")
	private String vname;
	@Column(name="cvname")
	private String cvname;
	@Column(name="vtype")
	private String vtype;//1为字符串,2为整形,3为日期
	@Column(name="vorder")
	private Long vorder;
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getCvname() {
		return cvname;
	}
	public void setCvname(String cvname) {
		this.cvname = cvname;
	}
	public String getVtype() {
		return vtype;
	}
	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
	public Long getVorder() {
		return vorder;
	}
	public void setVorder(Long vorder) {
		this.vorder = vorder;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}