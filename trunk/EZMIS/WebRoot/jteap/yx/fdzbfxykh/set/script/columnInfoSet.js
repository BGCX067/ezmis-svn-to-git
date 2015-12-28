
var grid = window.dialogArguments.grid;
var directiveId = window.dialogArguments.directiveId;
var id = "";
var defaSortno = 0;
//修改前的字段名
var beforColumnCode = "";

var gridDataList = grid.getStore().reader.jsonData.list;
if(gridDataList[gridDataList.length-1] != null){
	//默认排序号 (最后一条记录的排序号 + 1)
	defaSortno = parseInt(gridDataList[gridDataList.length-1].sortno) + 1;
}

//指标中文名称
var txtDirectiveName = new Ext.form.TextField({
	id: 'directiveName',
	renderTo: 'divDirectiveName',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请输入指标中文名称',
	allowBlank: false
});

//指标英文名称
var txtDirectiveCode = new Ext.form.TextField({
	id: 'directiveCode',
	renderTo: 'divDirectiveCode',
	vtype: 'alphanum',
	maxLength: 30,
	width: 150,
	maxLengthText: '最长30个字符',
	blankText: '请输入指标英文名称',
	allowBlank: false
});

//取数编码
var txtDataTable = new Ext.form.TextField({
	id: 'dataTable',
	renderTo: 'divDataTable',
	maxLength: 50,
	width: 150,
	maxLengthText: '最长50个字符',
	blankText: '请输入取数编码'/*,
	allowBlank: false*/
});

/*var dict_qsgz = $dictList("xzb_qsgz");
//数据源-取数规则
var storeQsgz = new Ext.data.Store({
	data: {rows:dict_qsgz},
	reader: new Ext.data.JsonReader({
		root: 'rows',
		id: 'id'
	},['key','value','id'])
});

//取数规则
var comboSisCedianbianma = new Ext.form.ComboBox({
	id: 'sisCedianbianma',
	renderTo: 'divSisCedianbianma',
	valueField: "key",
	displayField: "key",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择取数规则',
	emptyText: '请选择取数规则',
	width : 150,
	forceSelection: true,
	editable: false,
//	allowBlank: false,
	store: storeQsgz
});*/

//备注
var txtRemark = new Ext.form.TextField({
	id: 'remark',
	renderTo: 'divRemark',
	maxLength: 100,
	width: 150,
	maxLengthText: '最长100个字符'
});

//排序号 
var txtSortno = new Ext.form.NumberField({
	id: 'txtSortno',
	renderTo: 'divSortno',
	value: defaSortno,
	minValue: 0,
	width: 150,
	blankText: '请输入排序号',
	allowBlank: false
});

//指标类型数据源
var storeSumOrAvg=  new Ext.data.SimpleStore({
	fields: ["retrunValue", "displayText"],
	data: [['求和项','求和项'],['平均值项','平均值项'],['计算项','计算项']]
});

//指标类型
var comboSumOrAvg = new Ext.form.ComboBox({
	id: 'comboSumOrAvg',
	renderTo: 'divSumOrAvg',
	valueField: "retrunValue",
	displayField: "displayText",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择指标类型',
	emptyText: '请选择指标类型',
	width : 150,
	forceSelection: true,
	editable: false,
	allowBlank: false,
	store: storeSumOrAvg
});

var select = window.dialogArguments.select;
if(select != null){
	//修改时 赋值
	id = select.id;
	directiveId = select.data.directiveId;
	txtDirectiveName.setValue(select.data.directiveName);
	txtDirectiveCode.setValue(select.data.directiveCode);
	beforColumnCode = select.data.directiveCode;
	if(select.data.sumOrAvg != ""){
		comboSumOrAvg.setValue(select.data.sumOrAvg);
	}
	txtDataTable.setValue(select.data.dataTable);
//	comboSisCedianbianma.setValue(select.data.sisCedianbianma);
	txtRemark.setValue(select.data.remark);
	txtSortno.setValue(select.data.sortno);
}

//默认选中第一个输入项
txtDirectiveName.focus();

/**
 * 保存
 */
function save(jx){
	/** 数据验证 */
	if(!txtDirectiveName.validate()){
		alert('请输入正确的指标中文名称');
		txtDirectiveName.focus(true);
		return;
	}
	if(!txtDirectiveCode.validate()){
		alert('请输入正确的指标英文名称');
		txtDirectiveCode.focus(true);
		return;
	}
	if(!comboSumOrAvg.validate()){
		alert('请选择指标类型');
		comboSumOrAvg.focus();
		return;
	}
	if(!txtDataTable.validate()){
		alert('请输入正确的取数编码');
		txtDataTable.focus(true);
		return;
	}
	/*if(!comboSisCedianbianma.validate()){
		alert('请输入正确的取数规则');
		comboSisCedianbianma.focus(true);
		return;
	}*/
	if(!txtRemark.validate()){
		alert('请输入正确的备注');
		txtRemark.focus(true);
		return;
	}
	if(!txtSortno.validate()){
		alert('请输入排序号');
		txtSortno.focus(true);
		return;
	}
		
	/** 保存 */	
	Ext.Ajax.request({
		url: link9,
		method: 'post',
		params: {id:id, directiveId:directiveId, beforColumnCode:beforColumnCode,
				directiveName:txtDirectiveName.getValue().trim(),
				directiveCode:txtDirectiveCode.getValue().trim(),
				sumOrAvg:comboSumOrAvg.getValue(),
				dataTable:txtDataTable.getValue().trim(),
				//sisCedianbianma:comboSisCedianbianma.getValue(),
				remark:txtRemark.getValue().trim(),
				sortno:txtSortno.getValue()},
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
