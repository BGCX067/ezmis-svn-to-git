package com.jteap.sb.sbydgl.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 描述 : 设备异动信息
 * 作者 : caofei
 * 时间 : Jul 26, 2010
 * 参数 : 
 * 返回值 : 
 * 异常 : 
 */
@Entity
@Table(name = "TB_SB_SBYDGL_YDXX")
public class Sbydxx {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;                     //编号
	
	@Column(name = "type")
	private String type;                   //异动类型
	
	@Column(name = "ydbh")
	private String ydbh;                   //异动编号
	
	@Column(name = "status")
	private String status;                 //异动状态
	
	@Column(name = "sbbm")
	private String sbbm;                   //设备编码
	
	@Column(name = "sbmc")
	private String sbmc;                   //设备名称
	
	@Column(name = "ydyy") 
	private String ydyy;                   //异动原因
	
	@Column(name = "ydfa") 
	private String ydfa;                   //异动方案
	
	@Column(name = "ydhyxfs")
	private String ydhyxfs;                //异动后运行方式
	
	@Column(name = "sqbm")
	private String sqbm;                   //申请部门
	
	@Column(name = "sqr")
	private String sqr;                    //申请人
	
	@Column(name = "sqsj")
	private Date sqsj;                     //申请时间
	
	@Column(name = "jxbyj")
	private String jxbyj;                  //检修部会审意见
	
	@Column(name = "jxbhsr")
	private String jxbhsr;                 //检修部会审人
	
	@Column(name = "jxbhssj")
	private Date jxbhssj;                  //检修部会审时间
	
	@Column(name = "fdbyj")
	private String fdbyj;                  //发电部会审意见
	
	@Column(name = "fdbhsr")
	private String fdbhsr;                 //发电部会审人
	
	@Column(name = "fdbhssj")
	private Date fdbhssj;                  //发电部会审时间
	
	@Column(name = "sjbyj")
	private String sjbyj;                  //生技部审核意见
	
	@Column(name = "sjbshr")
	private String sjbshr;                 //生技部审核人
	
	@Column(name = "sjbshsj")
	private Date sjbshsj;                  //生技部审核时间
	
	@Column(name = "ldyj")
	private String ldyj;                   //公司领导审批意见
	
	@Column(name = "ldspr")
	private String ldspr;                  //公司领导审批人
	
	@Column(name = "ldspsj")
	private Date ldspsj;                   //公司领导审批时间
	
	@Column(name = "zxqk")
	private String zxqk;                   //执行情况
	
	@Column(name = "zxr")
	private String zxr;                    //执行人
	
	@Column(name = "zxsj")
	private Date zxsj;                     //执行时间
	
	@Column(name = "fhr")
	private String fhr;                    //复核人
	
	@Column(name = "fhsj")
	private Date fhsj;                     //复核时间
	
	@Column(name = "remark")
	private String remark;                 //备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYdbh() {
		return ydbh;
	}

	public void setYdbh(String ydbh) {
		this.ydbh = ydbh;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getYdyy() {
		return ydyy;
	}

	public void setYdyy(String ydyy) {
		this.ydyy = ydyy;
	}

	public String getYdfa() {
		return ydfa;
	}

	public void setYdfa(String ydfa) {
		this.ydfa = ydfa;
	}

	public String getYdhyxfs() {
		return ydhyxfs;
	}

	public void setYdhyxfs(String ydhyxfs) {
		this.ydhyxfs = ydhyxfs;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public Date getSqsj() {
		return sqsj;
	}

	public void setSqsj(Date sqsj) {
		this.sqsj = sqsj;
	}

	public String getJxbyj() {
		return jxbyj;
	}

	public void setJxbyj(String jxbyj) {
		this.jxbyj = jxbyj;
	}

	public String getJxbhsr() {
		return jxbhsr;
	}

	public void setJxbhsr(String jxbhsr) {
		this.jxbhsr = jxbhsr;
	}

	public Date getJxbhssj() {
		return jxbhssj;
	}

	public void setJxbhssj(Date jxbhssj) {
		this.jxbhssj = jxbhssj;
	}

	public String getFdbyj() {
		return fdbyj;
	}

	public void setFdbyj(String fdbyj) {
		this.fdbyj = fdbyj;
	}

	public String getFdbhsr() {
		return fdbhsr;
	}

	public void setFdbhsr(String fdbhsr) {
		this.fdbhsr = fdbhsr;
	}

	public Date getFdbhssj() {
		return fdbhssj;
	}

	public void setFdbhssj(Date fdbhssj) {
		this.fdbhssj = fdbhssj;
	}

	public String getSjbyj() {
		return sjbyj;
	}

	public void setSjbyj(String sjbyj) {
		this.sjbyj = sjbyj;
	}

	public String getSjbshr() {
		return sjbshr;
	}

	public void setSjbshr(String sjbshr) {
		this.sjbshr = sjbshr;
	}

	public Date getSjbshsj() {
		return sjbshsj;
	}

	public void setSjbshsj(Date sjbshsj) {
		this.sjbshsj = sjbshsj;
	}

	public String getLdyj() {
		return ldyj;
	}

	public void setLdyj(String ldyj) {
		this.ldyj = ldyj;
	}

	public String getLdspr() {
		return ldspr;
	}

	public void setLdspr(String ldspr) {
		this.ldspr = ldspr;
	}

	public Date getLdspsj() {
		return ldspsj;
	}

	public void setLdspsj(Date ldspsj) {
		this.ldspsj = ldspsj;
	}

	public String getZxqk() {
		return zxqk;
	}

	public void setZxqk(String zxqk) {
		this.zxqk = zxqk;
	}

	public String getZxr() {
		return zxr;
	}

	public void setZxr(String zxr) {
		this.zxr = zxr;
	}

	public Date getZxsj() {
		return zxsj;
	}

	public void setZxsj(Date zxsj) {
		this.zxsj = zxsj;
	}

	public String getFhr() {
		return fhr;
	}

	public void setFhr(String fhr) {
		this.fhr = fhr;
	}

	public Date getFhsj() {
		return fhsj;
	}

	public void setFhsj(Date fhsj) {
		this.fhsj = fhsj;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
