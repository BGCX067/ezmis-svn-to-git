<!-- 当前模块所有具有权限的操作 -->
<jteap:operations />
<script>
	// 备品备件信息显示列表
	var link1="${contextPath}/jteap/jx/bpbjgl/BpbjxxAction!showListAction.do";

	// 专业树显示
	var link2="${contextPath}/jteap/jx/bpbjgl/BpbjZyflAction!showTreeAction.do";
	
	// 添加专业分类
	var link3="${contextPath}/jteap/jx/bpbjgl/BpbjZyflAction!addZyflAction.do";
	
	// 删除专业分类
	var link4="${contextPath}/jteap/jx/bpbjgl/BpbjZyflAction!deleteNodeAction.do";
	
	// 备品备件信息显示
	var link5="${contextPath}/jteap/jx/bpbjgl/BpbjxxAction!showUpdateAction.do";
	
	// 备品备件信息保存
	var link6="${contextPath}/jteap/jx/bpbjgl/BpbjxxAction!saveOrUpdateAction.do";
	
	// 显示备品备件信息编辑窗口
	var link7="${contextPath}/jteap/jx/bpbjgl/bpbjxx/bpbjxxEditeForm.jsp";
	
	// 删除备品备件信息
	var link8="${contextPath}/jteap/jx/bpbjgl/BpbjxxAction!removeAction.do";
	
	// 显示备品备件信息查看窗口
	var link9="${contextPath}/jteap/jx/bpbjgl/bpbjxx/bpbjxxViewForm.jsp";
</script>