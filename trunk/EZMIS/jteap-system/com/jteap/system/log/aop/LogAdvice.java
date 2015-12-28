package com.jteap.system.log.aop;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.jteap.core.Constants;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.LogMethod.LOGLEVEL;
import com.jteap.core.support.LogMethod.LOGPOS;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UserSession;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.log.manager.LogManager;
import com.jteap.system.log.model.Log;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
/**
 * 負責記錄日志
 * @author 朱启亮
 * @date 2008-1-30
 */
@SuppressWarnings("unchecked")
public class LogAdvice{
	//人员管理器
	private PersonManager personManager;
	//日志管理器
	private LogManager logManager;
	
	
	/**
	 * apo:around环绕方式记录日志
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	public Object saveLoginAndLogic(ProceedingJoinPoint jp) throws Throwable{
		
		LogMethod logMethod = LogMethodText(jp);
	
		Object ret = null;
		try {
			if (logMethod != null
					&& (logMethod.loglevel().equals(LOGLEVEL.ALL) || logMethod
							.loglevel().equals(LOGLEVEL.OPERATION))
					&& (logMethod.logpos().equals(LOGPOS.BEFORE) || logMethod.logpos().equals(LOGPOS.ALL))) {
				doLog(logMethod,jp);
			}
			
			ret = jp.proceed();
			
			if (logMethod != null
					&& (logMethod.loglevel().equals(LOGLEVEL.ALL) || logMethod
							.loglevel().equals(LOGLEVEL.OPERATION))
					&& (logMethod.logpos().equals(LOGPOS.AFTER) || logMethod.logpos().equals(LOGPOS.ALL))) {
				doLog(logMethod,jp);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
			
		}
		return ret;
	}
	
	/**
	 * 创建并保存日志对象
	 * @param logMethod
	 * @param jp
	 */
	private void doLog(LogMethod logMethod,ProceedingJoinPoint jp){
		String logText = logMethod.name();
		String classStr = jp.getTarget().getClass().getName();
		
		Object obj = jp.getThis();
		if(obj instanceof AbstractAction){
			AbstractAction action = (AbstractAction) obj;
			HttpServletRequest request = action.getRequest();
			logText += "\r\n请求:"+request.getRequestURI()+"\r\n";
			Enumeration params = request.getParameterNames();
			while(params.hasMoreElements()){
				String param = (String) params.nextElement();
				String value = request.getParameter(param);
				logText+=("参数->"+param+":"+value+"\r\n");
			}
		}
		saveLog(jp,logText,classStr);
		
	}
//
//	/**
//	 * 记录正常状态下的日志（包括登录，逻辑）
//	 * @param jp
//	 * @return
//	 * @throws Exception
//	 * @author 朱启亮
//	 * @date 2008-1-22
//	 */
//	public void saveLoginAndLogic(JoinPoint jp) throws Exception{
//		LogMethod logMethod = LogMethodText(jp);
//		if (logMethod != null
//				&& (logMethod.loglevel().equals(LOGLEVEL.ALL) || logMethod
//						.loglevel().equals(LOGLEVEL.OPERATION))) {
//			String logText = logMethod.name();
//			String classStr = jp.getTarget().getClass().getName();
//			
//			Object obj = jp.getThis();
//			if(obj instanceof AbstractAction){
//				AbstractAction action = (AbstractAction) obj;
//				HttpServletRequest request = action.getRequest();
//				logText += "\r\n请求:"+request.getRequestURI()+"\r\n";
//				Enumeration params = request.getParameterNames();
//				while(params.hasMoreElements()){
//					String param = (String) params.nextElement();
//					String value = request.getParameter(param);
//					logText+=("参数->"+param+":"+value+"\r\n");
//				}
//			}
//			saveLog(jp,logText,classStr);
//		}
//	}
	/**
	 * 发生异常的时候，记录异常日志【记录范围：已经注册过的动作中的BusinessException】
	 * @param jp
	 * @param ex
	 * @throws Exception
	 * @author 朱启亮
	 * @date 2008-1-24
	 */
	public void saveException(JoinPoint jp,Exception ex) throws Exception{
		if(ex instanceof BusinessException){
			LogMethod logMethod = LogMethodText(jp);
			if (logMethod != null
					&& (logMethod.loglevel().equals(LOGLEVEL.ALL) || logMethod
							.loglevel().equals(LOGLEVEL.EXCEPTION))) {
				String logText = logMethod.name();
				String exceptionStr = exceptionToString(ex);
				String classStr = jp.getTarget().getClass().getName();
				String methodStr = classStr + "." + jp.getSignature().getName();
				logText += "时发生异常(" + methodStr + ")  具体信息:" + exceptionStr;
				saveLog(jp,logText, classStr, ex);
			}
		}
	}
	/**
	 * 将Exception中的信息以StringWriter返回
	 * @param ex
	 * @return
	 * @author 朱启亮
	 * @date 2008-1-31
	 */
	public String exceptionToString(Exception ex){
        StringWriter sw = new StringWriter();   
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.getBuffer().toString();
	}
	
