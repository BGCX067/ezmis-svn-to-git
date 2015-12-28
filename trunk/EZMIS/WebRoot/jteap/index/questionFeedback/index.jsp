<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="indexScript.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="index.css" type="text/css"></link>
	</head>

	<body scroll="no" id="index">
		<%@ include file="/inc/ext-all.jsp"%>
		<script type="text/javascript" src="${contextPath}/script/date.js"></script>
		<div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						MIS系统反馈问题
					</div>
					<div class="pop-but">
						<div class="pop-butMain">
							<input type="button" id="save" value="添加问题"
								onclick="btnAdd_Click();" class="pop-but01">
							<input type="button" id="colse" value="修改问题"
								onclick="btnModify_Click();" class="pop-but01">
						</div>
					</div>
					<table align="center" height="60" width="90%" class="LabelBodyTb">
						<tr>
							<td class="POPtabTbEntry2">
								<div id="divGrid"></div>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>

		<!-- 加载等待图标 开始 -->
		<div id="loading-mask" style=""></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="${contextPath}/resources/extanim32.gif" width="32"
					height="32" style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div>
		<!-- 加载等待图标 结束 -->

		<!-- 加载脚本库  开始  -->
		<script type="text/javascript" src="script/RightGrid.js"
			charset="UTF-8"></script>
		<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>

		<!-- 入口程序 -->
		<script type="text/javascript">
		Ext.onReady(function(){		
			Ext.QuickTips.init();
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});
    </script>
		<!-- 加载脚本库  结束 -->

	</body>
</html>
