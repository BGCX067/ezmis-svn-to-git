package com.jteap.system.person.web;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.MD5;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.person.manager.P2ResManager;
import com.jteap.system.person.manager.P2RoleManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.OperationManager;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;

/**
 * 处理用户信息的action
 * 
 * @author tantyou
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class P2GAction extends AbstractAction {
	private GroupManager groupManager;
	private PersonManager personManager;
	private P2GManager p2gManager;
	private P2RoleManager p2roleManager;
	private RoleManager roleManager;
	private OperationManager operationManager;
	private P2ResManager p2resManager;

	/**
	 * 列表之前的预处理
	 */
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String groupId = request.getParameter("groupId");
		if (StringUtils.isNotEmpty(groupId)) {
			HqlUtil.addCondition(hql, "group.id", groupId);
		}
		String isAdmin = request.getParameter("isAdmin");
		if (StringUtils.isNotEmpty(isAdmin)) {
			HqlUtil.addCondition(hql, "isAdmin", isAdmin);
		}
		// String hqlWhere =
		// StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
		String hqlWhere = request.getParameter("queryParamsSql");
		// 查询该组织下的人员
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}

		HqlUtil.addCondition(hql, "person.isDel",0);
		
		if(!this.isHaveSortField()){
			// 默认排序方式
			HqlUtil.addOrder(hql, "isAdmin", "desc", false);
		}
		
	}

	/**
	 * 复制当前人员到指定的组织去
	 * 
	 * @return
	 * @throws Exception
	 */
	public String copyPersonsToTheGroupAction() throws Exception {
		String groupId = request.getParameter("groupId");
		String personIds = request.getParameter("personIds");

		String[] ids = personIds.split(",");
		Group group = p2gManager.get(Group.class, groupId);

		int i = 0;
		String msg = "";

		for (String personId : ids) {
			try {
				Person person = p2gManager.get(Person.class, personId);
				if (person.equals(personManager.getCurrentPerson(sessionAttrs))) {
					msg = "您没有权限移动或复制本人所在的组织";
					break;
				}
				if (!p2gManager.isExist(groupId, personId)) {

					P2G p2g = new P2G();
					p2g.setPerson(person);
					p2g.setGroup(group);
					p2g.setAdmin(false);
					p2g.setIndicator(personManager.getCurrentPerson(
							sessionAttrs).getUserLoginName());
					p2gManager.save(p2g);
					i++;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				msg += "复制人员【" + personId + "】的时候出现异常";
				continue;
			}
		}
		this.outputJson("{success:true,copy:" + i + ",msg:'" + msg + "'}");
		return NONE;
	}

	/**
	 * 将指定人员从当前组织移动到新的组织中去
	 * 
	 * @return
	 * @throws Exception
	 */
	public String movePersonsToTheGroupAction() throws Exception {
		// 从那个组织
		String fromGroupId = request.getParameter("fromGroupId");
		// 到那个组织
		String toGroupId = request.getParameter("toGroupId");
		// 那些人
		String personIds = request.getParameter("personIds");
		String[] ids = personIds.split(",");
		// 得到组织
		Group toGroup = p2gManager.get(Group.class, toGroupId);

		int i = 0;
		String msg = "";
		for (String personId : ids) {
			try {
				Person person = p2gManager.get(Person.class, personId);
				if (person.equals(personManager.getCurrentPerson(sessionAttrs))) {
					msg = "您没有权限移动或复制本人所在的组织";
					break;
				}
				if (!p2gManager.isExist(toGroupId, personId)) {

					// 清除之前的关联，并建立新的关联
					if (StringUtils.isNotEmpty(fromGroupId))
						p2gManager
								.deleteByPersonAndGroup(personId, fromGroupId);

					P2G p2g = new P2G();
					p2g.setPerson(person);
					p2g.setGroup(toGroup);
					p2g.setAdmin(false);
					p2g.setIndicator(personManager.getCurrentPerson(
							sessionAttrs).getUserLoginName());
					p2gManager.save(p2g);
					i++;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				msg += "移动人员【" + personId + "】的时候出现异常";
				continue;
			}
		}
		this.outputJson("{success:true,copy:" + i + ",msg:'" + msg + "'}");
		return NONE;
	}

	/**
	 * 
	 * 方法功能描述 :修改人员组织，角色
	 * 
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 *             2007-12-23 返回类型：String
	 */
	public String updatePersonAction() throws Exception {
		String creator = (String) this.request.getSession().getAttribute(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		boolean bRoleSetup = Boolean.parseBoolean(request
				.getParameter("bRoleSetup"));
		boolean bGroupSetup = Boolean.parseBoolean(request
				.getParameter("bGroupSetup"));

		String personid = request.getParameter("id");
		Person person = this.personManager.get(personid);
		// 转字符编码
		String username = request.getParameter("userName");
		String date = request.getParameter("userBirthday");
		String config = request.getParameter("userConfig");
		String sex = request.getParameter("userSex");
		String groupId = request.getParameter("groupId");
		String roleId = request.getParameter("roleId");
		String groupIds[] = groupId.split(",");
		String roleIds[] = roleId.split(",");
		String userLoginName = request.getParameter("userLoginName");
		String userLoginName3 = request.getParameter("userLoginName2");
		
		person.setUserLoginName2(userLoginName3);
		// 重新设置人员的信息
		person.setSex(sex);
		// 登录名
		if (!person.getUserName().equals(username)) {
			String userLoginName2 = personManager.converterToFirstLetterSpell(username);
			person.setUserLoginName2(userLoginName2);
		}
		person.setUserName(username);
		person.setUserLoginName(userLoginName);
		person.setConfig(config);
		person.setBirthday((StringUtils.isNotEmpty(date)) ? DateUtils
				.parseDate(date) : null);

		// 设置与组织关联
		if (bGroupSetup) {
			createP2G(groupIds, person, creator, true);
		}
		// 设置与角色关联
		if (bRoleSetup) {
			createP2R(roleIds, person, creator, true);
		}
		// 如果组织和角色没有变更
		this.personManager.save(person);
		return NONE;
	}
	/**
	 * linkRoleAction(角色关联)
	 * TODO(将src的角色付给target)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @throws Exception 
	 * @Exception 异常对象
	*/
	public String linkRoleAction() throws Exception{
	    String srcPersonId=request.getParameter("srcPersonId");
	    String targetPersonId=request.getParameter("targetPersonId");
	    String creator = (String) this.request.getSession().getAttribute(
                Constants.SESSION_CURRENT_PERSON_LOGINNAME);
	    Person srcPerson=this.personManager.get(srcPersonId);
	    Person targetPerson=this.personManager.get(targetPersonId);
	    Set<P2Role> set=targetPerson.getRoles();
	    String roleId="";
	    for(P2Role p2r:set)
	        roleId +=p2r.getRole().getId()+",";
	    String[] roleIds=roleId.split(",");
	    createP2R(roleIds, srcPerson, creator, true);
	    this.outputJson("{success:true}");
	    return NONE;
	}

	/**
	 * 
	 * 方法功能描述 :解除这个用户和组织，角色的关系
	 * 
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 *             2007-12-25 返回类型：String
	 */
	public String removePersonFromGroupAction() throws Exception {
		String p2gIdss = request.getParameter("p2gids");
		final String p2gIds[] = p2gIdss.split(",");
		final StringBuffer msg = new StringBuffer();

		Collection<P2G> p2gs = p2gManager.findByIds(p2gIds);
		for (P2G p2g : p2gs) {
			if (p2g.getPerson().equals(
					personManager.getCurrentPerson(sessionAttrs))
					&& !p2g.getGroup().getCreator().equals(
							personManager.getCurrentPerson(sessionAttrs)
									.getUserLoginName())) {
				msg.append("您没有权限从当前组织中移除本人");
				break;
			}
			p2gManager.remove(p2g);
		}
		outputJson("{success:true,msg:'" + msg + "'}");
		return NONE;
	}

	/**
	 * 
	 * 方法功能描述 :增加人员到指定的组织，角色
	 * 
	 * @author 唐剑钢
	 * @return 2007-12-21 返回类型：String
	 */
	public String addPersonToTheGroupAction() throws Exception {

		String creator = (String) this.request.getSession().getAttribute(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		MD5 md5 = new MD5();
		// 取客户端传过来的参数
		String username = request.getParameter("userName");
		String userLoginName = request.getParameter("userLoginName");
		String config = request.getParameter("userConfig");
		String sex = request.getParameter("userSex");
		String groupId = request.getParameter("groupId");
		String roleId = request.getParameter("roleId");
		String date = request.getParameter("userBirthday");
		String groupIds[] = groupId.split(",");
		String roleIds[] = roleId.split(",");
		// 设置人员信息
		Person person = new Person();
		person.setUserName(username);
		// 工号
		person.setUserLoginName(userLoginName);
		// 登录名
		String userLoginName2 = personManager.converterToFirstLetterSpell(username);
		person.setUserLoginName2(userLoginName2);
		person.setBirthday((StringUtils.isNotEmpty(date)) ? DateUtils
				.parseDate(date) : null);
		person.setSex(sex);
		person.setCreateDt(new Date());
		person.setCreator(creator);
		// 设置初始化密码
		String mdPassword = md5.getMD5ofStr(SystemConfig.getProperty("DEFAULT_START_PASSWORD"));
		person.setUserPwd(mdPassword);
		person.setStatus(Person.PERSON_STATUS_NORMAL);
		person.setConfig(config);

		// 设置与组织关联
		if (StringUtils.isNotEmpty(groupId)) {
			createP2G(groupIds, person, creator, false);
		}
		// 设置与角色关联
		if (StringUtils.isNotEmpty(roleId)) {
			createP2R(roleIds, person, creator, false);
		}
		personManager.save(person);

		return NONE;
	}

	/**
	 * 创建人员和组织集合之间的关联
	 * 
	 * @author tantyou
	 * @param groupIds
	 * @param person
	 * @param adminMap
	 *            用于这个组织是否是管理员用户
	 * @param indicator
	 * @param bClearOld
	 *            是否删除原有关联
	 */
	private void createP2G(String groupIds[], Person person, String indicator,
			boolean bClearOld) throws Exception {
		HashMap adminMap = new HashMap();
		if (bClearOld) {
			Set<P2G> p2gset = person.getGroups();
			if (p2gset.size() > 0) {
				adminMap = p2gManager.deleteByAllPersonAndGroup(p2gset);
			}
		}
		for (String id : groupIds) {
			if (StringUtils.isNotEmpty(id)) {
				P2G p2g = new P2G();
				Group group = groupManager.get(id);
				p2g.setGroup(group);
				p2g.setPerson(person);
				if (adminMap.containsKey(group.getId())) {
					p2g.setAdmin(true);
				} else {
					p2g.setAdmin(false);
				}
				p2g.setIndicator(indicator);
				p2gManager.save(p2g);
			}
		}
	}

	/**
	 * 
	 * 方法功能描述 创建人员和角色集合的关联关系
	 * 
	 * @author 唐剑钢
	 * @param roleIds
	 * @param person
	 * @param indicator
	 * @param bClearOld
	 *            是否把以前的关联解除
	 * @throws Exception
	 *             2008-1-31 返回类型：void
	 */
	private void createP2R(String roleIds[], Person person, String indicator,
			boolean bClearOld) throws Exception {
		if (bClearOld) {
			Set<P2Role> p2rset = person.getRoles();
			if (p2rset.size() > 0) {
				p2roleManager.deleteByAllPersonAndRole(p2rset);
			}
		}
		for (String id : roleIds) {
			if (StringUtils.isNotEmpty(id)) {
				P2Role p2r = new P2Role();
				Role role = this.roleManager.get(id);
				p2r.setRole(role);
				p2r.setPerson(person);
				p2r.setIndicator(indicator);
				if (person.getRoles() == null) {
					person.setRoles(new HashSet<P2Role>());
				}
				person.getRoles().add(p2r);
				this.p2roleManager.save(p2r);
				this.personManager.save(person);
			}
		}
	}

	/**
	 * 
	 * 方法功能描述 :删除所有的用户
	 * 
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 *             2007-12-24 返回类型：String
	 */
	public String batchDelPersonToTheGroupAction() throws Exception {
		// this.p2gManager.evitPerson();
		String personids = request.getParameter("personids");
		String personIdx[] = personids.split(",");
		String msg = "";
		Collection<Person> persons = p2gManager.findByIds(Person.class,
				personIdx);
		for (Person person : persons) {
			if (person.equals(personManager.getCurrentPerson(sessionAttrs))) {
				msg = "您无权限删除本人";
				break;
			}
			person.setIsDel(1);
			personManager.save(person);
			// person.getGroups().clear();
			// person.getRoles().clear();
//			personManager.remove(person);
		}
		outputJson("{success:true,msg:'" + msg + "'}");
		return NONE;
	}

	/**
	 * 向组织中添加管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addAdminToGroupAction() throws Exception {
		String userLoginName = request.getParameter("userLoginName");
		String groupId = request.getParameter("groupId");
		p2gManager.addAdminToGroup(userLoginName, groupId, personManager
				.getCurrentPerson(sessionAttrs).getUserLoginName());
		outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 
	 * 方法功能描述 :批量为一个组织加入管理员(对应功能按钮：指定管理员)
	 * 
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 *             2008-2-19 返回类型：String
	 */
	public String setupAdminForGroupAction() throws Exception {
		String p2gIdss = request.getParameter("p2gids");
		String p2gIds[] = p2gIdss.split(",");
		Collection<P2G> p2gs = p2gManager.findByIds(p2gIds);
		for (P2G p2g : p2gs) {
			p2g.setAdmin(true);
			// 设定管理员默认的权限
			Collection<Resource> ress = operationManager
					.findAdminOperationList();
			for (Resource res : ress) {
				p2resManager.setupResourceDirect(p2g.getPerson(), res,
						personManager.getCurrentPerson(sessionAttrs)
								.getUserLoginName());
			}
			p2gManager.save(p2g);
		}
		// this.p2gManager.batchSetupAdminForGroup(p2gIds);
		outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 移除管理员身份
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAdminFromGroupAction() throws Exception {
		String userLoginName = request.getParameter("userLoginName");
		String groupId = request.getParameter("groupId");
		p2gManager.removeAdminFromGroup(userLoginName, groupId);
		outputJson("{success:true}");
		return NONE;
	}
	
	public String getAdminPersonAction() throws Exception {
		String groupId = request.getParameter("id");
		if (StringUtils.isNotEmpty(groupId)) {
			Collection persons = groupManager.findAdminPersons(groupId);
			String json = JSONUtil.listToJson(persons, new String[] { "id",
					"person", "userLoginName", "userName" });

			json = "{success:true,totalCount:'" + persons.size() + "',list:"
					+ json + "}";

			this.outputJson(json);
		} else {
			this.outputJson("{success:true,totalCount:'0',list[]}");
		}
		return NONE;
	}

	public String[] listJsonProperties() {
		return new String[] { "id", "person", "group", "userName", "admin",
				"userLoginName", "sex", "status","myRoles", "userLoginName2" };
	}

	public HibernateEntityDao getManager() {
		return p2gManager;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
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

	public P2GManager getP2gManager() {
		return p2gManager;
	}

	public void setP2gManager(P2GManager manager) {
		p2gManager = manager;
	}

	public P2RoleManager getP2roleManager() {
		return p2roleManager;
	}

	public void setP2roleManager(P2RoleManager manager) {
		p2roleManager = manager;
	}

	public OperationManager getOperationManager() {
		return operationManager;
	}

	public void setOperationManager(OperationManager operationManager) {
		this.operationManager = operationManager;
	}

	public P2ResManager getP2resManager() {
		return p2resManager;
	}

	public void setP2resManager(P2ResManager manager) {
		p2resManager = manager;
	}

}
