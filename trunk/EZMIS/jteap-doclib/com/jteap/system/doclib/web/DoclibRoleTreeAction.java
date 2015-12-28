package com.jteap.system.doclib.web;

import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.role.manager.R2RManager;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;

@SuppressWarnings({"serial", "unchecked"})
public class DoclibRoleTreeAction extends AbstractTreeAction<Role>{
	//角色管理器
	private RoleManager roleManager;
	//role to resource 中间对象管理器
	private R2RManager r2rManager;
	//用户管理器
	private PersonManager personManager;
	private Collection<Role> currentRoles;//当前登录用户说拥有的所有角色的集合
	
	private P2GManager p2gManager;

	
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

	@Override
	protected Collection getChildren(Object bean) {
		Role role=(Role) bean;
		return role.getChildRoles();

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
		//Person person = personManager.getCurrentPerson(sessionAttrs);
		//判断当前用户是否root用户
		//if(this.isCurrentRootUser()){
			return roleManager.findRootRoles();
//		} else {
//			// 如果是非root用户,则取得当前用户所拥有的角色
//			currentRoles = new ArrayList();
//			for (P2Role p2r : person.getRoles()) {
//				currentRoles.add(p2r.getRole());
//			}
//			Collection roles=roleManager.findRootRolesOfThePerson(person);
//			currentRoles.addAll(roles);
//			return currentRoles;
//		}

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
	public String[] listJsonProperties() {

		return new String[] { "rolename", "id", "childRoles","creator" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "rolename", "sortNo", "remark", "inheritable",
				"id" };
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
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

	@Override
	public HibernateEntityDao getManager() {
		return roleManager;
	}

}