	/**
	 * 判断JoinPoint的方法是否注册过，注册过就返回注册的字符串，否则返回NULL
	 * @param jp
	 * @return
	 * @author 朱启亮
	 * @date 2008-1-31
	 */
	public LogMethod LogMethodText(JoinPoint jp){
		LogMethod tag = null;
		Class logObjectClazz = jp.getTarget().getClass();
		try {
			Method method = logObjectClazz.getMethod(jp.getSignature()
					.getName());
			tag = method.getAnnotation(LogMethod.class);
		} catch (Exception e) {
			
		} 
		return tag;
	}
	
	/**
	 * 记录一条日志【登陆，逻辑，异常】
	 * @param logText
	 * @param classStr
	 * @param exceptions
	 * @author 朱启亮
	 * @date 2008-1-31
	 */
	public void saveLog(JoinPoint jp,String logText,String classStr,Exception...exceptions){
		Object obj = jp.getThis();
		String ip = "unknow";
		if(obj instanceof AbstractAction){
			AbstractAction action = (AbstractAction) obj;
			HttpServletRequest request = action.getRequest();
			ip = this.getIpAddr(request);
		}
		
		HttpSession session = (HttpSession)UserSession.get(Constants.THREADLOCAL_CURRENT_SESSION);
		Serializable personid = (Serializable)session.getAttribute(Constants.SESSION_CURRENT_PERSONID);
		Person person = null;
		//区分游客，root用户，普通用户
		if(personid==null){
			person = new Person();
			person.setUserName(Constants.GUEST_NAME);
			person.setId(Constants.GUEST_ID);
			person.setUserLoginName(Constants.GUEST_NAME);
		}else if(personid.equals(Constants.ADMINISTRATOR_ID)){
			person = new Person();
			person.setUserName(Constants.ADMINISTRATOR_NAME);
			person.setId(Constants.ADMINISTRATOR_ID);
			person.setUserLoginName(Constants.ADMINISTRATOR_ACCOUNT);
		}else{
			PersonManager personManager = this.getPersonManager();
			person = personManager.get(personid);
		}
		StringUtil.isoToUTF8(logText);
		Log log = new Log();
		log.setIp(ip);
		log.setLog(logText);
		log.setDt(new Date());
		//记录日志的类型
		if(exceptions.length==0){
//			if(classStr.equals(Constants.LOGINACTION)){
//				log.setType(Log.LOG_TYPE_LOGIN);
//			}else{
				log.setType(Log.LOG_TYPE_LOGIC);
//			}
		}else{
			log.setType(Log.LOG_TYPE_EXCEPTION);
		}
		log.setPersonName(person.getUserName());
		if(person.getUserName().equals(Constants.GUEST_NAME) || person.getUserName().equals(Constants.ADMINISTRATOR_NAME)){
			log.setPersonLoginName(person.getUserLoginName());
		}else{
			log.setPersonLoginName(person.getUserLoginName());
		}
		logManager.save(log);
	}
	/***************************get××× and set××× method开始***************************/
	public PersonManager getPersonManager() {
		return personManager;
	}
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	public LogManager getLogManager() {
		return logManager;
	}
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}
	/***************************get××× and set××× method结束***************************/
	
	/**
	 * 获取客户端真实ip
	 */
	private String getIpAddr(HttpServletRequest request) {
       String ip = request.getHeader("x-forwarded-for");
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
       }
       return ip;
   }
}
