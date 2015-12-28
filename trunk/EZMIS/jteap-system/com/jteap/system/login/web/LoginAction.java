package com.jteap.system.login.web;

import javax.servlet.http.HttpSession;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.LogMethod;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.manager.RootManager;
import com.jteap.system.person.model.Person;

/**
 * 负责登录及注销的动作
 * @author tantyou
 *
 */
@SuppressWarnings("serial")
public class LoginAction extends AbstractAction{
	
    private String userLoginName;			//用户名
    private String username;
    private String userPwd;					//用户密码
    private PersonManager personManager;	//当前管理对象
    private RootManager rootManager;		//平台管理员管理对象
    
    
	/**
	 * 登录
	 */
    @LogMethod(name="登录系统")
	public String execute() throws Exception {
		this.clearErrorsAndMessages();
		//如果是平台管理员登录，验证方式不一样
		Person person;
		if(username.equals(Constants.ADMINISTRATOR_ACCOUNT)){
			person=rootManager.validate(userPwd);
		}else{
			person=personManager.findPersonByLoginNameAndPwd(username, userPwd);
		}

    	if(person==null){
    		this.addActionMessage("*用户名或密码错误");
    		return INPUT;
    	}else{
    		if(person.getStatus()!=null && person.getStatus().equals(Person.PERSON_STATUS_LOCKED)){
    			this.addActionMessage("*当前用户已被锁定，如有任何问题请联系管理人员");
        		return INPUT;
    		}
    		this.setSessionInfo(person);
    		return SUCCESS;
    	} 
    }

	
	/**
	 * 登出动作
	 * @return
	 * @throws Exception
	 */
    @LogMethod(name="注销",logpos=LogMethod.LOGPOS.BEFORE)
	public String logout() throws Exception{
		setSessionInfo(null);
		return SUCCESS;
	}
	
	/**
	 * 设置用户的会话信息
	 * @param personId
	 * @param personLoginName
	 * @param personName
	 */
	private void setSessionInfo(Person person){
		HttpSession session=request.getSession();
		String personLoginName=null;
		String personName=null;
		String personId=null;
		String personRoles = null;
		String personGroups = null;
		if(person!=null){
			personLoginName=person.getUserLoginName();
			personName=person.getUserName();
			personId=person.getId().toString();
			personRoles = person.getMyRoles();
			personGroups = person.getMyGroupNames();
		}
		session.setAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME, personLoginName);
		session.setAttribute(Constants.SESSION_CURRENT_PERSON_NAME, personName);
		session.setAttribute(Constants.SESSION_CURRENT_PERSONID, personId);
		session.setAttribute("SESSION_CURRENT_PERSONROLES", personRoles);
		session.setAttribute("SESSION_CURRENT_GROUP_NAME", personGroups);
	}

	/**
	 * 设定当前管理对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return personManager;
	}

//geter and setter method======================= start
	
	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public RootManager getRootManager() {
		return rootManager;
	}

	public void setRootManager(RootManager rootManager) {
		this.rootManager = rootManager;
	}
//	geter and setter method======================= end	

	@Override
	public String[] listJsonProperties() {
		
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		
		return null;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	
}
