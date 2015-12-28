package com.jteap.wfengine.workflow.actionHandler;



import java.util.HashMap;
import java.util.Map;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.jpdl.el.impl.JbpmExpressionEvaluator;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.model.NodePermission;
import com.jteap.wfengine.workflow.util.WFConstants;

/**
 *
 *初始化节点事件
 *node-enter
 */
public class InitNodeEventHandler implements ActionHandler {

	private static final long serialVersionUID = 5593827574610795242L;


	public void execute(ExecutionContext context) throws Exception {
		
		FlowConfigManager flowConfigManager =  (FlowConfigManager)SpringContextUtil.getBean("flowConfigManager");
		
		//此处初始化环节变量值
		int iVersion = context.getProcessDefinition().getVersion();
		String workFlowNm = context.getProcessDefinition().getName();
		TaskNode taskNode = (TaskNode) context.getNode();   
		Task task = (Task)taskNode.getTasks().iterator().next();   
		
		//设置流程相关变量到全局变量中
		configWorkflowVar(context);
		
		
		//根据版本号和流程配置内码获取流程配置对象
		FlowConfig flowConfig = flowConfigManager.getFlowConfigByNmAndVersion(iVersion, workFlowNm);
		
		if(flowConfig==null) return;
		
		//遍历所有节点配置对象
		for(NodeConfig nodeConfig :flowConfig.getNodeConfigs()){
			//是否是当前节点
			if(nodeConfig.getName().equals(taskNode.getName())){
//				//初始化或创建当前节点使用的流程变量
//				for(NodeVariable nodeVariable :nodeConfig.getNodeVariables()){	
//					context.getContextInstance().setVariable(nodeVariable.getVariable().getName(),nodeVariable.getValue());
//				}
				
				NodePermission nodePermission = nodeConfig.getNodePermission();
				
				//当前节点是会签节点时
				if(nodePermission != null && nodePermission.getProcessKind().equals(WFConstants.WORKFLOW_PORCESS_KIND_MULTI)){
					
			        String actorIds = (String) JbpmExpressionEvaluator.evaluate(task.getPooledActorsExpression(),
							new ExecutionContext(context.getProcessInstance().getRootToken()));
			        
			        context.getContextInstance().setVariable(taskNode.getName() +"processMode", nodePermission.getProcessMode());
			        context.getContextInstance().setVariable(taskNode.getName() +"processKind", nodePermission.getProcessKind());
			        context.getContextInstance().setVariable(WFConstants.WORKFLOW_NODE_ACTORID_NAME, actorIds);
			        
					dynamicCreateTaskInstance(context,nodePermission.getProcessMode(),actorIds);
				}
				break;
			}
		}
		
	}
	
	/**
	 * 设置流程相关变量
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	private void configWorkflowVar(ExecutionContext context) {
		Map vars=new HashMap();
		TaskNode taskNode = (TaskNode) context.getNode();   
		if(taskNode!=null){
			
			vars.put("curNodeName", taskNode.getName());
//			vars.put("curNodeActors", ti.getPooledActors().toString());
			
			context.getContextInstance().addVariables(vars);
		}
		
	}

	/**
	 * 动态创建任务实例
	 *@author  MrBao 
	 *@date 	  2009-7-29
	 *@param context
	 *@param sortAble  1为串行，0为并行会签
	 *@param actorIds  会签人员ids
	 *@return void
	 *@remark
	 */
	public void dynamicCreateTaskInstance(ExecutionContext context,String processMode, String actorIds){
		TaskNode taskNode = (TaskNode) context.getNode();   
	    Task task = (Task)taskNode.getTasks().iterator().next();   
	    
        Token token = context.getToken();   
        TaskMgmtInstance tmi = context.getTaskMgmtInstance();   
        
        //获取自定义的进行会签的人员
        //String  strActors= context.getContextInstance().getVariable("actors").toString();
//        String   strSortAble = context.getContextInstance().getVariable(taskNode.getName() +"sortAble").toString();
        
		String [] arrActor = actorIds.split(",");

		 //无序会签，根据会签人员，每人创建一个TaskInstance   
		if(processMode.equals("0")){
	        for(String strActor : arrActor){
	        	tmi.createTaskInstance(task, token).setPooledActors(strActor);
	        }
		}else if(processMode.equals("1")){//有序会签则只先给第一个会签人员创建任务
			tmi.createTaskInstance(task, token).setPooledActors(arrActor[0]);
			context.getContextInstance().setVariable(taskNode.getName() +"currentActorIndex", 0);
		}
	}

}
