package com.jteap.wfengine.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * 环节变量对象，用于处理流程各环节的设置
 *@filename   NodeVariable.java
 *@author       MrBao
 *@date  	        2009-7-8
 *@remark
 */
@Entity  
@Table(name="tb_wf_node_var")
public class NodeVariable {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;//编号
	
	@ManyToOne()
	@JoinColumn(name="NODE_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private NodeConfig nodeConfig;//节点编号
	
	@ManyToOne()
	@JoinColumn(name="VAR_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Variable variable;//变量编号
	
	@Column(name="VAR_VALUE")
	private String value;//变量值
	
	@Column(name="ISNEED")
	private boolean isNeed;//是否必填

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isNeed() {
		return isNeed;
	}

	public void setNeed(boolean isNeed) {
		this.isNeed = isNeed;
	}

	public NodeConfig getNodeConfig() {
		return nodeConfig;
	}

	public void setNodeConfig(NodeConfig nodeConfig) {
		this.nodeConfig = nodeConfig;
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}
	
}
