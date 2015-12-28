package com.jteap.wfengine.wfi.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.def.Node.NodeType;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.cform.manager.CFormManager;
import com.jteap.form.cform.model.CForm;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;
import com.jteap.wfengine.wfi.manager.WorkFlowInstanceManager;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.NodeConfigManager;
import com.jteap.wfengine.workflow.manager.NodeOperationManager;
import com.jteap.wfengine.workflow.manager.NodePermissionManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.model.NodeOperation;
import com.jteap.wfengine.workflow.model.NodePermission;
import com.jteap.wfengine.workflow.util.WFConstants;
import com.jteap.wfengine.workflow.web.MyJbpmFunctionMapper;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqLogManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;

/**
 * 流程实例动作类
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings( {"serial","unchecked","unused" })
public class WorkFlowInstanceAction extends AbstractAction {

	private NodeOperationManager nodeOperationManager; 
	// 流程实例管理器
	private WorkFlowInstanceManager workFlowInstanceManager;
	private TaskToDoManager taskToDoManager;
	// 流程操作管理器jbpmOperateManager
	private JbpmOperateManager jbpmOperateManager;

	private FlowConfigManager flowConfigManager;
	private NodeConfigManager nodeConfigManager;

	private PhysicTableManager physicTableManager;

	private WorkFlowLogManager workFlowLogManager;

	private PersonManager personManager;
	
	private NodePermissionManager nodePermissionManager;
	
	private EFormManager eformManager;
	
	private CFormManager cformManager;
	
	private JdbcManager jdbcManager;
	
	private XqjhsqLogManager xqjhsqLogManager;
	
	private XqjhsqDetailManager xqjhsqDetailManager;
	
	private XqjhsqManager xqjhsqManager;
	
	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

	public XqjhsqDetailManager getXqjhsqDetailManager() {
		return xqjhsqDetailManager;
	}
	
	public void setXqjhsqDetailManager(XqjhsqDetailManager xqjhsqDetailManager) {
		this.xqjhsqDetailManager = xqjhsqDetailManager;
	}

	public XqjhsqLogManager getXqjhsqLogManager() {
		return xqjhsqLogManager;
	}

	public void setXqjhsqLogManager(XqjhsqLogManager xqjhsqLogManager) {
		this.xqjhsqLogManager = xqjhsqLogManager;
	}

	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	public EFormManager getEformManager() {
		return eformManager;
	}

	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}

	public NodePermissionManager getNodePermissionManager() {
		return nodePermissionManager;
	}

	public void setNodePermissionManager(NodePermissionManager nodePermissionManager) {
		this.nodePermissionManager = nodePermissionManager;
	}

	public PhysicTableManager getPhysicTableManager() {
		return physicTableManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}

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

	public WorkFlowInstanceManager getWorkFlowInstanceManager() {
		return workFlowInstanceManager;
	}

	public void setWorkFlowInstanceManager(
			WorkFlowInstanceManager workFlowInstanceManager) {
		this.workFlowInstanceManager = workFlowInstanceManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSDEFINITION_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID","FLOW_TOPIC"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	/**
	 * 显示指定表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String showListAction() throws Exception {

		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		try {

			String sql = "SELECT distinct a.ID_,a.VERSION_,a.START_,a.END_,b.FLOW_NAME," +
					"b.id as FLOW_CONFIG_ID,b.FLOW_FORM_ID,c.FLOW_TOPIC FROM JBPM_PROCESSINSTANCE a," +
					" tb_wf_flowconfig b,tb_wf_todo c where a.processdefinition_ = b.pd_id " +
					"and a.id_ =c.flow_instance_id";
			String sqlWhere = request.getParameter("queryParamsSql");
//			sqlWhere = StringUtil.isoToUTF8(sqlWhere);
			if (StringUtils.isNotEmpty(sqlWhere)) {
				sql = sql + " AND " + sqlWhere;
			}
			sql += " order by a.start_ desc";
			Page page = workFlowInstanceManager.getPage(sql,
					iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());
 
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);

		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 删除流程实例
	 * 
	 * @author MrBao
	 * @date 2009-7-31
	 * @return
	 * @throws Exception
	 * @return String
	 * @remark
	 */
	public String deleteProcessInstranceAction() throws Exception {
		try {
			String ids = request.getParameter("ids");
			if (StringUtils.isNotEmpty(ids)) {
				String idsTmpArray[] = ids.split(",");
				String docId = "";
				ProcessInstance pi = null;
				for (String spid : idsTmpArray) {
					if (StringUtils.isNotEmpty(spid)) {
						// 流程实例ID
						Long pid = Long.parseLong(spid);
						// 流程实例
						pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(pid);
						Long pdefId = pi.getProcessDefinition().getId();
						docId = (String) pi.getContextInstance().getVariable("docid");
						
						// 删除流程实例
						jbpmOperateManager.deleteProcessInstance(pid);
						// 删除待办任务实例
						List<TaskToDo> todoList = taskToDoManager.findBy(
								"flowInstance", Long.toString(pid));
						for (int i = 0; i < todoList.size(); i++) {
							TaskToDo todo = todoList.get(i);
							taskToDoManager.remove(todo);
						}
						// 删除业务数据
						deleteBusinessData(pdefId, docId);
					}
				}
			}
			this.outputJson("{success:true}");
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 删除业务数据
	 * 作者 : wangyun
	 * 时间 : 2010-12-2
	 * 参数 : 
	 * 		pid ： 流程实例ID
	 * 异常 : Exception
	 */
	private void deleteBusinessData(Long pid, String docid) throws Exception {
		FlowConfig flowConfig = flowConfigManager.findUniqueBy("pd_id", pid);
		String formId = flowConfig.getFormId();
		EForm eform = eformManager.get(formId);
		DefTableInfo tableInfo = eform.getDefTable();
		try {
			String schema = SystemConfig.getProperty("jdbc.schema");
			String pk = physicTableManager.findPrimaryKeyColumnNameList(schema, tableInfo.getTableCode()).get(0);
			String sql = "DELETE "+schema+"."+tableInfo.getTableCode()+" WHERE "+pk+" = '"+docid+"'";
			physicTableManager.executeSqlBatch(sql);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 流程重启
	 * @author 肖平松
	 * @version Nov 6, 2009
	 * @return
	 * @throws Exception
	 */
	public String resetFlowAction() throws Exception {
		try{
			String id = request.getParameter("id");
			Long pid = Long.parseLong(id);
			ProcessInstance pi = jbpmOperateManager.getJbpmTemplate()
					.findProcessInstance(pid);
			pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
			Long pd_id = pi.getProcessDefinition().getId();
			
			// 流程配置对象
			FlowConfig flowConfig = flowConfigManager.findFlowConfigByPDID(pd_id);

			// 表单类型
			String formType = flowConfig.getFormtype();
			// 表单ID
			String formId=request.getParameter("formId");
			// 业务数据ID
			String docid = StringUtil.dealNull(pi.getContextInstance().getVariable("docid"));
			//为了避免json字符串为空导致报错
			String docRecJson = "{}";
			// 如果是EFORM
			if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
				EForm eform = eformManager.get(formId);
				if (StringUtil.isNotEmpty(docid)) {
					Map rec = physicTableManager.getRecById(docid, eform.getDefTable().getSchema(), eform.getDefTable().getTableCode());
					docRecJson = JSONUtil.mapToJson(rec);
				}
				// 如果是CFORM
			} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
				CForm cform = cformManager.get(formId);
				if (StringUtil.isNotEmpty(docid)) {
					Map rec = physicTableManager.getRecById(docid, cform.getDefTable().getSchema(), cform.getDefTable().getTableCode());
					docRecJson = JSONUtil.mapToJson(rec);
				}
			}
			
			String flowConfigId = flowConfig.getId();
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(
					pi.getId(), sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString());
			String taskName = "";
			String nextActor = "";
			String nextNodes = "";
			if (ti == null) {
				this.outputJson("{success:true,msg:'流程已经结束或者您不是处理人，无法重启!'}");
				return NONE;
			} else {
				taskName = ti.getName();
				String nextTaksActor = jbpmOperateManager
						.getNextTaskActorByNode(ti);
				nextActor = nextTaksActor;
				if (nextTaksActor != null && !nextTaksActor.equals("")) {
					String[] nextTasksActorArray = nextTaksActor.split("~");
					String tempActor = "";
					for (int i = 0; i < nextTasksActorArray.length; i++) {
						String[] nextTaskActorArray = nextTasksActorArray[i].split(",");
						for (int j = 0; j < nextTaskActorArray.length; j++) {
							tempActor += personManager.findPersonByLoginName(
									nextTaskActorArray[j]).getUserName()
									+ ",";
						}
					}
					nextActor = tempActor.substring(0, tempActor.length() - 1);
				}
				nextNodes = jbpmOperateManager.getNextTaskNameByNode(ti);
			}
	
			String startNodeName = workFlowInstanceManager
					.findStartNodeName(flowConfigId);
			String taskResult = "流程重启至<" + startNodeName + ">";
	
			TaskInstance backTaskInstance = jbpmOperateManager
					.getTaskInstanceByTaskName(startNodeName, pi);
	
			Node node = backTaskInstance.getTask().getTaskNode();// 有可能是要退到头的
	
			backTaskInstance.getToken().setNode(node);
			// 关闭当前未完成任务
			jbpmOperateManager.closeTask(pi,null);
			// 重新启动回退任务实例
			jbpmOperateManager.startTask(backTaskInstance);
			
			//当前环节处理人
			String curTaskActor = jbpmOperateManager.getActorIdsByTask(ti.getTask(), ti, this.getCurUserLoginName());
			
			String topic = flowConfig.getTopicCF();
			HashMap jsonMap = (HashMap) JSONUtil.parseObjectHasDate(docRecJson);
			topic = evalTodoTopic(topic, jsonMap);
			//保存待办
			taskToDoManager.saveToDo(flowConfig, pi, this.getCurUserName(), curTaskActor, taskName, topic, docid, Long.toString(ti.getToken().getId()), null, this.getCurUserLoginName());
			//写日志
			workFlowLogManager.addFlowLog(pid, taskName, this.getCurUserName(), this.getCurUserLoginName(), nextActor,nextNodes, taskResult, "");
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		this.outputJson("{success:true,msg:'流程重启成功'}");

		return NONE;
	}

	@Override
	public String showUpdateAction() throws BusinessException {
		try{
			String token = request.getParameter("token");
			String pid = request.getParameter("pid");
			// 待办ID
			String id = request.getParameter("id");
			request.setAttribute("dbid", id);
			//公用参数
			String param = request.getParameter("param");
			request.setAttribute("param", param);
			// 情况一 从流程实例 进入 修改方式
			if (StringUtil.isNotEmpty(pid)) {
				// 流程实例
				ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
				Long pd_id = pi.getProcessDefinition().getId();
				// 流程配置对象
				FlowConfig flowConfig = flowConfigManager.findFlowConfigByPDID(pd_id);
				request.setAttribute("pi", pi);
				request.setAttribute("flowConfig", flowConfig);
				// 表单变量
				pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME,this.getCurUserLoginName());
				mappingSystemVar(pi,flowConfig,token);
			} else {
				// 情况二 起草
				String flowConfigId = request.getParameter("flowConfigId"); // 流程配置编号
	
				if (StringUtil.isNotEmpty(flowConfigId)) {
					FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
					request.setAttribute("flowConfig", flowConfig);
					String startNodeName = workFlowInstanceManager
							.findStartNodeName(flowConfigId);
					
					NodeConfig nc = nodeConfigManager.findNodeConfigByName(
							flowConfig.getId(), startNodeName);
					String nodeVarJson = JSONUtil.listToJson(nc.getNodeVariables(),
							new String[] { "variable", "id", "isNeed", "name",
									"name_cn", "type", "value", "kind",
									"storemode", "dispalyStyle" });
					request.setAttribute("process_form_id", nc.getProcess_form_id());
					request.setAttribute("nodeVarJson", nodeVarJson);
					request.setAttribute("curNodeName", startNodeName);
					request.setAttribute("curActors", this.sessionAttrs
							.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME));
					setNodeInfo(flowConfigId,startNodeName);
					setNodeOperation(startNodeName,flowConfig.getId());
	
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return "showForm";
	}

	/**
	 * 
	 * 描述 : 显示流程页面
	 * 作者 : wangyun
	 * 时间 : Jul 23, 2010
	 * 异常 : Exception
	 */
	public String showViewAction() throws Exception {
		String token = request.getParameter("token");
		String pid = request.getParameter("pid");
		// 流程实例
		ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
		Long pd_id = pi.getProcessDefinition().getId();
		// 流程配置对象
		FlowConfig flowConfig = flowConfigManager.findFlowConfigByPDID(pd_id);
		request.setAttribute("pi", pi);
		request.setAttribute("flowConfig", flowConfig);
		// 表单变量
		pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME,this.getCurUserLoginName());
		// 关联业务数据的记录编号
		String docid = StringUtil.dealNull(pi.getContextInstance().getVariable("docid"));
		request.setAttribute("docid", docid);
		request.setAttribute("token", token);
		return "showFormView";
	}

	private void setNodeInfo(String flowConfigId,String nodename){
		//获取当前环节的信息
		List<NodePermission> npList = nodePermissionManager.getNodePermission(flowConfigId, nodename);
		String json = JSONUtil.listToJson(npList, new String[]{"processMode","processKind","isOneProcessActor","isChooseActor"});
		json = StringUtil.delStartString(json, "[");
		json = StringUtil.delEndString(json, "]");
		HashMap jsonmap = (HashMap) JSONUtil.parseObject(json);
//		String processMode = jsonmap.get("processMode")!=null?jsonmap.get("processMode").toString():"";
		String processKind = jsonmap.get("processKind")!=null?jsonmap.get("processKind").toString():"";
//		String isOneProcessActor= jsonmap.get("isOneProcessActor")!=null?jsonmap.get("isOneProcessActor").toString():"";
//		String isChooseActor = jsonmap.get("isChooseActor")!=null?jsonmap.get("isChooseActor").toString():"";
		
		request.setAttribute("processKind", processKind);
	}
	
	@SuppressWarnings("unchecked")
	private void setNodeOperation(String nodeName, String flowConfigId) throws Exception {
		//nodeName = StringUtil.isoToUTF8(nodeName);

		String name = SystemConfig.getProperty("WORKFLOW.OPS2");
		String mark = SystemConfig.getProperty("WORKFLOW.OPS1");
		String[] names = name.split(",");
		String[] marks = mark.split(",");
		//配置文件中的环节操作
		ArrayList arrList = new ArrayList();
		for(int i = 0;i < names.length;i++){
			HashMap map = new HashMap();
			map.put("name", names[i]);
			map.put("mark", marks[i]);
			map.put("show", WFConstants.WF_NODE_SHOW);
			arrList.add(map);
		}

		NodeConfig nc = null;
		ArrayList ncList = (ArrayList) nodeConfigManager.find("from NodeConfig nc where nc.flowConfig.id=? and nc.name=?", flowConfigId,nodeName);
		if(ncList.size() > 1)
			throw new Exception("同一流程内有多个名称相同的环节");
		else if(ncList.size() == 1){
			nc = (NodeConfig) ncList.get(0);
		}

		//数据库中的环节操作
		List noList = nodeOperationManager.findBy("nodeConfig", nc);
		List retJsonList = new ArrayList();


		for(int j = 0;j < noList.size();j++){
			NodeOperation no = (NodeOperation) noList.get(j);

			// 操作权限
			String opPerm_Group = no.getOpPerm_Group();
			String opPerm_Role = no.getOpPerm_Role();
			String actorStr = "";
			// 将角色转换为人员登录名
			if (StringUtil.isNotEmpty(opPerm_Group) || StringUtil.isNotEmpty(opPerm_Role)) {
				actorStr = MyJbpmFunctionMapper.actors(opPerm_Group, opPerm_Role);
			}
			for(int i = 0;i < arrList.size();i++){
				HashMap map = (HashMap) arrList.get(i);
				if(map.get("name").equals(no.getName())){
					// 如果没有操作权限配置，则默认为拥有操作权限；如果配置了操作权限，且当前人员拥有此操作权限，则显示
					if (StringUtil.isEmpty(actorStr) || StringUtil.isNotEmpty(actorStr) && actorStr.indexOf(getCurUserLoginName())> -1) {
						map.put("name", no.getName());
						map.put("mark", no.getMark());
						map.put("show", no.getShow());
						retJsonList.add(map);
						break;
					}
				}
			}
		}
		
		String jsonList = JSONUtil.listToJson(retJsonList, new String[]{"name","mark","show"});
		StringBuffer sbJson = new StringBuffer(jsonList);
		
		//将返回的JSON字符串中的双引号变为单引号，以便作为字符串返回
		StringUtil.replaceAll(sbJson, "\"", "'");
		
		request.setAttribute("showbutton", sbJson);
	}

//	/**
//	 * 处理节点变量
//	 * 
//	 * @param pi
//	 * @param flowConfig
//	 * @throws Exception 
//	 */
//	private void mappingNodeVar(ProcessInstance pi, FlowConfig flowConfig) throws Exception {
//		TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(
//				pi.getId(), this.getCurUserLoginName());
//		
//		if (ti != null) {
//			String nodeName = ti.getName();
//			NodeConfig nc = nodeConfigManager.findNodeConfigByName(flowConfig
//					.getId(), nodeName);
//			String nodeVarJson = JSONUtil.listToJson(nc.getNodeVariables(),
//					new String[] { "variable", "id", "isNeed", "name",
//							"name_cn", "type", "value", "kind", "storemode",
//							"dispalyStyle" });
//			request.setAttribute("nodeVarJson", nodeVarJson);
//			request.setAttribute("curNodeName", nodeName);
//			request.setAttribute("curActors", this.sessionAttrs
//					.get(Constants.SESSION_CURRENT_PERSON_NAME));
//			setNodeInfo(flowConfig.getId(),nodeName);
//			setNodeOperation(nodeName,flowConfig.getId());
//		}
//	}

	/**
	 * 映射系统变量到环境中去
	 * 系统变量 起草人： creator 
	 * 起草时间: creatdt 
	 * 下一个环节： nextNodeName
	 * 下一个环节处理人: nextActors 
	 * 上一个环节： preNodeName 
	 * 上一环节处理人：preNodeActors 
	 * 流程名称： flowName 
	 * 当前环节名称： curNodeName 
	 * 当前环节处理人: curActors 
	 * 流程实例编号： pid 
	 * 流程版本： ver
	 * 业务数据ID docid
	 * 
	 * @param flowConfig
	 * @throws Exception 
	 */
	private void mappingSystemVar(ProcessInstance pi,FlowConfig flowConfig,String token) throws Exception {
		TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pi.getId(),this.getCurUserLoginName(),token);
		if (ti != null) {
			String nodeName = ti.getName();
			NodeConfig nc = nodeConfigManager.findNodeConfigByName(flowConfig.getId(), nodeName);
			String nodeVarJson = JSONUtil.listToJson(nc.getNodeVariables(),
					new String[] { "variable", "id", "isNeed", "name",
							"name_cn", "type", "value", "kind", "storemode",
							"dispalyStyle" });
			request.setAttribute("process_form_id", nc.getProcess_form_id());
			request.setAttribute("nodeVarJson", nodeVarJson);
			request.setAttribute("curNodeName", nodeName);
			request.setAttribute("curActors", this.getCurUserName());
			setNodeInfo(flowConfig.getId(),nodeName);
			setNodeOperation(nodeName,flowConfig.getId());

			// 下一环节的名称及处理人 多个环节之间以~分隔
			String nextNodes = jbpmOperateManager.getNextTaskNameByNode(ti);
			String nextActors = jbpmOperateManager.getNextTaskActorByNode(ti);

			request.setAttribute("nextActors", nextActors);
			request.setAttribute("nextNodes", nextNodes);

			// 上一环节处理人及环节名称
			String preNodeName = "";
			String preActor = "";
			TaskInstance curTi = ti;
			while(curTi!=null){
				preNodeName = StringUtil.dealNull(curTi.getContextInstance().getVariable(curTi.getTask().getTaskNode().getId()+"_"+curTi.getProcessInstance().getId() + "_preNodeName"));
				preActor =  StringUtil.dealNull(curTi.getContextInstance().getVariable(curTi.getTask().getTaskNode().getId()+"_"+curTi.getProcessInstance().getId() + "_preActor"));
				
				if(curTi.getToken().hasParent())	break;
				
				TaskInstance preTi = jbpmOperateManager.getTaskInstanceByTaskName(preNodeName, pi);
				if(preNodeName.equals("") || preTi == null || StringUtil.isEmpty(token))	break;
				//如果TOKEN相同
				if(token.equals(Long.toString(preTi.getToken().getId()))){
					break;
				}
				//否则
				else{
					curTi = jbpmOperateManager.getTaskInstanceByTaskName(preNodeName, pi);
				}
			}
			// 上一步环节信息是在任务结束事件中定义的 定义格式为 tasknode.id_token.id_preNodeName
//			List<Node> preNodes = jbpmOperateManager.findPrevTaskNode(ti
//					.getTask().getParent().getId());
			// 以循环的方式取得上一步环节的名称及处理人，取道对应的值就不循环下去了，主要是考虑到分支的情况
//			for (Node node : preNodes) {
//				//如果上一环节是环节
//				if(node.getNodeType().equals(NodeType.Task)){
//					Object tmpNodeName = ti.getContextInstance().getVariable(
//							node.getId() + "_" + ti.getToken().getId()
//									+ "_preNodeName");
//					Object tmpActors = ti.getContextInstance().getVariable(
//							node.getId() + "_" + ti.getToken().getId()
//									+ "_preActor");
//					if (tmpNodeName != null && tmpActors != null) {
//						preNodeName = tmpNodeName + "";
//						preActor = tmpActors + "";
//						break;
//					}
//				}else if(node.getNodeType().equals(NodeType.Fork)){	//如果上一环节是会签
//					List<Node> list = jbpmOperateManager.findPrevTaskNode(node.getId());
//					Node taskNode = list.get(0);
//					TaskInstance taskInstance = jbpmOperateManager.getTaskInstanceByTaskName(taskNode.getName(), pi);
//					preNodeName = taskNode.getName();
//					preActor = jbpmOperateManager.getActorIdsByTask(taskInstance.getTask(), taskInstance);
//				}
//				else if(node.getNodeType().equals(NodeType.Join)){
//					List<Node> list = jbpmOperateManager.findPrevTaskNode(node.getId());
//					for(int i = 0;i < list.size();i++){
//						Node taskNode = list.get(i);
//						TaskInstance taskInstance = jbpmOperateManager.getTaskInstanceByTaskName(taskNode.getName(), pi);
//						preNodeName += taskNode.getName() + "~";
//						preActor += jbpmOperateManager.getActorIdsByTask(taskInstance.getTask(), taskInstance) + "~";
//					}
//					if(preNodeName.length() > 0)	preNodeName = StringUtil.delEndString(preNodeName, "~");
//					if(preActor.length() > 0)	preActor = StringUtil.delEndString(preActor, "~");
//				}
//			}
			String curActors = getCurActors(ti); // 当前处理人

			request.setAttribute("preNodeName", preNodeName);
			request.setAttribute("preActor", preActor);
			request.setAttribute("curActors", curActors);
			request.setAttribute("token", ti.getToken().getId());
			request.setAttribute("ti", ti);
		}

		// 流程起草人
		String creator = StringUtil.dealNull(pi.getContextInstance().getVariable("creator"));
		// 流程起草时间
		Date creatdt = (Date) pi.getContextInstance().getVariable("creatdt");
		// 流程名称
		String flowName = StringUtil.dealNull(pi.getContextInstance().getVariable("flowName"));
		// 关联业务数据的记录编号
		String docid = StringUtil.dealNull(pi.getContextInstance().getVariable("docid"));

		request.setAttribute("creator", creator);
		request.setAttribute("creatdt", creatdt);
		request.setAttribute("flowName", flowName);

		request.setAttribute("ver", pi.getProcessDefinition().getVersion());
		request.setAttribute("pid", pi.getId());
		request.setAttribute("docid", docid);
	}

	/**
	 * 取得当前任务实例的处理人，以逗号分隔的方式输出多个处理人
	 * 
	 * @param ti
	 * @return
	 */
	private String getCurActors(TaskInstance ti) {
		String curActors = "";
		for (PooledActor act : ti.getPooledActors()) {
			curActors += act.getActorId() + ",";
		}
		String value = "";
		if (curActors.length() > 0)
			value = curActors.substring(0, curActors.length() - 1);
		return value;
	}


	public String saveUpdateAction() throws BusinessException {
		try{
			// 流程实例编号
			String pid = request.getParameter("pid");
			// 业务数据编号
			String docid = request.getParameter("docid");
			// 流程配置编号
			String flowConfigId = request.getParameter("flowConfigId");
			FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
	
			// 表单类型
			String formType = flowConfig.getFormtype();
			HashMap jsonMap = null;
			// 获得业务数据的MAP
			// EFORM
			if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
				String formSn = request.getParameter("formSn");
				EForm eform = eformManager.findUniqueBy("sn", formSn);
				DefTableInfo defTabInfo = eform.getDefTable();
				String tableName = defTabInfo.getTableCode();
				String schema = defTabInfo.getSchema();
				jsonMap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
				for (Object key : jsonMap.keySet()) {
					Object value = jsonMap.get(key);
					if (value != null && value.toString().length() > 4000) {
						jsonMap.put(key, "");
					}
				}
				// CFORM
			} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
				String recordJson = request.getParameter("recordJson");
				jsonMap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
			}
			
			// 环节名称
			String taskName = request.getParameter("curNodeName");
			// 环节处理人
//			String taskActor = request.getParameter("curActors");
			String actor = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
			String actorLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
			// 下一个环节处理人
			String nextTaksActor = request.getParameter("nextActors");
			String nextActor = nextTaksActor;
			String tempActor = "";
			if (!nextTaksActor.equals("")) {
				String[] nextTasksActorTemp = nextActor.split("~");
				for(int i = 0;i < nextTasksActorTemp.length;i++){
					String[] nextTasksActorArray = nextTasksActorTemp[i].split(",");
					for (int j = 0; j < nextTasksActorArray.length; j++) {
						tempActor += personManager.findPersonByLoginName(nextTasksActorArray[j]).getUserName()+ ",";
					}
				}
			}
			// 如果没有流程 则起草一个
			if (StringUtil.isEmpty(pid)) {
				// 起草流程，得到一个新的流程实例
				ProcessInstance pi = draftNewProcess(flowConfig, docid);
				pid = pi.getId()+"";
				// taskManager.saveTask(flowConfigId,StringUtil.strToLong(pid),this.getCurUserLoginName());
	
				// 在业务数据中解析主题
				String cf = flowConfig.getTopicCF();
				cf = evalTodoTopic(cf, jsonMap);
//				writeOpSuccessScript("流程实例【" + pid + "】保存成功");
				
				// 流程起草
			}
			if(tempActor.length() > 0)
				nextActor = tempActor.substring(0, tempActor.length() - 1);
			String nextTaksName = request.getParameter("nextNodes");
			String taskResult = "";
			String sfyrz = (String) jsonMap.get("SFYRZ");    
			if (StringUtil.isNotEmpty(sfyrz) && "true".equalsIgnoreCase(sfyrz)) {
				String hql = "from XqjhsqDetail as x where x.xqjhsq.id = ?";
				Object args[] = {docid};
				List<XqjhsqDetail> xqjhsqDetailList = xqjhsqDetailManager.find(hql, args);
				//添加操作日志
				xqjhsqLogManager.addCzLog(Long.parseLong(pid),jsonMap,actor,actorLoginName,xqjhsqDetailList,nextActor, nextTaksName,taskName,docid);
			}
			writeOpSuccessScript("保存成功");
			taskResult = "保存流程";
			//将业务数据存入临时变量中
			jbpmOperateManager.setProcessVariable(Long.parseLong(pid), jsonMap);
			
			// 添加流程日志tempActor
			workFlowLogManager.addFlowLog(Long.parseLong(pid), taskName, actor, actorLoginName, nextActor, nextTaksName, taskResult, "");
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}


	/**
	 * 待办主题计算
	 * @author 肖平松
	 * @version Oct 28, 2009
	 * @param cf
	 * @param map
	 * @return
	 */
	private String evalTodoTopic(String cf, HashMap map) {
		if(StringUtil.isEmpty(cf))	return "";
		String regex = "\\$\\{[^\\}\\{\"']*\\}"; // 匹配${}公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while (mt.find()) {
			String group = mt.group();
			String topic = group.substring(group.indexOf("{") + 1,
					group.lastIndexOf("}")).toUpperCase();
			Object value = map.get(topic);
			if (value == null) {
				value = "";
			}
			if(value instanceof Date){
				value = DateUtils.formatDate((Date)value, "yyyy-MM-dd HH:mm:ss");
			}
			cf = mt.replaceFirst(value + "");
			mt = p.matcher(cf);
		}
		return cf;
	}


	/**
	 * 跟踪流程,获取当前环节
	 * @author 肖平松
	 * @version Nov 7, 2009
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String wfTraceAction() throws UnsupportedEncodingException {
		String pid = request.getParameter("pid");
		if (StringUtil.isNotEmpty(pid)) {
			List <TaskInstance> tis = jbpmOperateManager
					.findTaskInstanceByProcessInstance(Long.parseLong(pid));
			if (tis != null) {
				String nodeName="";
				for(TaskInstance ti:tis){
					nodeName+=ti.getName()+",";
				}
				if(nodeName.length() > 0)
					nodeName = StringUtil.delEndString(nodeName, ",");
				request.setAttribute("currentNode", nodeName);
			}
		}
		return "showWFTrace";
	}
	
	
	
	/**
	 * 流程执行下一步动作
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String goToNextNodeAction() throws BusinessException {
		try{
			String token = request.getParameter("token");
			String selNodeIsBack = request.getParameter("selNodeIsBack");	//选择的跳转环节是否是由于条件不满足而退回的节点
			String pid 			= request.getParameter("pid");			//流程实例编号
			String docid 		= request.getParameter("docid"); 		// 业务数据编号
			String flowConfigId = request.getParameter("flowConfigId");	//流程配置编号
			String curUserName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
			String curLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
			String preNodeName = request.getParameter("preNodeName");
			FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
			
			// 表单类型
			String formType = flowConfig.getFormtype();
			HashMap recordMap = null;
			// 获得业务数据的MAP
			// EFORM
			if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
				String formSn = request.getParameter("formSn");
				EForm eform = eformManager.findUniqueBy("sn", formSn);
				DefTableInfo defTabInfo = eform.getDefTable();
				String tableName = defTabInfo.getTableCode();
				String schema = defTabInfo.getSchema();
				recordMap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
				// CFORM
			} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
				String recordJson = request.getParameter("recordJson");
				recordMap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
			}

			//如果流程编号为空
			if (StringUtil.isEmpty(pid)) {
				// 流程起草 新建
				pid = draftNewProcess(flowConfig, docid) + "";
			}
			//当前任务实例
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(Long.parseLong(pid), this.getCurUserLoginName(),token);
			//是否是属于不满足条件的回退
			if (!StringUtil.isEmpty(selNodeIsBack) && selNodeIsBack.equals("true")) {
				processGoBack(ti, preNodeName, curUserName, curLoginName, flowConfig, recordMap,docid);
			} else {
				//不是回退就继续下一步
				processGoNext(ti, flowConfig, recordMap, curUserName, curLoginName, docid);
			}
//			writeOpSuccessScript("流程实例编号为【" + pid + "】");
			myWriteOpSuccessScript("提交成功");
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}
	
	protected void myWriteOpSuccessScript(String msg) throws IOException{
		StringBuffer script=new StringBuffer();
		response.setCharacterEncoding("UTF-8");
		
		script.append("<HTML><HEAD>");
		script.append("<meta http-equiv=\"Content-Type\" Content=\"text/html; Charset=UTF-8\">");
		
		
		script.append("<script>\r\n");
		//script.append("alert('"+(msg==null?"":""+msg)+"');\r\n");
		script.append("window.close();\r\n");
		script.append("</script>\r\n");
		script.append("</HEAD></HTML>");
		
		this.getOut().print(script.toString());
	}
	
	/**
	 * 处理跳转到下一步的程序段
	 * @param ti
	 * @param flowConfig
	 * @param recordMap
	 * @param curUserName
	 * @param docid
	 */
	private void processGoNext(TaskInstance ti, FlowConfig flowConfig, HashMap recordMap, String curUserName, String curLoginName, String docid) {
		
		String selNodeName 	= request.getParameter("selNodeName");
		String selActorIds 	= request.getParameter("selActorIds");	//选择的处理人,如果可以为空表示没有选择处理人，那么所有可能的人都将参与处理
		String selNextNodeType = request.getParameter("selNextNodeType");	//下一个环节的类型 Task/Fock/Join
		
		String nextTaksName = request.getParameter("nextNodes");	//下一个任务
		String nextActorIds = request.getParameter("nextActors");	//下一个环节处理人
		String curTaskName = request.getParameter("curNodeName");	//当前处理任务名称
		//处理类型,值为"single"(单人处理)或者"multi"(多人处理)
		String processKind 	= request.getParameter("processKind");
		if (StringUtil.isNotEmpty(selActorIds)) {
			nextActorIds = selActorIds.replace('|', ',');
		}else if (!selNodeName.equals(WFConstants.WF_ENDSTATE)) {
			nextActorIds = findSelNextActorIds(nextTaksName,nextActorIds,selNodeName,selNextNodeType);
		}
		String nextActorNames = changeToChineseName(nextActorIds);	//下一个环节处理人中文名称
		
		//执行下一步
		List<String> nodeList = jbpmOperateManager.goNextTask(ti, this.getCurUserLoginName(),selNodeName);
		String toDoNextTaksName = "";
		for (String taskName : nodeList) {
			toDoNextTaksName += taskName+"~";
		}
		if(toDoNextTaksName.length() > 0)	toDoNextTaksName = StringUtil.delEndString(toDoNextTaksName, "~");
		String taskLog = "转到下一环节";
		if(!selNodeName.equals(WFConstants.WF_ENDSTATE)){
			//记录待办任务
			String cf = flowConfig.getTopicCF();
			cf = evalTodoTopic(cf, recordMap);
			taskToDoManager.saveToDo(flowConfig,ti.getProcessInstance(),curUserName, nextActorIds, toDoNextTaksName, cf,docid,ti.getToken().getId()+"",processKind,this.getCurUserLoginName());
			taskLog += "<" + toDoNextTaksName + ">";
		}else{
			taskToDoManager.disableTaskToDoByToken(ti.getProcessInstance().getId()+"", ti.getToken().getId()+"",this.getCurUserLoginName());
			taskLog += "<" + WFConstants.WF_ENDSTATE + ">";
		}
		String sfyrz = (String) recordMap.get("SFYRZ");         //是否有操作日志(针对需求计划申请每一步记录具体操作日志)
		if (StringUtil.isNotEmpty(sfyrz) && "true".equalsIgnoreCase(sfyrz)) {
			String hql = "from XqjhsqDetail as x where x.xqjhsq.id = ?";
			Object args[] = {docid};
			List<XqjhsqDetail> xqjhsqDetailList = xqjhsqDetailManager.find(hql, args);
			//添加操作日志
			xqjhsqLogManager.addCzLog(ti.getProcessInstance().getId(),recordMap,curUserName,curLoginName,xqjhsqDetailList,nextTaksName,curTaskName,nextActorNames,docid);
		}
		if(StringUtil.isNotEmpty(sfyrz) && "true".equalsIgnoreCase(sfyrz)){
			String hqls = "update Xqjhsq as t set t.scsjsz = '', t.xjsjsz = '', t.xgsjsz = '' where t.id = ?";
			Object arg[] = {docid};
			xqjhsqManager.createQuery(hqls, arg).executeUpdate();
			xqjhsqManager.flush();
		}
		//记录流程日志
		workFlowLogManager.addFlowLog(ti.getProcessInstance().getId(), curTaskName, curUserName,curLoginName,nextActorNames, nextTaksName, taskLog, "");
	}
	
	

	
	/**
	 * 处理回退的代码段
	 * @param ti			当前环节任务实例
	 * @param toNodeName	上一环节名称
	 * @param curUserName	当前登录用户名
	 * @param flowConfig	流程配置
	 * @param recordMap		
	 * @param docid			业务数据编号
	 */
	private void processGoBack(TaskInstance ti,String toNodeName,String curUserName, String curUserLoginName, FlowConfig flowConfig,HashMap recordMap,String docid){
		String backReason = request.getParameter("backReason");// 回退原因
		ProcessInstance pi = ti.getProcessInstance();
		pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
		String curNodeName = ti.getName();
		// 上一环节处理人
		String preActor =  request.getParameter("preActor");
//		System.out.println("preActor:"+preActor);
		// 回退目标节点不是空
		if (StringUtil.isNotEmpty(toNodeName)) {
			//获取要退回的一个或者多个任务实例(不包括多人处理)
			TaskInstance toTi = jbpmOperateManager.goBackToTask(pi.getId(), toNodeName, ti.getId(), curUserName, curUserLoginName, backReason, preActor);
			Token t = toTi.getToken();
			Map map = t.getChildren();
			//如果上一级环节实例有子TOKEN,则在待办中禁用所有子TOKEN的环节
			for(Object key : map.keySet()){
				Token tempToken = (Token) map.get(key);
				taskToDoManager.disableTaskToDoByToken(pi.getId()+"", Long.toString(tempToken.getId()),this.getCurUserLoginName());
			}
			taskToDoManager.disableTaskToDoByNodeName(pi.getId()+"", curNodeName,this.getCurUserLoginName());
			
			//获取上一级环节(回退目标环节)的处理人
			if("设备缺陷单".equals(request.getParameter("flowName")))
			    preActor = jbpmOperateManager.getActorIdsByTask(toTi.getTask(), toTi,this.getCurUserLoginName());
//			System.out.println("preActor:"+preActor);
//			String curNodePerson = actorIds;
			
			String postPerson = this.getCurUserName();
			String nodename = toNodeName;
			//待办主题
			String cf = flowConfig.getTopicCF();
			cf = evalTodoTopic(cf, recordMap);
			String curToken = Long.toString(ti.getToken().getId());
			//记录待办任务
			taskToDoManager.saveToDo(flowConfig,pi,postPerson,preActor, nodename, cf,docid,curToken,null,this.getCurUserLoginName());
		}
	}

	/**
	 * 
	 * 描述 : 处理撤销
	 * 作者 : wangyun
	 * 时间 : Jul 14, 2010
	 * 参数 : 
	 * 		ti ： 任务实例
	 * 		toNodeName ： 上一环节名
	 * 		curUserName ： 当前操作人名
	 * 		flowConfig ： 流程配置
	 * 		recordMap ： 业务数据
	 * 		docid ： 业务数据ID
	 * 返回值 : 无
	 * 异常 : IOException
	 * @param taskInstances 
	 * @param curUserName 
	 * @param flowConfig 
	 * @param docid 
	 * @param recordMap 
	 */
	private void processCancel(ProcessInstance pi, Node preNode, Collection<TaskInstance> taskInstances, String curUserName, String curUserLoginName, String docid, FlowConfig flowConfig, HashMap recordMap) throws IOException {
		// 撤销任务，并且得到撤销到的任务实例
		TaskInstance toTi = jbpmOperateManager.cancelTask(pi, preNode, curUserName, curUserLoginName);
		// 遍历所有未完成的任务实例，将其分配的代办任务禁用掉
		for (TaskInstance ti : taskInstances) {
			taskToDoManager.disableTaskToDoByNodeName(pi.getId()+"", ti.getTask().getName(),this.getCurUserLoginName());
		}

		// 撤销发送人
		String postPerson = this.getCurUserName();
		// 撤销目标人员
		String curNodePerson = this.getCurUserLoginName();
		String nodename = toTi.getTask().getName();
		//待办主题
		String cf = flowConfig.getTopicCF();
		cf = evalTodoTopic(cf, recordMap);
		//记录待办任务
		taskToDoManager.saveToDo(flowConfig,pi,postPerson,curNodePerson, nodename, cf,docid,null,null,this.getCurUserLoginName());
	}

	/**
	 * 流程回退
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String goBackNodeAction() throws IOException {
		String token = request.getParameter("token");	//当前TOKEN编号
		String docid = request.getParameter("docid");//业务数据编号
		String pid = request.getParameter("pid");//流程实例编号
		String curUserName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();//当前处理人
		String curLoginName = this.getCurUserLoginName();
		String preNodeName = request.getParameter("preNodeName");//上一环节名称(如环节1~环节2)
		String flowConfigId = request.getParameter("flowConfigId");//流程配置ID
		FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
		try {
			if(StringUtil.isEmpty(preNodeName)){
				writeOpSuccessScript("无法退回");
				return NONE;
			}
			
			// 表单类型
			String formType = flowConfig.getFormtype();
			HashMap recordMap = null;
			// 获得业务数据的MAP
			// EFORM
			if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
				String formSn = request.getParameter("formSn");
				EForm eform = eformManager.findUniqueBy("sn", formSn);
				DefTableInfo defTabInfo = eform.getDefTable();
				String tableName = defTabInfo.getTableCode();
				String schema = defTabInfo.getSchema();
				recordMap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
				// CFORM
			} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
				String recordJson = request.getParameter("recordJson");
				recordMap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
			}
			
			//获取当前任务实例
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(Long.parseLong(pid), this.getCurUserLoginName(),token);
			// 执行回退操作
			processGoBack(ti, preNodeName, curUserName, curLoginName, flowConfig, recordMap, docid);
//			writeOpSuccessScript("回退成功:流程实例编号【" + pid + "】");
			writeOpSuccessScript("回退成功");
		} catch (Exception ex) {
			writeOpSuccessScript("退回失败");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 环节撤销
	 * 作者 : wangyun
	 * 时间 : Jul 14, 2010
	 * 参数 : 
	 * 异常 ： IOException 
	 */
	public String cancelNodeAction() throws IOException {
		String pid = request.getParameter("pid");//流程实例编号
		String curUserName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();//当前处理人
		String curUserLoginName = this.getCurUserLoginName();
		String docid = request.getParameter("docid");//业务数据编号
		String flowConfigId = request.getParameter("flowConfigId");//流程配置ID
		FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
		try {
			ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
			pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
			boolean isCanCancel = true;
			// 找到所有未完成任务实例
			Collection<TaskInstance> taskInstances = jbpmOperateManager.getUnfinishedTaskInstance(pi, null);
			
			Node preNodeTmp = null;
			for (TaskInstance taskInstance : taskInstances) {
				// 上一环节处理人
				String preActor =  StringUtil.dealNull(taskInstance.getContextInstance().getVariable(taskInstance.getTask().getTaskNode().getId()+"_"+taskInstance.getProcessInstance().getId() + "_preActor"));
				// 上一环节节点名称
				String preNodeName = (String) taskInstance.getContextInstance().getVariable(taskInstance.getTask().getTaskNode().getId()+"_"+pi.getId()+"_preNodeName");
				// 当前token
				Token token = taskInstance.getToken();
				// 上一环节节点
				Node preNode = jbpmOperateManager.findTaskNodeByNodeName(preNodeName, pi.getProcessDefinition().getId());
				preNodeTmp = preNode;

				// 如果上一节点类型为汇集，不允许撤销
				if (NodeType.Join.equals(preNode.getNodeType())) {
					writeOpSuccessScript("会签已结束,无法撤销!");
					isCanCancel = false;
					break;
				}
				// 如果token有父token，则流程进行在会签中。如果上一环节类型不为会签，则表示流程已经走到分支中，分支进行中则不允许撤销
				if (token.hasParent() && !NodeType.Fork.equals(preNode.getNodeType())) {
					writeOpSuccessScript("流程已在会签分支中,无法撤销!");
					isCanCancel = false;
					break;
				}
				// 如果会签中有分支已经执行完毕，则无法撤销
				if (token.hasParent() && token.getParent().getActiveChildren().size() != token.getParent().getChildren().size()) {
					writeOpSuccessScript("会签中已有分支结束,无法撤销!");
					isCanCancel = false;
					break;
				}
				// 如果上一环节处理人不为当前登录人，无法撤销
				if (!this.getCurUserLoginName().equals(preActor)) {
					writeOpSuccessScript("你不是上一环节处理人,无法撤销!");
					isCanCancel = false;
					break;
				}
				// 如果任务已被签收，则无法撤销
				TaskToDo taskTodo = taskToDoManager.findByPidAndToken(String.valueOf(pi.getId()), String.valueOf(token.getId()));
				if (taskTodo.getCurSignIn() != null) {
					writeOpSuccessScript("环节已被签收,无法撤销!");
					isCanCancel = false;
					break;
				}
			}

			// 如果可以撤销，则进行撤销操作
			if (isCanCancel) {
				// 表单类型
				String formType = flowConfig.getFormtype();
				HashMap recordMap = null;
				// 获得业务数据的MAP
				// EFORM
				if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
					String formSn = request.getParameter("formSn");
					EForm eform = eformManager.findUniqueBy("sn", formSn);
					DefTableInfo defTabInfo = eform.getDefTable();
					String tableName = defTabInfo.getTableCode();
					String schema = defTabInfo.getSchema();
					recordMap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
					// CFORM
				} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
					String recordJson = request.getParameter("recordJson");
					recordMap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
				}
				processCancel(pi, preNodeTmp, taskInstances, curUserName, curUserLoginName, docid, flowConfig, recordMap);
//				writeOpSuccessScript("撤销成功:流程实例编号【" + pid + "】");
				writeOpSuccessScript("撤销成功");
			}
		} catch (Exception ex) {
			writeOpSuccessScript("撤销失败");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 根据当前选择的跳转环节，在下一步
	 * @param nextTaksName
	 * @param nextActorIds
	 * @param selNodeName
	 * @param selNextNodeType 
	 * @return
	 */
	private String findSelNextActorIds(String nextTaksName,
			String nextActorIds, String selNodeName, String selNextNodeType) {
		String result = null;
		if(selNextNodeType.equals(NodeType.Fork.toString()) || selNextNodeType.equals(NodeType.Join.toString())){
			result = nextActorIds;
		}else{
			String nextTaskNameArray[] = nextTaksName.split("~");
			int idx = -1;
			for (int i = 0; i < nextTaskNameArray.length; i++) {
				if(nextTaskNameArray[i].equals(selNodeName)){
					idx = i;
					break;
				}
			}
			String nextActorIdArray[] = nextActorIds.split("~");
			result = nextActorIdArray[idx];
		}
		return result;
	}


	/**
	 * 节点配置对象的封装，将默认节点和节点类型加入到节点配置对象中
	 * @description
	 * @author tantyou
	 * @date 2009-11-13 上午09:16:11
	 */
	public class NodeWapper extends NodeConfig{
		private boolean defaultNode;	//是否是默认路由节点
		private String nodeType;		//节点类型 Task/Fock/Join
		private boolean isBackNode;			//表明是否是退回节点

		
		public boolean getIsBackNode() {
			return isBackNode;
		}


		public void setIsBackNode(boolean isBackNode) {
			this.isBackNode = isBackNode;
		}


		public String getNodeType() {
			return nodeType;
		}


		public void setNodeType(String nodeType) {
			this.nodeType = nodeType;
		}


		public boolean isDefaultNode() {
			return defaultNode;
		}


		public void setDefaultNode(boolean defaultNode) {
			this.defaultNode = defaultNode;
		}

	}

	
	/**
	 * 获取下一环节的名称，处理人和环节信息
	 * @author 肖平松
	 * @version Oct 23, 2009
	 * @return
	 * @throws Exception
	 */
	public String getNodePermissionAction() throws BusinessException{
		try{
			String token = request.getParameter("token");
			String pid = request.getParameter("pid");
			String flowConfigId = request.getParameter("flowConfigId");
			String preNodeName = request.getParameter("preNodeName");	//上一环节名称，用于如果无满足条件的跳转则退回
			String docid = request.getParameter("docid");
			FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
			// 表单类型
			String formType = flowConfig.getFormtype();
			HashMap recordMap = null;
			// 获得业务数据的MAP
			// EFORM
			if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
				String formSn = request.getParameter("formSn");
				EForm eform = eformManager.findUniqueBy("sn", formSn);
				DefTableInfo defTabInfo = eform.getDefTable();
				String tableName = defTabInfo.getTableCode();
				String schema = defTabInfo.getSchema();
				recordMap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
				for (Object key : recordMap.keySet()) {
					Object value = recordMap.get(key);
					if (value != null && value.toString().length() > 3500) {
						recordMap.put(key, "");
					}
				}
				// CFORM
			} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
				String recordJson = request.getParameter("recordJson");
				recordMap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
			}

			jbpmOperateManager.setProcessVariable(Long.parseLong(pid), recordMap);
			
			//当前任务实例
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(Long.parseLong(pid), this.getCurUserLoginName(),token);
			
			//得到下一步节点集合,包含默认的跳转
			List<Transition> tranList = jbpmOperateManager.findAvalibleTransitions(flowConfigId, ti);
			
			//得到下一步节点处理人集合
			List<Node> nodeList = new ArrayList<Node>();
			//节点配置集合
			List<NodeWapper> ncList = new ArrayList<NodeWapper>();
			
			//如果既没有满足条件的路由也没有默认的路由的情况下,取得当前环节的上一个环节进行退回操作
			if(tranList.size()==0){
				Node preNode = jbpmOperateManager.findTaskNodeByNodeName(preNodeName,ti.getProcessInstance().getProcessDefinition().getId());
				if(preNode!=null){
					nodeList.add(preNode);
					NodeConfig nc = nodeConfigManager.findNodeConfigByName(flowConfigId, preNode.getName());
					NodeWapper nw = new NodeWapper();
					if(nc!=null){
						BeanUtils.copyProperties(nw, nc);
					}else{
						nw.setName(preNode.getName());
					}
					nw.setDefaultNode(false);
					nw.setIsBackNode(true);
					nw.setNodeType(preNode.getNodeType().toString());
					ncList.add(nw);
				}
				
			}else{
				for (Transition tr : tranList) {
					Node node = tr.getTo();
					nodeList.add(node);
					NodeConfig nc = nodeConfigManager.findNodeConfigByName(flowConfigId, node.getName());
					NodeWapper nw = new NodeWapper();
					if(nc!=null){
//						nodeList.add(node);
						BeanUtils.copyProperties(nw, nc);
					}else{
						nw.setName(node.getName().equals("汇集")?"会签结束":node.getName());
					}
					nw.setDefaultNode(jbpmOperateManager.getDefaultRouter(flowConfigId, tr));
					nw.setNodeType(node.getNodeType().toString());
					nw.setIsBackNode(false);
					ncList.add(nw);
				}
			}
			
			ti.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
			//处理人ID列表["0001,0002","0003"]
			List actorIdList = jbpmOperateManager.getActorListByNodeList(nodeList, ti);
			//处理人中文名称["张三,李四","王五"]
			List actorNameList = actorIdListToNameList(actorIdList);
			//将节点集合，处理人集合数据序列化后输出
			String ncListJson = JSONUtil.listToJson(ncList, new String[]{"name","nodeType","defaultNode","nodePermission","expression","processMode","processKind","isOneProcessActor","isChooseActor","isBackNode"});
			String actorIdListJson = JSONUtil.listToJson(actorIdList);
			String actorNameListJson = JSONUtil.listToJson(actorNameList);
			
			String returnJson = "{success:true,ncList:"+ncListJson+",actorIdList:"+actorIdListJson+",actorNameList:"+actorNameListJson+"}";
			this.outputJson(returnJson);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}
	
	
	/**
	 * 将处理人ID列表转换为中文用户名的列表
	 * @param actorIdList
	 * @return
	 */
	private List<String> actorIdListToNameList(List<String> actorIdList) {
		List nameList = new ArrayList();
		for (String ids : actorIdList) {
			String[] temp = ids.split(",");
			String username = "";
			//将处理人登录名转换为用户名
			for(int j = 0;j < temp.length;j++){
				String suffix = ((j+1==temp.length)?"":",");
				if(StringUtil.isNotEmpty(temp[j])){
					if(temp[j].equals(Constants.ADMINISTRATOR_ACCOUNT)){
						username += Constants.ADMINISTRATOR_NAME + suffix;
					}
					else{
						username += personManager.findUniqueBy("userLoginName", temp[j]).getUserName() + suffix;
					}
				}
			}
			nameList.add(username);
		}
		return nameList;
	}

	/**
	 * 获取可以跳转的所有环节信息
	 * @author 肖平松
	 * @version Oct 15, 2009
	 * @return
	 * @throws Exception 
	 */
	public String getAllNodePermissionAction() throws Exception{
		//当前环节的TOKEN
		String token = request.getParameter("token");
		//流程配置编号
		String flowConfigId = request.getParameter("flowConfigId");
		//流程实例编号
		String pid = request.getParameter("pid");
		//流程实例
		ProcessInstance curPi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
		//当前用户登录名
		String userLoginName = this.getCurUserLoginName();
		
		String[] fields = new String[]{"processMode","processKind","isOneProcessActor","isChooseActor"};
		String returnJson = "";
		//所有可跳转的主环节
		List<NodeConfig> list = jbpmOperateManager.findAllTaskNode(flowConfigId,Long.parseLong(pid));
		//所有可跳转的会签环节
//		List<TaskInstance> tiList = new ArrayList<TaskInstance>();
		for(int i = list.size()-1;i >= 0;i--){
			NodeConfig tempNc = list.get(i);
			TaskInstance tempTi = jbpmOperateManager.getTaskInstanceByTaskName(tempNc.getName(), curPi);
			if(tempTi.getToken().getParent() != null){
				list.remove(tempNc);
//				tiList.add(tempTi);
			}
		}
//		HashMap tiMap = new HashMap();
//		for(int i = 0;i < tiList.size();i++){
//			TaskInstance tempTi = tiList.get(i);
//			Node pNode = tempTi.getToken().getParent().getNode();
//			if(pNode.getNodeType().equals(NodeType.Fork)){
//				String joinName = pNode.getName();
//				String actors = jbpmOperateManager.getActorIdsByTask(tempTi.getTask(), tempTi, this.getCurUserLoginName());
//				if(tiMap.containsKey(joinName)){
//					String tempActors = tiMap.get(joinName).toString() + "," + actors;
//					tempActors = jbpmOperateManager.delSameElementAsList(tempActors);
//					tiMap.remove(joinName);
//					tiMap.put(joinName, tempActors);
//				}
//				else{
//					tiMap.put(joinName, actors);
//				}
//			}
//		}
//		//保存所有的会签环节
//		HashMap joinMap = new HashMap();
//		//遍历所有会签环节
//		for(Object key:tiMap.keySet()){
//			String joinName = key.toString();
//			String actorIds = tiMap.get(key).toString();
//			String usernames = changeToChineseName(actorIds);
//			
//			joinMap.put("taskType", "Join");
//			joinMap.put("default", false);
//			joinMap.put("taskName", joinName);
//			joinMap.put("username", usernames);
//			joinMap.put("actors", actorIds);
//			
//			returnJson += JSONUtil.mapToJson(joinMap) + ",";
//		}
		
		//当前任务实例
		TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(Long.parseLong(pid),userLoginName,token);
		//保存所有的会签环节
		HashMap taskMap = new HashMap();
		//遍历所有环节
		for(int i = 0;i < list.size();i++){
			NodeConfig nc = list.get(i);
			Task task = jbpmOperateManager.getTaskByName(nc.getName());
			ti.getProcessInstance().getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, userLoginName);
			String actors = jbpmOperateManager.getActorIdsByTask(task, ti);
			List<NodePermission> npList = (List<NodePermission>) nodePermissionManager.getNodePermission(flowConfigId,nc.getName());
			String json = JSONUtil.listToJson(npList, fields);
			json = StringUtil.delStartString(json, "[");
			json = StringUtil.delEndString(json, "]");
			HashMap jsonmap = (HashMap) JSONUtil.parseObject(json);
			String[] temp = actors.split(",");
			String username = "";
			for(int j = 0;j < temp.length;j++){
				if(temp[j].equals(Constants.ADMINISTRATOR_ACCOUNT)){
					username += Constants.ADMINISTRATOR_NAME;
				}
				else{
					username += personManager.findUniqueBy("userLoginName", temp[j]).getUserName() + ",";
				}
			}
			if(username.length() > 0){
				username = StringUtil.delEndString(username, ",");
			}
			
			taskMap.put("default", false);
			taskMap.put("taskType", "Task");
			taskMap.put("taskName", nc.getName());
			taskMap.put("username", username);
			taskMap.put("actors", actors);
			taskMap.put("processMode", jsonmap.get("processMode"));
			taskMap.put("processKind", jsonmap.get("processKind"));
			taskMap.put("isOneProcessActor", jsonmap.get("isOneProcessActor"));
			taskMap.put("isChooseActor", jsonmap.get("isChooseActor"));

			returnJson += JSONUtil.mapToJson(taskMap) + ",";
		}
		returnJson = StringUtil.delEndString(returnJson, ",");
		returnJson = "{success:true,data:[" + returnJson + "]}";
		//[{default:true/false,taskType:Fork,taskName:Fork,actors:角色A},{default:true/false,taskType:Task,taskName:环节A,actors:xiao,username:xiao,processMode:..,processKind:...,isOneProcessActor:...,isChooseActor:...}]
		this.outputJson(returnJson);
		return NONE;
	}
	
	/**
	 * 起草一个新的流程实例
	 * 通过Request输入参数
	 * 		docid						业务对象编号
	 * 		recordJson				业务对象数据
	 * 		curNodeName			当前处理环节
	 * 		curActors					当前处理人
	 * 		flowConfigId				流程配置编号
	 * @return	表单数据
	 * {"nextActors":"0845,0490,0963,0223,0390~0845","token":45974,"creatdt":"2009-10-22 17:40:46","pid":45973,"ver":1,"tid":45975,"flowName":"项目审批","nextNodes":"部门经理审批~总经理审批","creator":"root"}
	 * @throws Exception
	 */
	public String draftNewWorkFlowInstance() throws Exception{
		// 业务数据编号
		String docid = request.getParameter("docid"); 
		String taskName = request.getParameter("curNodeName"); 
		String actor = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
		String actorLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		String taskActor = request.getParameter("curActors");
		//流程配置编号
		String flowConfigId = request.getParameter("flowConfigId");
		FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
		ProcessInstance pi = draftNewProcess(flowConfig, docid);
		
		HashMap map = new HashMap();
		map.put("pid", pi.getId());

		// 表单类型
		String formType = flowConfig.getFormtype();
		HashMap recordmap = null;
		// 获得业务数据的MAP
		// EFORM
		if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
			String formSn = request.getParameter("formSn");
			EForm eform = eformManager.findUniqueBy("sn", formSn);
			DefTableInfo defTabInfo = eform.getDefTable();
			String tableName = defTabInfo.getTableCode();
			String schema = defTabInfo.getSchema();
			recordmap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
			for (Object key : recordmap.keySet()) {
				Object value = recordmap.get(key);
				if (value != null && value.toString().length() > 4000) {
					recordmap.put(key, "");
				}
			}
			pi.getContextInstance().setVariables(recordmap);
			// CFORM
		} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
			String recordJson = request.getParameter("recordJson");
			recordmap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
			pi.getContextInstance().setVariables(recordmap);
		}

		if (pi != null) {
			pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME,this.getCurUserLoginName());
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pi.getId(), this.getCurUserLoginName());
			if (ti != null) {
				// 下一结点处理人
				String nextActors = jbpmOperateManager.getNextTaskActorByNode(ti);
				// 下一结点名称
				String nextNodes = jbpmOperateManager.getNextTaskNameByNode(ti);
				map.put("nextNodes", nextNodes);
				map.put("nextActors", nextActors);

				map.put("tid", ti.getId());
				map.put("token", ti.getToken().getId());
			}

			// 流程起草人
			String creator = StringUtil.dealNull(pi.getContextInstance()
					.getVariable("creator"));
			// 流程起草时间
			Date creatdt = (Date) pi.getContextInstance().getVariable("creatdt");
			// 流程名称
			String flowName = StringUtil.dealNull(pi.getContextInstance()
					.getVariable("flowName"));
			map.put("creator", creator);
			map.put("creatdt", DateUtils.formatDate(creatdt));
			map.put("flowName", flowName);
			map.put("ver", pi.getProcessDefinition().getVersion());
		}

		//计算待办主题并保存待办对象
		String cf = flowConfig.getTopicCF();
		cf = evalTodoTopic(cf, recordmap);
		
		// 创建流程申请代办
		taskToDoManager.saveToDo(flowConfig,pi,actor, taskActor, taskName, cf,docid,pi.getRootToken().getId()+"", null,this.getCurUserLoginName());
		
		String nextActorIds = (String)map.get("nextActors");	//下一个环节处理人
		String nextActorNames = changeToChineseName(nextActorIds);	//下一个环节处理人中文名称
		//记录流程日志
		workFlowLogManager.addFlowLog(pi.getId(), taskName, actor, actorLoginName, nextActorNames, (String)map.get("nextNodes"), "起草流程", "");
		
		String returnJson = JSONUtil.mapToJson(map);
		returnJson = "{success:true,data:" + returnJson + "}";
		this.outputJson(returnJson);
		return NONE;
	}

	/**
	 * 将登陆名称转换为用户中文名
	 * @param nextActors 要转换的处理人字符串(例如:0001,0002~0003,0004)
	 * @return
	 */
	private String changeToChineseName(String userLoginNames){
		String nextActor = userLoginNames;
		if (StringUtil.isNotEmpty(userLoginNames)) {
			String tempActor = "";
			String[] nextTasksActorTemp = userLoginNames.split("~");
			for(int j = 0;j < nextTasksActorTemp.length;j++){
				String[] nextTasksActorArray = nextTasksActorTemp[j].split(",");
				for (int i = 0; i < nextTasksActorArray.length; i++) {
					if(StringUtil.isNotEmpty(nextTasksActorArray[i])){
						if (nextTasksActorArray[i]
						                        .equals(Constants.ADMINISTRATOR_ACCOUNT)) {
							tempActor += Constants.ADMINISTRATOR_NAME+",";
						} else {
							tempActor += personManager.findPersonByLoginName(
									nextTasksActorArray[i]).getUserName()
									+ ",";
						}
					}
				}
			}
			nextActor = tempActor.substring(0, tempActor.length() - 1);
		}
		return nextActor;
	}

	
	/**
	 * 跳转到指定的环节
	 * @author 肖平松
	 * @version Nov 7, 2009
	 * @return
	 * @throws Exception
	 */
	public String goToSelectedNodeAction() throws Exception{
//		String token = request.getParameter("token");
		String curNodeName = request.getParameter("curNodeName");
		String actor = this.getCurUserName();
		String actorLoginName = this.getCurUserLoginName();
		// 指定的环节
		String node = request.getParameter("node");
		node = StringUtil.isoToUTF8(node);
		// 指定环节的处理人
		String actors = request.getParameter("actors");
		actors = StringUtil.isoToUTF8(actors);
		String strpid = request.getParameter("pid");
		Long pid = Long.parseLong(strpid);
		try{
		ProcessInstance pi = jbpmOperateManager.getJbpmTemplate()
				.findProcessInstance(pid);
		pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
//		TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pid, this.getCurUserLoginName(),token);
		String nodeactors = "";
		//如果上一环节不为空
		if (StringUtil.isNotEmpty(node)) {
//			String nodeactors = jbpmOperateManager.gotoSelectedNode(pi, node,actors,this.getCurUserLoginName());
			// jbpm操作跳转到指定的环节，获得跳转指向的任务实例集合
			List<TaskInstance> jumpTi = jbpmOperateManager.gotoSelectedNode(pi, node,actors,this.getCurUserLoginName());
//			//获取要退回的一个或者多个任务实例(不包括多人处理)
//			List<TaskInstance> preTiList = jbpmOperateManager.goBackToTask(pi, preNodeName, Long
//					.parseLong(tid), actor);
			//遍历跳转到的环节实例
			for(int i = 0;i < jumpTi.size();i++){
				TaskInstance taskInstance = jumpTi.get(i);
//				Token t = taskInstance.getToken();
//				Map map = t.getChildren();
//				//如果上一级环节实例有子TOKEN,则在待办中禁用所有子TOKEN的环节
//				for(Object key : map.keySet()){
//					Token tempToken = (Token) map.get(key);
//					taskToDoManager.disableTaskToDo(pid, Long.toString(tempToken.getId()));
//				}
				//获取上一级环节(回退目标环节)的处理人
				String actorIds = jbpmOperateManager.getActorIdsByTask(taskInstance.getTask(), taskInstance,this.getCurUserLoginName());
				nodeactors += actorIds + ",";
//				List<TaskToDo> list = taskToDoManager.findBy("curTaskName", preTiList.get(i).getName(), "postTime", false);
//				TaskToDo todo = list.get(0);
				FlowConfig flowConfig = flowConfigManager.findFlowConfigByPDID(pi.getProcessDefinition().getId());
				String postPerson = this.getCurUserName();
				String curNodePerson = actorIds;
				String nodename = taskInstance.getName();
				// 业务数据编号
				String docid = request.getParameter("docid"); 
				
				// 表单类型
				String formType = flowConfig.getFormtype();
				HashMap recordMap = null;
				// 获得业务数据的MAP
				// EFORM
				if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
					String formSn = request.getParameter("formSn");
					EForm eform = eformManager.findUniqueBy("sn", formSn);
					DefTableInfo defTabInfo = eform.getDefTable();
					String tableName = defTabInfo.getTableCode();
					String schema = defTabInfo.getSchema();
					recordMap = (HashMap) jdbcManager.getRecById(docid, schema, tableName);
					// CFORM
				} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
					String recordJson = request.getParameter("recordJson");
					recordMap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
				}

				//待办主题
				String cf = flowConfig.getTopicCF();
				cf = evalTodoTopic(cf, recordMap);
				
				String curToken = Long.toString(taskInstance.getToken().getId());

				//记录待办任务
				taskToDoManager.saveToDo(flowConfig,pi,postPerson,curNodePerson, nodename, cf,docid,curToken,null,this.getCurUserLoginName());
			}
			writeOpSuccessScript("转到环节<" + node + ">成功");

		} else {
			writeOpSuccessScript("转到环节<" + node + ">失败");
		}
		
		nodeactors = StringUtil.delEndString(nodeactors, ",");
		String taskResult = "转到环节<" + node + ">";
		workFlowLogManager.addFlowLog(pid, curNodeName, actor, actorLoginName, nodeactors, node, taskResult, "");

