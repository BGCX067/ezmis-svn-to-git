<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/inc/import.jsp"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.zbjl.manager.ZhiBanJiLuManager"%>
<%@page import="com.jteap.yx.zbjl.model.ZhiBanJiLu"%>
<html>
	<head>
		<%@ include file="/inc/meta.jsp"%>
		<%@ include file="indexScript.jsp"%>
		<title>JTEAP 2.0</title>
		<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	</head>

	<body scroll="no" id="index">
		
		 <!-- 加载等待图标 开始 -->
		<div id="loading-mask" style=""></div>
		<div id="loading">
		  <div class="loading-indicator">
		  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
		  </div>
		</div>
	   	 <!-- 加载等待图标 结束 -->
	   	 
	   	 <!-- 加载脚本库  开始  -->
		<%@ include file="/inc/ext-all.jsp" %>

		<script type="text/javascript">
    
    /** 获取参数 */
    	<%String id = request.getParameter("id");%>
    	//Id
    	var id = "<%=id%>";
    	
    	//值班时间
    	var zbsj = (window.dialogArguments).lastJjbsj;
    	if(zbsj != null){
    		zbsj = zbsj.substring(0,10);
    	}
    	
    	var jlr = (window.dialogArguments).userName;
   		/** 验证 */ 	
    	//验证小时
    	function validateShi(){
    		if(txtShi.value.trim() != ""){
				if(isNaN(txtShi.value)){
					alert("小时必须是数字");
					txtShi.value = "";
					txtShi.focus();
				}else if(txtShi.value.indexOf(".") != -1){
					alert("小时必须是正整数");
					txtShi.value = "";
					txtShi.focus();
				}else if(txtShi.value <= 0 || txtShi.value > 24){
					alert("小时必须在1~24之间");
					txtShi.value = "";
					txtShi.focus();
				}    
			}		
    	}
    	//验证分
    	function validateFen(){
    		if(txtFen.value.trim() != ""){
				if(isNaN(txtFen.value)){
					alert("分必须是数字");
					txtFen.value = "";
					txtFen.focus();
				}else if(txtFen.value.indexOf(".") != -1){
					alert("分必须是正整数");
					txtFen.value = "";
					txtFen.focus();
				}else if(txtFen.value < 0 || txtFen.value > 60){
					alert("分必须在0~60之间");
					txtFen.value = "";
					txtFen.focus();
				}    
			}		
    	}
    	
   		/** 保存 */
    	function save(){
    	var now = new Date();
    		//通知时间
			var jlsj = now.format('Y-m-d');
			if(txtShi.value != ""){
				jlsj += " " + txtShi.value;
				if(txtFen.value != ""){
					jlsj += ":" + txtFen.value;
				} 
			}
			
			//通知岗位
			var tzgw = "";
			if(ckZz.checked){
				tzgw += "值长,";
			}
			if(ckJz.checked){
				tzgw += "#1机长,#2机长,#3机长,#4机长,";
			}
			if(ckDq.checked){
				tzgw += "电气,";
			}
			if(ckLm.checked){
				tzgw += "零米,";
			}
			if(tzgw == ""){
				alert("至少选择一个通知岗位");
				return;
			}
			
			//通知内容
			var jlnr = txtJlnr.value;
			if(jlnr.trim().length <= 0 ){
				alert("请输入通知内容");
				txtJlnr.focus();
				return;
			}else if(jlnr.trim().length > 600 ){
				alert("通知内容应少于300个汉字");
				txtJlnr.focus();
				return;
			}
			//保存
			Ext.Ajax.request({
				url: link5,
				method: 'post',
				params: {id:id, jlsj:jlsj, tzgw:tzgw, jlnr:jlnr, jlr:jlr},
				success: function(ajax){
					eval("responseObj="+ajax.responseText);
					if(responseObj.success){
						alert('保存成功');
						window.close();
					}else{
						alert('保存失败');
						window.close();
					}
				},
				failure: function(){
					alert('服务器忙,请稍后再试...');
				}
			});
		}
    	
    </script>

		<script type="text/javascript">
    	if(id != null){
	   		<%	
	   			Date date = new Date();
	   			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   			SimpleDateFormat formH = new SimpleDateFormat("HH");
	   			SimpleDateFormat formM = new SimpleDateFormat("mm");
	   			Map gwMap = new HashMap();
	   			String jlnr = "";
	   			if(id != null){
	   				ZhiBanJiLuManager zhiBanJiLuManager = (ZhiBanJiLuManager)SpringContextUtil.getBean("zhiBanJiLuManager");
		   			ZhiBanJiLu zhiBanJiLu = zhiBanJiLuManager.get(id);
		   			date = zhiBanJiLu.getJlsj();
		   			String[] gwArray = zhiBanJiLu.getTzgw().split(",");
		   			for(int i=0; i<gwArray.length; i++){
						gwMap.put(gwArray[i],"checked");
		   			}
		   			jlnr = zhiBanJiLu.getJlnr();
		   	%>
		   			jlr = "<%=zhiBanJiLu.getJlr()%>";
		   	<%
		   			if(date != null){
		   	%>
		   				zbsj = "<%=dateFormat.format(date)%>";
		   	<%		
		   			}
	   			}
	   		%>
    	}
    </script>

		<div class="pop-out">
			<div class="pop-in">
				<div class="pop-main">
					<div class="pop-title">
						值班通知
					</div>
					<table width="97.5%" align="center" class="LabelBodyTb">
						<tr>
							<td class="POPtab-Title">
								时间
							</td>
							<td class="POPtabTbEntry2">
								<%=dateFormat.format(date)%>
								<input id="txtShi" type="text" style="width: 20px;"
									onblur="validateShi()"
									value="<%
   							if(date != null)
   								out.write(formH.format(date));
	   						%>">
								时&nbsp;
								<input id="txtFen" type="text" style="width: 20px;"
									onblur="validateFen()"
									value="<%
	   							if(date != null)
	   								out.write(formM.format(date));
	   						%>">
								分
							</td>
						</tr>
						<tr>
							<td class="POPtab-Title">
								发送岗位
							</td>
							<td class="POPtabTbEntry2">
								&nbsp;&nbsp; 值长:
								<input id="ckZz" name="ckZz" type="checkbox">
								&nbsp; 机长:
								<input id="ckJz" type="checkbox">
								&nbsp; 电气:
								<input id="ckDq" type="checkbox">
								&nbsp; 零米:
								<input id="ckLm" type="checkbox">
							</td>
						</tr>
						<tr>
							<td class="POPtab-Title">
								通知内容
							</td>
							<td class="POPtabTbEntry2">
								<textarea id="txtJlnr" style="height: 125px; width: 380px"><%=jlnr%></textarea>
							</td>
						</tr>
					</table>

					<div class="pop-but">
						<div class="pop-butMain">
							<input type="button" class="pop-but01" value=" 发 布 " onclick="save()" />
							<input type="button" class="pop-but01" value=" 取 消 " onclick="window.close();" />
						</div>
					</div>

				</div>
			</div>
		</div>
		<script type="text/javascript">
    	if(id == "null"){
    		//添加时 默认选中对应的岗位类别
    		var gwlb = (window.dialogArguments).gwlb;
    		if(gwlb == "值长"){
    			document.getElementById("ckZz").checked = "checked";
    		}else if(gwlb == "1"){
    			document.getElementById("ckJz").checked = "checked";
    		}else if(gwlb == "2"){
    			document.getElementById("ckJz").checked = "checked";
    		}
    		else if(gwlb == "3"){
    			document.getElementById("ckJz").checked = "checked";
    		}
    		else if(gwlb == "4"){
    			document.getElementById("ckJz").checked = "checked";
    		}
    		else if(gwlb == "电气"){
    			document.getElementById("ckDq").checked = "checked";
    		}
    		else if(gwlb == "零米"){
    			document.getElementById("ckLm").checked = "checked";
    		}
    	}else {
    		//修改时 读取数据,选中岗位类别
			<%
				if(gwMap.get("值长") != null){
			%>
					document.getElementById("ckZz").checked = "checked";
			<%		
				}
			%>		
			<%
				if(gwMap.get("#1机长") != null && gwMap.get("#2机长") != null &&
					 gwMap.get("#3机长") != null &&gwMap.get("#4机长") != null){
			%>
					document.getElementById("ckJz").checked = "checked";
			<%		
				}
			%>		
			<%
				if(gwMap.get("电气") != null){
			%>
					document.getElementById("ckDq").checked = "checked";
			<%		
				}
			%>		
			<%
				if(gwMap.get("零米") != null){
			%>
					document.getElementById("ckLm").checked = "checked";
			<%		
				}
			%>		
    	}
    	txtJlnr.focus();
    </script>

	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){			
		  	//程序加载完成后撤除加载图片
		    setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		});	
    </script>
    <!-- 加载脚本库  结束 -->

	</body>
</html>
