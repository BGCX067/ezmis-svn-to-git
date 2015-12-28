<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.doclib.manager.DoclibManager"%>
<%@page import="com.jteap.system.doclib.manager.DoclibCatalogManager"%>
<%@page import="com.jteap.core.dao.support.Page"%>
<%@page import="com.jteap.core.utils.StringUtil"%>
<%@page import="java.util.List"%>
<%@ include file="/inc/import.jsp"%>
<%@ include file="/inc/meta.jsp" %>
<%@ include file="/inc/ext-all.jsp" %>
<%
	response.setHeader("Expires", "0");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("iso-8859-1");

	String catalogCode = request.getParameter("catalogCode");
	String title = "";
	String url="";
	if (catalogCode.equals("ghwj")) {
		title = "工会文件";
	} else if (catalogCode.equals("zzjs")) {
		title = "组织建设";
	} else if (catalogCode.equals("dsdt")) {
		title = "导师带徒";
	} else if (catalogCode.equals("hyfc")) {
		title = "会员风采";
	}else if(catalogCode.equals("gz110zl")){
		title = "工作110";
	}else if(catalogCode.equals("mzgl")){
		title = "民主管理";
	}else if(catalogCode.equals("ghjh")){
		title = "工会计划";
	}else if(catalogCode.equals("ghzd")){
		title = "工会制度";
	}else if(catalogCode.equals("ghjj")){
		title = "工会简介";
	}

	String sPageStart = request.getParameter("start");
	String sPageSize = request.getParameter("size");
	if (StringUtil.isEmpty(sPageStart)) {
		sPageStart = "0";
	}
	
	if (StringUtil.isEmpty(sPageSize)) {
		sPageSize = "40";
	}

	int pStart = Integer.parseInt(sPageStart);
	if(pStart<0){
		pStart=0;
	}
	int pSize = Integer.parseInt(sPageSize);

	DoclibManager contentService = (DoclibManager) SpringContextUtil
			.getBean("doclibManager");

	String hql = "from Doclib where doclibCatalog.catalogCode=? order by createdt desc";
	url="getDoclibContent";
	boolean falgjy=false;
	if("hdjy".equals(catalogCode)){
		hql = "from Hdjy where 'hdjy'=? order by scsj desc";
		title="活动剪影";
		url="getHdjyObject";
		falgjy=true;
	}
	if("tzzl".equals(catalogCode)){
		hql = "from Tzzl t where 'tzzl'=? order by t.scsj desc";
		title="通知专栏";
		url="getTzzlObject";
		falgjy=true;
	}
	request.setAttribute("flag",falgjy);
	Page page2 = contentService.pagedQuery(pStart, pSize, hql,catalogCode);
	List dataList = (List) page2.getResult();
	int nextStart = 0;
	int prevStart = 0;
	prevStart=page2.getStartOfPage(Integer.parseInt(page2.getCurrentPageNo()+ "")-1, page2.getPageSize());
	
	nextStart=page2.getStartOfPage(Integer.parseInt(page2.getCurrentPageNo()+ "")+1, page2.getPageSize() );


//	int lastStart = page2.getStartOfPage(Integer.parseInt(page2.getCurrentPageNo()+ ""), page2.getPageSize());
	int lastStart =page2.getPageSize()*((int)page2.getTotalPageCount()-1);
	int curPage=Integer.parseInt(page2.getCurrentPageNo()+ "");
	int totalPage=Integer.parseInt(page2.getTotalPageCount()+ "");
	long total = 1;
	if(page2.getTotalPageCount()!=0){
		total=page2.getTotalPageCount();
	} 
%>
<c:set var="totalPage" value="<%=totalPage%>"></c:set>
<c:set var="curPage" value="<%=curPage%>"></c:set>
<c:set var="lastStart" value="<%=lastStart%>"></c:set>
<c:set var="prevStart" value="<%=prevStart%>"></c:set>
<c:set var="nextStart" value="<%=nextStart%>"></c:set>
<c:set var="list" value="<%=dataList%>"></c:set>
<c:set var="title" value="<%=title%>"></c:set>
<c:set var="url" value="<%=url%>"></c:set>
<c:set var="catalogCode" value="<%=catalogCode%>"></c:set>
<c:set var="pager" value="<%=page2%>"></c:set>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>文章列表</title>
		<link href="style/style.css" rel="stylesheet" type="text/css" />
	</head>
