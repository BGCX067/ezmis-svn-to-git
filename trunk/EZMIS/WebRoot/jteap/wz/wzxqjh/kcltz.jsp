<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<link rel="stylesheet" href="../../index.css" type="text/css"></link>
	<%
		String xqjhDetailId = (String)request.getParameter("xqjhDetailId");
		String wzbm = (String)request.getParameter("wzbm");
		String wzmc = (String)request.getParameter("wzmc");
		String xhgg = (String)request.getParameter("xhgg");
		String free = (String)request.getParameter("free");
		String freekc = (String)request.getParameter("freekc");
	%>
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript">
		//库存量分配后提交
		function save(){
			var fpsl = document.getElementById("tzsl").value;
			if(parseFloat(fpsl) <= 0){
				alert("请输入分配数量!");
				return ;
			}else{
				var wzbm = '<%=wzbm%>';
				var xqjhDetailId = '<%=xqjhDetailId%>';
				Ext.Ajax.request({
						url:link4,
						success:function(ajax){
					 		var responseText=ajax.responseText;	
					 		var responseObject=Ext.util.JSON.decode(responseText);
					 		if(responseObject.success){
								if(responseObject.flag){
									alert("库存量调整成功!");
									window.returnValue = true;
									window.close();
									return;
								}else{
									alert("库存量不足!");
									return;
								}
					 		}else{
					 			if(!responseObject.flag){
					 				alert("已分配数量不能大批准数量!");
					 				return ;
					 			}
					 			//alert(responseObject.msg);
					 		}				
						},
					 	failure:function(){
					 		alert("提交失败");
					 	},
					 	method:'POST',
					 	params: {wzbm:wzbm,fpsl:fpsl,xqjhDetailId:xqjhDetailId}// Ext.util.JSON.encode(selections.keys)
					});
			}
			//window.close();
		}
	</script>
  </head>

	<body scroll="no" id="index">
		<div class="pop-out">
		<div class="pop-in">
			<div class="pop-main">
				<div class="pop-title">
					需求计划分配调整
				</div>
				
			    <table align="center" height="70" width="97.5%" class="LabelBodyTb">
			    	<tr>
			    		<td class="POPtab-Title" colspan="2" style="text-align:left">
							物资:<font color="red"><%=wzmc %>(<%=xhgg %>)</font>的当前自由库存量为:<font color="red"><%=freekc %></font>
						</td>
			    	</tr>
			    	<tr>
			    		<td class="POPtab-Title" width="300" style="text-align:left">
			    			对该需求计划的自由库存分配调整为:
			    		</td>
			    		<td class="POPtabTbEntry2">
			    			<input type="text" id="tzsl" name="tzsl" vlaue="" size="25" />
			    		</td>
			    	</tr>
			    </table>
			    <div class="pop-but">
					<div class="pop-butMain">
						<input type="button" id="save" value=" 确 定 " onclick="save();" class="pop-but01">
		    			<input type="button" id="colse" value=" 取 消 " onclick="window.close();" class="pop-but01">
					</div>
				</div>
   			</div>
   		</div>
   	</div>
	</body>
	<script type="text/javascript">
		document.getElementById("tzsl").value = 0.00;
	</script>
</html>
