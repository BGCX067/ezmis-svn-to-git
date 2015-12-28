package com.jteap.wfengine.workflow.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Session;
import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.graph.def.Event;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.def.Node.NodeType;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.EndState;
import org.jbpm.graph.node.Fork;
import org.jbpm.graph.node.Join;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.jpdl.el.impl.JbpmExpressionEvaluator;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springmodules.workflow.jbpm31.JbpmCallback;
import org.springmodules.workflow.jbpm31.JbpmTemplate;

import com.jteap.core.Constants;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.util.WFConstants;

@SuppressWarnings({ "unused", "serial","deprecation","unchecked" })
public class JbpmOperateManager  {
	private FlowConfigManager flowConfigManager;
	private TaskToDoManager taskToDoManager;
	private JbpmTemplate jbpmTemplate;
	private WorkFlowLogManager workFlowLogManager;
	private PersonManager personManager;
	private NodePermissionManager nodePermissionManager;
	
//	private String curLoginUserName;
	
	public NodePermissionManager getNodePermissionManager() {
		return nodePermissionManager;
	}

	public void setNodePermissionManager(NodePermissionManager nodePermissionManager) {
		this.nodePermissionManager = nodePermissionManager;
	}

	public WorkFlowLogManager getWorkFlowLogManager() {
		return workFlowLogManager;
	}

	public void setWorkFlowLogManager(WorkFlowLogManager workFlowLogManager) {
		this.workFlowLogManager = workFlowLogManager;
	}

	public JbpmTemplate getJbpmTemplate() {
		return jbpmTemplate;
	}

	public void setJbpmTemplate(JbpmTemplate jbpmTemplate) {
		this.jbpmTemplate = jbpmTemplate;
	}
	
