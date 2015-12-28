<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
	//组织树
	var link1="${contextPath}/jteap/system/group/GroupAction!showTreeAction.do";
	
	//列表
	var link4="${contextPath}/jteap/wz/gqjtz/GqjtzAction!showListAction.do?queryParamsSql=obj.grgyqf='1'";
	
	//删除
	var link5="${contextPath}/jteap/wz/gqjtz/GqjtzAction!removeAction.do";
	
	//添加修改功能
	var link6="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YGQJTZ_GR";
	
	//请求组织树形结构
	var link9="${contextPath}//jteap/system/group/GroupAction!showTreeAction.do";
	
	//取得所有用户列表的时候
	var link10="${contextPath}/jteap/system/person/PersonAction!showListAction.do";
	
	//游离用户列表
	var link11="${contextPath}/jteap/system/person/PersonAction!showDissciationPersonListAction.do";
	
	//请求人员列表数据
	var link12="${contextPath}/jteap/system/person/P2GAction!showListAction.do";
	
	//人员选择页面的url
	var link13="${contextPath}/jteap/system/cms/dy/personSelect.jsp" ;
	
	// 角色过滤树
	var link14="${contextPath}/jteap/system/person/P2RoleAction!showListAction.do";
	
	var link15="${contextPath}/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_YGQJTZ_GR_VIEW";
	
</script>