package com.jteap.wfengine.tasktodo.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wfengine.tasktodo.model.TaskToDo;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.model.FlowConfig;

/**
 * 待办任务管理器
 * 
 * @author 肖平松
 * @version Sep 24, 2009
 */
@SuppressWarnings({"unchecked"})
public class TaskToDoManager extends HibernateEntityDao<TaskToDo> {

	private FlowConfigManager flowConfigManager;
	// 流程操作管理器
	private JbpmOperateManager jbpmOperateManager;

	public JbpmOperateManager getJbpmOperateManager() {
		return jbpmOperateManager;
	}

	public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager) {
		this.jbpmOperateManager = jbpmOperateManager;
	}

	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}

	public void setDaibanFlag(String dbid, boolean b) {
		TaskToDo todo = this.get(dbid);
		todo.setFlag(b);
		save(todo);
	}

	/**
	 * 通过TOKEN将待办设置为无效
	 * @author 肖平松
	 * @version Nov 17, 2009
	 * @param pid		流程实例编号
	 * @param token		TOKEN编号
	 */
	public void disableTaskToDoByToken(final String pid, final String token, String dealPerson) {
		String hql = "from TaskToDo t where t.flowInstance = ? and t.token = ?";
		List<TaskToDo> lst = this.find(hql, new Object[]{pid, token});
		for (TaskToDo taskToDo : lst) {
			taskToDo.setFlag(false);
			String alertWindowUrl = "url=/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do?pid="
				+pid+"&token="+token+"&isEdit=false";
			taskToDo.setAlertWindowUrl(alertWindowUrl);
			taskToDo.setStatus("已处理");
			taskToDo.setDealPerson(dealPerson);
			taskToDo.setDealTime(new Date());
			this.save(taskToDo);
		}
	}
	
	/**
	 * 通过结点名称将待办设置为无效
	 * @author 肖平松
	 * @version Nov 17, 2009
	 * @param pid		流程实例编号
	 * @param nodeName	结点名称
	 */
	public void disableTaskToDoByNodeName(final String pid, final String nodeName, String dealPerson) {
		String hql = "from TaskToDo t where t.flowInstance = ? and t.curTaskName = ?";
		List<TaskToDo> lst = this.find(hql, new Object[]{pid, nodeName});
		for (TaskToDo taskToDo : lst) {
			taskToDo.setFlag(false);
			String alertWindowUrl = "url=/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do?pid="
				+pid+"&token="+taskToDo.getToken()+"&isEdit=false";
			taskToDo.setAlertWindowUrl(alertWindowUrl);
			taskToDo.setStatus("已处理");
			taskToDo.setDealPerson(dealPerson);
			taskToDo.setDealTime(new Date());
			this.save(taskToDo);
		}
	}

	/**
	 * 将当前待办的flag设置为false,并新建一个待办,去掉当前登录用户
	 * 
	 * @author 肖平松
	 * @version Nov 2, 2009
	 * @param pid
	 * @param token
	 * @param actor
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public String removeCurActor(final String pid, final String token,
			String actor) {
		List<TaskToDo> list = this
				.find(
						"from TaskToDo t where t.flowInstance = ? and t.token = ? and t.flag = true",
						pid, token);
		if (list.size() != 1) {
			return null;
		}
		TaskToDo ttd = (TaskToDo) list.get(0);
		String curProcessPerson = ttd.getCurNodePerson();
		String[] strList = curProcessPerson.split(",");
		String newProcessPerson = "";
		for (int i = 0; i < strList.length; i++) {
			if (!strList[i].equals(actor)) {
				newProcessPerson += strList[i] + ",";
			}
		}
		ttd.setFlag(false);
		// 如果有处理人,就删除最后的","
		if (newProcessPerson.length() > 0) {
			newProcessPerson = StringUtil.delEndString(newProcessPerson, ",");
			TaskToDo tasktodo = new TaskToDo();
			tasktodo.setFlowInstance(ttd.getFlowInstance());
			tasktodo.setPostPerson(ttd.getPostPerson());
			tasktodo.setFlowConfig(ttd.getFlowConfig());
			tasktodo.setFlowname(ttd.getFlowname());
			tasktodo.setCurNodePerson(newProcessPerson);
			tasktodo.setCurTaskName(ttd.getCurTaskName());
			tasktodo.setPostTime(new Date());
			tasktodo.setDocId(ttd.getDocId());
			tasktodo.setFlowTopic(ttd.getFlowTopic());
			tasktodo.setFlag(true);// 设置为新的待办
			tasktodo.setToken(ttd.getToken());
			this.save(tasktodo);
		}
		this.save(ttd);
		return newProcessPerson;
	}

	/**
	 * 创建新的待办
	 * @author 肖平松
	 * @version Nov 3, 2009
	 * @param fc
	 * @param pi
	 * @param postPerson
	 * @param taskActor
	 * @param taskName
	 * @param topic
	 * @param docid
	 * @param token
	 */
	private void createToDo(FlowConfig fc,
			ProcessInstance pi, String postPerson, String taskActor, String taskName,String topic, String docid) {
		String[] taskActorArr = taskActor.split("~");
		String[] taskArr = taskName.split("~");
		for (int i = 0; i < taskArr.length; i++) {
			String tn = taskArr[i];
			TaskInstance ti = jbpmOperateManager.getOpenTaskInstanceByTaskName(tn,pi);
			if (ti != null) {
				// 首页显示处理状态
				String status = "待处理";
				// 待办窗口
				String alertWindowUrl = "wfForm=pid="+pi.getId()+"&token="+ti.getToken().getId()+"&isEdit=true";
				// 子系统名称
				String childSystemName = fc.getFlowCatalog().getCatalogName();
				TaskToDo tasktodo = new TaskToDo();
				tasktodo.setFlowInstance(pi.getId() + "");
				tasktodo.setPostPerson(postPerson);
				tasktodo.setFlowConfig(fc);
				tasktodo.setFlowname(fc.getName());
				tasktodo.setCurNodePerson(taskActorArr[i]);
				tasktodo.setCurTaskName(taskArr[i]);
				tasktodo.setPostTime(new Date());
				tasktodo.setDocId(docid);
				tasktodo.setFlowTopic(topic);
				tasktodo.setFlag(true);
				tasktodo.setToken(ti.getToken().getId() + "");
				tasktodo.setStatus(status);
				tasktodo.setAlertWindowUrl(alertWindowUrl);
				tasktodo.setChildSystemName(childSystemName);
				this.save(tasktodo);
			}
		}

	}
	
	/**
	 * 保存待办任务
	 * @param fc			流程配置
	 * @param pi			流程实例
	 * @param postPerson 	发送人
	 * @param taskActor		环节处理人
	 * @param taskName		环节名称
	 * @param topic			待办主题
	 * @param docid			业务对象编号
	 * @param token			当前流程令牌编号
	 * @param processKind	处理模式(单人/多人)
	 * @param curLoginName	当前登录用户
	 */
	public void saveToDo(FlowConfig fc,ProcessInstance pi,String postPerson, String taskActor, String taskName,
			String topic, String docid,String curToken,String processKind,String curLoginName) {
		//如果是单人处理
		if(processKind == null || processKind.equals("single")){
			this.disableTaskToDoByToken(pi.getId()+"", curToken, curLoginName);
			this.createToDo(fc,pi,postPerson,taskActor,taskName,topic,docid);
		}
		//如果是多人处理
		else{
			String curActor = this.removeCurActor(pi.getId()+"",curToken, curLoginName);
			if(StringUtil.isEmpty(curActor)){
				this.createToDo(fc, pi, postPerson, taskActor, taskName, topic, docid);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public TaskToDo findByPidAndToken(String pid, String token) {
		String hql = "from TaskToDo where flowInstance = ? and token=? and flag='1'";
		List<TaskToDo> list = this.find(hql, new Object[] {pid, token});
		if (list.size() > 0) 
			return list.get(0);
		
		return null;
	}
	
	/**
	 * 获取当前登录人的待办事项
	 * @param curPersonLoginName		当前登录名
	 * @param flag						(true 获取未处理的待办事项, false 获取已处理的待办事项)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Map<String, List<TaskToDo>> findCurrentLoginDeals(String curPersonLoginName, boolean flag){
		Map<String, List<TaskToDo>> dealsMap = new LinkedHashMap<String, List<TaskToDo>>();
		
		//当前日期 yyyy-MM-dd
		String currentDate = "";
		String beforDate = "";
		
		String flagStr = "";
		if(flag){
			flagStr = "1";
		}else {
			flagStr = "0";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date nowDate = new Date();
			currentDate = dateFormat.format(nowDate);
			beforDate = dateFormat.format(new Date(nowDate.getYear(),nowDate.getMonth(),nowDate.getDate()-7));
		}
		
		//查询出 所有子系统名称
		String hql = "select t.childSystemName from TaskToDo t where t.flag=" + flagStr + " group by t.childSystemName";
		List<String> childSysNameList = this.find(hql);
		
		//遍历子系统名称 查询出各子系统的待办事项
		for(String childSysName : childSysNameList){
			//系统管理可以查看所有 待办事项
			if("root".equals(curPersonLoginName)){
				hql = "from TaskToDo t where t.childSystemName='" + childSysName + "'";
			}else {
				hql = "from TaskToDo t where t.curNodePerson like '%" + curPersonLoginName + "%' and t.childSystemName='" + childSysName + "'";
			}
			hql += " and t.flag=" + flagStr;
			
			//如果是查询已办事项 只查询当前时间~7天前的数据	
			if(!flag){
				hql += " and to_char(t.dealTime,'yyyy-mm-dd')<='" + currentDate + "' and to_char(t.dealTime,'yyyy-mm-dd')>='" + beforDate + "'";
				hql += " order by t.dealTime desc";
			}else{
				hql += " order by t.postTime desc";
			}
			
			List<TaskToDo> list = this.find(hql);
			if(list.size() > 0){
				dealsMap.put(childSysName, list);
			}
		}
		return dealsMap;
	}

	/**
	 * 
	 * 描述 : 根据当前登录人获得所有的待办
	 * 作者 : wangyun
	 * 时间 : Aug 30, 2010
	 * 参数 : 
	 * 		curPersonLoginName ： 当前登录人
	 * 返回值 : 
	 * 		lst ： 所有的待办
	 * 
	 */
	public List<TaskToDo> findAllTodo(String curPersonLoginName) {
		String hql = "from TaskToDo t where t.flag='1'";
		if (!Constants.ADMINISTRATOR_ACCOUNT.equals(curPersonLoginName)) {
			hql += "and t.curNodePerson like '%"+curPersonLoginName+"%'";
		}
		List<TaskToDo> lst = this.find(hql);
		return lst;
	}
	
	/**
	 * 创建待办事项
	 * @param docId					业务数据Id
	 * @param status				业务数据状态 	(未完成、已完成; 待审核,已审核;...   根据具体业务来定)
	 * @param alertWindowUrl		弹出窗口路径 	(如弹出的窗口为自己写的jsp,则以"url="开头; 如弹出的窗口为efrom表单,则以"formSn="开头; 如弹出的窗口为wfForm表单,则以"wfForm="开头;)
	 * @param flag					是否处理过   	(true未处理,false已处理)
	 * @param childSystemName		子系统名称   	("运行管理、设备管理、检修管理...")
	 * @param flowTopic				待办事项标题 
	 * @param curNodePerson			待处理人登录名	(如有多个待处理人 以","隔开)
	 * @param postTime				待办发送时间
	 * @return						TaskToDo 	待办事项实体,用于获取taskId 用于保存到业务数据中
	 */
	public TaskToDo createDeals(String docId, String status, String alertWindowUrl, boolean flag, String childSystemName, String flowTopic, String curNodePerson, Date postTime){
		TaskToDo taskToDo = new TaskToDo();
		taskToDo.setDocId(docId);
		taskToDo.setStatus(status);
		taskToDo.setAlertWindowUrl(alertWindowUrl);
		taskToDo.setFlag(flag);
		taskToDo.setChildSystemName(childSystemName);
		taskToDo.setFlowTopic(flowTopic);
		taskToDo.setCurNodePerson(curNodePerson);
		taskToDo.setPostTime(postTime);
		
		this.save(taskToDo);
		return taskToDo;
	}
	
	/**
	 * 完成待办事项
	 * @param taskId			待办事项Id
	 * @param flag				是否处理过	(true未处理,false已处理) 
	 * @param status			业务数据状态 	(未完成、已完成; 待审核,已审核;...   根据具体业务来定)
	 * @param alertWindowUrl	弹出窗口路径 	(因为处理业务后,参数会改变)(如弹出的窗口为自己写的jsp,则以"url="开头; 如弹出的窗口为efrom表单,则以"formSn="开头; 如弹出的窗口为wfForm表单,则以"wfForm="开头;)
	 * @param dealPeson			完成人登录名	(如有多个待处理人 以","隔开)
	 * @param dealTime			完成时间
	 */
	public void saveDeals(String taskId, boolean flag, String status, String alertWindowUrl, String dealPeson, Date dealTime){
		TaskToDo taskToDo = this.get(taskId);
		taskToDo.setFlag(flag);
		taskToDo.setAlertWindowUrl(alertWindowUrl);
		taskToDo.setStatus(status);
		taskToDo.setDealPerson(dealPeson);
		taskToDo.setDealTime(dealTime);
		
		this.save(taskToDo);
	}

	/**
	 * 
	 * 描述 : 改变待办任务中处理人 
	 * 作者 : wangyun
	 * 时间 : 2010-9-26
	 * 参数 : 
	 * 		pid ： 流程实例ID
	 * 		token ： 流程token
	 * 		curNodePerson ： 待处理人
	 * 返回值 : 无
	 * 异常 : Exception
	 */
	public void changeCurNodePerson(long pid, String token, String curNodePerson) throws Exception {
		try {
			String hql = "from TaskToDo t where t.flowInstance='"+String.valueOf(pid)+"' and t.token='"+token+"' and t.flag='1'";
			List<TaskToDo> list = this.find(hql);
			if (list.size() > 0) {
				TaskToDo taskToDo = list.get(0);
				taskToDo.setCurNodePerson(curNodePerson);
				this.save(taskToDo);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
