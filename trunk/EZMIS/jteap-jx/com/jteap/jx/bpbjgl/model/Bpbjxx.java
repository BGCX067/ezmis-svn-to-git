package com.jteap.jx.bpbjgl.model;

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
 * 备品备件信息
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_BPBJGL_BPBJXX")
public class Bpbjxx {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 专业分类
	@ManyToOne
	@JoinColumn(name = "ZYFLID")
	@LazyToOne(LazyToOneOption.PROXY)
	private BpbjZyfl bpbjZyfl;

	// 设备编码
	@Column(name = "SBBM")
	private String sbbm;

	// 设备名称
	@Column(name = "sbmc")
	private String sbmc;

	// 形式及规格
	@Column(name = "xsjgg")
	private String xsjgg;

	// 单位
	@Column(name = "dw")
	private String dw;

	// 额定数量
	@Column(name = "edsl")
	private Long edsl;

	// 实际数量
	@Column(name = "sjsl")
	private Long sjsl;

	// 预警数量
	@Column(name = "yjsl")
	private Long yjsl;

	// 备注
	@Column(name = "remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BpbjZyfl getBpbjZyfl() {
		return bpbjZyfl;
	}

	public void setBpbjZyfl(BpbjZyfl bpbjZyfl) {
		this.bpbjZyfl = bpbjZyfl;
	}

	public String getSbbm() {
		return sbbm;
	}

	public void setSbbm(String sbbm) {
		this.sbbm = sbbm;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getXsjgg() {
		return xsjgg;
	}

	public void setXsjgg(String xsjgg) {
		this.xsjgg = xsjgg;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public Long getEdsl() {
		return edsl;
	}

	public void setEdsl(Long edsl) {
		this.edsl = edsl;
	}

	public Long getSjsl() {
		return sjsl;
	}

	public void setSjsl(Long sjsl) {
		this.sjsl = sjsl;
	}

	public Long getYjsl() {
		return yjsl;
	}

	public void setYjsl(Long yjsl) {
		this.yjsl = yjsl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
