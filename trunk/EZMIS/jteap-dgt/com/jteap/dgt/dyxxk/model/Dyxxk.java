package com.jteap.dgt.dyxxk.model;

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

import com.jteap.dgt.dzz.model.Dzz;

/**
 * 团员信息实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_DGT_DYXXK")
@SuppressWarnings("unchecked")
public class Dyxxk {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="DANGZU",insertable=false,updatable=false)
	@LazyToOne(LazyToOneOption.PROXY)
	private Dzz dzz;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="SEX")
	private String sex;
	
	@Column(name="MINZU")
	private String minzu;
	
	@Column(name="XUELI")
	private String xueli;
	
	@Column(name="BIRTHDAY")
	private Date birthday;
	
	@Column(name="PHOTO")
	private byte[] photo;
	
	@Column(name="BUMEN")
	private String bumen;
	
	@Column(name="JOIN_DATE")
	private Date join_date;
	
	@Column(name="DANGZU")
	private String dangzu;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="SORTNO")
	private Integer sortno;

	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getXueli() {
		return xueli;
	}

	public void setXueli(String xueli) {
		this.xueli = xueli;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getBumen() {
		return bumen;
	}

	public void setBumen(String bumen) {
		this.bumen = bumen;
	}

	public Date getJoin_date() {
		return join_date;
	}

	public void setJoin_date(Date join_date) {
		this.join_date = join_date;
	}

	public String getDangzu() {
		return dangzu;
	}

	public void setDangzu(String dangzu) {
		this.dangzu = dangzu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public Dzz getDzz() {
		return dzz;
	}

	public void setDzz(Dzz dzz) {
		this.dzz = dzz;
	}


}
