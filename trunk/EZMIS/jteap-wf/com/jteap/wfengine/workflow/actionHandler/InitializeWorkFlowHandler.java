package com.jteap.wfengine.workflow.actionHandler;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.Variable;
import com.jteap.wfengine.workflow.util.WFConstants;


/**
 * 初始化工作流,流程实例启动时调用
 * 主要用来初始化系统变量
 * 系统变量包括
 * 
 * 起草人：		creator
 * 起草时间: 		creatdt
 * 下一个环节：	nextNodeName
 * 下一个环节处理人:  nextActors
 * 上一个环节：	prevNodeName
 * 上一环节处理人：prevNodeActors
 * 流程名称：		flowName
 * 流程实例编号：	pid
 * 流程版本：		ver
 * 业务数据ID		docid
 * 
 * @author MrBao
 * 
 *
 */
public class InitializeWorkFlowHandler implements ActionHandler {

	private static final long serialVersionUID = 1094789881747533788L;
	
	public void execute(ExecutionContext context) throws Exception {
		
		FlowConfigManager flowConfigManager =  (FlowConfigManager)SpringContextUtil.getBean("flowConfigManager");
		
		//此处初始化流程变量值
		int iVersion = context.getProcessDefinition().getVersion();
		String workFlowNm = context.getProcessDefinition().getName();
		
		FlowConfig flowConfig = flowConfigManager.getFlowConfigByNmAndVersion(iVersion, workFlowNm);
		
		for(Variable variable :flowConfig.getFlowVariables()){
			context.getContextInstance().setVariable(variable.getName(),variable.getRealValue());	//此处加空字符串初始化流程变量的值，以确保为字符串
		}
		
		context.getContextInstance().setVariable(WFConstants.WF_VAR_FLOWNAME, flowConfig.getName());
		
	}

}
