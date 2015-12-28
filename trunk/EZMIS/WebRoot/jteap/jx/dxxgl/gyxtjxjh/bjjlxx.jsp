<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="indexScript.jsp"%>
		<title>检修设备信息</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	</head>

	<%
		String jhnf = request.getParameter("jhnf");
		String tdId = request.getParameter("tdId");
		String[] strTmp = tdId.split("_");
		String sbid = strTmp[0].substring(2, strTmp[0].length());
		String month = strTmp[1];
	%>
	<body scroll="no" onload="load()">
		<div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						记录信息
					</div>
					<form id="form"
						action=""
						method="post" enctype="multipart/form-data" target="ftarget">
						<table width="96%" height="50%" align="center" class="LabelBodyTb">
							<tr>
								<td class="POPtab-Title">
									记录内容：
								</td>
								<td class="POPtabTbEntry2">
									<input type="text" name="jlnr" style="height:100;width:280">
								</td>
							</tr>
						</table>
						<input type="hidden" name="id" id="id">
						<input type="hidden" name="jhnf" id='jhnf' value="<%=jhnf%>">
						<input type="hidden" name="sbid" id='sbid' value="<%=sbid%>">
						<input type="hidden" name="month" id='month' value="<%=month%>">
					</form>
					<iframe name="ftarget" style="display: none"></iframe>
					<div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()"
								class="pop-but01">
							<input type="button" value=" 关 闭 " onclick="window.close();"
								class="pop-but01">
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
	<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
	<script type="text/javascript">
			var timer = null;
			// 初始化
			function load() {
				var url = contextPath+"/jteap/jx/dxxgl/GyxtjxjhJlAction!showUpdateAction.do";
				var param = {};
				param.sbid = $('sbid').value;
				param.jhnf = $('jhnf').value;
				param.month = $('month').value;
		    	AjaxRequest_Sync(url,param,function(ajax){
		    		var text = ajax.responseText;
		    		eval("var result =" + (text) + "");
		    		if (result.success == true) {
		    			var id = result.id;
		    			$('id').value=id;
		    			$('jlnr').value=result.jlnr;
					}
		    	})				
			}

		    // 保存
		    function save() {
		    	var url = contextPath+"/jteap/jx/dxxgl/GyxtjxjhJlAction!saveOrUpdateAction.do";
		    	var param = {};
		    	param.id = $('id').value;
		    	param.sbid = $('sbid').value;
		    	param.jhnf = $('jhnf').value;
		    	param.month = $('month').value;
		    	param.jlnr = $('jlnr').value;
		    	
		    	AjaxRequest_Sync(url,param,function(ajax){
		    		var text = ajax.responseText;
		    		eval("var result =" + (text) + "");
		    		if (result.success == true) {
						window.returnValue = true;
						window.close();
					} else {
						alert(result.msg);
					}
		    	})
		    }
  </script>
</html>
