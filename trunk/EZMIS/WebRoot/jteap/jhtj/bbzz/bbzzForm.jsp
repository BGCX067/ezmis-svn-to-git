<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="indexScript.jsp" %>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<%@ include file="/inc/ext-all.jsp" %>
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		<SCRIPT LANGUAGE="JavaScript" src="${contextPath}/script/ntkoocx.js"></SCRIPT>
		<script type="text/javascript" src="${contextPath}/script/lightbox.js"></script>
		<script>
			var oJson = window.dialogArguments;
			var id ="${param.id}";	//表单编号
			var isUpdate ="${param.isUpdate}";	//是否是修改
			function closeExcelForm(){
				TANGER_OCX_OnDocumentClosed();
				//window.close();
			}
		
		</script>
		<script type="text/javascript" src="script/bbzzForm.js"></script>
		<style>
			.lightbox{width:400px;background:#FFFFFF;border:1px solid #ccc;line-height:25px; top:20%; left:20%;display:none;}
			.lightbox dt{width-background:#f4f4f4; padding:2px;}
		</style>
		<style>
			.bottomDiv{
				text-align: right;
				padding: 10 10 10 10;
			}
		</style>
		
	</head>

	<body onload="onload();" onresize="windowResize();">
		<dl id="idBox" class="lightbox">
		  <dt id="idBoxHead"><b><center>正在从服务器获取数据,请稍候...</center></b> </dt>
		  <dd style="text-align:center;">
		  		<img src="${contextPath}/jteap/jhtj/bbsj/icon/process.gif"/>
		  </dd>
		</dl>
		 <iframe   style="display:none"   id="ifAlert"   ></iframe> 
		 
		<div id="bgDiv">
			<FORM id="myForm" METHOD="POST" ENCTYPE="multipart/form-data" ACTION="${contextPath}/jteap/jhtj/bbzz/bbzzAction!saveUpdateAction.do" >
			
			<div id="hideDiv" style="display:none;margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #385F5E;">
				报表编号：<input type="text" name="id" value="${param.id}"/>
				报表模板编号：<input type="text" name="bbindexid"/>
				接口参数和值：<input type="text" name="sqlValue"/>
			</div>
			<div id="splash">
				<img src="icon/loading.gif">
			</div>
			<div style="height:520px;" id="ntkoDiv" style="display:none">
			
				<object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" codebase="${contextPath}/jteap/jhtj/OfficeControl.cab#version=4,0,6,1" width="100%" height="100%">
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
                    <SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>
				</object>
				<!-- 以下函数相应控件的两个事件:OnDocumentClosed,和OnDocumentOpened -->
				<script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
					TANGER_OCX_OnDocumentClosed();
				</script>
				<script language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
					TANGER_OCX_OnDocumentClosed();//先关闭
					TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
					TANGER_OCX_OBJ.Titlebar=false;
					TANGER_OCX_OBJ.Toolbars=true;
					TANGER_OCX_OBJ.Menubar=false;
					box.Show();
					//TANGER_OCX_OBJ.FileNew=false;
					//TANGER_OCX_OBJ.FileOpen=false;
					//TANGER_OCX_OBJ.FileSaveAs=false;
				</script>
				</div>
			</FORM>
		</div>
		<div id="toolBar" style="margin:2px 2px 2px 2px;height:30px;padding:10 10 10 10;background-color:#c3daf9;border: solid 1px #385F5E;width:100%">
			<button name="saveButton" onclick="subForm();">保存</button>
			<button name="btnFull" onclick="switchFullScreen();">全屏查看</button>
			<button name="exportButton" onclick="exportExcelForm();" name="saveButton">导出报表</button>
			<button onclick="cancel();" name="importButton">关闭</button>
			<input type="hidden" name="isReadOnly" value="true">
		</div>


	</body>
</html>
