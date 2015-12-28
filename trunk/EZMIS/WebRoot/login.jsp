<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<title>登陆</title>
		<link href="login.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="${contextPath}/script/prototype.js"></script>
		<script>
	
	    //验证
		function validate(){
			if($("username").value.length==0){
				alert("用户名为必填项");
				$("username").focus();
				return false;
			}else{
				if($("userPwd").value.length==0){
					alert("密码必填项");
					$("userPwd").focus();
					return false;
				}else{
					return true;
				}
			}
		}
		//登录
		function login(){
			if(validate()){
				$("LoginAction").submit();
			}
		}
		//设置可信站点
		//var SiteName="192.168.10.52";
		//SetTrustSite(SiteName);
		function SetTrustSite(){
		var WshShell=new ActiveXObject("WScript.Shell");
		//添加信任站点ip
		WshShell.RegWrite("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range100\\","");
		WshShell.RegWrite("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Domains\\10.229.41.139\\http",2,"REG_DWORD");
		WshShell.RegWrite("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range100\\:Range","10.229.41.139");
		alert("信任站点设置成功！");
		}
	</script>
	</head>

	<body>
		<table width="1000" border="0" align="center" valign="middle"
			cellpadding="0" cellspacing="0" class="content">
			<tr>
				<td valign="top">
					<div class="LoginBG">
						<div class="LoginRight">
							<div class="LoginTxt03"></div>
							<s:form action="LoginAction" name="loginForm" method="POST">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td></td>
										<td colspan="2">
											<s:actionmessage cssStyle="color:red" />
										</td>
									</tr>
									<tr>
										<td class="LoginTxt04" width="75">
											用户名：
										</td>
										<td>
											<input type="text" id="username" name="username" value=""
												onkeydown="if(event.keyCode==13) $('userPwd').focus();"
												size="12" class="LoginInput01"
												onMouseOver="this.className='LoginInput02'"
												onmouseout="this.className='LoginInput01'" />
										</td>
									</tr>
									<tr>
										<td class="LoginTxt04">
											密码：
										</td>
										<td>
											<input type="password" id="userPwd" name="userPwd" value=""
												class="LoginInput01"
												onMouseOver="this.className='LoginInput02'"
												onmouseout="this.className='LoginInput01'"
												onkeydown="if(event.keyCode==13) login();" />
										</td>
									</tr>
								</table>
								<div style="margin-top: 15px">
									<table width="160" border="0" align="right" cellpadding="0"
										cellspacing="0">
										<tr>
											<td align="right">
												<input class="LoginButton" type="submit" name="button"
													value="登录">
											</td>
										</tr>
									</table>
									
								</div>
							</s:form>
						</div>
						<div class="LoginLink">
							&nbsp;
							<a href="#" target="_self"
								title="设置可信站点步骤:&#10①打开网页浏览器的【工具】-【Internet选项】;&#10②进入到【安全】选项,选择【可信站点】,点击【站点】;&#10③将http://10.229.41.60添加到可信站点;(注：若提示不能添加,请将【对该区域中的所有站点要求服务器验证】去掉)&#10④将可信站点的安全级别设为低;">如何设置可信站点？</a>
							&nbsp;
							<a href="IE7.exe" target="_blank">运行MIS系统请安装 <span
								style="color: #F00">IE7</span> 点击下载</a> &nbsp;
							<a href="MIS小助手.exe" target="_blank"><span
								style="color: #F00">MIS</span>小助手点击下载</a>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>

<script type="text/javascript">
	//页面加载后,选中用户名输入框,便于用户输入
	window.onload = function(){
		document.getElementById("username").focus();
	}
		
</script>
