<%@page import="com.jteap.system.resource.model.Module"%>
<%@page import="com.jteap.system.resource.manager.ModuleManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>

<%
	if(sTheme.equals("MODERN")){
%>
<div id="headerDiv">
	<div class="top-Bg">
 	<div class="top-Under"></div>
	</div>
</div>



<div id="headerDiv1">
	<div class="blk_18" id="container">
		<div style="width:25px;height:20px;float:left;">
				<a class="LeftBotton" id="leftButton" onclick="ISL_GoUp_1();ISL_StopUp_1();" 
					href="javascript:void(0);" target="_self"></a>
		</div>
		<div class="pcont" id="ISL_Cont_1">
			<div class="ScrCont">
				<div id="List1_1">
					<%
					ModuleManager moduleManager = (ModuleManager)SpringContextUtil.getBean("moduleManager");
					Collection<Module> roots = moduleManager.findModule(null);
					String icon = "";
					Object[] obj = roots.toArray();
						for(int i = 0; i < obj.length; i++){
							Module module = (Module)obj[i];
							if(module.getIcon() == null){
								icon = "resources/icon/ico_1.png";
							}else{
								icon = module.getIcon();
							}
							%>
							<a class="pl" target="_self" href="javascript:void(0);" onMouseOver="onMouseOverEvent(<%=i%>)" id="<%=i%>" onclick="refreshTree('<%=module.getId()%>','<%=i%>')">
								<img src="${contextPath}/<%=icon %>" alt="" width="83" height="63" />
								<%=module.getResName() %>
							</a><%
							if(i == 0){%>
								<script type="text/javascript">
									funcPanel.getRootNode().id = "<%=module.getId()%>";
									document.getElementById('<%=i%>').style.color = "#5dacec"
									document.getElementById('<%=i%>').style.width = "103px";
									document.getElementById('<%=i%>').style.background = "#D9E8FB";
								</script><%
							}%>
							<%
						}
					%>
					<input type="hidden" id="picNum" value="<%=roots.size()%>" />
					<!-- piclist begin -->
					<!-- piclist end -->
				</div>
				<div id="List2_1"></div>
			</div>
		</div>
			<div style="width:25px;height:20px;float:left;">
				<a class="RightBotton" id="rightButton" onclick="ISL_GoDown_1();ISL_StopDown_1();" 
					href="javascript:void(0);" target="_self"></a>
			</div>
	</div>
	<div class="c"></div>
	<script type="text/javascript">
	        //picrun_ini()
	 </script>
	<!-- picrotate_left end -->
</div>
<!------------------>
<script type="text/javascript">
	//判断控制图片左右滚动的按钮是否显示
	var screenWidth = document.body.clientWidth;
	var pictureNum = document.getElementById("picNum").value;
	if(pictureNum*107>document.body.clientWidth){
		document.getElementById("leftButton").style.display = "block";
		document.getElementById("rightButton").style.display = "block";
	}else{
		document.getElementById("leftButton").style.display = "none";
		document.getElementById("rightButton").style.display = "none";
	}
	
	var temp = null;
	if(window.onresize && typeof window.onresize == "function"){
		temp = window.onresize;
	}
	
	window.onresize=function(){
		var blk_18_width = document.body.clientWidth+"px";
		var blk_18_pcont_width = (document.body.clientWidth - 50)+"px";
		document.getElementById("container").style.width = blk_18_width;
		document.getElementById("ISL_Cont_1").style.width = blk_18_pcont_width;
		
		var picSum = document.getElementById("picNum").value;  //图片总数
		var displatPicSum = Math.floor((document.body.clientWidth/107));    //可视图片数
		if(GetObj('ISL_Cont_1').scrollLeft>=107*(picSum-displatPicSum)){
			MoveLock_1=false;
		}else{
			GetObj('ISL_Cont_1').scrollLeft+=Space_1;
		}
	}
	
	window.onload = function(){
		var blk_18_width = document.body.clientWidth+"px";
		var blk_18_pcont_width = (document.body.clientWidth - 50)+"px";
		document.getElementById("container").style.width = blk_18_width;
		document.getElementById("ISL_Cont_1").style.width = blk_18_pcont_width;
	}
	
	var oldId = null;
	var num = 0;
	function refreshTree(id,tagId){
		funcPanel.getRootNode().id = id;
		funcPanel.getRootNode().reload();
		//默认效果
		document.getElementById(0).style.background = "#CBD9E4";
		document.getElementById(0).style.color = "#213f71";
		if(num == 0){
			document.getElementById(tagId).style.color = "#5dacec";
			document.getElementById(tagId).style.width = "103px";
			document.getElementById(tagId).style.background = "#D9E8FB";
		}else{
			if(tagId != oldId){
				//高亮效果
				document.getElementById(tagId).style.color = "#5dacec";
				document.getElementById(tagId).style.width = "103px";
				document.getElementById(tagId).style.background = "#D9E8FB";
				
				//默认效果
				document.getElementById(oldId).style.background = "#CBD9E4";
				document.getElementById(oldId).style.color = "#213f71";
			}
		}
		num++;
		oldId = tagId;
	}
	
	function onMouseOutEvent(id){
		document.getElementById(id).style.background = "#CBD9E4";
		document.getElementById(id).style.color = "#213f71";
	}
	
	var temp = null;
	var sum = 0;
	function onMouseOverEvent(id){
		if(sum == 0){
			document.getElementById(id).style.color = "#5dacec";
			document.getElementById(id).style.width = "103px";
			document.getElementById(id).style.background = "#D9E8FB";
			
			//document.getElementById(0).style.background = "#CBD9E4";
			//document.getElementById(0).style.color = "#213f71";
		}else{
			if(temp != id){
				document.getElementById(id).style.color = "#5dacec";
				document.getElementById(id).style.width = "103px";
				document.getElementById(id).style.background = "#D9E8FB";
				
				document.getElementById(temp).style.background = "#CBD9E4";
				document.getElementById(temp).style.color = "#213f71";
			}
		}
		sum++;
		temp = id;
		
		if(num == 0){
			document.getElementById(0).style.color = "#5dacec";
			document.getElementById(0).style.width = "103px";
			document.getElementById(0).style.background = "#D9E8FB";
		}else{
			document.getElementById(oldId).style.color = "#5dacec";
			document.getElementById(oldId).style.width = "103px";
			document.getElementById(oldId).style.background = "#D9E8FB";
		}
	}
	
</script>

<%
	}
%>





