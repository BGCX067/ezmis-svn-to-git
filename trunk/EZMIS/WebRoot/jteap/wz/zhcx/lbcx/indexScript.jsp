<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>	
	//请求指标树形结构
	var link1 = "${contextPath}/jteap/wz/wzlb/WzlbAction!showTreeAction.do";
	
	var link2 = "${contextPath}/jteap/wz/wzda/WzdaAction!showListAction.do";
	//删除物资档案
	var link3 = "${contextPath}/jteap/wz/wzda/WzdaAction!removeAction.do";

	//批量修改物资档案类别
	var link4 = "${contextPath}/jteap/wz/wzda/WzdaAction!updateWzlbAction.do";

	//批量修改物资库位
	var link5 = "${contextPath}/jteap/wz/wzda/WzdaAction!updateKWAction.do";

	//移动组织节点
	var link6="${contextPath}/jteap/wz/wzlb/WzlbAction!simpleDragMoveNodeAction.do";
	
	//导出选项卡
	var link7="${contextPath}/jteap/wz/wzda/WzdaAction!exportWzCardAction.do";

	//批量修改特殊分类
	var link8="${contextPath}/jteap/wz/wzda/WzdaAction!updateTsflAction.do";

	//批量修改库存量
	var link9="${contextPath}/jteap/wz/wzda/WzdaAction!updateDqkcAction.do";
	
	//仓库列表
	var link10="${contextPath}/jteap/wz/ckgl/CkglAction!showListAction.do";
</script>