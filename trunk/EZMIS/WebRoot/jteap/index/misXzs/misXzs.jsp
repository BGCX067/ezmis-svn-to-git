<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
		<%@ include file="/inc/import.jsp"%>
		<%@ include file="indexScript.jsp"%>
		<script type="text/javascript">
			function setTab(name, cursel, n) {
				var nowIndex = null;
				for (i = 1; i <= n; i++) {
					var menu = document.getElementById(name + i);
					var con = document.getElementById("con_" + name + "_" + i);
					menu.className = i == cursel ? "hover" : "";
					if (nowIndex != null) {
						continue;
					} else {
						nowIndex = i == cursel ? i : null;
					}
				}
				var iframe = document.getElementById('ifra');
				if (nowIndex != '' || nowIndex != null) {
					var url = eval("link" + nowIndex);
					if (iframe.src != url) {
						iframe.src = url;
					}
				}
			}
		</script>
		<link href="style-aide/aide.css" rel="stylesheet" type="text/css" />
	</head>

	<body style="border: 0px; margin:0 0 0 0;"  leftmargin="0" topmargin="0" >
		<div style="width: 100%; height: 100%; ">
			<div id="lib_Tab1">

				<div class="lib_Menubox lib_tabborder">
					<ul>
						<li id="one1" onClick="setTab('one',1,2)" class="hover">
							实时信息
						</li>
						<li id="one2" onClick="setTab('one',2,2)">
							待办事项
						</li>
					</ul>
				</div>

				<div class="lib-line"></div>

				<div class="lib_Contentbox lib_tabborder">
					<iframe id="ifra"
						src="${contextPath}/jteap/index/nowInfo/index.jsp"
						style="width: 100%; height: 100%;"></iframe>
				</div>

			</div>
		</div>

	</body>
</html>
