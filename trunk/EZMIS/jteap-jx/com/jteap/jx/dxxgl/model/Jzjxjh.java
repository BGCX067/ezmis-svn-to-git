package com.jteap.jx.dxxgl.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

/**
 * 机组检修计划表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_JZJXJH")
public class Jzjxjh {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 计划名称
	@Column(name = "JHMC")
	private String jhmc;

	// 检修性质
	@Column(name = "jxxz")
	private String jxxz;

	// 机组
	@Column(name = "jz")
	private String jz;

	// 起始日期
	@Column(name = "qsrq")
	private Date qsrq;

	// 结束时间
	@Column(name = "jsrq")
	private Date jsrq;

	// 人工费用
	@Column(name = "rgfy")
	private Double rgfy;

	// 材料费用
	@Column(name = "clfy")
	private Double clfy;

	// 费用合计
	@Column(name = "fyhj")
	private Double fyhj;

	// 内容概要
	@Column(name = "NRGY")
	private String nrgy;

	// 检修任务书
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "JXRWS", columnDefinition = "BLOB", nullable = true)
	private byte[] jxrws;

	// 检修任务书名称
	@Column(name = "JXRWS_MC")
	private String jxrwsMc;

	// 检修项目
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "JXXM", columnDefinition = "BLOB", nullable = true)
	private byte[] jxxm;

	// 检修项目名称
	@Column(name = "JXXM_MC")
	private String jxxmMc;

	// 检修技术协议
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "JXJSXY", columnDefinition = "BLOB", nullable = true)
	private byte[] jxjsxy;

	// 检修技术协议名称
	@Column(name = "JXJSXY_MC")
	private String jxjsxyMc;

	// 其他附件1
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "qtfj1", columnDefinition = "BLOB", nullable = true)
	private byte[] qtfj1;

	// 其他附件1名称
	@Column(name = "qtfj_mc1")
	private String qtfjMc1;

	// 其他附件2
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "qtfj2", columnDefinition = "BLOB", nullable = true)
	private byte[] qtfj2;

	// 其他附件2名称
	@Column(name = "qtfj_mc2")
	private String qtfjMc2;

	// 机组检修项目
	@OneToMany(mappedBy = "jzjxjh")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Jzjxxm> jzjxxms;

	public Jzjxjh() {
	}

	public Jzjxjh(String id, String jhmc, String jxxz, String jz, Date qsrq, Date jsrq, Double rgfy, Double clfy,
			String nrgy) {
		this.id = id;
		this.jhmc = jhmc;
		this.jxxz = jxxz;
		this.jz = jz;
		this.qsrq = qsrq;
		this.jsrq = jsrq;
		this.rgfy = rgfy;
		this.clfy = clfy;
		this.nrgy = nrgy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJhmc() {
		return jhmc;
	}

	public void setJhmc(String jhmc) {
		this.jhmc = jhmc;
	}

	public String getJxxz() {
		return jxxz;
	}

	public void setJxxz(String jxxz) {
		this.jxxz = jxxz;
	}

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
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

	public Double getRgfy() {
		return rgfy;
	}

	public void setRgfy(Double rgfy) {
		this.rgfy = rgfy;
	}

	public Double getClfy() {
		return clfy;
	}

	public void setClfy(Double clfy) {
		this.clfy = clfy;
	}

	public Double getFyhj() {
		return fyhj;
	}

	public void setFyhj(Double fyhj) {
		this.fyhj = fyhj;
	}

	public String getNrgy() {
		return nrgy;
	}

	public void setNrgy(String nrgy) {
		this.nrgy = nrgy;
	}

	public byte[] getJxrws() {
		return jxrws;
	}

	public void setJxrws(byte[] jxrws) {
		this.jxrws = jxrws;
	}

	public byte[] getJxxm() {
		return jxxm;
	}

	public void setJxxm(byte[] jxxm) {
		this.jxxm = jxxm;
	}

	public byte[] getJxjsxy() {
		return jxjsxy;
	}

	public void setJxjsxy(byte[] jxjsxy) {
		this.jxjsxy = jxjsxy;
	}

	public String getJxrwsMc() {
		return jxrwsMc;
	}

	public void setJxrwsMc(String jxrwsMc) {
		this.jxrwsMc = jxrwsMc;
	}

	public String getJxxmMc() {
		return jxxmMc;
	}

	public void setJxxmMc(String jxxmMc) {
		this.jxxmMc = jxxmMc;
	}

	public String getJxjsxyMc() {
		return jxjsxyMc;
	}

	public void setJxjsxyMc(String jxjsxyMc) {
		this.jxjsxyMc = jxjsxyMc;
	}

	public byte[] getQtfj1() {
		return qtfj1;
	}

	public void setQtfj1(byte[] qtfj1) {
		this.qtfj1 = qtfj1;
	}

	public String getQtfjMc1() {
		return qtfjMc1;
	}

	public void setQtfjMc1(String qtfjMc1) {
		this.qtfjMc1 = qtfjMc1;
	}

	public byte[] getQtfj2() {
		return qtfj2;
	}

	public void setQtfj2(byte[] qtfj2) {
		this.qtfj2 = qtfj2;
	}

	public String getQtfjMc2() {
		return qtfjMc2;
	}

	public void setQtfjMc2(String qtfjMc2) {
		this.qtfjMc2 = qtfjMc2;
	}

	public Set<Jzjxxm> getJzjxxms() {
		return jzjxxms;
	}

	public void setJzjxxms(Set<Jzjxxm> jzjxxms) {
		this.jzjxxms = jzjxxms;
	}

}
