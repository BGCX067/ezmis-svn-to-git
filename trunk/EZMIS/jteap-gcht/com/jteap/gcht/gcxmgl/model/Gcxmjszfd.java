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
 * TbHtGcxmjszfd entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_HT_GCXMJSZFD", schema = "EZMIS")
public class Gcxmjszfd implements java.io.Serializable {

	// Fields

	private String id;
	private String status;
	private String xmmc;
	private String rq;
	private String xmwcsjhnr;
	private String cbdw;
	private String skdwzh;
	private String xmfyly;
	private String khh;
	private String xmhtje;
	private String yljyfkje;
	private String bcyfk;
	private String bckk;
	private String xmssbmyj;
	private String xmssbmzrqm;
	private String xmssbmzrsj;
	private String aqjcbmyj;
	private String scjsbyj;
	private String jhjybyj;
	private String cwbyj;
	private String zhjs;
	private String fgfzjl;
	private String zjl;
	private String aqjcbmzrqpsj;
	private String aqjcbzrqm;
	private String scjsbzrqz;
	private String scjsbzrqzsj;
	private String jhjybzrqz;
	private String jhjybzrqpsj;
	private String cwbzrqz;
	private String cwbzrqpsj;
	private String zhjsyj;
	private String fgfzjlqpsj;
	private String fgfzjlyj;
	private String zjlyj;
	private String zjlqpsj;
	private String bcsfkdx;
	private String zhjsqpsj;
	private String xmssbmzz;
	private String ssbmzzqpsj;
	private Date cjsj;
	private String gcysId;
	private String xmbh;
	private String bcsfk;
	private String xmyj;
	private String sqbm;
	private String sqr;
	private String sqsj;
	private String zfzt;
	private String fqzt;

	// Constructors

	/** default constructor */
	public Gcxmjszfd() {
	}

	/** minimal constructor */
	public Gcxmjszfd(String bcsfk, String xmyj) {
		this.bcsfk = bcsfk;
		this.xmyj = xmyj;
	}

