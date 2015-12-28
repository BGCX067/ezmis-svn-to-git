<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.yx.dqgzgl.manager.DqgzHandleManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.dqgzgl.model.DqgzHandle"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	<style>
		td {
			width: 100px;
		}
	</style>
  </head>
 
  <body scroll="no" id="index">
	 <!-- 加载等待图标 开始 -->
   	 <!-- 加载等待图标 结束 -->
   	 
   	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	
	<script type="text/javascript" src="${contextPath}/script/ext-extend/form/ComboTree.js"></script>
		
	<!-- 入口程序 -->
    <!-- 加载脚本库  结束 -->
    
    <div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						定期工作处理
					</div>
					
					<%
						//标识是否已完成 完成则只能查询此页面
						String wancheng = request.getParameter("wancheng");
						String isDisabled = "";
						String isReadonly = "";
						if(wancheng != null){
							isDisabled = "disabled='disabled'";
							isReadonly = "readonly='readonly'";
						}
						
						String id = request.getParameter("id");
						DqgzHandleManager dqgzHandleManager = (DqgzHandleManager)SpringContextUtil.getBean("dqgzHandleManager");
					 	DqgzHandle dqgzHandle = dqgzHandleManager.get(id);
					 	String ckBaiB = "";
					 	String ckZhongB = "";
					 	String ckYeB = "";
					 	String[] bcArray = dqgzHandle.getBc().split(",");
					 	for(int i=0; i<bcArray.length; i++){
							if("夜班".equals(bcArray[i])){
								ckYeB = "checked";
							}else if("白班".equals(bcArray[i])){
								ckBaiB = "checked";
							}else if("中班".equals(bcArray[i])){
								ckZhongB = "checked";
							} 
					 	}
					 	
					 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					 	String curName=(String)session.getAttribute(Constants.SESSION_CURRENT_PERSON_NAME);
						String curDate = dateFormat.format(new Date());
					 %>
					
				    <table align="center" height="82%" width="97.5%" class="LabelBodyTb">
				    	<tr>
				    		<td class="POPtab-Title">负责部门</td>
				    		<td class="POPtabTbEntry2">
				    			<input type="text" style="width: 130px;" readonly="readonly" value="<%=dqgzHandle.getFzbm()%>">
				    		</td>
				    		<td class="POPtab-Title">负责岗位</td>
				    		<td class="POPtabTbEntry2">
				    			<input type="text" style="width: 130px;"  readonly="readonly" value="<%=dqgzHandle.getFzgw()%>">
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">工作规律</td>
				    		<td class="POPtabTbEntry2">
				    			<input id="gzgl" type="text" style="width: 130px;"  readonly="readonly" value="<%=dqgzHandle.getGzgl()%>">
				    		</td>
				    		<td class="POPtab-Title">定期工作专业</td>
				    		<td class="POPtabTbEntry2">
				    			<input type="text" style="width: 130px;"  readonly="readonly" value="<%=dqgzHandle.getDqgzzy()%>">
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">班次</td>
				    		<td colspan="3" class="POPtabTbEntry2" style="width: 455px;">
				    			&nbsp;&nbsp; 夜班
								<input disabled="disabled" style="width: auto" type="checkbox" <%=ckYeB%> >
								&nbsp; 白班
								<input disabled="disabled" style="width: auto" type="checkbox" <%=ckBaiB%> >
								&nbsp; 中班
								<input disabled="disabled" style="width: auto" type="checkbox" <%=ckZhongB%> >
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">实验及切换项目</td>
				    		<td colspan="3" class="POPtabTbEntry2" style="width: 455px;">
				    			<textarea readonly="readonly" rows="3" cols="1"  style="width: 95%;"><%=dqgzHandle.getDqgzNr()%></textarea>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">处理时间</td>
				    		<td class="POPtabTbEntry2">
				    			<input id="chuliDt" readonly="readonly" style="width: 130px;" type="text"  value="<%=curDate%>">
				    		</td>
				    		<td class="POPtab-Title">处理人</td>
				    		<td class="POPtabTbEntry2">
				    			<input id="chuliRen" readonly="readonly" style="width: 130px;" type="text"  value="<%=curName%>">
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="POPtab-Title">处理内容</td>
				    		<td colspan="3" class="POPtabTbEntry2" style="width: 455px;">
				    			<textarea id="chuliNr" rows="3" cols="1" style="width: 95%;" <%=isReadonly %>><%=dqgzHandle.getChuliNr()==null?"":dqgzHandle.getChuliNr() %></textarea>
				    		</td>
				    	</tr>
				    </table>
					
				    <div class="pop-but">
						<div class="pop-butMain">
							<input type="button" value=" 完 成 " onclick="save();" class="pop-but01" <%=isDisabled %>>
			    			<input type="button" value=" 取 消 " onclick="window.close();" class="pop-but01">
						</div>
					</div>	
    			</div>
    		</div>
    	</div>
    	
    	<script type="text/javascript">
    		//弹出窗口自动调节大小
    		window.dialogWidth = "600px";
    		window.dialogHeight = "400px";
    		
    		//弹出窗口居中
    		var screen_Width = window.screen.availWidth;
			var screen_Height = window.screen.availHeight;
    		window.dialogLeft = (screen_Width - 500)/2;
    		window.dialogTop = (screen_Height - 400)/2;
    		
    		var dict_dqgzgl=$dictList("dqgzgl");
    		for (var i = 0; i < dict_dqgzgl.length; i++) {
				if(dict_dqgzgl[i].value == gzgl.value){
					gzgl.value = dict_dqgzgl[i].key;
					break;
				}
			}
    	
    		/**
			 * 保存
			 */
			var save = function(){
				/** 数据验证 */
				//处理内容
				if(chuliNr.value.trim() == ""){
					alert("请输入实验及切换项目");
					chuliNr.value = "";
					chuliNr.focus();
					return;
				}else{
					if(chuliNr.value.length > 2000){
						alert("实验及切换项目 输入长度应为2000个字符以内");
						chuliNr.focus();
						return;
					}
				}
				
				/** 定期工作处理 */	
				Ext.Ajax.request({
					url: link3,
					method: 'post',
					params: {id:"<%=id%>", chuliNr:chuliNr.value, chuliDt:chuliDt.value, chuliRen:chuliRen.value, curPersonLoginName:curPersonLoginName},
					success: function(ajax){
						eval("responseObj=" + ajax.responseText);
						if(responseObj.success == true){
							alert('处理成功');
							window.close();
						}else{
							alert('处理失败,请联系管理员...');
							window.close();
						}
					},
					failure: function(){
						alert('服务器忙,请稍后再试...');
					}
				})
			}
    	</script>
    	
  </body>
</html>
