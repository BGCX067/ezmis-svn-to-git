
<%@ page import="java.util.*" %>
<%@page import="com.jteap.core.Constants"%>
<%@page import="com.jteap.core.support.SystemConfig"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %> 
<%@ taglib uri="/WEB-INF/jteap.tld" prefix="jteap"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<c:set var="contextPath" scope="page" value="${pageContext.request.contextPath}"/>
<%  
	response.setCharacterEncoding("UTF-8");
	request.setCharacterEncoding("UTF-8");
	String curPersonId=(String)session.getAttribute(Constants.SESSION_CURRENT_PERSONID);
	String curPersonName=(String)session.getAttribute(Constants.SESSION_CURRENT_PERSON_NAME);
	String curPersonLoginName=(String)session.getAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
	String curPersonRoles = (String)session.getAttribute("SESSION_CURRENT_PERSONROLES");
	String curPersonGroupNames = (String)session.getAttribute(Constants.SESSION_CURRENT_GROUP_NAME);
	String contextPath=request.getContextPath();
	boolean isRoot=(curPersonId!=null && curPersonId.equals(Constants.ADMINISTRATOR_ID))?true:false;
	String sTheme = SystemConfig.getProperty("JTEAP_SYSTEM_THEME","CLASSICS");
	
%>

 
<script>
	
	//当前用户信息 
	var curPersonId="<%=curPersonId %>";
	var curPersonName="<%=curPersonName%>"; 
	var curPersonLoginName="<%=curPersonLoginName%>";
	var curPersonRoles = "<%=curPersonRoles%>";
	var curPersonGroupNames = "<%=curPersonGroupNames%>";
	//分页信息
	var CONSTANTS_PAGE_DEFAULT_LIMIT=<%=SystemConfig.getProperty("PAGE_DEFAULT_LIMIT")%>;
	//环境路径
	var CONTEXT_PATH='${contextPath}';
	//是否管理员登录
	var isRoot=<%=isRoot%>;
	//系统名称
	var JTEAP_SYSTEM_NAME = '<%=SystemConfig.getProperty("JTEAP_SYSTEM_NAME","JTEAP开发平台")%>';
	//JTEAP主题
	var JTEAP_SYSTEM_THEME = "<%=sTheme%>";
	
	
</script>