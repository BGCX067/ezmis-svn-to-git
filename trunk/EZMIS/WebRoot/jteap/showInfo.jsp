<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>My JSP 'showInfo.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>

	<body>
		<span
			style="margin: 0 auto; ont-weight: thin; color: #15428B; white-space: nowrap; cursor: pointer; padding: 4px 0; font-family: '黑体';">正在开发中...</span>
		<br>
		<span
			style="margin: 0 auto; ont-weight: thin; color: #15428B; white-space: nowrap; cursor: pointer; padding: 4px 0; font-family: '黑体';">当前时间：</span>
		<div id="time"
			style="margin: 0 auto; ont-weight: thin; color: #15428B; white-space: nowrap; cursor: pointer; padding: 4px 0; font-family: '黑体';">
			<script type="text/javascript">
				document.getElementById('time').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());
				setInterval("document.getElementById('time').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);
			</script>
		</div>
	</body>
</html>
