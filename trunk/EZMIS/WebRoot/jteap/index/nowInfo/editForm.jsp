<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index">
	
    <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						机组运行状态设置
					</div>
					
				    <table align="center" height="50%" width="50%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">&nbsp;</td>
				    		<td class="POPtab-Title">机组状态</td>
				    		<td class="POPtab-Title">连续天数</td>
				    		<td class="POPtab-Title">备查天数</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#1机组</td>
				    		<td class="POPtabTbEntry2"><div id="divStatus1"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divRunDay1"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divBcDay1"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#2机组</td>
				    		<td class="POPtabTbEntry2"><div id="divStatus2"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divRunDay2"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divBcDay2"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#3机组</td>
				    		<td class="POPtabTbEntry2"><div id="divStatus3"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divRunDay3"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divBcDay3"></div></td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">#4机组</td>
				    		<td class="POPtabTbEntry2"><div id="divStatus4"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divRunDay4"></div></td>
				    		<td class="POPtabTbEntry2"><div id="divBcDay4"></div></td>
				    	</tr>
				    </table>
					
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 确 定 " onclick="save()" class="pop-but01">
			    			<input type="button" value=" 取 消 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
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
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		
	<script type="text/javascript" src="script/editForm.js" charset="UTF-8"></script>
	<!-- 加载脚本库  结束 -->
	
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
    
  </body>
</html>