//		this.writeOpSuccessScript(taskResult);
		}
		catch(Exception e){
			throw new BusinessException();
		}
		return NONE;
	}

	/**
	 * 根据流程配置编号，起草一个新的流程实例
	 * 
	 * @param flowConfigId
	 * @throws Exception
	 */
	public ProcessInstance draftNewProcess(FlowConfig flowConfig, String docid) throws Exception {
		if (flowConfig.getPd_id() == null) {
			throw new BusinessException("流程还没发布，请先发布流程！");
		}

		String userName = this.sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		long pid = jbpmOperateManager.createProcessInstance(flowConfig.getNm(), userName);
		ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(pid);
		pi.getContextInstance().setVariable(WFConstants.WF_VAR_DOCID, docid);
		if (StringUtil.isNotEmpty(flowConfig.getFormId())) {
			request.setAttribute("formId", flowConfig.getFormId());
			request.setAttribute("type", flowConfig.getFormtype());
		}
		request.setAttribute("processPageUrl", flowConfig.getProcess_url());

		return pi;
	}

	/**
	 * 
	 * 描述 : 签收
	 * 作者 : wangyun
	 * 时间 : Jul 15, 2010
	 * 异常 : Exception
	 */
	public String signInAction() throws Exception {
		try {
			String pid = request.getParameter("pid");
			String token = request.getParameter("token");
			String formSn = request.getParameter("formSn");
			TaskToDo taskTodo = taskToDoManager.findByPidAndToken(pid, token);
			if (taskTodo != null) {
				// 如果已经被签出
				if (taskTodo.getCurSignIn() != null) {
					if(("TB_WZ_XQJHSQ").equals(formSn)){
						if(!this.getCurUserLoginName().equals(taskTodo.getCurSignIn().getUserLoginName())){
							this.outputJson("{success:false}");
							return NONE;
						}else{
							this.outputJson("{success:true}");
							return NONE;
						}
					}
					// 并且签收人不是当前登录人
					else if(this.getCurUserLoginName().equals(taskTodo.getCurSignIn().getUserLoginName())) {
						this.outputJson("{success:false}");
						return NONE;
					}
				}
				Person person = personManager.findUniqueBy("userLoginName", this.getCurUserLoginName());
				taskTodo.setCurSignIn(person);
				taskToDoManager.save(taskTodo);
				this.outputJson("{success:true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
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

	public TaskToDoManager getTaskToDoManager() {
		return taskToDoManager;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public NodeOperationManager getNodeOperationManager() {
		return nodeOperationManager;
	}

	public void setNodeOperationManager(NodeOperationManager nodeOperationManager) {
		this.nodeOperationManager = nodeOperationManager;
	}
	
	/**
	 * 获取当前用户登录名
	 */
	private String getCurUserLoginName(){
		return sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
	}

	/**
	 * 获取当前用户名称
	 */
	private String getCurUserName(){
		return sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
	}

	public CFormManager getCformManager() {
		return cformManager;
	}

	public void setCformManager(CFormManager cformManager) {
		this.cformManager = cformManager;
	}
	

}
