/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.fdzbfxykh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 小指标字段定义
 * @author caihuinwen
 */
@Entity
@Table(name = "TB_YX_DIRECTIVE_COLUMNINFO")
public class DirectiveColumnInfo{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "DIRECTIVE_ID")	
	private java.lang.String directiveId;
	
	@Column(name = "DIRECTIVE_CODE")	
	private java.lang.String directiveCode;
	
	@Column(name = "DIRECTIVE_NAME")	
	private java.lang.String directiveName;
	
	//取数编码
	@Column(name = "DATA_TABLE")	
	private java.lang.String dataTable;
	
	//取数规则
	@Column(name = "SIS_CEDIANBIANMA")	
	private java.lang.String sisCedianbianma;
	
	@Column(name = "REMARK")	
	private java.lang.String remark;
	
	@Column(name = "SORTNO")
	private java.lang.String sortno;
	
	@Column(name = "SUM_OR_AVG")
	private java.lang.String sumOrAvg;

	public java.lang.String getSumOrAvg() {
		return sumOrAvg;
	}
	public void setSumOrAvg(java.lang.String sumOrAvg) {
		this.sumOrAvg = sumOrAvg;
	}
	public java.lang.String getSortno() {
		return sortno;
	}
	public void setSortno(java.lang.String sortno) {
		this.sortno = sortno;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getDirectiveId() {
		return this.directiveId;
	}
	
	public void setDirectiveId(java.lang.String value) {
		this.directiveId = value;
	}
	public java.lang.String getDirectiveCode() {
		return this.directiveCode;
	}
	
	public void setDirectiveCode(java.lang.String value) {
		this.directiveCode = value;
	}
	public java.lang.String getDirectiveName() {
		return this.directiveName;
	}
	
	public void setDirectiveName(java.lang.String value) {
		this.directiveName = value;
	}
	public java.lang.String getDataTable() {
		return this.dataTable;
	}
	
	public void setDataTable(java.lang.String value) {
		this.dataTable = value;
	}
	public java.lang.String getSisCedianbianma() {
		return this.sisCedianbianma;
	}
	
	public void setSisCedianbianma(java.lang.String value) {
		this.sisCedianbianma = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
}
