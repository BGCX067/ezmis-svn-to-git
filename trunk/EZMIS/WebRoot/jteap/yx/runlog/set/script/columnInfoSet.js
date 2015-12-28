
var grid = window.dialogArguments.grid;

var gridDataList = grid.getStore().reader.jsonData.list;
if(gridDataList[gridDataList.length-1] != null){
	//默认排序号 (最后一条记录的排序号 + 1)
	var defaSortno = gridDataList[gridDataList.length-1].sortno + 1;
}

//中文名称
var txtColumnName= new Ext.form.TextField({
	id: 'txtColumnName',
	renderTo: 'divColumnName',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请输入中文名称',
	allowBlank: false
});

//英文名称
var txtColumnCode= new Ext.form.TextField({
	id: 'txtColumnCode',
	renderTo: 'divColumnCode',
	vtype:'alphanum',
	maxLength: 30,
	width: 150,
	maxLengthText: '最长30个字符',
	blankText: '请输入英文名称',
	allowBlank: false
});

//排序号 
var txtSortno= new Ext.form.NumberField({
	id: 'txtSortno',
	renderTo: 'divSortno',
	value: defaSortno,
	minValue: 0,
	width: 150,
	blankText: '请输入排序号',
	allowBlank: false
});

//额定值
var txtEdingzhi= new Ext.form.TextField({
	id: 'txtEdingzhi',
	renderTo: 'divEdingzhi',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请输入额定值'
});

//计量单位
var txtJiliangdanwei= new Ext.form.TextField({
	id: 'txtJiliangdanwei',
	renderTo: 'divJiliangdanwei',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请输入计量单位',
	allowBlank: false
});

var storeJizu = new Ext.data.SimpleStore({
	fields: ["value", "key"],
	data: [['1#','1#'],['2#','2#'],['3#','3#'],['4#','4#']]
});

//取数机组
var comboJizu= new Ext.form.ComboBox({
	id: 'txtJizu',
    renderTo: 'divJizu',
    store: storeJizu,
	valueField: "value",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择机组',
	emptyText: '请选择机组',
	width: 150,
	editable: false,
	forceSelection: true,
	allowBlank: false,
	listeners: {
		select: function(){
			var jizu = comboJizu.getValue();
			if(jizu == "3#"){
				txtDataTableCode.setValue("301");
				txtDataTableCode.setDisabled(true);
			}else if(jizu == "4#"){
				txtDataTableCode.setValue("401");
				txtDataTableCode.setDisabled(true);
			}
		}
	}
});

//取数表编码
var txtDataTableCode= new Ext.form.NumberField({
	id: 'txtDataTableCode',
	renderTo: 'divDataTableCode',
	minValue: 0,
	width: 150,
	blankText: '取数表编码'/*,
	allowBlank: false*/
});

//测点编码
var txtSisCedianbianma= new Ext.form.NumberField({
	id: 'txtSisCedianbianma',
	renderTo: 'divSisCedianbianma',
	minValue: 0,
	width: 150,
	blankText: '请输入SIS测点编码'/*,
	allowBlank: false*/
});

var id = window.dialogArguments.id;
var tableId = window.dialogArguments.tableId;

if(id != null){
	//修改时 赋值
	txtColumnCode.setValue(window.dialogArguments.columnCode);
	txtColumnName.setValue(window.dialogArguments.columnName);
	txtEdingzhi.setValue(window.dialogArguments.edingzhi);
	txtJiliangdanwei.setValue(window.dialogArguments.jiliangdanwei);
	txtSortno.setValue(window.dialogArguments.sortno);
	comboJizu.setValue(window.dialogArguments.jizu);
	txtDataTableCode.setValue(window.dialogArguments.dataTableCode);
	txtSisCedianbianma.setValue(window.dialogArguments.sisCedianbianma);
}

//默认选中第一个输入项
txtColumnName.focus();

/**
 * 保存
 */
var save = function(jx){
	/** 数据验证 */
	if(!txtColumnName.validate()){
		alert('请输入正确的中文名称');
		txtColumnName.focus(true);
		return;
	}
	if(!txtColumnCode.validate()){
		alert('请输入正确的英文名称');
		txtColumnCode.focus(true);
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
	if(!txtEdingzhi.validate()){
		alert('请输入正确的额定值');
		txtEdingzhi.focus(true);
		return;
	}
	if(!txtJiliangdanwei.validate()){
		alert('请输入正确的计量单位');
		txtJiliangdanwei.focus(true);
		return;
	}
	/*if(!txtDataTableCode.validate()){
		alert('请输入正确的取数表名');
		txtDataTableCode.focus(true);
		return;
	}
	if(!txtSisCedianbianma.validate()){
		alert('请输入正确的测点编码');
		txtSisCedianbianma.focus(true);
		return;
	}*/
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link10,
		method: 'post',
		params: {id:id, tableId:tableId,
				columnCode:txtColumnCode.getValue().trim(), columnName:txtColumnName.getValue().trim(),
				edingzhi:txtEdingzhi.getValue().trim(), jiliangdanwei:txtJiliangdanwei.getValue().trim(),
				sisCedianbianma:txtSisCedianbianma.getValue(), sortno:txtSortno.getValue().toString().trim(),
				dataTableCode:txtDataTableCode.getValue(),jizu:comboJizu.getValue()},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				grid.getStore().reload();
				if(jx != null){
					window.location = "columnInfoSet.jsp";		
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
