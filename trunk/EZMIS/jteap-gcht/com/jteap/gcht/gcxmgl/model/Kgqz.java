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
 * TbHtKgqz entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_HT_KGQZ", schema = "EZMIS")
public class Kgqz implements java.io.Serializable {

	// Fields

	private String id;
	private String lxid;
	private String status;
	private Date cjsj;
	private String gcxmmc;
	private String gcxmbh;
	private String gccbdw;
	private String gcnr;
	private String sfczfjwzhs;
	private String gcsfsjtsgz;
	private Date jhkgrq;
	private String jhgq;
	private String jz;
	private String zy;
	private String kgnf;
	private String cbdwfzr;
	private String cbdwfzrlxfs;
	private String xcsgfzr;
	private String xcsgfzrlxfs;
	private String xmfzbm;
	private String xmfzr;
	private String xmfzbmzgyj;
	private String xmfzbmzg;
	private String sfqdht;
	private String sjbqz;
	private String sfqdaqxy;
	private String ajbqz;
	private String jsfzr;
	private String jsfzrlxfs;
	private String xmfzrlxfs;
	private String sfjbkgtjZykg;
	private String xmssbmzrqz;
	private String bz;
	private String sfphlhjz;
	private String zhswbqz;
	private String sqbm;
	private String jhjybqz;
	private String xmyj;
	private String isys;

	// Constructors

	/** default constructor */
	public Kgqz() {
	}

	/** minimal constructor */
	public Kgqz(String xmyj) {
		this.xmyj = xmyj;
	}

