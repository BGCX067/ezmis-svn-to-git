<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>
<script>
	var formSn = "TB_FORM_TZZL";
	//显示自定义表单列表
	var link1="${contextPath}/jteap/bz/bzinfo/BzInfoAction!showEFormRecListAction.do?formSn="+formSn+"&dqbz=${param.dqbz}";	
</script>
