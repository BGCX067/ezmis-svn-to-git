package com.jteap.wfengine.workflow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity  
@Table(name="tb_wf_work_entrust")
public class WorkEntrust {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;//编号
	
	@Column(name="WE_CREATE_DATE")
	private Date createDate;//创建时间
	
	@Column(name="WE_CREATE_MAN")
	private String createMan;//创建人
	
	@Column(name="WE_TITLE")
	private String title;//标题
	
	@Column(name="WE_SYS_NAME")
	private String sysName;//工作来源系统名

	@Column(name="WE_PROECSS_MAN")
	private String processMan;//处理人
	
	@Column(name="WE_PROXY_MAN")
	private String proxyMan;//代办人
	
	@Column(name="WE_PROCESSINSTANCE_ID")
	private String pi_id;//流程实例编号
	
	@Column(name="WE_FLOW_VER")
	private int workFlowVerId;//流程版本号
	
	@Column(name="WE_YW_ID")
	private String yw_id;//业务数据编号
	
	@Column(name="PDID")
	private String pd_id;//流程定义编号

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getProcessMan() {
		return processMan;
	}

	public void setProcessMan(String processMan) {
		this.processMan = processMan;
	}

	public String getProxyMan() {
		return proxyMan;
	}

	public void setProxyMan(String proxyMan) {
		this.proxyMan = proxyMan;
	}

	public String getPi_id() {
		return pi_id;
	}

	public void setPi_id(String pi_id) {
		this.pi_id = pi_id;
	}

	public int getWorkFlowVerId() {
		return workFlowVerId;
	}

	public void setWorkFlowVerId(int workFlowVerId) {
		this.workFlowVerId = workFlowVerId;
	}

	public String getYw_id() {
		return yw_id;
	}

	public void setYw_id(String yw_id) {
		this.yw_id = yw_id;
	}

	public String getPd_id() {
		return pd_id;
	}

	public void setPd_id(String pd_id) {
		this.pd_id = pd_id;
	}
	
	
}
