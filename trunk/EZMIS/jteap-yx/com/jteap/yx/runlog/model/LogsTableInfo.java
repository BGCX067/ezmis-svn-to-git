/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.yx.runlog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 运行日志报表
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_LOGS_TABLEINFO")
public class LogsTableInfo{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "LOG_CATALOG_ID")	
	private java.lang.String logCatalogId;
	
	@Column(name = "LOG_CATALOG_NAME")	
	private java.lang.String logCatalogName;
	
	@Column(name = "TABLE_CODE")	
	private java.lang.String tableCode;
	
	@Column(name = "TABLE_NAME")	
	private java.lang.String tableName;
	
	@Column(name = "SORTNO")	
	private long sortno;
	
	@Column(name = "CAIYANGDIAN")	
	private java.lang.String caiyangdian;
	
	@Column(name = "REMARK")	
	private java.lang.String remark;
	
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getLogCatalogId() {
		return this.logCatalogId;
	}
	
	public void setLogCatalogId(java.lang.String value) {
		this.logCatalogId = value;
	}
	public java.lang.String getTableCode() {
		return this.tableCode;
	}
	
	public void setTableCode(java.lang.String value) {
		this.tableCode = value;
	}
	public java.lang.String getTableName() {
		return this.tableName;
	}
	
	public void setTableName(java.lang.String value) {
		this.tableName = value;
	}
	public long getSortno() {
		return this.sortno;
	}
	
	public void setSortno(long value) {
		this.sortno = value;
	}
	public java.lang.String getCaiyangdian() {
		return this.caiyangdian;
	}
	
	public void setCaiyangdian(java.lang.String value) {
		this.caiyangdian = value;
	}
	public java.lang.String getRemark() {
		return remark;
	}
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	public java.lang.String getLogCatalogName() {
		return logCatalogName;
	}
	public void setLogCatalogName(java.lang.String logCatalogName) {
		this.logCatalogName = logCatalogName;
	}
	
}
