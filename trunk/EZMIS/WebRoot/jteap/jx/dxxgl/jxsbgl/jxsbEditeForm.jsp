<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>检修设备信息</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" onload="load()">
   	   <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						检修设备信息
					</div>
					<form id="form" action="${contextPath}/jteap/jx/dxxgl/JzsbtzAction!saveOrUpdateAction.do" target="ftarget" method="post" enctype="multipart/form-data">
					    <table width="96%" height="50%" align="center"class="LabelBodyTb">
					    	<tr>
					    		<td class="POPtab-Title">设备名称</td>
					    		<td class="POPtabTbEntry2"><div id="divSbmc"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">检修周期</td>
					    		<td class="POPtabTbEntry2"><div id="divJxzq" style="String:left"></div>
					    		<!--  <font size='2pt'>年</font>-->
					    		</td>
					    	</tr>
					    	<tr>
					    		<td class="poptab-title">项目级别</td>
					    		<td class="POPtabTbEntry2"><div id="divXmjb"></div></td>
					    	</tr>
					    	<tr>
					    		<td class="POPtab-Title">项目序号</td>
					    		<td class="POPtabTbEntry2"><div id="divXmxh"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="id"></div></td>
					    	</tr>
					    	<tr style="display:none">
					    		<td><div id="zyId"></div></td>
					    	</tr>	
					    </table>
				    </form>
				    <iframe name="ftarget" style="display:none"></iframe>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
   	 <!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	<script type="text/javascript" src="script/jxsbEditeForm.js" charset="UTF-8"></script>
	
	<!-- 入口程序 -->
    <script type="text/javascript">
    	var id = '${param.id}';
    	var zyId = '${param.sszy}';
    	hdnId.setValue(id);
    	hdnZyId.setValue(zyId);
		Ext.onReady(function(){
			Ext.QuickTips.init();
		});	
    </script>
    <!-- 加载脚本库  结束 -->
    
  </body>
</html>
