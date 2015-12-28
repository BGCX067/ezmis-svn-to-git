<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.system.person.model.Person"%>
<%@ include file="/inc/import.jsp"%>

<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="java.util.List"%>
<html>
	<head>
		<!-- 将IE8使用IE7的标准模式进行渲染,避免制作出的页面在IE8下面出现错误 -->
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<!--  -->
		<title>运行管理子系统首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
		<%
			//EZMIS首页 我的快捷选中的模块Id
			String childModuleId = request.getParameter("childModuleId");
			//EZMIS首页 我的快捷选中的大模块Id
			String bigModuleId = request.getParameter("bigModuleId");
			//EZMIS首页 子系统模块Id			
			String moduleId = request.getParameter("moduleId");
			
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
			
			List<Module> moduleList = null;
			if(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString() == "1"){
				Person person = new Person();
				person.setId(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString());
				person.setUserLoginName(session.getAttribute("SESSION_CURRENT_PERSON_LOGINNAME").toString());
				moduleList = (List<Module>)moduleManager.findModuleByParentWithPerm(person,moduleId);
			}else{
				moduleList = (List<Module>)moduleManager.findModuleByParentWithPerm(personManager.get(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString()),moduleId);
			}
		%>
	</head>

	<body scroll="no">
		
		<script type="text/javascript">
			
			//初始化EZMIS首页 我的快捷
			function intiMyQuick(){
				var childModuleId = "<%=childModuleId%>";
				var bigModuleId = "<%=bigModuleId%>";
				var moduleId = "<%=moduleId%>";
				//alert('子系统大模块Id  '+bigModuleId);alert('选中模块Id  '+childModuleId);alert('子系统模块Id  '+moduleId);return;
				
				//判断页面加载是否为EZMIS首页 我的快捷
				if(bigModuleId !="null" && childModuleId != "null" && moduleId != "null" && childModuleId != moduleId){
					//如果点击的是 大模块节点
					if(childModuleId == bigModuleId){
						document.getElementById("portal").src = "module.jsp?moduleId="+bigModuleId;
					}else{
						document.getElementById("portal").src = "module.jsp?moduleId="+bigModuleId+"&childId="+childModuleId;
					}
					
					//设置导航栏样式
					var array_li = document.getElementsByTagName("li");
					array_li[oldClassId.value].className = "";
					array_li["li_"+bigModuleId].className = "current";
					
					oldClassId.value = "li_"+bigModuleId;
				}
			}
								
			//跳转到模块 (iframe路径跳转)
			function goToModule(type,modId,classId){
				var array_li = document.getElementsByTagName("li");
				
				if(type == '1'){
					document.getElementById("portal").src = "menu.jsp?moduleId=<%=moduleId%>";
					array_li[oldClassId.value].className = "";
					array_li[classId].className = "current";
				}else{
					document.getElementById("portal").src = "module.jsp?moduleId="+modId;
					array_li[oldClassId.value].className = "";
					array_li[classId].className = "current";
				}
				
				oldClassId.value = classId;
			}
			
    	</script>
		
		<input type="hidden" id="oldClassId" value="0">
		
		<table width="100%" height="100%">
			<tr>
				<td height="10%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						id="headerDiv">
						<tr>
							<td class="top-left">
								&nbsp;
							</td>
							<td class="top-center">
								&nbsp;
							</td>
							<td class="top-right">
								<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
									codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0"
									width="564" height="93">
									<param name="movie" value="images/top-right-01-yx.swf">
									<param name="quality" value="high">
									<param name=wmode value=transparent>
									<embed src="images/top-right-01-yx.swf" quality="high"
										pluginspage="http://www.macromedia.com/go/getflashplayer"
										type="application/x-shockwave-flash" width="564" height="93"></embed>
								</object>
							</td>
						</tr>
					</table>
					<div class="nav" id="navDiv">
						<ul class="glossymenu">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<%
											for (int i=0; i<moduleList.size(); i++) {
												Module module = moduleList.get(i);
												String li_id = "li_" + module.getId();
												
												if (i == 0) {
										%>
										<!-- 子系统首页导航 -->
										<li id="<%=li_id%>" class="current">
											<a href="javascript:void(0);" onclick="goToModule('1','<%=module.getId()%>','<%=li_id%>');">
												<b><%=module.getResName()%></b>
											</a>
										</li>
										<%
												} else {
										%>
										<!-- 子系统模块导航 -->
										<li id="<%=li_id%>">
											<a href="javascript:void(0);" onclick="goToModule('2','<%=module.getId()%>','<%=li_id%>');">
												<b><%=module.getResName()%></b>
											</a>
										</li>
										<%
												}
											}
										%>
									</td>
									<td width="20">
										<img src="images/ico-return.gif" width="16" height="16" />
									</td>
									<td width="95" class="mis-return">
										<a href="../index2.jsp">MIS系统首页</a>
									</td>
								</tr>
							</table>
						</ul>
					</div>
				</td>
			</tr>
			<tr>
				<td height="70%">
					<IFRAME id='portal' name='portal' scrolling='no' src="menu.jsp?moduleId=<%=moduleId%>" style='width: 100%; height: 75%' frameborder='no'></IFRAME>
				</td>
			</tr>
			<tr>
				<td height="10%" valign="bottom">
					<!-- 底部 -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						id="buttom" valign="bottom">
						<tr class="bom" id="bomDiv">
							<td class="bom-left">
								登录信息 : <%=curPersonName%>  所在部门:<%=curPersonGroupNames%>
							</td>
							<td class="bom-center">
								版权所有 湖北能源集团鄂州发电有限公司
							</td>
							<td class="bom-right">
								<a href="#" onclick="logout_Click()">注销</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>

<script type="text/javascript"> 
	window.onload = function(){
		var headerDivHeight = document.getElementById("headerDiv").clientHeight;
		var iframeDivHieght = document.getElementById("portal").clientHeight;
		var navDivHeight = document.getElementById("navDiv").clientHeight;
		var bomDivHeight = document.getElementById("bomDiv").clientHeight;
		var root_height = (window.screen.availHeight-headerDivHeight-navDivHeight-bomDivHeight-35);
		document.getElementById("portal").style.height = root_height+"px";
		
		intiMyQuick();
	}
	
	/**
	 * 注销
	 */
	function logout_Click() {
		if (window.confirm('确定要注销吗？')) {
			window.location.href = '${contextPath}/LogoutAction.do';
		}
	}
</script>
