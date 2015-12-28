<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<meta http-equiv="x-ua-compatible" content="ie=7" />
		<%@ include file="indexScript.jsp"%>
		<%
			String moduleId = (String) request.getParameter("moduleId");
			String childId = (String) request.getParameter("childId");
		%>
		<title>班组管理子系统首页</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>	
		<%
			ModuleManager moduleManager = (ModuleManager) SpringContextUtil.getBean("moduleManager");
			Collection<Module> roots = moduleManager.findModule(moduleId);
			Object[] obj = roots.toArray();
			Module module = null;
		%>
		<script type="text/javascript">
		
			function goToPage(bs,childId,classId){
				parent.window.document.getElementById("portal").src = link10+"?bs="+bs+"&moduleId=40288ada29a155520129a15c7754001a";
//				parent.window.document.getElementById("portal").src = "module.jsp?moduleId="+moduleId+"&childId="+childId;
		//		parent.window.document.getElementById(0).className = "";
		//		parent.window.document.getElementById(classId).className = "current";
			}
		</script> 
	</head>

	<body>
		<table width="100%" style="height:100%" border="0" cellspacing="0" cellpadding="0"
			class="box1-tab">
			<tr>
				<td class="box1-top-left">&nbsp; 
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
				<td class="box1-main" valign="top">
					<div class="sub-title">
						<table width="100%"  border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="sub-left-ico">
									<img src="images/ico-sub-title-01.gif" width="16" height="16" />
								</td>
								<td class="sub-title-txt">
									<%
										Module mod = (Module)moduleManager.get(moduleId);
									%>
									<%=mod.getResName() %>导航地图
								</td>
								<td class="sub-right-ico">
									<!-- 
									<a href="#" onclick="toMaxWindow();"><img src="images/ico-maximize.gif" id="imageId" width="16"
											height="16" /> </a>
									-->
								</td>
							</tr>
						</table>
					</div>
					<!-- height表示快捷菜单高度 -->
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="1" class="TabTb" >
						<tr>
							<td class="LabelBodyTbTitle">
								发电部
							</td>
							<td class="TabTbEntry">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="1" height="100%">
									<tr>
										<td class="LabelBodyTbEntry2">
											<div class="chromestyle" id="chromemenu">
												<ul>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-YIZ','2','2')">一值</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-ERZ','2','2')">二值</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-SANZ','2','2')">三值</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-SIZ','2','2')">四值</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-WUZ','2','2')">五值</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-HUASB','2','2')">化水班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('FDB-HUAYB','2','2')">化验班</a>
													</li>
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="LabelBodyTbTitle">
								检修部
							</td>
							<td  class="TabTbEntry">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="1" height="100%">
									<tr>
										<td class="LabelBodyTbEntry">
											<div class="chromestyle" id="chromemenu">
												<ul>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-REGYB','2','2')">热工一班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-REGERB','2','2')">热工二班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-DIANSB','2','2')">电试班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-DIANQJXB','2','2')">电气检修班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-GUOLJXB','2','2')">锅炉检修班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-QIJJXB','2','2')">汽机检修班</a>
													</li>
													<!-- 
													<li>
														<a href="javascript:void(0);" onclick="goToPage('JXB-TONGXB','2','2')">通讯班</a>
													</li> -->
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="LabelBodyTbTitle">
								燃运部
							</td>
							<td  class="TabTbEntry">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="1" height="100%">
									<tr>
										<td class="LabelBodyTbEntry2">
											<div class="chromestyle" id="chromemenu">
												<ul>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('RYB-QUYB','2','2')">取样班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('RYB-ZHIYB','2','2')">制样班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('RYB-MEIGHYB','2','2')">煤管化验班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('RYB-ZONGHB','2','2')">综合班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('RYB-DIANKJXB','2','2')">电控检修班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('RYB-JIQJXB','2','2')">机械检修班</a>
													</li>
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="LabelBodyTbTitle">
								关联公司
							</td>
							<td  class="TabTbEntry">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="1" height="100%">
									<tr>
										<td class="LabelBodyTbEntry">
											<div class="chromestyle" id="chromemenu">
												<ul>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('GLGS-QICB','2','2')">汽车班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('GLGS-JINGGB','2','2')">经管办</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('GLGS-ZONGHB','2','2')">综合班</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('GLGS-ZHAODS','2','2')">招待所</a>
													</li>
													<li>
														<a href="javascript:void(0);" onclick="goToPage('GLGS-QIXB','2','2')">汽修班</a>
													</li>
												</ul>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
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
	</body>
</html>
