<%@ page language="java" pageEncoding="UTF-8"%>
<script>
	//显示checkbox的角色树
	var link1="${contextPath}/jteap/system/role/RoleAction!showRoleTreeForCheckAction.do";
	
	//显示checkbox群组的树
	var link2="${contextPath}//jteap/system/group/GroupAction!showGroupTreeForCheckAction.do";
	
	//人员选择页面的url
	var link3="${contextPath}/jteap/system/person/personSelect.jsp" ;

	var link4="${contextPath}/jteap/wfengine/workflow/NodeOperationAction!getNodeOperation.do" ;
	
	//查询可编辑域
	var link5 = "${contextPath}/jteap/wfengine/cform/CFormAction!findEditableInputsAction.do";
	
	// 显示处理页面表单树
	var link6 = "${contextPath}/jteap/form/eform/EFormCatalogAction!showCatalogAndItemTreeForCheckByFormIdAction.do" ;
</script>