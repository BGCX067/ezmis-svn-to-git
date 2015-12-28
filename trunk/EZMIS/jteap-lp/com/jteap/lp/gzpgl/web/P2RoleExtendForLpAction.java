package com.jteap.lp.gzpgl.web;

import com.jteap.system.person.manager.P2RoleManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.person.web.P2RoleAction;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;

/**
 * 人员角色关联Action--两票继承使用
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial"})
public class P2RoleExtendForLpAction extends P2RoleAction {

	private PersonManager personManager;
	private RoleManager roleManager;
	private P2RoleManager p2roleManager;
	
	/**
	 * 
	 * 描述 : 判断人员是否存在在角色中 支持多角色 判断 or关系
	 * 作者 : wangyun
	 * 时间 : 2010-12-15
	 * 异常 : Exception
	 * 
	 */
	public String validatePersonInRole() throws Exception {
		String personId = request.getParameter("personId");
		String roleSn = request.getParameter("roleSn");
		boolean is_rolw =false;
		Person person = personManager.get(personId);
		for(String rolesn:roleSn.split(",")){
			Role role = roleManager.findUniqueBy("roleSn", rolesn);
			P2Role p2role = p2roleManager.findByPersonAndRole(person, role);
			if(p2role!=null){
				is_rolw = true;
				break;
			}
		}
		
	
		String json = "{success:"+is_rolw+"}";
		this.outputJson(json);
		return NONE;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public P2RoleManager getP2roleManager() {
		return p2roleManager;
	}

	public void setP2roleManager(P2RoleManager manager) {
		p2roleManager = manager;
	}
	
}
