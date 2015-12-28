package com.jteap.wfengine.tasktodo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.NodeConfigManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;

/**
 * 待办任务动作类
 *
 * @author 肖平松	
 * @version Sep 24, 2009
 */
@SuppressWarnings("serial")
public class TaskToDoAction extends AbstractAction {
	// 待办任务实例管理器
	private TaskToDoManager taskToDoManager;
	// 流程操作管理器
	private JbpmOperateManager jbpmOperateManager;

	private FlowConfigManager flowConfigManager;
	private NodeConfigManager nodeConfigManager;

	
	private WorkFlowLogManager workFlowLogManager;
	
	private PersonManager personManager;


	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}

	public JbpmOperateManager getJbpmOperateManager() {
		return jbpmOperateManager;
	}

	public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager) {
		this.jbpmOperateManager = jbpmOperateManager;
	}

	public TaskToDoManager getTaskToDoManager() {
		return taskToDoManager;
	}

	public void setTaskToDoManager(
			TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		//只显示自己的任务
		HqlUtil.addCondition(hql, "curNodePerson", sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME),HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		
		//只显示没有处理的任务
		HqlUtil.addCondition(hql, "flag", 1,HqlUtil.LOGIC_AND);
		
		//查询条件
		String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
		//在一类型日志中查询
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}

	/**
	 * 
	 * 描述 : 释放签收
	 * 作者 : wangyun
	 * 时间 : Jul 16, 2010
	 * 异常 : Exception
	 */
	public String releaseSignInAction() throws Exception {
		try {
			String pid = request.getParameter("pid");
			String token = request.getParameter("token");
			TaskToDo taskToDo = taskToDoManager.findByPidAndToken(pid, token);
			// 如果没有签收人，则直接返回
			if (taskToDo == null || taskToDo.getCurSignIn() == null) {
				this.outputJson("{success:true}");
				return NONE;
			}

			String curLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
			// 如果签收人为当前登录人，则释放签收，将代办中签收人设置为空
			if (curLoginName.equals(taskToDo.getCurSignIn().getUserLoginName())) {
				if(!("物资需求计划申请").equals(taskToDo.getFlowname())){
					taskToDo.setCurSignIn(null);
					taskToDoManager.save(taskToDo);
					this.outputJson("{success:true}");
				}
			}
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 获得所有未处理待办
	 * 作者 : wangyun
	 * 时间 : Aug 30, 2010
	 * 异常 : Exception
	 * 
	 */
	public String getAllTodoAction() throws Exception {
		try {
			String curPersonLoginName = (String) sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
			List<TaskToDo> lst = taskToDoManager.findAllTodo(curPersonLoginName);
			String ids = "";
			for (TaskToDo taskToDo : lst) {
				ids += taskToDo.getId()+",";
			}
			if (lst.size() > 0) {
				ids = ids.substring(0, ids.lastIndexOf(","));
			}
			String json = "{\"success\":\"1\",\"taskTodoId\":\""+ids+"\"}";
			outputJson(json);
		} catch (Exception e) {
			outputJson("{\"success\":\"0\",\"msg\":\"数据库异常，请联系管理员\"");
			e.printStackTrace();
		}
		return NONE;
	}
	
	@Override
	public String[] listJsonProperties() {
		return new String[] { "id","flowTopic","curTaskName","postTime","time","postPerson","curNodePerson","flowInstance","flowname","flowConfig","id","token","curSignIn","userName","userLoginName"};
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeConfigManager getNodeConfigManager() {
		return nodeConfigManager;
	}

	public void setNodeConfigManager(NodeConfigManager nodeConfigManager) {
		this.nodeConfigManager = nodeConfigManager;
	}

	public WorkFlowLogManager getWorkFlowLogManager() {
		return workFlowLogManager;
	}

	public void setWorkFlowLogManager(WorkFlowLogManager workFlowLogManager) {
		this.workFlowLogManager = workFlowLogManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return taskToDoManager;
	}


}
