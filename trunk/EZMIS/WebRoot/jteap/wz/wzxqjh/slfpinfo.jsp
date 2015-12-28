<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	<script type="text/javascript" src="${contextPath}/script/date.js" charset="UTF-8"></script>
  </head>

	<body scroll="no" id="index">
		<div class="pop-out">
		<div class="pop-in">
			<div class="pop-main">
				<div class="pop-title">
					需求分配情况
				</div>
				
			    <table align="center" height="150" width="97.5%" class="LabelBodyTb">
			    	<tr>
			    		<td class="POPtab-Title" width="200">需求计划编号</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divXqjhbh"></div>
			    		</td>
			    		<td class="POPtab-Title" width="180">生效时间</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSxsj"></div>
			    		</td>
			    		<td class="POPtab-Title" width="180">操作员</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divOperator"></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">工程类别</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divGclb"></div>
			    		</td>
			    		<td class="POPtab-Title">工程项目</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divGcxm"></div>
			    		</td>
			    		<td class="POPtabTbEntry2" colspan="2">
			    			<div id=""></div>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title">申请部门</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSqbm"></div>
			    		</td>
			    		<td class="POPtab-Title">申请时间</td>
			    		<td class="POPtabTbEntry2">
			    			<div id="divSqsj"></div>
			    		</td>
			    		<td class="POPtabTbEntry2" colspan="2" style="text-align:right">
				    			<input type="button" id="setting" value="库存量调整" onclick="setting()" class="pop-but01">
			    		</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtabTbEntry2" colspan="6">
			    			<div id="divXqjhDetailForslfpGrid"></div>
			    		</td>
			    	</tr>
			    </table>
				<!-- 
			    <div class="pop-but">
					<div class="pop-butMain">
						<input type="button" id="save" value=" 保 存 " onclick="save()" class="pop-but01">
		    			<input type="button" id="colse" value=" 关 闭 " onclick="window.close();" class="pop-but01">
					</div>
				</div>
				-->	
   			</div>
   		</div>
   	</div>
   	
   <!-- 加载等待图标 开始 -->
	 <div id="loading-mask" style=""></div>
	 <div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->
   	 
   	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="script/slfpinfo.js" charset="UTF-8"></script>
	
		
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){		
			Ext.QuickTips.init();
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});
    </script>
    <!-- 加载脚本库  结束 -->	
   	
	</body>
</html>
