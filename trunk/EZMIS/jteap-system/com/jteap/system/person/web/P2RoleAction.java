package com.jteap.system.person.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.P2RoleManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
/**
 * 
 * 类功能描述:主要是对角色和用户的中间表操作
 * @author 唐剑钢
 * 2008-2-19
 */
@SuppressWarnings({ "unchecked", "serial" })
public class P2RoleAction extends AbstractAction {

	private P2RoleManager p2roleManager;
	private PersonManager personManager;
	private RoleManager	roleManager;
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	/**
	 * 
	 * 方法功能描述 :给指定的用户，设定角色(对应前台的 设定角色按钮)
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-2-19
	 * 返回类型：String
	 */
	public String setupRoleForPersonAction()throws Exception{
		//指定人
		String indicator=(String)this.request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		String	personIdx=request.getParameter("personids");
		String  roleIdx=request.getParameter("roleids");
		
		if(StringUtils.isNotEmpty(personIdx)){
			//人员数组
			String	personIds[]=personIdx.split(",");
			boolean bMuti = personIds.length>0;	//多人设置标记
			//关系设置
			for(String pid:personIds){
				Person person=personManager.get(pid);
				Set<P2Role> p2rs = person.getRoles();
				if(!bMuti){
					Object[] roles = person.getRoles().toArray();
					for (Object object : roles) {
						P2Role role =(P2Role) object;
						person.getRoles().remove(role);
						p2roleManager.remove(role);
					}
				}
				// 清除人员所有的角色
				if (p2rs != null) {
					p2rs.clear();
				}
				// 指定角色不为空则将此角色与人员关联
				if(StringUtil.isNotEmpty(roleIdx)){
					//指定的角色数组
					String 	roleIds[]=roleIdx.split(",");
					for(String	rid:roleIds){
						Role role=roleManager.get(rid);
						//如果对应的用户没有指定过这个角色,就进行关联，否则，不关联
						P2Role p2role=new P2Role();
						p2role.setPerson(person);
						p2role.setRole(role);
						p2role.setIndicator(indicator);
						p2rs.add(p2role);
					}
				}
				person.setRoles(p2rs);
				personManager.save(person);
			}
		}
		_clearL2Cache();
		outputJson("{success:true}");
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 将指定用户添加到角色
	 * 作者 : wangyun
	 * 时间 : Aug 15, 2010
	 * 异常 : Exception
	 */
	public String addPerson2RoleAction() throws Exception {
		String personIds = request.getParameter("personIds");
		String roleId = request.getParameter("roleId");
		//指定人
		String indicator=(String)this.request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		try {
			Role role = roleManager.get(roleId);
			String[] strPerson = personIds.split(",");
			for (String personId : strPerson) {
				P2Role p2role = new P2Role();
				Person person = personManager.get(personId);
				boolean isExist = p2roleManager.isExist(person, role);
				if (isExist) {
					continue;
				}
				p2role.setPerson(person);
				p2role.setRole(role);
				p2role.setIndicator(indicator);
				p2roleManager.save(p2role);
			}
			_clearL2Cache();
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 解除这个用户角色的关系
	 * 作者 : wangyun
	 * 时间 : Aug 15, 2010
	 * 异常 :
	 */
	public String removePersonFromRoleAction() throws Exception {
		String personidss = request.getParameter("personids");
		String roleId = request.getParameter("roleId");
		final String personids[] = personidss.split(",");
		final StringBuffer msg = new StringBuffer();

		Role role = roleManager.get(roleId);
		for (String personId : personids) {
			Person person = personManager.get(personId);
			P2Role p2r = p2roleManager.findByPersonAndRole(person, role);
			if (p2r != null) {
				if (person.getId().equals(Constants.SESSION_CURRENT_PERSONID)
						&& !p2r.getIndicator().equals(personManager.getCurrentPerson(sessionAttrs).getUserLoginName())) {
					msg.append("您没有权限从当前组织中移除本人");
					break;
				}
				p2roleManager.remove(p2r);
			}
		}
		_clearL2Cache();
		outputJson("{success:true,msg:'" + msg + "'}");
		return NONE;
	}

	/**
	 * 清理二级缓存
	 */
	private void _clearL2Cache() {
		sessionFactory.evict(P2Role.class);
		sessionFactory.evict(Role.class);
		sessionFactory.evict(Person.class);
		sessionFactory.evictCollection("com.jteap.system.person.model.Person.roles");
		sessionFactory.evictCollection("com.jteap.system.role.model.Role.persons");
		sessionFactory.evictQueries();
		
	}
	
	
	public HibernateEntityDao getManager() {
		return p2roleManager;
	}
	
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		String roleId=request.getParameter("roleId");
		if(StringUtils.isNotEmpty(roleId)){
			HqlUtil.addCondition(hql, "role.id", roleId);
		}
		String hqlWhere = WebUtils.getRequestParam(request, "queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		
		HqlUtil.addCondition(hql, "person.isDel",0);
		//默认排序方式
		if(!this.isHaveSortField())
			HqlUtil.addOrder(hql, "person.userName", "desc",false);
	}

	public String[] listJsonProperties() {
		return new String[]{"id","person","role","userName","admin","userLoginName","userLoginName2","sex","status"};
	}

	public String[] updateJsonProperties() {
	
		return null;
	}

	public P2RoleManager getP2roleManager() {
		return p2roleManager;
	}

	public void setP2roleManager(P2RoleManager manager) {
		p2roleManager = manager;
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
	
	


}
