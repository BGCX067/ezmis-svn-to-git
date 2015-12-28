<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="index2.css" type="text/css"></link>
	 <link rel="stylesheet" type="text/css" href="${contextPath}/script/ext-extend/form/Datetime/datetime.css"></link>
  </head>
 
  <body scroll="no" id="index">
	
    <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						电能表换表处理
					</div>
					
				    <table align="center" height="85.5%" width="98.5%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">电能表:</td>
				    		<td class="POPtabTbEntry2"><div id="divElecId"></div></td>
				    		<td class="POPtab-Title">换表时间:</td>
				    		<td class="POPtabTbEntry2"><div id="divTime"></div></td>
				    		<td class="POPtab-Title">换表人:</td>
				    		<td class="POPtabTbEntry2"><div id="divHbr"></div></td>
				    	</tr>
				    	<tr>
				    		<td colspan="6">&nbsp;</td>
				    	</tr>
				    	<tr>
				    		<td colspan="6">
			    			  <table align="center" height="99%" width="99%" class="LabelBodyTb">
			    			    <tr>
						    		<td class="POPtab-Title" width="12.5%">&nbsp;</td>
						    		<td class="POPtab-Title" width="12.5%">&nbsp;</td>
						    		<td class="POPtab-Title" width="12.5%">&nbsp;</td>
						    		<td class="POPtab-Title" width="12.5%">&nbsp;</td>
						    		<td class="POPtab-Title" width="12.5%">正向有功</td>
						    		<td class="POPtab-Title" width="12.5%">反向有功</td>
						    		<td class="POPtab-Title" width="12.5%">正向无功</td>
						    		<td class="POPtab-Title" width="12.5%">反向无功</td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title" rowspan="4">旧表</td>
						    		<td class="POPtab-Title">表名</td>
						    		<td class="POPtabTbEntry2"><div id="divElecIdO"></div></td>
						    		<td class="POPtab-Title">峰</td>
						    		<td class="POPtabTbEntry2"><div id="divFpzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divFpfO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divFqzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divFqfO"></div></td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title">表号</td>
						    		<td class="POPtabTbEntry2"><div id="divElecbhO"></div></td>
						    		<td class="POPtab-Title">平</td>
						    		<td class="POPtabTbEntry2"><div id="divPpzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPpfO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPqzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPqfO"></div></td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title">CT</td>
						    		<td class="POPtabTbEntry2"><div id="divCtO"></div></td>
						    		<td class="POPtab-Title">谷</td>
						    		<td class="POPtabTbEntry2"><div id="divGpzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divGpfO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divGqzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divGqfO"></div></td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title">PT</td>
						    		<td class="POPtabTbEntry2"><div id="divPtO"></div></td>
						    		<td class="POPtab-Title">总</td>
						    		<td class="POPtabTbEntry2"><div id="divPzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPfO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divQzO"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divQfO"></div></td>
						    	</tr>
						    	
						    	<tr>
						    		<td class="POPtab-Title" rowspan="4">新表</td>
						    		<td class="POPtab-Title">表名</td>
						    		<td class="POPtabTbEntry2"><div id="divElecIdN"></div></td>
						    		<td class="POPtab-Title">峰</td>
						    		<td class="POPtabTbEntry2"><div id="divFpzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divFpfN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divFqzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divFqfN"></div></td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title">表号</td>
						    		<td class="POPtabTbEntry2"><div id="divElecbhN"></div></td>
						    		<td class="POPtab-Title">平</td>
						    		<td class="POPtabTbEntry2"><div id="divPpzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPpfN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPqzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPqfN"></div></td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title">CT</td>
						    		<td class="POPtabTbEntry2"><div id="divCtN"></div></td>
						    		<td class="POPtab-Title">谷</td>
						    		<td class="POPtabTbEntry2"><div id="divGpzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divGpfN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divGqzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divGqfN"></div></td>
						    	</tr>
						    	<tr>
						    		<td class="POPtab-Title">PT</td>
						    		<td class="POPtabTbEntry2"><div id="divPtN"></div></td>
						    		<td class="POPtab-Title">总</td>
						    		<td class="POPtabTbEntry2"><div id="divPzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divPfN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divQzN"></div></td>
						    		<td class="POPtabTbEntry2"><div id="divQfN"></div></td>
						    	</tr>
						      </table>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td colspan="6">&nbsp;</td>
				    	</tr>
				    </table>
					
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()" class="pop-but01">
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
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/Datetime/Datetime.js"></script>
	
	<script type="text/javascript" src="script/change.js" charset="UTF-8"></script>
	
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
