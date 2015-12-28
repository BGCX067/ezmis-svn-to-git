package com.jteap.wfengine.workflow.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wfengine.tasktodo.model.TaskToDo;

/**
 * 流程配置实体
 *@filename   FlowConfig.java
 *@author       MrBao
 *@date  	        2009-7-8
 *@remark
 */
@Entity  
@Table(name="tb_wf_flowconfig")
public class FlowConfig {
	
	/** 表单类型 01 ： EFORM */
	public final static String FORM_TYPE_EFORM = "01";
	/** 表单类型 02 ： CFORM */
	public final static String FORM_TYPE_CFORM = "02";

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;//编号
	
	@Column(name="FLOW_NAME")
	private String name;//流程名称
	
	@Type(type="text")
	@Column(name="FLOW_DEF_XML")
	private String defXml;//流程定义xml
	
	@ManyToOne()
	@JoinColumn(name="FLOW_CATALOG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private FlowCatalog flowCatalog;//分类编号
	
	@Column(name="PD_ID")
	private Long  pd_id;//jbpm流程定义编号
	
	@Column(name="FLOW_VERSION")
	private int version;//流程版本号
	
	@Column(name="FLOW_FORM_TYPE")
	private String formtype;//表单类型
	
	@Column(name="FLOW_FORM_ID")
	private String formId;
	
	@OneToMany(mappedBy="flowConfig")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="FLOWCONFIG_ID")
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<NodeConfig> nodeConfigs;//节点配置集合
	
	@OneToMany(mappedBy="flowConfig")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="FLOWCONFIG_ID")
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Variable> flowVariables;//变量集合
	
//	@OneToMany(mappedBy="flowConfig")
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//	@JoinColumn(name="FLOWCONFIG_ID")
//	@LazyCollection(LazyCollectionOption.TRUE)
//	private Set<FlowOperation> flowOperations;//操作集合
	
	@OneToMany(mappedBy="flowConfig")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="FLOW_CONFIG_ID")
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<TaskToDo> taskTodos;
	
	
	@Column(name="IS_NEW_VER")
	private boolean newVer;		//是否最新版本

	@Column(name="VALIDATED")
	private boolean validated;		//是否验证过
	
	@Column(name="FLOW_PROCESS_URL")
	private String process_url;//处理页面url
	
	@Column(name="FLOW_CREATER")
	private String creater;//创建人
	
	@Column(name="FLOW_TIME")
	private Date createrTime; //创建时间
	
	@Column(name="FLOW_DESCRIPTION")
	private String description;//流程说明  
	
	@Column(name="FLOW_NM")
	private String nm;//流程内码
	
	@Column(name="TOPIC_CF")
	private String topicCF;//主题

	public String getId() {
		return id;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
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

	public String getDefXml() {
		return defXml;
	}

	public void setDefXml(String defXml) {
		this.defXml = defXml;
	}

	public Long getPd_id() {
		return pd_id;
	}

	public void setPd_id(Long pd_id) {
		this.pd_id = pd_id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getType() {
		return formtype;
	}

	public void setType(String formtype) {
		this.formtype = formtype;
	}

	public String getFormtype() {
		return formtype;
	}

	public void setFormtype(String formtype) {
		this.formtype = formtype;
	}

	public String getProcess_url() {
		return process_url;
	}

	public void setProcess_url(String process_url) {
		this.process_url = process_url;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreaterTime() {
		return createrTime;
	}

	public void setCreaterTime(Date createrTime) {
		this.createrTime = createrTime;
	}

	public String getDescription() {
		return description;
	}

	public Set<TaskToDo> getTaskTodos() {
		return taskTodos;
	}

	public void setTaskTodos(Set<TaskToDo> taskTodos) {
		this.taskTodos = taskTodos;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<NodeConfig> getNodeConfigs() {
		return nodeConfigs;
	}

	public void setNodeConfigs(Set<NodeConfig> nodeConfigs) {
		this.nodeConfigs = nodeConfigs;
	}

//	public Set<FlowOperation> getFlowOperations() {
//		return flowOperations;
//	}
//
//	public void setFlowOperations(Set<FlowOperation> flowOperations) {
//		this.flowOperations = flowOperations;
//	}

	public FlowCatalog getFlowCatalog() {
		return flowCatalog;
	}

	public void setFlowCatalog(FlowCatalog flowCatalog) {
		this.flowCatalog = flowCatalog;
	}

	public Set<Variable> getFlowVariables() {
		return flowVariables;
	}

	public void setFlowVariables(Set<Variable> flowVariables) {
		this.flowVariables = flowVariables;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public boolean isNewVer() {
		return newVer;
	}

	public void setNewVer(boolean newVer) {
		this.newVer = newVer;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public String getTopicCF() {
		return topicCF;
	}

	public void setTopicCF(String topicCF) {
		this.topicCF = topicCF;
	}


	
}
