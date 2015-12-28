<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.system.person.model.Person"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<%
			String moduleId = (String) request.getParameter("moduleId");
		%>
		<title>设备管理子系统首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<%
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
			Collection<Module> roots = null;
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
		<table width="100%" style="height: 100%" border="0" cellspacing="0"
			cellpadding="0" class="box1-tab">
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
					<div class="sub-title">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="sub-left-ico">
									<img src="images/ico-sub-title-01.gif" width="16" height="16" />
								</td>
								<td class="sub-title-txt">
									<%
										Module mod = (Module) moduleManager.get(moduleId);
									%>
									<%=mod.getResName()%>导航地图
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
					<!-- height表示快捷菜单高度 -->
					<table width="100%" height="10%" border="0" align="center" valign="top"
						cellpadding="0" cellspacing="1" class="LabelBodyTb">
						<%
							//过滤子系统功能模块名称
							if (session.getAttribute("SESSION_CURRENT_PERSON_ID").toString() == "1") {
								Person person = new Person();
								person.setId(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString());
								person.setUserLoginName(session.getAttribute("SESSION_CURRENT_PERSON_LOGINNAME").toString());
								roots = moduleManager.findModuleByParentWithPerm(person,moduleId);
							} else {
								roots = moduleManager.findModuleByParentWithPerm(personManager.get(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString()), moduleId);
							}
							Object[] obj = roots.toArray();
							for (int i = 1; i < obj.length; i++) {
								Module module = (Module) obj[i];
								Collection<Module> modules = moduleManager.findModule(module.getId().toString());
								Object[] object = modules.toArray();
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
											<div class="IcoNav-main">
													<%
														//过滤子系统功能模块下子模块名称
														if (session.getAttribute("SESSION_CURRENT_PERSON_ID").toString() == "1") {
															Person person = new Person();
															person.setId(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString());
															person.setUserLoginName(session.getAttribute("SESSION_CURRENT_PERSON_LOGINNAME").toString());
															roots = moduleManager.findModuleByParentWithPerm(person,module.getId().toString());
														} else {
															roots = moduleManager.findModuleByParentWithPerm(personManager.get(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString()), module.getId().toString());
														}
														Object[] ob = roots.toArray();
														for (int j = 0; j < ob.length; j++) {
																Module mods = (Module) object[j];
																Collection<Module> childModules = moduleManager.findModule(mods.getId().toString());
																Object[] childObj = childModules.toArray();
																if (childObj.length > 0) {
																	for (int k = 0; k < childObj.length; k++) {
																		Module childmods = (Module) childObj[k];
													%>
														<li>
															<a href="javascript:void(0);"
																onclick="goToPage('<%=module.getId()%>','<%=childmods.getId()%>','<%=i%>');">
														       	<div>
																   <img src="../../<%=childmods.getIcon() %>" width="90" height="70" align="center" /><br />
																   <%=childmods.getResName()%>
															   </div>
													       </a>
													     </li>
													<%
														}
													%>
													<%
														} else {
													%>
														<li>
															<a href="javascript:void(0);"
																onclick="goToPage('<%=module.getId()%>','<%=mods.getId()%>','<%=i%>');">
														       <div>
																   <img src="../../<%=mods.getIcon()%>" width="90" height="70" align="center" /><br />
																   <%=mods.getResName()%>
															   </div>
															</a>
														</li>
													<%
														}
													%>
													<%
														}
													%>
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
