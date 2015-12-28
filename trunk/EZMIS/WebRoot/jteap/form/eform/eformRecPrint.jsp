<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%
		//
			String formSn = request.getParameter("formSn");
			String docid = request.getParameter("docid");
		%>
		
		
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<script>
			var formSn = "<%=formSn%>";
			var docid = "<%=docid%>";
		</script>
		<%@ include file="/inc/ext-all.jsp" %>
		
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		<script type="text/javascript" src="script/eformRecPrint.js"></script>
		<SCRIPT LANGUAGE="JavaScript" src="script/ntkoocx.js"></SCRIPT>
		<style>
			.bottomDiv{
				text-align: right;
				padding: 10 10 10 10;
			}
			td{
				font-size:9pt;
			}
		</style>
		
	</head>

	<body onload="onload();" onresize="windowResize();">
	
			
		<div id="bgDiv">
			<FORM id="myForm" METHOD="POST" ENCTYPE="multipart/form-data" ACTION="${contextPath}/jteap/form/eform/EFormAction!saveOrUpdateEFormAction.do" >
			<div id="toolBar" style="margin:2px 2px 2px 2px;height:30px;padding:10 10 10 10;background-color: #efefef;border: solid 1px #e1e1e1">
				<button class="op" id='btnPrint' onclick="TANGER_OCX_PrintDoc()">打印</button>
				<button class="op" id='btnPreview' onclick="doPrintPreview();">打印预览</button>
				<button onclick="window.close();">退出</button>
			</div>
			
			<div id="splash" style="display:block">
				<img src="icon/loading.gif">
			</div>
			<div style="height:500px;" id="ntkoDiv" style="display:none">
			
				<object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" codebase="OfficeControl.cab#version=5,0,0,2" width="100%" height="100%">
					<param name="ProductCaption" value="泰和科技">
					<param name="ProductKey" value="A492D8C979693BA909B6FB74AD815DF2C5C51203">
			        <param name="BorderStyle" value="0">
                    <param name="BorderColor" value="1">
                    <param name="TitlebarColor" value="14402205">
			        <param name="TitlebarTextColor" value="0">
			        <param name="Caption" value="">
			        <param name="IsShowToolMenu" value="-1">
			        <param name="IsNoCopy" value="0">
			        <param name="IsUseUTF8URL" value="true">
			        <param name="IsUseUTF8Data" value="true">
                    <SPAN STYLE="color:red">
                    不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。或者<a href="NTKOSetup.zip">下载控件</a>手动进行安装</SPAN>
				</object>
				<!-- 以下函数相应控件的两个事件:OnDocumentClosed,和OnDocumentOpened -->
				<script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
					TANGER_OCX_OnDocumentClosed();
				</script>
				<script language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
					TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
					TANGER_OCX_OBJ.Titlebar=false;
					TANGER_OCX_OBJ.Toolbars=false;
					TANGER_OCX_OBJ.Menubar=false;
					TANGER_OCX_OBJ.FileNew=false;
					TANGER_OCX_OBJ.FileOpen=false;
					TANGER_OCX_OBJ.FullScreenMode=false;
					TANGER_OCX_OBJ.FileSaveAs=false;
				</script>
				
				</div>
			</FORM>
		</div>
		


	</body>
</html>
