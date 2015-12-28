
//序号
var txtXh = new Ext.form.NumberField({
	id: 'txtXh',
	renderTo: 'divXh',
	width: 120,
	allowBlank: false,
	readOnly: isReadOnly
});

//通知内容
var txtNr = new Ext.form.TextArea({
	id: 'txtNr',
	renderTo: 'divNr',
	maxLength: 200,
	width: 300,
	height: 150,
	maxLengthText: '最长100个字符',
	allowBlank: false,
	readOnly: isReadOnly
});

//发出人
var txtFcr = new Ext.form.TextField({
	id: 'txtFcr',
	renderTo: 'divFcr',
	maxLength: 30,
	width: 120,
	readOnly: true
});

//发出时间
var txtSj = new Ext.form.TextField({
	id: 'txtSj',
	renderTo: 'divSj',
	maxLength: 30,
	width: 120,
	readOnly: true
});

var select = window.dialogArguments.select;
var id = null;

if(select != null){
	id = select.data.id;
	txtXh.setValue(select.data.xh);
	txtNr.setValue(select.data.nr);
	txtFcr.setValue(select.data.fcr);
	txtSj.setValue(select.data.sj);
}else{
	txtFcr.setValue(curPersonName);
	txtSj.setValue(new Date().format("Y-m-d H:i"));
}
txtXh.focus();

//保存
function save(jx){
	/** 数据验证 */
	if(!txtXh.validate()){
		if(txtXh.getValue() == ""){
			alert("请输入序号");
		}else{
			alert("请输入正确的序号");
		}
		return;
	}
	if(!txtNr.validate()){
		if(txtNr.getValue() == ""){
			alert("请输入通知内容");
		}else{
			alert("输入字符过多");
		}
		return;
	}
	
	var fcr = txtFcr.getValue();
	var sj = txtSj.getValue();
	//修改时,重置发出人、发出时间
	if(showJx == "none;" && showSave == ""){
		fcr = curPersonName;
		sj = new Date().format("Y-m-d H:i");
	}
	/** 保存 */	
	Ext.Ajax.request({
		url: link3,
		method: 'post',
		params: {id:id, xh:txtXh.getValue(), nr:txtNr.getValue(), fcr:fcr, sj:sj},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				if(jx == null){
					window.close();
				}else{
					window.location = "editForm.jsp";
				}
				if(window.dialogArguments.grid != null){
					window.dialogArguments.grid.getStore().reload();
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
