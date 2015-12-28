package com.jteap.gcht.gcxmgl.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * TbHtYsd entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_HT_YSD", schema = "EZMIS")
public class Ysd implements java.io.Serializable {

	// Fields

	private String id;
	private Date cjsj;
	private String status;
	private String gcnr;
	private String zy;
	private String jz;
	private String gcxmbh;
	private String gcxmmc;
	private String kgqzId;
	private String sgdw;
	private String kgsj;
	private String jgsj;
	private String ysnf;
	private String bz;
	private String zlbz;
	private String xcqlqk;
	private String zlyjqk;
	private String jsr;
	private String sgdwjxfzr;
	private String sgdwzjbYj;
	private String sgdwzjbQm;
	private String sgdwzjbQmsj;
	private String sbgxbzYj;
	private String sbgxbzQm;
	private String sbgxbzQmsj;
	private String sbgxbmYj;
	private String sbgxbmQm;
	private String sbgxbmQmsj;
	private String scjsbYj;
	private String scjsbQm;
	private String scjsbQmsj;
	private String zgcsyj;
	private String zgcs;
	private String zgcsspsj;
	private String sgdwfzrqz;
	private String sqbm;
	private String qtxgbm;
	private String qtxgbmqz;
	private String qtxgbmqzsj;
	private String xmyj;
	private String iszf;
	private String sqr;

	// Constructors

	/** default constructor */
	public Ysd() {
	}

	/** minimal constructor */
	public Ysd(String xmyj) {
		this.xmyj = xmyj;
	}

	/** full constructor */
	public Ysd(Date cjsj, String status, String gcnr, String zy, String jz,
			String gcxmbh, String gcxmmc, String kgqzId, String sgdw,
			String kgsj, String jgsj, String ysnf, String bz, String zlbz,
			String xcqlqk, String zlyjqk, String jsr, String sgdwjxfzr,
			String sgdwzjbYj, String sgdwzjbQm, String sgdwzjbQmsj,
			String sbgxbzYj, String sbgxbzQm, String sbgxbzQmsj,
			String sbgxbmYj, String sbgxbmQm, String sbgxbmQmsj,
			String scjsbYj, String scjsbQm, String scjsbQmsj, String zgcsyj,
			String zgcs, String zgcsspsj, String sgdwfzrqz, String sqbm,
			String qtxgbm, String qtxgbmqz, String qtxgbmqzsj, String xmyj,
			String iszf,String sqr) {
		this.cjsj = cjsj;
		this.status = status;
		this.gcnr = gcnr;
		this.zy = zy;
		this.jz = jz;
		this.gcxmbh = gcxmbh;
		this.gcxmmc = gcxmmc;
		this.kgqzId = kgqzId;
		this.sgdw = sgdw;
		this.kgsj = kgsj;
		this.jgsj = jgsj;
		this.ysnf = ysnf;
		this.bz = bz;
		this.zlbz = zlbz;
		this.xcqlqk = xcqlqk;
		this.zlyjqk = zlyjqk;
		this.jsr = jsr;
		this.sgdwjxfzr = sgdwjxfzr;
		this.sgdwzjbYj = sgdwzjbYj;
		this.sgdwzjbQm = sgdwzjbQm;
		this.sgdwzjbQmsj = sgdwzjbQmsj;
		this.sbgxbzYj = sbgxbzYj;
		this.sbgxbzQm = sbgxbzQm;
		this.sbgxbzQmsj = sbgxbzQmsj;
		this.sbgxbmYj = sbgxbmYj;
		this.sbgxbmQm = sbgxbmQm;
		this.sbgxbmQmsj = sbgxbmQmsj;
		this.scjsbYj = scjsbYj;
		this.scjsbQm = scjsbQm;
		this.scjsbQmsj = scjsbQmsj;
		this.zgcsyj = zgcsyj;
		this.zgcs = zgcs;
		this.zgcsspsj = zgcsspsj;
		this.sgdwfzrqz = sgdwfzrqz;
		this.sqbm = sqbm;
		this.qtxgbm = qtxgbm;
		this.qtxgbmqz = qtxgbmqz;
		this.qtxgbmqzsj = qtxgbmqzsj;
		this.xmyj = xmyj;
		this.iszf = iszf;
		this.sqr = sqr;
	}

