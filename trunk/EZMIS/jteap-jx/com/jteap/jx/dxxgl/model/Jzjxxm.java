package com.jteap.jx.dxxgl.model;

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

/**
 * 机组检修项目表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_JZJXXM")
public class Jzjxxm {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 所属专业
	@Column(name = "sszy")
	private String sszy;

	// 项目名称
	@Column(name = "xmmc")
	private String xmmc;

	// 记录人
	@Column(name = "jlr")
	private String jlr;

	// 记录时间
	@Column(name = "jlsj")
	private Date jlsj;

	// 备注
	@Column(name = "remark")
	private String remark;

	// 机组检修计划
	@ManyToOne
	@JoinColumn(name="JZJXJH_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private Jzjxjh jzjxjh;

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

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Jzjxjh getJzjxjh() {
		return jzjxjh;
	}

	public void setJzjxjh(Jzjxjh jzjxjh) {
		this.jzjxjh = jzjxjh;
	}

}
