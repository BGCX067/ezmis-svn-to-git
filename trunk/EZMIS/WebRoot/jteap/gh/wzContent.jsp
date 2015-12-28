<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.form.eform.manager.EFormFjManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@ include file="/inc/import.jsp"%>
<%@ include file="/inc/meta.jsp" %>
<%@ include file="/inc/ext-all.jsp" %>
<html>
  <head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文章正文</title>
<link href="${contextPath}/jteap/gh/style/style.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
	//返回文档页面
	function gotoGhPages(){
		Ext.Ajax.request({
			url:"${contextPath}/jteap/doclib/DoclibAction!getGhjjUrlAction.do",
			success:function(ajax){
				var responseText=ajax.responseText;	
				var responseObj = Ext.decode(responseText);
				if (responseObj.success) {
					var url=contextPath+"/jteap/system/doclib/generate/"+responseObj.url;
					window.parent.window.location= url;
				}
			},
			failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST'
		});
	}
</script>
<body>
<table width="1024" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td class="mainBG">
    
<!--  start of top --> 
     <div class="top">
     
      <div id="top-menu">
         <ul>
           <li><a href="${contextPath}/GhAction!getGhObjectsAction.do" class="scrollover">首页</a></li>
           <li><a href="javascript:function(){return;}" onclick="gotoGhPages();" class="scrollover">工会简介</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=ghwj" class="scrollover">工会文件</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=zzjs" class="scrollover">组织建设</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=mzgl" class="scrollover">民主管理</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=hyfc" class="scrollover">会员风采</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=hdjy" class="scrollover">活动剪影</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=dsdt" class="scrollover">导师带徒</a></li>
           <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=gz110zl" class="scrollover">工会110</a></li>
         </ul> 
	    </div>
     </div>
<!--  end of top --> 

<!--  start of main -->
<table width="888" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>


<div class="subMAIN" style="height: 700px;">
        <!--  start of Title -->
         <div class="RoundedCorner" >
          ${Doclib.title }
         </div>
        <!--  end of Title -->
       
       <div class="ArticleInfo">发布时间&nbsp;:&nbsp;&nbsp;
  		 <c:choose>
           	<c:when test="${requestScope.model eq 'hdjy'}">
           		 ${Doclib.SCSJ}
           	</c:when>
           	<c:otherwise>
           		${Doclib.createDtStr }
           	</c:otherwise>
           </c:choose>
       </div>
     
<!--  start of sub-txt -->
     <div class="ArticleMain" id="textdiv">
     <c:if test="${requestScope.model eq 'hdjy'}">
        	<center><img src="${contextPath}/GhAction!getHdImg.do?id=${Doclib.BTTP}" width="149" height="103" /><br></center>
     </c:if>
       ${Doclib.content }


	  <div style="margin:10px; padding:10px">
		<c:if test="${!empty requestScope.list}">
			<fieldset>
		      <legend class="ArticleMainIco"> &nbsp;相关附件&nbsp; </legend>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
				  <c:forEach items="${requestScope.list}" var="file">
				  		<tr>
				            <td class="NewsIcoTwo">&nbsp;</td>
				            <td class="NewsTxtTwo">
				            <c:choose>
				            	<c:when test="${requestScope.model eq 'hdjy'}">
				            		<a href="${contextPath}/jteap/form/EFormFjAction!downloadFj.do?id=${file.id}">${file.name }</a>
				            	</c:when>
				            	<c:otherwise>
				            		<a href="${contextPath}/jteap/doclib/DoclibAction!downAttachAction.do?attachid=${file.id}">${file.name }</a>
				            	</c:otherwise>
				            </c:choose>
				            </td>
				          </tr>
				  </c:forEach>
				 </table>
		          
		    </fieldset>
		</c:if>
	  </div>
     </div>
<!--  end of sub-txt -->     
</div>



   </td>
  </tr>
</table>
<!--  end of main -->

<div class="bom">&nbsp;</div>
     
    </td>
  </tr>
</table>



</body>
</html>
