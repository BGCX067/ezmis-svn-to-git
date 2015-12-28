
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

.mod-frame-link {
	height: 16px;
	float: left;
	position: fixed;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-right-style: solid;
	border-bottom-style: solid;
	border-right-color: #93b9d9;
	border-bottom-color: #93b9d9;
	padding: 3px 5px 0px 5px;
	color: #1c488f;
	background-image: url(icon/mod-frame-bg.gif);
	background-repeat: repeat-x;
	background-position: left top;
	font-size: 13px;
	text-decoration: none;
}

.mod-frame-link a:link,.mod-frame-link a:visited {
	color: #1c488f;
	text-decoration: none;
}

.mod-frame-link a:hover {
	color: #ff812a;
	text-decoration: none;
}

.mod-frame-hover {
	height: 16px;
	float: left;
	position: fixed;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-right-style: solid;
	border-bottom-style: solid;
	border-right-color: #93b9d9;
	border-bottom-color: #FFFFFF;
	padding: 3px 8px 0px 8px;
	color: #000000;
	background-image: url(icon/mod-frame-hbg.gif);
	background-repeat: repeat-x;
	background-position: left top;
	font-size: 13px;
	text-decoration: none;
}

.mod-frame-hover a:link,.mod-frame-hover a:visited {
	color: #000000;
	text-decoration: none;
}

.mod-frame-hover a:hover {
	color: #000000;
	text-decoration: none;
	font-weight: bold;
}

.frame {
	width: 100%;
	background-image: url(icon/mod-frame-bg.gif);
	background-repeat: repeat-x;
	background-position: left top;
}

.LM01Txt {
	height: 21px;
	margin-right: 5px;
	font-size: 13px;
	color: #1C488F;
	text-decoration: none;
	padding-left: 2px;
	padding-top: 3px;
	text-align: left;
}

.arrowlistmenu {
	width: 100%;
}

.arrowlistmenu a {
	color: #1c488f; /*custom bullet list image*/
	display: block;
	padding: 2px 0;
	padding-left: 19px; /*link text is indented 19px*/
	text-decoration: none;
	font-weight: bold;
	border-bottom: 1px solid #D3E3F1;
	font-size: 90%;
}

.arrowlistmenu a:visited {
	color: #1c488f;
}

.arrowlistmenu a:hover { /*hover state CSS*/
	color: #F26422;
	background-color: #F3F3F3;
}
</style>
			
			<script>
		  function  categoryTab(n) {
		      for(i=0;i<tabs.length;i++){
		        var tab = tabs[i];
		        var categoryTab = categoryTabs[i];
		        if(i==n){
		          tab.className= "mod-frame-hover";
		          categoryTab.style.display = "block";
		        }else{
		          tab.className= "mod-frame-link";
		          categoryTab.style.display = "none";
		        }
		      }
		  }
			</script>
  </head>
 
  <body scroll="no">
	<div>
		<div class="frame">
			<div id='tabs' class="mod-frame-hover" onclick="categoryTab(0);">
				<a href="javascript:">同期对比</a>
			</div>
			<div id='tabs' class="mod-frame-link" onclick="categoryTab(1);">
				<a href="javascript:">机组对比</a>
			</div>
			<div id='tabs' class="mod-frame-link" onclick="categoryTab(2);">
				<a href="javascript:">指标关联</a>
			</div>
		</div>
		<span id='categoryTabs'>
			<div class="arrowlistmenu">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach items="${list}" var="zbjd">
						<c:if test="${zbjd.fxfs==1}">
							<tr>
								<td class="LM01Txt">
									<a href="${contextPath}/jteap/jhtj/zyzb/zyzbAction!getZbjdViewIndexAction.do?id=${zbjd.id}" target="rightIframe">${zbjd.name}</a>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</div> </span>
		<span id='categoryTabs' style="display: none">
			<div class="arrowlistmenu">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach items="${list}" var="zbjd">
						<c:if test="${zbjd.fxfs==2}">
							<tr>
								<td class="LM01Txt">
									<a href="${contextPath}/jteap/jhtj/zyzb/zyzbAction!getZbjdViewIndexAction.do?id=${zbjd.id}" target="rightIframe">${zbjd.name}</a>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</div> </span>
		<span id='categoryTabs' style="display: none">
			<div class="arrowlistmenu">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach items="${list}" var="zbjd">
						<c:if test="${zbjd.fxfs==3}">
							<tr>
								<td class="LM01Txt">
									<a href="${contextPath}/jteap/jhtj/zyzb/zyzbAction!getZbjdViewIndexAction.do?id=${zbjd.id}" target="rightIframe">${zbjd.name}</a>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</div>
		 </span>
	</div>
  </body>
</html>
