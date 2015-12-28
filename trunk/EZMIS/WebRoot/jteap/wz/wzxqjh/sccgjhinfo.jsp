<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.system.role.manager.RoleManager"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.system.person.manager.PersonManager"%>
<%@page import="com.jteap.system.role.model.Role"%>
<%@page import="com.jteap.system.person.model.Person"%>
<%@page import="com.jteap.system.person.model.P2Role"%>
<%@page import="com.jteap.core.utils.JSONUtil"%>
<%@ include file="/inc/import.jsp" %>
<%@ include file="indexScript.jsp" %>
<html>
  <head>
	<title>需求计划生成采购计划</title>
	<%
		Map map = (Map)request.getAttribute("map");
		String sqjhid = (String)request.getParameter("sqjhid");
		String wzbm = (String)request.getParameter("wzbm");
		String isChecked = (String)request.getParameter("isChecked");
		RoleManager roleManager = (RoleManager) SpringContextUtil.getBean("roleManager");
		PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
		Role role = roleManager.findUniqueBy("roleSn","WZ_CGY");
		Set<P2Role> p2rs = role.getPersons();
		Person person = null;
		List personList = new ArrayList();
		for (P2Role p2r :p2rs){
			person = p2r.getPerson();
			personList.add(person);
		}
	%>
	<style>
		table#border{   
			border-top:#000 1px solid;   
			border-left:#000 1px solid;   
		}   
		table#border td{   
			border-bottom:#000 1px solid;   
			border-right:#000 1px solid;   
		}  
		h1{
			font-family:楷体, "Times New Roman";
		}
		table{
			font-size:13px;
			font-weight:lighter;
			font-family:宋体, "Times New Roman";
		}
		.pop-but01 {
			BORDER-RIGHT: #ceb80d 1px solid;
			BORDER-TOP: #ceb80d 1px solid;
			FONT-SIZE: 12px;
			FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#ffffff, EndColorStr=#f3dc41); 								    BORDER-LEFT: #ceb80d 1px solid;
			CURSOR: hand;
			COLOR: #000000;
			BORDER-BOTTOM: #ceb80d 1px solid;
			margin: 0px 2px 0px 2px;
			text-decoration: none;
			padding: 1px 0px 0px 0px;
			height: 23px;
			width: auto;
		}
	</style>
	<%@ include file="/inc/ext-all.jsp" %>
  </head>

	<body style="padding:5px;margin:0;">
		<div style="text-align:right">
			<input type="checkbox" id="wcgx" name="wcgx" onclick="checkShow();" /><font size="2px;">只查看未采购项</font>
    		<input type="button" id="sccgd" value="按采购员生成采购单" onclick="sccgd();" class="pop-but01" />
		</div>
	<div style="overflow-y:auto;width:100%;height:350px;">
		<br>
			<%
				for(Object key : map.keySet()){
					double pzslTotal = 0;
					double fpslTotal = 0;
					double cgslTotal = 0;
					List objs = (List)map.get(key);
			%>
			
					<table id="wzinfor" border="0" cellspacing="0" width="100%" align="center">  
						<tr>
							<td colspan="3" align="left" width="50%">
								物资:<%=((Object[])objs.get(0))[1] %>
							</td>
							<td colspan="2" align="center" width="20%">
								仓库:<%=((Object[])objs.get(0))[2] %>
							</td>
							<td colspan="2" align="center" width="20%">
								可用库存:<font color="blue"><%=((Object[])objs.get(0))[3] %></font>
							</td>
						</tr>
					</table>
					<table id="border" border="0" cellspacing="0" width="100%" align="center" name="xqjh_table">  
						<tr>
							<td align="center" width="30%">
								需求计划编号
							</td>
							<td align="center" width="10%">
								计量单位
							</td>
							<td align="center" width="10%">
								计划单价
							</td>
							<td align="center" width="10%">
								批准数量
							</td>
							<td align="center" width="10%">
								分配量
							</td>
							<td align="center" width="10%">
								已采购
							</td>
							<td align="center" width="10%">
								采购员
							</td>
						</tr>
						<% 
							for(int j = 0; j<objs.size();j++){
								Map<String, Object> objMaps = new HashMap<String, Object>();
								pzslTotal += (Double)((Object[])objs.get(j))[7];
								fpslTotal += (Double)((Object[])objs.get(j))[8];
								cgslTotal += (Double)((Object[])objs.get(j))[9]; 
						%>
						
						<tr>
							<td align="left" id="xqjhbh<%=j %>" >
							<%=((Object[])objs.get(j))[4] %>
							<%objMaps.put("xqjhbh",((Object[])objs.get(j))[4]); %>
							</td>
							<td align="left" id="jldw<%=j %>">
							<%=((Object[])objs.get(j))[5] %>
							<%objMaps.put("jldw",((Object[])objs.get(j))[5]); %>
							</td>
							<td align="right" id="jhdj<%=j %>)">
							<%=((Object[])objs.get(j))[6] %>
							<%objMaps.put("jhdj",((Object[])objs.get(j))[6]); %>
							</td>
							<td align="right" id="pzsl<%=j %>">
							<%=((Object[])objs.get(j))[7] %>
							<%objMaps.put("pzsl",((Object[])objs.get(j))[7]); %>
							</td>
							<td align="right" id="fpsl<%=j %>">
							<%=((Object[])objs.get(j))[8] %>
							<%objMaps.put("fpsl",((Object[])objs.get(j))[8]); %>
							</td>
							<td align="right" id="cgsl<%=j %>" tx = "1">
							<%=((Object[])objs.get(j))[9] %>
							<%objMaps.put("cgsl",((Object[])objs.get(j))[9]); %>
							<input name = "xqjhDetailId" type="hidden" id="xqjhDetailId<%=j %>" value="<%=((Object[])objs.get(j))[10] %>" />
							</td>
							
							<%objMaps.put("xqjhDetailId",((Object[])objs.get(j))[10]); %>
							<td align="center">
								<select id="cgy" width="40px" wzxqjhmxid="<%=((Object[])objs.get(j))[10] %>" wzbm="<%=((Object[])objs.get(0))[0]%>">
								<%
									for(int k = 0; k < personList.size(); k++){
										Person persons = (Person)personList.get(k);
										if(persons.getUserLoginName().equals(curPersonLoginName)){
								%>
											<option value="<%=persons.getUserLoginName() %>" selected>
												<%=persons.getUserName() %>
											</option>
								<%
										}else{
								%>
											<option value="<%=persons.getUserLoginName() %>">
												<%=persons.getUserName() %>
											</option>
								<%
										}
									}
								%>
								</select>
								<input type="hidden" id="cgys" name="cgys" value="" />
								<script type="text/javascript">
									var objs = document.getElementById("cgy");
									for(var i =0; i<objs.length; i++){
										if(objs[i].selected){
											document.getElementById("cgy").value = objs[i].value;
										}
									}
								</script>
							</td>
						</tr>
						<%
							}
						 %>
					</table>
					<table border="0" cellspacing="0" width="100%" align="center">
						  <tr>
							<td align="left" width="30%">
							</td>
							<td align="left" width="10%">
							</td>
							<td align="right" width="10%">
								合计:
							</td>
							<td align="right" width="10%">
								
								<font color="blue"><%=pzslTotal %></font>
							</td>
							<td align="right" width="10%">
								<font color="blue"><%=fpslTotal %></font>
							</td>
							<td align="right" width="10%">
								<font color="blue"><%=cgslTotal %></font>
							</td>
							<td align="center" width="10%">
							</td>
						</tr>
					</table>
					<hr>
					<br/>
			<%
			}
				//retJson = JSONUtil.listToJson(objList);
			%>
	</body>
