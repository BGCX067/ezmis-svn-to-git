
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<style>
			body {
				font-family: Arial, sans-serif;
				margin: 0px;
				padding: 0px;
			}
			
			#tabsJ {
				float: left;
				width: 34px;
				font-size: 13px;
				line-height: normal;
				font-weight: bold;
				vertical-align: top;
			}
			
			#tabsJ ul {
				margin: 0;
				padding: 0px 0px 5px 0px;
				list-style: none;
			}
			
			#tabsJ li {
				display: inline;
				margin: 0;
				padding: 0;
			}
			
			#tabsJ a {
				float: left;
				margin: 0;
				padding: 0;
				text-decoration: none;
				border-bottom-width: 1px;
				border-bottom-style: solid;
				border-bottom-color: #CCCCCC;
			}
			
			#tabsJ a span {
				float: left;
				display: block;
				padding: 10px 10px 5px 10px;
				color: #24618E;
				background-image: url(icon/tabrightJ.gif);
				background-repeat: no-repeat;
				background-position: left top;
			}
			
			/* Commented Backslash Hack hides rule from IE5-Mac \*/
			#tabsJ a span {
				float: none;
			}
			
			/* End IE5-Mac hack */
			#tabsJ a:hover span {
				color: #FFF;
			}
			
			#tabsJ a:hover {
				background-position: 0% -176px;
			}
			
			#tabsJ a:hover span {
				background-position: 100% -176px;
			}
			</style>
			
			<script>
		  function showDCTabInfo(url,ff){
					var objItemList = document.getElementsByTagName("span");
					for(var i = 0;i < objItemList.length;i ++){
						if(ff == i){
							objItemList[i].style.backgroundPosition = "100% -176px";
		          			objItemList[i].style.color = "#ffffff";
						}
						else{
							objItemList[i].style.backgroundPosition = "100% 0px";
		          			objItemList[i].style.color = "#24618E";
						}
					}
						document.getElementById("_main").src=url;
				}
				
				function overItem(ff){
					var objItemList = document.getElementsByTagName("span");
					for(var i = 0;i < objItemList.length;i ++){
						//alert(objItemList[i].outerHTML);
						if(ff == i){
							objItemList[i].style.backgroundPosition = "100% -176px";
							objItemList[i].style.color = "#000000";
						}else{
							objItemList[i].style.backgroundPosition = "100% 0px";
							objItemList[i].style.color = "#666666";
						}
					}
				}
				
				function changeSize(){
			   		try{		
						var height = document.body.offsetHeight;
						var width=document.body.offsetWidth;	
						var ver=navigator.appVersion;			
						if(ver.indexOf("MSIE 7")>-1){			
							//document.all.tableContainer.style.height=height-80;
							$("_main").style.height=height-37;
						}else{
							//document.all.tableContainer.style.height=height-120;
							$("_main").style.height=height-5;	
						}
						}catch(e){}
			   	 }
			    window.onresize=changeSize;
			</script>
  </head>
 
  <body scroll="no" onload="showDCTabInfo('${contextPath}/jteap/jhtj/zyzb/zyzbAction!getLeftViewAction.do?zbfl=1','0');">
	   <table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="margin-top: 1px">
			<tr>
				<td width="100%" valign="top">
					<div style="padding: 0px 0px 0px 0px">
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							style="margin-bottom: 1px">
							<tr>
								<td id="tabsJ">
									<ul>
										<li>
											<a
												href="javascript:showDCTabInfo('${contextPath}/jteap/jhtj/zyzb/zyzbAction!getLeftViewAction.do?zbfl=1','0')"><span>电量指标</span>
											</a>
										</li>
										<li>
											<a
												href="javascript:showDCTabInfo('${contextPath}/jteap/jhtj/zyzb/zyzbAction!getLeftViewAction.do?zbfl=2','1')"><span>燃料指标</span>
											</a>
										</li>
										<li>
											<a
												href="javascript:showDCTabInfo('${contextPath}/jteap/jhtj/zyzb/zyzbAction!getLeftViewAction.do?zbfl=3','2')"><span>可靠性指标</span>
											</a>
										</li>
									</ul>
								</td>
								
								<td height="100%">
									<iframe name="_main" id="_main" src="" width="100%"
										height="100%" frameborder="1" marginwidth="0"
										marginheight="0" scrolling="no"></iframe>
										<script type="text/javascript">changeSize();</script>
								</td>
							</tr>
						</table>
					</div>
				</td>
				
			</tr>
		</table>
  </body>
</html>
