package com.jteap.jx.dxxgl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 
 * 公用系统检修计划-标记表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_GYXTJXJH_BJ")
public class GyxtjxjhBj {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 设备ID
	@Column(name = "SBID")
	private String sbid;

	// 计划年份
	@Column(name = "JHNF")
	private String jhnf;

	// 标记月份
	@Column(name = "BJYF")
	private String bjyf;

	// 标记人
	@Column(name = "BJR")
	private String bjr;

	// 标记时间
	@Column(name = "BJSJ")
	private Date bjsj;

	// 标记颜色
	@Column(name = "BJYS")
	private String bjys;

	// 标记图标
	@Column(name = "BJTB")
	private String bjtb;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSbid() {
		return sbid;
	}

	public void setSbid(String sbid) {
		this.sbid = sbid;
	}

	public String getBjyf() {
		return bjyf;
	}

	public void setBjyf(String bjyf) {
		this.bjyf = bjyf;
	}

	public String getBjr() {
		return bjr;
	}

	public void setBjr(String bjr) {
		this.bjr = bjr;
	}

	public Date getBjsj() {
		return bjsj;
	}

	public void setBjsj(Date bjsj) {
		this.bjsj = bjsj;
	}

	public String getBjys() {
		return bjys;
	}

	public void setBjys(String bjys) {
		this.bjys = bjys;
	}

	public String getBjtb() {
		return bjtb;
	}

	public void setBjtb(String bjtb) {
		this.bjtb = bjtb;
	}

	public String getJhnf() {
		return jhnf;
	}

	public void setJhnf(String jhnf) {
		this.jhnf = jhnf;
	}

}
