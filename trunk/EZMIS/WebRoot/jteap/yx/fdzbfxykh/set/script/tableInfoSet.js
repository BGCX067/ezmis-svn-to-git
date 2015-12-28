
var rootNode = window.dialogArguments.rootNode;
var oNode = window.dialogArguments.oNode; 

var tableId = "";
var defaSortno = 0;

if(rootNode != null && rootNode.id == "rootNode"){
	if(rootNode.lastChild != null){
		//默认排序号 (最后一条记录的排序号 + 1)
		defaSortno = rootNode.lastChild.attributes.sortNo + 1;
	}
}

//指标表定义中文名称
var txtTableName = new Ext.form.TextField({
	id: 'tableName',
	renderTo: 'divTableName',
	minLength: 2,
	maxLength: 50,
	width: 150,
	minLengthText: '最短2个字符',
	maxLengthText: '最长50个字符',
	blankText: '请输入中文名称',
	allowBlank: false
});

//指标表定义名称
var txtTableCode = new Ext.form.TextField({
	id: 'tableCode',
	renderTo: 'divTableCode',
	vtype:'alphanum',
	minLength: 2,
	maxLength: 30,
	width: 150,
	minLengthText: '最短2个字符',
	maxLengthText: '最长30个字符',
	blankText: '请输入英文名称',
	allowBlank: false
});

//备注
var txtRemark = new Ext.form.TextField({
	id: 'remark',
	renderTo: 'divRemark',
	minLength: 2,
	maxLength: 100,
	width: 150,
	minLengthText: '最短2个字符',
	maxLengthText: '最长100个字符'
});

//排序号 
var txtSortno= new Ext.form.NumberField({
	id: 'sortno',
	renderTo: 'divSortno',
	value: defaSortno,
	minValue: 0,
	width: 150,
	blankText: '请输入排序号',
	allowBlank: false
});

if(oNode != null && oNode.id != "rootNode"){
	//修改时
	tableId = oNode.attributes.id;
	
	Ext.Ajax.request({
		url: link3,
		method: 'post',
		params: {id:tableId},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				txtTableName.setValue(responseObj.data[0].tableName);
				txtTableCode.setValue(responseObj.data[0].tableCode);
				txtRemark.setValue(responseObj.data[0].remark);
				txtSortno.setValue(responseObj.data[0].sortno);
			}else{
				alert('服务器忙,请稍后再试');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试');
		}
	})
	
}

//默认选中第一个输入项
txtTableName.focus();

/**
 * 保存
 */
function save(){
	/** 数据验证 */
	if(!txtTableName.validate()){
		alert('请输入正确的中文名称');
		txtTableName.focus(true);
		return;
	}
	if(!txtTableCode.validate()){
		alert('请输入正确的英文名称');
		txtTableCode.focus(true);
		return;
	}
	if(!txtRemark.validate()){
		alert('请输入正确的备注');
		txtRemark.focus(true);
		return;
	}
	if(!txtSortno.validate()){
		alert('请输入排序号');
		txtSortno.focus(true);
		return;
	}else{
		if(txtSortno.getValue().toString().indexOf(".") != -1){
			alert("排序号必须是正整数");
			txtSortno.focus(true);
			return;
		}
	}
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link2,
		method: 'post',
		params: {id:tableId, tableCode:txtTableCode.getValue().trim(), tableName:txtTableName.getValue().trim(),
				remark:txtRemark.getValue().trim(), sortno:txtSortno.getValue()},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				window.close();
			}else{
				alert('保存失败');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})	
}
