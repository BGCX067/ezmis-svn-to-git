<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<title>JTEAP 2.0</title>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<link rel="stylesheet" href="${contextPath}/resources/lightbox.css" type="text/css"></link>	
	<script type="text/javascript" src="${contextPath}/script/prototype.js"></script>
	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
	<script type="text/javascript" src="${contextPath}/script/lightbox.js"></script>
	<script type="text/javascript" src="script/index.js" charset="UTF-8"></script>
	<%@ include file="/inc/meta.jsp" %>
  </head>
 
  <body onload="initPage();">
  	
	<dl id="idBox" class="lightbox">
	  <dt id="idBoxHead"><b>正在保存,请稍候...</b> </dt>
	  <dd style="text-align:center;">
	  		<img src="${contextPath}/resources/process.gif"/>
	  </dd>
	</dl>
  	<div>
  		<input type="button" value="保存" onclick="submitForm();"/>
  	</div>
  	<div>
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
  			<form name="form1" action="${contextPath}/jteap/system/SystemFuncAction!saveSystemConfigAction.do">
  			<tr>
  				<td width="200" style="background-color: #D4E0F2">参数名称</td>
  				<td width="360" style="background-color: #D4E0F2">参数值</td>
  				<td style="background-color: #D4E0F2">参数描述</td>
  			</tr>
  			<tr>
  				<td>影像库目录设置</td>
  				<td><input type="text" value="" name="param_YX_FILE_URL"/></td>
  				<td>设定影像系统中的影像文件的存放路径</td>
  			</tr>
			<tr>
  				<td>默认空间配额(单位：MB)</td>
  				<td id="tdNsr">
					<span>个体户纳税人: </span><input type="text" value="" name="param_YX_PE_1" style="width:257px"/><br/>
					<span>小规模纳税人:</span><input type="text" value="" name="param_YX_PE_2" style="width:257px"/><br/>
					<span>一般纳税人:</span><input type="text" value="" name="param_YX_PE_3" style="width:257px"/><br/>
					<span>税局内部部门:</span><input type="text" value="" name="param_YX_PE_4" style="width:257px"/><br/>
					<span>税局内部个人:</span><input type="text" value="" name="param_YX_PE_5" style="width:257px"/><br/>
					<span>其他：</span><input type="text" value="" name="param_YX_PE_0" style="width:257px"/>
				</td>
  				<td>按照纳税人性质(个体户纳税人_1、小规模纳税人_2、一般纳税人_3、税局内部部门_4、税局内部个人_5)指定默认配额
</td>
  			</tr>  		
  			<tr>
  				<td>界面皮肤设置</td>
  				<td>
  					<select name="param_JTEAP_SKIN" style="width:100%">
  						<option value="Default">默认皮肤</option>
  						<option value="Vista">皮肤一</option>
  						<option value="Green">皮肤二</option>
  						<option value="Theme">皮肤三</option>
  					</select>
  				</td>
  				<td>可选的皮肤为：Default,Vista,Green</td>
  			</tr>
  			<tr>
  				<td>非法格式文件</td>
  				<td><input type="text" value="" name="param_YX_FILE_ILLEGAL_PATTERN"/></td>
  				<td>设定不允许上传的文件类型,以逗号分隔</td>
  			</tr>
  			<!--
  			 <tr>
  				<td>销毁缓冲时限设置(单位：月)</td>
  				<td><input type="text" value="" name="param_YX_DESTORY_BUFFER_TIME"/></td>
  				<td>针对销毁文件提供一个缓冲时间，当销毁一个文件后过指定时间后真实删除记录和文件。</td>
  			</tr>
  			
  			 -->
  			<tr>
  				<td>销毁轮询周期</td>
  				<td>
	  				<select name="param_YX_DESTORY_CYCLE_PERIOD" style="width:100%">
	  					<option value="-1">从不</option>
	  					<option value="0 0/10 * ? * *">10分钟一次</option>
	  					<option value="0 0/5 * ? * *">5分钟一次</option>
	  					<option value="0 0/2 * ? * *">2分钟一次</option>
	  				</select>
  				</td>
  				<td>针对到达销毁周期的影像资料文件，多长时间轮询一次，一般建议一个月轮询一次,采取QuartZ的表达式设置周期，表达式设置参考<a href=#>帮助</a></td>
  			</tr>  
  			<tr>
  				<td>过期轮询周期</td>
  				<td>
  					<select name="param_YX_DELAY_CYCLE_PERIOD" style="width:100%">
	  					<option value="-1">从不</option>
	  					<option value="0 0/10 * ? * *">10分钟一次</option>
	  					<option value="0 0/5 * ? * *">5分钟一次</option>
	  					<option value="0 0/2 * ? * *">2分钟一次</option>
	  				</select>
  				</td>
  				<td>针对过期影像资料文件，多长时间轮询一次，一般建议一个月轮询一次,采取QuartZ的表达式设置周期，表达式设置参考<a href=#>帮助</a></td>
  			</tr>
  			</form>
  		</table>
  	</div>
  </body>
</html>
