<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/import.jsp" %>
<html>
  <head>
	<%@ include file="/inc/meta.jsp" %>
	<%@ include file="indexScript.jsp"  %>
	<title>JTEAP 2.0</title>

	<link rel="stylesheet" href="MoreProperties.css" type="text/css"></link>
	<link rel="stylesheet" href="index.css" type="text/css"></link>	
	<script language="javascript" type="text/javascript" src="${contextPath}/component/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${contextPath}/script/common.js"></script>
	<script type="text/javascript" src="${contextPath}/script/date.js"></script>

	<script type="text/javascript" src="${contextPath}/script/prototype.js" charset="GBK"></script>
	<script type="text/javascript" src="${contextPath}/script/validator1.0.js" charset="GBK"></script>
	<script type="text/javascript" src="script/MoreProperties.js" charset="utf-8"></script>
	
	
  	<script type="text/javascript">
	  	var personid="${param.personid}";
	  	var readOnly="${param.readOnly}";
	  	function doCancel() {
		    var isIFrame = (top!=this?true:false);
		    if(isIFrame){
		        var closeFn = window.parent.closeExtWindow;
		        if(closeFn)
		            closeFn();
		    }else{ 
		        window.opener=null;
		        window.open("","_self");
		        window.close();
		    }
		}
	  	
	 </script>
	<style>
		.message{
			font-size : 12px;
			color : #666666;
		}
		.error1{
			font-size : 12px;
			color : #FF0000;
		}
		.error2{
			font-size : 12px;
			color : #0000ff;
		}
	</style>
  </head>
  <body onload="initExtend();">
  <div id="loading-mask" style=""></div>
	<div id="loading">
	  <div class="loading-indicator">
	  	<img src="${contextPath}/resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
	  </div>
	</div>
   	 <!-- 加载等待图标 结束 -->

   	 <!-- 加载脚本库  开始 -->
   	 <%@ include file="/inc/ext-all.jsp" %>
  
  
  
	 <form action="">
	 <input type="button" style="display:none;" id="saveButton" name="saveButton" value="保存" onclick="saveData();">
	 <div>
	 	<span class="sp3">基本信息</span>
	 	<div style="float:left;width:330px;height:170px;padding: 5 5 5 5;">
	 		<iframe name="iconIframe" src="${contextPath}/jteap/system/person/UploadIcon.jsp?personid=${param.personid}&readOnly=${param.readOnly}" width="100%" height="100%"></iframe>
	 	</div>
	 	<span class="sp1"><span class="sp2">用户名:</span><input type="text" name="userLoginName" readonly="readonly" id="userLoginName"></span>
	 	<span class="sp1"><span class="sp2">昵称:</span><input type="text" name="userName" id="userName"></span>
	 	<span class="sp1"><span class="sp2">性别:</span>
	 		<select id="sex" name="sex">
				<option value="男">男</option>
				<option value="女">女</option>
			</select>
	 	</span>
	 	<span class="sp1"><span class="sp2">出生日期:</span><input type="text" name="birthday" id="birthday" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"></span>
	 	<span class="sp1"><span class="sp2">民族:</span><input type="text" name="mz" id="mz"></span>
	 	<span class="sp1"><span class="sp2">出生地:</span><input type="text" name="csd" id="csd"></span>
	 	<span class="sp1"><span class="sp2">曾用名:</span><input type="text" name="userName2" id="userName2"></span>
	 	<span class="sp1"><span class="sp2">籍贯:</span><input type="text" name="jg" id="jg"></span>
	 	<span class="sp1"><span class="sp2">参加工作日期:</span><input type="text" name="cjgzrq" id="cjgzrq" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"></span>
	 	<span class="sp1"><span class="sp2">计算工龄日期:</span><input type="text" name="jsglrq" id="jsglrq" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"></span>
	 	<span class="sp1"><span class="sp2">健康状况:</span>
				<select id="jkzk" name="jkzk">
					<option value="1">优</option>
					<option value="2">良</option>
					<option value="3">中</option>
					<option value="4">差</option>
				</select>
		</span>
		<span class="sp1"><span class="sp2">职务级别:</span><input type="text" name="zwjb" id="zwjb"></span>
		<span class="sp1"><span class="sp2">身份证号:</span><input type="text" id="sfz" name="sfz" id="sfz"></span>
		<span class="sp1"><span class="sp2">社会保障号:</span><input type="text" name="sbh" id="sbh"></span>
		<span class="sp1"><span class="sp2">进入本单位时间:</span><input type="text" name="jrdwsj" id="jrdwsj" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"></span>
		<span class="sp1"><span class="sp2">用工性质:</span><input type="text" name="ygxz" id="ygxz"></span>
		<span class="sp1"><span class="sp2">户口性质:</span><input type="text" name="hkxz" id="hkxz"></span>
		<span class="sp1"><span class="sp2">户口所在地:</span><input type="text" name="hkszd" id="hkszd"></span>
		<span class="sp1"><span class="sp2">婚姻状况:</span>
			<select id="hyzk" name="hyzk">
				<option value="1">已婚</option>
				<option value="2">未婚</option>
			</select>
		</span>
		<span class="sp1"><span class="sp2">手机:</span><input type="text" name="sj" id="sj"></span>
		<span class="sp1"><span class="sp2">办公电话:</span><input type="text" name="bgdh" id="bgdh"></span>
		<span class="sp1"><span class="sp2">家庭地址:</span><input type="text" name="jtdz" id="jtdz"></span>
		<span class="sp1"><span class="sp2">电子邮件:</span><input type="text" name="dzyj" id="dzyj"></span>
		<span class="sp1"><span class="sp2">后备干部:</span>
			<select id="hbgb" name="hbgb">
				<option value="1">是</option>
				<option value="2">否</option>
			</select>
		</span>
		<span class="sp1"><span class="sp2">首次后备日期:</span><input type="text" name="schbrq" id="schbrq" class="Wdate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"></span>
		<span class="sp1"><span class="sp2">个人身份:</span><input type="text" name="grsf" id="grsf"></span>
		<span class="sp1"><span class="sp2">员工单位标识:</span><input type="text" name="ygdwbs" id="ygdwbs"></span>
		<div>
			<span class="sp3">扩展信息</span>
			<input type="hidden" name="config">
				<div id="extendinfo"></div>
		</div>
		<div>
			<span class="sp3">附件信息</span>
			<iframe scrolling="no" name="accIframe" src="${contextPath}/jteap/system/person/UploadAccessories.jsp?personid=${param.personid}&readOnly=${param.readOnly}" width="100%"></iframe>
		</div>
	 </div>
	 </form>
  </body>
