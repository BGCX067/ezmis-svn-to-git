package com.jteap.wfengine.workflow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 流程跟踪日志实体类
 *@filename   WorkFlowLog.java
 *@author       MrBao
 *@date  	        2009-7-8
 *@remark
 */
@Entity  
@Table(name="tb_wf_log")
public class WorkFlowLog {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;//编号
	
	@Column(name="PI_ID")
	private String pi_id;//流程实例编号
	
	@Column(name="TASK_NAME")
	private String taskName;//任务名称
	
	@Column(name="TASK_ACTOR")
	private String taskActor;//任务执行者
	
	@Column(name="TASK_LOGIN_NAME")
	private String taskLoginName; // 任务执行者登录名
	
	@Column(name="TASK_RESULT")
	private String taskResult;//处理结果
	
	@Column(name="NEXT_TASK_NAME")
	private String nextTaksName;//下一任务名
	
	@Column(name="NEXT_TASK_ACTOR")
	private String nextTaksActor;//下一任务执行者
	
	@Column(name="PROCESS_DATE")
	private Date porcessDate;//下一任务名
	
	@Column(name="REMARK")
	private String remark;//备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPi_id() {
		return pi_id;
	}

	public void setPi_id(String pi_id) {
		this.pi_id = pi_id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskActor() {
		return taskActor;
	}

	public void setTaskActor(String taskActor) {
		this.taskActor = taskActor;
	}

	public String getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}

	public String getNextTaksName() {
		return nextTaksName;
	}

	public void setNextTaksName(String nextTaksName) {
		this.nextTaksName = nextTaksName;
	}

	public String getNextTaksActor() {
		return nextTaksActor;
	}

	public void setNextTaksActor(String nextTaksActor) {
		this.nextTaksActor = nextTaksActor;
	}

	public Date getPorcessDate() {
		return porcessDate;
	}

	public void setPorcessDate(Date porcessDate) {
		this.porcessDate = porcessDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTaskLoginName() {
		return taskLoginName;
	}

	public void setTaskLoginName(String taskLoginName) {
		this.taskLoginName = taskLoginName;
	}
	
}
