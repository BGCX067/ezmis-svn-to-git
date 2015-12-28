package com.jteap.sb.sbtzgl.model;

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
 * 设备基础台帐
 * @author caofei
 */
@Entity
@Table(name = "TB_SB_YXTZGL_JCTZ")
public class Jctz {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;                             //编号
	
	@ManyToOne()
	@JoinColumn(name="TZFL_CATALOG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private SbtzCatalog sbtzCatalog;               //设备台帐分类ID
	
	@Column(name = "KKS")
	private String kks;                            //KKS码
	
	@Column(name = "SBBM")
	private String sbbm;                           //设备编码
	
	@Column(name = "YBMC")
	private String ybmc;                           //仪表名称
	 
	@Column(name = "YT")
	private String yt;                             //用途
	
	@Column(name = "XSJGF")
	private String xsjgf;                          //型式及规范
	
	@Column(name = "DW")
	private String dw;                             //单位
	
	@Column(name = "SL")
	private String sl;                             //数量
	
	@Column(name = "AZDD")
	private String azdd;                           //安装地点
	
	@Column(name = "XTTH")
	private String xtth;                           //系统图号
	
	@Column(name = "REMARK")                       //备注
	private String remark;
	
	@Column(name = "CJSJ")
	private Date cjsj;                             //创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKks() {
		return kks;
	}

	public void setKks(String kks) {
		this.kks = kks;
	}

	public String getSbbm() {
		return sbbm;
	}

	public void setSbbm(String sbbm) {
		this.sbbm = sbbm;
	}

	public String getYbmc() {
		return ybmc;
	}

	public void setYbmc(String ybmc) {
		this.ybmc = ybmc;
	}

	public String getYt() {
		return yt;
	}

	public void setYt(String yt) {
		this.yt = yt;
	}

	public String getXsjgf() {
		return xsjgf;
	}

	public void setXsjgf(String xsjgf) {
		this.xsjgf = xsjgf;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getAzdd() {
		return azdd;
	}

	public void setAzdd(String azdd) {
		this.azdd = azdd;
	}

	public String getXtth() {
		return xtth;
	}

	public void setXtth(String xtth) {
		this.xtth = xtth;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public SbtzCatalog getSbtzCatalog() {
		return sbtzCatalog;
	}

	public void setSbtzCatalog(SbtzCatalog sbtzCatalog) {
		this.sbtzCatalog = sbtzCatalog;
	}
	
}
