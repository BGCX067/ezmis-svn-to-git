package com.jteap.system.role.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.manager.R2RManager;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.R2R;
import com.jteap.system.role.model.Role;

/**
 * 角色Action
 * 
 * @author tantyou
 * @date 2008-1-19
 */
@SuppressWarnings({ "unchecked", "serial" })
public class RoleAction extends AbstractTreeAction<Role> {
	//角色管理器
	private RoleManager roleManager;
	//role to resource 中间对象管理器
	private R2RManager r2rManager;
	//用户管理器
	private PersonManager personManager;
	private Collection<Role> currentRoles;//当前登录用户说拥有的所有角色的集合
	
	private P2GManager p2gManager;

	/**
	 * 显示这个角色的详细信息
	 * 
	 * @author 唐剑钢 2008-1-19
	 * @throws IOException
	 */
	public String showRoleDetailAction() throws Exception {
		String id = request.getParameter("roleId");
		Object obj = roleManager.get(id);
		String json = JSONUtil.objectToJson(obj, updateJsonProperties());
		outputJson("{success:true,data:[" + json + "]}");
		return NONE;
	}

	
	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */ 

	public String showRoleTreeForCheckAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		//设定扩展字段
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			
			public void beanHandler(Object obj, Map map) {
				map.put("checked", new Boolean(false));
				map.put("ccCheck", new Boolean(false));
			}
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}

	/**
	 * 
	 * 方法功能描述 :选中已经选中的CHECBOX，提供给用户修改
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-1-23
	 * 返回类型：String
	 */
	public String showRloeTreeForIsCheckAction()throws Exception{
		final HashMap<String,String> rolemap=new HashMap<String,String>();
		String presonid=request.getParameter("id");
		Person person = null;
		if(StringUtil.isNotEmpty(presonid)){
			person=this.personManager.get(presonid);
		}
		
		if(person != null){
			//存入选中的CHECBOX
			Set<P2Role> p2r=person.getRoles();
		    for(P2Role pr:p2r){
		    	Role role=pr.getRole();
		    	 rolemap.put(role.getId().toString(), role.getId().toString());
		    }
		}
		
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				//如果这个对象的ID和这个角色对的ID相等那么就过滤掉,设置真,否则设置为假
				map.put("ccCheck", new Boolean(true));
				
				Role role=(Role) obj;
				
				map.put("leaf", role.getChildRoles().size()>0?false:true);
				map.put("expanded", true);
				if(rolemap.containsKey(role.getId().toString())){
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
		this.outputJson(result.toString());
		return NONE;
	}
	/**
	 * 简单创建角色的动作,只需要提供角色组织编号,角色名称，就可以创建一个角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public String simpleCreateRoleAction() throws Exception {
		String parentId = request.getParameter("parentId");
		String roleName = request.getParameter("roleName");
		String preSortNo = request.getParameter("preSortNo");

		Role newRole = new Role();
		newRole.setRolename(roleName);
		newRole.setSortNo(Integer.parseInt(preSortNo) + 1000) ;
		// 设定角色创建用户
		newRole.setCreator(personManager.getCurrentPerson(sessionAttrs).getUserLoginName());
		
		// 在创建一个角色的时候,默认的设置为可以继承
		newRole.setInheritable(true);
		if (StringUtils.isNotEmpty(parentId)) {
			Role parentRole = roleManager.get(parentId);
			// 如果这个角色有继承权限
			Set<R2R> ress = parentRole.getResources();
			for (R2R res : ress) {
				if (res.isCommunicable()) {
					R2R r2r = new R2R();
					Resource resource = res.getResource();
					r2r.setRole(newRole);
					r2r.setCommunicable(true);
					r2r.setInherit(true);
					r2r.setResource(resource);
					r2rManager.save(r2r);
				}
			}
			newRole.setParentRole(parentRole);
		}
		roleManager.save(newRole);
		outputJson("{success:true,id:'" + newRole.getId() + "'}");
		return NONE;
	}

	
	
	
	@Override
	public String deleteNodeAction() throws Exception {
		String roleId = request.getParameter("nodeId");
		roleManager.removeById(roleId);
		roleManager.evitRoles();
		outputJson("{success:true}");
		return NONE;
	}


	/**
	 * 删除角色动作之前，需要确定该角色没有子组织了，而且没有资源关联了
	 */
	@Override
	protected String beforeDeleteNode(Object node) {
		Role role = (Role) node;
		if (role.getChildRoles().size() > 0) {
			return "请先删除该角色的子角色";
		}
		return null;
	}

	/**
	 * 修改一个角色之前，需确定这个父角色，如果修改了继承开关属性，需要进行相应的操作
	 */
	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		Role role = (Role) obj;
		Role original=(Role) originalObject;
		if (request.getParameter("inheritable") == null) {
			role.setInheritable(false);
		}
		if(role.getParentRole()!=null){
			if(original.isInheritable()==role.isInheritable()){
//				System.out.println("未修改");
			}else{
				//如果修改成了继承
				if(role.isInheritable()){
					r2rManager.cascadeExtendResouce(role.getId().toString(), role.getParentRole().getId().toString());
				}else{//如果修改成了不继承
					r2rManager.clearExtendResourceOfTheRole(role.getId().toString());
				}
//				System.out.println("修改了:"+original.isInheritable()+"->"+role.isInheritable());
			}
		}
		
	}

	@Override
	protected Collection getChildren(Object bean) {
//		if(isCurrentRootUser()){
			Role role=(Role) bean;
			return role.getChildRoles();
//		}else{
//			//非root用户，只显示自己拥有的角色
//			Collection children=new ArrayList();
//			Role role=(Role) bean;
//			Collection<Role> tmpChilds=role.getChildRoles();
//			for (Role rolex : tmpChilds) {
//				if(this.currentRoles.contains(rolex)){
//					children.add(rolex);
//				}
//			}
//			return children;
//		}
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentRole";
	}

	/**
	 * 取得根节点 当前人员所拥有的角色根节点集合 如果当前登录的是root用户，则不需要任何条件的取得所有角色
	 * 否则指定当前用户编号，取得当前人员所拥有的角色
	 */
	@Override
	protected Collection getRootObjects() {
		Person person = personManager.getCurrentPerson(sessionAttrs);
		//判断当前用户是否root用户
		if(this.isCurrentRootUser()){
			return roleManager.findRootRoles();
		} else {
			// 如果是非root用户,则取得当前用户所拥有的角色
			currentRoles = new ArrayList();
			for (P2Role p2r : person.getRoles()) {
				currentRoles.add(p2r.getRole());
			}
			Collection roles=roleManager.findRootRolesOfThePerson(person);
			currentRoles.addAll(roles);
			return currentRoles;
		}

	}

	/**
	 * 显示角色树
	 * 
	 * @author tantyou
	 * @date 2008-1-21
	 */
	public String showRoleTreeAction() throws Exception {
		JsonConfig jsonConfig = getTreeJsonConfig();// 配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor = new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				Role role=(Role) obj;
				map.put("uiProvider", "RoleNodeUI");
				map.put("creator",role.getCreator());
			}
		});
