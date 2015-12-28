<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.form.eform.manager.EFormManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.form.eform.model.EForm"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
<%@page import="com.jteap.form.dbdef.manager.PhysicTableManager"%>
<%@page import="com.jteap.core.utils.JSONUtil"%>
<%@page import="com.jteap.system.jdbc.manager.JdbcManager"%>
<%@page import="com.jteap.core.utils.WebUtils"%>
<%@ include file="/inc/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	 	<base target="_self" />
		<%@ include file="/inc/meta.jsp"%>
		<%
			String formSn= request.getParameter("formSn");
			String docid = request.getParameter("docid");
			String design = request.getParameter("design");
			String status = request.getParameter("st");//状态 01或没有这个参数表示新增|修改状态  02表示打印状态
			String op = request.getParameter("op");//判断是否进行新的操作
			if(StringUtil.isEmpty(status)){
				status = "01";
			}
			
			if(StringUtil.isEmpty(formSn)){
				pageContext.getOut().write("表单调用非法,formSn参数必须");
				return;
			}
			EFormManager eformManager = (EFormManager)SpringContextUtil.getBean("eformManager");
			
			EForm eform = eformManager.getEFormBySn(formSn);
			if(eform==null){
				pageContext.getOut().write("指定的表单定义不存在");
				return;
			}
			
			pageContext.setAttribute("eform",eform);
			String formUrl = contextPath+SystemConfig.getProperty("EXCEL_HTML_CONTEXT_URL","/eform/html")+"/"+eform.getExHtmlUrl();
			String docRecJson = "";//
			//System.out.println(docid);
			if(StringUtil.isNotEmpty(docid)){
				JdbcManager jdbcManager=(JdbcManager)SpringContextUtil.getBean("jdbcManager");
				String schema = eform.getDefTable().getSchema();
				String tableName = eform.getDefTable().getTableCode();
				Map rec = jdbcManager.getRecById(docid,schema,tableName);
				docRecJson = JSONUtil.mapToJson(rec);
				///System.out.println(docRecJson);
				//如果是新增操作 则直接保存表单数据
				if("New".equalsIgnoreCase(op)){
					docid = "";
				}
			}
			String queryString = WebUtils.getQueryString(request);
		 %>
		<title>JTEAP 2.0自定义表单</title>
		<style>
			.lightbox{width:200px;background:#FFFFFF;font-size:9pt;border:1px solid #ccc;line-height:25px; top:20%; left:20%;display:none;}
			.lightbox dt{background:#f4f4f4; padding:2px;}
		</style>
		<script type="text/javascript" src="${contextPath}/script/lightbox.js"></script>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		<script type="text/javascript" src="script/editors.js"></script>
		<script>
			var status = '<%=status%>';
			var formUrl = '<%=formUrl%>';
		</script>
		<script type="text/javascript" src="script/eformRec.js"></script>
		
		<%
			if(!eform.isFinalManuscript()){
				%>
				<script>
					if(confirm("表单没有定稿,是否需要现在定稿？")){
						doFinalManuscript("<%=eform.getId()%>");
					}
				</script>
				<%
				return;
			}
		 %>
	</head> 
	<body onload="onload();" onresize="">
		<dl id="idBox" class="lightbox">
		  <dt id="idBoxHead"><b>正在加载数据，请稍候。。。。</b> </dt>
		  <dd style="text-align:center;">
		  		<img src="${contextPath}/resources/process.gif"/>
		  </dd>
		</dl>

		<div id="toolBar" style="display:<%=(design!=null && design.equals("true"))?"block":"none" %>;margin:2px 2px 2px 2px;font-size:10pt;padding:10px 10px 10px 10px;border:1px solid #EFEFEF;">
			<form name="searchForm" action="eformRec.jsp">
				<input type="hidden" name="formSn" value="<%=formSn %>"/>
				<input type="text" name="docid" value="<%=docid==null?"":docid %>"/>
				<button onclick = "$('searchForm').submit();">加载文档</button>
				<button onclick = "$('docid').value='';$('searchForm').submit();">新建</button>
				<button onclick = "submitForm();">保存</button>
				<button onclick = "openPrintForm();">打印预览</button>
				<button onclick = "doPrintForm();">打印</button>
				<button onclick="$('hideDiv').toggle();">参数开关</button>
				<button onclick="f_exportRecExcel();">导出Excel</button>
				<button onclick = "eformDesign('${eform.id}');">表单设计</button>
			</form>
		</div>
		<div id="hideDiv" style="display:none;margin:2px 2px 2px 2px;font-size:10pt;padding:10 10 10 10;border:1px solid #EFEFEF;">
				可编辑域：<textarea name="editableInputs" rows="3" cols="50" style="width:100%;">${eform.editableInputs}</textarea><br/>
				可编辑域：<textarea name="excelDataItemXml" rows="3" cols="50" style="width:100%;">${eform.excelDataItemXml}</textarea><br/>
				分类编号:<input type="text" value="${eform.catalog.id}" name="txtCatalogId"/><br/>
				表单编号：<input type="text" name="eformId" value="${eform.id}"/><br/>
				表单Sn：<input type="text" name="txtSn" value="${eform.sn}"/><br/>
				表单宽度：<input type="text" name="formWidth" value="${eform.width}"/><br/>
				表单高度：<input type="text" name="formHeight" value="${eform.height}"/><br/>
				关联表定义编号：<input type="text" name="defTableId" value="${eform.defTable.id}"/><br/>
				关联表SCHEMA：<input type="text" name="schema" value="${eform.defTable.schema}"/><br/>
				关联表名称：<input type="text" name="defTableCode" value="${eform.defTable.tableCode}"/><br/>
				HTML 文件名 随机：<input type="text" name="excelHtmlFileName" value="${eform.exHtmlUrl}"/><br/>
				表单加载完成执行脚本:<textarea id="onloadScript" name="onloadScript" rows="5" cols="30"><%= eform.getOnloadScript() %></textarea>
				业务表数据：<textarea id="recordJson" name="recordJson" rows="5" cols="30"><%= docRecJson %></textarea>
				request参数：<input type="text" name="queryString" value="<%=queryString %>"/><br/>
			</div>
		<div id="eformCnt" style="height:100px;width:100px;overflow-y:auto;overflow-x:auto;">
			<iframe id='eformFrame' onload="" src="#" height="500px" width="100px" frameborder="no"></iframe>
		</div>
	</body>
</html>
