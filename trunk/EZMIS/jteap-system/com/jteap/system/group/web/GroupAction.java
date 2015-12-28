package com.jteap.system.group.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.Person;

/**
 * 处理用户信息的action
 * @author tantyou
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class GroupAction extends AbstractTreeAction<Group>{

	//用户管理器
	private GroupManager groupManager;
	
    private PersonManager personManager;
    
    private SessionFactory sessionFactory;
    
    private Collection rootGroups;
	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(Group.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				Group group=(Group) obj;
				map.put("creator",group.getCreator());
				map.put("uiProvider", "GroupNodeUI");
				map.put("qtip", group.getGroupName());
				map.put("leaf", group.getChildGroups().size()>0?false:true);
				//只展开第一层
				if(group.getParentGroup()==null)
					map.put("expanded", true);
				else
					map.put("expanded", false);
			}
		});
		
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}
	
	
	@Override
	public String deleteNodeAction() throws Exception {
		String groupId = request.getParameter("nodeId");
		groupManager.removeById(groupId);
		groupManager.evitGroups();
		outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 简单创建组织的动作,只需要提供父亲组织编号,组织名称，就可以创建一个组织
	 * @return
	 * @throws Exception
	 */
	public String simpleCreateGroupAction() throws Exception{
		String parentId=request.getParameter("parentId");
		String groupName=request.getParameter("groupName");
		String preSortNo = request.getParameter("preSortNo");
		//如果添加节点在游离用户下面 则会出现
		if("".equals(preSortNo)) {
			preSortNo = "0";
		}
		Group newGroup=new Group();
		newGroup.setGroupName(groupName);
		newGroup.setCreator(personManager.getCurrentPerson(sessionAttrs).getUserLoginName());
		newGroup.setSortNo(Integer.parseInt(preSortNo)+1000);
		if(StringUtils.isNotEmpty(parentId)){
			Group parentGroup=groupManager.get(parentId);
			newGroup.setParentGroup(parentGroup);
		}
		
		groupManager.save(newGroup);
		groupManager.evitGroups();
		outputJson("{success:true,id:'"+newGroup.getId()+"'}");
		return NONE;
	}
	
	/**
	 * 取得指定组织的管理用户
	 * @return
	 * @throws Exception
	 */

	public String getAdminPersonAction() throws Exception{
		String groupId=request.getParameter("id");
		if(StringUtils.isNotEmpty(groupId)){
			Collection persons= groupManager.findAdminPersons(groupId);
			String json=JSONUtil.listToJson(persons,new String[]{"id","person","userLoginName","userName"});
			
			json="{success:true,totalCount:'"+persons.size()+"',list:"+json+"}"; 
			
			this.outputJson(json);
		}else{
			this.outputJson("{success:true,totalCount:'0',list[]}");
		}
		return NONE;
	}
	
	/**
	 * 取得指定组织的非管理用户
	 * @return
	 * @throws Exception
	 */
	public String getNotAdminPersonAction() throws Exception{
		return NONE;
	}
	/**
	 * 
	 * 方法功能描述 :选中已经原有的CHECBOX，提供给用户修改
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2007-12-23
	 * 返回类型：String
	 */
	public String showGroupTreeForIsCheckAction()throws Exception{
		String presonid=request.getParameter("id");
		Person person=this.personManager.get(presonid);
		//存入选中的CHECBOX
		final HashMap<String,String> groupmap=new HashMap<String,String>();
		Set<P2G> p2g=person.getGroups();
	    for(P2G pg:p2g){
	    	Group group=pg.getGroup();
	    	groupmap.put(group.getId().toString(), group.getId().toString());
	    }
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				//如果这个对象的ID和这个角色对的ID相等那么就过滤掉,设置真,否则设置为假
				Group group=(Group) obj;
				map.put("ccCheck", new Boolean(true));
				map.put("leaf", group.getChildGroups().size()>0?false:true);
				map.put("expanded", true);
				
				if(groupmap.containsKey(group.getId().toString())){
				   map.put("checked", new Boolean(true));
				}else{
				   map.put("checked", new Boolean(false));
				}
			}
			
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		String json=result.toString();
		this.outputJson(json);
		return NONE;
	}
	
	/**
	 * 显示带checkbox的Tree
	 * @return
	 * @throws Exception
	 */
	public String showGroupTreeForCheckAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();

		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				Group group=(Group) obj;
				map.put("ccCheck", new Boolean(true));
				map.put("leaf", group.getChildGroups().size()>0?false:true);
				map.put("expanded", true);
				map.put("checked", new Boolean(false));
			}
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		String json=result.toString();
		this.outputJson(json);
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 验证组织标识唯一性
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 异常 : Exception
	 */
	public String validateGroupSnUniqueAction() throws Exception {
		String groupSn=request.getParameter("groupSn");
		String groupId=request.getParameter("groupId");
		Group group=groupManager.findUniqueBy("groupSn", groupSn);
		if(group!=null){
			if(StringUtils.isNotEmpty(groupId) && groupId.equals(group.getId().toString())){
				outputJson("{unique:true}");
			}else{
				outputJson("{unique:false}");	
			}
		}else{
			outputJson("{unique:true}");
		}
		return NONE;
	}
	
	/**
	 * 在列表的时候，需要json化的字段
	 */
	@Override
	public String[] listJsonProperties() {
		return new String[]{"groupName","creator","id","childGroups","qtip"};	
	}
	
	
	/**
	 * 更新的时候需要json化的字段
	 */
	@Override
	public String[] updateJsonProperties() {
		return new String[]{"groupName","remark","creator","groupSn"};
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		return groupManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
//		Group group=(Group) bean;
//		return group.getChildGroups();
		return null;
	}

	/**
	 * 取得组织树根节点集合
	 * 如果是管理员登录 根节点应该从所有组织中查询
	 * 否则只显示当前用户所在的组织作为根节点
	 * 比如用户X存在与组织A/B/C、A/B、D/E/F、D
	 * 此时返回的组织集合应该为A/B、D两项作为根节点
	 */
	@Override
	protected Collection getRootObjects() {
		//根据父亲节点查询
		String parentId=request.getParameter("parentId");
		if(parentId==null){
			parentId = request.getParameter("node");
		}
		if(StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")){
			return groupManager.findGroupByParentId(parentId);
		}
		rootGroups=groupManager.findRootGroups();
		/**
		 * 该系统比较小，不需要数据权限的过滤，故注释掉该段
		
		//查询初始根节点
		if(this.isCurrentRootUser()){
			//管理员无权限限制，显示所有的根节点
			
		}
		else{
			
		} */
		return rootGroups;
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "groupName";
	}
	
	protected String getSortNoPropertyName(Class beanClass){
		return "sortNo";
	}


	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentGroup";
	}


	@Override
	protected void dragMoveNodeProcess(Object obj, boolean parentChanged,
			String oldParentId, String newParentId) {
		sessionFactory.evictCollection("com.jteap.system.group.model.Group.childGroups");
	}

	/**
	 * 删除组织动作之前，需要确定该组织没有子组织了，而且没有人员关联了
	 */
	@Override
	protected String beforeDeleteNode(Object node) {
		Group group=(Group) node;
		if(group.getChildGroups().size()>0){
			return "请先删除该组织的子组织";
		}
		if(group.getPersons().size()>0){
			return "请将该组织的人员删除或者移除到别的组织，然后再删除当前组织";
		}
		return null;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	


}
