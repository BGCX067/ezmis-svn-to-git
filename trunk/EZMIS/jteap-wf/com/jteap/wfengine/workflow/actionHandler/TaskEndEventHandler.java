package com.jteap.wfengine.workflow.actionHandler;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

/**
 * 任务处理完毕 触发该事件
 * task-end
 * @author tanchang
 *
 */
public class TaskEndEventHandler implements ActionHandler {

	private static final long serialVersionUID = 5593827574610795242L;


	public void execute(ExecutionContext context) throws Exception {
		TaskInstance ti = context.getTaskInstance();
		
		String preNodeName = ti.getName();	//上一环节名称
		
		String preActor = context.getContextInstance().getTransientVariable("actor")+"";
		String tokenId = ti.getToken().getId()+"";
		
		context.getContextInstance().setVariable(ti.getTask().getParent().getId()+"_"+tokenId+"_preNodeName",preNodeName);
		context.getContextInstance().setVariable(ti.getTask().getParent().getId()+"_"+tokenId+"_preActor", preActor);
		
	}
	

}
