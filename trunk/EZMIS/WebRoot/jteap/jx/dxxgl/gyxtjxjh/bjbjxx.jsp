<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.dict.manager.DictManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.dict.model.Dict"%>
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

		DictManager dictManager = (DictManager) SpringContextUtil.getBean("dictManager");
		List<Dict> dictYs = (List<Dict>) dictManager.findDictByUniqueCatalogName("GYJH_YS");
		List<Dict> dictTb = (List<Dict>) dictManager.findDictByUniqueCatalogName("GYJH_TB");
	%>
	<jteap:dict catalog="GYJH_YS"></jteap:dict>
	<body scroll="no" onload="load()">
		<div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						标记信息
					</div>
					<form id="form"
						action=""
						method="post" enctype="multipart/form-data" target="ftarget">
						<table width="96%" height="50%" align="center" class="LabelBodyTb">
							<tr>
								<td class="POPtab-Title">
									标记方式：
								</td>
								<td class="POPtabTbEntry2">
									<input type="radio" name="bjfs" value="ys" onclick="ysSelect()" checked="checked">
									颜色
									<select name="bjys" id="bjys"
										style="width: 50; background: #FFFFFF"
										onchange="this.style.background=$dictValue('GYJH_YS', this.options[this.options.selectedIndex].value);this.blur();">
										<%
											for (Dict dict : dictYs) {
										%>
										<option value="<%=dict.getKey()%>"
											style="background-color: <%=dict.getValue()%>;"></option>
										<%
											}
										%>
									</select>
								</td>
								<td class="POPtabTbEntry2">
									<input type="radio" name="bjfs" value="tb" onclick="tbSelect()">
									图标
									<select name="bjtb" id="bjtb">
										<%
											for (Dict dict : dictTb) {
										%>
										<option value="<%=dict.getKey()%>"
											style="background-img: <%=dict.getKey()%>;"></option>
										<%
											}
										%>
									</select>
									</div>
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
				var url = contextPath+"/jteap/jx/dxxgl/GyxtjxjhBjAction!showUpdateAction.do";
				var param = {};
				param.sbid = $('sbid').value;
				param.jhnf = $('jhnf').value;
				param.month = $('month').value;
		    	AjaxRequest_Sync(url,param,function(ajax){
		    		var text = ajax.responseText;
		    		eval("var result =" + (text) + "");
		    		if (result.success == true) {
		    			var bjys = result.bjys;
		    			var bjtb = result.bjtb;
		    			var id = result.id;
		    			$('id').value=id;
		    			$('bjys').value=bjys;
		    			$('bjtb').value=bjtb;
					} else {
					}
		    	})				
			}

			function ysChange() {
				this.style.background=$dictValue("GYJH_YS", this.options[this.options.selectedIndex].value);this.blur();
			}

		    function ysSelect() {
		  		$('bjtb').disabled=true;
		  		$('bjys').disabled=false;
		    }
		  
		    function tbSelect() {
		  		$('bjtb').disabled=false;
		   		$('bjys').disabled=true;
		 	}

		    // 保存
		    function save() {
		    	var url = contextPath+"/jteap/jx/dxxgl/GyxtjxjhBjAction!saveOrUpdateAction.do";
		    	var param = {};
		    	param.id = $('id').value;
		    	param.sbid = $('sbid').value;
		    	param.jhnf = $('jhnf').value;
		    	param.month = $('month').value;
		    	param.bjys = $('bjys').value;
		    	param.bjtb = $('bjtb').value;
		    	
		    	AjaxRequest_Sync(url,param,function(ajax){
		    		var text = ajax.responseText;
		    		eval("var result =" + (text) + "");
		    		if (result.success == true) {
		    			var ret = new Object();
		    			ret.success = true;
		    			ret.tbId = '<%=tdId%>';
		    			ret.color = $('bjys').value;
		    			ret.icon = $('bjtb').value;
						window.returnValue = ret;
						window.close();
					} else {
						alert(result.msg);
					}
		    	})
		    }
  </script>
</html>
