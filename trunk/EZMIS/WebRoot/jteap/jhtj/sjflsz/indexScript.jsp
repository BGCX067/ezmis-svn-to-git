 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<!-- å½åæ¨¡åææå·ææéçæä½ -->

<jteap:operations/>
<jteap:dict catalog="FLBS,JZMC,SJLX"/>
<script>	
	var link1="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!showTreeAction.do";
	
	var link2="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!deleteNodeAction.do";
	
	var link3="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!saveUpdateAction.do";

	var link4="${contextPath}/jteap/jhtj/sjflsz/tjItemKindKeyAction!showListAction.do";
	var link5="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!removeAction.do";
	//增加，修改分类
	var link6="${contextPath}/jteap/jhtj/sjflsz/sjflszForm.jsp";
	var link7="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!findDictAction.do";
	var link8="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!showUpdateAction.do";
	//增加修改关键字信息
	var link9="${contextPath}/jteap/jhtj/sjflsz/keyForm.jsp";
	//查找关键字是否唯一
	var link10="${contextPath}/jteap/jhtj/sjflsz/tjItemKindKeyAction!validateNameUniqueAction.do";
	var link11="${contextPath}/jteap/jhtj/sjflsz/tjItemKindKeyAction!saveUpdateAction.do";
	var link12="${contextPath}/jteap/jhtj/sjflsz/tjItemKindKeyAction!showUpdateAction.do";
	var link13="${contextPath}/jteap/jhtj/sjflsz/tjItemKindKeyAction!removeAction.do";
	var link14="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!ghostAction.do";
	var link15="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!validateNameUniqueAction.do";
	
</script>