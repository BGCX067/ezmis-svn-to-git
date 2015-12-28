var tableName = window.dialogArguments.tableName;
var htid = window.dialogArguments.htid;;
var bwid = window.dialogArguments.bwid;

function chooseHt(){
	var obj = {};
	obj.tableName = tableName;
	var url = contextPath + '/jteap/gcht/htzx/htbw/htGrid.jsp';
	var result = showIFModule(url,"选择合同","true",500,300,obj);
	if(result != null){
		htid = result.htid;
		txtHtmc.setValue(result.htmc);
		txtHtbh.setValue(result.htbh);
	}
}

//合同名称
var txtHtmc = new Ext.form.TextField({
	id: 'txtHtmc',
    applyTo: 'txtHtmc',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请选择合同名称',
	readOnly: true,
	allowBlank: false
});

//合同编号
var txtHtbh = new Ext.form.TextField({
	id: 'txtHtbh',
    applyTo: 'txtHtbh',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请选择合同编号',
	readOnly: true,
	allowBlank: false
});

//备忘内容
var txtBwnr = new Ext.form.TextArea({
	id: 'txtBwnr',
	renderTo: 'divBwnr',
	maxLength: 1000,
	width: 350,
	height: 150,
	maxLengthText: '最长500个字符',
	blankText: '请输入备忘内容',
	allowBlank: false
});

//修改时赋值
if(bwid != null && bwid != ""){
	txtHtmc.setValue(window.dialogArguments.htmc);
	txtHtbh.setValue(window.dialogArguments.htbh);
	txtBwnr.setValue(window.dialogArguments.bwnr);
}

/**
 * 保存
 */
function save(jx){
	/** 数据验证 */
	if(!txtHtmc.validate()){
		alert("请选择合同名称");
		txtHtmc.focus();
		return;
	}
	if(!txtHtbh.validate()){
		alert("请选择合同编号");
		txtHtbh.focus();
		return;
	}
	if(!txtBwnr.validate()){
		alert("请输入备忘内容");
		txtBwnr.focus();
		return;
	}
	
	/** 保存定期工作设置 */	
	Ext.Ajax.request({
		url: link4,
		method: 'post',
		params: {htid:htid, htmc:txtHtmc.getValue(), htbh:txtHtbh.getValue(),
				bwid:bwid, bwnr: txtBwnr.getValue()},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				if(window.dialogArguments.grid != null){
					window.dialogArguments.grid.getStore().reload();
				}
				if(jx == null){
					window.close();
				}else{
					window.location = "bwForm.jsp";
				}
			}else{
				alert('保存失败,请联系管理员...');
				window.close();
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}
