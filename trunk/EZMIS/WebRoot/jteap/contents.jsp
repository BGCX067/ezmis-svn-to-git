<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.index.manager.PreferenceManager"%>
<%@page import="com.jteap.index.model.Preference"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ include file="/inc/import.jsp"%>
	<%@ include file="/inc/meta.jsp"%>
	<%@ include file="indexScript.jsp"%>
	<head>
		<title>内容发布</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="styles.css"></link>
		<script type="text/javascript">
			var config = ""; 
		
		<%
			String personId = (String)session.getAttribute(Constants.SESSION_CURRENT_PERSONID);
			PreferenceManager preferenceManager = (PreferenceManager)SpringContextUtil.getBean("preferenceManager");
			Preference preference = preferenceManager.findConfig(personId);
			if(preference != null){
		%>
				config = "<%=preference.getConfig()%>";
		<%		
			}
		 %>
		</script>
	</head>
	
	<body>
		<div class="move" id="root_row">
			<div class="title">
				<div class="title_close">
					<!--<img src="close.png"/>-->
				</div>
				<div class="title_edit"></div>
				<div class="title_lock">
					锁定
				</div>
				<div class="title_reduce">
					缩小
				</div>
				<div class="title_a">
					&nbsp;
				</div>
			</div>
			<div class="content" id="contentDiv">

			</div>
		</div>

		<div class="root" id="root_id">
			<div class="root-main">
				<div class="mis_left left">

					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="box1-tab">
						<tr>
							<td class="box1-top-left">
								&nbsp;
							</td>
							<td class="box1-top-center">
								&nbsp;
							</td>
							<td class="box1-top-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-left">
								&nbsp;
							</td>
							<td class="box1-main">

								<div class="cell_left left"
									style="overflow-x: hidden; overflow-y: auto;" id="m_1">
									<div class="line">
										&nbsp;
									</div>
								</div>

							</td>
							<td class="box1-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-bom-left">
								&nbsp;
							</td>
							<td class="box1-bom-center">
								&nbsp;
							</td>
							<td class="box1-bom-right">
								&nbsp;
							</td>
						</tr>
					</table>

				</div>

				<div class="mis_center left">

					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="box1-tab">
						<tr>
							<td class="box1-top-left">
								&nbsp;
							</td>
							<td class="box1-top-center">
								&nbsp;
							</td>
							<td class="box1-top-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-left">
								&nbsp;
							</td>
							<td class="box1-main">

								<div class="cell_center left"
									style="overflow-x: hidden; overflow-y: auto;" id="m_2">
									<div class="line">
										&nbsp;
									</div>
								</div>

							</td>
							<td class="box1-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-bom-left">
								&nbsp;
							</td>
							<td class="box1-bom-center">
								&nbsp;
							</td>
							<td class="box1-bom-right">
								&nbsp;
							</td>
						</tr>
					</table>

				</div>

				<div class="mis_right left">

					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="box1-tab">
						<tr>
							<td class="box1-top-left">
								&nbsp;
							</td>
							<td class="box1-top-center">
								&nbsp;
							</td>
							<td class="box1-top-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-left">
								&nbsp;
							</td>
							<td class="box1-main">


								<div class="cell_right left"
									style="overflow-x: hidden; overflow-y: auto;" id="m_3">
									<div class="line">
										&nbsp;
									</div>
								</div>

							</td>
							<td class="box1-right">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="box1-bom-left">
								&nbsp;
							</td>
							<td class="box1-bom-center">
								&nbsp;
							</td>
							<td class="box1-bom-right">
								&nbsp;
							</td>
						</tr>
					</table>

				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	var headerDivHeight = parent.document.getElementById("headerDiv").clientHeight;
	var bomDivHieght = parent.document.getElementById("bomDiv").clientHeight;
	var root_height = (window.screen.availHeight-headerDivHeight-bomDivHieght-60);//-140
	document.getElementById("root_id").style.height = root_height+"px";
	document.getElementById("m_1").style.height = root_height+"px";
	document.getElementById("m_2").style.height = root_height+"px";
	document.getElementById("m_3").style.height = root_height+"px";
</script>

<!-- 加载脚本库  开始  -->
<%@ include file="/inc/ext-all.jsp" %>
<script type="text/javascript" src="contents.js" charset="UTF-8"></script>
<!-- 入口程序 -->
<script type="text/javascript">
Ext.onReady(function(){		
	Ext.QuickTips.init();
});
</script>
<!-- 加载脚本库  结束 -->