//		final Class cls = GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(Role.class, jsonBeanProcessor);

		// 开始json化
		Collection roots = getRootObjects();
		JSONArray result = JSONArray.fromObject(roots, jsonConfig);
		// 输出
		this.outputJson(result.toString());
		return NONE;

	}

	/**
	 * @see RoleAction
	 * 当拖拽节点到了新的父亲的时候，如果当前角色可以继承资源，则将从旧节点继承的资源除去，继承新节点的可传播资源
	 */
	@Override
	protected void dragMoveNodeProcess(Object obj, boolean parentChanged,
			String oldParentId, String newParentId) {
		
		//如果换了新的父亲节点的话，需要删除已经有的继承资源，继承新父亲角色的资源
		if(parentChanged){
			Role role=(Role) obj;
			if(role.isInheritable()){
				r2rManager.clearExtendResourceOfTheRole(role.getId().toString());
				if(StringUtils.isNotEmpty(newParentId)){
					r2rManager.cascadeExtendResouce(role.getId().toString(),newParentId);
				}	
			}
			
		}
	}

	/**
	 * 
	 * 描述 : 验证角色标识唯一性
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 异常 : Exception
	 */
	public String validateRoleSnUniqueAction() throws Exception {
		String roleSn=request.getParameter("roleSn");
		String roleId=request.getParameter("roleId");
		Role role=roleManager.findUniqueBy("roleSn", roleSn);
		if(role!=null){
			if(StringUtils.isNotEmpty(roleId) && roleId.equals(role.getId().toString())){
				outputJson("{unique:true}");
			}else{
				outputJson("{unique:false}");	
			}
		}else{
			outputJson("{unique:true}");
		}
		return NONE;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {

		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {

		return "rolename";
	}

	@Override
	public HibernateEntityDao getManager() {

		return roleManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "rolename", "id", "childRoles","creator","roleSn" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "rolename", "sortNo", "remark", "inheritable",
				"id","roleSn" };
	}

	public R2RManager getR2rManager() {
		return r2rManager;
	}

	public void setR2rManager(R2RManager manager) {
		r2rManager = manager;
	}
	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}


	public Collection<Role> getCurrentRoles() {
		return currentRoles;
	}


	public void setCurrentRoles(Collection<Role> currentRoles) {
		this.currentRoles = currentRoles;
	}


	public P2GManager getP2gManager() {
		return p2gManager;
	}


	public void setP2gManager(P2GManager manager) {
		p2gManager = manager;
	}

}
