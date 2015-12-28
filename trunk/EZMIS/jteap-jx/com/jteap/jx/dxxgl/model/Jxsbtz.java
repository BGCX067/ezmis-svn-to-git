package com.jteap.jx.dxxgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 检修设备台账表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_SBJCTZ")
public class Jxsbtz {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 所属专业
	@Column(name = "sszy")
	private String sszy;

	// 设备名称
	@Column(name = "sbmc")
	private String sbmc;

	// 检修周期
	@Column(name = "jxzq")
	private String jxzq;

	// 项目级别
	@Column(name = "XMJB")
	private String xmjb;

	// 项目序号
	@Column(name = "xmxh")
	private String xmxh;

	// 排序号
	@Column(name = "sortno")
	private Integer sortNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSszy() {
		return sszy;
	}

	public void setSszy(String sszy) {
		this.sszy = sszy;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getJxzq() {
		return jxzq;
	}

	public void setJxzq(String jxzq) {
		this.jxzq = jxzq;
	}

	public String getXmjb() {
		return xmjb;
	}

	public void setXmjb(String xmjb) {
		this.xmjb = xmjb;
	}

	public String getXmxh() {
		return xmxh;
	}

	public void setXmxh(String xmxh) {
		this.xmxh = xmxh;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}
