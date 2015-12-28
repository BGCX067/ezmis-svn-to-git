 
<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
 
<%
	boolean debugger=false;
	if(debugger){
		%>
			<%@ include file="/inc/ext-base_debug.jsp" %>
			<%@ include file="/inc/ext-all_debug.jsp" %>
			<%@ include file="/inc/ext-widgets_debug.jsp" %>
		<%
	}else{
		%>
			<script type="text/javascript" src="${contextPath}/script/adapter/ext/ext-base.js"></script>
			<script type="text/javascript" src="${contextPath}/script/ext-all.js"></script> 
		<%
	}
%>



<script type="text/javascript" src="${contextPath}/script/build/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="${contextPath}/script/common.js"></script>

<script>
Ext.BLANK_IMAGE_URL='${contextPath}/resources/s.gif';
</script>


