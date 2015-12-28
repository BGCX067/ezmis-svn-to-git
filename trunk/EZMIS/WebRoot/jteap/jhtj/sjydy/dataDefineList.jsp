<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 <%@ include file="/inc/ext-all.jsp" %>
	<link type="text/css" rel="stylesheet" href="${contextPath}/component/columnlist/includes/columnlist.css" />
		
	<script type="text/javascript" src="${contextPath}/component/columnlist/includes/sortabletable.js"></script>
	<script type="text/javascript" src="${contextPath}/component/columnlist/includes/columnlist.js"></script>
	<SCRIPT language="JavaScript" src="${contextPath}/script/xml.js"></SCRIPT>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelValuePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNodeUI.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/tree/CheckboxTreeNode.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/upload/UploadDialog.js" charset="UTF-8"></script>
    <script type="text/javascript">
			var currSystemId="${param.currentSysId}";
			var isUpdate="${param.isUpdate}";
			var id="${param.id}";
	</script>    
	<script type="text/javascript" src="script/op_dataDefine-xml.js" charset="UTF-8"></script>
	<script type="text/javascript" src="script/op_dataDefine.js" charset="UTF-8"></script>
  <body  onload="onloadEvent();">
		<script type="text/javascript">
			var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中，请稍等"}); 
			myMask.show();
		</script>
		
		
		<!-- 内容域 开始 -->
				<TABLE border="0" width='100.5%'  height="100%">			
					<TR class="GridCellJ"> 
						<td class="GridCellL2" align='left' width='50%' height="100%">
							<!--列表开始-->
							<div id="container" class="webfx-columnlist" style="width:100%;height: 100%">
								<div id="head" class="webfx-columnlist-head" style="width:100%;">
									<table cellspacing="0" cellpadding="0" style="width:100%">
										<tr>
											<td>数据视图<img src="${contextPath}/component/columnlist/images/asc.png"/></td>
											<td>名称<img src="${contextPath}/component/columnlist/images/asc.png"/></td>
											</tr>
									</table>
								</div>
								<div id="body" class="webfx-columnlist-body" style="width: 100%;height: 100%">
									<table cellspacing="0" cellpadding="0" style="width: 100%;">
										<colgroup span="2">
											<col style="width: 30%;" />
											<col style="width: 30%;" />						
										</colgroup>
										<c:forEach items="${tables}" var="table">
											<tr>
												<td>
													<input onclick="onTableChecked();" type="checkbox" name="chk_table_${table.vname}" style="border: 0px"/>${table.vname}
												</td>
												<td>
													<input type="text" name="input_table_${table.vname}" value="<c:if test="${table.vname!=null}">${table.vname}</c:if>" style="border: 0px" onchange="onTableNameChanged();" onclick="list1.selectRow(this.parentNode.parentNode.rowIndex);">
												</td>
											</tr>						
										</c:forEach>
									</table>
								</div>
							</div>
							<!--列表结束-->
						</td>
						<TD class="GridCellL2" width='50%'>
							<!--字段列表开始-->
							<div id="container2" class="webfx-columnlist" style="width:100%;height: 100%">
								<div id="head2" class="webfx-columnlist-head" style="width:100%;">
									<table cellspacing="0" cellpadding="0" style="width:100%">
										<tr>
												<td>字段<img src="${contextPath}/component/columnlist/images/asc.png"/></td>
											<td>名称<img src="${contextPath}/component/columnlist/images/asc.png"/></td>
											<td>数据类型<img src="${contextPath}/component/columnlist/images/asc.png"/></td>
											</tr>
									</table>
								</div>
								<div id="body2" class="webfx-columnlist-body" style="width: 100%;height: 100%">
									<table cellspacing="0" cellpadding="0" style="width: 100%;" id="table_001">
										<colgroup span="2">
											<col style="width: 30%;" />
											<col style="width: 40%;" />			
											<col style="width: 30%;" />					
										</colgroup>
										<tr><td></td><td></td><td></td></tr>
									</table>
								</div>
							</div>
							<!--字段列表结束-->
						</TD>			
					</TR>	
						
					<TR height="30px">
						<TD align='right' valign="middle" colspan=4>
							<form name="myForm" target="next" action="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!showLastPageAction.do" method="post">
							<input type="hidden" name="xmlField" value=""/>
							<input type="hidden" name="sysid" value=""/>
							<input type="hidden" name="isUpdate" value=""/>
							<input type="hidden" name="id" value=""/>
							是否保留sql:<select name="isSql"><option value="1">是</option><option value="2">否</option></select>
							<Input type="button" value="全选" class="button01" onclick="checkedAll(this);" />
							<Input type="button" value="下一步" class="button01" onclick="submitForm();"/>
							<Input type="button" value="取消" class="button01" onclick="window.close();"/>
							</form>
						</TD>
					</TR>
				</TABLE>				
			<!-- 内容域 结束-->
  </body>
</html>

<script type="text/javascript">
//列表一

	var list1 = new WebFXColumnList();
	list1.colorEvenRows=false;	//不使用颜色间隔
	list1.sortable=false;
	list1.columnMovable=false;
	list1.columnWidthResizable=true;
	list1.multiple=false;
	var rc = list1.bind(document.getElementById('container'), document.getElementById('head'), document.getElementById('body'));
// 这里由于数据为空的时候，导致错误而屏蔽掉
//	list1.setSortTypes([TYPE_STRING, TYPE_INPUT]);
	list1.onselect=onSelectTable;
	
//列表二	
	var list2 = new WebFXColumnList();
	list2.colorEvenRows=false;	//不使用颜色间隔
	list2.sortable=false;
	list2.columnMovable=false;
	list2.columnWidthResizable=true;
	list2.multiple=false;	
	var rc2 = list2.bind(document.getElementById('container2'), document.getElementById('head2'), document.getElementById('body2'));
// 这里由于数据为空的时候，导致错误而屏蔽掉
//	list2.setSortTypes([TYPE_STRING, TYPE_INPUT,TYPE_STRING]);	
//list1.selectRow(0);

</script>