 
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.core.Constants"%>

<jteap:operations/>
<jteap:dict catalog="YWLX,SJLX2,QSFS"/>
<script>	
	var link1="${contextPath}/jteap/jhtj/sjflsz/tjItemKindAction!showTreeAction.do";
	
	var link4="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!showListAction.do";
	
	var link5="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!removeAction.do";
	
	var link6="${contextPath}/jteap/jhtj/sjxxxdy/sjxxxdyForm.jsp";
	
	var link7="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!showUpdateAction.do";
	
	var link8="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!saveUpdateAction.do";
	
	//验证信息项编码是否唯一
	var link9="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!validateNameUniqueAction.do";
	//取应用接口dll
	var link10="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!findDllNameAllAction.do";
	//跳到增加或者修改页面
	var link11="${contextPath}/jteap/jhtj/sjxxxdy/templateFrame.jsp";
	//创建表结构
	var link12="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!createTableAction.do";
	//显示其他系统主页
	var link13="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!showOtherSystemPageAction.do";
	//显示数据源树
	var link14="${contextPath}/jteap/jhtj/sjydy/tjAppIOAction!showTreeAction.do";
	//显示其他系统的form信息
	var link15="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!showOtherSystemFormAction.do";
	//显示计算的主页
	var link16="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!showComputePageAction.do";
	//测试计算项
	var link17="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!testCexpAction.do";
	//取得数据源ID
	var link18="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!findTjAppioByVnameAction.do";
	//查找数据项中文名
	var link19="${contextPath}/jteap/jhtj/sjxxxdy/tjInfoItemAction!getCfnameByFnameAction.do";
	//导入excel
	var link20="${contextPath}/jteap/jhtj/sjxxxdy/importExcel.jsp";
</script>