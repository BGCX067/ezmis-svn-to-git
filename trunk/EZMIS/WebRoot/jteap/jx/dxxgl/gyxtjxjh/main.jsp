<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.jx.dxxgl.model.Jxsbtz"%>
<%@page import="com.jteap.jx.dxxgl.manager.GyxtjxjhManager"%>
<%@page import="com.jteap.jx.dxxgl.model.Gyxtjxjh"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.jteap.jx.dxxgl.model.GyxtjxjhDataItem"%>
<%@page import="com.jteap.jx.dxxgl.model.GyxtjxjhBj"%>
<%@page import="java.lang.reflect.Method"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<style>
		TABLE {
			font-size: 12px;
			color: 043A7A;
			background-color: #E3E3E3;
		}
		TD {
			background-color: white;
		}
		.ValueInput{
			border: 1px solid #FFFFFF;
			width: 48px;
			height: 18px;
			text-align: left;
		}
		.ValueInput2{
			border: 1px solid #FFFFFF;
			width: 48px;
			height: 18px;
			text-align: left;
			background-color: #F5DEB3;
		}
	</style>
  </head>
  <jteap:dict catalog="GYJH_YS"></jteap:dict>
  <body id="index">
	
	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="script/main.js"></script>	
    <!-- 加载脚本库  结束 -->
	<script type="text/javascript">
    	var row_ids = new Array();
    	var row_sbmc = new Array();
    	var focusItem;
    </script>
	<%
		String zyId = request.getParameter("sszy");
		String year = request.getParameter("year");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String nowYear = sdf.format(new Date());
		String readOnly = "";
		if (!year.equals(nowYear)) {
			readOnly = "readonly='readonly'";
		}
		GyxtjxjhManager gyxtjxjhManager = (GyxtjxjhManager)SpringContextUtil.getBean("gyxtjxjhManager");
	%>
	
	<table id="myTable" cellspacing="1">
		<%
		// 获得页面所有数据
		List<GyxtjxjhDataItem> lstDataItem = gyxtjxjhManager.getGyxtjxjhData(zyId, year);
		for(GyxtjxjhDataItem dataItem : lstDataItem){
			// 设备台账
			Jxsbtz jxsbtz = dataItem.getJxsbtz();

			// 设备ID
			String sbid = jxsbtz.getId();
			// 设备名称
			String sbmc = jxsbtz.getSbmc();
			// 大小修周期
			String jxzq = jxsbtz.getJxzq();
		%>
		<script type="text/javascript">
			row_ids.push("<%=sbid%>");
			row_sbmc.push("<%=sbmc%>");
		</script>
		<%
			// 公用检修计划
			Gyxtjxjh gyxtjxjh = dataItem.getGyxtjxjh();
			if (gyxtjxjh != null) {
				String id = gyxtjxjh.getId();
		%>
			<tr id="<%=id%>">
				<!-- 大小修周期 -->
				<td align="center" nowrap="nowrap" width="45" height="20">
					<%=jxzq==null?"":jxzq %>
				</td>
			<%	
				// 查找上次检修时间
				String strScjxsj = gyxtjxjhManager.findScjxsjByScjxYearAndSbid(sbid);
				if (strScjxsj == null) {
					strScjxsj = "";
				}
			%>
				<td align="center" nowrap="nowrap" width="90" height="20">
						<%= strScjxsj%>
				</td>
			<%
				List<GyxtjxjhBj> lstBj = dataItem.getLstBj();
				// 遍历每个月的标记
				for (int i = 1; i < 13; i++) {
					GyxtjxjhBj gyxtjxjhBj = lstBj.get(i-1);
					String bjys = "";
					String bjtb = "";
					String style = "";
					// 如果标记不为空则设置样式
					if (gyxtjxjhBj != null) {
						bjys = gyxtjxjhBj.getBjys();
						bjtb = gyxtjxjhBj.getBjtb();
						style = "style='background-color:"+bjys+"'";
					}
					
					// 获得每个月单元格的值
					String strGet = "getMonth" + i;
					Method method = Gyxtjxjh.class.getMethod(strGet, new Class[]{});
					Object obj = method.invoke(gyxtjxjh, new Object[]{});
					String motValue = "";
					if (obj != null) {
						motValue = obj.toString();
					}
					
					String tdId = "td"+sbid+"_"+i;
			%>
					<td align="left" nowrap="nowrap" width="48" height="20">
						<input type="text" id='<%=tdId%>' value="<%=motValue %>" class="ValueInput" onfocus="tdInputFocus('<%=tdId%>')" onblur="tdInputBlur('<%=tdId%>')" <%=readOnly%> <%=style %>>
					</td>
			<%
				}
			} else {
			%>
			<tr>
				<!-- 大小修周期 -->
				<td align="center" nowrap="nowrap" width="60" height="20">
					<%=jxzq==null?"":jxzq %>
				</td>
				<td align="left" nowrap="nowrap" width="50" height="20">
				</td>
			<%
				for (int i = 1; i < 13; i++) {
					String tdId = "td"+sbid+"_"+i;
				
			%>
				<td align="left" nowrap="nowrap" width="50" height="20">
					<input type="text" id='<%=tdId%>'  class="ValueInput" onfocus="tdInputFocus('<%=tdId%>')" onblur="tdInputBlur('<%=tdId%>')" <%=readOnly%>>
				</td>
			<%
				}
			}
			%>
		</tr>
		<%
		}
		%>
	</table>
	
	<form name="myForm" method="post" action="${contextPath}/jteap/jx/dxxgl/GyxtjxjhAction!exportExcelAction.do" target="ftarget">
		<input id="jhnf" name="jhnf" type="hidden">
	</form>
	<iframe name="ftarget" style="display:none"></iframe>
	<script type="text/javascript">
		//设置iframe滚动条级联
		window.attachEvent("onscroll",function(){
	    	parent.topIF.document.body.scrollLeft = document.body.scrollLeft;
	    	parent.leftIF.document.body.scrollTop = document.body.scrollTop;
		});
		
		//设置输入项样式
		function tdInputFocus(id){
			document.getElementById(id).className = "ValueInput2";
			document.getElementById(id).select();
			focusItem = document.getElementById(id);
		}
		function tdInputBlur(id){
			document.getElementById(id).className = "ValueInput";
		}
		
		//保存
		function save(mask){
			var all_params = [];
			var year = parent.dtTime.value;
			var table = document.getElementById("myTable");

			for(var index=0; index<row_ids.length; index++){
				var params = {};
				var sbid = row_ids[index];
				params.sbid = sbid;
				params.sbmc = row_sbmc[index];
				var tr = table.rows[index];
				params.id = tr.id;

				for (var i = 1; i < 13; i++) {
					var td_item = document.getElementById("td"+sbid+"_"+i);
					var pid = "td"+sbid+"_"+i;
					var pvalue = td_item.value;
					eval("params."+pid+"=pvalue");
				}
				all_params.push(params);
			}
			
			// 保存全部日志记录
			saveMain(all_params,year,mask);
		}
		
		// 标记
		function biaoji() {
			if (focusItem) {
				var url = link3 + "?tdId="+focusItem.id + "&jhnf="+<%=year%>;
				var retValue = showModule(url, true, 400, 110);
				if (retValue && retValue.success == true) {
					var item = document.getElementById(retValue.tbId);
					item.style.background = $dictValue('GYJH_YS', retValue.color);
				}
			}
		}
		
		// 记录
		function jilu() {
			if (focusItem) {
				var url = link4 + "?tdId="+focusItem.id + "&jhnf="+<%=year%>;
				var retValue = showModule(url, true, 400, 160);
			}
		}

		var timer = null;
		var myMask = null;
		//导出
		function exportData(mask){
			document.getElementById('jhnf').value = <%=year%>
			myMask = mask;
			//导出全部日志记录
			myForm.submit();
			timer = window.setInterval("monitor()", 1000);
		}
		
		function monitor() {
			var iFrameTarget = $('ftarget')
			if (iFrameTarget.readyState == 'interactive') {
					window.clearInterval(timer);
					myMask.hide();
			}
		}
	</script>
  </body>
</html>
