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
 * TbHtWtd entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_HT_WTD", schema = "EZMIS")
public class Wtd implements java.io.Serializable {

	// Fields

	private String id;
	private Double gcjsje;
	private String zgcsqz;
	private String gzcsyj;
	private String jhbzrqzsj;
	private String jhbzrqz;
	private String jhbzgqzsj;
	private String jhbzgqz;
	private String jhbyj;
	private String sjbzrqzsj;
	private String sjbzgqzsj;
	private String sqbmzgqzsj;
	private String sqbmzg;
	private String sqbmqzsj;
	private String sqbmqz;
	private String xmbz;
	private String yfkhh;
	private String jfkhh;
	private String yfdwzh;
	private String jfxxzh;
	private String yfxx;
	private String jfxx;
	private String gcjsjedx;
	private Date cjsj;
	private String xmbh;
	private String xmlx;
	private String xmmc;
	private String xmyj;
	private String cbfs;
	private String gcys;
	private Date kgrq;
	private String jhgq;
	private String zy;
	private String sqbm;
	private String lxnf;
	private String lxyy;
	private String gcnr;
	private String sjbyj;
	private String sjbzg;
	private String sjbzr;
	private String jycjdw;
	private String zgfzjlyj;
	private String zgfzjl;
	private String zgfzjlspsj;
	private String zjlyj;
	private String zjl;
	private String zjlspsj;
	private String xmxh;
	private String status;
	private String zgcsqzsj;
	private String yffzr;
	private String yffzrlxfs;
	private String yfsgfzr;
	private String yfsgfzrlxfs;
	private String iskg;

	// Constructors

	/** default constructor */
	public Wtd() {
	}

