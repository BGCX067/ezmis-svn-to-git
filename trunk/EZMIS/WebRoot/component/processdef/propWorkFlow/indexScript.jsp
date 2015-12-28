<%@ page language="java" pageEncoding="UTF-8"%>


<script>
	//列出CFORM表单分类和表单项的树
	var link1 = "${contextPath}/jteap/form/cform/CFormCatalogAction!showCatalogAndItemTreeForCheckAction.do" ; 
	
	//查询CFORM可编辑域
	var link2 = "${contextPath}/jteap/form/cform/CFormAction!findEditableInputsAction.do";
	
	//列出EFORM表单分类和表单项的树
	var link3 = "${contextPath}/jteap/form/eform/EFormCatalogAction!showCatalogAndItemTreeForCheckAction.do" ; 
	
	//查询EFORM可编辑域
	var link4 = "${contextPath}/jteap/form/eform/EFormAction!findEditableInputsAction.do";
</script>