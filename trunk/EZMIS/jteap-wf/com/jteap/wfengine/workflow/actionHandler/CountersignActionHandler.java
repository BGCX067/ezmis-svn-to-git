package com.jteap.wfengine.workflow.actionHandler;

import java.util.Collection;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.jteap.wfengine.workflow.util.WFConstants;


/**
 *  会签中每个任务结束时，调用此类，处理会签跳转路径
 *@filename   CountersignActionHandler.java
 *@author       MrBao
 *@date  	        2009-7-16
 *@remark
 */
public class CountersignActionHandler implements ActionHandler {
	
	private static final long serialVersionUID = 5632213896936275400L;

	@SuppressWarnings("unchecked")   
    public void execute(ExecutionContext context) throws Exception {   
		
        Token token = context.getToken();   
        Node taskNode = (Node) context.getNode();   
        
		Task task =	context.getTask(); 
		//(Task)taskNode.getTasks().iterator().next();   
		//TaskInstance ti = context.getTaskInstance();
		
        TaskMgmtInstance tmi = context.getTaskMgmtInstance();   
        
        Collection<TaskInstance> unfinishedTasks = tmi.getUnfinishedTasks(token);   
        ContextInstance contextInstance =  context.getProcessInstance().getContextInstance();
        
        //选择的状态，同意，不同意
        Object variable =  contextInstance.getVariable("selectedState");
        
        //选择的路径名
        String selectTransitionName =  contextInstance.getVariable("selectTransitionName")+"";
      
        //会签模式，串行，并行
        String   strProcessMode = contextInstance.getVariable(taskNode.getName() +"processMode")+"";
        
        //获取自定义的进行会签的人员
        String  strActors= contextInstance.getVariable(WFConstants.WORKFLOW_NODE_ACTORID_NAME)+"";
        
		String [] arrActor = strActors.split(",");
        Boolean selectedState=null;   
        if (variable == null) {   
//            throw new RuntimeException(   
//                    "Jbpm transition error: user selected transition is null!");   
        	selectedState=true;
        } else {   
            // 获取当前TaskInstance选择的路径
        	selectedState = Boolean.parseBoolean(variable.toString());   
        }   
  
        if(strProcessMode.equals("0")){//无序会签时
	        // 如果用户选择不通过的路径，则自动完成余下所有的会签Task并转到相应路径
	        if (!selectedState) {   
	            for (TaskInstance unfinishedTaskInstance : unfinishedTasks) {   
	                // 把没有完成的TaskInstance的ActorId改为automatic complete   
	                if (!unfinishedTaskInstance.hasEnded()) {   
	                	token.unlock("token[" + token.getId() + "]");  
	                	unfinishedTaskInstance.end(selectTransitionName); 
	                }    
	            }   
	        }   
        }else if(strProcessMode.equals("1")){//有序会签时
        	 if (selectedState){//选择的是会签同意，继续创建任务       		
        		 int currentActorIndex =  Integer.parseInt(contextInstance.getVariable(taskNode.getName() +"currentActorIndex").toString());
        		 if((currentActorIndex +1)< arrActor.length){//分派下一个会签任务
        			 tmi.createTaskInstance(task, token).setActorId(arrActor[currentActorIndex +1]);
        			 contextInstance.setVariable("currentActorId", currentActorIndex +1);
        		 }else{//有序会签结束
        			 token.unlock("token[" + token.getId() + "]");  
        			 context.getProcessInstance().signal(selectTransitionName);
        		 }
        	 }else{
        		 token.unlock("token[" + token.getId() + "]"); 
        		 context.getProcessInstance().signal(selectTransitionName);
        	 }
        }
        
    }   


}