	/**
	 * 发布一个流程定义
	 *@author  MrBao 
	 *@date 	  2009-7-10
	 *@param  xml
	 *@return
	 *@return Long		
	 *@remark
	 */
	public Long deployProcessDefinition(final String xml){
		
		return (Long) jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){ 
	                ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(xml);
	        		// 利用容器的方法将流程定义数据部署到数据库上 
	        		jbpmContext.deployProcessDefinition(processDefinition); 
	                return new Long(processDefinition.getId()); 
	          }
	       }); 
	} 
	
	public Long deployProcessDefinition(final InputStream is){	
		return (Long) jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){ 
	      			ProcessDefinition processDefinition = ProcessDefinition.parseXmlInputStream(is); 
	      			
	        		jbpmContext.deployProcessDefinition(processDefinition); 
	                return new Long(processDefinition.getId()); 
	          }
	       }); 
	} 
	
	/**
	 * 根据流程定义id 删除流程定义
	 *@author  MrBao 
	 *@date 	  2009-7-30
	 *@param processDefinitionId
	 *@return
	 *@return boolean
	 *@remark
	 */
	public boolean deleteProcessDefinition(final Long processDefinitionId){
		return (Boolean) jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){
	        	  try{
	            	jbpmContext.getGraphSession().deleteProcessDefinition(processDefinitionId);
	        	  }catch(Exception ex){}
	                return true; 
	          }
	       }); 
	}
	
	/**
	 * 流程起草
	 *@author  MrBao 
	 *@date 	  2009-7-10
	 *@param processName
	 *@param userName
	 *@return
	 *@return Long
	 *@remark
	 */
	public Long createProcessInstance(final String processName,final String userName) {
		
		return (Long) jbpmTemplate.execute(new JbpmCallback() {
			public Object doInJbpm(JbpmContext jbpmContext) {
				ProcessDefinition processDefinition = jbpmContext
						.getGraphSession().findLatestProcessDefinition(processName);
				ProcessInstance pi = processDefinition.createProcessInstance();
				//设置流程实例创建人
				pi.getContextInstance().setVariable(WFConstants.WF_VAR_CREATOR, userName);
				
				//设置流程实例创建时间
				pi.getContextInstance().setVariable(WFConstants.WF_VAR_CREATDT, new Date());
				pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, userName);
				pi.signal();
				
				jbpmContext.save(pi);
				
				startProcessInstance(pi.getId());
				jbpmContext.getSession().flush();
				return new Long(pi.getId()); 
			}
		});
	}
	
	/**
	 * 开始一个流程实例
	 *@author  MrBao 
	 *@date 	  2009-7-16
	 *@param processInstanceId
	 *@return
	 *@return boolean
	 *@remark
	 */
	public boolean startProcessInstance(final Long processInstanceId){
			
		return (Boolean) jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){ 
	       
	        	  ProcessInstance pi = jbpmContext.getProcessInstance(processInstanceId);
	        	  List<TaskInstance> taskList=jbpmContext.getTaskMgmtSession().findTaskInstancesByProcessInstance(pi);
	        	  if(taskList.size()>0){
	        		  TaskInstance taskInsatnce =taskList.iterator().next();
	        		  if(taskInsatnce.getToken().getNode().getNodeType() == NodeType.StartState){
	        			  endTask(taskInsatnce,null);
	        			  return true;
	        		  }
	        	  }
	        	  return false;
	          }
	       }); 
	}
	
	/**
	 * 保存流程实例
	 *@author  MrBao 
	 *@date 	  2009-7-10
	 *@param processInstanceId	
	 *@return
	 *@return Long
	 *@remark
	 */
	public Long saveProcessInstance( Long  processInstanceId) {
		
		final ProcessInstance processInstance = jbpmTemplate.findProcessInstance(processInstanceId);
		return (Long) jbpmTemplate.execute(new JbpmCallback() {
			
			public Object doInJbpm(JbpmContext context) {
				context.save(processInstance);
				return new Long(processInstance.getId());
			}
		});
	}
	
	/**
	 * 回退(回收)任务
	 * 
	 * @author MrBao
	 * @date 2009-6-26
	 * @param pi							流程实例id
	 * @param toNodeName				回退到哪个任务
	 * @param currTaskInstanceId	当前任务实例id
	 * @return
	 * @return String
	 * @remark
	 */
	public TaskInstance goBackToTask(final Long pid, final String toNodeName, final Long currTaskInstanceId,final String taskActor, final String taskActorLoginName, final String backReason, final String preActor) {
		final ProcessInstance pi = this.jbpmTemplate.findProcessInstance(pid);
		return (TaskInstance)jbpmTemplate.execute(new JbpmCallback() {
			public Object doInJbpm(JbpmContext context) {
				//现在执行的任务实例
				TaskInstance currTaskInstance = context.getTaskInstance(currTaskInstanceId);
				// 回退目标的任务实例
				TaskInstance toTaskInstance = getTaskInstanceByTaskName(toNodeName, pi);
				Task backToTask = toTaskInstance.getTask();
				//记录回退日志
				writeGoBackTaskLog(currTaskInstance,backToTask,taskActor, taskActorLoginName, backReason, preActor);

				
				Node node = toTaskInstance.getTask().getTaskNode();
				//是否关闭同级其他的token的流程实例
				boolean isCloseSameLevelTaskInstance = (currTaskInstance.getToken().getId() != toTaskInstance.getToken().getId());
				//关闭当前未完成任务
				closeTask(pi,currTaskInstance.getToken(),isCloseSameLevelTaskInstance);

				// 获得前面节点
				List<Node> preNodes = findPrevTaskNode(currTaskInstance.getTask().getTaskNode().getId());
				if (preNodes.size() > 0) {
					Node preNode = preNodes.get(0);
					// 前面节点如果是普通节点或者分支，则直接回退到前面一个节点
					if (preNode.getNodeType().equals(NodeType.Task)) {
						startTask(toTaskInstance);
					} else if (preNode.getNodeType().equals(NodeType.Fork)) {
						startTask(toTaskInstance);
						// 如果前面节点是汇集，则回退到汇集前一步的所有节点,使之所有节点重新签名
					} else if (preNode.getNodeType().equals(NodeType.Join)) {
						List<Node> startNodes = findPrevTaskNode(preNode.getId());
						for (Node startNode : startNodes) {
							TaskInstance preTaskInstance = getTaskInstanceByTaskName(startNode.getName(), pi);
							startTask(preTaskInstance);
						}
					}
				}

				toTaskInstance.getToken().setNode(node);
				
				// 手动触发进入节点事情
				ExecutionContext executionContext =new  ExecutionContext(toTaskInstance.getToken());
				node.fireEvent(Event.EVENTTYPE_NODE_ENTER, executionContext);
				return toTaskInstance;
			}
		}); 
	}

	/**
	 * 
	 * 描述 : 撤销任务
	 * 作者 : wangyun
	 * 时间 : Jul 17, 2010
	 * 参数 : 
	 * 返回值 : 
	 * 异常 :
	 * @param curUserName 
	 */
	public TaskInstance cancelTask(final ProcessInstance pi, final Node preNode, final String curUserName, final String taskActorLoginName) {
		return (TaskInstance)jbpmTemplate.execute(new JbpmCallback() {
			public Object doInJbpm(JbpmContext context) {
				// 找到所有未完成任务实例
				Collection<TaskInstance> taskInstances = getUnfinishedTaskInstance(pi, null);
				Iterator<TaskInstance> it = taskInstances.iterator();

				// 找到第一个未完成任务实例（由于撤销条件的约束，所有未完成的任务实例的上一环节为同一个）
				TaskInstance ti = it.next();

				// 撤销目标的任务实例
				TaskInstance toTaskInstance = getTaskInstanceByTaskName(preNode.getName(), pi);
				Node node = toTaskInstance.getTask().getTaskNode();
				//记录撤销日志
				writeCancelTaskLog(ti, toTaskInstance.getTask(), curUserName, taskActorLoginName, "");
				// 关闭所有未完成任务实例
				closeTask(pi, null);
				// 执行撤销目标的任务实例
				startTask(toTaskInstance);
				toTaskInstance.getToken().setNode(node);
				
				// 手动触发进入节点事情
				ExecutionContext executionContext =new  ExecutionContext(toTaskInstance.getToken());
				node.fireEvent(Event.EVENTTYPE_NODE_ENTER, executionContext);
				return toTaskInstance;
			}
		}); 
	}

	
	/**
	 * 根据任务名称取得任务实例对象
	 * @param taskName
	 * @param pi
	 * @return
	 */
	protected List<TaskInstance> getTaskInstancesByTaskName(String taskName,
			ProcessInstance pi) {
		List<TaskInstance> tiList = new ArrayList<TaskInstance>();
		
		Collection coll = pi.getTaskMgmtInstance().getTaskInstances();
		
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TaskInstance taskInstance = (TaskInstance) it.next();
			String name = taskInstance.getTask().getName();
			if (name != null && name.equals(taskName)) {
				tiList.add(taskInstance);
			}
		}
		return tiList;
	}

	/**
	 * 结束任务
	 * 
	 * @author MrBao
	 * @date 2009-7-10
	 * @param ti
	 * @param transitionName
	 * @return
	 * @return boolean
	 * @remark
	 */
	public boolean endTask(final Long taskInstanceId,
		final String transitionName) {
		return (Boolean) jbpmTemplate.execute(new JbpmCallback() {
			
			public Object doInJbpm(JbpmContext context) {
				TaskInstance ti = context.getTaskInstance(taskInstanceId);
				if(StringUtil.isNotEmpty(transitionName)){
					ti.end(transitionName);
				}else{
					ti.end();
				}
				
				// 此处记录日志
				writeEndTaskLog(ti,transitionName);
				return false;
			}
		});
	}
	
	public boolean endTask(final TaskInstance ti,
			final String transitionName) {

		return (Boolean) jbpmTemplate.execute(new JbpmCallback() {
			public Object doInJbpm(JbpmContext context) {
				if(StringUtil.isNotEmpty(transitionName)){
					ti.end(transitionName);
				}else{
					ti.end();
				}
				// 此处记录日志
				writeEndTaskLog(ti,transitionName);
				return false;
			}
		});

	}

	/**
	 * 关闭指定token的所有任务,
	 * @param pi							
	 * @param token
	 * @param closeSameLevelTaskInstance  该参数指定是否关闭与该token同级的token的所有任务 （true：关闭同级token;false:不关闭同级token）
	 */
	public void closeTask(ProcessInstance pi,Token token,boolean closeSameLevelTaskInstance) {
		// 不关闭同级token,直接关闭任务实例
		if(!closeSameLevelTaskInstance){
			closeTask(pi,token);
			// 关闭同级token
		}else{
			// 如果有同级token，则关闭同级token对应的所有任务实例
			if(token.getParent()!=null){
				Token parentToken = token.getParent();
				Map<String,Token> childTokensMap = parentToken.getChildren();
				for(String key:childTokensMap.keySet()){
					Token childToken = childTokensMap.get(key);
					closeTask(pi,childToken);
				}
				// 如果没有同级token，则直接关闭当前实例（一般用于从节点回退到会签中）
			} else {
				closeTask(pi,token);
			}
		}
	}

	/**
	 * 关闭指定token的所有任务,
	 * @param pi							
	 * @param token
	 */
	public void closeTask(ProcessInstance pi,Token token) {
		Collection coll = getUnfinishedTaskInstance(pi,token);
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TaskInstance taskInstance = (TaskInstance) it.next();
			taskInstance.setSignalling(false);
			taskInstance.cancel();
			taskInstance.setEnd(new Date());
			if (taskInstance.getToken().hasParent()) {// 如果是分支节点
				// 子令牌是否可以激活(恢复)其父令牌
				taskInstance.getToken().setAbleToReactivateParent(false);
			}
		}
	}
	

	/**
	 * 开始一个任务
	 * 
	 * @author MrBao
	 * @date 2009-6-26
	 * @param taskInstance
	 * @return void
	 * @remark
	 */
	public void startTask(TaskInstance taskInstance) {
		taskInstance.restart();

		if (taskInstance.getToken().hasParent()) {// 如果是分支节点
			// 子令牌是否可以激活(恢复)其父令牌
			taskInstance.getToken().setAbleToReactivateParent(true);
		}
	}

	/**
	 * 根据任务名，返回任务实例
	 * 
	 * @author MrBao
	 * @date 2009-6-26
	 * @param tn
	 * @param pi
	 * @return
	 * @return TaskInstance
	 * @remark   pi.getProcessDefinition().getTaskMgmtDefinition().getTask(taskName)
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public TaskInstance getTaskInstanceByTaskName(String tn, ProcessInstance pi) {
		Collection coll = pi.getTaskMgmtInstance().getTaskInstances();
		
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TaskInstance taskInstance = (TaskInstance) it.next();
			String name = taskInstance.getTask().getName();
			if (name != null && name.equals(tn)) {
				return taskInstance;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 描述 : 根据任务名称查找所有未结束的任务实例
	 * 作者 : wangyun
	 * 时间 : Jul 16, 2010
	 * 参数 : 
	 * 返回值 : 
	 * 异常 :
	 */
	public TaskInstance getOpenTaskInstanceByTaskName(String tn,
			ProcessInstance pi) {
		Collection coll = pi.getTaskMgmtInstance().getTaskInstances();
		
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TaskInstance taskInstance = (TaskInstance) it.next();
			String name = taskInstance.getTask().getName();
			if (name != null && name.equals(tn) && taskInstance.isOpen()) {
				return taskInstance;
			}
		}
		return null;
	}

	/**
	 * 获取当前未完成任务实例集合
	 * 
	 * @author MrBao
	 * @date 2009-6-26
	 * @param pi
	 * @return
	 * @return Collection
	 * @remark
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public Collection getUnfinishedTaskInstance(ProcessInstance pi,Token tk) {
		Collection collTaskInstance = new ArrayList();
		if(tk == null){
			List listTaken = pi.findAllTokens();
			for (int i = 0; i < listTaken.size(); i++) {
				Token token = (Token) listTaken.get(i);
				collTaskInstance.addAll(pi.getTaskMgmtInstance().getUnfinishedTasks(token));
			}
		}else{
			collTaskInstance.addAll(pi.getTaskMgmtInstance().getUnfinishedTasks(tk));
		}
		return collTaskInstance;
	}

	/**
	 * 设置或修改流程变量的值
	 *@author  MrBao 
	 *@date 	  2009-7-10
	 *@param pIId					流程实例编号
	 *@param vailableName	变量名
	 *@param value				变量值
	 *@return void
	 *@remark
	 */
	public void setProcessVariable(final Long pIId, final String vailableName, final Object value){
		
		jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){ 
	             	ProcessInstance pi= jbpmContext.getProcessInstance(pIId);
	             	pi.getContextInstance().setVariable(vailableName, value);
	                return null; 
	          }
	     }); 	
	}
	
	/**
	 *  设置或修改流程变量的值
	 *@author  MrBao 
	 *@date 	  2009-7-10
	 *@param pIId  			流程实例编号
	 *@param variableMap  变量集合
	 *@return void
	 *@remark
	 */
	public void setProcessVariable(final Long pIId, final Map variableMap){
		
		jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){ 
	        	  if(variableMap != null){
		             	ProcessInstance pi= jbpmContext.getProcessInstance(pIId);
		             	pi.getContextInstance().setVariables(variableMap);
	        	  }
				return null;
	          }
	    });
	}
	
	/**
	 * 获取流程变量
	 *@author  MrBao 
	 *@date 	  2009-7-10
	 *@param pIId					流程实例编号
	 *@param vailableName	变量名称
	 *@return
	 *@return Object
	 *@remark
	 */
	public Object getProcessVariable(final Long pIId, final String vailableName){
		
		return jbpmTemplate.execute(new JbpmCallback(){        
	          public Object doInJbpm(JbpmContext jbpmContext){ 
	             	ProcessInstance pi= jbpmContext.getProcessInstance(pIId);
	                return pi.getContextInstance().getVariable(vailableName);
	          }
	       }); 	
	}
	
	/** 
	* 删除流程实例 
	*/ 
	@SuppressWarnings("unchecked") 
	public boolean deleteProcessInstance(final long processInstanceId) { 
		return (Boolean)jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
				ProcessInstance aProcessInstance = context.getGraphSession().loadProcessInstance(processInstanceId); 
				context.getGraphSession().deleteProcessInstance(aProcessInstance); 
				return true; 
			} 
		}); 
	}
	
	/** 
	* 结束流程实例 
	*/ 
	@SuppressWarnings("unchecked") 
	public boolean endProcessInstance(final long processInstanceId) { 
		return (Boolean)jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
				ProcessInstance aProcessInstance = context.getGraphSession().loadProcessInstance(processInstanceId); 
				aProcessInstance.end(); 
				return true; 
			} 
		}); 
	} 

	/** 
	* 暂停流程实例 
	*/ 
	@SuppressWarnings("unchecked") 
	public boolean suspendedProcessInstance(final long processInstanceId) { 
		return (Boolean)jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
				ProcessInstance aProcessInstance = context.getGraphSession().loadProcessInstance(processInstanceId); 
				aProcessInstance.suspend(); 
				return true; 
			} 
		}); 
	} 
	
	/**
	 * 记录回退日志
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param ti
	 *@param transitionName
	 *@return void
	 *@remark final ProcessInstance pi, final String taskName
	 */
	public void writeGoBackTaskLog(final TaskInstance currTaskInstance, Task backToTask,String taskActor, String taskActorLoginName, String backReason, String preActor){
		
		String taskName = currTaskInstance.getName();
		String nextTaksName = null;
		String nextTaksActor = null;
		String taskResult = null;		//记录选择的路径名
		Long pi_id = currTaskInstance.getProcessInstance().getId();
//		String taskActor = "";
//		for(PooledActor actor : currTaskInstance.getPooledActors()){
//			taskActor += actor.getActorId() + ",";
//		}
//		String actor = taskActor.substring(0,taskActor.length()-1);;
//		if(!taskActor.equals("")){
//			actor = personManager.findPersonByLoginName(taskActor).getUserName();
//		}
//		String curActor =  sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
//		if(!actor.equals("")){
//			String[] taskActorArray = actor.split(",");
//			String tempActor = "";
//			for(int i = 0;i < taskActorArray.length;i++){
//				tempActor += personManager.findPersonByLoginName(taskActorArray[i]).getUserName() + ",";
//			}
//			curActor = tempActor.substring(0,tempActor.length()-1);
//		}
//		String nextActor = "";
//		if (StringUtil.isEmpty(preActor)) {
//			
//		}
//		nextTaksActor = getActorIdsByTask(backToTask, currTaskInstance);
//		String nextActor = nextTaksActor;
//		if(!nextTaksActor.equals("")){
//			String[] nextTasksActorArray = nextTaksActor.split(",");
//			String tempActor = "";
//			for(int i = 0;i < nextTasksActorArray.length;i++){
//				tempActor += personManager.findPersonByLoginName(nextTasksActorArray[i]).getUserName() + ",";
//			}
//			nextActor = tempActor.substring(0,tempActor.length()-1);
//		}
		String preActorLoginName = personManager.findPersonByLoginName(preActor).getUserName();
		nextTaksName = backToTask.getName();
		taskResult = "回退至<" + nextTaksName  + ">";
		
		//写入日志表
		workFlowLogManager.addFlowLog(pi_id, taskName, taskActor, taskActorLoginName, preActorLoginName, nextTaksName, taskResult, backReason);
	}

	/**
	 * 记录撤销日志
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param ti
	 *@param transitionName
	 *@return void
	 *@remark final ProcessInstance pi, final String taskName
	 */
	public void writeCancelTaskLog(final TaskInstance currTaskInstance , Task cancelToTask,String taskActor, String taskActorLoginName, String backReason){
		String taskName = currTaskInstance.getName();
		String nextTaksName = null;
		String nextTaksActor = null;
		String taskResult = null;		//记录选择的路径名
		Long pi_id = currTaskInstance.getProcessInstance().getId();
		nextTaksName = cancelToTask.getName();
		nextTaksActor = getActorIdsByTask(cancelToTask, currTaskInstance);
		String nextActor = nextTaksActor;
		if(!nextTaksActor.equals("")){
			String[] nextTasksActorArray = nextTaksActor.split(",");
			String tempActor = "";
			for(int i = 0;i < nextTasksActorArray.length;i++){
				tempActor += personManager.findPersonByLoginName(nextTasksActorArray[i]).getUserName() + ",";
			}
			nextActor = tempActor.substring(0,tempActor.length()-1);
		}
		taskResult = "撤销至<" + nextTaksName  + ">";
		
		//写入日志表
		workFlowLogManager.addFlowLog(pi_id, taskName, taskActor, taskActorLoginName, nextActor, nextTaksName, taskResult, backReason);
	}
	
	/**
	 * 记录任务结束日志
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param ti
	 *@param transitionName
	 *@return void
	 *@remark
	 */
	public void writeEndTaskLog(final TaskInstance ti , String transitionName){
		String taskName = ti.getName();
		String nextTaksName = null;
		String nextTaksActor = null;
		String taskResult = null;
		Long pi_id = ti.getProcessInstance().getId();
		String taskActor = ti.getPooledActors().iterator().next().getActorId();
		Person person = personManager.findUniqueBy("userLoginName", taskActor);
		

		//当前节点
		Node currNode = ti.getToken().getNode();
		
		//获取当前节点未完成任务实例集合
		
		String processKind = ti.getContextInstance().getVariable(currNode.getName() +"processKind") + "";
		//本节点是否为会签节点
		if(StringUtil.isNotEmpty(processKind) && !processKind.equals(WFConstants.WORKFLOW_PORCESS_KIND_MULTI)){
			
			//获取流程自动转向路径名
			Transition autoTransition = getTransitionNameByTaskInstance(ti,null);
			String autoTransitionName = autoTransition.getName();
			//不为空,则使用自动跳转路径，否则使用指定路径
			if(StringUtil.isNotEmpty(autoTransitionName)){
				transitionName = autoTransitionName;
			}else if(StringUtil.isEmpty(autoTransitionName) && StringUtil.isEmpty(transitionName)){
				//指定路径和条件路径都为空时，选择默认路径
				transitionName =  ti.getToken().getNode().getDefaultLeavingTransition().getName();
			}
			
			//下一节点
			Node nexNode = currNode.getLeavingTransition(transitionName).getTo();
			
			//是任务节点
			if(nexNode.getNodeType().equals(NodeType.Task)){
				Task task = getNextTaskByNode(currNode,transitionName);
				nextTaksName = task.getName();
				nextTaksActor = getActorIdsByTask(task, ti);
			}else{//不是任务节点
				nextTaksName = nexNode.getName();				
				nextTaksActor = getNextTaskActorByNode(ti);
			}
		}else{//会签时
			nextTaksName = ti.getName();
			nextTaksActor = "";//getUnfinishedTaskActors(coll);		
		}
		//记录选择的路径名
		taskResult = transitionName;
		//写入日志表
		workFlowLogManager.addFlowLog(pi_id, taskName, person.getUserName(), taskActor, nextTaksActor, nextTaksName,taskResult, "");
	}
	
	
	/**
	 * 流程转到下一步
	 * @param ti			当前任务实例
	 * @param userName		当前处理人
	 * @param node			指定下一步的节点名称
	 * @return
	 */
	public List<String> goNextTask(final TaskInstance ti,final String userName,final String node){
		Transition tran = (Transition) jbpmTemplate.execute(new JbpmCallback() {
			public Object doInJbpm(JbpmContext jbpmContext) {
				Transition autoTransition = null;
				autoTransition = getTransitionNameByTaskInstance(ti,node);
				autoTransition.removeConditionEnforcement();//清除强制性标记
				String transitionName = autoTransition.getTo().getName();
				//设置处理当前任务的用户名,保存起来供后续操作调用,比如在流程结束事件中将该用户名称作为上一步处理人保存起来
				ti.getProcessInstance().getContextInstance().setTransientVariable("actor", userName);
				ti.getProcessInstance().getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, userName);
				ti.getProcessInstance().getContextInstance().setTransientVariable("ti", ti);
				ti.end(autoTransition);
				return autoTransition;
			}
		});
		List<String> retNodeName = new ArrayList<String>();
		//下一个任务的实例,如果下一个节点属于非task节点，则下一个任务的实例为空
		List<TaskInstance> resultTiList = new ArrayList<TaskInstance>();
		if (tran != null) {
			Node n = tran.getTo();
			if (n.getNodeType().equals(NodeType.Task)) {
				retNodeName.add(n.getName());
//				resultTiList.add(this.getTaskInstanceByTaskName(n.getName(), ti
//						.getProcessInstance()));
			} else if (n.getNodeType().equals(NodeType.Fork) ||n.getNodeType().equals(NodeType.Join) ) {
				List<Transition> tranList = n.getLeavingTransitionsList();
				for (int i = 0; i < tranList.size(); i++) {
					Transition transition = tranList.get(i);
					Node tasknode = transition.getTo();
					retNodeName.add(tasknode.getName());
//					TaskInstance nextTi = this.getTaskInstanceByTaskName(tasknode.getName(),ti.getProcessInstance());
//					if(nextTi!=null)
//						resultTiList.add(nextTi);
				}
			}
		}
		return retNodeName;
	}
	
	/**
	 * 执行下一步，并返回下一步环节名称　
	 * @author 肖平松
	 * @version Sep 14, 2009
	 * @param ProcessInstanceId
	 * @param userName
	 * @return
	 */
	public String goNextTask(final String ProcessInstanceId,final String userName) {
	
		Transition tran = (Transition) jbpmTemplate.execute(new JbpmCallback() {
			public Object doInJbpm(JbpmContext jbpmContext) {
				ProcessInstance pi = jbpmContext.getProcessInstance(Long.parseLong(ProcessInstanceId));
				//如果用户是管理员用户，随便得到一个任务实例并执行,否则需要得到当前用户的任务实例进行执行
				List<TaskInstance> taskList = null;
				if(userName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
					taskList = jbpmContext.getTaskMgmtSession().findTaskInstancesByProcessInstance(pi);
				}else{
					taskList = jbpmContext.getTaskMgmtSession().findPooledTaskInstances(userName);
				}
				
				Transition autoTransition = null;
				if (taskList.size() > 0) {
					TaskInstance taskInstance = taskList.iterator().next();
					autoTransition = getTransitionNameByTaskInstance(taskInstance,null);
					String transitionName = autoTransition.getTo().getName();
					//设置处理当前任务的用户名,保存起来供后续操作调用,比如在流程结束事件中将该用户名称作为上一步处理人保存起来
					pi.getContextInstance().setTransientVariable("actor", userName);
					pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, userName);
					taskInstance.end(autoTransition);
				}
				return autoTransition;
			}
		});
		
		if(tran != null){
			Node n = tran.getTo();
			String nextNodeName = "";
			if(n.getNodeType().equals(NodeType.Task))
				nextNodeName = n.getName();
			else if(n.getNodeType().equals(NodeType.Fork) || n.getNodeType().equals(NodeType.Join)){
				List<Transition> list = n.getLeavingTransitionsList();
				for(Transition ts : list){
					nextNodeName += ts.getTo().getName() + "~";
				}
				if(nextNodeName.length() > 0){
					nextNodeName = StringUtil.delEndString(nextNodeName, "~");
				}
			}
			return nextNodeName;
		}
		else
			return "";
	}
	
	/**
	 * 获取当前任务结束时，流程选择的跳转 路径名
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param ti
	 *@return
	 *@return String
	 *@remark
	 */
	public Transition getTransitionNameByTaskInstance(TaskInstance ti,String node){
		Transition transition = null;
		List<Transition> list  =  ti.getAvailableTransitions();
		if(list.size() == 1){
			return list.get(0);
		}
		
		if(list.size()>1){
//			if(hasConditionTransition(ti)){//存在有条件转向
			if(StringUtil.isNotEmpty(node)){
				for(Transition t : list){
					if(t.getTo().getName().equals(node)){
						t.removeConditionEnforcement();
						transition = t;
					}
				}
			}
			return transition;
//			}
		}
		//都不满足条件时选择默认跳转
		else{
			list = (List<Transition>) ti.getToken().getNode().getLeavingTransitions();
			return list.get(0);
		}
	}
	
	/**
	 * 是否存在条件转向路径
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param ti
	 *@return
	 *@return Boolean
	 *@remark
	 */
	public boolean hasConditionTransition(TaskInstance ti){
			
		List<Transition> list = ti.getToken().getNode().getLeavingTransitions();
		for(Transition transition : list){
			if(transition.getCondition() != null) return true;
		}
		return false;
	}
	
	/**
	 * 获取未完成任务实例的执行者，用逗号分隔
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param coll
	 *@return
	 *@return String
	 *@remark
	 */
	public String getUnfinishedTaskActors(Collection<TaskInstance> coll){
		String taksActors = null;
		StringBuffer sb = new StringBuffer();
		for(TaskInstance taskInstance : coll){
			sb.append(taskInstance.getActorId() + ",");
		}
		String tempActor = sb.toString();
		if(tempActor.endsWith(",")){
			taksActors = tempActor.substring(0,tempActor.length()-1);
		}else{
			taksActors = tempActor;
		}
		return taksActors;
	}
	
	/**
	 * 获取任务执行者
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param task
	 *@param processInstanceId
	 *@return
	 *@return String
	 *@remark
	 */
	public String getActorIdsByTask(final Task task, final TaskInstance ti,final String userLoginName){
		final Long processInstanceId = ti.getProcessInstance().getId();
		Object obj  = jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
				ExecutionContext executionContext =new  ExecutionContext(ti.getToken());
				ti.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME,userLoginName);
				executionContext.setTaskInstance(ti);
				String actorIds = (String) JbpmExpressionEvaluator.evaluate(task.getPooledActorsExpression(),executionContext);
				return actorIds;
			}
		 });
		if(obj != null){
			return obj.toString();
		}
		
		return  null;
	}
	
	/**
	 * 根据节点列表取得对应节点列表的处理人列表
	 * @param nodeList	普通环节,不能是会签环节
	 * @param ti
	 * @return
	 */
	public List getActorListByNodeList(List<Node> nodeList,TaskInstance ti){
		List actorsList = new ArrayList();
		for (Node node : nodeList) {
			String ids = "";
			if(node.getNodeType().equals(NodeType.Task)){
				Task task = ti.getProcessInstance().getProcessDefinition().getTaskMgmtDefinition().getTask(node.getName());
				if(task!=null){
					ids = getActorIdsByTask(task, ti);
				}
			}
			else if(node.getNodeType().equals(NodeType.Fork) || node.getNodeType().equals(NodeType.Join)){
				List<Transition> trList = node.getLeavingTransitionsList();
				for (Transition tran : trList) {
					Node joinNode = tran.getTo();
					Task task = ti.getProcessInstance().getProcessDefinition().getTaskMgmtDefinition().getTask(joinNode.getName());
					if(task!=null){
						ids += getActorIdsByTask(task, ti) + ",";
					}
				}
				if(ids.length() > 0) ids = StringUtil.delEndString(ids, ",");
			}
			actorsList.add(ids);
		}
		return actorsList;
	}
	
