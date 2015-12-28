package com.jteap.jhtj.sjxxxdy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 *描述：增加删除操作表
 *时间：2010-4-9
 *作者：童贝
 *
 */
@Entity
@Table(name="TJ_ITEMOPR")
public class TjItemOpr {
	@Id
	@Column(name = "id", unique = true, nullable = false) 
	private Long id;//序号
	@Column(name="tabname")
	private String tabname;//表名
	@Column(name="item")
	private String item;//字段名
	@Column(name="itype")
	private String itype;//字段类型
	@Column(name="cd")
	private Long cd;//长度
	@Column(name="xsw")
	private Long xsw;//小数位
	@Column(name="oprflag")
	private Long oprflag;//操作标志： 1--为插入，0--删除
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
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
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
