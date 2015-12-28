<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.jteap.core.web.SpringContextUtil"%>
<%@page import="com.jteap.yx.jjb.manager.JiaoJieBanManager"%>
<%@page import="com.jteap.yx.jjb.model.JiaoJieBan"%>
<%@page import="com.jteap.yx.zbjl.manager.JbyxfsManager"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp" %>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>
	<style type="text/css">
		.btn{
			BORDER-RIGHT: #CEB60C 1px solid; PADDING-RIGHT: 2px; BORDER-TOP: 
            #CEB60C 1px solid; PADDING-LEFT: 2px; FONT-SIZE: 12px; FILTER: 
            progid:DXImageTransform.Microsoft.Gradient(GradientType=0, 
            StartColorStr=#FFFFFF, EndColorStr=#F3DC41); BORDER-LEFT: #CEB60C 
            1px solid; CURSOR: hand; COLOR: black; PADDING-TOP: 0px; 
            BORDER-BOTTOM: #CEB60C 1px solid
		}
	</style>
  </head>
	
  <body scroll="no" id="index" style="background-color: #D5E2F2">
  	
  	<!-- 数据字典 -->
	<jteap:dict catalog="zbbc_sj_dy,zbzb"></jteap:dict>
	
	<%
		//岗位类别
		String gwlb = request.getParameter("gwlb");
		if("1".equals(gwlb)){
			gwlb = "#1机长"; 
		}else if("2".equals(gwlb)){
			gwlb = "#2机长"; 
		}else if("3".equals(gwlb)){
			gwlb = "#3机长"; 
		}else if("4".equals(gwlb)){
			gwlb = "#4机长";
		}
		String lastJjbsj = "";
		String lastJiebanbc = "";
		String lastJiebanzb = "";
		String lastJiebanr = "";
		String lastJiebanid = "";
		String firstJjbsj = "";
		String firstJiebanbc = "";
		
  		JiaoJieBanManager jiaoJieBanManager = (JiaoJieBanManager)SpringContextUtil.getBean("jiaoJieBanManager");
		//获取最后交接班信息
		JiaoJieBan lastJiaoJieBan = jiaoJieBanManager.findFirstOrLast("last",gwlb);
		JiaoJieBan firstJiaoJieBan = jiaoJieBanManager.findFirstOrLast("first",gwlb);
		
		if(lastJiaoJieBan != null){
			lastJjbsj = lastJiaoJieBan.getJjbsj();
			lastJiebanbc = lastJiaoJieBan.getJiebanbc();
			lastJiebanzb = lastJiaoJieBan.getJiebanzb();
			lastJiebanr = lastJiaoJieBan.getJiebanr();
			lastJiebanid = lastJiaoJieBan.getJiebanrId();
			firstJjbsj = firstJiaoJieBan.getJjbsj();
			firstJiebanbc = firstJiaoJieBan.getJiebanbc();
		}
	 %>
	 
    <table align="center" width="99%" border="1" cellpadding="10" bgcolor="#DFE8F6">
    	<tr>
    		<td colspan="2">
    			<table>
    				<tr>
    					<td>
    						<font id="fnJiaoBTime"></font>：
    					</td>
    					<td>
    						<font id="fnJiaoBBanCi" class="JC"></font>：
    					</td>
    					<td>
    						<input id="txtJiaoBanZB" type="text">
    					</td>
    				</tr>
    			</table>
    		</td>
    		<td colspan="2">
	    		<table>
	    				<tr>
	    					<td>
    							<font id="fnJieBTime"></font>：
    						</td>
	    					<td>
	    						<font id="fnJieBBanCi" class="JC"></font>：
	    					</td>
	    					<td>
				    			<input id="txtJieBanZB" type="text">
	    					</td>
	    				</tr>
	    		</table>
    		</td>
    	</tr>
    	<tr>
    		<td class="NOR" valign="top">
    			<table width="100%">
    				<tr>
    					<td>
    						<font class="JC">交班人</font>：
    					</td>
    					<td>
							<input id="txtJiaoBanRen" type="text">
    					</td>
    				</tr>
    			</table>
    		</td>
    		<td class="NOL" valign="top">
    			<table width="100%">
    				<tr>
    					<td> 
    						<font class="JC">密码</font>：
    					</td>
    					<td>
							<input id="txtJiaoBanPass" type="password">
    					</td>
    				</tr>
    			</table>
    		</td>
    		<td class="NOR" valign="top">
    			<table width="100%">
    				<tr>
    					<td>
    						<font class="JC">接班人</font>：
    					</td>
    					<td>
							<input id="txtJieBanRen" type="text">
    					</td>
    				</tr>
    			</table>
    		</td>
    		<td class="NOL" valign="top">
    			<table width="100%">
    				<tr>
    					<td>
    						<font class="JC">密码</font>：
    					</td>
    					<td>
							<input id="txtJieBanPass" type="password">
    					</td>
    				</tr>
    			</table>
    		</td>
    	</tr>
   	<%
   		if("值长".equals(gwlb)){
   	 %>
    	<tr>
    		<td class="NOR" valign="top">
    			<table width="100%">
    				<tr>
    					<td>
    						<font class="JC">交班人2</font>：
    					</td>
    					<td>
							<input id="txtJiaoBanRen2" type="text">
    					</td>
    				</tr>
    			</table>
    		</td>
    		<td class="NOL">&nbsp;</td>
    		<td class="NOR" valign="top">
    			<table width="100%">
    				<tr>
    					<td>
    						<font class="JC">接班人2</font>：
    					</td>
    					<td>
							<input id="txtJieBanRen2" type="text">
    					</td>
    				</tr>
    			</table>
    		</td>
    		<td class="NOL">&nbsp;</td>
    	</tr>
   	<%
   		}
   	 %>
    	
    	<%
    		JbyxfsManager jbyxfsManager = (JbyxfsManager)SpringContextUtil.getBean("jbyxfsManager");
    		String jbyxfs[] = new String[2];
    		String jbyxfsFormSn[] = new String[2];
    		String jzHao = "";
    		String jjrParams = "";
    		
    		if("值长".equals(gwlb)){
    			jbyxfs[0] = "值长交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_ZZJBYXFS";
    			jjrParams = "值长";
    		}else if("#1机长".equals(gwlb)){
    			jbyxfs[0] = "#1机长交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_JZJBYXFS_300";
    			jzHao = "1";
    			jjrParams = "1";
    		}else if("#2机长".equals(gwlb)){
    			jbyxfs[0] = "#2机长交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_JZJBYXFS_300";
    			jzHao = "2";
    			jjrParams = "2";
    		}else if("#3机长".equals(gwlb)){
    			jbyxfs[0] = "#3机长交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_JZJBYXFS_600";
    			jzHao = "3";
				jjrParams = "3";
    		}else if("#4机长".equals(gwlb)){
    			jbyxfs[0] = "#4机长交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_JZJBYXFS_600";
    			jzHao = "4";
    			jjrParams = "4";
    		}else if("电气".equals(gwlb)){
    			jbyxfs[0] = "电气主控交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_DQZKJBYXFS";
    			jbyxfs[1] = "600MW电气交班运行方式";
    			jbyxfsFormSn[1] = "TB_YX_FORM_EQDQJBYXFS";
    			jjrParams = "电气";
    		}else if("零米".equals(gwlb)){
    			jbyxfs[0] = "脱硫交班运行方式";
    			jbyxfsFormSn[0] = "TB_YX_FORM_TLJBYXFS";
    			jbyxfs[1] = "精处理及炉内交班运行方式";
    			jbyxfsFormSn[1] = "TB_YX_FORM_JCLJLNJBYXFS";
    			jjrParams = "零米";
    		}
    	 %>
    	<tr>
    		<td class="NOR" colspan="4">
    			<table align="right">
    				<tr>
  			<%
 				for(int i=0; i<jbyxfs.length; i++){
 					if(jbyxfs[i] != null && !"".equals(jbyxfs[i])){
 					
   			%>
    					<td align="right" style="padding-right: 20px;padding-left: 20px;">
    						<font>
    							<%=jbyxfs[i] %>
    						</font>
    					</td>
    					<td align="right">
   							<%
								String jbyxfsId = jbyxfsManager.findJbyxfs(jbyxfsFormSn[i],lastJjbsj,lastJiebanbc,jzHao);
								String status = "(未完成)";
								String color = "red;";
								if(jbyxfsId != null){
									status = "(已完成)";
									color = "green;";
								}
							 %>
    						<!-- 根据时间、班次、值别、 查询运行方式对应的表单物理表是否有该条数据 -->
							<a  href="javascript:void(0);" class="HC" style="color: <%=color%>" onclick="addJbyxfs('/jteap/form/eform/eformRec.jsp?formSn=<%=jbyxfsFormSn[i] %>','<%=jbyxfsId==null?"":jbyxfsId%>','<%=jzHao%>')">
								 <%=status %>
							</a>
    					</td>
   			<%
   					}
   				}
   			 %>
    				</tr>
    			</table>
    		</td>
    	</tr>	
    	
    	<tr>
    		<td colspan="4" align="right">
    			<input id="btnSave" type="button" value=" 确定交班 " onclick="save()" class="btn">
    		</td>
    	</tr>
    </table>
	
   	<!-- 加载脚本库  开始  -->
	<%@ include file="/inc/ext-all.jsp" %>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>
	
	<script type="text/javascript">
		//当前接班时间
		var nowDate = new Date();
		//当前日期
		var nowYmd = nowDate.format('Y-m-d');
		//现在 时、分、秒
		var nowHms = nowDate.format('H:i:s');
		//现在班次
		var nowBc = "";
		
	/** 根据 数据字典初始化 */
		//夜班开始、结束时间
		var yeB = $dictValue("zbbc_sj_dy","ye_beginTime");
		var yeE = $dictValue("zbbc_sj_dy","ye_endTime");
		//白班开始、结束时间
		var baiB = $dictValue("zbbc_sj_dy","bai_beginTime");
		var baiE = $dictValue("zbbc_sj_dy","bai_endTime");
		//中班开始、结束时间
		var zhongB = $dictValue("zbbc_sj_dy","zhong_beginTime");
		var zhongE = $dictValue("zbbc_sj_dy","zhong_endTime");
									
		//获取初始交班班次
		 if(nowHms >= yeB && nowHms <= yeE){
			nowBc = "夜班";
		}else if(nowHms >= baiB && nowHms <= baiE){
			nowBc = "白班";
		}else if(nowHms >= zhongB && nowHms <= zhongE){
			nowBc = "中班";
		}
		
		var lastJjbsj = "<%=lastJjbsj%>";
		var lastJiebanbc = "<%=lastJiebanbc%>";
		var lastJiebanzb = "<%=lastJiebanzb%>";
		var lastJiebanr = "<%=lastJiebanr%>";
		var lastJiebanid = "<%=lastJiebanid%>";
		var firstJjbsj = "<%=firstJjbsj%>";
		var firstJiebanbc = "<%=firstJiebanbc%>"
		var gwlb = "<%=gwlb%>";
		var jjrParams = encodeURIComponent("<%=jjrParams%>");
		jjrParams = "?gwlb=" + jjrParams;
		if(lastJjbsj == ""){
			lastJjbsj = nowYmd;
			firstJjbsj = nowYmd;
			lastJiebanbc = nowBc;
			firstJiebanbc = nowBc;
		}
	</script>
	
	<script type="text/javascript" src="script/jobForm.js" charset="UTF-8"></script>
	<!-- 加载脚本库  结束 -->
	 	
	<!-- 入口程序 -->
    <script type="text/javascript">
		Ext.onReady(function(){		
			Ext.QuickTips.init();
		});
    </script>
	    
  </body>
</html>
