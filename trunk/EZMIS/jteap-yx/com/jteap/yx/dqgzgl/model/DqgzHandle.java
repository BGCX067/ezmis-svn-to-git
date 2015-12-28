/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 定期工作处理
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_DQGZ_HANDLE")
public class DqgzHandle {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")	
	private java.lang.String id;
	
	@Column(name = "DQGZ_SET_ID")	
	private java.lang.String dqgzSetId;
	
	@Column(name = "FZBM")	
	private java.lang.String fzbm;
	
	@Column(name = "FZGW")
	private java.lang.String fzgw;
	
	@Column(name = "GZGL")	
	private java.lang.String gzgl;
	
	@Column(name = "DQGZ_ZY")	
	private java.lang.String dqgzzy;
	
	@Column(name = "BC")	
	private java.lang.String bc;
	
	@Column(name = "DQGZ_MC")	
	private java.lang.String dqgzMc;
	
	@Column(name = "DQGZ_NR")	
	private java.lang.String dqgzNr;
	
	@Column(name = "DQGZ_FL")
	private java.lang.String dqgzFl;
	
	@Column(name = "DQGZ_CREATE_DT")
	private java.util.Date dqgzCreateDt;
	
	@Column(name = "CHULI_REN")
	private java.lang.String chuliRen;
	
	@Column(name = "CHULI_NR")
	private java.lang.String chuliNr;
	
	@Column(name = "CHULI_DT")
	private java.util.Date chuliDt;
	
	@Column(name = "STATUS")
	private java.lang.String status;
	
	@Column(name = "TASKID")
	private java.lang.String taskId;
	
	public java.lang.String getTaskId() {
		return taskId;
	}

	public void setTaskId(java.lang.String taskId) {
		this.taskId = taskId;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getDqgzSetId() {
		return dqgzSetId;
	}

	public void setDqgzSetId(java.lang.String dqgzSetId) {
		this.dqgzSetId = dqgzSetId;
	}

	public java.lang.String getFzbm() {
		return fzbm;
	}

	public void setFzbm(java.lang.String fzbm) {
		this.fzbm = fzbm;
	}

	public java.lang.String getFzgw() {
		return fzgw;
	}

	public void setFzgw(java.lang.String fzgw) {
		this.fzgw = fzgw;
	}

	public java.lang.String getGzgl() {
		return gzgl;
	}

	public void setGzgl(java.lang.String gzgl) {
		this.gzgl = gzgl;
	}

	public java.lang.String getDqgzzy() {
		return dqgzzy;
	}

	public void setDqgzzy(java.lang.String dqgzzy) {
		this.dqgzzy = dqgzzy;
	}

	public java.lang.String getBc() {
		return bc;
	}

	public void setBc(java.lang.String bc) {
		this.bc = bc;
	}

	public java.lang.String getDqgzMc() {
		return dqgzMc;
	}

	public void setDqgzMc(java.lang.String dqgzMc) {
		this.dqgzMc = dqgzMc;
	}

	public java.lang.String getDqgzNr() {
		return dqgzNr;
	}

	public void setDqgzNr(java.lang.String dqgzNr) {
		this.dqgzNr = dqgzNr;
	}

	public java.lang.String getDqgzFl() {
		return dqgzFl;
	}

	public void setDqgzFl(java.lang.String dqgzFl) {
		this.dqgzFl = dqgzFl;
	}

	public java.util.Date getDqgzCreateDt() {
		return dqgzCreateDt;
	}

	public void setDqgzCreateDt(java.util.Date dqgzCreateDt) {
		this.dqgzCreateDt = dqgzCreateDt;
	}

	public java.lang.String getChuliRen() {
		return chuliRen;
	}

	public void setChuliRen(java.lang.String chuliRen) {
		this.chuliRen = chuliRen;
	}

	public java.lang.String getChuliNr() {
		return chuliNr;
	}

	public void setChuliNr(java.lang.String chuliNr) {
		this.chuliNr = chuliNr;
	}

	public java.util.Date getChuliDt() {
		return chuliDt;
	}

	public void setChuliDt(java.util.Date chuliDt) {
		this.chuliDt = chuliDt;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	
}
