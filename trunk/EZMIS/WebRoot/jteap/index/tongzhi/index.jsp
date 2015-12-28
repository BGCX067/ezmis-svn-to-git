<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.index.manager.XiTongTongZhiManager"%>
<%@page import="com.jteap.index.model.XiTongTongZhi"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
  </head>
 
  <script>
	lstart=152
	loop=true
	speed=1000 
	pr_step=5 
	function makeObj(obj,nest){
		nest=(!nest) ? '':'document.'+nest+'.'
		this.css=(document.layers) ? eval(nest+'document.'+obj):eval(obj+'.style')
		this.scrollHeight=document.layers?this.css.document.height:eval(obj+'.offsetHeight')
		this.up=goUp
		this.obj = obj + "Object"
		eval(this.obj + "=this")
		return this
	}
	function goUp(speed){
		if(parseInt(this.css.top)>-this.scrollHeight){
			this.css.top=parseInt(this.css.top)-pr_step
			setTimeout(this.obj+".up("+speed+")",speed)
		}else if(loop) {
			this.css.top=lstart
			eval(this.obj+".up("+speed+")")
		}
	}
	function slideInit(){
		oSlide=makeObj('divNews','divCont')
		oSlide.css.top=lstart
		oSlide.up(speed)
	}
	onload=slideInit
  </script>

  <%
  	XiTongTongZhiManager xiTongTongZhiManager = (XiTongTongZhiManager)SpringContextUtil.getBean("xiTongTongZhiManager");
  	String sql = "from XiTongTongZhi order by sj desc";
  	List<XiTongTongZhi> list = xiTongTongZhiManager.find(sql);
   %>	
	  
  <body id="index" scroll="no">
  	<div id="divCont" style="position:absolute; width:100%; height:100%; top:5; left:5;">
		<div id="divNews" style="position:absolute; top:0; left:0">
			<font style="font-size: 12px;">
			<%
			int size = 5;
			if(list.size()<5){
				size = list.size();
			}
			for(int i = 0;i<size;i++){
			 	XiTongTongZhi xiTongTongZhi = list.get(i);
			 %>
			 	&nbsp;&nbsp;&nbsp;
			 	<%=xiTongTongZhi.getNr() %>
			 	<div align="right" style="width:100%;">
				 	<%=xiTongTongZhi.getFcr() %>&nbsp;&nbsp;&nbsp;&nbsp;
				 	<%=xiTongTongZhi.getSj() %>&nbsp;&nbsp;&nbsp;&nbsp;
			 	</div>
			 	<br>
			 <%
			 }
			  %>
			</font>
		</div>
	</div>
  </body>
</html>
