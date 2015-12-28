//KKS码
var txtKKS= new Ext.form.TextField({
	id: 'txtKKS',
	renderTo: 'divKKS',
	maxLength: 32,
	width: 150,
	maxLengthText: '最长32个字符',
	blankText: '请输入KKS码',
	allowBlank: false
});

//设备编码
var txtSbbm= new Ext.form.TextField({
	id: 'txtSbbm',
	renderTo: 'divSbbm',
	vtype:'alphanum',
	maxLength: 32,
	width: 150,
	maxLengthText: '最长32个字符',
	blankText: '请输入设备编码',
	allowBlank: false
});

//仪表名称
var txtYbmc= new Ext.form.TextField({
	id: 'txtYbmc',
	renderTo: 'divYbmc',
	minValue: 0,
	width: 150,
	blankText: '请输入仪表名称',
	allowBlank: false
});

//型式及规范
var txtXsjgf= new Ext.form.TextField({
	id: 'txtXsjgf',
	renderTo: 'divXsjgf',
	maxLength: 500,
	width: 150,
	maxLengthText: '最长500个字符',
	blankText: '请输入型式及规范'
});

//单位
var txtDw= new Ext.form.TextField({
	id: 'txtDw',
	renderTo: 'divDw',
	maxLength: 64,
	width: 150,
	maxLengthText: '最长64个字符',
	blankText: '请输入单位',
	allowBlank: false
});

//数量
var txtSl= new Ext.form.NumberField({
	id: 'txtSl',
	renderTo: 'divSl',
	maxLength: 5,
	width: 150,
	maxLengthText: '最长5个字符',
	blankText: '请输入数量',
	allowBlank: false
});

//安装地点
var txtAzdd= new Ext.form.TextField({
	id: 'txtAzdd',
	renderTo: 'divAzdd',
	maxLength: 200,
	width: 150,
	maxLengthText: '最长200个字符',
	blankText: '请输入安装地点',
	allowBlank: false
});

//系统图号
var txtXtth= new Ext.form.TextField({
	id: 'txtXtth',
	renderTo: 'divXtth',
	maxLength: 64,
	width: 150,
	maxLengthText: '最长64个字符',
	blankText: '请输入系统图号',
	allowBlank: false
});

//用途
var txtYt= new Ext.form.TextField({
	id: 'txtYt',
	renderTo: 'divYt',
	maxLength: 500,
	width: 390,
	maxLengthText: '最长500个字符',
	blankText: '请输入用途',
	allowBlank: false
});

//备注
var txtRemark= new Ext.form.TextArea({
	id: 'txtRemark',
	renderTo: 'divRemark',
	maxLength: 500,
	height:130,
	width: 400,
	maxLengthText: '最长500个字符',
	blankText: '请输入备注',
	allowBlank: false
});

var id = window.dialogArguments.id;
var tableId = window.dialogArguments.tableId;

if(id != null){
	//修改时 赋值
	txtKKS.setValue(window.dialogArguments.kks);
	txtSbbm.setValue(window.dialogArguments.sbbm);
	txtYbmc.setValue(window.dialogArguments.ybmc);
	txtXsjgf.setValue(window.dialogArguments.xsjgf);
	txtDw.setValue(window.dialogArguments.dw);
	txtSl.setValue(window.dialogArguments.sl);
	txtAzdd.setValue(window.dialogArguments.azdd);
	txtXtth.setValue(window.dialogArguments.xtth);
	txtYt.setValue(window.dialogArguments.yt);
	txtRemark.setValue(window.dialogArguments.remark);
}



/**
 * 保存
 */
var save = function(jx){
	/** 数据验证 */
	if(!txtKKS.validate()){
		alert('请输入正确的KKS码');
		txtKKS.focus(true);
		return;
	}
	if(!txtSbbm.validate()){
		alert('请输入正确的设备编码');
		txtSbbm.focus(true);
		return;
	}
	if(!txtYbmc.validate()){
		alert('请输入仪表名称');
		txtYbmc.focus(true);
		return;
	}
	if(!txtXsjgf.validate()){
		alert('请输入正确的型式及规范');
		txtXsjgf.focus(true);
		return;
	}
	if(!txtDw.validate()){
		alert('请输入正确的单位');
		txtDw.focus(true);
		return;
	}
	if(!txtSl.validate()){
		alert('请输入正确的数量');
		txtSl.focus(true);
		return;
	}
	if(!txtAzdd.validate()){
		alert('请输入正确的安装地点');
		txtAzdd.focus(true);
		return;
	}
	if(!txtXtth.validate()){
		alert('请输入正确的系统图号');
		txtXtth.focus(true);
		return;
	}
	if(!txtYt.validate()){
		alert('请输入正确的用途');
		txtYt.focus(true);
		return;
	}
	if(!txtRemark.validate()){
		alert('请输入备注');
		txtRemark.focus(true);
		return;
	}
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link6,
		method: 'post',
		params: {id:id, tableId:tableId,
				kks:txtKKS.getValue().trim(), sbbm:txtSbbm.getValue().trim(),
				ybmc:txtYbmc.getValue().trim(), xsjgf:txtXsjgf.getValue().trim(),
				dw:txtDw.getValue().trim(), sl:txtSl.getValue().toString().trim(),
				azdd:txtAzdd.getValue().trim(),xtth:txtXtth.getValue().trim(),
				yt:txtYt.getValue().trim(),remark:txtRemark.getValue().trim(),
				tzflCatalogId:sbtzCatalogId},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				if(jx != null){
					window.location = "jctzinfo.jsp";		
				}else{
					window.close();
				}
			}else{
				alert('保存失败');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})	
}
