package com.jteap.sso.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.sso.server.SSOContainer;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.manager.RootManager;
import com.jteap.system.person.model.Person;
/**
 * 
 *描述：
 *	单点登录动作
 *时间：2010-2-6
 *作者：谭畅
 *
 */
public class SSOLoginAction extends AbstractAction {
	private static Log log = LogFactory.getLog(SSOLoginAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersonManager personManager;
	private RootManager rootManager;
	
	public RootManager getRootManager() {
		return rootManager;
	}

	public void setRootManager(RootManager rootManager) {
		this.rootManager = rootManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}
	
	/**
	 *描述：
	 *	单点登录Action
	 *时间：2010-2-6
	 *作者：谭畅
	 *参数：
	 *	@param lgn 用户登录名
	 *  @param lgp 用户密码
	 *返回值:
	 *  登录成功：{success:1,tokenId:'令牌号'}
	 *  登录失败：{success:0,msg:'单点登录失败'}
	 *抛出异常：
	 */
	@LogMethod(name="单点登录")
	public String ssoLoginAction() throws Exception{
		SSOContainer sc = SSOContainer.getInstance();
		String loginName = request.getParameter("lgn");
		String loginPwd = request.getParameter("lgp");
		String sessionId = request.getSession().getId();
		if(StringUtil.isNotEmpty(loginName) && StringUtil.isNotEmpty(loginPwd)){
			Person person;
			if(loginName.equals(Constants.ADMINISTRATOR_ACCOUNT)){
				person=rootManager.validateMd5(loginPwd);
			}else{
				person = personManager.findPersonByLoginNameAndPwdMd5(loginName, loginPwd);
			}
			//单点登录
			if(person!=null){
				String tokenId = UUIDGenerator.hibernateUUID();
				sc.putTokenId(tokenId, sessionId);
				sc.putUserInfo(sessionId, person);
				setSessionInfo(request,person); 
				log.info("用户【"+person.getUserName()+"】单点登录成功，获得TOKENID为："+tokenId+",SESSIONID:"+sessionId);
				this.outputJson("{\"success\":\"1\",\"tokenId\":\""+tokenId+"\"}");
			}else{//单点登录失败
				log.info("尝试使用"+loginName+"单点登录失败");
				this.outputJson("{\"success\":\"0\",\"msg\":\"尝试使用"+loginName+"单点登录失败\"}");
			}
		}
		return NONE;
	}

	/**
	 * 
	 *描述：
	 *	单点登录注销
	 *时间：2010-2-6
	 *作者：谭畅
	 *参数：
	 *	@param tokenId 令牌编号
	 *返回值:
	 *	注销成功 {success:1}
	 *  注销失败 {success:0,msg:'失败原因'}
	 *抛出异常：
	 */
	public String ssoLogoutAction() throws Exception{
		SSOContainer sc = SSOContainer.getInstance();
		String tokenId = request.getParameter("tk");
		if(StringUtil.isNotEmpty(tokenId)){
			sc.clearSessionByToken(tokenId);
			this.outputJson("{\"success\":\"1\",\"msg\":\"TOKENID:"+tokenId+"注销成功\"}");
		}else{
			this.outputJson("{\"success\":\"0\",\"msg\":\"tokenId无效\"}");	
		}
		
		return NONE;
		
	}

	/**
	 * 
	 * 描述 : MIS小助手获取版本信息
	 * 作者 : wangyun
	 * 时间 : Aug 31, 2010
	 * 异常 : Exception
	 * 
	 */
	public String getExeVersionAction() throws Exception {
		try {
			String path = request.getSession().getServletContext().getRealPath(SystemConfig.getProperty("VERSION_INFO"));
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path))); 
			
			String versionLine = br.readLine();
			String isForceUpdateLine = br.readLine();
			String version = "";
			String isForceUpdate = "";
			if (StringUtil.isNotEmpty(versionLine)) {
				String[] strs = versionLine.split("=");
				version = strs[1];
			}
			if (StringUtil.isNotEmpty(isForceUpdateLine)) {
				String[] strs = isForceUpdateLine.split("=");
				isForceUpdate = strs[1];
			}
			outputJson("{\"success\":\"1\",\"version\":\""+version+"\",\"isForceUpdate\":\""+isForceUpdate+"\"}");
		} catch (Exception e) {
			outputJson("{\"success\":\"0\",\"msg\":\"数据库异常，请联系管理员\"}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : MIS小助手获得下载地址
	 * 作者 : wangyun
	 * 时间 : Aug 31, 2010
	 * 异常 : Exception
	 * 
	 */
	public String getDownLoadUrl() throws Exception {
		try {
			String downloadUrl = SystemConfig.getProperty("MIS_DOWNLOAD_URL");
			if (StringUtil.isNotEmpty(downloadUrl)) {
				outputJson("{\"success\":\"1\",\"downloadUrl\":\""+downloadUrl+"\"}");
			}
		} catch (Exception e) {
			outputJson("{\"success\":\"0\",\"msg\":\"数据库异常，请联系管理员\"}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 设置用户的会话信息
	 * @param personId
	 * @param personLoginName
	 * @param personName
	 */
	private void setSessionInfo(HttpServletRequest request,Person person){
		HttpSession session=request.getSession();
		String personLoginName=null;
		String personName=null;
		String personId=null;
		if(person!=null){
			personLoginName=person.getUserLoginName();
			personName=person.getUserName();
			personId=person.getId().toString();
		}
		session.setAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME, personLoginName);
		session.setAttribute(Constants.SESSION_CURRENT_PERSON_NAME, personName);
		session.setAttribute(Constants.SESSION_CURRENT_PERSONID, personId);
	}

}
