package com.jteap.jhtj.sjxxxdy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 *描述：修改操作表
 *时间：2010-4-9
 *作者：童贝
 *
 */
@Entity
@Table(name="TJ_ITEMUPDATE")
public class TjItemUpdate {
	@Id
	@Column(name = "id", unique = true, nullable = false) 
	private Long id;//序号
	@Column(name="tabname")
	private String tabname;//数据表名
	@Column(name="nitem")
	private String nitem;//新字段名
	@Column(name="oitem")
	private String oitem;//旧字段名
	@Column(name="itype")
	private String itype;//字段类型
	@Column(name="cd")
	private Long cd;//字段长度
	@Column(name="xsw")
	private Long xsw;//字段小数位
	@Column(name="oprflag")
	private Long oprflag;//操作标志： 0--修改字段，1--修改数据类型或长度、小数位
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTabname() {
		return tabname;
	}
	public void setTabname(String tabname) {
		this.tabname = tabname;
	}
	public String getNitem() {
		return nitem;
	}
	public void setNitem(String nitem) {
		this.nitem = nitem;
	}
	public String getOitem() {
		return oitem;
	}
	public void setOitem(String oitem) {
		this.oitem = oitem;
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
	public Long getOprflag() {
		return oprflag;
	}
	public void setOprflag(Long oprflag) {
		this.oprflag = oprflag;
	}

}
