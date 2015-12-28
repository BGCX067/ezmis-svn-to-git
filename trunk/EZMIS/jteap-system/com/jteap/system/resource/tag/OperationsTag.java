package com.jteap.system.resource.tag;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.utils.CollectionUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.ModuleManager;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.resource.model.Module;

/**
 * 显示当前模块的，具有权限的操作
 * 
 * @author tantyou
 * @date 2008-2-15
 */
@SuppressWarnings({ "unused", "serial","unchecked" })
public class OperationsTag extends BodyTagSupport{
	
	private PageContext pageContext;		//jsp页面环境变量
	private HttpServletRequest request;		//web请求对象
	private HttpServletResponse response;	//web输出对象
	
	/**
	 * 初始化变量
	 */
	@Override
	public void setPageContext(PageContext pageContext) {
		this.pageContext=pageContext;
		this.request=(HttpServletRequest) pageContext.getRequest();
		this.response=(HttpServletResponse) pageContext.getResponse();
		super.setPageContext(pageContext);
	}

	/**
	 * 标签结束的时候,取出具有权限的操作，并以json对象的方式输出到页面
	 */

	@Override
	public int doEndTag() throws JspException {
		String moduleId=request.getParameter("moduleId");
		//如果没有指定模块编号，则不做任何操作
		if(StringUtils.isEmpty(moduleId))
			return super.doEndTag();
		try {
			ModuleManager moduleManager=(ModuleManager) SpringContextUtil.getBean("moduleManager");
			ResourceManager resManager=(ResourceManager) SpringContextUtil.getBean("resManager");
			
			//当前模块
			Module module=moduleManager.get(moduleId);
			//取得当前模块所拥有的所有操作
			Collection ops=moduleManager.findAllOperationOfTheModule(module);
			Collection permOps;
			
			//当前登录用户
			Person currentPerson=getCurrentPerson();
			
			//判断是否为root用户,如果为root用户，则显示所有的可见资源，否则只取当前用户所拥有权限的资源
			if(currentPerson.getId().equals(Constants.ADMINISTRATOR_ID)){
				permOps=resManager.findAllVisibledResource();
			}else{
				permOps=resManager.findResourceByPerson(currentPerson);
			}
			//当前用户具有权限的操作=当前用户所拥有的所有资源 和 当前模块所拥有的操作 的交集
			Collection currentOps=CollectionUtils.intersection(permOps, ops);
			//根据排序字段进行排序
			if(currentOps instanceof List){
				CollectionUtils.sort((List) currentOps, "sortNo");
			}
			//输出的json字符串
			String json;
			//看看是否存在操作，如果不存在，则直接返回一个空的JS数组
			if(currentOps.size()>0){
				json=JSONUtil.listToJson(currentOps,new String[]{"id","showText","resName","type","sn","icon","tip"});
			}else{
				json="[]";
			}
			pageContext.getOut().println("<script>\r\nvar ops="+json+";\r\n</script>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.doEndTag();
	}
	

	/**
	 * 取得当前登录用户对象
	 * @return
	 */
	protected Person getCurrentPerson(){
		String currentPersonId=(String) request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
		Person person;
		if(currentPersonId.equals(Constants.ADMINISTRATOR_ID)){
			person=new Person();
			person.setId(Constants.ADMINISTRATOR_ID);
			person.setUserName(Constants.ADMINISTRATOR_NAME);
			person.setUserLoginName(Constants.ADMINISTRATOR_ACCOUNT);
		}else{
			PersonManager personManager=(PersonManager) SpringContextUtil.getBean("personManager");
			person=personManager.get(currentPersonId);
		}
		return person;
	}

}
