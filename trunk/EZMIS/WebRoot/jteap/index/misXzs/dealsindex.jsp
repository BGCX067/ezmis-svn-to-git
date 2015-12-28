<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.wfengine.tasktodo.manager.TaskToDoManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.wfengine.tasktodo.model.TaskToDo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<title>待办事项</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link href="style-aide/aide.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
			a{
	  			color: #000;
				cursor: hand;
	  		}
	  		a:HOVER {
				color: #F60;
			}
		</style>
		
		<script type="text/javascript" src="${contextPath}\script\common.js" charset="UTF-8"></script>
		<script type="text/javascript" src="style-aide/jquery-1.2.2.pack.js"></script>
		<script type="text/javascript" src="style-aide/ddaccordion.js"></script>
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		
		<script type="text/javascript">
			ddaccordion.init({
				headerclass: "submenuheader", //Shared CSS class name of headers group
				contentclass: "submenu", //Shared CSS class name of contents group
				collapseprev: true, //Collapse previous content (so only one open at any time)? true/false 
				defaultexpanded: [0], //index of content(s) open by default [index1, index2, etc] [] denotes no content
				animatedefault: false, //Should contents open by default be animated into view?
				persiststate: true, //persist state of opened contents within browser session?
				toggleclass: ["", ""], //Two CSS classes to be applied to the header when it's collapsed and expanded, respectively ["class1", "class2"]
				togglehtml: ["suffix", "<img src='style-aide/plus.gif' class='statusicon' />", "<img src='style-aide/minus.gif' class='statusicon' />"], //Additional HTML added to the header when it's collapsed and expanded, respectively  ["position", "html1", "html2"] (see docs)
				animatespeed: "normal", //speed of animation: "fast", "normal", or "slow"
				oninit:function(headers, expandedindices){ //custom code to run when headers have initalized
					//do nothing
				},
				onopenclose:function(header, index, state, isclicked){ //custom code to run whenever a header is opened or closed
					//do nothing
				}
			});
		</script>
	</head>

	<body id="index" scroll="auto">
		
		<script type="text/javascript">
			
			//弹出待办事项处理窗口
			function alertWindow(alertWindowUrl,id,tr_index){
				var oldTr = document.getElementById("tr_"+oldIndex);
				if(oldTr != null){
					//设置上一个选中的tr 样式  以到达 tr之间的互斥效果
					var oldIndex = parseInt(old_td_id.value);
					var oldTdCls_1 = "";
					var oldTdCls_2 = "";
					
					if(oldIndex%2 != 0){
						oldTdCls_1 = "AidTABTbEntry";
						oldTdCls_2 = "AidTABTbEntry3";
					}else{
						oldTdCls_1 = "AidTABTbEntry2";
						oldTdCls_2 = "AidTABTbEntry4";
					}
					
					oldTr.childNodes[0].className = oldTdCls_1;
					oldTr.childNodes[1].className = oldTdCls_1;
					oldTr.childNodes[2].className = oldTdCls_2;
				}
				
				var curTr = document.getElementById("tr_"+tr_index);
				curTr.childNodes[0].className = "AidTABTbEntrySelect";
				curTr.childNodes[1].className = "AidTABTbEntrySelect";
				curTr.childNodes[2].className = "AidTABTbEntrySelect1";
				
				old_td_id.value = tr_index;
					
				if(alertWindowUrl != "null" && alertWindowUrl != "" && id != "null" && id != ""){
					var fuhao = "?";
					if(alertWindowUrl.indexOf("?") != -1){
						fuhao = "&";
					}
					
					var flagSignIn = false;
					var wkflowParam;
					if(alertWindowUrl.indexOf("url=") != -1){
						//子系统单独模块的jsp
						alertWindowUrl = alertWindowUrl.substring(4,alertWindowUrl.length);
						alertWindowUrl = contextPath + alertWindowUrl + fuhao + "id=" + id;
					}else if(alertWindowUrl.indexOf("formSn=") != -1){ 
						//efrom表单
						alertWindowUrl = contextPath + "jteap/form/eform/eformRec.jsp?" + alertWindowUrl;
					}else if(alertWindowUrl.indexOf("wfForm=") != -1){
						//工作流表单
						alertWindowUrl = alertWindowUrl.substring(7,alertWindowUrl.length);
						alertWindowUrl = contextPath + "/jteap/wfengine/wfi/WorkFlowInstanceAction!showViewAction.do?" + alertWindowUrl;
						wkflowParam = alertWindowUrl;
						flagSignIn = true;
					}else{
						alert("url参数错误,请联系管理员...");
						return;
					}
					alertWindowUrl = window.location.protocol + "//" + window.location.host + alertWindowUrl;
					
					showIFModule(alertWindowUrl,"待办事项","true",317,400,{});
					if (flagSignIn) {
						var paramx = wkflowParam.split("&");
						var pid = paramx[0].split("=")[1];
						var token = paramx[1].split("=")[1];
						
						var params = {};
						params.pid = pid;
						params.token = token;
						var url = contextPath+"/jteap/wfengine/tasktodo/TaskToDoAction!releaseSignInAction.do";
						AjaxRequest_Sync(url,params,function(req){
						})
					}
					
					window.location.reload();
					if (window.parent.parent.frames["dbybIF"]) {
						window.parent.parent.frames["dbybIF"].frames["downDealsIF"].location.reload();
					}
				}	
			}
			
		</script>
		
		<div style="background-color: #FFF;width: 100%;height:300px;">
			<div>

				<!--- star ---->
				<div class="Aidemenu">
					
					<%
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
					
						TaskToDoManager taskToDoManager = (TaskToDoManager)SpringContextUtil.getBean("taskToDoManager");
						Map<String, List<TaskToDo>> map = taskToDoManager.findCurrentLoginDeals(curPersonLoginName,true);
						if(map.size() < 1){
							out.print("<font style='color:green;font-size:12px;font-weight:bold;'>暂无待办事项...</font>");
						}
						
						for(Map.Entry<String, List<TaskToDo>> entry : map.entrySet()){
							List<TaskToDo> list = entry.getValue();
					 %>
					
					<a class="menuitem submenuheader" href="#"><%=entry.getKey() %>（<%=list.size() %> 条记录）</a>
					<div class="submenu">
						
						<input type="hidden" id="old_td_id" value="0">
						
						<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" class="AidTAB">
							<tr></tr>
							<%
							for(int i=0; i<list.size(); i++){
								TaskToDo taskToDo = list.get(i);
								//tr 样式控制
								String tdCls_1 = "";
								String tdCls_2 = "";
								
								if(i%2 != 0){
									tdCls_1 = "AidTABTbEntry";
									tdCls_2 = "AidTABTbEntry3";
								}else{
									tdCls_1 = "AidTABTbEntry2";
									tdCls_2 = "AidTABTbEntry4";
								}
							 %>
							<tr id="tr_<%=i%>" onclick='alertWindow("<%=taskToDo.getAlertWindowUrl()%>","<%=taskToDo.getDocId()%>","<%=i%>")' >
								<td width="70%" class="<%=tdCls_1%>">
									<a href="javascript:void(0)"><%=taskToDo.getFlowTopic() %></a>
								</td>
								<td width="15%" class="<%=tdCls_1%>">
									<%=taskToDo.getStatus()%>
								</td>
								<td width="15%" class="<%=tdCls_2%>">
									<%=dateFormat.format(taskToDo.getPostTime()) %>
								</td>
							</tr>
							<%
							}
							 %>
						</table>
					</div>
					<%
						}
					 %>
					
				</div>
				<!--- end ----->
				
			</div>
		</div>

	</body>
</html>
