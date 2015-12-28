<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.doclib.manager.DoclibCatalogManager"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/inc/import.jsp"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<title>JTEAP-CMS内容发布平台</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
		<%
			String moduleId = (String) request.getParameter("moduleId");
			String catalogCode = (String)request.getParameter("catalogCode");
			String flag = (String)request.getParameter("flag");
			DoclibCatalogManager catalogManager = (DoclibCatalogManager) SpringContextUtil.getBean("doclibCatalogManager");
		%>
	</head>

	<body scroll="no">
		<input type="hidden" id="moduleId" value="<%=moduleId%>" />
		<!-- 加载等待图标 开始 -->
		<!-- 加载等待图标 结束 -->
		<!-- 加载脚本库  开始  -->
		<%@ include file="/inc/ext-all.jsp"%>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		<script type="text/javascript" src="FunctionPanel.js" charset="UTF-8"></script>
		<script type="text/javascript"
			src="${contextPath}/script/ext-extend/form/SelectBox.js"
			charset="UTF-8"></script>
		<script type="text/javascript"
			src="${contextPath}/script/ext-extend/SearchField.js"></script>
		<script type="text/javascript"
			src="${contextPath}/script/ext-extend/tab/TabCloseMenu.js"></script>
		<script type="text/javascript"
			src="${contextPath}/script/ext-extend/form/UniqueTextField.js"
			charset="UTF-8"></script>
		<script type="text/javascript"
			src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
		<script type="text/javascript"
			src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
		<script type="text/javascript" src="EditCurrPresonForm.js"
			charset="UTF-8"></script>
		<script type="text/javascript" src="ChangePasswordForm.js"
			charset="UTF-8"></script>
		<script type="text/javascript" src="MainMenu.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/jteap/index.js" charset="UTF-8"></script>
		<!-- 程序入口 -->
		<script type="text/javascript">
			var catalogCode = "<%=catalogCode%>";
			var moduleId = "<%=moduleId%>";
			var flag = "<%=flag%>";
			function intiMyQuick(){
				//判断页面加载是否为EZMIS首页 我的快捷
				if(flag=='1'){
					document.getElementById("portal").src = "module.jsp?moduleId="+moduleId+"&catalogCode="+catalogCode;
                 	//设置导航栏样式                                                                                                                  
					var array_li = document.getElementsByTagName("li");
					array_li[oldClassId.value].className = "";
					array_li[catalogCode].className = "current";
					oldClassId.value = catalogCode;
				}
			}
			
			function goToModule(type,modId,catalogCode){
				var array_li = document.getElementsByTagName("li");
				if(type == '1'){
					document.getElementById("portal").src = "menu.jsp?moduleId=<%=moduleId%>";
					array_li[oldClassId.value].className = "";
					array_li[catalogCode].className = "current";
				}else{
					document.getElementById("portal").src = "module.jsp?moduleId="+moduleId+"&catalogCode="+catalogCode;
					array_li[oldClassId.value].className = "";
					array_li[catalogCode].className = "current";
				}
				oldClassId.value = catalogCode;
			}
			
			function addDiv(){
				var html='<table width="100%" border="0" cellspacing="0" cellpadding="0" class="box1-tab" style="height:83%;"><tr><td class="box1-top-left">&nbsp;</td><td class="box1-top-center">&nbsp;</td><td class="box1-top-right">&nbsp;</td></tr><tr><td class="box1-left">&nbsp;</td><td class="box1-main"><div class="sub-title"><table width="100%" border="0" cellspacing="0" cellpadding="0" valign="top"><tr><td class="sub-left-ico"><img src="images/ico-sub-title-01.gif" width="16" height="16" /></td><td class="sub-title-txt" id="moduleResName"></td><td class="sub-right-ico"></td></tr></table></div><div id="rightModule" style="height:300px"><IFRAME id="portal" name="portal" scrolling="no" src="menu.jsp?moduleId=<%=moduleId%>" style="width: 100%; height: 75%" frameborder="no"></IFRAME></div></td><td class="box1-right">&nbsp;</td></tr><tr><td class="box1-bom-left">&nbsp;</td><td class="box1-bom-center">&nbsp;</td><td class="box1-bom-right">&nbsp;</td></tr></table>';
				$("tdportal").innerHTML=html;
			}
			
			function removeDiv(){
				$("tdportal").innerHTML='<IFRAME id="portal" name="portal" scrolling="no" src="menu.jsp?moduleId=<%=moduleId%>" style="width: 100%; height: 75%" frameborder="no"></IFRAME>';
			}
			
			function getQueryString(link,name){
				// 如果链接没有参数，或者链接中不存在我们要获取的参数，直接返回空 
				if(link.indexOf("?")==-1 || link.indexOf(name+'=')==-1) { 
					return '';
				} 
				// 获取链接中参数部分 
				var queryString = link.substring(link.indexOf("?")+1); 
				// 分离参数对 ?key=value&key2=value2 
				var parameters = queryString.split("&"); 
				var pos, paraName, paraValue; 
				for(var i=0; i<parameters.length; i++){ 
					// 获取等号位置 
					pos = parameters[i].indexOf('='); 
					if(pos == -1) { continue; } 
				 	// 获取name 和 value 29.        
				 	paraName = parameters[i].substring(0, pos); 
					paraValue = parameters[i].substring(pos + 1); 
					// 如果查询的name等于当前name，就返回当前值，同时，将链接中的+号还原成空格
					if(paraName == name){
						return unescape(paraValue.replace(/\+/g, " ")); 
					}
				}
				return '';
			  }
    	</script>
    	
		<!-- 加载脚本库  结束 -->
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
									<param name="movie" value="images/top-right-01-aj.swf">
									<param name="quality" value="high">
									<param name=wmode value=transparent>
									<embed src="images/top-right-01-aj.swf" quality="high"
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
											ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
											Collection<Module> roots = moduleManager.findModule(moduleId);
											Object[] obj = roots.toArray();
											for (int i = 0; i < obj.length; i++) {
												Module module = (Module) obj[i];
												Collection<Module> modules = moduleManager.findModule(module
														.getId().toString());
												String catalogCodeid="0";
												if(i>0){
													catalogCodeid = module.getLink().split("catalogCode=")[1];	
												}
												if (i == 0) {
										%>
										<li class="current" id="<%=catalogCodeid%>">
											<a  onclick="goToModule('1','<%=module.getId()%>','<%=catalogCodeid%>','<%=module.getLink()%>');"><b><%=module.getResName()%></b>
											</a>
										</li>
										<%
											} else {
										%>
										<li id="<%=catalogCodeid%>">
											<a onclick="goToModule('2','<%=module.getId()%>','<%=catalogCodeid%>','<%=module.getLink()%>');"><b><%=module.getResName()%></b>
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
				<td height="70%" id="td1">
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
								登录信息:<%=curPersonName%>  所在部门:<%=curPersonGroupNames%>
							</td>
							<td class="bom-center">
								版权所有 湖北能源集团鄂州发电有限公司
							</td>
							<td class="bom-right">
								<a href="#" onclick='logout_Click();'>注销</a>
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
		//document.getElementById("portal2").style.height = (root_height-48)+"px";
	}
</script>
