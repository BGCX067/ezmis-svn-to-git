<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.person.model.Person"%>
	<%@ include file="/inc/import.jsp"%>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/UniqueTextField.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/LabelPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/TitlePanel.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="ChangePasswordForm.js" charset="UTF-8"></script>
	<script type="text/javascript" src="index.js" charset="UTF-8"></script>
	<!-- 加载脚本库  结束 -->
		<title>EZMIS首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
	</head>
	
	
<html>	
	<body scroll="no">
	
		<div class="mis-top-bg" id="headerDiv">
			<div class="mis-top-logo">
				<div class="mis-top-but">
					<table border="0" align="right" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<img src="images/premium_support.png" width="20" height="20" />
							</td>
							<td class="mis-top-txt">
								<font style="font-size:13px;color:#FFF;font-weight:bold;text-decoration:none;">MIS维护电话：4125</font>
							</td>
							<td>
								<img src="images/ico-MisTop-0.gif" width="20" height="20" />
							</td>
							<td class="mis-top-txt">
								<a href="javascript:void(0);" onclick='questionFeedback();'>MIS系统问题反馈</a>
							</td>
							<td>
								<img src="images/ico-MisTop-1.gif" width="20" height="20" />
							</td>
							<td class="mis-top-txt">
								<a href="javascript:void(0);" onclick='setMyQuick()'>设置我的快捷</a>
							</td>
							<td>
								<img src="images/ico-MisTop-2.gif" width="20" height="20" />
							</td>
							<td class="mis-top-txt">
								<a href="#" onclick="changeCurrentPwd()">修改密码</a>
							</td>
							<td>
								<img src="images/ico-MisTop-3.gif" width="20" height="20" />
							</td>
							<td class="mis-top-txt">
								<a href="#" onclick='logout_Click();'>注销</a>
							</td>
						</tr>
					</table>
				</div>

				<div class="mis-top-nav">
					<div id="headerDiv1">
						<div class="blk_18" id="container">
							<div style="width: 25px; height: 20px; float: left;">
								<a class="LeftBotton" id="leftButton"
									onclick="ISL_GoUp_1();ISL_StopUp_1();"
									href="javascript:void(0);" target="_self"></a>
							</div>
							<div class="pcont" id="ISL_Cont_1">
								<div class="ScrCont">
									<div id="List1_1">
										<%
											ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
											PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
											Collection<Module> roots = null;
											if(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString() == "1"){
												Person person = new Person();
												person.setId(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString());
												person.setUserLoginName(session.getAttribute("SESSION_CURRENT_PERSON_LOGINNAME").toString());
												roots = moduleManager.findModuleByParentWithPerm(person,null);
											}else{
												roots = moduleManager.findModuleByParentWithPerm(personManager.get(session.getAttribute("SESSION_CURRENT_PERSON_ID").toString()),null);
											}
											String icon = "";
											Object[] obj = roots.toArray();
											for (int i = 0; i < obj.length; i++) {
												Module module = (Module) obj[i];
												if (module.getIcon() == null) {
													icon = "resources/icon/ico_1.png";
												} else {
													icon = module.getIcon();
												}
										%>
										<a class="pl" href="<%=module.getLink()%>?moduleId=<%=module.getId()%>">
											<img src="${contextPath}/<%=icon%>" alt="" width="50" height="50" /> <div id='"moduleName<%=i%>"'></div></a>
										<script type="text/javascript">
											var moduleName = "<%=module.getResName()%>";
											document.getElementById('"moduleName<%=i%>"').innerText = moduleName;
										</script>
										<%
											if (i == 0) {
										%>
										<%
											}
										%>
										<%
											}
										%>
										<input type="hidden" id="picNum" value="<%=roots.size()%>" />
										<!-- piclist begin -->
										<!-- piclist end -->
									</div>
									<div id="List2_1"></div>
								</div>
							</div>
							<div style="width: 25px; height: 20px; float: left;">
								<a class="RightBotton" id="rightButton"
									onclick="ISL_GoDown_1();ISL_StopDown_1();"
									href="javascript:void(0);" target="_self"></a>
							</div>
						</div>
						<!-- picrotate_left end -->
					</div>
				</div>
			</div>
		</div>
		<div id="iframeDiv">
			<IFRAME id='contentIframe' name='contentIframe' scrolling='no' src="contents.jsp" style='width: 100%; height: 100%' frameborder='no'></IFRAME>
		</div>

		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="bom" id="bomDiv">
			<tr>
				<td class="bom-left">
					登录信息 : <%=curPersonName%>  所在部门:<%=curPersonGroupNames%>
				</td>
				<td class="bom-center">
					版权所有 湖北能源集团鄂州发电有限公司
				</td>
				<td class="bom-right">
					<a href="#" onclick='logout_Click();'>注销</a>
				</td>
			</tr>
		</table>

	</body>