	/** full constructor */
	public Gcxmjszfd(String status, String xmmc, String rq,
			String xmwcsjhnr, String cbdw, String skdwzh, String xmfyly,
			String khh, String xmhtje, String yljyfkje, String bcyfk,
			String bckk, String xmssbmyj, String xmssbmzrqm, String xmssbmzrsj,
			String aqjcbmyj, String scjsbyj, String jhjybyj, String cwbyj,
			String zhjs, String fgfzjl, String zjl, String aqjcbmzrqpsj,
			String aqjcbzrqm, String scjsbzrqz, String scjsbzrqzsj,
			String jhjybzrqz, String jhjybzrqpsj, String cwbzrqz,
			String cwbzrqpsj, String zhjsyj, String fgfzjlqpsj,
			String fgfzjlyj, String zjlyj, String zjlqpsj, String bcsfkdx,
			String zhjsqpsj, String xmssbmzz, String ssbmzzqpsj, Date cjsj,
			String gcysId, String xmbh, String bcsfk, String xmyj,String sqbm,
			String sqr, String sqsj,String zfzt,String fqzt) {
		this.status = status;
		this.zfzt = zfzt;
		this.fqzt = fqzt;
		this.xmmc = xmmc;
		this.rq = rq;
		this.xmwcsjhnr = xmwcsjhnr;
		this.cbdw = cbdw;
		this.skdwzh = skdwzh;
		this.xmfyly = xmfyly;
		this.khh = khh;
		this.xmhtje = xmhtje;
		this.yljyfkje = yljyfkje;
		this.bcyfk = bcyfk;
		this.bckk = bckk;
		this.xmssbmyj = xmssbmyj;
		this.xmssbmzrqm = xmssbmzrqm;
		this.xmssbmzrsj = xmssbmzrsj;
		this.aqjcbmyj = aqjcbmyj;
		this.scjsbyj = scjsbyj;
		this.jhjybyj = jhjybyj;
		this.cwbyj = cwbyj;
		this.zhjs = zhjs;
		this.fgfzjl = fgfzjl;
		this.zjl = zjl;
		this.aqjcbmzrqpsj = aqjcbmzrqpsj;
		this.aqjcbzrqm = aqjcbzrqm;
		this.scjsbzrqz = scjsbzrqz;
		this.scjsbzrqzsj = scjsbzrqzsj;
		this.jhjybzrqz = jhjybzrqz;
		this.jhjybzrqpsj = jhjybzrqpsj;
		this.cwbzrqz = cwbzrqz;
		this.cwbzrqpsj = cwbzrqpsj;
		this.zhjsyj = zhjsyj;
		this.fgfzjlqpsj = fgfzjlqpsj;
		this.fgfzjlyj = fgfzjlyj;
		this.zjlyj = zjlyj;
		this.zjlqpsj = zjlqpsj;
		this.bcsfkdx = bcsfkdx;
		this.zhjsqpsj = zhjsqpsj;
		this.xmssbmzz = xmssbmzz;
		this.ssbmzzqpsj = ssbmzzqpsj;
		this.cjsj = cjsj;
		this.gcysId = gcysId;
		this.xmbh = xmbh;
		this.bcsfk = bcsfk;
		this.xmyj = xmyj;
		this.sqbm = sqbm;
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

	@Column(name = "STATUS", length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	@Column(name = "ZFZT", length = 50)
	public String getZfzt() {
		return zfzt;
	}

	public void setZfzt(String zfzt) {
		this.zfzt = zfzt;
	}
	
	
	@Column(name = "FQZT", length = 50)
	public String getFqzt() {
		return fqzt;
	}

	public void setFqzt(String fqzt) {
		this.fqzt = fqzt;
	}
	

	@Column(name = "XMMC", length = 200)
	public String getXmmc() {
		return this.xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	@Column(name = "RQ", length = 50)
	public String getRq() {
		return this.rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	@Column(name = "XMWCSJHNR", length = 1000)
	public String getXmwcsjhnr() {
		return this.xmwcsjhnr;
	}

	public void setXmwcsjhnr(String xmwcsjhnr) {
		this.xmwcsjhnr = xmwcsjhnr;
	}

	@Column(name = "CBDW", length = 100)
	public String getCbdw() {
		return this.cbdw;
	}

	public void setCbdw(String cbdw) {
		this.cbdw = cbdw;
	}

	@Column(name = "SKDWZH", length = 50)
	public String getSkdwzh() {
		return this.skdwzh;
	}

	public void setSkdwzh(String skdwzh) {
		this.skdwzh = skdwzh;
	}

	@Column(name = "XMFYLY", length = 100)
	public String getXmfyly() {
		return this.xmfyly;
	}

	public void setXmfyly(String xmfyly) {
		this.xmfyly = xmfyly;
	}

	@Column(name = "KHH", length = 100)
	public String getKhh() {
		return this.khh;
	}

	public void setKhh(String khh) {
		this.khh = khh;
	}

	@Column(name = "XMHTJE", length = 20)
	public String getXmhtje() {
		return this.xmhtje;
	}

	public void setXmhtje(String xmhtje) {
		this.xmhtje = xmhtje;
	}

	@Column(name = "YLJYFKJE", length = 20)
	public String getYljyfkje() {
		return this.yljyfkje;
	}

	public void setYljyfkje(String yljyfkje) {
		this.yljyfkje = yljyfkje;
	}

	@Column(name = "BCYFK", length = 20)
	public String getBcyfk() {
		return this.bcyfk;
	}

	public void setBcyfk(String bcyfk) {
		this.bcyfk = bcyfk;
	}

	@Column(name = "BCKK", length = 50)
	public String getBckk() {
		return this.bckk;
	}

	public void setBckk(String bckk) {
		this.bckk = bckk;
	}

	@Column(name = "XMSSBMYJ", length = 500)
	public String getXmssbmyj() {
		return this.xmssbmyj;
	}

	public void setXmssbmyj(String xmssbmyj) {
		this.xmssbmyj = xmssbmyj;
	}

	@Column(name = "XMSSBMZRQM", length = 50)
	public String getXmssbmzrqm() {
		return this.xmssbmzrqm;
	}

	public void setXmssbmzrqm(String xmssbmzrqm) {
		this.xmssbmzrqm = xmssbmzrqm;
	}

	@Column(name = "XMSSBMZRSJ", length = 50)
	public String getXmssbmzrsj() {
		return this.xmssbmzrsj;
	}

	public void setXmssbmzrsj(String xmssbmzrsj) {
		this.xmssbmzrsj = xmssbmzrsj;
	}

	@Column(name = "AQJCBMYJ", length = 50)
	public String getAqjcbmyj() {
		return this.aqjcbmyj;
	}

	public void setAqjcbmyj(String aqjcbmyj) {
		this.aqjcbmyj = aqjcbmyj;
	}

	@Column(name = "SCJSBYJ", length = 50)
	public String getScjsbyj() {
		return this.scjsbyj;
	}

	public void setScjsbyj(String scjsbyj) {
		this.scjsbyj = scjsbyj;
	}

	@Column(name = "JHJYBYJ", length = 50)
	public String getJhjybyj() {
		return this.jhjybyj;
	}

	public void setJhjybyj(String jhjybyj) {
		this.jhjybyj = jhjybyj;
	}

	@Column(name = "CWBYJ", length = 50)
	public String getCwbyj() {
		return this.cwbyj;
	}

	public void setCwbyj(String cwbyj) {
		this.cwbyj = cwbyj;
	}

	@Column(name = "ZHJS", length = 50)
	public String getZhjs() {
		return this.zhjs;
	}

	public void setZhjs(String zhjs) {
		this.zhjs = zhjs;
	}

	@Column(name = "FGFZJL", length = 50)
	public String getFgfzjl() {
		return this.fgfzjl;
	}

	public void setFgfzjl(String fgfzjl) {
		this.fgfzjl = fgfzjl;
	}

	@Column(name = "ZJL", length = 50)
	public String getZjl() {
		return this.zjl;
	}

	public void setZjl(String zjl) {
		this.zjl = zjl;
	}

	@Column(name = "AQJCBMZRQPSJ", length = 50)
	public String getAqjcbmzrqpsj() {
		return this.aqjcbmzrqpsj;
	}

	public void setAqjcbmzrqpsj(String aqjcbmzrqpsj) {
		this.aqjcbmzrqpsj = aqjcbmzrqpsj;
	}

	@Column(name = "AQJCBZRQM", length = 50)
	public String getAqjcbzrqm() {
		return this.aqjcbzrqm;
	}

	public void setAqjcbzrqm(String aqjcbzrqm) {
		this.aqjcbzrqm = aqjcbzrqm;
	}

	@Column(name = "SCJSBZRQZ", length = 50)
	public String getScjsbzrqz() {
		return this.scjsbzrqz;
	}

	public void setScjsbzrqz(String scjsbzrqz) {
		this.scjsbzrqz = scjsbzrqz;
	}

	@Column(name = "SCJSBZRQZSJ", length = 50)
	public String getScjsbzrqzsj() {
		return this.scjsbzrqzsj;
	}

	public void setScjsbzrqzsj(String scjsbzrqzsj) {
		this.scjsbzrqzsj = scjsbzrqzsj;
	}

	@Column(name = "JHJYBZRQZ", length = 50)
	public String getJhjybzrqz() {
		return this.jhjybzrqz;
	}

	public void setJhjybzrqz(String jhjybzrqz) {
		this.jhjybzrqz = jhjybzrqz;
	}

	@Column(name = "JHJYBZRQPSJ", length = 50)
	public String getJhjybzrqpsj() {
		return this.jhjybzrqpsj;
	}

	public void setJhjybzrqpsj(String jhjybzrqpsj) {
		this.jhjybzrqpsj = jhjybzrqpsj;
	}

	@Column(name = "CWBZRQZ", length = 50)
	public String getCwbzrqz() {
		return this.cwbzrqz;
	}

	public void setCwbzrqz(String cwbzrqz) {
		this.cwbzrqz = cwbzrqz;
	}

	@Column(name = "CWBZRQPSJ", length = 50)
	public String getCwbzrqpsj() {
		return this.cwbzrqpsj;
	}

	public void setCwbzrqpsj(String cwbzrqpsj) {
		this.cwbzrqpsj = cwbzrqpsj;
	}

	@Column(name = "ZHJSYJ", length = 50)
	public String getZhjsyj() {
		return this.zhjsyj;
	}

	public void setZhjsyj(String zhjsyj) {
		this.zhjsyj = zhjsyj;
	}

	@Column(name = "FGFZJLQPSJ", length = 50)
	public String getFgfzjlqpsj() {
		return this.fgfzjlqpsj;
	}

	public void setFgfzjlqpsj(String fgfzjlqpsj) {
		this.fgfzjlqpsj = fgfzjlqpsj;
	}

	@Column(name = "FGFZJLYJ", length = 50)
	public String getFgfzjlyj() {
		return this.fgfzjlyj;
	}

	public void setFgfzjlyj(String fgfzjlyj) {
		this.fgfzjlyj = fgfzjlyj;
	}

	@Column(name = "ZJLYJ", length = 50)
	public String getZjlyj() {
		return this.zjlyj;
	}

	public void setZjlyj(String zjlyj) {
		this.zjlyj = zjlyj;
	}

	@Column(name = "ZJLQPSJ", length = 50)
	public String getZjlqpsj() {
		return this.zjlqpsj;
	}

	public void setZjlqpsj(String zjlqpsj) {
		this.zjlqpsj = zjlqpsj;
	}

	@Column(name = "BCSFKDX", length = 50)
	public String getBcsfkdx() {
		return this.bcsfkdx;
	}

	public void setBcsfkdx(String bcsfkdx) {
		this.bcsfkdx = bcsfkdx;
	}

	@Column(name = "ZHJSQPSJ", length = 50)
	public String getZhjsqpsj() {
		return this.zhjsqpsj;
	}

	public void setZhjsqpsj(String zhjsqpsj) {
		this.zhjsqpsj = zhjsqpsj;
	}

	@Column(name = "XMSSBMZZ", length = 50)
	public String getXmssbmzz() {
		return this.xmssbmzz;
	}

	public void setXmssbmzz(String xmssbmzz) {
		this.xmssbmzz = xmssbmzz;
	}

	@Column(name = "SSBMZZQPSJ", length = 50)
	public String getSsbmzzqpsj() {
		return this.ssbmzzqpsj;
	}

	public void setSsbmzzqpsj(String ssbmzzqpsj) {
		this.ssbmzzqpsj = ssbmzzqpsj;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CJSJ", length = 11)
	public Date getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	@Column(name = "GCYS_ID", length = 50)
	public String getGcysId() {
		return this.gcysId;
	}

	public void setGcysId(String gcysId) {
		this.gcysId = gcysId;
	}

	@Column(name = "XMBH", length = 50)
	public String getXmbh() {
		return this.xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	@Column(name = "BCSFK", nullable = false, length = 50)
	public String getBcsfk() {
		return this.bcsfk;
	}

	public void setBcsfk(String bcsfk) {
		this.bcsfk = bcsfk;
	}

	@Column(name = "XMYJ", nullable = false, length = 50)
	public String getXmyj() {
		return this.xmyj;
	}

	public void setXmyj(String xmyj) {
		this.xmyj = xmyj;
	}
	@Column(name = "SQBM", nullable = false, length = 50)
	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}
	
	@Column(name = "SQR", nullable = false, length = 50)
	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	
	@Column(name = "SQSJ", nullable = false, length = 50)
	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}
	
	
	
}