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
 * 小指标表定义
 * @author caihuiwen
 */
@Entity
@Table(name = "TB_YX_DIRECTIVE_TABLEINFO")
public class DirectiveTableInfo{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	@Column(name = "TABLE_CODE")	
	private java.lang.String tableCode;
	
	@Column(name = "TABLE_NAME")	
	private java.lang.String tableName;
	
	@Column(name = "REMARK")	
	private java.lang.String remark;
	
	@Column(name = "SORTNO")	
	private long sortno;
	
	@Column(name = "FINAL_MANUSCRIPT")
	private boolean finalManuscript;
	
	@Column(name = "MODIFYREC")
	private java.lang.String modifyRec;
	
	public boolean getFinalManuscript() {
		return finalManuscript;
	}
	public void setFinalManuscript(boolean finalManuscript) {
		this.finalManuscript = finalManuscript;
	}
	public java.lang.String getModifyRec() {
		return modifyRec;
	}
	public void setModifyRec(java.lang.String modifyRec) {
		this.modifyRec = modifyRec;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
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
	public java.lang.String getRemark() {
		return this.remark;
	}
	
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public long getSortno() {
		return this.sortno;
	}
	
	public void setSortno(long value) {
		this.sortno = value;
	}
	
}
