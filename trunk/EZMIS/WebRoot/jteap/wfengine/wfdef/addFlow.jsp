<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		
		<script type="text/javascript" src="${contextPath}/script/lightbox.js"></script>
		<style>
		.lightbox{width:200px;background:#FFFFFF;border:1px solid #ccc;line-height:25px; top:20%; left:20%;display:none;}
		.lightbox dt{background:#f4f4f4; padding:2px;}
		</style>
		
		<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
		<script type="text/javascript">
		
			var box = null;
		
			var getLabelValue= function(nodeName,objCell){
				if(objCell==null) {
					objCell = cell ;
				}
				var attrs = objCell.value.attributes;
				for (var i = 0;i < attrs.length; i++) {
					if(attrs[i].nodeName == nodeName){
						return  attrs[i].nodeValue;
					}
				}
			}
			/**
			*保存流程设计
			*/
			function saveWf(){
			    if(getLabelValue("flowName",addFlow.editor.graph.getModel().getRoot())==""){
			    	alert("请填写流程属性") ;
			    	return ;
			    }
			    
			    box.Show();
			    
				var re = addFlow.editor.save_backcall(
					CONTEXT_PATH+"/jteap/wfengine/workflow/WorkflowAction!uploadProcessDefAction.do",
					function(){
						box.Close();
						alert("保存成功");
						window.returnValue = true ; 
						window.close() ;
					});
			}
			
			function cancelWf(){
				window.returnValue = false ;
				window.close() ;
			}
			
			function onload(){
				box = new LightBox("idBox");
				box.Over = true;
				box.OverLay.Color = "#000";
				box.OverLay.Opacity = 50;
				box.Center = true;
			}
		</script>
		<style>
			.formTitle{
				height:50px;
				text-align: center;
				font-weight: bold;
				padding-top:5px;
				font-size: 15pt;
			}
			.bottomDiv{
				text-align: right;
				padding:10 10 0 0;
			}
			body,td{
				font-size: 10pt;
			}
			td{
				padding-left: 10px;
			}
			
		</style>
	</head>

	<body onload="onload();">
	
		<dl id="idBox" class="lightbox">
		  <dt id="idBoxHead"><b>正在保存,请稍候...</b> </dt>
		  <dd style="text-align:center;">
		  		<img src="icon/process.gif"/>
		  </dd>
		</dl>
		
		<iframe src="${contextPath}/component/processdef/workfloweditor.jsp" scrolling="no"
			width="100%" height="558" id="addFlow"></iframe>
		<br/>
		<div class="bottomDiv">
			<button onclick="saveWf()">保存</button>
			<button onclick="cancelWf()">取消</button>
		</div>
	</body>
</html>
