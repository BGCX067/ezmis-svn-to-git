<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.utils.UUIDGenerator"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
<%@page import="com.jteap.form.eform.model.EForm"%>
<%@page import="com.jteap.form.eform.manager.EFormManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.form.eform.model.EFormCatalog"%>
<%@page import="com.jteap.form.eform.manager.EFormCatalogManager"%>
<%@page import="com.jteap.form.dbdef.model.DefTableInfo"%>
<%@page import="org.hibernate.Hibernate"%>
<%@page import="org.hibernate.ObjectNotFoundException"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%
			EFormManager eformManager = (EFormManager)SpringContextUtil.getBean("eformManager");
			EFormCatalogManager eformCatalogManager = (EFormCatalogManager)SpringContextUtil.getBean("eformCatalogManager");
			String id = request.getParameter("id");
			String jid = UUIDGenerator.javaId();
			EForm eform = null;
			DefTableInfo defTable = null;
			String htmlFileName = null;	//生成HTML文件名称
			boolean isNew = StringUtil.isEmpty(id);
			String defTableName = null;
			String oldFormSn = "";
			if(!isNew){
				eform = eformManager.get(id);
				pageContext.setAttribute("opMode","Modify");
				defTable = eform.getDefTable();
				try{ 
					Hibernate.initialize(defTable); 
				}catch(ObjectNotFoundException   ex){ 
				    defTable = null;
				}
				oldFormSn = eform.getSn();
				//defTableName = eform.getDefTable().getTableName()+"【"+eform.getDefTable().getTableName()+"】";
				htmlFileName = eform.getExHtmlUrl();
			}else{
				eform = new EForm();
				eform.setWidth(600);
				eform.setHeight(500);
				eform.setTitle("TB_FORM_"+jid);
				eform.setNewVer(true);
				eform.setSn("TB_FORM_"+jid);
				String catalogId = request.getParameter("catalogId");
				EFormCatalog catalog = eformCatalogManager.get(catalogId);
				eform.setCatalog(catalog);
				defTable = new DefTableInfo();
				defTable.setSchema(SystemConfig.getProperty("jdbc.schema"));
				defTable.setTableCode("TB_FORM_"+jid);
				defTable.setTableName("TB_FORM_"+jid);
				defTable.setId("");
				eform.setDefTable(defTable);
				eform.setExcelDataItemXml("<root></root>");
				pageContext.setAttribute("opMode","Create");
				defTableName = eform.getDefTable().getTableCode();
				htmlFileName = eform.getSn()+".htm";
			}
			pageContext.setAttribute("eform",eform);
		%>
		
		
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<script>
			var eformId="${param.id}"	//表单编号
			var opMode = "${opMode}";
			var oldFormSn = "<%=oldFormSn%>";
		</script>
		<%@ include file="/inc/ext-all.jsp" %>
		
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		<script type="text/javascript" src="script/eformEdit.js"></script>
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
				
				<button onclick="importExcelForm();">导入表单</button>
				<button onclick="exportExcelForm();">导出表单</button>
				<button onclick="submitHtmlForm();">保存表单</button>
				<button onclick="synchronizeDefTable();">同步数据定义</button>
			
				<button class="op" onClick="AddPictureFromLocal()">添加图片</button> 
				<button onclick="editInitInfo();">初始化脚本</button>
				<button class="op" onclick="TANGER_OCX_PrintDoc()">打&nbsp;&nbsp;&nbsp;&nbsp;印</button>
				<button class="op" onclick="TANGER_OCX_PrintPreview()">打印预览</button>
				<button name="btnFull" onclick="switchFullScreen();">全屏设计</button>
				<button onclick="window.close();">退&nbsp;&nbsp;&nbsp;&nbsp;出</button>
				<button onclick="paramSwithToggle();">参数开关</button>
				<button onclick="window.open('NTKOUninstall.exe');">控件卸载</button>
				<button onclick="window.open('NTKOSetup.zip');">手工控件安装</button>
				<button onclick="loadFormTemplate();">加载表单模板</button>
			</div>
			<div id="formDiv" style="margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				<table width="100%">
					<tr>
						<td>关联表</td>
						<td>
							<input type="text" value="<%=(defTable!=null?defTable.getTableName():"")%>" id="txtDefTableName" name="txtDefTableName" size="37" <%=(isNew||defTable==null)?"":"readonly" %> onkeyup = "doSynTableName();"/>
							<button onclick="selectDefTable();">...</button>
						</td>
					</tr>
					
					</tr>
						<td width='10%'>表单标题：</td>
						<td width='40%'><input type="text" value="${eform.title}" name="txtTitle" size="40"/></td>
						<td width='10%'>表单标识：</td>
						<td width='40%'>
							<input type="text" value="${eform.sn}" name="txtSn" size="40" />
						</td>
					</tr>
					<tr>
						<td>表单高度</td>
						<td><input type="text" value="${eform.height}" name="txtHeight" size="10"/></td>
						<td>表单宽度</td>
						<td><input type="text" value="${eform.width}" name="txtWidth" size="10"/></td>
					</tr>
					
				</table>
			</div> 
			<div id="hideDiv" style="display:none;margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				可编辑域：<textarea name="editableInputs" rows="3" cols="50" style="width:100%;">${eform.editableInputs}</textarea><br/>
				可编辑域：<textarea name="excelDataItemXml" rows="3" cols="50" style="width:100%;">${eform.excelDataItemXml}</textarea><br/>
				分类编号:<input type="text" value="${eform.catalog.id}" name="txtCatalogId"/><br/>
				表单编号：<input type="text" name="id" value="${eform.id}"/><br/>
				关联表定义编号：<input type="text" name="defTableId" value="<%=(defTable==null?"":defTable.getId()) %>"/><br/>
				关联表SCHEMA：<input type="text" name="schema" value="<%=(defTable==null?"":defTable.getSchema()) %>"/><br/>
				关联表名称：<input type="text" name="defTableCode" value="<%=(defTable==null?"":defTable.getTableCode()) %>"/><br/>
				是否创建新版本：<input type="text" name="txtSaveNewVer" value="0"/><br/>
				文件选择：<input type="file" name="selectFile" onchange="selectFileChanged();"/><br/>
				HTML文件发布路径：<input type="text" name="excelHtmlPublishPath" value="<%=SystemConfig.getProperty("EXCEL_HTML_PUBLISH_URL","\\\\localhost\\html")%>"/><br/>
				HTML 文件名 随机：<input type="text" name="excelHtmlFileName" value="<%=htmlFileName%>"/><br/>
				EFORM初始化脚本：<textarea name="onloadScript" rows="3" cols="50" style="width:100%;">${eform.onloadScript}</textarea><br/>
				
			</div>
			
			<div id="splash" style="display:block">
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
                    <SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。或者<a href="NTKOSetup.zip">下载控件</a>手动进行安装</SPAN>
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
					TANGER_OCX_OBJ.FileNew=false;
					TANGER_OCX_OBJ.FileOpen=false;
					TANGER_OCX_OBJ.FullScreenMode=false;
					TANGER_OCX_OBJ.FileSaveAs=false;
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
