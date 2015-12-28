
package com.jteap;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.system.person.model.Person;

/**
 * 
 * @author tantyou
 *
 * 系统过滤器，用于进行类似用户是否登录的判断
 */
public class SystemFilter implements Filter {

    public void destroy() {
    }
  
    public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain)throws IOException, ServletException {
    	HttpServletRequest httpRequest=(HttpServletRequest)request;    	
    	String personId=(String) httpRequest.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
    	//根据用户是否登录进行判断
    	if(StringUtils.isBlank(personId)){
    		Person person=new Person();
			person.setId(Constants.ADMINISTRATOR_ID);
			person.setUserName(Constants.ADMINISTRATOR_NAME);
			person.setUserLoginName(Constants.ADMINISTRATOR_ACCOUNT);
			setSessionInfo(httpRequest,person);
    	}
    	//传递控制到下一个过滤器
    	chain.doFilter(request, response);	
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
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    
}