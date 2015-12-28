<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.jjb.manager.PaiBanManager"%>
<%@page import="com.jteap.yx.jjb.model.PaiBan"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
  </head>
 
  <body scroll="no" id="index">
  	
  	<%
  		PaiBanManager paiBanManager = (PaiBanManager)SpringContextUtil.getBean("paiBanManager");
  	 	List<PaiBan> list = paiBanManager.getAll();
  	 	String baiBzb = "";
  	 	String zhongBzb = "";
  	 	String yeBzb = "";
  	 	
  	 	for(PaiBan paiBan : list){
  	 		if("夜班".equals(paiBan.getBc())){
  	 			yeBzb = paiBan.getZb();
  	 		}else if("白班".equals(paiBan.getBc())){
  	 			baiBzb = paiBan.getZb();
  	 		}else if("中班".equals(paiBan.getBc())){
  	 			zhongBzb = paiBan.getZb();
  	 		}
  	 	}
  	 	
  	 	SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM");
  	 	String nowYm = dataFormat.format(new Date());
  	 	String nowYmd1 = nowYm + "-01";
  	 	String nowYmd2 = nowYm + "-02";
  	 	String nowYmd3 = nowYm + "-03";
  	 	String nowYmd4 = nowYm + "-04";
  	 	String nowYmd5 = nowYm + "-05";
  	 %>
  	 <script type="text/javascript">
  	 	var yeBzb = "<%=yeBzb%>";
  	 	var baiBzb = "<%=baiBzb%>";
  	 	var zhongBzb = "<%=zhongBzb%>";
  	 </script>
  	
  	<!-- 数据字典 -->
  		<!-- 值班值别 -->
	<jteap:dict catalog="zbzb"></jteap:dict>
	
    <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						排班设置
					</div>
				    <table width="98%" height="71%" align="center"class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">时间</td>
				    		<td class="POPtabTbEntry2">
				    			<%=nowYmd1 %>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<%=nowYmd2 %>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<%=nowYmd3 %>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<%=nowYmd4 %>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<%=nowYmd5 %>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">夜班</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divYe1"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divYe2"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divYe3"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divYe4"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divYe5"></div>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">白班</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZao1"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZao2"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZao3"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZao4"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZao5"></div>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">中班</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZhong1"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZhong2"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZhong3"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZhong4"></div>
				    		</td>
				    		<td class="POPtabTbEntry2">
				    			<div id="divZhong5"></div>
				    		</td>
				    	</tr>
				    </table>
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 保 存 " onclick="save()" class="pop-but01">
			    			<input type="button" value=" 关 闭 " onclick="window.close();" class="pop-but01">
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
		
	<script type="text/javascript" src="script/paiBanForm.js" charset="UTF-8"></script>
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
