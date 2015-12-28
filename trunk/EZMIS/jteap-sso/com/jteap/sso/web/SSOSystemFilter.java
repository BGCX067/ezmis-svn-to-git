package com.jteap.sso.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jteap.core.Constants;
import com.jteap.core.utils.StringUtil;
import com.jteap.sso.server.SSOContainer;
import com.jteap.system.person.model.Person;
/**
 *描述：
 *  单点登录监听器
 *时间：2010-2-6
 *作者：谭畅
 *
 */
public class SSOSystemFilter implements Filter {
	private static SSOContainer sc = SSOContainer.getInstance();


	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) req;
		String tokenId = request.getParameter("tokenId");
		if(StringUtil.isNotEmpty(tokenId)){
			setUserInfoByToken(tokenId,request);
		}
		chain.doFilter(req, resp);
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
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
	

	private void setUserInfoByToken(String tokenId,HttpServletRequest request){
		String sessionId = sc.getSessionIdByToken(tokenId);
		if(StringUtil.isNotEmpty(sessionId)){
			Person person = sc.getPersonBySession(sessionId);
			setSessionInfo(request, person);
		}
	}
}
