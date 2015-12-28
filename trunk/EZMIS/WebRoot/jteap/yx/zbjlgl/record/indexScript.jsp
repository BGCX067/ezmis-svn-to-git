<!-- 当前模块所有具有权限的操作 -->
<jteap:operations/>

<script>

	//根据 登录人岗位 查询值班[记录]
	var link1 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!showRecordByTypeAction.do";
	
	//保存修改 值班记录
	var link2 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!saveOrUpdateJlAction.do";
	
	//删除
	var link3 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!removeAction.do";
	
	//根据 登录人岗位 查询通知信息
	var link4 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!showTzByTypeAction.do";
	
	//保存或修改 值班通知
	var link5 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!saveOrUpdateTzAction.do";
	
	//领导审阅 密码校对
	var link6 = "${contextPath}/jteap/system/person/PersonAction!validateNameAndPasswordAction.do";
	
	//保存领导审阅记录
	var link7 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!saveShenYueAction.do";
	
	//根据 交班、接班班次  获取  交班、接班值别
	var link8 = "${contextPath}/jteap/yx/jjb/PaiBanAction!findByBcAction.do";
	
	//根据值班班次、值班时间获取审阅信息
	var link9 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!findShenYueAction.do";
	
	//根据formSn、时间、班次查询单条记录
	var link10 = "${contextPath}/jteap/yx/zbjl/JbyxfsAction!findOneBySnSjBcAction.do";
	
	//解除锁定
	var link11 = "${contextPath}/jteap/yx/zbjl/ZhiBanJiLuAction!unLockOPAction.do";
	
	//解除锁定
	var link12 = "${contextPath}/jteap/yx/jjb/JiaoJieBanAction!getJieBanRenAction.do";
	
</script>