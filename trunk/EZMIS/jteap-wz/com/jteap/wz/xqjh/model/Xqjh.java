package com.jteap.wz.xqjh.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;

/**
 * 描述 : 需求计划实体 作者 : caofei 时间 : Oct 21, 2010 参数 : 返回值 : 异常 :
 */
@Entity
@Table(name = "TB_WZ_XQJH")
public class Xqjh {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private java.lang.String id;

	@Column(name = "XQJHSQBH")
	private String xqjhsqbh; // 需求计划申请编号
	
	@Column(name = "SXSJ")
	private Date sxsj; // 生效时间

	@Column(name = "GCLB")
	private String gclb; // 工程类别

	@Column(name = "GCXM")
	private String gcxm; // 工程项目

	@Column(name = "SQBM")
	private String sqbm; // 申请部门

	@Column(name = "SQSJ")
	private Date sqsj; // 申请时间

	@Column(name = "STATUS")
	private String status; // 状态

	@Column(name = "OPERATOR")
	private String operator; // 操作人

	@Column(name = "XQJHBH")
	private String xqjhbh; // 需求计划编号

	// 所有条目明细
	@OneToMany(mappedBy = "xqjh")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<XqjhDetail> xqjhDetail; // 需求计划明细
	
	@Column(name="DYSZT")
	private String dyszt;
	
	@Column(name="DYSCZR")
	private String dysczr;

	public Set<XqjhDetail> getXqjhDetail() {
		return xqjhDetail;
	}

	public void setXqjhDetail(Set<XqjhDetail> xqjhDetail) {
		this.xqjhDetail = xqjhDetail;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Date getSxsj() {
		return sxsj;
	}

	public void setSxsj(Date sxsj) {
		this.sxsj = sxsj;
	}

	public String getGclb() {
		return gclb;
	}

	public void setGclb(String gclb) {
		this.gclb = gclb;
	}

	public String getGcxm() {
		return gcxm;
	}

	public void setGcxm(String gcxm) {
		this.gcxm = gcxm;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public Date getSqsj() {
		return sqsj;
	}

	public void setSqsj(Date sqsj) {
		this.sqsj = sqsj;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getXqjhbh() {
		return xqjhbh;
	}

	public void setXqjhbh(String xqjhbh) {
		this.xqjhbh = xqjhbh;
	}

	public Xqjhsq getXqjhsq(){
		XqjhsqManager xqjhsqManager = (XqjhsqManager)SpringContextUtil.getBean("xqjhsqManager");
		return xqjhsqManager.findUniqueBy("id",this.xqjhsqbh);
	}
	
	public String getXqjhsqbh() {
		return xqjhsqbh;
	}

	public void setXqjhsqbh(String xqjhsqbh) {
		this.xqjhsqbh = xqjhsqbh;
	}
	public String getDyszt() {
		return dyszt;
	}

	public void setDyszt(String dyszt) {
		this.dyszt = dyszt;
	}

	public String getDysczr() {
		return dysczr;
	}

	public void setDysczr(String dysczr) {
		this.dysczr = dysczr;
	}
}
