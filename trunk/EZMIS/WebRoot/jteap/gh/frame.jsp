<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/inc/import.jsp"%>
<%@ include file="/inc/meta.jsp" %>
<%@ include file="/inc/ext-all.jsp" %>
<% String path = request.getContextPath();%>
<html>
	<head>
		<title>工会管理</title>
		<link href="${contextPath}/jteap/gh/style/style.css" rel="stylesheet" type="text/css" />
	</head>

<SCRIPT type="text/javascript">
  	function gotoPages(id){
		Ext.Ajax.request({
				url:"${contextPath}/jteap/doclib/DoclibAction!getUrlAction.do",
				success:function(ajax){
					var responseText=ajax.responseText;	
					var responseObj = Ext.decode(responseText);
					if (responseObj.success) {
						var url=contextPath+"/jteap/system/doclib/generate/"+responseObj.url;
						window.parent.window.location= url;
					}
				},
				failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {docid:id}//Ext.util.JSON.encode(selections.keys)		
			});
		/*	
		AjaxRequest_Sync(, param, function(req) {
			asdfsadf
			var responseText = req.responseText;
			var responseObj = Ext.decode(responseText);
			alert(resposeObj);
			if (responseObj.success) {
				var url=contextPath+"/jteap/system/doclib/generate/"+responseObj.url;
				var scrWidth=screen.availWidth;
				var scrHeight=screen.availHeight;
				alert('');
				window.parent.window.location= url;
				//self.moveTo(0,0);
				//self.resizeTo(scrWidth,scrHeight);
			}
		});*/
  	}
  
  </SCRIPT>
	<body style="background-color: #FFFFFF">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0">
	<c:if test="${empty list}">
		<tr>
			<td >
				&nbsp;
			</td>
			<td class="NewsTxt4">
				暂无数据
			</td>
			<td class="NewsTime4">
				&nbsp; <s:property value="#clist.createDtStrNy" />
			</td>
		</tr>
	</c:if>
      <s:iterator id="clist" value="list" status="c">
			<tr>
				<td class="NewsIco4">
					&nbsp;
				</td>
				<td class="NewsTxt4">
					<a href="javascript:function(){return;}" onclick="gotoPages('<s:property value="#clist.id"/>');" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width: 205"><s:property value="#clist.title" /></a>
				</td>
				<td class="NewsTime4">
					&nbsp; <s:property value="#clist.createDtStrNy" />
				</td>
			</tr>
		</s:iterator>
     </table>
	</body>
</html>
