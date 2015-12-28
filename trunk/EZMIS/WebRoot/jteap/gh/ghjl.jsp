<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工会简介</title>
<link href="style/style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="1024" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td class="mainBG">
    
<!--  start of top --> 
     <div class="top">
     
      <div id="top-menu">
         <ul>
          <li><a href="${contextPath}/GhAction!getHdjcObjects.do" class="scrollover">首页</a></li>
          <li><a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=ghjj" class="scrollover">工会简介</a></li>
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


   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tab">
     <tr>
       <td class="Tab-topLEFT">&nbsp;</td>
       <td class="Tab-topCENTER">
        <div class="TAB-topTitle">工会简介</div>
       </td>
       <td class="Tab-topRIGHT">&nbsp;</td>
     </tr>
     <tr>
       <td class="Tab-left">&nbsp;</td>
       <td class="Tab-main">
       
<!--  start of sub-txt -->
     <div class="ArticleMain">
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
			            		<a href="${contextPath}/jteap/doclib/DoclibAction!downAttachAction.do?attachid=${file.id}">${file.name }</a>
				            </td>
				          </tr>
				  </c:forEach>
				 </table>
		          
		    </fieldset>
		</c:if>
	  </div>
     </div>
<!--  end of sub-txt -->     
   
       
       </td>
       <td class="Tab-right">&nbsp;</td>
     </tr>
     <tr>
       <td class="Tab-bomLEFT">&nbsp;</td>
       <td class="Tab-bomCENTER">&nbsp;</td>
       <td class="Tab-bomRIGHT">&nbsp;</td>
     </tr>
   </table>



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