	/** full constructor */
	public Wtd(Double gcjsje, String zgcsqz, String gzcsyj,
			String jhbzrqzsj, String jhbzrqz, String jhbzgqzsj, String jhbzgqz,
			String jhbyj, String sjbzrqzsj, String sjbzgqzsj,
			String sqbmzgqzsj, String sqbmzg, String sqbmqzsj, String sqbmqz,
			String xmbz, String yfkhh, String jfkhh, String yfdwzh,
			String jfxxzh, String yfxx, String jfxx, String gcjsjedx,
			Date cjsj, String xmbh, String xmlx, String xmmc, String xmyj,
			String cbfs, String gcys, Date kgrq, String jhgq, String zy,
			String sqbm, String lxnf, String lxyy, String gcnr, String sjbyj,
			String sjbzg, String sjbzr, String jycjdw, String zgfzjlyj,
			String zgfzjl, String zgfzjlspsj, String zjlyj, String zjl,
			String zjlspsj, String xmxh, String status, String zgcsqzsj,
			String yffzr, String yffzrlxfs, String yfsgfzr, String yfsgfzrlxfs,
			String iskg) {
		this.gcjsje = gcjsje;
		this.zgcsqz = zgcsqz;
		this.gzcsyj = gzcsyj;
		this.jhbzrqzsj = jhbzrqzsj;
		this.jhbzrqz = jhbzrqz;
		this.jhbzgqzsj = jhbzgqzsj;
		this.jhbzgqz = jhbzgqz;
		this.jhbyj = jhbyj;
		this.sjbzrqzsj = sjbzrqzsj;
		this.sjbzgqzsj = sjbzgqzsj;
		this.sqbmzgqzsj = sqbmzgqzsj;
		this.sqbmzg = sqbmzg;
		this.sqbmqzsj = sqbmqzsj;
		this.sqbmqz = sqbmqz;
		this.xmbz = xmbz;
		this.yfkhh = yfkhh;
		this.jfkhh = jfkhh;
		this.yfdwzh = yfdwzh;
		this.jfxxzh = jfxxzh;
		this.yfxx = yfxx;
		this.jfxx = jfxx;
		this.gcjsjedx = gcjsjedx;
		this.cjsj = cjsj;
		this.xmbh = xmbh;
		this.xmlx = xmlx;
		this.xmmc = xmmc;
		this.xmyj = xmyj;
		this.cbfs = cbfs;
		this.gcys = gcys;
		this.kgrq = kgrq;
		this.jhgq = jhgq;
		this.zy = zy;
		this.sqbm = sqbm;
		this.lxnf = lxnf;
		this.lxyy = lxyy;
		this.gcnr = gcnr;
		this.sjbyj = sjbyj;
		this.sjbzg = sjbzg;
		this.sjbzr = sjbzr;
		this.jycjdw = jycjdw;
		this.zgfzjlyj = zgfzjlyj;
		this.zgfzjl = zgfzjl;
		this.zgfzjlspsj = zgfzjlspsj;
		this.zjlyj = zjlyj;
		this.zjl = zjl;
		this.zjlspsj = zjlspsj;
		this.xmxh = xmxh;
		this.status = status;
		this.zgcsqzsj = zgcsqzsj;
		this.yffzr = yffzr;
		this.yffzrlxfs = yffzrlxfs;
		this.yfsgfzr = yfsgfzr;
		this.yfsgfzrlxfs = yfsgfzrlxfs;
		this.iskg = iskg;
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

	@Column(name = "GCJSJE", precision = 12, scale = 4)
	public Double getGcjsje() {
		return this.gcjsje;
	}

	public void setGcjsje(Double gcjsje) {
		this.gcjsje = gcjsje;
	}

	@Column(name = "ZGCSQZ", length = 50)
	public String getZgcsqz() {
		return this.zgcsqz;
	}

	public void setZgcsqz(String zgcsqz) {
		this.zgcsqz = zgcsqz;
	}

	@Column(name = "GZCSYJ", length = 2000)
	public String getGzcsyj() {
		return this.gzcsyj;
	}

	public void setGzcsyj(String gzcsyj) {
		this.gzcsyj = gzcsyj;
	}

	@Column(name = "JHBZRQZSJ", length = 50)
	public String getJhbzrqzsj() {
		return this.jhbzrqzsj;
	}

	public void setJhbzrqzsj(String jhbzrqzsj) {
		this.jhbzrqzsj = jhbzrqzsj;
	}

	@Column(name = "JHBZRQZ", length = 50)
	public String getJhbzrqz() {
		return this.jhbzrqz;
	}

	public void setJhbzrqz(String jhbzrqz) {
		this.jhbzrqz = jhbzrqz;
	}

	@Column(name = "JHBZGQZSJ", length = 50)
	public String getJhbzgqzsj() {
		return this.jhbzgqzsj;
	}

	public void setJhbzgqzsj(String jhbzgqzsj) {
		this.jhbzgqzsj = jhbzgqzsj;
	}

	@Column(name = "JHBZGQZ", length = 50)
	public String getJhbzgqz() {
		return this.jhbzgqz;
	}

	public void setJhbzgqz(String jhbzgqz) {
		this.jhbzgqz = jhbzgqz;
	}

	@Column(name = "JHBYJ", length = 2000)
	public String getJhbyj() {
		return this.jhbyj;
	}

	public void setJhbyj(String jhbyj) {
		this.jhbyj = jhbyj;
	}

	@Column(name = "SJBZRQZSJ", length = 50)
	public String getSjbzrqzsj() {
		return this.sjbzrqzsj;
	}

	public void setSjbzrqzsj(String sjbzrqzsj) {
		this.sjbzrqzsj = sjbzrqzsj;
	}

	@Column(name = "SJBZGQZSJ", length = 50)
	public String getSjbzgqzsj() {
		return this.sjbzgqzsj;
	}

	public void setSjbzgqzsj(String sjbzgqzsj) {
		this.sjbzgqzsj = sjbzgqzsj;
	}

	@Column(name = "SQBMZGQZSJ", length = 50)
	public String getSqbmzgqzsj() {
		return this.sqbmzgqzsj;
	}

	public void setSqbmzgqzsj(String sqbmzgqzsj) {
		this.sqbmzgqzsj = sqbmzgqzsj;
	}

	@Column(name = "SQBMZG", length = 50)
	public String getSqbmzg() {
		return this.sqbmzg;
	}

	public void setSqbmzg(String sqbmzg) {
		this.sqbmzg = sqbmzg;
	}

	@Column(name = "SQBMQZSJ", length = 50)
	public String getSqbmqzsj() {
		return this.sqbmqzsj;
	}

	public void setSqbmqzsj(String sqbmqzsj) {
		this.sqbmqzsj = sqbmqzsj;
	}

	@Column(name = "SQBMQZ", length = 50)
	public String getSqbmqz() {
		return this.sqbmqz;
	}

	public void setSqbmqz(String sqbmqz) {
		this.sqbmqz = sqbmqz;
	}

	@Column(name = "XMBZ", length = 1000)
	public String getXmbz() {
		return this.xmbz;
	}

	public void setXmbz(String xmbz) {
		this.xmbz = xmbz;
	}

	@Column(name = "YFKHH", length = 200)
	public String getYfkhh() {
		return this.yfkhh;
	}

	public void setYfkhh(String yfkhh) {
		this.yfkhh = yfkhh;
	}

	@Column(name = "JFKHH", length = 200)
	public String getJfkhh() {
		return this.jfkhh;
	}

	public void setJfkhh(String jfkhh) {
		this.jfkhh = jfkhh;
	}

	@Column(name = "YFDWZH", length = 200)
	public String getYfdwzh() {
		return this.yfdwzh;
	}

	public void setYfdwzh(String yfdwzh) {
		this.yfdwzh = yfdwzh;
	}

	@Column(name = "JFXXZH", length = 200)
	public String getJfxxzh() {
		return this.jfxxzh;
	}

	public void setJfxxzh(String jfxxzh) {
		this.jfxxzh = jfxxzh;
	}

	@Column(name = "YFXX", length = 200)
	public String getYfxx() {
		return this.yfxx;
	}

	public void setYfxx(String yfxx) {
		this.yfxx = yfxx;
	}

	@Column(name = "JFXX", length = 200)
	public String getJfxx() {
		return this.jfxx;
	}

	public void setJfxx(String jfxx) {
		this.jfxx = jfxx;
	}

	@Column(name = "GCJSJEDX", length = 200)
	public String getGcjsjedx() {
		return this.gcjsjedx;
	}

	public void setGcjsjedx(String gcjsjedx) {
		this.gcjsjedx = gcjsjedx;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CJSJ", length = 11)
	public Date getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	@Column(name = "XMBH", length = 50)
	public String getXmbh() {
		return this.xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	@Column(name = "XMLX", length = 50)
	public String getXmlx() {
		return this.xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	@Column(name = "XMMC", length = 50)
	public String getXmmc() {
		return this.xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	@Column(name = "XMYJ", length = 50)
	public String getXmyj() {
		return this.xmyj;
	}

	public void setXmyj(String xmyj) {
		this.xmyj = xmyj;
	}

	@Column(name = "CBFS", length = 50)
	public String getCbfs() {
		return this.cbfs;
	}

	public void setCbfs(String cbfs) {
		this.cbfs = cbfs;
	}

	@Column(name = "GCYS", length = 50)
	public String getGcys() {
		return this.gcys;
	}

	public void setGcys(String gcys) {
		this.gcys = gcys;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "KGRQ", length = 11)
	public Date getKgrq() {
		return this.kgrq;
	}

	public void setKgrq(Date kgrq) {
		this.kgrq = kgrq;
	}

	@Column(name = "JHGQ", length = 50)
	public String getJhgq() {
		return this.jhgq;
	}

	public void setJhgq(String jhgq) {
		this.jhgq = jhgq;
	}

	@Column(name = "ZY", length = 50)
	public String getZy() {
		return this.zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	@Column(name = "SQBM", length = 50)
	public String getSqbm() {
		return this.sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	@Column(name = "LXNF", length = 50)
	public String getLxnf() {
		return this.lxnf;
	}

	public void setLxnf(String lxnf) {
		this.lxnf = lxnf;
	}

	@Column(name = "LXYY", length = 500)
	public String getLxyy() {
		return this.lxyy;
	}

	public void setLxyy(String lxyy) {
		this.lxyy = lxyy;
	}

	@Column(name = "GCNR", length = 4000)
	public String getGcnr() {
		return this.gcnr;
	}

	public void setGcnr(String gcnr) {
		this.gcnr = gcnr;
	}

	@Column(name = "SJBYJ", length = 500)
	public String getSjbyj() {
		return this.sjbyj;
	}

	public void setSjbyj(String sjbyj) {
		this.sjbyj = sjbyj;
	}

	@Column(name = "SJBZG", length = 50)
	public String getSjbzg() {
		return this.sjbzg;
	}

	public void setSjbzg(String sjbzg) {
		this.sjbzg = sjbzg;
	}

	@Column(name = "SJBZR", length = 50)
	public String getSjbzr() {
		return this.sjbzr;
	}

	public void setSjbzr(String sjbzr) {
		this.sjbzr = sjbzr;
	}

	@Column(name = "JYCJDW", length = 100)
	public String getJycjdw() {
		return this.jycjdw;
	}

	public void setJycjdw(String jycjdw) {
		this.jycjdw = jycjdw;
	}

	@Column(name = "ZGFZJLYJ", length = 500)
	public String getZgfzjlyj() {
		return this.zgfzjlyj;
	}

	public void setZgfzjlyj(String zgfzjlyj) {
		this.zgfzjlyj = zgfzjlyj;
	}

	@Column(name = "ZGFZJL", length = 50)
	public String getZgfzjl() {
		return this.zgfzjl;
	}

	public void setZgfzjl(String zgfzjl) {
		this.zgfzjl = zgfzjl;
	}

	@Column(name = "ZGFZJLSPSJ", length = 50)
	public String getZgfzjlspsj() {
		return this.zgfzjlspsj;
	}

	public void setZgfzjlspsj(String zgfzjlspsj) {
		this.zgfzjlspsj = zgfzjlspsj;
	}

	@Column(name = "ZJLYJ", length = 500)
	public String getZjlyj() {
		return this.zjlyj;
	}

	public void setZjlyj(String zjlyj) {
		this.zjlyj = zjlyj;
	}

	@Column(name = "ZJL", length = 50)
	public String getZjl() {
		return this.zjl;
	}

	public void setZjl(String zjl) {
		this.zjl = zjl;
	}

	@Column(name = "ZJLSPSJ", length = 50)
	public String getZjlspsj() {
		return this.zjlspsj;
	}

	public void setZjlspsj(String zjlspsj) {
		this.zjlspsj = zjlspsj;
	}

	@Column(name = "XMXH", length = 50)
	public String getXmxh() {
		return this.xmxh;
	}

	public void setXmxh(String xmxh) {
		this.xmxh = xmxh;
	}

	@Column(name = "STATUS", length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ZGCSQZSJ", length = 50)
	public String getZgcsqzsj() {
		return this.zgcsqzsj;
	}

	public void setZgcsqzsj(String zgcsqzsj) {
		this.zgcsqzsj = zgcsqzsj;
	}

	@Column(name = "YFFZR", length = 50)
	public String getYffzr() {
		return this.yffzr;
	}

	public void setYffzr(String yffzr) {
		this.yffzr = yffzr;
	}

	@Column(name = "YFFZRLXFS", length = 50)
	public String getYffzrlxfs() {
		return this.yffzrlxfs;
	}

	public void setYffzrlxfs(String yffzrlxfs) {
		this.yffzrlxfs = yffzrlxfs;
	}

	@Column(name = "YFSGFZR", length = 50)
	public String getYfsgfzr() {
		return this.yfsgfzr;
	}

	public void setYfsgfzr(String yfsgfzr) {
		this.yfsgfzr = yfsgfzr;
	}

	@Column(name = "YFSGFZRLXFS", length = 50)
	public String getYfsgfzrlxfs() {
		return this.yfsgfzrlxfs;
	}

	public void setYfsgfzrlxfs(String yfsgfzrlxfs) {
		this.yfsgfzrlxfs = yfsgfzrlxfs;
	}

	@Column(name = "ISKG", length = 50)
	public String getIskg() {
		return this.iskg;
	}

	public void setIskg(String iskg) {
		this.iskg = iskg;
	}

}