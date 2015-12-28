package com.jteap.lp.pkgl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 两票-票库管理表实体bean
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_LP_LPPK")
public class Pkgl {

	// 编号
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 票种分类
	@Column(name = "PFL")
	private String pfl;

	// 票名称
	@Column(name = "PMC")
	private String pmc;

	// 创建部门
	@Column(name = "CJBM")
	private String cjbm;

	// 创建人
	@Column(name = "CJR")
	private String cjr;

	// 创建时间
	@Column(name = "CJSJ")
	private Date cjsj;

	// 票状态
	@Column(name = "PZT")
	private String pzt;

	// 是否标准票
	@Column(name = "ISBZP")
	private String isBzp;

	// 关联流程号
	@Column(name = "GLLCH")
	private String gllch;

	// 备注
	@Column(name = "BZSM")
	private String bzsm;

	// 内码
	@Column(name = "NM")
	private String nm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPfl() {
		return pfl;
	}

	public void setPfl(String pfl) {
		this.pfl = pfl;
	}

	public String getPmc() {
		return pmc;
	}

	public void setPmc(String pmc) {
		this.pmc = pmc;
	}

	public String getCjbm() {
		return cjbm;
	}

	public void setCjbm(String cjbm) {
		this.cjbm = cjbm;
	}

	public String getCjr() {
		return cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getPzt() {
		return pzt;
	}

	public void setPzt(String pzt) {
		this.pzt = pzt;
	}

	public String getIsBzp() {
		return isBzp;
	}

	public void setIsBzp(String isBzp) {
		this.isBzp = isBzp;
	}

	public String getGllch() {
		return gllch;
	}

	public void setGllch(String gllch) {
		this.gllch = gllch;
	}

	public String getBzsm() {
		return bzsm;
	}

	public void setBzsm(String bzsm) {
		this.bzsm = bzsm;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

}
