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
 * 团员信息实体类
 * @author lvchao
 *
 */


@Entity  
@Table(name="tb_dgt_tyxxk")
@SuppressWarnings("unchecked")
public class Tyxxk {

	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	
	@Column(name="ZHENGZHI")
	private String zhengzhi;
	
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="TUAN_ZHIWU")
	private String tuan_zhiwu;
	
	
	@Column(name="BIRTHDAY")
	private Date birthday;
	
	@Column(name="SORTNO")
	private Integer sortno;

	@Column(name="SEX")
	private String sex;
	
	@Column(name="BUMEN")
	private String bumen;
	
	@Column(name="BUMEN_ZHIWU")
	private String bumen_zhiwu;
	
	@Column(name="NAME")
	private String name;

	@Column(name="TUANZU")
	private String tuanzu;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZhengzhi() {
		return zhengzhi;
	}

	public void setZhengzhi(String zhengzhi) {
		this.zhengzhi = zhengzhi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTuan_zhiwu() {
		return tuan_zhiwu;
	}

	public void setTuan_zhiwu(String tuan_zhiwu) {
		this.tuan_zhiwu = tuan_zhiwu;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBumen() {
		return bumen;
	}

	public void setBumen(String bumen) {
		this.bumen = bumen;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBumen_zhiwu() {
		return bumen_zhiwu;
	}

	public void setBumen_zhiwu(String bumen_zhiwu) {
		this.bumen_zhiwu = bumen_zhiwu;
	}

	public String getTuanzu() {
		return tuanzu;
	}

	public void setTuanzu(String tuanzu) {
		this.tuanzu = tuanzu;
	}
	
}
