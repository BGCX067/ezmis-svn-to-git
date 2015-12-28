<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.utils.UUIDGenerator"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<%
			String schema = SystemConfig.getProperty("jdbc.schema","");		
		%>
		<script>
			var cformId="${param.id}"	//表单编号
			var cformType = "<c:if test="${cform != null}">${cform.type}</c:if>";
			var schema = "<%=schema%>";
		</script>
		
		<%@ include file="/inc/ext-all.jsp" %>
		<script type="text/javascript" src="script/excelForm.js"></script>
		<SCRIPT LANGUAGE="JavaScript" src="script/ntkoocx.js"></SCRIPT>
		
		<style>
			.bottomDiv{
				text-align: right;
				padding: 10 10 10 10;
			}
		</style>
		
	</head>

	<body onload="onload();" onresize="windowResize();">
	
			
		<div id="bgDiv">
			<FORM id="myForm" METHOD="POST" ENCTYPE="multipart/form-data" ACTION="${contextPath}/jteap/form/cform/CFormAction!saveOrUpdateExcelCFormAction.do" >
			<div id="toolBar" style="margin:2px 2px 2px 2px;height:30px;padding:10 10 10 10;background-color: #efefef;border: solid 1px #e1e1e1">
				
				<button onclick="importExcelForm();">导入表单</button>
				<button onclick="exportExcelForm();">导出表单</button>
				<button onclick="setRowZbCell();">设置为行指标列</button>
				<button onclick="submitForm();">保存表单</button>
				<button onclick="submitHtmlForm();">保存HTML表单</button>
				
				<c:if test="${cform!=null}">	
				<button onclick="$('txtSaveNewVer').value='1';submitForm();">另存为新版本</button>
				</c:if>
				
				<button class="op" onClick="AddPictureFromLocal()">添加图片</button>   
				<button class="op" onclick="TANGER_OCX_PrintDoc()">打&nbsp;&nbsp;&nbsp;&nbsp;印</button>
				<button class="op" onclick="TANGER_OCX_PrintPreview()">打印预览</button>
				<button name="btnFull" onclick="switchFullScreen();">全屏设计</button>
				<button onclick="window.close();">退&nbsp;&nbsp;&nbsp;&nbsp;出</button>
				<button onclick="test2();">测试</button>
				
			</div>
			<div id="formDiv" style="margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				表单标题：<input type="text" value="${cform.title}" name="txtTitle" size="40"/> <span style="color:red">*表单标题 必填项</span><br/>
				关联表中文名：<input type="text" value="<c:if test="${cform != null && cform.defTable!=null}">${cform.defTable.schema }.${cform.defTable.tableCode}【${cform.defTable.tableName}】</c:if>" id="txtDefTableName" name="txtDefTableName" size="37" readonly/>
									<button onclick="selectDefTable();">...</button> <span style="color:red">*表单关联表</span><br/>
				模版类型：<select id="selDimFormType" name="selDimFormType">
										<option value="EXCEL">一维</option>
										<option value="EXCEL_2">二维</option>
										<option value="EXCEL_2_D">二维(动)</option>
								  </select><span style="color:red">*请选择一种表单类型</span><br/>
				关联字典：<input type="text" value="${cform.dictId}" id="txtDictId"  name="txtDictId" size="40"/> <span style="color:red">关联字典</span><br/>
				行指标单元格：<input type="text" value="${cform.rowzbCell}" id="txtRowZbCell"  name="txtRowZbCell" size="40"/> <span style="color:red">行指标开始单元格</span><br/>

			</div> 
			<div id="hideDiv" style="display:none;margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				可编辑域：<textarea name="editableInputs" rows="3" cols="50" style="width:100%;">${cform.editableInputs}</textarea>
				数据项XML：<textarea name="excelDataItemXml" rows="3" cols="50" style="width:100%;">
					<c:if test="${cform!=null && cform.excelDataItemXml!=null }">${cform.excelDataItemXml}</c:if>
					<c:if test="${cform==null || cform.excelDataItemXml==null || cform.excelDataItemXml==''}">&lt;root&gt;&lt;/root&gt;</c:if>
				</textarea>
				分类编号:<input type="text" value="${param.catalogId}" name="txtCatalogId" size="40"/>
				表单编号：<input type="text" name="id" value="${param.id}"/>
				关联表定义编号：<input type="text" name="defTableId" value="${cform.defTable.id}"/>
				关联表schema：<input type="text" name="schema" value="${cform.defTable.schema}"/>
				关联表名：<input type="text" name="defTableCode" value="${cform.defTable.tableCode}"/>
				是否创建新版本：<input type="text" name="txtSaveNewVer" value="0"/>
				文件选择：<input type="file" name="selectFile" onchange="selectFileChanged();"/>
				HTML文件发布路径：<input type="text" name="excelHtmlPublishPath" value="<%=SystemConfig.getProperty("EXCEL_HTML_PUBLISH_URL","\\\\localhost\\html")%>"/>
				HTML 文件名 随机：<input type="text" name="excelHtmlFileName" value="<%=UUIDGenerator.javaId()%>.htm"/>
			
			</div>
			
			<div id="splash">
				<img src="icon/loading.gif">
			</div>
			<div style="height:460px;" id="ntkoDiv" style="display:none">
			
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
                    <SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>
				</object>
				<!-- 以下函数相应控件的两个事件:OnDocumentClosed,和OnDocumentOpened -->
				<script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
					TANGER_OCX_OnDocumentClosed();
				</script>
				<script language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
					TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
					TANGER_OCX_OBJ.Titlebar=false;
					TANGER_OCX_OBJ.Toolbars=true;
					TANGER_OCX_OBJ.Menubar=false;
					//TANGER_OCX_OBJ.FileNew=false;
					//TANGER_OCX_OBJ.FileOpen=false;
					//TANGER_OCX_OBJ.FileSaveAs=false;
				</script>
				<!-- 双击事件 -->
				<script language="JScript" for=TANGER_OCX event="OnSheetBeforeDoubleClick(SheetName, row, col, cancel)">
					onCellDbClick(row,col);
					CancelSheetDoubleClick=true;
				</script>
				
				</div>
			</FORM>
		</div>
		


	</body>
</html>
