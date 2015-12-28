<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="/inc/meta.jsp" %>
		<title>系统管理首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
		<%
			//EZMIS首页 我的快捷选中的模块Id
			String childModuleId = request.getParameter("childModuleId");
			//EZMIS首页 我的快捷选中的大模块Id
			String bigModuleId = request.getParameter("bigModuleId");
			
			String moduleId = (String) request.getParameter("moduleId");
			
			ModuleManager moduleManager = (ModuleManager)SpringContextUtil.getBean("moduleManager");
			
			Module childModule = null;
			if(childModuleId != null){
				childModule = moduleManager.get(childModuleId);
			}
			
		%>
	</head>

	<body scroll="no">

		<script type="text/javascript">
		
			function showTree(modId,link,resName){
				var moduleId = "<%=moduleId%>";
				
				//如果点击的是系统管理节点
				if(moduleId == modId){
					 modId = document.getElementById("firstModuleId").value;
					 link = document.getElementById("firstModuleLink").value;
					 resName = firstModuleResName = document.getElementById("firstModuleResName").value;
				}
				
				if(link.indexOf("?") == -1){
		    		document.getElementById("portal").src = contextPath + '/jteap/'+link+"?moduleId="+modId;
	    		}else{
	    			document.getElementById("portal").src = contextPath + '/jteap/'+link+"&moduleId="+modId;
	    		}
				document.getElementById("portal").document.getElementById("moduleResName").innerText = resName;
				
				var array_li = document.getElementsByTagName("li");
				array_li[oldClassId.value].className = "";
				array_li["li_"+modId].className = "current";
				
				oldClassId.value = "li_"+modId;
			}
			
			function goToModule(type,modId,classId,link,resName){
				var array_li = document.getElementsByTagName("li");
				
				if(type == '1'){
					showTree(modId,link,resName);
					
					array_li[oldClassId.value].className = "";
					array_li[classId].className = "current";
				}else{
					showTree(modId,link,resName);
					
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
									<param name="movie" value="images/top-right-01-workflow.swf">
									<param name="quality" value="high">
									<param name=wmode value=transparent>
									<embed src="images/top-right-01-workflow.swf" quality="high"
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
											Collection<Module> roots = moduleManager.findModule(moduleId);
											Object[] obj = roots.toArray();
											for (int i = 0; i < obj.length; i++) {
												Module module = (Module) obj[i];
												
												Collection<Module> modules = moduleManager.findModule(module.getId().toString());
												Object[] object = modules.toArray();
												String li_id = "li_" + module.getId();
												if (i == 0) {
										%>
										<li class="current" id="<%=li_id%>">
											<a href="javascript:void(0);"
												onclick="goToModule('1','<%=module.getId()%>','<%=li_id%>','<%=module.getLink()%>','<%=module.getResName() %>');"><b><%=module.getResName()%></b>
											</a>
											<input type="hidden" value="<%=module.getId()%>" id="firstModuleId" />
											<input type="hidden" value="<%=module.getLink()%>" id="firstModuleLink" />
											<input type="hidden" value="<%=module.getResName()%>" id="firstModuleResName" />
										</li>
										<%
											} else {
										%>
										<li id="<%=li_id%>">
											<a href="javascript:void(0);"
												onclick="goToModule('2','<%=module.getId()%>','<%=li_id%>','<%=module.getLink()%>','<%=module.getResName() %>');"><b><%=module.getResName()%></b>
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
				<td height="70%" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="box1-tab" style="height:83%;">
						<tr>
							<td class="box1-top-left">
								&nbsp;
							</td>
							<td class="box1-top-center">
								&nbsp;
							</td>
							<td class="box1-top-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-left">
								&nbsp;
							</td>
							<td class="box1-main">
								<!-- 主面板 -->
								<div class="sub-title">
									<table width="100%" border="0" cellspacing="0" cellpadding="0" valign="top">
										<tr>
											<td class="sub-left-ico">
												<img src="images/ico-sub-title-01.gif" width="16"
													height="16" />
											</td>
											<td class="sub-title-txt" id="moduleResName">
												
											</td>
											<td class="sub-right-ico">
											</td>
										</tr>
									</table>
								</div>
								<div id="rightModule" style="height:300px">
								<IFRAME id='portal' name='portal' scrolling='no' src="" style='width: 100%;' frameborder='no'></IFRAME>
								</div>
							</td>
							<td class="box1-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-bom-left">
								&nbsp;
							</td>
							<td class="box1-bom-center">
								&nbsp;
							</td>
							<td class="box1-bom-right">
								&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="10%" valign="bottom">
					<!-- 底部 -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						id="buttom" valign="bottom">
						<tr class="bom" id="bomDiv">
							<td class="bom-left">
								登录信息 : <%=curPersonName%>
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

	var firstModuleId = document.getElementById("firstModuleId").value;
	var firstModuleLink = document.getElementById("firstModuleLink").value;
	var firstModuleResName = document.getElementById("firstModuleResName").value;
	
	//如果点击的是 系统管理的子节点
	<%if(childModule != null){%>
		firstModuleId = "<%=childModule.getId()%>";
		firstModuleResName = "<%=childModule.getResName()%>";
		firstModuleLink = "<%=childModule.getLink()%>";
	<%}%>
	
	showTree(firstModuleId,firstModuleLink,firstModuleResName);

	window.onload = function(){
		var headerDivHeight = document.getElementById("headerDiv").clientHeight;
		var iframeDivHieght = document.getElementById("portal").clientHeight;
		var navDivHeight = document.getElementById("navDiv").clientHeight;
		var bomDivHeight = document.getElementById("bomDiv").clientHeight;
		var root_height = (window.screen.availHeight-headerDivHeight-navDivHeight-bomDivHeight-80);
		document.getElementById("portal").style.height = root_height+"px";
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
