package com.jteap.form.dbdef.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;



/**
 * 表定义
 * @author tanchang
 *
 */
@Entity
@Table(name = "TB_FORM_DEF_TABLEINFO")
public class DefTableInfo {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type="string")
	private String id;
	
	@Column(name="TABLE_CODE")
	private String tableCode;
	
	@Column(name="TABLE_NAME")
	private String tableName;
	
	@Column(name="TABLE_SCHEMA")
	private String schema;
	
	@Column(name="COMM")
	private String comm;
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="MODIFYREC", columnDefinition="CLOB", nullable=true) 
	private String modifyRec;
	
	@OneToMany(mappedBy="table")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="COLUMN_ORDER")
	private Set<DefColumnInfo> columns = new HashSet<DefColumnInfo>();
	
	@ManyToOne()
	@JoinColumn(name="PARENTID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DefTableInfo parent;
	
	@OneToMany(mappedBy="parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause="TABLE_NAME")
	private Set<DefTableInfo> children;
	
	public Set<DefColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(Set<DefColumnInfo> columns) {
		this.columns = columns;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComm() {
		return comm;
	}
	public void setComm(String comm) {
		this.comm = comm;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String toString() {
		return "表定义:["+this.schema+"."+this.tableCode+"("+this.tableName+")]";
	}
	public String getModifyRec() {
		return modifyRec;
	}
	public void setModifyRec(String modifyRec) {
		this.modifyRec = modifyRec;
	}
	public DefTableInfo getParent() {
		return parent;
	}
	public void setParent(DefTableInfo parent) {
		this.parent = parent;
	}
	public Set<DefTableInfo> getChildren() {
		return children;
	}
	public void setChildren(Set<DefTableInfo> children) {
		this.children = children;
	}

}
