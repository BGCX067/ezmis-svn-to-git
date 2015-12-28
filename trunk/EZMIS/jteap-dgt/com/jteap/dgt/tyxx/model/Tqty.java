package com.jteap.dgt.tyxx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 团青推优实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="tb_dgt_tqty")
@SuppressWarnings("unchecked")
public class Tqty {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="SEX")
	private String sex;
	
	@Column(name="MINZU")
	private String minzu;
	
	@Column(name="BUMEN")
	private String bumen;
	
	@Column(name="XUELI")
	private String xueli;
	
	@Column(name="TUANZU")
	private String tuanzu;
	
	@Column(name="SHENGQING")
	private Date shengqing;
	
	@Column(name="TUIYOU_SHIJIAN")
	private Date tuiyou_shijian;
	
	@Column(name="JIJI_FENZI")
	private String jiji_fenzi;
	
	@Column(name="BIRTHDAY")
	private Date birthday;
	
	@Column(name="BUMEN_ZHIWU")
	private String bumen_zhiuwu;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMinzu() {
		return minzu;
	}

	public void setMinzu(String minzu) {
		this.minzu = minzu;
	}

	public String getBumen() {
		return bumen;
	}

	public void setBumen(String bumen) {
		this.bumen = bumen;
	}

	public String getXueli() {
		return xueli;
	}

	public void setXueli(String xueli) {
		this.xueli = xueli;
	}

	public String getTuanzu() {
		return tuanzu;
	}

	public void setTuanzu(String tuanzu) {
		this.tuanzu = tuanzu;
	}

	public Date getShengqing() {
		return shengqing;
	}

	public void setShengqing(Date shengqing) {
		this.shengqing = shengqing;
	}

	public Date getTuiyou_shijian() {
		return tuiyou_shijian;
	}

	public void setTuiyou_shijian(Date tuiyou_shijian) {
		this.tuiyou_shijian = tuiyou_shijian;
	}

	public String getJiji_fenzi() {
		return jiji_fenzi;
	}

	public void setJiji_fenzi(String jiji_fenzi) {
		this.jiji_fenzi = jiji_fenzi;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBumen_zhiuwu() {
		return bumen_zhiuwu;
	}

	public void setBumen_zhiuwu(String bumen_zhiuwu) {
		this.bumen_zhiuwu = bumen_zhiuwu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
