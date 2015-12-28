var pWin = window.dialogArguments.opener; //

function onload() {

}
// 单元格标识
var txtUserName = new Ext.form.TextField({
	disabled : false,
	width : 190,
	maxLength : 50,
	allowBlank : false,
	fieldLabel : "用户名",
	name : "userLoginName"
});
var txtPwd = new Ext.form.TextField({
	inputType : "password",
	disabled : false,
	width : 190,
	maxLength : 50,
	allowBlank : false,
	fieldLabel : "密码",
	enableKeyEvents:true,
	name : "userPassword"
});
var txtRemark = new Ext.form.TextArea({
	id: 'remark',
	maxLength: 500,
	height: 120,
	width: 190,
	fieldLabel : "签名说明",
	maxLengthText: '最长500个字符',
	name : "remark"
	
});

txtPwd.on("keypress", function(field, e) {
	if (e.keyCode == 13)
		btnValidateHandler();
})

var simpleForm = new Ext.FormPanel({
	labelAlign : 'left',
	buttonAlign : 'right',
	id : "myForm",
	style : 'margin:2px',
	bodyStyle : 'padding:0px',
	waitMsgTarget : true,
	width : '100%',
	frame : true, // 圆角风格
	labelWidth : 70, // 标签宽度
	items : [{
		layout : 'column',
		border : false,
		labelSeparator : '：',
		defaults : {
			blankText : '必填字段'
		},
		items : [{
			// 第一行布局
				columnWidth : 1,
				layout : 'form',
				border : false,
				items : [txtUserName]
			}, {
				// 第一行布局
				columnWidth : 1,
				layout : 'form',
				border : false,
				items : [txtPwd]
			},{
				//第三行布局
				columnWidth: 1,
				layout : 'form',
				border : false,
				items : [txtRemark]
			}]
	}],
	buttons : [{
		text : '确定',
		handler : btnValidateHandler
	}, {
		text : '取消',
		handler : function() {
			window.close();
		}
	}]
});
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	items : [simpleForm]
}

var titlePanel = new Ext.app.TitlePanel({
	caption : '签名验证',
	border : false,
	region : 'north'
});

function btnValidateHandler() {
	if (!simpleForm.form.isValid()) {
		alert('数据校验失败，请检查填写的数据是否正确');
		return;
	}

	var url = contextPath + "/jteap/system/person/PersonAction!validateNameAndPasswordAction.do";
	var params = Form.serialize($("myForm"));
	var remark = txtRemark.getValue().trim();
	// 提交数据
	Ext.Ajax.request({
		url : url,
		success : function(ajax) {
			var responseText = ajax.responseText;
			var responseObj = Ext.util.JSON.decode(responseText);
			if (responseObj.unique != null && responseObj.unique == true) {
				// 验证成功
			window.returnValue = responseObj.person.userName+","+remark;
			window.close();
		} else {
			alert("用户名或密码错误,验证未通过");

		}
	},
	failure : function() {
		alert("提交失败");
	},
	method : 'POST',
	params : params
})

}