<script type="text/javascript">
	function goTo(){
		var i=document.getElementById('select').value;
		var sPageSize=<%=sPageSize%>;
		i=(i-1)*sPageSize;
		document.location='wzList.jsp?catalogCode=${catalogCode}&start='+i;
	}
	
	function check(start){
		if(start>=<%=page2.getTotalCount()%>){
			window.location.reload();
		}
	}
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
	function gotoPages(id){
		Ext.Ajax.request({
				url:"${contextPath}/jteap/doclib/DoclibAction!getUrlAction.do",
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
			 	method:'POST',
			 	params: {docid:id}//Ext.util.JSON.encode(selections.keys)		
			});
		}	
</script>	
	<body  >
		<table width="1024" border="0" align="center" cellpadding="0"
			cellspacing="0">
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
					<table width="888" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="tab" height="700px;">
									<tr>
										<td class="Tab-topLEFT">
											&nbsp;
										</td>
										<td class="Tab-topCENTER">
											<div class="TAB-topTitle">${title }专栏
											</div>
										</td>
										<td class="Tab-topRIGHT">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="Tab-left">
											&nbsp;
										</td>
										<td class="Tab-main">
											<!--  start of txt -->
											<table width="100%" border="0" cellpadding="0"
												cellspacing="0">
												<c:forEach items="${list}" var="list">
													<tr>
														<td class="NewsIco">
															&nbsp;
														</td>
														<td class="NewsTxt">
															<c:if test="${flag==true}"><a href="${contextPath}/GhAction!${url}.do?id=${list.id}"  style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width: 656">${list.title}</a></c:if>
															<c:if test="${flag==false}"><a href="javascript:function(){return;}" onclick="gotoPages('${list.id}');" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width: 656">${list.title}</a></c:if>
														</td>
														<td class="NewsTime">
															 ${list.createDtStr}
														</td>
													</tr>
												</c:forEach>
											</table>
											<!--  end of txt -->

										</td>
										<td class="Tab-right">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="Tab-bomLEFT">
											&nbsp;
										</td>
										<td class="Tab-bomCENTER">
											&nbsp;
										</td>
										<td class="Tab-bomRIGHT">
											&nbsp;
										</td>
									</tr>
								</table>
									
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class="Footer-txt2">
												总共&nbsp;
												<span><%=total%></span>&nbsp;页
											&nbsp;&nbsp; 当前第&nbsp;
											<span><%=page2.getCurrentPageNo()%></span>&nbsp;/&nbsp;
											<span><%=total%></span>&nbsp;页
										</td>
										<td class="Footer-txt" align="right">
											<img src="style/Footer-ico-1.gif" width="11" height="12" />
											<a href="wzList.jsp?catalogCode=${catalogCode}&start=0">首页</a>
											<img src="style/Footer-ico-2.gif" width="11" height="12" />
											<a href="wzList.jsp?catalogCode=${catalogCode}&start=${prevStart }">上一页</a>
											<img src="style/Footer-ico-3.gif" width="11" height="12" />
											<a href="wzList.jsp?catalogCode=${catalogCode}&start=${nextStart }" onclick="check('${nextStart }');">下一页</a>
											<img src="style/Footer-ico-4.gif" width="11" height="12" />
											<a href="wzList.jsp?catalogCode=${catalogCode}&start=${lastStart }">尾页</a> &nbsp;&nbsp;&nbsp;&nbsp;
											<label>
												<select name="select" id="select" onchange="goTo();">
												<c:forEach begin="1" end="${totalPage}" var="i">
													<option value="${i}" <c:if test="${i==curPage}">selected</c:if>>跳至${i}</option>
												</c:forEach>
													</select>
												</label>
												&nbsp;&nbsp;
											</td>
										</tr>
									</table>
									
										</td>
						</tr>
					</table>
					<!--  end of main -->
					<div class="bom">
						&nbsp;
					</div>
				</td>
			</tr>
		</table>

	</body>
</html>