//	
//	/**
//	 * 根据TaskNode取得处理该节点的处理人
//	 * @param taskNode
//	 * @param ti
//	 * @return
//	 */
//	public String getActorIdsByTaskNode(final TaskNode taskNode, final TaskInstance ti){
//		final Task task = taskNode.getTask(taskNode.getName());
//		return getActorIdsByTask(task,ti);
//	}
	
	/**
	 * 获取任务执行者
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param task
	 *@param processInstanceId
	 *@return
	 *@return String
	 *@remark
	 */
	public String getActorIdsByTask(final Task task, final TaskInstance ti){
		final Long processInstanceId = ti.getProcessInstance().getId();
		Object obj  = jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
				ExecutionContext executionContext =new  ExecutionContext(ti.getToken());
				executionContext.setTaskInstance(ti);
				String actorIds = (String) JbpmExpressionEvaluator.evaluate(task.getPooledActorsExpression(),executionContext);
				return actorIds;
			}
		 });
		if(obj != null){
			return obj.toString();
		}
		
		return  null;
	}
	
	/**
	 * 获取下一个任务节点的任务对象
	 *@author  MrBao 
	 *@date 	  2009-7-13
	 *@param node					当前节点对象
	 *@param transitionName		跳转路径名	
	 *@return
	 *@return Task
	 *@remark
	 */
	public Task getNextTaskByNode(final Node node,final String transitionName) {
		
		Object obj = jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
			
				Node n = node.getLeavingTransition(transitionName).getTo();
				Task task = null;
				if (n.getNodeType().equals(NodeType.Task)) {
					Long id = n.getId();
					Session hSession = context.getSession();
					TaskNode td = (TaskNode) hSession.get(TaskNode.class, id);
					task = ((Task) (td.getTasks().iterator().next()));
				}
				return task; 
			} 
		}); 
		
		if(obj != null){
			return (Task)obj;
		}
		return null;
	}
	
	/**
	 * 获取下一节点的任务名称
	 *@author  MrBao 
	 *@date 	  2009-7-31
	 *@param node
	 *@param processInstanceId
	 *@return String
	 */
	public String getNextTaskNameByNode(final TaskInstance ti) {
		final StringBuffer sb = new StringBuffer();
		Object obj = jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) { 
				getNextTaskInfoByNode(ti,context ,"taskName",sb);
				return null; 
			} 
		}); 
		String taskNames = sb.toString();
		taskNames = StringUtil.delEndString(taskNames, ",");
		
		return taskNames;
	}
	
	/**
	 * 根据流程配置和流程实例，取得下一步满足条件的节点
	 * 如果下一步满足条件
	 * @param flowConfigId
	 * @param ti
	 * @return
	 */
	public List<Node> findNextAvalibleNode(String flowConfigId,TaskInstance ti){
		List<Node> nodeList = new ArrayList<Node>();
		//满足条件的跳转
		List<Transition> tranList = ti.getAvailableTransitions();
		for (Transition transition : tranList) {
			Node node = transition.getTo();
			nodeList.add(node);
		}
		return nodeList;
	}
	
	/**
	 * 查询有效的满足条件的路由方向并返回，该方法主要用于如果有效节点不存在，也没有默认的跳转则返回空
	 * 如果没有满足条件的路由方向，也没有指定默认的跳转方向，则返回空的列表
	 * @param ti
	 * @return
	 */
	public List<Transition> findAvalibleTransitions(String flowConfigId,TaskInstance ti){
		List<Transition> tranList = ti.getAvailableTransitions();
		if(tranList!=null && tranList.size()>0)
			return tranList;
		else{
			List<Transition> allLevavingTrans = ti.getTask().getTaskNode().getLeavingTransitionsList();
			for (Transition tran : allLevavingTrans) {
				if(getDefaultRouter(flowConfigId,tran)){
					tranList.add(tran);
				}
			}
		}
		return tranList;
	}
	
