<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String result = request.getAttribute("result").toString();
	if("true".equals(result)){
%>
	<script type="text/javascript">
		alert("导入流程成功");
		window.dialogArguments.opener.isImport = true;
	</script>	
<% 		
	}else if("false".equals(result)){
%>
	<script type="text/javascript">
		alert("错误文件,或文件已损坏,导入流程失败");
		window.dialogArguments.opener.isImport = false;
	</script>	
<% 			
	}
 %>
 
 <script type="text/javascript">
	window.close();
</script>	

