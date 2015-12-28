package com.jteap.system.iplock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * IP封锁 对象
 * @author tanchang
 *
 */
@Entity  
@Table(name="TB_sys_IPLOCK")
@SuppressWarnings("unchecked")
public class IPLock { 
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;	//编号
	
	@Column(name="STARTIP")
	private String startIp;	//开始IP
	
	@Column(name="ENDIP")
	private String endIp;		//结束IP
	
	@Column(name="RULE")
	private String rule;	//规则 1允许 0阻止
	
	@Column(name="COMM")
	private String comm;	//备注
	
	
	public static String IP_RULE_DIE = "0";	//阻止
	public static String IP_RULE_ALLOW = "1";//允许
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getComm() {
		return comm;
	}

	public void setComm(String comm) {
		this.comm = comm;
	}
	

}
