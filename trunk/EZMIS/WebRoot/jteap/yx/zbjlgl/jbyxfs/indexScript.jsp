<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>
	
	//查询所有交班运行方式
	var link1 = "${contextPath}/jteap/yx/zbjl/JbyxfsAction!findByFormSnAction.do";
	
	//根据formSn、时间、班次查询单条记录
	var link2 = "${contextPath}/jteap/yx/zbjl/JbyxfsAction!findOneBySnSjBcAction.do";
	
	//获取最后一条记录
	var link3 = "${contextPath}/jteap/yx/zbjl/JbyxfsAction!findLasJtAction.do";
	
</script>