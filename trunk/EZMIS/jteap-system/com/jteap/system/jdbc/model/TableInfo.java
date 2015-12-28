package com.jteap.system.jdbc.model;

/**
 * 物理表模型对象
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings("unused")
public class TableInfo {
	private String tableName;	//表名
	private String schema;		//schema

	private String fullName;	//全名
	private String id;
	

	public String getId() {
		return schema+"."+tableName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return schema+"."+tableName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

}
