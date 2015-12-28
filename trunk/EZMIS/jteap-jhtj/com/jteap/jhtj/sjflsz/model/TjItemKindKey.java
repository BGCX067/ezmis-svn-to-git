package com.jteap.jhtj.sjflsz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 
 *描述：关键字
 *时间：2010-4-8
 *作者：童贝
 *
 */
@Entity
@Table(name="TJ_ITEMKINDKEY")
public class TjItemKindKey {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="kid")
	private String kid;//分类编码
	@Column(name="icode")
	private String icode;//关键字编码

	@Column(name="iname")
	private String iname;//关键字名称
	@Column(name="lx")
	private Long lx;//类型,数据类型：1－整数型、2－字符型，3－日期型；
	@Column(name="cd")
	private Long cd;//长度
	@Column(name="iorder")
	private Long iorder;//顺序号
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getIcode() {
		return icode;
	}
	public void setIcode(String icode) {
		this.icode = icode;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public Long getLx() {
		return lx;
	}
	public void setLx(Long lx) {
		this.lx = lx;
	}
	public Long getCd() {
		return cd;
	}
	public void setCd(Long cd) {
		this.cd = cd;
	}
	public Long getIorder() {
		return iorder;
	}
	public void setIorder(Long iorder) {
		this.iorder = iorder;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
}
