package com.jteap.wz.tjrw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.jteap.core.utils.DateUtils;

/**
 * 统计任务实体类
 * @author lvchao
 *
 */
@Entity
@Table(name = "TB_WZ_TJRW")
public class Tjrw {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "RWMC")	
	private java.lang.String rwmc;	//任务名称
	
	@Column(name = "RWLB")	
	private java.lang.String rwlb;	//任务类别  比如：物资结存 资金动态
	
	@Column(name = "NF")	
	private java.lang.String nf;	//报表年份
	
	@Column(name = "YF")	
	private java.lang.String yf;	//报表月份
	
	@Column(name = "RI")	
	private java.lang.String ri;		//报表日期
	
	@Column(name = "CJSJ")	
	private java.util.Date cjsj;	//创建时间
	
	@Column(name = "STARTDT")	
	private java.util.Date startDt;	//开始时间
	
	@Column(name = "ENDDT")
	private java.util.Date endDt;	//结束时间
	
	@Column(name = "ZT")	
	private java.lang.String zt;	//任务状态  0等待 1进行中 2已完毕
	
	@Column(name = "BZ")	
	private java.lang.String bz;	//备注    显示新建过程信息

	
	
	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}
	
	public java.lang.String getRwmc() {
		return rwmc;
	}

	public void setRwmc(java.lang.String rwmc) {
		this.rwmc = rwmc;
	}
 
	public java.util.Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(java.util.Date cjsj) {
		this.cjsj = cjsj;
	}

	public java.lang.String getZt() {
		return zt;
	}

	public void setZt(java.lang.String zt) {
		this.zt = zt;
	}

	public java.lang.String getBz() {
		return bz;
	}

	public void setBz(java.lang.String bz) {
		this.bz = bz;
	}

	public java.lang.String getRwlb() {
		return rwlb;
	}

	public void setRwlb(java.lang.String rwlb) {
		this.rwlb = rwlb;
	}

	public java.lang.String getNf() {
		return nf;
	}

	public void setNf(java.lang.String nf) {
		this.nf = nf;
	}

	public java.lang.String getYf() {
		return yf;
	}

	public void setYf(java.lang.String yf) {
		this.yf = yf;
	}

	public java.util.Date getStartDt() {
		return startDt;
	}

	public void setStartDt(java.util.Date startDt) {
		this.startDt = startDt;
	}

	public java.util.Date getEndDt() {
		return endDt;
	}

	public void setEndDt(java.util.Date endDt) {
		this.endDt = endDt;
	}
	public String getStartStr(){
		String str = null;
		if(this.startDt!=null){
			str = DateUtils.formatDate(this.startDt, "yyyy-MM-dd HH:mm:ss");
		}
		return str;
	}
	public String getEndStr(){
		if(this.endDt!=null){
			return DateUtils.formatDate(this.endDt, "yyyy-MM-dd HH:mm:ss");
		}
		return null;
	}

	public java.lang.String getRi() {
		return ri;
	}

	public void setRi(java.lang.String ri) {
		this.ri = ri;
	}

}
