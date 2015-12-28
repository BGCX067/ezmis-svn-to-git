package com.jteap.jx.dxxgl.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 检修总结报告表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_JXZJBG")
public class Jxzjbg {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 报告名称
	@Column(name = "BGMC")
	private String bgmc;

	// 检修性质
	@Column(name = "JXXZ")
	private String jxxz;

	// 所属机组
	@Column(name = "SSJZ")
	private String ssjz;

	// 负责人员
	@Column(name = "FZRY")
	private String fzry;

	// 起始日期
	@Column(name = "QSRQ")
	private Date qsrq;

	// 终止日期
	@Column(name = "JSRQ")
	private Date jsrq;

	// 报告摘要
	@Column(name = "BGZY")
	private String bgzy;

	// 验收意见
	@Column(name = "YSYJ")
	private String ysyj;

	// 验收日期
	@Column(name = "YSRQ")
	private Date ysrq;

	// 验收部门
	@Column(name = "YSBM")
	private String ysbm;

	// 存在问题
	@Column(name = "CZWT")
	private String czwt;

	// 备注说明
	@Column(name = "BZSM")
	private String bzsm;

	// 报告附件
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BGFJ", columnDefinition = "BLOB", nullable = true)
	private byte[] bgfj;

	// 报告附件名称
	@Column(name = "BGFJ_MC")
	private String bgfjMc;

	// 检修计划
	@Column(name = "JXJH_ID")
	private String jxjhId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBgmc() {
		return bgmc;
	}

	public void setBgmc(String bgmc) {
		this.bgmc = bgmc;
	}

	public String getJxxz() {
		return jxxz;
	}

	public void setJxxz(String jxxz) {
		this.jxxz = jxxz;
	}

	public String getSsjz() {
		return ssjz;
	}

	public void setSsjz(String ssjz) {
		this.ssjz = ssjz;
	}

	public String getFzry() {
		return fzry;
	}

	public void setFzry(String fzry) {
		this.fzry = fzry;
	}

	public Date getQsrq() {
		return qsrq;
	}

	public void setQsrq(Date qsrq) {
		this.qsrq = qsrq;
	}

	public Date getJsrq() {
		return jsrq;
	}

	public void setJsrq(Date jsrq) {
		this.jsrq = jsrq;
	}

	public String getBgzy() {
		return bgzy;
	}

	public void setBgzy(String bgzy) {
		this.bgzy = bgzy;
	}

	public String getYsyj() {
		return ysyj;
	}

	public void setYsyj(String ysyj) {
		this.ysyj = ysyj;
	}

	public Date getYsrq() {
		return ysrq;
	}

	public void setYsrq(Date ysrq) {
		this.ysrq = ysrq;
	}

	public String getYsbm() {
		return ysbm;
	}

	public void setYsbm(String ysbm) {
		this.ysbm = ysbm;
	}

	public String getCzwt() {
		return czwt;
	}

	public void setCzwt(String czwt) {
		this.czwt = czwt;
	}

	public String getBzsm() {
		return bzsm;
	}

	public void setBzsm(String bzsm) {
		this.bzsm = bzsm;
	}

	public byte[] getBgfj() {
		return bgfj;
	}

	public void setBgfj(byte[] bgfj) {
		this.bgfj = bgfj;
	}

	public String getBgfjMc() {
		return bgfjMc;
	}

	public void setBgfjMc(String bgfjMc) {
		this.bgfjMc = bgfjMc;
	}

	public String getJxjhId() {
		return jxjhId;
	}

	public void setJxjhId(String jxjhId) {
		this.jxjhId = jxjhId;
	}

}
