<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
<%@page import="com.jteap.form.cform.model.CForm"%>
<%@page import="com.jteap.form.dbdef.manager.PhysicTableManager"%>
<%@page import="com.jteap.core.utils.JSONUtil"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ include file="/inc/import.jsp"%>
<%@ page import="com.jteap.form.cform.manager.*" %>

<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%
			CFormManager cformManager=(CFormManager)SpringContextUtil.getBean("cformManager");
			PhysicTableManager physicTableManager=(PhysicTableManager)SpringContextUtil.getBean("physicTableManager");
			String cformId=request.getParameter("cformId");
			String docid = StringUtil.dealNull(request.getParameter("docid"));	//业务数据编号
			String docRecJson = "{}";	//为了避免json字符串为空导致报错
			if(StringUtil.isEmpty(cformId)){
				%>
					<b>表单调用方式不正确</b>
				<%
				return;
			}else{
				CForm cform=cformManager.get(cformId);
				request.setAttribute("cform",cform);
				if(StringUtil.isNotEmpty(docid)){
					Map rec = physicTableManager.getRecById(docid,cform.getDefTable().getSchema(),cform.getDefTable().getTableCode());
					docRecJson = JSONUtil.mapToJson(rec);
				}
			}
		 %>
		 
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<script>
		var cformId="${param.cformId}"	//表单编号
		</script>
		<%@ include file="/inc/ext-all.jsp" %>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		<script type="text/javascript" src="script/excelFormRec.js"></script>
		<SCRIPT LANGUAGE="JavaScript" src="script/ntkoocx.js"></SCRIPT>
		<script>
			oRecordJson=<%=docRecJson%>;	//数据记录json对象
			
		</script>
		<style>
			.bottomDiv{
				text-align: right;
				padding: 10 10 10 10;
			}
			#tuliDiv div{
				font-size: 9pt;
				text-align: center;
				padding-top: 4px;
				float: left;
				cursor:hand;
				height:22px;width:80px;
				border: solid 1px white;
			}
		</style>
		
	</head>

	<body onload="onload();" onresize="windowResize();">
		<div id="bgDiv">
			<div id="toolBar" style="display:block;margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				<form name="searchForm" action="excelFormRec.jsp">
					<input type="hidden" name="cformId" value="${param.cformId}"/>
					<input type="text" name="docid" value="<%=docid %>"/><button onclick = "$('searchForm').submit();">加载文档</button>
					<button onclick = "saveForm();">保存</button>
					<button onclick = "var xx=saveFormInAjax(true); alert(xx); $('searchForm').docid.value = xx; $('myForm').docid.value = xx;">AJAX保存</button>
				</form>
			</div>	
			<FORM id="myForm" METHOD="POST" ACTION="${contextPath}/jteap/form/cform/CFormAction!saveOrUpdateExcelCFormRecAction.do" >
			<div id="hideDiv" style="display:none;margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				
				数据项对应：<textarea name="excelDataItemXml" rows="3" cols="50" style="width:100%;">
					<c:if test="${cform!=null && cform.excelDataItemXml!=null }">${cform.excelDataItemXml}</c:if>
					<c:if test="${cform==null || cform.excelDataItemXml==null || cform.excelDataItemXml==''}">&lt;root&gt;&lt;/root&gt;</c:if>
				</textarea>
				表单编号：<input type="text" name="id" value="${param.cformId}"/>
				关联表定义编号：<input type="text" name="defTableId" value="${cform.defTable.id}"/>
				关联表名称：<input type="text" name="defTableCode" value="${cform.defTable.tableCode}"/>
				业务表数据记录编号：<input type="text" name="docid" value="<%=docid %>"/>
				业务表数据：<textarea name="recordJson" rows="5" cols="30"><%= docRecJson %></textarea>
			</div>
			<div id="splash">
				<img src="icon/loading.gif">
			</div>
			<div style="height:460px;" id="ntkoDiv" style="display:none">
			
				<object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" codebase="OfficeControl.cab#version=4,0,6,1" width="100%" height="100%">
					<param name="ProductCaption" value="泰和科技">
					<param name="ProductKey" value="A492D8C979693BA909B6FB74AD815DF2C5C51203">
			        <param name="BorderStyle" value="0">
                    <param name="BorderColor" value="1">
                    <param name="TitlebarColor" value="14402205">
			        <param name="TitlebarTextColor" value="0">
			        <param name="Caption" value="">
			        <param name="IsShowToolMenu" value="-1">
			        <param name="IsNoCopy" value="no">
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
					TANGER_OCX_OBJ.Toolbars=false;
					TANGER_OCX_OBJ.Menubar=false;
					//TANGER_OCX_OBJ.FileNew=false;
					//TANGER_OCX_OBJ.FileOpen=false;
					//TANGER_OCX_OBJ.FileSaveAs=false;
				</script>
				<!-- 双击事件 -->
				<script language="JScript" for=TANGER_OCX event="OnSheetBeforeDoubleClick(SheetName, row, col, cancel)">
					var cancel=onCellDbClick(row,col);
					CancelSheetDoubleClick=cancel;
				</script>
				
				<script language="JScript" for=TANGER_OCX event="OnSheetSelectionChange(sheetName, row, col)">
				</script>
				</div>
			</FORM>
		</div>
		<div id="tuliDiv">
			<div style = "background-color:F1F0F6;">可编辑域</div>
			<div style = "background-color:CCFFFF;" title = "计算域主要依靠计算公式自动生成计算值,无论是显示时计算还是保存时计算还是创建时计算，所以计算域是无法编辑的">计算域</div>
			<div style = "background-color:CCFFCC;" title="显示型字段主要针对已保存的数据进行计算，所以该类型的数据不可编辑，也不保存进数据库">显示型字段</div>
		</div>
		


	</body>
</html>
