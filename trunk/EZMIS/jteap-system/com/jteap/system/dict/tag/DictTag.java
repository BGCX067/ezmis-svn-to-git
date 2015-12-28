package com.jteap.system.dict.tag;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.utils.JSONPropertiesFilter;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

/**
 * 显示当前模块的，具有权限的操作
 * 
 * @author tantyou
 * @date 2008-2-15
 */
@SuppressWarnings({ "unused", "serial","unchecked" })
public class DictTag extends BodyTagSupport{
	
	private PageContext pageContext;		//jsp页面环境变量
	private HttpServletRequest request;		//web请求对象
	private HttpServletResponse response;	//web输出对象
	private String catalog;
	
	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

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
		DictManager dictManager = (DictManager) SpringContextUtil.getBean("dictManager");
		StringBuffer dictSB = new StringBuffer("var jteap_dict = {");
		try{
			if(StringUtils.isNotEmpty(catalog)){
				String cats[] = StringUtil.split(catalog, ",");
				for (String cat : cats) {
					Collection<Dict> dicts = dictManager.findDictByUniqueCatalogName(cat);
					String json = JSONUtil.listToJson(dicts,new String[]{"id","key","value"});
					dictSB.append("\""+cat+"\":"+json+",");
				}
				String sResult = dictSB.toString();
				if(sResult.endsWith(",")){
					sResult = StringUtil.delEndString(sResult, ",");
				}
				sResult+="}";
				pageContext.getOut().print("<script>\r\n"+sResult+";\r\n</script>");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return super.doEndTag();
	}
	

	/**
	 * 将Map中的对象序列化成JSON字符串
	 * @param map
	 * @return
	 */
	public String mapToJson(Map map){
		JsonConfig config = new JsonConfig();
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.setJsonPropertyFilter(new JSONPropertiesFilter(new String[]{"id","key"}));
		JSONObject obj=JSONObject.fromObject(map,config);
		return obj.toString();
	}

}
