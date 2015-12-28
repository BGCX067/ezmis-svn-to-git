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
 * 公用系统检修计划-记录表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_GYXTJXJH_JL")
public class GyxtjxjhJl {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 设备ID
	@Column(name = "SBID")
	private String sbid;

	@Column(name = "JHNF")
	private String jhnf;

	// 记录月份
	@Column(name = "JLYF")
	private String jlyf;

	// 记录人
	@Column(name = "JLR")
	private String jlr;

	// 记录时间
	@Column(name = "JLSJ")
	private Date jlsj;

	// 记录内容
	@Column(name = "JLNR")
	private String jlnr;

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

	public String getJlyf() {
		return jlyf;
	}

	public void setJlyf(String jlyf) {
		this.jlyf = jlyf;
	}

	public String getJlr() {
		return jlr;
	}

	public void setJlr(String jlr) {
		this.jlr = jlr;
	}

	public Date getJlsj() {
		return jlsj;
	}

	public void setJlsj(Date jlsj) {
		this.jlsj = jlsj;
	}

	public String getJlnr() {
		return jlnr;
	}

	public void setJlnr(String jlnr) {
		this.jlnr = jlnr;
	}

	public String getJhnf() {
		return jhnf;
	}

	public void setJhnf(String jhnf) {
		this.jhnf = jhnf;
	}

}
