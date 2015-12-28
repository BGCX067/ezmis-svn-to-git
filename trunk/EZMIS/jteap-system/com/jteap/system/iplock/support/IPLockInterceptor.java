package com.jteap.system.iplock.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.utils.StringUtil;
import com.jteap.system.iplock.model.IPLock;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 用于拦截所有Action的拦截器
 * 查看是否有权限运行希望运行的动作
 * 
 * @author tantyou
 * @date 2008-2-20
 */
@SuppressWarnings("serial")
public class IPLockInterceptor extends AbstractInterceptor {

	
	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		HttpServletRequest request=(HttpServletRequest) ai.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
		HttpServletResponse response=(HttpServletResponse) ai.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
		
		if(IpLockSupport.ipLocks == null)
			IpLockSupport.initIpLockList();
		
		boolean bAllow = ipAdaptor(request);
		if(!bAllow){
			response.sendRedirect(request.getContextPath()+"/dead.jsp");
			return null;
		}
				
		String result = ai.invoke();
		return result;
	}

	
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
	
	/**
	 * 判断当前请求是否被IP封锁了
	 * @param request
	 * @return true 表明允许访问
	 *         false 表明被IP封锁
	 */
	private boolean ipAdaptor(HttpServletRequest request){
		String ip = getIpAddr(request);
		if(ip.equals("0:0:0:0:0:0:0:1"))
			return true;
		for (IPLock ipLock :IpLockSupport.ipLocks) {
			boolean isIn = StringUtil.isIpBetween(ipLock.getStartIp(), ipLock.getEndIp(), ip);
			if(isIn){
				if(ipLock.getRule().equals(IPLock.IP_RULE_DIE)){
					return false;
				}
			}
		}
		return true;
	}
}