</html>
<script type="text/javascript">
	var isChecked = '<%=isChecked%>';
	if(isChecked == "true"){
		document.getElementById("wcgx").checked = true;
	}else{
		document.getElementById("wcgx").checked = false;
	}
	function checkShow(){
		var sqjhid = '<%=sqjhid%>';
		var wzbm = '<%=wzbm%>';
		var checkedObj = document.getElementById("wcgx");
		if(checkedObj.checked){    
			window.location.href = link6+"?sqjhid="+sqjhid+"&wzbm="+wzbm+"&type=wcgx&isChecked=true";
		}else{
			window.location.href = link6+"?sqjhid="+sqjhid+"&wzbm="+wzbm+"&type=all&isChecked=false";
		}
	}
	//按采购员生成采购单
	function sccgd(){
		var tbs = document.getElementsByTagName("select");
		var str = "";
		var flag = true;
		for(i = 0; i < tbs.length; i++){
			if(i == 0){
				//str = "{'cgy':'" + tbs[i].value +"','xqjhDetailId':'" + tbs[i].wzxqjhmxid +"','wzbm':'" + tbs[i].wzbm;
				if(tbs[i].value == ""){
					flag = false;
					break;
				}
				str = tbs[i].value + "," + tbs[i].wzxqjhmxid + "," + tbs[i].wzbm;
			}else{
				//str += "','cgy':'" + tbs[i].value + "','xqjhDetailId':'" + tbs[i].wzxqjhmxid + "','wzbm:'" + tbs[i].wzbm;
				if(tbs[i].value == ""){
					flag = false;
					break;
				}
				str += ","+tbs[i].value + "," + tbs[i].wzxqjhmxid + "," + tbs[i].wzbm;
			}
		}
		if(!flag){
			alert("请选择采购员!");
			return;
		}
		//str += "'}";
		Ext.Ajax.request({
				url:link7,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
						alert("生成采购单成功!");
						window.returnValue = true;
						window.close();
						return;
			 		}else{
			 			alert("生成采购单失败!");
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ops: str}
		});
	}
</script>
