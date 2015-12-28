package com.jteap.wfengine.tasktodo.model;

import java.io.Serializable;
import java.util.Date;

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

import com.jteap.system.person.model.Person;
import com.jteap.wfengine.workflow.model.FlowConfig;

/**
 * 待办任务实体类
 *
 * @author 肖平松	
 * @version Sep 24, 2009
 */
@Entity
@Table(name="tb_wf_todo")
public class TaskToDo {
	// 编号
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private Serializable id;
	
	//流程实例
	@Column(name="FLOW_INSTANCE_ID")
	private String flowInstance;
	
	//流程配置
	@ManyToOne()
	@JoinColumn(name="FLOW_CONFIG_ID")
	@LazyToOne(LazyToOneOption.PROXY)
	private FlowConfig flowConfig;
	
	//流程名称
	@Column(name="FLOW_NAME")
	private String flowname;  
	
	//处理人
	@Column(name="CURRENT_PROCESS_PERSON")
	private String curNodePerson;   			
	
	//发起人
	@Column(name="POST_PERSON")
	private String postPerson;
	
	 //发起时间
	@Column(name="POST_TIME")
	private Date postTime;
	
	 //当前环节
	@Column(name="CURRENT_TASKNAME")
	private String curTaskName;
	
	 //主题
	@Column(name="FLOW_TOPIC")
	private String flowTopic;
	
	 //业务数据编号
	@Column(name="DOCID")
	private String docId;
	
	 //业务数据编号
	@Column(name="FLAG")
	private Boolean flag;
	
	//当前流程实例的令牌编号
	@Column(name="TOKEN")
	private String token;
	
	// 当前签收人
	@ManyToOne
	@JoinColumn(name="CURSIGNIN")
	@LazyToOne(LazyToOneOption.PROXY)
	private Person curSignIn;
	
	//子系统名称
	@Column(name="CHILD_SYSTEM_NAME")
	private String childSystemName;
	
	//弹出窗口路径
	@Column(name="ALERT_WINDOW_URL")
	private String alertWindowUrl;
	
	//业务状态
	@Column(name="STATUS")
	private String status;
	
	//完成人
	@Column(name="DEAL_PERSON")
	private String dealPerson;
	
	//完成时间
	@Column(name="DEAL_TIME")
	private Date dealTime;
	
	public String getDealPerson() {
		return dealPerson;
	}

	public void setDealPerson(String dealPerson) {
		this.dealPerson = dealPerson;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChildSystemName() {
		return childSystemName;
	}

	public void setChildSystemName(String childSystemName) {
		this.childSystemName = childSystemName;
	}

	public String getAlertWindowUrl() {
		return alertWindowUrl;
	}

	public void setAlertWindowUrl(String alertWindowUrl) {
		this.alertWindowUrl = alertWindowUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getFlowInstance() {
		return flowInstance;
	}

	public void setFlowInstance(String flowInstance) {
		this.flowInstance = flowInstance;
	}

	public FlowConfig getFlowConfig() {
		return flowConfig;
	}

	public void setFlowConfig(FlowConfig flowConfig) {
		this.flowConfig = flowConfig;
	}

	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}

	public String getCurNodePerson() {
		return curNodePerson;
	}

	public void setCurNodePerson(String curNodePerson) {
		this.curNodePerson = curNodePerson;
	}

	public String getPostPerson() {
		return postPerson;
	}

	public void setPostPerson(String postPerson) {
		this.postPerson = postPerson;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}


	public String getFlowTopic() {
		return flowTopic;
	}

	public void setFlowTopic(String flowTopic) {
		this.flowTopic = flowTopic;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getCurTaskName() {
		return curTaskName;
	}

	public void setCurTaskName(String curTaskName) {
		this.curTaskName = curTaskName;
	}

	public Person getCurSignIn() {
		return curSignIn;
	}

	public void setCurSignIn(Person curSignIn) {
		this.curSignIn = curSignIn;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

}
