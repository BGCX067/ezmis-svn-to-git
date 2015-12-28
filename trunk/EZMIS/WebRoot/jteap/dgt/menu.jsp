<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.person.model.Person"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="/inc/meta.jsp"%>
		<title>党工团管理子系统首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		
		<%
		    String moduleId = request.getParameter("moduleId");
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
			
			Person person = new Person();
			person.setId(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString());
			person.setUserLoginName(session.getAttribute("SESSION_CURRENT_PERSON_LOGINNAME").toString());
			
			List<Module> moduleList = null;
			//过滤子系统功能模块名称
			if (session.getAttribute("SESSION_CURRENT_PERSON_ID").toString() == "1") {
				moduleList = (List<Module>)moduleManager.findModuleByParentWithPerm(person,moduleId);
			} else {
				moduleList = (List<Module>)moduleManager.findModuleByParentWithPerm(personManager.get(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString()), moduleId);
			}
			
			Module module = null;
		%>
		<script type="text/javascript">
			
			var sum = 1;
			var oldX = window.screenLeft;
			var oldY = window.screenTop;
			var bottomX = parent.window.document.getElementById("buttom").getBoundingClientRect().left;
			var bottomY = parent.window.document.getElementById("buttom").getBoundingClientRect().top;
			
			function toMaxWindow(){
				if(sum % 2 == 1){
				//最大化
					window.moveTo(2,1);
					window.resizeBy(0,155);
					document.getElementById("imageId").src = "images/ico-minimize.gif";
				}else{
				//最小化
					window.moveTo(oldX-3,oldY-25);
					window.resizeBy(0,-155);
					document.getElementById("imageId").src = "images/ico-maximize.gif";
					//parent.window.document.getElementById("buttom").style.positionX = bottomX;
					//parent.window.document.getElementById("buttom").style.positionY = bottomY;
				}
				sum++;
			}
			
			function goToPage(moduleId,childId,classId){
				parent.window.document.getElementById("portal").src = "module.jsp?moduleId="+moduleId+"&childId="+childId;
				var oldClassId = parent.window.document.getElementById("oldClassId");
				
				var array_li = parent.window.document.getElementsByTagName("li");
				array_li[oldClassId.value].className = "";
				array_li[classId].className = "current";
				
				oldClassId.value = classId;
			}
			
		</script> 
	</head>

	<body>
		<table width="100%" style="height:100%" border="0" cellspacing="0" cellpadding="0"
			class="box1-tab">
			<tr>
				<td class="box1-top-left">&nbsp; 
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
					<div class="sub-title">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="sub-left-ico">
									<img src="images/ico-sub-title-01.gif" width="16" height="16" />
								</td>
								<td class="sub-title-txt">
									<%
										Module mod = (Module)moduleManager.get(moduleId);
									%>
									<%=mod.getResName() %>导航地图
								</td>
								<td class="sub-right-ico">
									<!-- 
									<a href="#" onclick="toMaxWindow();"><img src="images/ico-maximize.gif" id="imageId" width="16"
											height="16" /> </a>
									-->
								</td>
							</tr>
						</table>
					</div>
					<table width="100%" height="95%" border="0" align="center" cellpadding="0"
						cellspacing="1" class="LabelBodyTb">
						<%
							for (int i = 1; i < moduleList.size(); i++) {
								module = moduleList.get(i);
								List<Module> childModules = null;
								
								//过滤子系统功能模块名称
								if (session.getAttribute("SESSION_CURRENT_PERSON_ID").toString() == "1") {
									childModules = (List<Module>)moduleManager.findModuleByParentWithPerm(person,module.getId().toString());
								} else {
									childModules = (List<Module>)moduleManager.findModuleByParentWithPerm(personManager.get(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString()), module.getId().toString());
								}
						%>
						<tr>
							<td class="LabelBodyTbTitle">
								<%=module.getResName()%>
							</td>
							<%
										if (i % 2 == 1) {
							%>
							<td class="LabelBodyTbEntry2">
								<%
									} else {
								%>
							
							<td class="LabelBodyTbEntry">
								<%
									}
								%>
								<table width="98%" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr>
										<td>
											<div class="chromestyle" id="chromemenu">
												<ul>
													<%
													for (int j = 0; j < childModules.size(); j++) {
														Module mods = childModules.get(j);
													%>	
														<div>
															<li>
																<a href="javascript:void(0);" onclick="goToPage('<%=module.getId()%>','<%=mods.getId()%>','li_<%=module.getId()%>');"><%=mods.getResName() %></a>
															</li>
														</div>
													<%
													}	
												    %>
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<%
							}
						%>
					</table>
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
	</body>
</html>
