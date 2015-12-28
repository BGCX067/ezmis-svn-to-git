package com.jteap.dgt.fjdgztj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
/**
 * 通知专栏实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_FORM_TZZL")
@SuppressWarnings("unchecked")
public class Tzzl {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="TZBT")
	private String title;
	
	@Column(name="TZNR")
	private String content;
	
	@Column(name="SCR")
	private String SCR;
	
	@JoinColumn(name="SCSJ")
	private String scsj;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSCR() {
		return SCR;
	}

	public void setSCR(String scr) {
		SCR = scr;
	}
	 
	public String getCreateDtStr() {
		return scsj.replace("年", "-").replace("月","-").replace("日","");
	}
	public String getScsj() {
		return scsj.replace("年", "-").replace("月","-").replace("日","");
	}

	public void setScsj(String scsj) {
		this.scsj = scsj;
	}
	public String getCreateDtStrNy(){
		return scsj.split("年")[1].replace("月",".").replace("日","");
	}
}