//	public String getNextProperTaskNameByNode(final String flowConfigId,final TaskInstance ti){
//		final StringBuffer sb = new StringBuffer();
//		Object obj = jbpmTemplate.execute(new JbpmCallback() { 
//			public Object doInJbpm(JbpmContext context) { 
//				getNextProperTaskInfoByNode(flowConfigId,ti,context ,sb);
//				return null; 
//			}
//		}); 
//		String taskNames = sb.toString();
//		
//		taskNames = StringUtil.delEndString(taskNames, ",");
//		System.out.println(taskNames);
//		return taskNames;
//	}
//	
//	/**
//	 * 获取下一个有效的任务信息
//	 * @author 肖平松
//	 * @version Nov 4, 2009
//	 * @param flowConfigId	流程配置编号
//	 * @param ti	当前任务实例
//	 * @param context
//	 * @param string
//	 * @param sb	保存返回值
//	 */
//	private void getNextProperTaskInfoByNode(String flowConfigId,TaskInstance ti,
//			JbpmContext context, StringBuffer sb) {
//		String[] fields = new String[]{"processMode","processKind","isOneProcessActor","isChooseActor"};
//		//当前节点
//		Node node = ti.getTask().getTaskNode();
//		//当前环节的所有的有效转换
//		List<Transition> trs = ti.getAvailableTransitions();
//		//如果没有找到符合条件的跳转，则找到默认跳转路径
//		if(trs.size() <= 0){
//			trs.add(ti.getTask().getTaskNode().getLeavingTransitions().get(0));
//		}
//		sb.append("{");
//		//遍历当前环节所有的有效转换
//		for(int i = 0;i < trs.size();i++){
//			Transition ts = trs.get(i);
//			//获取该转换的默认路由
//			Boolean defaultRouter = this.getDefaultRouter(flowConfigId,ts);
//			//如果只有一个有效转换,则设置为默认路由
//			if(trs.size() == 1){
//				defaultRouter = true;
//			}
//			sb.append("'default':" + defaultRouter + ",");
//			//获取当前转换的下一个结点
//			Node n = ts.getTo();
//			//下一个结点的名称
//			String name = n.getName();
//			//如果下一环节是环节
//			if (n.getNodeType().equals(NodeType.Task)){
//				//下一个环节的ID
//				Long id = n.getId();
//				Session hSession = context.getSession();
//				TaskNode td = (TaskNode) hSession.get(TaskNode.class, id);
//				Task task = (Task) (td.getTasks().iterator().next());
//				sb.append("'taskType':'Task',");
//				
//				sb.append("'taskName':'" + task.getName() + "',");
//				//根据任务信息获取处理人信息
//				String actorIds = getActorIdsByTask(task,ti );
//				sb.append("'actors':'" + actorIds + "',");
//				
//				String[] temp = actorIds.split(",");
//				String username = "";
//				//将处理人登录名转换为用户名
//				for(int j = 0;j < temp.length;j++){
//					if(temp[j].equals(Constants.ADMINISTRATOR_ACCOUNT)){
//						username += Constants.ADMINISTRATOR_NAME + ",";
//					}
//					else{
//						username += personManager.findUniqueBy("userLoginName", temp[j]).getUserName() + ",";
//					}
//				}
//				if(username.length() > 0){
//					username = StringUtil.delEndString(username, ",");
//				}
//				sb.append("'username':'" + username + "',");
//				
//				List<NodePermission> npList = (List<NodePermission>) nodePermissionManager.getNodePermission(flowConfigId,task.getName());
//				String json = JSONUtil.listToJson(npList, fields);
//				
//				json = StringUtil.delStartString(json, "[");
//				json = StringUtil.delEndString(json, "]");
//				
//				HashMap jsonmap = (HashMap) JSONUtil.parseObject(json);
//
//				sb.append("'processMode':'" + jsonmap.get("processMode") + "',");
//				sb.append("'processKind':'" + jsonmap.get("processKind") + "',");
//				sb.append("'isOneProcessActor':'" + jsonmap.get("isOneProcessActor") + "',");
//				sb.append("'isChooseActor':'" + jsonmap.get("isChooseActor") + "'");
//			}else if(n.getNodeType().equals(NodeType.Fork) || n.getNodeType().equals(NodeType.Join)){
//				//如果下一环节是会签或者汇集
//				//会签ID
//				Long id = n.getId();
//				Session hSession = context.getSession();
//				//获取会签的有效转换
//				List<Transition> tsList = null;
//				if(n.getNodeType().equals(NodeType.Fork)){
//					Fork fork = (Fork)hSession.get(Fork.class, id);
//					tsList = fork.getLeavingTransitionsList();
//					sb.append("'taskType':'Fork',");
//					sb.append("'taskName':'会签',");
//				}
//				else{
//					Join join = (Join)hSession.get(Join.class, id);
//					tsList = join.getLeavingTransitionsList();
//					sb.append("'taskType':'Join',");
//					sb.append("'taskName':'会签结束',");
//				}
//				StringBuffer actors = new StringBuffer();
//				String actorIds = "";
//				//遍历会签的所有有效转换
//				for(Transition tran : tsList){
//					//会签的下一个环节
//					Node nd = tran.getTo();
//					//如果会签的下一个环节是任务环节
//					if(nd.getNodeType().equals(NodeType.Task)){
//						//任务环节的ID
//						Long id1 = nd.getId();
//						hSession = context.getSession();
//						
//						TaskNode td = (TaskNode) hSession.get(TaskNode.class, id1);
//						Task task = (Task) (td.getTasks().iterator().next());
//
////						String actorIds = getActorIdsByTask(task,ti );
////						actorIds += task.getPooledActorsExpression() + ",";
//						
//						//根据任务信息获取处理人信息
//						actorIds += getActorIdsByTask(task,ti ) + ",";
//					}
//				}
//				if(actorIds.length() > 0)	actorIds = StringUtil.delEndString(actorIds, ",");
//				actorIds = this.delSameElementAsList(actorIds);
//				String[] temp = actorIds.split(",");
//				String username = "";
//				//将处理人登录名转换为用户名
//				for(int j = 0;j < temp.length;j++){
//					if(temp[j].equals(Constants.ADMINISTRATOR_ACCOUNT)){
//						username += Constants.ADMINISTRATOR_NAME + ",";
//					}
//					else{
//						username += personManager.findUniqueBy("userLoginName", temp[j]).getUserName() + ",";
//					}
//				}
//				if(username.length() > 0){
//					username = StringUtil.delEndString(username, ",");
//				}
//				sb.append("'username':'" + username + "',");
//				sb.append("'actors':'" + actorIds + "'");
//			}else if(n.getNodeType().equals(NodeType.EndState)){
//				sb.append("'taskName':'流程结束',");
//				sb.append("'actors':'',");
//				sb.append("'username':''");
//			}
//			
//		}
//		sb.append("}");
//	} 
	
	/**
	 * 删除字符串中的相同的元素．如"a,b,b,a"变为"a,b"
	 * @author 肖平松
	 * @version Oct 30, 2009
	 * @param str
	 * @return
	 */
	public String delSameElementAsList(String str){
		Set set = new HashSet();
		String[] strList = str.split(",");
		for(int i = 0;i < strList.length;i++){
			set.add(strList[i]);
		}
		String retString = set.toString();
		retString = StringUtil.deleteWhitespace(retString);
		retString = StringUtil.delStartString(retString, "[");
		retString = StringUtil.delEndString(retString, "]");
		return retString;
	}
	
	/**
	 * 获取Transition的默认路由
	 * @author 肖平松
	 * @version Oct 26, 2009
	 * @param flowConfigId
	 * @param ts
	 * @return
	 */
	public Boolean getDefaultRouter(String flowConfigId, Transition ts) {
		
		if(ts.getName() == null)	return false;
		
		FlowConfig fc = flowConfigManager.get(flowConfigId);
		String xml = fc.getDefXml();
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Element mxGraphModel = null;
		Element root = null;
		mxGraphModel = document.getRootElement();
		root = mxGraphModel.element("root");

		//所有连线
		Element edge = null;
		List list = root.elements();
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if (e.getName().equals("Edge") && e.attributeValue("label").equals(ts.getName())) {
				edge = e;
			}
		}
		if(edge == null)	return false;
		String router = edge.attributeValue("router");
		if(router == null)	return false;
		HashMap routerMap = (HashMap) JSONUtil.parseObject(router);
		Boolean defaultRouter = (Boolean) routerMap.get("is_default_route");
		if(defaultRouter == null)	defaultRouter = false;
		return defaultRouter;
	}

	/**
	 * 获取下一个节点的执行人
	 *@author  MrBao 
	 *@date 	  2009-7-31
	 *@param node
	 *@param processInstanceId
	 *@return
	 *@return String
	 *@remark
	 */
	public String getNextTaskActorByNode(final TaskInstance ti) {
		
		final StringBuffer sb = new StringBuffer();
		Object obj = jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) {
				Node node = ti.getTask().getTaskNode();
				Long processInstanceId = ti.getProcessInstance().getId();
				getNextTaskInfoByNode(ti, context ,"taskActor",sb);
				return null; 
			} 
		}); 
		String actorIds = sb.toString();
		actorIds = StringUtil.delEndString(actorIds, ",");
		if(actorIds == null)	actorIds = "";
		return actorIds;
	}
	
	/**
	 * 获取下一个节点的信息
	 *@author  MrBao 
	 *@date 	  2009-7-31
	 *@param node
	 *@param context
	 *@param taskPropertyName    taskName，taskActor
	 *@param sb
	 *@param processInstanceId
	 *@return void
	 *@remark
	 */
	public void getNextTaskInfoByNode(TaskInstance ti, JbpmContext context, String taskPropertyName,StringBuffer sb) {
		Node node = ti.getTask().getTaskNode();
		List list = node.getLeavingTransitions();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Transition tr = (Transition) it.next();
			Node n = tr.getTo();
			String name = n.getName();
			//如果下一环节是节点
			if (n.getNodeType().equals(NodeType.Task)){
				Long id = n.getId();
				Session hSession = context.getSession();
				TaskNode td = (TaskNode) hSession.get(TaskNode.class, id);
				Task task = (Task) (td.getTasks().iterator().next());
				
				if(taskPropertyName.equals("taskName")){
					//任务名称之间用"~"分隔
					sb.append(task.getName() + "~");
				}else if(taskPropertyName.equals("taskActor")){
					String actorIds = getActorIdsByTask(task,ti );
					//处理人之间用"~"分隔
					sb.append(actorIds + "~");
				}

			}
			//如果下一环节是终结环节
			else if(n.getNodeType().equals(NodeType.EndState)){
				Long id = n.getId();
				Session hSession = context.getSession();
				EndState td = (EndState) hSession.get(EndState.class, id);
				
				if(taskPropertyName.equals("taskActor")){
					sb.append("~");
				}else if(taskPropertyName.equals("taskName")){
					sb.append(td.getName() + "~");
				}
			}
			//如果下一环节是分支
			else if(n.getNodeType().equals(NodeType.Fork)){
				Long id = n.getId();
				Session hSession = context.getSession();
				Fork fork = (Fork) hSession.get(Fork.class, id);
				List<Transition> tsList = fork.getLeavingTransitionsList();
				StringBuffer actors = new StringBuffer();
				for(Transition ts : tsList){
					Node nd = ts.getTo();
					if(nd.getNodeType().equals(NodeType.Task)){
						Long id1 = nd.getId();
						hSession = context.getSession();
						
						TaskNode td = (TaskNode) hSession.get(TaskNode.class, id1);
						Task task = (Task) (td.getTasks().iterator().next());
						
						if(taskPropertyName.equals("taskName")){
							sb.append(task.getName() + "~");
						}else if(taskPropertyName.equals("taskActor")){
							String actorIds = getActorIdsByTask(task,ti );
							sb.append(actorIds + "~");
						}
					}
				}
			}
			//如果下一环节是汇集
			else if(n.getNodeType().equals(NodeType.Join)){
				Long id = n.getId();
				Session hSession = context.getSession();
				Join join = (Join) hSession.get(Join.class, id);
				List<Transition> tsList = join.getLeavingTransitionsList();
				StringBuffer actors = new StringBuffer();
				for(Transition ts : tsList){
					Node nd = ts.getTo();
					if(nd.getNodeType().equals(NodeType.Task)){
						Long id1 = nd.getId();
						hSession = context.getSession();
						
						TaskNode td = (TaskNode) hSession.get(TaskNode.class, id1);
						Task task = (Task) (td.getTasks().iterator().next());
						
						if(taskPropertyName.equals("taskName")){
							sb.append(task.getName() + "~");
						}else if(taskPropertyName.equals("taskActor")){
							String actorIds = getActorIdsByTask(task,ti );
							sb.append(actorIds + "~");
						}
					}
				}
			}
		}
		
		if(sb.length() > 0 && sb.charAt(sb.length()-1) == '~'){
			sb.delete(sb.length()-1, sb.length());
		}
	}
	/**
	 * 根据流程实例取得所有的任务实例
	 * @param pid
	 * @return
	 */
	public List<TaskInstance> findTaskInstanceByProcessInstance(final Long pid){
		Object list=jbpmTemplate.execute(new JbpmCallback(){

			public Object doInJbpm(JbpmContext context) throws JbpmException {
				ProcessInstance  processInstance = context.getProcessInstance(pid);
				List<TaskInstance> list = context.getTaskMgmtSession().findTaskInstancesByProcessInstance(processInstance);
				return list;
			}});
		return (List<TaskInstance>) list;
		
	}
	
	public TaskInstance findCurrentTaskInstance(Long pid,String userLoginName){
		return findCurrentTaskInstance(pid,userLoginName,null);
	}
	/**
	 * 根据流程实例编号和当前用户登录名称取得当前流程实例的任务实例对象
	 * 如果登录用户使系统管理员，则取得第一个任务实例并返回,否则根据用
	 * 户取得当前用户的并属于指定流程实例的任务实例并返回
	 * @param pid
	 * @param userLoginName
	 * @return
	 */
	public TaskInstance findCurrentTaskInstance(Long pid,String userLoginName,String token){
		ProcessInstance pi=jbpmTemplate.findProcessInstance(pid);
		if(userLoginName==null || userLoginName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
			List<TaskInstance> list = findTaskInstanceByProcessInstance(pid);
			return list.size()>0?list.get(0):null;
		}
		
		List<TaskInstance> list=jbpmTemplate.findPooledTaskInstances(userLoginName);
		for (TaskInstance ti : list) {
			if(StringUtil.isNotEmpty(token)){
				if(ti.getToken().getId() == Long.parseLong(token)) return ti;
			}else{
				if(ti.getProcessInstance().getId() == pid) return ti;
			}
		}
		return null;
	}
	public TaskInstance findCurrentTaskInstance(Long pid){
		return findCurrentTaskInstance(pid,null);
	}
	
	/**
	 * 根据指定的node id查询其上一个环节对象
	 * @param taks
	 * @return
	 */
	public List<Node> findPrevTaskNode(long nodeId){
		String hql="from org.jbpm.graph.def.Transition t where t.to.id = ?";
		List<Transition> list = jbpmTemplate.getHibernateTemplate().find(hql,new Object[]{nodeId});
		List<Node> result = new ArrayList<Node>();
		for (Transition transition : list) {
			result.add(transition.getFrom());
		}
		return result;
	}
	
	/**
	 * 根据节点名称取得节点对象
	 * @param nodeName
	 * @param pid
	 * @return
	 */
	public Node findTaskNodeByNodeName(String nodeName,Long pid){
		if(StringUtil.isEmpty(nodeName))
			return null;
		String hql="from org.jbpm.graph.def.Node n where n.name = ? and n.processDefinition.id = ?";
		System.out.println(hql);
		List<Node> list = jbpmTemplate.getHibernateTemplate().find(hql,new Object[]{nodeName,pid});
		Node result = null;
		if(list.size()>0){
			result = list.get(0);
		}
		return result;
	}
	
	/**
	 * 查找所有结点配置已经经过的节点配置对象集合
	 * @author 肖平松
	 * @version Oct 14, 2009
	 * @param flowConfigId	流程配置编号
	 * @return	所有结点配置
	 */
	public List<NodeConfig> findAllTaskNode(String flowConfigId,Long pid){
		ProcessInstance pi=jbpmTemplate.findProcessInstance(pid);
		String hql = "from NodeConfig nc where nc.flowConfig.id=? and nc.name in(select name from org.jbpm.taskmgmt.exe.TaskInstance t where procinst_=?)";
		List<NodeConfig> list = jbpmTemplate.getHibernateTemplate().find(hql,new Object[]{flowConfigId,pid});
		return list;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public TaskToDoManager getTaskToDoManager() {
		return taskToDoManager;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	/**
	 * 根据环节名称找结点(Node)
	 * @author 肖平松
	 * @version Oct 14, 2009
	 * @param name 环节名称
	 * @return 结点
	 */
	public Node getTaskNodeByName(String name) {
		String hql="from org.jbpm.graph.def.Transition t where t.to.name = ?";
		List<Transition> list = jbpmTemplate.getHibernateTemplate().find(hql,new Object[]{name});
		return list.get(0).getTo();
	}

	/**
	 * 根据环节名称获取任务
	 * @author 肖平松
	 * @version Oct 15, 2009
	 * @param name	环节名称
	 * @return	任务
	 */
	public Task getTaskByName(final String name) {
		Object obj = jbpmTemplate.execute(new JbpmCallback() { 
			public Object doInJbpm(JbpmContext context) {
				Node n = getTaskNodeByName(name);
				Long id = n.getId();
				Session hSession = context.getSession();
				TaskNode td = (TaskNode) hSession.get(TaskNode.class, id);
				return td;
			} 
		}); 
		
		return (Task) (((TaskNode) obj).getTasks().iterator().next());
	}

	/**
	 * 跳转到指定的环节
	 * @author 肖平松
	 * @version Nov 10, 2009
	 * @param pi				流程实例
	 * @param node				环节或者会签名称
	 * @param actors			处理人
	 * @param curUserLoginName	当前用户登录名
	 * @return
	 */
	public List<TaskInstance> gotoSelectedNode(ProcessInstance pi,final String node, String actors,String curUserLoginName) {
		List<TaskInstance> retList = new ArrayList<TaskInstance>();
		
		// 获取指定环节的节点
		Node n = this.getTaskNodeByName(node);
		// 如果该节点是任务环节
		if(n.getNodeType().equals(NodeType.Task)){
			// 得到任务实例集合
			List<TaskInstance> tiList = this.getTaskInstancesByTaskName(node, pi);
			for(int i = 0;i < tiList.size();i++){
				TaskInstance ti = tiList.get(i);
				
				// 取任务跳转所指向的节点
				Node node1 = ti.getTask().getTaskNode();
				if (node1 == null) {
					node1 = ti.getTask().getStartState();// 有可能是要退到头的
				}
				// token指向跳转节点
				ti.getToken().setNode(node1);
				//关闭当前未完成任务
				closeTask(pi,ti.getToken());
				startTask(ti);
				
				retList.add(ti);
			}
		}
		//如果是会签
		else if(n.getNodeType().equals(NodeType.Join)){
			List<Transition> tsList = n.getLeavingTransitionsList();
			for(int i = 0;i < tsList.size();i++){
				Transition ts = tsList.get(i);
				String nodeName = ts.getTo().getName();
				TaskInstance ti = this.getTaskInstanceByTaskName(nodeName, pi);
				Node node2 = ti.getTask().getTaskNode();// 取任务跳转所在节点
				if (node2 == null) {
					node2 = ti.getTask().getStartState();// 有可能是要退到头的
				}
				ti.getToken().setNode(node2);
				//关闭当前未完成任务
				closeTask(pi,ti.getToken());
				startTask(ti);
				
				retList.add(ti);
			}
			
		}
		return retList;
	}
//	
//	private Transition getTransitionByTaskName(String node) {
//		String hql="from org.jbpm.graph.def.Transition t where t.to.name = ?";
//		List<Transition> list = jbpmTemplate.getHibernateTemplate().find(hql,new Object[]{node});
//		return list.get(0);
//	}
//	

	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}

//	public String getCurLoginUserName() {
//		return curLoginUserName;
//	}
//
//	public void setCurLoginUserName(String curLoginUserName) {
//		this.curLoginUserName = curLoginUserName;
//	}
}
