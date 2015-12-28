package com.jteap.system.person.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.manager.P2ResManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Res;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
/**
 * 处理用户信息的action
 * @author tantyou
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class PersonAction extends AbstractAction{
	//用户管理器
	private PersonManager personManager;
	private GroupManager groupManager;
	private RoleManager roleManager;
	private ResourceManager resManager;
	private P2ResManager p2resManager;
	/**
	 * 修改密码Action
	 * @return
	 * @throws Exception
	 */
	public String changePasswordAction() throws Exception{
		String userName=request.getParameter("userLoginName");
		String newPassword=request.getParameter("newPassword");
		personManager.changePassword(userName, newPassword);
		outputJson("{success:true}");
		return NONE;
	}
    
	
	/**
	 * Action
	 * 提供给Ext2.0 UniqueTextField 验证是否存在用户名称和密码的用户
	 * @return
	 * @throws Exception
	 */
	public String validateNameAndPasswordAction() throws Exception{
		
		String userName=request.getParameter("userLoginName");
		String userPwd=request.getParameter("userPassword");
		Person person=personManager.findPersonByLoginNameAndPwd(userName, userPwd);
		PrintWriter out=response.getWriter();
		if(person!=null){
			String personJson = JSONUtil.objectToJson(person, new String[]{"id","userName","userLoginName","sex","birthday","status","creator","createDt"});
			out.print("{unique:true,person:"+personJson+"}");
		}else{
			out.print("{unique:false}");
		}
		return NONE;
	}
	
	/**
	 * 验证指定的用户名称是否已经存在
	 * @return {unique:false} 已经存在
	 * 			{unique:true} 不存在
	 * @throws Exception
	 */
	public String validateUserNameUniqueAction() throws Exception{
		String userName=request.getParameter("userLoginName");
		String personId=request.getParameter("personId");
		Person person=personManager.findUniqueBy("userLoginName", userName);
		if(person!=null){
			if(StringUtils.isNotEmpty(personId) && personId.equals(person.getId().toString())){
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
	 * 
	 * 描述 : 验证指定的工号是否已经存在
	 * 作者 : wangyun
	 * 时间 : Aug 25, 2010
	 * 异常 : Exception
	 * 
	 */
	public String validateUserLoginName2Action() throws Exception {
		String userLoginName2=request.getParameter("userLoginName2");
		String personId=request.getParameter("personId");
		Person person=personManager.findUniqueBy("userLoginName2", userLoginName2);
		if(person!=null){
			if(StringUtils.isNotEmpty(personId) && personId.equals(person.getId().toString())){
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
	 * 显示游离用户，不属于任何组织的用户
	 * @return
	 * @throws Exception
	 */
	public String showDissciationPersonListAction() throws Exception{
		StringBuffer hql=getPageBaseHql();
		//添加group为null的条件
		HqlUtil.addCondition(hql,"id","select b.person.id from P2G b",null,HqlUtil.TYPE_NOT_IN);
		//添加查询条件
		beforeShowList(request,response,hql);
		String json=getPageCollectionJson(hql.toString());
		this.outputJson(json);
		return NONE;
	}
	/**
	 * 
	 * 方法功能描述 :显示修改人员的信息
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-1-26
	 * 返回类型：String
	 */
	
	public String showModifyPersonAction() throws Exception{
		String personid=request.getParameter("id");
		this.log.debug(personid);
		Object obj=getManager().get(personid);
		String json=JSONUtil.objectToJson(obj, updateJsonProperties());
		outputJson("{success:true,data:["+json+"]}"); 
		return NONE;
	}
	
	/**
	 * 
	 * 方法功能描述 :显示修改当前人员的信息
	 * @author 朱启亮
	 * @return
	 * @throws Exception
	 * 2008-1-26
	 * 返回类型：String
	 */
	@LogMethod(name="准备修改个人信息")
	public String showModifyCurrPersonAction() throws Exception{
		String personid=(String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
		this.log.debug(personid);
		Object obj=getManager().get(personid);
		String json=JSONUtil.objectToJson(obj, updateJsonProperties());
		outputJson("{success:true,data:["+json+"]}"); 
		return NONE;
	}
	/**
	 * 
	 * 方法功能描述 :显示人员详细信息
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-1-26
	 * 返回类型：String
	 */
	public String showDetailPersonAction() throws Exception {
		String personid = request.getParameter("id");
		Person person = this.personManager.get(personid);
		// 取这个用户对应的组织
		Collection<Group> groups = this.groupManager.findGroupByPerson(person);
		List<String> groupPath = new ArrayList<String>();
		for (Group group : groups) {
			groupPath.add(group.getPathWithText());
		}
		Collections.sort(groupPath);
		String gp = StringUtil.stringTojeson(groupPath);
		String group = "group:[" + gp + "]";
		// 取这个用户对应的角色
		Collection<Role> roles = this.roleManager.findRoleByPerson(person);
		List<String> rolePath = new ArrayList<String>();
		for (Role role : roles) {
			rolePath.add(role.getPathWithText());
		}
		Collections.sort(rolePath);
		// 把对应的集合转成对象形式
		String rl = StringUtil.stringTojeson(rolePath);
		String role = "role:[" + rl + "]";
		// 取得这个用户对应的资源
		Collection<Resource> resources = this.resManager.findResourceByPerson(person);
		List<String> resourcePath = new ArrayList<String>();
		for (Resource resource : resources) {
			resourcePath.add(resource.getPathWithText());
		}
		// 为使显示美观，进行排序
		Collections.sort(resourcePath);
		String res = StringUtil.stringTojeson(resourcePath);
		String resource = "resource:[" + res + "]";
		// 人员
		String preson = JSONUtil.objectToJson(person, updateJsonProperties());
		preson = "data:[" + preson + "]";

		outputJson("{success:true," + preson + "," + group + "," + role + "," + resource + "}");
		return NONE;
	}
	/**
	 * 
	 * 方法功能描述 :默认初始化用户的密码	为888888
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-1-29
	 * 返回类型：String
	 */
	@LogMethod(name="初始化密码")
	public String initPersonPassWordAction()throws Exception{
		String personIdx=request.getParameter("personids");
		String personIds[]=personIdx.split(",");
		this.personManager.initPassword(personIds);
		outputJson("{success:true}");
		return NONE;
	}
	/**
	 * 
	 * 方法功能描述 :锁定用户
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-1-31
	 * 返回类型：String
	 */
	@LogMethod(name="锁定用户")
	public String lockPersonAction()throws Exception{
		String personIdx=request.getParameter("personids");
		String personIds[]=personIdx.split(",");
		this.personManager.lockAndUnlockPersons(personIds,true);
		outputJson("{success:true}");
		return 	NONE;
	}

	/**
	 * 批量为用户指定资源
	 * @return
	 * @throws Exception
	 */
	public String setupResourceOfThePersonAction() throws Exception{
		String personIds=request.getParameter("personIds");
		String personIdx[]=personIds.split(",");
		String ress=request.getParameter("resIds");
		String resx[]=ress.split(",");
		String msg="";
		for (String personId : personIdx) {
			if(StringUtils.isEmpty(personId)) continue;
//			if(personId.equals(personManager.getCurrentPerson(sessionAttrs).getId().toString())){
//				msg+="您没有为本人赋予资源的权限，请联系您的上级管理员";
//				break;
//			}
			// 清除人员所有的指定权限
			Person person=personManager.get(personId);
			Collection<P2Res> p2ress = p2resManager.findBy("person.id", personId);
			for (P2Res p2res : p2ress) {
				p2resManager.remove(p2res);
			}
			p2resManager.flush();
			// 为人员赋予权限
			for (String resId : resx) {
				if(StringUtils.isEmpty(resId)) continue;
				Resource res=resManager.get(resId);
				p2resManager.setupResourceDirect(person, res, personManager.getCurrentPerson(sessionAttrs).getUserLoginName());
			}
		}
		outputJson("{success:true,msg:'"+msg+"'}");
		return NONE;
	}
	
	/**
	 * 清除指定人员的所有权限
	 * @return
	 * @throws Exception
	 */
	public String clearResourceOfThePersonAction() throws Exception{
		String personIds=request.getParameter("personIds");
		String personIdx[]=personIds.split(",");
		Collection<P2Res> p2ress=p2resManager.findByIds("person.id",personIdx);
		String msg="";
		for (P2Res p2res : p2ress) {
			if(p2res.getPerson().equals(personManager.getCurrentPerson(sessionAttrs))){
				msg="您不能清除本人所具有的权限";
				break;
			}
			p2resManager.remove(p2res);
		}
//		p2resManager.batchDelP2ResByPersonIds(personIdx);
		outputJson("{success:true,msg:'"+msg+"'}");
		return NONE;
	}


	/**
	 * 显示列表前的预处理（主要用于查询）
	 * @param request
	 * @param response
	 * @param hql
	 * @author 朱启亮
	 * @date 2008-2-18
	 */
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
//			String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
			String hqlWhere = request.getParameter("queryParamsSql");
			//在一类型日志中查询
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
			
			HqlUtil.addCondition(hql, "isDel",0);
			
			if(!this.isHaveSortField()){
				HqlUtil.addOrder(hql, "userLoginName", "asc");
			}
			
			
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * 
	 * 方法功能描述 :解除锁定
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-2-20
	 * 返回类型：String
	 */
	public String moveLockPersonAction()throws Exception{
		String personIdx=request.getParameter("personids");
		String personIds[]=personIdx.split(",");
		this.personManager.lockAndUnlockPersons(personIds,false);
		outputJson("{success:true}");
		return NONE;
	}
	/**
	 * 更新当前用户基本信息
	 * @return
	 * @author 朱启亮
	 * @date 2008-2-20
	 */
	@LogMethod(name="当前用户信息被更新")
	public String updateCurrPersonAction() throws Exception {
		try{
			String personid=(String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
			Person person=this.personManager.get(personid);
			//转字符编码
			String username=request.getParameter("userName");
		    String date=request.getParameter("userBirthday");
		    String config=request.getParameter("userConfig");
			String sex=request.getParameter("userSex");
			//重新设置人员的信息
		    person.setSex(sex);
		    person.setUserName(username);
		    person.setConfig(config);
		    person.setBirthday((StringUtils.isNotEmpty(date))?DateUtils.parseDate(date):null);
			//保存人员信息
			this.personManager.save(person);
			outputJson("{success:true}");
		}catch(Exception ex){
			throw new BusinessException(ex.getCause());
		}
	    return  NONE;
	}
	
	/**
	 * 
	 * 描述 : 根据所有用户的中文名，生成对应拼音结构的登录名（结构如： 张三->zhangs）
	 * 作者 : wangyun
	 * 时间 : Aug 24, 2010
	 * 异常 : Exception
	 * 
	 */
	public String hz2PyAction() throws Exception {
		try {
			personManager.hz2Py();
			outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 
	 * @return {"联系方式1":"11","联系方式2":""......
	 * @throws Exception 
	 */
	public String getPersonConfig() throws Exception{
		String personname = request.getParameter("personname");
		try {
			List<Person> personlist =personManager.findPersonByUserName(personname);
			outputJson("{success:true,config:"+personlist.get(0).getConfig()+"}");
		} catch (RuntimeException e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}



	public HibernateEntityDao getManager() {
		
		return personManager;
	}

	public String[] listJsonProperties() {
		return new String[]{"userLoginName","id","userName","sex","status","myRoles", "userLoginName2", "config"};
	}

	public String[] updateJsonProperties() {
		return new String[]{"userLoginName","id","userName","birthday","time","sex","config", "userLoginName2"};
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}


	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public ResourceManager getResManager() {
		return resManager;
	}


	public void setResManager(ResourceManager resManager) {
		this.resManager = resManager;
	}


	public RoleManager getRoleManager() {
		return roleManager;
	}


	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}


	public P2ResManager getP2resManager() {
		return p2resManager;
	}


	public void setP2resManager(P2ResManager manager) {
		p2resManager = manager;
	}

}
