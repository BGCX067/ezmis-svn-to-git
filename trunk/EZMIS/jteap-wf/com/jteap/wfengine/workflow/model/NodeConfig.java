package com.jteap.wfengine.workflow.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * 环节配置实体
 * 
 * @filename NodeConfig.java
 * @author MrBao
 * @date 2009-7-8
 * @remark
 */
@Entity
@Table(name = "tb_wf_node_config")
public class NodeConfig {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;// 编号

	@Column(name = "NODE_NAME")
	private String name;// 节点名

	@Column(name = "NODE_DESCRIPTION")
	private String description;// 节点说明

	@Column(name = "NODE_PROCESS_URL")
	private String process_url;// 处理页面url

	@Column(name = "NODE_PROCESS_FORM_ID")
	private String process_form_id;// 处理页面的ID

	@ManyToOne()
	@JoinColumn(name = "FLOWCONFIG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	private FlowConfig flowConfig;// 流程配置对象

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PERM_ID", referencedColumnName = "id", unique = true)
	private NodePermission nodePermission;// 权限对象

	@OneToMany(mappedBy = "nodeConfig")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@JoinColumn(name = "NODE_ID")
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<NodeVariable> nodeVariables;// 节点变量集合

	@OneToMany(mappedBy = "nodeConfig")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@JoinColumn(name = "NODE_ID")
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<NodeOperation> nodeOperations;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcess_url() {
		return process_url;
	}

	public void setProcess_url(String process_url) {
		this.process_url = process_url;
	}

	public FlowConfig getFlowConfig() {
		return flowConfig;
	}

	public void setFlowConfig(FlowConfig flowConfig) {
		this.flowConfig = flowConfig;
	}

	public NodePermission getNodePermission() {
		return nodePermission;
	}

	public void setNodePermission(NodePermission nodePermission) {
		this.nodePermission = nodePermission;
	}

	public Set<NodeVariable> getNodeVariables() {
		return nodeVariables;
	}

	public void setNodeVariables(Set<NodeVariable> nodeVariables) {
		this.nodeVariables = nodeVariables;
	}

	public Set<NodeOperation> getNodeOperations() {
		return nodeOperations;
	}

	public void setNodeOperations(Set<NodeOperation> nodeOperations) {
		this.nodeOperations = nodeOperations;
	}

	public String getProcess_form_id() {
		return process_form_id;
	}

	public void setProcess_form_id(String process_form_id) {
		this.process_form_id = process_form_id;
	}

}
