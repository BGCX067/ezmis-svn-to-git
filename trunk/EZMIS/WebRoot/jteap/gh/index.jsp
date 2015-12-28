<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/inc/import.jsp"%>
<%@ include file="/inc/meta.jsp" %>
<%@ include file="/inc/ext-all.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>工会管理</title>
		<link href="${contextPath}/jteap/gh/style/style.css" rel="stylesheet"
			type="text/css" />
		<link rel="stylesheet" href="${contextPath}/jteap/gh/style/skin.css" type="text/css"></link>
		<script type="text/javascript" src="${contextPath}/jteap/gh/js/jquery-1.4.2.min.js"></script>
 		<script type="text/javascript" src="${contextPath}/jteap/gh/js/jquery.js"></script>
 		<script type="text/javascript" src="${contextPath}/jteap/gh/js/jquery_24.js"></script>
		<script type="text/javascript" src="${contextPath}/jteap/gh/js/jquery.jcarousel.js"></script>
		<script type="text/javascript" src="${contextPath}/jteap/gh/js/jquery_pic.js"></script>
		<style type="text/css">
			#scrollDivPic{width:263px;height:203px;overflow:hidden; text-align:center;}
			#scrollDivPic li{list-style-type:none;}
		</style>
		<script language="javascript">
			//通知 和简介 公共滚动方法
		    function moveText(demo1,demo2,demo){
			   	var speed=20; 
				demo2.innerHTML=demo1.innerHTML    
				function Marquee(){    
					if(demo2.offsetTop-demo.scrollTop<=0)    
						demo.scrollTop-=demo1.offsetHeight    
					else{    
						demo.scrollTop++    
					}    
				}    
				var MyMar=setInterval(Marquee,speed)    
				demo.onmouseover=function() {clearInterval(MyMar)}    
				demo.onmouseout=function() {MyMar=setInterval(Marquee,speed)} 
		    }	
		    /*
	    	function te(){
	    		var tzzl = document.getElementById('tzzls');
			 	var tzzl1 = document.getElementById('tzzl1');
	    		function gg(){
	    			//tzzl1.offsetTop++;
		    		tzzl.scrollTop++;
		    		asdf
		    		if(tzzl.scrollTop==tzzl.scrollHeight){
		    			tzzl.scrollTop = 0;
		    			//tzzl1.offsetTop = 0;
		    		}
		    		setTimeout(gg,10);
	    		}
	    		gg();
	    	}
	    	 function moveTexts(demo1,demo2,demo,falg){
			   	var speed=10; 
				demo2.innerHTML=demo1.innerHTML    
				function Marquee(){    
					if(demo2.offsetTop-demo.scrollTop<=0)    
						demo.scrollTop-=demo1.offsetHeight    
					else{    
						demo.scrollTop=demo.scrollTop+1;
					}    
				}
				//if('in'==falg){
				 	var MyMar=setInterval(Marquee,speed);
				//}
				//if('out'==falg){
				//	speed=5;
				//	var MyMar=setInterval(Marquee,speed);
				//	setInterval(function(){clearInterval(MyMar)},200);
					
				//}    
				    
				setInterval(function(){clearInterval(MyMar)},1900);
				
				//demo.onmouseover=function() {clearInterval(MyMar)}    
				//demo.onmouseout=function() {MyMar=setInterval(Marquee,speed)} 
		    }	
		     function moveTextOut(demo1,demo2,demo){
			   	var speed=5; 
				demo2.innerHTML=demo1.innerHTML    
				function Marquee(){    
					if(demo2.offsetTop-demo.scrollTop<=0)    
						demo.scrollTop-=demo1.offsetHeight    
					else{    
						demo.scrollTop=demo.scrollTop+3;
					}    
				}    
				var MyMar=setInterval(Marquee,speed)    
				setInterval(function(){clearInterval(MyMar)},200);
				
				//demo.onmouseover=function() {clearInterval(MyMar)}    
				//demo.onmouseout=function() {MyMar=setInterval(Marquee,speed)} 
		    }	*/
		    //连接到文档页面
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
			//初始化滚动 滚动剪影风采
 			function mar(){
				var speed=20; 
				var Photo = document.getElementById("Photo");
		        var marquePic1 =document.getElementById("marquePic1");
		        var marquePic2 = document.getElementById("marquePic2");
				marquePic2.innerHTML=marquePic1.innerHTML 
				function Marquee(){ 
					if(marquePic2.offsetWidth-Photo.scrollLeft<=0){ 
						Photo.scrollLeft-=marquePic1.offsetWidth;
					}else{ 
						Photo.scrollLeft++ ;
						if(Photo.scrollLeft==170){
							Photo.scrollLef=0;
						}
					} 
				}
				var MyMar=setInterval(Marquee,speed) 
				Photo.onmouseover=function() {clearInterval(MyMar);} 
				Photo.onmouseout=function() {MyMar=setInterval(Marquee,speed)} 
				//////////工会简介滚动
			 	 var ghjj = document.getElementById('ghjjs');
			 	 var ghjj1 = document.getElementById('ghjj1');
			 	 var ghjj2 = document.getElementById('ghjj2');
			 	 moveText(ghjj1,ghjj2,ghjj);
			 	
			}
			/*
			//工会简介滚动
			function ghgo(falg){
				var ghjj = document.getElementById('ghjjs');
			 	var ghjj1 = document.getElementById('ghjj1');
			 	var ghjj2 = document.getElementById('ghjj2');
			 	moveTexts(ghjj1,ghjj2,ghjj,falg);
			}
			 */
			//友情链接
			function gotoPage(url){
				window.open(url);
			}
			function zUrl(url){
				window.parent.window.location='${contextPath}'+url;
		  	}
		  	
		</script>
		
	</head>

	<body onload="mar()">
		<table width="1024" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td class="mainBG">

			        <table width="1024" align="center" border="0" cellspacing="0" cellpadding="0">
			        <tr>
			          <td>
						  <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="1024" height="211">
						    <param name="movie" value="${contextPath}/jteap/gh/style/top.swf" />
						    <param name="quality" value="high" />
						    <embed src="${contextPath}/jteap/gh/style/top.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="1024" height="211"></embed>
						  </object>          
			          </td>
			        </tr>
			      </table>
					<!--  start of top -->
					<div class="top">

						<div id="top-menu">
							<ul>
								<li>
									<a href="${contextPath}/GhAction!getGhObjectsAction.do"
										class="scrollover">首页</a>
								</li>
								<li>
									<a href="javascript:function(){return;}" onclick="gotoGhPages();" class="scrollover">工会简介</a>
								</li>
								<li>
									<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=ghwj"
										class="scrollover">工会文件</a>
								</li>
								<li>
									<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=zzjs"
										class="scrollover">组织建设</a>
								</li>
								<li>
									<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=mzgl"
										class="scrollover">民主管理</a>
								</li>
								<li>
									<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=hyfc"
										class="scrollover">会员风采</a>
								</li>
								<li>
									<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=hdjy"
										class="scrollover">活动剪影</a>
								</li>
								<li>
									<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=dsdt"
										class="scrollover">导师带徒</a>
								</li>
								<li>
									<a
										href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=gz110zl"
										class="scrollover">工会110</a>
								</li>
							</ul>
						</div>

					</div>
					<!--  end of top -->

					<!--  start of main -->
					<table width="930" align="center" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="313">
								<!-- 工会简介 start-->
								<div class="L1-Title">
									<div class="L1-Ttiel-Txt">
										工会简介
									</div>
									<div class="L1-Ttiel-More">
										<a href="#" onclick="gotoPages('${ghjj.id}');">详细</a>
									</div>
								</div>
								<div class="L1-Mian MianGH" >
									 <div id="ghjjs" style="overflow:hidden; width:300px; height:200px;">
									 	<div id="ghjj1" >
											<c:if test="${empty ghjj}">
												暂无简介
											</c:if>
											<c:if test="${not empty ghjj}">
												&nbsp;&nbsp;&nbsp;&nbsp;${ghjj.content}
											</c:if>
									 	</div>
									 	<div id="ghjj2"></div>
									 </div>
								</div>
								<!-- 工会简介 end-->
							</td>
							<td width="12">
								&nbsp;
							</td>
							<td width="313">
								<!-- 工会文件 start-->
								<div class="L1-Title">
									<div class="L1-Ttiel-Txt">
										工会文件
									</div>
									<div class="L1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=ghwj">更多</a>
									</div>
								</div>
								<div class="L1-Mian Mian-1">
									<!--  start of txt -->
										 <IFRAME name="iframe1" ID=IFrame1 width="100%" FRAMEBORDER='yes' height='210px' SCROLLING=NO SRC="GhAction!getDoclibCatalogTop5.do?catalogCode=ghwj"></IFRAME>
									<!--  end of txt -->
								</div>
								<!-- 工会文件 end-->
							</td>
							<td rowspan="5" width="14">
								&nbsp;
							</td>
							<td width="278">
								<!-- 工会通知 start-->
								<div class="R1-Title">
									<div class="R1-Ttiel-Txt">
										工会通知
									</div>
									<div class="R1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=tzzl">更多</a>
									</div>
								</div>
								<div class="R1-Mian Mian-TZ">
									<!--  start of txt -->
									<div id="scrollDivPic">
											<ul>
												<c:if test="${not empty tzzlList}">
													<c:forEach items="${tzzlList}" var="tzzl">
														<li style="text-align:center;">
															<table width="95%" align="center" border="0" cellspacing="0" cellpadding="0">
																<tr>
																	<td class="LMAIco">
																		&nbsp;
																	</td>
																	<td class="LMATxt">
																		<a href="javascript:function(){return;}" onclick="zUrl('/EZMIS/GhAction!getTzzlObject.do?id=${tzzl.id}');">${tzzl.content}</a>&nbsp;
																		<span>${tzzl.createDtStrNy}</span>
																	</td>
																</tr>
															</table>
														</li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									<!--  end of txt -->
								</div>
								<div class="R1-Bom"></div>
								<!-- 工会通知 end-->
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<!-- AD start-->
								<div class="ADmain">
									<div id="AD-Box">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=gz110zl"><IMG title="" src="${contextPath}/jteap/gh/style/AD-Box.gif"> </a>
									</div>
									<img src="${contextPath}/jteap/gh/style/AD.jpg" width="638" height="82" />
								</div>
								<!-- AD end-->
							</td>
							<td rowspan="4">
								<!-- 导师带徒弟专栏 start-->
								<div class="R1-Title">
									<div class="R1-Ttiel-Txt">
										导师带徒专栏
									</div>
									<div class="R1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=dsdt">更多</a>
									</div>
								</div>
								<div class="R1-Mian Mian-3">
									<!--  start of txt -->
										<IFRAME name="iframe1" ID=IFrame1 width="96%" FRAMEBORDER=0 SCROLLING=NO SRC="GhAction!getDoclibCatalogTop5.do?catalogCode=dsdt"></IFRAME>
									<!--  end of txt -->
								</div>
								<div class="R1-Bom"></div>
								<!-- 导师带徒弟专栏 end-->


								<!-- 工会网络 start-->
								<div class="R2-Title">
									<div class="R2-Ttiel-Txt">
										工会网络
									</div>
								</div>
								<div class="R2-Mian">
									<!--  start of link -->

									<label>
										<select name="select" id="select" class="InputLink" onchange="gotoPage(this.value)" >
											<option>
												省级工会链接
											</option>
											<option value = 'http://10.229.41.8'>
												 鄂州电厂厂网
											</option>
										</select>
									</label>

									<label>
										<select name="select" id="select" class="InputLink" onchange="gotoPage(this.value)" >
											<option>
												市级工会链接
											</option>
											<option value = 'http://10.0.1.1'>
												 湖北能源集团公司
											</option>
										</select>
									</label>

									<!--  end of link -->
								</div>
								<!-- 工会网络 end-->

							</td>
						</tr>
						<tr>
							<td>
								<!-- 会员风采 start-->
								<div class="L2-Title">
									<div class="L2-Ttiel-Txt">
										会员风采
									</div>
									<div class="L1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=hyfc">更多</a>
									</div>
								</div>
								<div class="L1-Mian Mian-2">
									<!--  start of txt -->
										<IFRAME name="iframe1" ID=IFrame1 width="100%" height='108px' FRAMEBORDER=0 SCROLLING=NO SRC="GhAction!getDoclibCatalogTop5.do?catalogCode=hyfc"></IFRAME>
									<!--  end of txt -->
								</div>
								<!-- 会员风采 end-->
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								<!-- 民主管理 start-->
								<div class="L2-Title">
									<div class="L2-Ttiel-Txt">
										民主管理
									</div>
									<div class="L1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=mzgl">更多</a>
									</div>
								</div>
								<div class="L1-Mian Mian-2">
									<!--  start of txt -->
										<IFRAME name="iframe1" ID=IFrame1 width="100%" height='108px' FRAMEBORDER=0 SCROLLING=NO SRC="GhAction!getDoclibCatalogTop5.do?catalogCode=mzgl"></IFRAME>
									<!--  end of txt -->
								</div>
								<!-- 民主管理 end-->
							</td>
						</tr>
						<tr>
							<td>
								<!-- 工会计划 start-->
								<div class="L2-Title">
									<div class="L2-Ttiel-Txt">
										工会计划
									</div>
									<div class="L1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=ghjh">更多</a>
									</div>
								</div>
								<div class="L1-Mian Mian-2">
									<!--  start of txt -->
										<IFRAME name="iframe1" ID=IFrame1 width="100%" height='108px' FRAMEBORDER=0 SCROLLING=NO SRC="GhAction!getDoclibCatalogTop5.do?catalogCode=ghjh"></IFRAME>
									<!--  end of txt -->
								</div>
								<!-- 工会计划 end-->
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								<!-- 工会制度 start-->
								<div class="L2-Title">
									<div class="L2-Ttiel-Txt">
										工会制度
									</div>
									<div class="L1-Ttiel-More">
										<a href="${contextPath}/jteap/gh/wzList.jsp?catalogCode=ghzd">更多</a>
									</div>
								</div>
								<div class="L1-Mian Mian-2">
									<!--  start of txt -->
										<IFRAME name="iframe1" ID=IFrame1 width="100%" height='108px' FRAMEBORDER=0 SCROLLING=NO SRC="GhAction!getDoclibCatalogTop5.do?catalogCode=ghzd"></IFRAME>
									<!--  end of txt -->
								</div>
								<!-- 工会制度 end-->
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<div class="PhotoDIV">
									<div id="Photo">
										<c:if test="${!empty list}">
											<table border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td valign="top" bgcolor="ffffff" id="marquePic1"
														class="scroll_div">
														<table border="0" cellspacing="0" cellpadding="0">
															<tr>
																<c:forEach items="${list}" var="list">
																	<td>
																		<table border="0" cellspacing="0" cellpadding="0">
																			<tr>
																				<td>
																					<a
																						href="${contextPath}/GhAction!getHdjyObject.do?id=${list.id}"><img
																							src="${contextPath}/GhAction!getHdImg.do?id=${list.BTTP}"
																							width="149" height="103" title='${list.BT}' />
																					</a>
																				</td>
																			</tr>
																			<tr>
																				<td class="PhotoTxt" id="title">
																					<a title='${list.BT}'
																						style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width: 150"
																						href="${contextPath}/GhAction!getHdjyObject.do?id=${list.id}">${list.BT}</a>
																				</td>
																			</tr>
																		</table>
																	</td>
																</c:forEach>
															</tr>
														</table>
													</td>
													<td id="marquePic2" valign="top"></td>
												</tr>
											</table>
										</c:if>
									</div>
								</div>
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