</html>

<script type="text/javascript">
	//判断控制图片左右滚动的按钮是否显示
	var screenWidth = document.body.clientWidth;
	var pictureNum = document.getElementById("picNum").value;
	if(pictureNum*106>document.body.clientWidth){         //默认是107
		document.getElementById("leftButton").style.display = "block";
		document.getElementById("rightButton").style.display = "block";
	}else{
		document.getElementById("leftButton").style.display = "none";
		document.getElementById("rightButton").style.display = "none";
	}
	
	var temp = null;
	if(window.onresize && typeof window.onresize == "function"){
		temp = window.onresize;
	}
	
	window.onresize=function(){
		var blk_18_width = document.body.clientWidth+"px";
		var blk_18_pcont_width = (document.body.clientWidth - 50)+"px";
		document.getElementById("container").style.width = blk_18_width;
		document.getElementById("ISL_Cont_1").style.width = blk_18_pcont_width;
		
		var picSum = document.getElementById("picNum").value;  //图片总数
		var displatPicSum = Math.round((document.body.clientWidth/107));    //可视图片数
		if(GetObj('ISL_Cont_1').scrollLeft>=107*(picSum-displatPicSum)){
			MoveLock_1=false;
		}else{
			GetObj('ISL_Cont_1').scrollLeft+=Space_1;
		}
	}
	
	window.onload = function(){
		var headerDivHeight = document.getElementById("headerDiv").clientHeight;
		var bomDivHieght = document.getElementById("bomDiv").clientHeight;
		var root_height = (window.screen.availHeight-headerDivHeight-bomDivHieght-30);//-140
		document.getElementById("iframeDiv").style.height = root_height;
		var blk_18_width = document.body.clientWidth+"px";
		var blk_18_pcont_width = (document.body.clientWidth - 50)+"px";
		document.getElementById("container").style.width = blk_18_width;
		document.getElementById("ISL_Cont_1").style.width = blk_18_pcont_width;
	}
	
	function goToPage(moduleId,moduleName,moduleLink){
		var screen_Width = window.screen.availWidth;
		var screen_Height = window.screen.availHeight;
		window.location.href = moduleLink+"?moduleId="+moduleId;
	}
	
	//问题反馈
	function questionFeedback(){
		var url = contextPath + '/jteap/index/questionFeedback/index.jsp';
		showIFModule(url,"MIS系统问题反馈","true",600,450,{});
	}
	
	//设置我的快捷
	function setMyQuick(){
		var url = contextPath + '/jteap/index/myQuick/setMyQuickCkTree.jsp';
		showIFModule(url,"设置我的快捷","true",317,400,{});
		window.frames["contentIframe"].frames["myQuickIF"].location.reload();
	}
	
	//修改密码
	function changeCurrentPwd(){
		<%if("root".equals(curPersonLoginName)){
		%>
			alert("平台管理员不在这里修改密码");
			return;
		<%	
		}%>
		var pwdForm = new ChangePasswordFormWindow();
		pwdForm.show();
	}
	
</script>