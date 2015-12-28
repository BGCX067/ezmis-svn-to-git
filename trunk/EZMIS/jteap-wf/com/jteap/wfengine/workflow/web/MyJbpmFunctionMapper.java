package com.jteap.wfengine.workflow.web;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;


import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;

import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ProcessInstance;

import org.jbpm.jpdl.el.FunctionMapper;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.util.EFormExp;
import com.jteap.form.eform.util.EFormExpVelocityCF;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.util.WFConstants;

@SuppressWarnings({ "unused", "serial","deprecation","unchecked" })
public class MyJbpmFunctionMapper implements FunctionMapper {


	private static ExecutionContext executionContext;
	
	private GroupManager groupManager;


	

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public Method resolveFunction(String prefix, String localName) {
		System.out.println(prefix+":"+localName);
		try {
			Method me=null;
			if(localName.equals("xxx")){
				me=MyJbpmFunctionMapper.class.getMethod("xxx", new Class[]{Object.class});
			}
			if(localName.equals("actors")){
				me=MyJbpmFunctionMapper.class.getMethod("actors", new Class[]{String.class});
			} 
			return me;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int xxx(Object aaa){
		System.out.println(aaa+"=======ddddddddddddddddddd");
		return 1000;
	}

	/**
	 * 根据组织内码，角色内码，返回任务执行者集合
	 *@author wangyun 
	 *@date   2010-12-23
	 *@param groupSns	'JXB','销售部','客户部'
	 *@param roleSns		'管理员','部门经理'
	 *@return String    "张三,李四"
	 */
	public static String actors(String groupSns, String roleSns) {
		if ("null".equals(groupSns)) groupSns = "";
		if ("null".equals(roleSns)) roleSns = "";
		
		Collection<Person> persons = new ArrayList();
		StringBuffer sb = new StringBuffer();
		PersonManager personManager = null;
			
		if(StringUtil.isNotEmpty(groupSns) || StringUtil.isNotEmpty(roleSns)){
			personManager = (PersonManager) SpringContextUtil.getBean("personManager");
		}
		//获取组内用户对象
		if(StringUtil.isNotEmpty(groupSns)){
			persons.addAll(personManager.findPersonByGroupSns(groupSns));
		}
		//获取角色内用户对象
		if(StringUtil.isNotEmpty(roleSns)){
			Collection<Person> personsFRole = personManager.findPersonByRoleSns(roleSns);
			for(Person person: personsFRole){
				//如果不在集合中
				if(!persons.contains(person)){
					persons.add(person);
				}
			}
		}
		//取登录名并格式化
		for(Person person : persons){
			sb.append(person.getUserLoginName() + ",");
		}
		String actorStr = sb.toString();
		actorStr = StringUtil.delEndString(sb.toString(),",");
		return actorStr;
	}

	/**
	 * 根据组名，角色名，和用户登录名，返回任务执行者集合
	 *@author  MrBao 
	 *@date 	  2009-7-14
	 *@param groupIds	'生产部','销售部','客户部'
	 *@param roleIds		'管理员','部门经理'
	 *@param userLoginNames   
	 *@return
	 *@return String    "张三,李四"
	 *@remark			String groupIds,String roleIds,String userLoginNames
	 */
	public static String actors(String groupIds,String roleIds,String userLoginNames){
		if(groupIds.equals("null"))	groupIds = "";
		if(roleIds.equals("null"))	roleIds = "";
		if(userLoginNames.equals("null"))	userLoginNames = "";
		
		Collection<Person> persons = new ArrayList();
		StringBuffer sb = new StringBuffer();
		PersonManager personManager = null;
			
		if(StringUtil.isNotEmpty(groupIds) || StringUtil.isNotEmpty(roleIds)){
			personManager = (PersonManager) SpringContextUtil.getBean("personManager");
		}
		//获取组内用户对象
		if(StringUtil.isNotEmpty(groupIds)){
			persons.addAll(personManager.findPersonByGroupIds(groupIds));
		}
		//获取角色内用户对象
		if(StringUtil.isNotEmpty(roleIds)){
			Collection<Person> personsFRole = personManager.findPersonByRoleIds(roleIds);
			for(Person person: personsFRole){
				//如果不在集合中
				if(!persons.contains(person)){
					persons.add(person);
				}
			}
		}
		//取登录名并格式化
		for(Person person : persons){
			sb.append(person.getUserLoginName() + ",");
		}
		String actorStr = sb.toString();
		//获取直接传的登录名
		if(StringUtil.isNotEmpty(userLoginNames)){
			String [] arrStr = userLoginNames.split(",");
			for(String actor : arrStr){
				if(actorStr.indexOf(actor) < 0){
					sb.append(actor + ",");
				}
			}
		}
		actorStr = StringUtil.delEndString(sb.toString(),",");
		return actorStr;
	}

	public static String  actors(String arrJson) throws Exception{
		JbpmOperateManager jbpmOperateManager = (JbpmOperateManager) SpringContextUtil.getBean("jbpmOperateManager");
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringContextUtil.getBean("flowConfigManager");
		RoleManager roleManager = (RoleManager) SpringContextUtil.getBean("roleManager");
		GroupManager groupManager = (GroupManager) SpringContextUtil.getBean("groupManager");
		PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
		P2GManager p2gManager = (P2GManager) SpringContextUtil.getBean("p2gManager");
		String actoridsResult = "";
		try {
			TaskInstance ti = null;
			if(executionContext.getContextInstance().getTransientVariable("ti")!=null){
				ti = (TaskInstance) executionContext.getContextInstance().getTransientVariable("ti");
			}
			else{
				ti = executionContext.getTaskInstance();
			}
			// 用户登录名
			String userLoginName = (String) executionContext.getContextInstance().getTransientVariable(WFConstants.WF_VAR_LOGINNAME);
			//流程实例
			ProcessInstance pi = null;
			pi = executionContext.getContextInstance().getProcessInstance();
			Long pdefId = pi.getProcessDefinition().getId();
			FlowConfig flowConfig = flowConfigManager.findFlowConfigByPDID(pdefId);
			String flowName = flowConfig.getName();
			Person person = null;
			Collection<Group> group = null;
			if (!Constants.ADMINISTRATOR_ACCOUNT.equals(userLoginName)) {
				person = personManager.findPersonByLoginName(userLoginName);
				group = groupManager.findGroupByPerson(person);
			}
			// 是否过滤本部门人员
			Boolean flagIsSelfDept = false;
			// 是否有公式过滤(规则暂时只支持从业务数据中动态选取人员、部门或者角色来过滤)
			Boolean flagIsCalculate = false;
			// 人员计算公式
			String personCal = "";
			// 组织计算公式
			String groupCal = "";
			// 角色计算公式
			String roleCal = "";
			
			StringBuffer sb = new StringBuffer();
			JSONArray jsonArray =  JSONArray.fromObject(arrJson);
			for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				
				String persons = JSONUtil.getPropertyValueByJsonObject(object.toString(),"persons");
				String groups = JSONUtil.getPropertyValueByJsonObject(object.toString(),"groups");
				String roles = JSONUtil.getPropertyValueByJsonObject(object.toString(),"roles");
				
				sb.append(actors(groups,roles,persons) + ",");
				
				List personlist = new ArrayList();
				
				//本部门负责人
				Boolean selfdeptcharge = Boolean.parseBoolean(JSONUtil.getPropertyValueByJsonObject(object.toString(),"is_self_dept"));
				if(selfdeptcharge && userLoginName!=null && !userLoginName.equals("")){
					if(userLoginName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
						personlist.add(Constants.ADMINISTRATOR_ACCOUNT);
					}
					else{
						for(Group g:group){
							String id = (String) g.getId();
							Collection<P2G> p2g = groupManager.findAdminPersons(id);
							for(P2G p:p2g){
								Person per = p.getPerson();
								personlist.add(per.getUserLoginName());
							}
						}
					}
				}
				//流程创建者
				Boolean pcreator = Boolean.parseBoolean(JSONUtil.getPropertyValueByJsonObject(object.toString(),"is_pcreator"));
				if(pcreator && userLoginName!=null && !userLoginName.equals("")){
					if(userLoginName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
						personlist.add(Constants.ADMINISTRATOR_ACCOUNT);
					}
					else{
						Token token = executionContext.getToken();
						String id = (String) executionContext.getContextInstance().getVariable(WFConstants.WF_VAR_CREATOR, token);
						//Person p = personManager.get(id);
						personlist.add(id);
					}
					
				}
				//上级部门负责人
				Boolean updept = Boolean.parseBoolean(JSONUtil.getPropertyValueByJsonObject(object.toString(),"is_up_dept"));
				if(updept && userLoginName!=null && !userLoginName.equals("")){
					if(userLoginName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
						personlist.add(Constants.ADMINISTRATOR_ACCOUNT);
					}
					else{
						for(Group g:group){
							if(g.getParentGroup() == null) break;
							String id = (String) g.getParentGroup().getId();
							Collection<P2G> p2g = groupManager.findAdminPersons(id);
							for(P2G p:p2g){
								Person per = p.getPerson();
								personlist.add(per.getUserLoginName());
							}
						}
					}
				}
				//上一环节处理人
				Boolean upnodeperson = Boolean.parseBoolean(JSONUtil.getPropertyValueByJsonObject(object.toString(),"up_node_person"));
				if(upnodeperson && userLoginName!=null && !userLoginName.equals("")){
					if(userLoginName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
						personlist.add(Constants.ADMINISTRATOR_ACCOUNT);
					}
					else{
						//Person p = personManager.get(actors);
						personlist.add(userLoginName);
					}
				}
				
				// 是否为本部门人员
				Boolean selfDept = Boolean.parseBoolean(JSONUtil.getPropertyValueByJsonObject(object.toString(), "is_dept_filter"));
				if (selfDept) {
					flagIsSelfDept = true;
				}
				
				// 规则计算公式
				personCal = JSONUtil.getPropertyValueByJsonObject(object.toString(),"personCal");
				groupCal = JSONUtil.getPropertyValueByJsonObject(object.toString(), "groupCal");
				roleCal = JSONUtil.getPropertyValueByJsonObject(object.toString(), "roleCal");
				if (StringUtil.isNotEmpty(personCal) || StringUtil.isNotEmpty(groupCal) || StringUtil.isNotEmpty(roleCal)) {
					flagIsCalculate  = true;
				}
				
				for(int i = 0;i < personlist.size();i++){
					String p = (String) personlist.get(i);
					sb.append(p + ",");
				}
			}
			String actorids = StringUtil.delEndString(sb.toString(),",");
			
			Set actorSet = new HashSet();
			// 过滤本部门人员
			if (flagIsSelfDept && !Constants.ADMINISTRATOR_ACCOUNT.equals(userLoginName)) {
				String[] actoridx = actorids.split(",");
				for (String actor : actoridx) {
					Person actorPerson = personManager.findPersonByLoginName(actor);
					Collection<Group> actorGroup = groupManager.findGroupByPerson(actorPerson);
					for (Group g : actorGroup) {
						Boolean flagTmp = false;
						for (Group loginGroup : group) {
							if (g.getId().equals(loginGroup.getId())) {
								actorSet.add(actor);
								flagTmp = true;
								break;
							}
						}
						if (flagTmp) {
							break;
						}
					}
				}
			} else {
				String[] actoridx = actorids.split(",");
				for (String actor : actoridx) {
					actorSet.add(actor);
				}
			}
			
			List<String> list = new ArrayList<String>(actorSet);
			// 进行公式计算
			if (flagIsCalculate) {
				Velocity.init();
				StringWriter writer = new StringWriter();
				VelocityContext context = new VelocityContext();
				EFormExpVelocityCF api = new EFormExpVelocityCF();
				api.setContextMap(ti.getProcessInstance().getContextInstance().getVariables());
				context.put("CF", api);

				// 如果有组织计算公式
				String groupName = "";
				if (StringUtil.isNotEmpty(groupCal)) {
					groupCal = groupCal.replace("$34$", "\"");
					groupName = Velocity.evaluate(context, writer, null, groupCal)?writer.toString():groupCal;
					//流程里面，通过申请人部门过滤
					if(("物资需求计划申请").equals(flowName) || ("物资补料计划申请").equals(flowName) || ("立项委托单").equals(flowName) || ("开工签证").equals(flowName) || ("工程验收").equals(flowName) || ("工程结算支付单").equals(flowName)){
						if(StringUtil.isNotEmpty(groupName)){
							if(!(groupName.contains("部"))){
								Group group2 = groupManager.findGroupByBzName(groupName);
								groupName = group2.getGroupName();
							}
						}
					}
				}
				int count = list.size();
				for (int i = 0; i < count; i++) {
					String actor = list.get(i);
					Person actorPerson = personManager.findPersonByLoginName(actor.toString());
					if (StringUtil.isNotEmpty(groupName)) {
						String[] groupNames = groupName.split(",");
						boolean flagIsInGroup = true;
						for (String groupNamex : groupNames) {
							Group groupx = groupManager.findGroupByName(groupNamex);
							if (groupx !=null && !p2gManager.isExist(groupx.getId().toString(), actorPerson.getId().toString())) {
								flagIsInGroup = false;
							} else {
								flagIsInGroup  = true;
								break;
							}
						}
						if (!flagIsInGroup) {
							list.remove(actor);
							i--;
							count--;
						}
					}
				}
			}
			
			Boolean hasRoot = false;
			if(actorids.indexOf("root") != -1)	hasRoot = true;
			for (String actor : list) {
				if(!actor.equals("null") && !actor.equals(Constants.ADMINISTRATOR_ACCOUNT))
					actoridsResult += actor + ",";
			}
			
			if(hasRoot == true)
				actoridsResult += Constants.ADMINISTRATOR_ACCOUNT + ",";
			actoridsResult = StringUtil.delEndString(actoridsResult.toString(),",");
			if(actoridsResult == null)	actoridsResult = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actoridsResult;
	}
	
	
	
	public static void main(String[] args) {
		String groupIds = "'40288ac42235b274012235b3b0a50003'";
		String roleIds = "";
		String userLoginNames = "";
		
		//System.out.println(actors(groupIds, roleIds, userLoginNames));
	}


	@SuppressWarnings("static-access")

	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public static ExecutionContext getExecutionContext() {
		return executionContext;
	}
}
