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
 * 运行日志指标信息
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_LOGS_COLUMNINFO")
public class LogsColumnInfo{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "TABLE_ID")	
	private java.lang.String tableId;
	
	@Column(name = "COLUMN_CODE")	
	private java.lang.String columnCode;
	
	@Column(name = "COLUMN_NAME")	
	private java.lang.String columnName;
	
	@Column(name = "EDINGZHI")	
	private java.lang.String edingzhi;
	
	@Column(name = "JILIANGDANWEI")	
	private java.lang.String jiliangdanwei;
	
	@Column(name = "SIS_CEDIANBIANMA")	
	private java.lang.String sisCedianbianma;
	
	@Column(name = "SORTNO")	
	private long sortno;
	
	@Column(name = "DATA_TABLECODE")	
	private java.lang.String dataTableCode;
	
	@Column(name = "JIZU")
	private java.lang.String jizu;
	
	public java.lang.String getJizu() {
		return jizu;
	}
	public void setJizu(java.lang.String jizu) {
		this.jizu = jizu;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getTableId() {
		return this.tableId;
	}
	
	public void setTableId(java.lang.String value) {
		this.tableId = value;
	}
	public java.lang.String getColumnCode() {
		return this.columnCode;
	}
	
	public void setColumnCode(java.lang.String value) {
		this.columnCode = value;
	}
	public java.lang.String getColumnName() {
		return this.columnName;
	}
	
	public void setColumnName(java.lang.String value) {
		this.columnName = value;
	}
	public java.lang.String getEdingzhi() {
		return this.edingzhi;
	}
	
	public void setEdingzhi(java.lang.String value) {
		this.edingzhi = value;
	}
	public java.lang.String getJiliangdanwei() {
		return this.jiliangdanwei;
	}
	
	public void setJiliangdanwei(java.lang.String value) {
		this.jiliangdanwei = value;
	}
	public java.lang.String getSisCedianbianma() {
		return this.sisCedianbianma;
	}
	
	public void setSisCedianbianma(java.lang.String value) {
		this.sisCedianbianma = value;
	}
	public long getSortno() {
		return this.sortno;
	}
	
	public void setSortno(long value) {
		this.sortno = value;
	}
	public java.lang.String getDataTableCode() {
		return dataTableCode;
	}
	public void setDataTableCode(java.lang.String dataTableCode) {
		this.dataTableCode = dataTableCode;
	}
	
}
