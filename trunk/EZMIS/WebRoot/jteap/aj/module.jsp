<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.doclib.manager.DoclibCatalogManager"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<title>JTEAP-YX公共页面</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
		<%
			String moduleId = (String) request.getParameter("moduleId");
			String childId = (String) request.getParameter("childId");
		//	System.out.println(childId);
			String catalogCode = (String) request.getParameter("catalogCode");
			DoclibCatalogManager catalogManager = (DoclibCatalogManager) SpringContextUtil.getBean("doclibCatalogManager");
			Module curModule=catalogManager.findModuleByCatalogCode(catalogCode);
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			Module module = (Module)moduleManager.get(moduleId);
			String moduleResName = (String)module.getResName();
		%>
	</head>
	<body scroll="no">
		<!-- 加载等待图标 开始 -->
		<div id="loading-mask" style=""></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="${contextPath}/resources/extanim32.gif" width="32"
					height="32" style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div>
		<!-- 加载等待图标 结束 -->
		<!-- 加载脚本库  开始  -->
		<%@ include file="/inc/ext-all.jsp"%>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		<script type="text/javascript" src="FunctionPanel.js" charset="UTF-8"></script>
		<script type="text/javascript" src="RightGrid.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/SelectBox.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/SearchField.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/tab/TabCloseMenu.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
		<script type="text/javascript" src="EditCurrPresonForm.js" charset="UTF-8"></script>
		<script type="text/javascript" src="ChangePasswordForm.js" charset="UTF-8"></script>
		<script type="text/javascript" src="MainMenu.js" charset="UTF-8"></script>
		<!-- 最大化最小化rightFrame -->
		<script type="text/javascript">
			var sum = 1;
			var oldX = window.screenLeft;
			var oldY = window.screenTop;
			function toMaxRightFrameWindow(){
				if(sum % 2 == 1){
				//最大化
					//document.getElementById("leftTree").style.display = 'none';
					document.getElementById("rightModule").style.height = window.screen.availHeight;
					window.moveTo(2,1);
					window.resizeBy(0,200);
					//rightModule.window.moveTo(5,10);
					//rightModule.window.resizeBy(265,150);
					document.getElementById("imageId").src = "images/ico-minimize.gif";
				}else{
				//最小化
					//document.getElementById("leftTree").style.display = 'block';
					document.getElementById("rightModule").style.height = window.screen.availHeight*0.705;
					window.moveTo(oldX-3,oldY-25);//修正module页面在点击多个模块是放大缩小向上平移现象(oldY-30)改为(oldY-25)
					window.resizeBy(0,-200);
					//rightModule.window.moveTo(oldX-3,oldY-30);
					//rightModule.window.resizeBy(-265,-150);
					document.getElementById("imageId").src = "images/ico-maximize.gif";
					//parent.window.document.getElementById("buttom").style.positionX = bottomX;
					//parent.window.document.getElementById("buttom").style.positionY = bottomY;
				}
				sum++;
			}
		</script>
		<script type="text/javascript" src="index.js" charset="UTF-8"></script>

		<!-- 程序入口 -->
		<script type="text/javascript">
		var moduleId="${param.moduleId}";
			var childId="<%=childId%>";
			var moduleResName="<%=moduleResName%>";

		Ext.onReady(function(){
			Ext.QuickTips.init();
			
			//funcPanel =  new FunctionPanel(moduleId,moduleResName,childId);
			
			//roleTree=new RoleTree();
	
		/*	var viewport=new Ext.Viewport({
				layout:'border',
				items:[funcPanel,mainPanel]
			});*/
			//初始化的时候隐藏查询面板
			//searchPanel.collapse(false);
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		    
		});	
    	</script>
		<!-- 加载脚本库  结束 -->

		<!--  HEADER 开始 -->
		<div id="header">

		</div>
		<!--  HEADER 结束 -->

		<table width="100%" style="height:100%" border="0" cellspacing="0" cellpadding="0" valign="top">
			<tr>
				<td width="100%" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="box1-tab" style="height:100%;">
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
										<!-- 添加双击事件ondblclick(当双击标题栏最大化,再双击最小化) -->
										<tr ondblclick="toMaxRightFrameWindow();">
											<td class="sub-left-ico">
												<img src="images/ico-sub-title-01.gif" width="16"
													height="16" />
											</td>
											<td class="sub-title-txt" id="moduleResName">
												
											</td>
											<!-- 去掉class="sub-right-ico"，添加align="right" -->
											<td align="right">
												<a href="#" onclick="toMaxRightFrameWindow();"><img src="images/ico-maximize.gif" id="imageId" width="16" height="16" /> </a>
											</td>
										</tr>
									</table>
								</div>
								<div id="rightModuleDiv">
								<IFRAME id='rightModule' name='rightModule' scrolling='yes' src="rightFrame.jsp?moduleId=<%=moduleId %>" style='height:95%;width:100%;' frameborder='yes'></IFRAME>
								</div>
								<!----------->
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
		</table>
	</body>
</html>
<script type="text/javascript">
	window.onload = function(){
		var link="<%=curModule.getLink()%>";
		var resName="<%=curModule.getResName()%>";
		var moduleId="<%=curModule.getId()%>";
		var childid="<%=childId%>";
		window.document.getElementById("rightModule").src=contextPath + '/jteap/'+link+"&moduleId="+moduleId+"&childid="+childid;
		document.getElementById("moduleResName").innerText = resName;
	}
</script>