	// Property accessors
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Id
	@GeneratedValue(generator="system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CJSJ", length = 11)
	public Date getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	@Column(name = "STATUS", length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "GCNR", length = 4000)
	public String getGcnr() {
		return this.gcnr;
	}

	public void setGcnr(String gcnr) {
		this.gcnr = gcnr;
	}

	@Column(name = "ZY", length = 50)
	public String getZy() {
		return this.zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	@Column(name = "JZ", length = 50)
	public String getJz() {
		return this.jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	@Column(name = "GCXMBH", length = 50)
	public String getGcxmbh() {
		return this.gcxmbh;
	}

	public void setGcxmbh(String gcxmbh) {
		this.gcxmbh = gcxmbh;
	}

	@Column(name = "GCXMMC", length = 50)
	public String getGcxmmc() {
		return this.gcxmmc;
	}

	public void setGcxmmc(String gcxmmc) {
		this.gcxmmc = gcxmmc;
	}

	@Column(name = "KGQZ_ID", length = 50)
	public String getKgqzId() {
		return this.kgqzId;
	}

	public void setKgqzId(String kgqzId) {
		this.kgqzId = kgqzId;
	}

	@Column(name = "SGDW", length = 100)
	public String getSgdw() {
		return this.sgdw;
	}

	public void setSgdw(String sgdw) {
		this.sgdw = sgdw;
	}

	@Column(name = "KGSJ", length = 50)
	public String getKgsj() {
		return this.kgsj;
	}

	public void setKgsj(String kgsj) {
		this.kgsj = kgsj;
	}

	@Column(name = "JGSJ", length = 50)
	public String getJgsj() {
		return this.jgsj;
	}

	public void setJgsj(String jgsj) {
		this.jgsj = jgsj;
	}

	@Column(name = "YSNF", length = 50)
	public String getYsnf() {
		return this.ysnf;
	}

	public void setYsnf(String ysnf) {
		this.ysnf = ysnf;
	}

	@Column(name = "BZ", length = 500)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "ZLBZ", length = 4000)
	public String getZlbz() {
		return this.zlbz;
	}

	public void setZlbz(String zlbz) {
		this.zlbz = zlbz;
	}

	@Column(name = "XCQLQK", length = 500)
	public String getXcqlqk() {
		return this.xcqlqk;
	}

	public void setXcqlqk(String xcqlqk) {
		this.xcqlqk = xcqlqk;
	}

	@Column(name = "ZLYJQK", length = 500)
	public String getZlyjqk() {
		return this.zlyjqk;
	}

	public void setZlyjqk(String zlyjqk) {
		this.zlyjqk = zlyjqk;
	}

	@Column(name = "JSR", length = 50)
	public String getJsr() {
		return this.jsr;
	}

	public void setJsr(String jsr) {
		this.jsr = jsr;
	}

	@Column(name = "SGDWJXFZR", length = 50)
	public String getSgdwjxfzr() {
		return this.sgdwjxfzr;
	}

	public void setSgdwjxfzr(String sgdwjxfzr) {
		this.sgdwjxfzr = sgdwjxfzr;
	}

	@Column(name = "SGDWZJB_YJ", length = 500)
	public String getSgdwzjbYj() {
		return this.sgdwzjbYj;
	}

	public void setSgdwzjbYj(String sgdwzjbYj) {
		this.sgdwzjbYj = sgdwzjbYj;
	}

	@Column(name = "SGDWZJB_QM", length = 50)
	public String getSgdwzjbQm() {
		return this.sgdwzjbQm;
	}

	public void setSgdwzjbQm(String sgdwzjbQm) {
		this.sgdwzjbQm = sgdwzjbQm;
	}

	@Column(name = "SGDWZJB_QMSJ", length = 50)
	public String getSgdwzjbQmsj() {
		return this.sgdwzjbQmsj;
	}

	public void setSgdwzjbQmsj(String sgdwzjbQmsj) {
		this.sgdwzjbQmsj = sgdwzjbQmsj;
	}

	@Column(name = "SBGXBZ_YJ", length = 500)
	public String getSbgxbzYj() {
		return this.sbgxbzYj;
	}

	public void setSbgxbzYj(String sbgxbzYj) {
		this.sbgxbzYj = sbgxbzYj;
	}

	@Column(name = "SBGXBZ_QM", length = 50)
	public String getSbgxbzQm() {
		return this.sbgxbzQm;
	}

	public void setSbgxbzQm(String sbgxbzQm) {
		this.sbgxbzQm = sbgxbzQm;
	}

	@Column(name = "SBGXBZ_QMSJ", length = 50)
	public String getSbgxbzQmsj() {
		return this.sbgxbzQmsj;
	}

	public void setSbgxbzQmsj(String sbgxbzQmsj) {
		this.sbgxbzQmsj = sbgxbzQmsj;
	}

	@Column(name = "SBGXBM_YJ", length = 500)
	public String getSbgxbmYj() {
		return this.sbgxbmYj;
	}

	public void setSbgxbmYj(String sbgxbmYj) {
		this.sbgxbmYj = sbgxbmYj;
	}

	@Column(name = "SBGXBM_QM", length = 50)
	public String getSbgxbmQm() {
		return this.sbgxbmQm;
	}

	public void setSbgxbmQm(String sbgxbmQm) {
		this.sbgxbmQm = sbgxbmQm;
	}

	@Column(name = "SBGXBM_QMSJ", length = 50)
	public String getSbgxbmQmsj() {
		return this.sbgxbmQmsj;
	}

	public void setSbgxbmQmsj(String sbgxbmQmsj) {
		this.sbgxbmQmsj = sbgxbmQmsj;
	}

	@Column(name = "SCJSB_YJ", length = 500)
	public String getScjsbYj() {
		return this.scjsbYj;
	}

	public void setScjsbYj(String scjsbYj) {
		this.scjsbYj = scjsbYj;
	}

	@Column(name = "SCJSB_QM", length = 50)
	public String getScjsbQm() {
		return this.scjsbQm;
	}

	public void setScjsbQm(String scjsbQm) {
		this.scjsbQm = scjsbQm;
	}

	@Column(name = "SCJSB_QMSJ", length = 50)
	public String getScjsbQmsj() {
		return this.scjsbQmsj;
	}

	public void setScjsbQmsj(String scjsbQmsj) {
		this.scjsbQmsj = scjsbQmsj;
	}

	@Column(name = "ZGCSYJ", length = 500)
	public String getZgcsyj() {
		return this.zgcsyj;
	}

	public void setZgcsyj(String zgcsyj) {
		this.zgcsyj = zgcsyj;
	}

	@Column(name = "ZGCS", length = 50)
	public String getZgcs() {
		return this.zgcs;
	}

	public void setZgcs(String zgcs) {
		this.zgcs = zgcs;
	}

	@Column(name = "ZGCSSPSJ", length = 50)
	public String getZgcsspsj() {
		return this.zgcsspsj;
	}

	public void setZgcsspsj(String zgcsspsj) {
		this.zgcsspsj = zgcsspsj;
	}

	@Column(name = "SGDWFZRQZ", length = 50)
	public String getSgdwfzrqz() {
		return this.sgdwfzrqz;
	}

	public void setSgdwfzrqz(String sgdwfzrqz) {
		this.sgdwfzrqz = sgdwfzrqz;
	}

	@Column(name = "SQBM", length = 50)
	public String getSqbm() {
		return this.sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	@Column(name = "QTXGBM", length = 50)
	public String getQtxgbm() {
		return this.qtxgbm;
	}

	public void setQtxgbm(String qtxgbm) {
		this.qtxgbm = qtxgbm;
	}

	@Column(name = "QTXGBMQZ", length = 50)
	public String getQtxgbmqz() {
		return this.qtxgbmqz;
	}

	public void setQtxgbmqz(String qtxgbmqz) {
		this.qtxgbmqz = qtxgbmqz;
	}

	@Column(name = "QTXGBMQZSJ", length = 50)
	public String getQtxgbmqzsj() {
		return this.qtxgbmqzsj;
	}

	public void setQtxgbmqzsj(String qtxgbmqzsj) {
		this.qtxgbmqzsj = qtxgbmqzsj;
	}

	@Column(name = "XMYJ", nullable = false, length = 50)
	public String getXmyj() {
		return this.xmyj;
	}

	public void setXmyj(String xmyj) {
		this.xmyj = xmyj;
	}

	@Column(name = "ISZF", length = 50)
	public String getIszf() {
		return this.iszf;
	}

	public void setIszf(String iszf) {
		this.iszf = iszf;
	}
	
	@Column(name = "SQR", length = 50)
	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	


}