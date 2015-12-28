<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	//表定义schema节点
	var link1="${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!showTreeSchemaAction.do";
	
	//请求表定义树形结构
	var link2="${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!showTreeAction.do";
	
	//请求字段定义列表
	var link3="${contextPath}/jteap/hb3e/zbdy/DefColumnInfoAction!showZbdyTreeGridAction.do";
	
	//保存表
	var link4="${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!saveUpdateAction.do";
	//删除表
	var link5="${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!deleteTableAction.do";	
	
	//修改表
	var link6="${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!showUpdateAction.do";	
	
	
	//修改字段
	var link7="${contextPath}/jteap/hb3e/zbdy/DefColumnInfoAction!showUpdateAction.do";
	
	//保存字段
	var link8="${contextPath}/jteap/hb3e/zbdy/DefColumnInfoAction!saveUpdateAction.do";	
	
	//保存字段
	var link15="${contextPath}/jteap/hb3e/zbdy/DefColumnInfoAction!resetSortNo.do";	
	
	//删除字段
	var link9="${contextPath}/jteap/hb3e/zbdy/DefColumnInfoAction!deleteNodeAction.do";	
	
	//取得所有物理表
	var link10="${contextPath}/jteap/hb3e/zbdy/PhysicTableAction!showTreeAction.do";	
	
	
	//判断是否存在指定的定义表
	var link11="${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!isExistDefTableAction.do";
	
	//导入物理表
	var link12="${contextPath}/jteap/hb3e/zbdy/PhysicTableAction!importPhysicTableAction.do";
	
	//重建物理表
	var link13="${contextPath}/jteap/hb3e/zbdy/PhysicTableAction!rebuildPhysicTableAction.do";

	//移动数据
	var link14="${contextPath}/jteap/hb3e/zbdy/DefColumnInfoAction!moveFieldAction.do";
	
	var link16= "${contextPath}/jteap/hb3e/zbdy/DefTableInfoAction!showTreeDataSourceAction.do";
	
	var link17="${contextPath}/jteap/hb3e/zbdy/DefDataSourceAction!showListAction.do";
	
	//请求指标树形结构
	var link18 = "${contextPath}/jteap/wz/ckgl/CkglAction!showTreeAction.do";
	
	var link19 = "${contextPath}/jteap/wz/kwwh/KwwhAction!getKWNodeAction.do";
	
</script>