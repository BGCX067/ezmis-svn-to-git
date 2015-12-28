package com.jteap.wfengine.workflow.util;

/**
 * 工作流相关常量
 * @author tanchang
 *
 */
public class WFConstants {

	/**
	 * JBPM事件处理类
	 */
	public static final String WORKFLOW_COUNTERSIGN_CLASS = "com.jteap.wfengine.workflow.actionHandler.CountersignActionHandler";
	public static final String WORKFLOW_PROCESS_START_CLASS = "com.jteap.wfengine.workflow.actionHandler.InitializeWorkFlowHandler";
	public static final String WORKFLOW_TASK_START_CLASS = "com.jteap.wfengine.workflow.actionHandler.TaskStartEventHandler";
	public static final String WORKFLOW_TASK_END_CLASS = "com.jteap.wfengine.workflow.actionHandler.TaskEndEventHandler";
	
	public static final String WORKFLOW_INIT_NODE_CLASS = "com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler";
	public static final String WORKFLOW_LEAVE_NODE_CLASS = "com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler";
	//多人模式处理，判断是否会签节点会签
	public static final String WORKFLOW_PORCESS_KIND_MULTI = "multi";
	//节点任务执行者变量
	public static final String WORKFLOW_NODE_ACTORID_NAME = "NodeActorId";
	
	
	//流程变量
	public static final String WF_VAR_CREATOR = "creator";		//流程创建人
	public static final String WF_VAR_CREATDT = "creatdt";		//流程创建时间
	public static final String WF_VAR_DOCID = "docid";			//业务数据编号
	public static final String WF_VAR_FLOWNAME = "flowName";			//工作流名称
	public static final String WF_VAR_LOGINNAME = "userLoginName";	//当前登录用户名
	
	public static final String WF_STARTSTATE = "流程开始";
	public static final String WF_ENDSTATE = "流程结束";
	
	//流程环节
	public static final String WF_NODE_SHOW = "显示";
	public static final String WF_NODE_NOTSHOW = "不显示";
}
