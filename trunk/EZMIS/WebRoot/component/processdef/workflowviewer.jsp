<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="contextPath" scope="page"
	value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8"); 
	response.setContentType( "text/html;charset=UTF-8 "); 
	String curNode = request.getParameter("currentNode");
%>
<html>
	<head>
		<!-- 
		<script type="text/javascript" src="${contextPath}/script/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="${contextPath}/script/ext-all.js"></script> 		
		<script type="text/javascript" src="${contextPath}/script/build/locale/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${contextPath}/script/common.js"></script>
		 -->
		
		<title>WorkflowEditor</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<style type="text/css" media="screen">
				div.base {
			
					overflow: hidden;
					white-space: nowrap;
					font-family: Arial;
					font-size: 8pt;
				}
				div.base#graph {
					border-style: solid;
					border-color: #F2F2F2;
					border-width: 1px;
					background: url('images/grid.gif');
				}			
		</style>
		
		<script type="text/javascript" src="${contextPath}/component/processdef/js/mxApplication.js"></script>
		<script type="text/javascript" src="${contextPath}/component/processdef/js/workflowviewer-mxclient.js"></script>
		<script type="text/javascript">
			mxConstants.DEFAULT_HOTSPOT = 1;
			mxBasePath = '${contextPath}/component/processdef/';
			//环境路径
			var CONTEXT_PATH='${contextPath}';
			
			function onload(){
				new mxApplication('${contextPath}/component/processdef/config/workflowviewer.xml');
				
				var flowConfigId="${param.flowConfigId}";
				if(flowConfigId!=null && flowConfigId!=""){
					var url=CONTEXT_PATH+"/jteap/wfengine/workflow/FlowConfigAction!getFlowConfigXmlAction.do?id="+flowConfigId;
					editor.open(url);
					var graph = editor.graph;
					//禁用绘图
					graph.setEnabled(false);
					//禁用提示框
					graph.setTooltips(false);
					//删除所有事件
					mxEvent.removeAllListeners(graph.container);

					var model = graph.getModel();

					var root = model.root;
					var v = root.value;
					var owner = v.ownerDocument;
					var xml = owner.xml;
					//当前环节的名称
					var curNode = "<%=curNode%>";
					var curNodeArray = curNode.split(',');
					
					for(var i = 0;i < curNodeArray.length;i++){
						update(graph, xml,curNodeArray[i]);
					}
				}
			}
			
			function update(graph, xml,curNode)
			{
				if (xml != null &&
					xml.length > 0)
				{
					var doc = mxUtils.parseXml(xml);
					if (doc != null &&
						doc.documentElement != null)
					{
						var model = graph.getModel();
						//获取task节点
						var nodes = doc.documentElement.getElementsByTagName('task');
						if (nodes != null &&
							nodes.length > 0)
						{
							model.beginUpdate();
							try
							{
								for (var i = 0; i < nodes.length; i++)
								{
									if(curNode == nodes[i].getAttribute('name')){
										var id = nodes[i].getAttribute('id');
										var cell = model.getCell(id);
										if (cell != null)
										{
											//设置当前环节填充颜色
											graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, '#FFFF66', [cell]);
											//设置当前环节文字的背景颜色
											graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, '#FFFF66', [cell]);
											//设置当前环节边框的宽度
											graph.setCellStyles(mxConstants.STYLE_STROKEWIDTH, 2,[cell]);
											//设置当前环节边框的颜色
											graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, '#FF9900',[cell]);
										}
									}
									
								}
							}
							finally
							{
								model.endUpdate();
							}
						}
					}
				}
			};
		</script>
		<script type="text/javascript">
		
		</script>
	</head>
	<body  style="padding: 0px 0px 0px 0px"
		onload="onload()">
			<table id="splash" width="100%" height="100%"
				style="background: white; position: absolute; top: 0px; left: 0px; z-index: 4;">
				<tr>
					<td align="center" valign="middle">
						<img src="images/loading.gif">
					</td>
				</tr>
			</table>
			<div id="graph" style="position: absolute;margin: 2px 0px 0px 2px">
				<!-- Graph Here -->
			</div>
			<div id="status" class="base" align="right">
				<!-- Status Here -->
			</div>
			<script type="text/javascript">
				document.getElementById("graph").style.height=600;
				document.getElementById("graph").style.width=790;
			</script>
	</body>
</html>
