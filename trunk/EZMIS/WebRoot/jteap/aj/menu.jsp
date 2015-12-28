<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.doclib.manager.DoclibCatalogManager"%>
<%@page import="com.jteap.system.doclib.model.DoclibCatalog"%>
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
		<%
			String moduleId = (String) request.getParameter("moduleId");
		%>
		<title>运行管理子系统首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
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
		<%
			DoclibCatalogManager catalogManager = (DoclibCatalogManager) SpringContextUtil.getBean("doclibCatalogManager");
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			Collection<DoclibCatalog> roots = catalogManager.findCatalogByParentId("40288a07236f337201236f5b93b70002");
			Object[] obj = roots.toArray();
			DoclibCatalog module = null;
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
				}
				sum++;
			}
			function goToPage(link,resName,moduleId,classid,childid,catalogCode){
				var oldClassId = parent.window.document.getElementById("oldClassId");
				var array_li = parent.window.document.getElementsByTagName("li");
				array_li[oldClassId.value].className = "";
				array_li[catalogCode].className = "current";
				oldClassId.value = catalogCode;
				parent.window.document.getElementById("portal").src = "module.jsp?moduleId="+moduleId+"&childId="+childid+"&catalogCode="+catalogCode;
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
							for(int i = 0; i < obj.length; i++) {
								module = (DoclibCatalog) obj[i];
								Module curModule=catalogManager.findModuleByCatalogCode(module.getCatalogCode());
								Collection<DoclibCatalog> modules = catalogManager.findCatalogByParentId(module.getId());
								Object[] object = modules.toArray();
								String CaalogCode="0";
								if(i>0){
									CaalogCode = module.getCatalogCode();
								}
						%>
						<tr>
							<td class="LabelBodyTbTitle">
								<%=module.getTitle()%>
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
													for (int j = 0; j < object.length; j++) {
														DoclibCatalog mods = (DoclibCatalog) object[j];
														//Collection<DoclibCatalog> childModules = catalogManager.findCatalogByParentId(mods.getId());
														//Object[] childObj = childModules.toArray();
														//if(childObj.length > 0){
														//	for(int k = 0; k < childObj.length; k++){
														//		DoclibCatalog childmods = (DoclibCatalog) childObj[k];
														
														//	}
														//}else{
														%>
														<div>
															<li>
																<a style="cursor:hand;" onclick="goToPage('<%=curModule.getLink() %>','<%=curModule.getResName() %>','<%=curModule.getId() %>','<%=CaalogCode %>','<%=mods.getId() %>','<%=module.getCatalogCode() %>');"><%=mods.getTitle() %></a>
															</li>
														</div>
													<%
														//}
												    %>
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
