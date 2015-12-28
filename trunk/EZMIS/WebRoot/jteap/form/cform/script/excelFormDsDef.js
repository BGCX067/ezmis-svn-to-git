var pWin = window.dialogArguments.opener;

/**
 * 页面初始化
 */
function onload() {

	var dsListJson = eval(pWin.dsListJson);
	if (typeof dsListJson != 'undefined') {
		Ext.each(dsListJson, function(ds) {
			dsList.appendItem(ds.name, ds.id);
		})
	}

	// 如果存在数据源，默认选中第一个
	if (dsList.size() > 0)
		dsList.selectByIdx(0);

}

/**
 * 新增数据源
 */
function appendNewDs() {
	var id = (new Ext.Button()).id;
	var idx = dsList.appendItem("新的数据源", id);
	dsList.selectByIdx(idx);
}

// 数据源列表
var dsList = new Ext.ux.Multiselect( {
	name : 'multiselect',
	fieldLabel : 'Multiselect',
	// data : [['系统时间','${systemTime}'],['系统日期','${systemDate}']],
	width : 150,
	height : 260,
	multiSelect : false,
	singleSelect : true,
	allowBlank : true,
	tbar : [ {
		text : '新增',
		handler : appendNewDs
	}, {
		text : '删除',
		handler : function() {
			dsList.removeSelection();
		}
	}]
});

dsList.on("change", function(dsList, value) {
	// alert(value);
	});


//数据源名称文本框
var txtDsName=new Ext.form.TextField({maxLength : 50,allowBlank :false,fieldLabel : "数据源名称",name : "txtDsName",width:200});

//数据源关联表名
var comboDsTName=new Ext.form.ComboBox({maxLength : 50,allowBlank :false,fieldLabel : "关联表名",name : "txtDsName",width:200	});

//数据源SQL语句
var txtDsSql=new Ext.form.TextArea({maxLength : 50,fieldLabel : "查询SQL语句",name : "txtDsSql",width:198});
// 中间
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	border : true,
	title : '数据源详情',
	//layout:"fit",
	items : [{
		xtype : "panel",
		layout : "column",
		bodyStyle:"padding:5px 2px 2px 12px",
		border:false,
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			labelWidth : 80,
			items : [txtDsName]
		},{
			columnWidth : 1,
			layout : "form",
			border : false,
			labelWidth : 80,
			items : [comboDsTName]
		},{
			columnWidth : 1,
			layout : "form",
			border : false,
			labelWidth : 80,
			items : [txtDsSql]
		}]
	}]
}

var lyLeft = {
	region : 'west',
	width : 150,
	split : true,
	border : false,
	items : [dsList]
}

var lySouth = {
	region : 'south',
	contentEl : 'bbar',
	height : 40
}