	/** full constructor */
	public Kgqz(String lxid, String status, Date cjsj, String gcxmmc,
			String gcxmbh, String gccbdw, String gcnr, String sfczfjwzhs,
			String gcsfsjtsgz, Date jhkgrq, String jhgq, String jz, String zy,
			String kgnf, String cbdwfzr, String cbdwfzrlxfs, String xcsgfzr,
			String xcsgfzrlxfs, String xmfzbm, String xmfzr, String xmfzbmzgyj,
			String xmfzbmzg, String sfqdht, String sjbqz, String sfqdaqxy,
			String ajbqz, String jsfzr, String jsfzrlxfs, String xmfzrlxfs,
			String sfjbkgtjZykg, String xmssbmzrqz, String bz, String sfphlhjz,
			String zhswbqz, String sqbm, String jhjybqz, String xmyj,
			String isys) {
		this.lxid = lxid;
		this.status = status;
		this.cjsj = cjsj;
		this.gcxmmc = gcxmmc;
		this.gcxmbh = gcxmbh;
		this.gccbdw = gccbdw;
		this.gcnr = gcnr;
		this.sfczfjwzhs = sfczfjwzhs;
		this.gcsfsjtsgz = gcsfsjtsgz;
		this.jhkgrq = jhkgrq;
		this.jhgq = jhgq;
		this.jz = jz;
		this.zy = zy;
		this.kgnf = kgnf;
		this.cbdwfzr = cbdwfzr;
		this.cbdwfzrlxfs = cbdwfzrlxfs;
		this.xcsgfzr = xcsgfzr;
		this.xcsgfzrlxfs = xcsgfzrlxfs;
		this.xmfzbm = xmfzbm;
		this.xmfzr = xmfzr;
		this.xmfzbmzgyj = xmfzbmzgyj;
		this.xmfzbmzg = xmfzbmzg;
		this.sfqdht = sfqdht;
		this.sjbqz = sjbqz;
		this.sfqdaqxy = sfqdaqxy;
		this.ajbqz = ajbqz;
		this.jsfzr = jsfzr;
		this.jsfzrlxfs = jsfzrlxfs;
		this.xmfzrlxfs = xmfzrlxfs;
		this.sfjbkgtjZykg = sfjbkgtjZykg;
		this.xmssbmzrqz = xmssbmzrqz;
		this.bz = bz;
		this.sfphlhjz = sfphlhjz;
		this.zhswbqz = zhswbqz;
		this.sqbm = sqbm;
		this.jhjybqz = jhjybqz;
		this.xmyj = xmyj;
		this.isys = isys;
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

	@Column(name = "LXID", length = 32)
	public String getLxid() {
		return this.lxid;
	}

	public void setLxid(String lxid) {
		this.lxid = lxid;
	}

	@Column(name = "STATUS", length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CJSJ", length = 11)
	public Date getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	@Column(name = "GCXMMC", length = 50)
	public String getGcxmmc() {
		return this.gcxmmc;
	}

	public void setGcxmmc(String gcxmmc) {
		this.gcxmmc = gcxmmc;
	}

	@Column(name = "GCXMBH", length = 50)
	public String getGcxmbh() {
		return this.gcxmbh;
	}

	public void setGcxmbh(String gcxmbh) {
		this.gcxmbh = gcxmbh;
	}

	@Column(name = "GCCBDW", length = 100)
	public String getGccbdw() {
		return this.gccbdw;
	}

	public void setGccbdw(String gccbdw) {
		this.gccbdw = gccbdw;
	}

	@Column(name = "GCNR", length = 4000)
	public String getGcnr() {
		return this.gcnr;
	}

	public void setGcnr(String gcnr) {
		this.gcnr = gcnr;
	}

	@Column(name = "SFCZFJWZHS", length = 50)
	public String getSfczfjwzhs() {
		return this.sfczfjwzhs;
	}

	public void setSfczfjwzhs(String sfczfjwzhs) {
		this.sfczfjwzhs = sfczfjwzhs;
	}

	@Column(name = "GCSFSJTSGZ", length = 50)
	public String getGcsfsjtsgz() {
		return this.gcsfsjtsgz;
	}

	public void setGcsfsjtsgz(String gcsfsjtsgz) {
		this.gcsfsjtsgz = gcsfsjtsgz;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "JHKGRQ", length = 11)
	public Date getJhkgrq() {
		return this.jhkgrq;
	}

	public void setJhkgrq(Date jhkgrq) {
		this.jhkgrq = jhkgrq;
	}

	@Column(name = "JHGQ", length = 50)
	public String getJhgq() {
		return this.jhgq;
	}

	public void setJhgq(String jhgq) {
		this.jhgq = jhgq;
	}

	@Column(name = "JZ", length = 50)
	public String getJz() {
		return this.jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	@Column(name = "ZY", length = 50)
	public String getZy() {
		return this.zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	@Column(name = "KGNF", length = 50)
	public String getKgnf() {
		return this.kgnf;
	}

	public void setKgnf(String kgnf) {
		this.kgnf = kgnf;
	}

	@Column(name = "CBDWFZR", length = 50)
	public String getCbdwfzr() {
		return this.cbdwfzr;
	}

	public void setCbdwfzr(String cbdwfzr) {
		this.cbdwfzr = cbdwfzr;
	}

	@Column(name = "CBDWFZRLXFS", length = 50)
	public String getCbdwfzrlxfs() {
		return this.cbdwfzrlxfs;
	}

	public void setCbdwfzrlxfs(String cbdwfzrlxfs) {
		this.cbdwfzrlxfs = cbdwfzrlxfs;
	}

	@Column(name = "XCSGFZR", length = 50)
	public String getXcsgfzr() {
		return this.xcsgfzr;
	}

	public void setXcsgfzr(String xcsgfzr) {
		this.xcsgfzr = xcsgfzr;
	}

	@Column(name = "XCSGFZRLXFS", length = 50)
	public String getXcsgfzrlxfs() {
		return this.xcsgfzrlxfs;
	}

	public void setXcsgfzrlxfs(String xcsgfzrlxfs) {
		this.xcsgfzrlxfs = xcsgfzrlxfs;
	}

	@Column(name = "XMFZBM", length = 50)
	public String getXmfzbm() {
		return this.xmfzbm;
	}

	public void setXmfzbm(String xmfzbm) {
		this.xmfzbm = xmfzbm;
	}

	@Column(name = "XMFZR", length = 50)
	public String getXmfzr() {
		return this.xmfzr;
	}

	public void setXmfzr(String xmfzr) {
		this.xmfzr = xmfzr;
	}

	@Column(name = "XMFZBMZGYJ", length = 500)
	public String getXmfzbmzgyj() {
		return this.xmfzbmzgyj;
	}

	public void setXmfzbmzgyj(String xmfzbmzgyj) {
		this.xmfzbmzgyj = xmfzbmzgyj;
	}

	@Column(name = "XMFZBMZG", length = 50)
	public String getXmfzbmzg() {
		return this.xmfzbmzg;
	}

	public void setXmfzbmzg(String xmfzbmzg) {
		this.xmfzbmzg = xmfzbmzg;
	}

	@Column(name = "SFQDHT", length = 50)
	public String getSfqdht() {
		return this.sfqdht;
	}

	public void setSfqdht(String sfqdht) {
		this.sfqdht = sfqdht;
	}

	@Column(name = "SJBQZ", length = 50)
	public String getSjbqz() {
		return this.sjbqz;
	}

	public void setSjbqz(String sjbqz) {
		this.sjbqz = sjbqz;
	}

	@Column(name = "SFQDAQXY", length = 50)
	public String getSfqdaqxy() {
		return this.sfqdaqxy;
	}

	public void setSfqdaqxy(String sfqdaqxy) {
		this.sfqdaqxy = sfqdaqxy;
	}

	@Column(name = "AJBQZ", length = 50)
	public String getAjbqz() {
		return this.ajbqz;
	}

	public void setAjbqz(String ajbqz) {
		this.ajbqz = ajbqz;
	}

	@Column(name = "JSFZR", length = 50)
	public String getJsfzr() {
		return this.jsfzr;
	}

	public void setJsfzr(String jsfzr) {
		this.jsfzr = jsfzr;
	}

	@Column(name = "JSFZRLXFS", length = 50)
	public String getJsfzrlxfs() {
		return this.jsfzrlxfs;
	}

	public void setJsfzrlxfs(String jsfzrlxfs) {
		this.jsfzrlxfs = jsfzrlxfs;
	}

	@Column(name = "XMFZRLXFS", length = 50)
	public String getXmfzrlxfs() {
		return this.xmfzrlxfs;
	}

	public void setXmfzrlxfs(String xmfzrlxfs) {
		this.xmfzrlxfs = xmfzrlxfs;
	}

	@Column(name = "SFJBKGTJ_ZYKG", length = 50)
	public String getSfjbkgtjZykg() {
		return this.sfjbkgtjZykg;
	}

	public void setSfjbkgtjZykg(String sfjbkgtjZykg) {
		this.sfjbkgtjZykg = sfjbkgtjZykg;
	}

	@Column(name = "XMSSBMZRQZ", length = 50)
	public String getXmssbmzrqz() {
		return this.xmssbmzrqz;
	}

	public void setXmssbmzrqz(String xmssbmzrqz) {
		this.xmssbmzrqz = xmssbmzrqz;
	}

	@Column(name = "BZ", length = 500)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "SFPHLHJZ", length = 50)
	public String getSfphlhjz() {
		return this.sfphlhjz;
	}

	public void setSfphlhjz(String sfphlhjz) {
		this.sfphlhjz = sfphlhjz;
	}

	@Column(name = "ZHSWBQZ", length = 50)
	public String getZhswbqz() {
		return this.zhswbqz;
	}

	public void setZhswbqz(String zhswbqz) {
		this.zhswbqz = zhswbqz;
	}

	@Column(name = "SQBM", length = 50)
	public String getSqbm() {
		return this.sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	@Column(name = "JHJYBQZ", length = 50)
	public String getJhjybqz() {
		return this.jhjybqz;
	}

	public void setJhjybqz(String jhjybqz) {
		this.jhjybqz = jhjybqz;
	}

	@Column(name = "XMYJ", nullable = false, length = 50)
	public String getXmyj() {
		return this.xmyj;
	}

	public void setXmyj(String xmyj) {
		this.xmyj = xmyj;
	}

	@Column(name = "ISYS", length = 50)
	public String getIsys() {
		return this.isys;
	}

	public void setIsys(String isys) {
		this.isys = isys;
	}

}