</html>
	
<script type="text/javascript">
	var validator = new MyValidator();
	validator.addValidation(new InputRangeCustomValidation("sfz", "false", "true","请输入数字", "身份证无效", "default", 1, 50, regObj["_idCard"]));
	validator.addValidation(new InputRangeCustomValidation("sbh", "false", "true","请输入数字", "社保无效", "default", 1, 50, regObj["_number"]));
	validator.addValidation(new InputRangeCustomValidation("sj", "false", "true","请输入数字", "手机无效", "default", 1, 50, regObj["_mobile"]));
	validator.addValidation(new InputRangeCustomValidation("bgdh", "false", "true","请输入数字", "电话无效", "default", 1, 50, regObj["_phone"]));
	validator.addValidation(new InputRangeCustomValidation("dzyj", "false", "true","请输入邮件", "邮件无效", "default", 1, 50, regObj["_email"]));
	validator.mySetUp(1);
	function _submit1(){
		if(validator.isPassed(1)){
			return true;
		}
		return false;
	}
	function _submit2(){
		if(validator.isPassed(2)){
			alert("验证通过，可以提交数据了！")
		}else{		
			alert("以下原因导致提交失败" + validator.popError);		
			return $(validator.errorIds[0]).focus();		
		}
	}
	
	
	Ext.onReady(function(){
		Ext.QuickTips.init();
		setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
	});
</script>
