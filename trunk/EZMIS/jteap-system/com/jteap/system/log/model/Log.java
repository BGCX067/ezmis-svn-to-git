package com.jteap.system.log.model;

import java.io.Serializable;
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
 * 日志的Model
 * @author 朱启亮
 * @date 2008-1-15
 */
@Entity
@Table(name="tb_sys_logs")
public class Log {	
	//日志类型标识【登录，逻辑，异常】
	public final static String LOG_TYPE_LOGIN="1";
	public final static String LOG_TYPE_LOGIC="2";
	public final static String LOG_TYPE_EXCEPTION="3";
	
	public Log(){}
	public Log(Serializable id,Date dt,String type,String personLoginName,String personName,String ip){
		this.id=id;
		this.dt=dt;
		this.type=type;
		this.personLoginName=personLoginName;
		this.personName=personName;
		this.ip=ip;
	}
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private Serializable id;		// 日志ID
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="LOG", columnDefinition="CLOB", nullable=true) 
	private String log;				// 日志内容
	
	@Column(name="dt")
	private Date dt;				//日期
	
	@Column(name="LOGTYPE")
	private String type;			// 日志类型
	
	@Column(name="personloginname")
	private String personLoginName;	// 用户帐号
	
	@Column(name="personname")
	private String personName;		// 用户名（此属性在用户对象中存在，这里设置是为减少数据库查询量）

	

	@Column(name="ip")
	private String ip;	//操作ip
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonLoginName() {
		return personLoginName;
	}

	public void setPersonLoginName(String personLoginName) {
		this.personLoginName = personLoginName;
	}

}
