package com.jteap.form.dbdef.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;


/**
 * 字段定义
 * @author tanchang
 *
 */
@Entity
@Table(name="TB_FORM_DEF_COLUMNINFO")
public class DefColumnInfo {

	private static final long serialVersionUID = -6974233825502423266L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type="string")
	private String id;					// 编号
	
	@ManyToOne()
	@JoinColumn(name="TABLE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private DefTableInfo table;
	
	
	@Column(name = "COLUMN_CODE")
	private String columncode;			//列名
	
	@Column(name = "COLUMN_NAME")
	private String columnname;			//列中文名称
	
	@Column(name = "COLUMN_TYPE")
	private String columntype;			//类型
	
	@Column(name = "COLUMN_LENGTH")
	private int columnlength;			//长度
	
	@Column(name = "DEFAULT_VALUE")
	private String defaultvalue;		//默认值
	
	@Column(name = "ALLOW_NULL")
	private boolean allownull;			//是否允许为空
	
	@Column(name = "COMM")
	private String comm;				//备注
	
	@Column(name ="COLUMN_ORDER")
	private long columnorder;			//字段排序号
	
	@Column(name ="COLUMN_PREC")
	private int columnprec;				//小数位数
	
	@Column(name ="IS_PK")
	private boolean pk;				//是否主键
	
	@Column(name ="FORMAT")
	private String format;			//显示格式
	
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean getAllownull() {
		return allownull;
	}

	public void setAllownull(boolean allownull) {
		this.allownull = allownull;
	}



	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public DefTableInfo getTable() {
		return table;
	}

	public void setTable(DefTableInfo table) {
		this.table = table;
	}

	public String getColumncode() {
		return columncode;
	}

	public void setColumncode(String columncode) {
		this.columncode = columncode;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getColumntype() {
		return columntype;
	}

	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}

	public int getColumnlength() {
		return columnlength;
	}

	public void setColumnlength(int columnlength) {
		this.columnlength = columnlength;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getComm() {
		return comm;
	}

	public void setComm(String comm) {
		this.comm = comm;
	}

	public long getColumnorder() {
		return columnorder;
	}

	public void setColumnorder(long columnorder) {
		this.columnorder = columnorder;
	}

	public int getColumnprec() {
		return columnprec;
	}

	public void setColumnprec(int columnprec) {
		this.columnprec = columnprec;
	}


}
