package com.jteap.form.eform.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 自定义表单附件表
 * @author tanchang
 *
 */
@Entity  
@Table(name="TB_FORM_EFORM_FJ")
@SuppressWarnings("unchecked")
public class EFormFj {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;	//编号
	
	@Column(name="FJ_NAME")
	private String name;	//标题
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "FJ_CONTENT", columnDefinition = "BLOB",nullable=true) 
	private byte[] content;	//附件内容
	
	@Column(name="FJ_TYPE")
	private String type;	//附件类型 
	
	@Column(name="FJ_CREATOR")
	private String creator;	//创建人
	
	@Column(name="FJ_CREATDT")
	private Date dt;		//创建时间
	
	@Column(name="FJ_PATH")
	private String path;	//文件存储路径
	
	@Column(name="FJ_DOCID")
	private String docid;	//所属主表外键
	
	@Column(name="FJ_ST")
	private String st;	//附件存储方案 DB-数据库?  FS-文件系统
	
	@Column(name="FJ_SIZE")
	private long size;	//附件大小 单位：字节

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
