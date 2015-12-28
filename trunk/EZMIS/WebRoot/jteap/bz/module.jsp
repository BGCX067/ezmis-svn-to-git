<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<title>JTEAP-YX公共页面</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<%
			String moduleId = (String) request.getParameter("moduleId");
			String childId = (String) request.getParameter("childId");
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
		<script type="text/javascript" src="FunctionPanel.js" charset="UTF-8"></script>
	
		<!-- 最大化最小化rightFrame -->
		<script type="text/javascript">
			var sum = 1;
			var oldX = window.screenLeft;
			var oldY = window.screenTop;
			function toMaxRightFrameWindow(){
				if(sum % 2 == 1){
				//最大化
					document.getElementById("leftTree").style.display = 'none';
					document.getElementById("rightModule").style.height = window.screen.availHeight*0.9;
					window.moveTo(2,1);
					window.resizeBy(0,300);
					//rightModule.window.moveTo(5,10);
					//rightModule.window.resizeBy(265,150);
					document.getElementById("imageId").src = "images/ico-minimize.gif";
				}else{
				//最小化
					document.getElementById("leftTree").style.display = 'block';
					document.getElementById("rightModule").style.height = window.screen.availHeight*0.685;
					window.moveTo(oldX-3,oldY-30);
					window.resizeBy(0,-300);
					//rightModule.window.moveTo(oldX-3,oldY-30);
					//rightModule.window.resizeBy(-265,-150);
					document.getElementById("imageId").src = "images/ico-maximize.gif";
					//parent.window.document.getElementById("buttom").style.positionX = bottomX;
					//parent.window.document.getElementById("buttom").style.positionY = bottomY;
				}
				sum++;
			}
		</script>

		<!-- 程序入口 -->
		<script type="text/javascript">
		var dqbz = "${param.dqbz}";
		var isMyBz = "${param.isMyBz}";
		var moduleId="${param.moduleId}";
			var childId="<%=childId%>";
			var moduleResName="<%=moduleResName%>";

		Ext.onReady(function(){
			Ext.QuickTips.init();
			
			funcPanel =  new FunctionPanel(moduleId,moduleResName,childId);
			
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
				<td width="22%" valign="top" id="leftTree">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="box1-tab">
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
								<!-- 功能菜单 开始 -->
								<div id="leftMenu">

								</div>
								<!-- 功能菜单 结束 -->
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
				<td width="78%" valign="top">
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
								<IFRAME id='rightModule' name='rightModule' scrolling='yes' src="" style='height:100%;width:100%;' frameborder='yes'></IFRAME>
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
		//alert("网页可见区域宽:"+document.body.clientWidth+"\n网页正文全文宽:"+document.body.scrollWidth+"\n屏幕分辨率的宽:"+window.screen.width+"\n屏幕可用工作区宽度:"+window.screen.availWidth);
		document.getElementById("func-tree").style.height = document.body.clientHeight*0.975;
		document.getElementById("rightModuleDiv").style.height = document.body.clientHeight*0.937;
		//document.getElementById("doc-body").style.height = window.screen.availHeight*0.7;
		//alert(document.getElementById("doc-body").clientWidth);
		//document.getElementById("doc-body").style.width = window.screen.availWidth*0.78;
		
	}
</script>
