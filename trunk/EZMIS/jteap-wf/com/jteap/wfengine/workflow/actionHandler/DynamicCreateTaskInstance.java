package com.jteap.wfengine.workflow.actionHandler;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.jpdl.el.impl.JbpmExpressionEvaluator;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

/**
 * 进入会签节点时，根据会签人员和配置，动态创建会签任务
 *@filename   DynamicCreateTaskInstance.java
 *@author       MrBao
 *@date  	        2009-7-16
 *@remark
 */
@SuppressWarnings({ "unused", "serial","deprecation","unchecked" })
public class DynamicCreateTaskInstance implements ActionHandler {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext context) throws Exception {

	    TaskNode taskNode = (TaskNode) context.getNode();   
	    Task task = (Task)taskNode.getTasks().iterator().next();   
	    
        Token token = context.getToken();   
        TaskMgmtInstance tmi = context.getTaskMgmtInstance();   
        
        //获取自定义的进行会签的人员
        String  strActors= context.getContextInstance().getVariable("actors").toString();
        
        String actorIds = (String) JbpmExpressionEvaluator.evaluate(task.getActorIdExpression(),
				new ExecutionContext(context.getProcessInstance().getRootToken()));
        
        
        boolean  strSortAble=Boolean.parseBoolean(context.getContextInstance().getVariable("sortAble").toString());
		String [] arrActor = strActors.split(",");

		 //无序会签，根据会签人员，每人创建一个TaskInstance   
		if(!strSortAble){
	        for(String strActor : arrActor){
	        	tmi.createTaskInstance(task, token).setActorId(strActor);   
	        }
		}else if(strSortAble){//有序会签则只先给第一个会签人员创建任务
			tmi.createTaskInstance(task, token).setActorId(arrActor[0]);   
			context.getContextInstance().setVariable("currentActorId", 0);
		}
	}

}
