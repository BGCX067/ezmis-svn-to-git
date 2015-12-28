package com.jteap.jhtj.sjxxxdy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 
 *描述：数据信息项定义
 *时间：2010-4-9
 *作者：童贝
 *
 */
@Entity
@Table(name="TJ_INFOITEM")
public class TjInfoItem {
	public static final String ITYPE_INT="INT";
	public static final String ITYPE_FLOAT="FLOAT";
	public static final String ITYPE_VARCHAR="VARCHAR";
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="KID")
	private String kid;//分类编码
	@Column(name="ITEM")
	private String item;//数据项编码
	@Column(name="SJLX")
	private String sjlx;//数据业务类型
	@Column(name="INAME")
	private String iname;//数据项名称
	@Column(name="DW")
	private String dw;//单位
	@Column(name="ITYPE")
	private String itype;//数据类型
	@Column(name="CD")
	private Long cd;//长度
	@Column(name="XSW")
	private Long xsw;//小数位
	@Column(name="QSFS")
	private Long qsfs;//取数方式
	@Column(name="SID")
	private String sid;//数据来源系统编号
	@Column(name="vname")
	private String vname;//数据视图名
	@Column(name="fname")
	private String fname;//数据字段名
	@Column(name="cexp")
	private String cexp;//计算公式
	@Column(name="corder")
	private Long corder;//计算顺序
	@Column(name="jsfs")
	private Long jsfs;//计算方式
	@Column(name="dname")
	private String dname;//DLL名称
	@Column(name="dfun_Id")
	private Long dfunId;//DLL功能号
	@Column(name="iorder")
	private Long iorder;//顺序号
	@Column(name="isvisible")
	private Long isvisible=new Long(1);//是否显示
	@Column(name="iskh")
	private Long iskh;//是否指标考核
	@Column(name="zbk")
	private String zbk;//指标卡编码

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getSjlx() {
		return sjlx;
	}
	public void setSjlx(String sjlx) {
		this.sjlx = sjlx;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getItype() {
		return itype;
	}
	public void setItype(String itype) {
		this.itype = itype;
	}
	public Long getCd() {
		return cd;
	}
	public void setCd(Long cd) {
		this.cd = cd;
	}
	public Long getXsw() {
		return xsw;
	}
	public void setXsw(Long xsw) {
		this.xsw = xsw;
	}
	public Long getQsfs() {
		return qsfs;
	}
	public void setQsfs(Long qsfs) {
		this.qsfs = qsfs;
	}
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getCexp() {
		return cexp;
	}
	public void setCexp(String cexp) {
		this.cexp = cexp;
	}
	public Long getCorder() {
		return corder;
	}
	public void setCorder(Long corder) {
		this.corder = corder;
	}
	public Long getJsfs() {
		return jsfs;
	}
	public void setJsfs(Long jsfs) {
		this.jsfs = jsfs;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public Long getDfunId() {
		return dfunId;
	}
	public void setDfunId(Long dfunId) {
		this.dfunId = dfunId;
	}
	public Long getIorder() {
		return iorder;
	}
	public void setIorder(Long iorder) {
		this.iorder = iorder;
	}
	public Long getIsvisible() {
		return isvisible;
	}
	public void setIsvisible(Long isvisible) {
		this.isvisible = isvisible;
	}
	public Long getIskh() {
		return iskh;
	}
	public void setIskh(Long iskh) {
		this.iskh = iskh;
	}
	public String getZbk() {
		return zbk;
	}
	public void setZbk(String zbk) {
		this.zbk = zbk;
	}
}
