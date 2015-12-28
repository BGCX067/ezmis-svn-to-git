package com.jteap.jhtj.sjqx.web;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.sjqx.manager.SjqxManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
/**
 * 
 *描述：数据权限的action
 *时间：2010-5-26
 *作者：童贝
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SjqxAction extends AbstractTreeAction {
	private SjqxManager sjqxManager;
	private RoleManager roleManager;
	private PersonManager personManager;
	private Collection<Role> currentRoles;//当前登录用户说拥有的所有角色的集合
	@Override
	protected Collection getChildren(Object bean) {
		Role role=(Role) bean;
		return role.getChildRoles();
	}

	@Override
	protected String getParentPropertyName(Class arg0) {
		return "parentRole";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
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
	 * 
	 *描述：保存数据权限
	 *时间：2010-5-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String saveQxAction() throws Exception{
		String flids=request.getParameter("flids");
		String bbids=request.getParameter("bbids");
		String roleid=request.getParameter("roleid");
		if(StringUtils.isNotEmpty(roleid)){
			this.sjqxManager.removeAllByRoleId(roleid);
		}
		if(StringUtils.isNotEmpty(flids)){
			this.sjqxManager.saveSjqx(flids,roleid, "1");
		}
		if(StringUtils.isNotEmpty(bbids)){
			this.sjqxManager.saveSjqx(bbids,roleid, "2");
		}
		outputJson("{success:true}");
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

		return new String[] { "rolename", "id", "childRoles","creator" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "rolename", "sortNo", "remark", "inheritable",
				"id" };
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

	public SjqxManager getSjqxManager() {
		return sjqxManager;
	}

	public void setSjqxManager(SjqxManager sjqxManager) {
		this.sjqxManager = sjqxManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

}
