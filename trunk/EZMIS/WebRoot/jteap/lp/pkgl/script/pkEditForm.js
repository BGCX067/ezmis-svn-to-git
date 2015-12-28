var distsPkfl = $dictListAjax("PFL");
var myMaskSave = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在保存，请稍候..."
});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'pzid'
})

// 票分类
var dsPfl = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		root : 'rows',
		id : 'id'
	}, ['key', 'value', 'id']),
	data : {
		rows : distsPkfl
	}
})
var cmbPfl = new Ext.form.ComboBox({
	hiddenName : 'pfl',
	renderTo : 'divPfl',
	store : dsPfl,
	editable : false,
	displayField : 'key',
	valueField : 'value',
	mode : 'local',
	allowBlank : false,
	triggerAction : 'all',
	typeAhead : true,
	typeAheadDelay : 2000,
	selectOnFocus : true,
	emptyText : '请选择票分类',
	width : 200
})

// 票名称
var txtPmc = new Ext.form.TextField({
	name : 'pmc',
	renderTo : 'divPmc',
	allowBlank : false,
	readOnly : true,
	width : 163
})
var btnChoose = new Ext.Button({
	text : '...',
	renderTo : 'divChoose',
	handler : pzChooseHandler
})
var hdnGllch = new Ext.form.Hidden({
	name : 'gllch'
})

// 创建部门
var cmbCjbm = new Ext.app.ComboTree({
	renderTo : 'divCjbm',
	dataUrl : link2,
	width : 200,
	listWidth : 300,
	listHeight : 80,
	allowBlank : false,
	triggerClass : 'comboTree',
	autoScroll : true,
	localData : false,
	rootVisible : false,
	readOnly : true
});

// 创建人
var txtCjr = new Ext.form.TextField({
	name : 'cjr',
	renderTo : 'divCjr',
	readOnly : true,
	width : 200
})

// 创建日期
var dtCjsj = new Ext.form.DateField({
	name : 'cjsj',
	renderTo : 'divCjsj',
	format : 'Y-m-d',
	readOnly : true,
	allowBlank : false,
	width : 200
});

// 是否标准票
var rdBzp = new Ext.form.RadioGroup({
	renderTo : 'divIsBzp',
	width : 130,
	columns : 2,
	items : [{
		boxLabel : '是',
		name : 'isBzp',
		inputValue : '1',
		checked : true
	}, {
		boxLabel : '否',
		name : 'isBzp',
		inputValue : '2',
		checked : false
	}]
})

// 票状态
var rdPzt = new Ext.form.RadioGroup({
	renderTo : 'divPzt',
	width : 130,
	columns : 2,
	items : [{
		boxLabel : '有效',
		name : 'pzt',
		inputValue : '1',
		checked : true
	}, {
		boxLabel : '无效',
		name : 'pzt',
		inputValue : '2',
		checked : false
	}]
})

// 内码
var txtNm = new Ext.form.TextField({
	id : 'nm',
	renderTo : 'divNm',
	vtype : 'alphanum',
	maxLength : 32,
	maxLengthText : '最长32位字母数字组合',
	name : 'nm',
	allowBlank : false,
	width : 200
});

// 备注说明
var txtBzsm = new Ext.form.TextArea({
	name : 'bzsm',
	renderTo : 'divBzsm',
	maxLength : 100,
	height : 80,
	width : 473
})

// *********** 功能处理 ***********//
function pzChooseHandler() {
	var indicatTree = new Ext.tree.TreePanel({
		id : 'resTree',
		autoScroll : true,
		autoHeight : false,
		height : 170,
		width : 150,
		originalValue : "",
		animate : false,
		containerScroll : true,
		defaults : {
			bodyStyle : 'padding:0px'
		},
		border : false,
		hideBorders : true,
		rootVisible : false,
		lines : false,
		bodyBorder : false,
		root : new Ext.tree.AsyncTreeNode({
			text : '所有表单',
			loader : new Ext.tree.TreeLoader({
				dataUrl : link6
			}),
			expanded : true
		}),
		submitChange : function() {
			var attrs = indicatTree.getSelectionModel().selNode.attributes;
			if (!attrs.isItem) {
				alert("您选择的是分类,请选择分类下的项");
				return;
			}
			hdnGllch.setValue(attrs.id);
			txtPmc.setValue(attrs.text);
			indicatWindow.close();
		}
	});
	var indicatWindow = new Ext.Window({
		layout : 'fit',
		title : '表单选择',
		width : 250,
		height : 350,
		frame : true,
		items : indicatTree,
		buttons : [{
			text : '确定',
			handler : function() {
				indicatTree.submitChange();
			}
		}, {
			text : '取消',
			handler : function() {
				indicatWindow.close();
			}
		}]
	});
	// 显示窗口
	indicatWindow.show();
}

// 初始化
function load() {
	if (id == null || id == '') {
		txtCjr.setValue(curPersonName);
		dtCjsj.setValue(formatDate(new Date(), "yyyy-MM-dd"));
		return;
	}
	myMaskLoad.show();
	Ext.Ajax.request({
		url : link3,
		method : 'POST',
		params : {
			id : id
		},
		success : function(ajax) {
			var responseText = ajax.responseText;
			var obj = Ext.decode(responseText);
			myMaskLoad.hide();
			if (obj.success) {
				var data = obj.data[0];
				cmbPfl.setValue(data.pfl);
				txtPmc.setValue(data.pmc);
				txtNm.setValue(data.nm);
				cmbCjbm.setRawValue(data.cjbm);
				txtCjr.setValue(data.cjr);
				dtCjsj.setValue(formatDate(new Date(data.cjsj['time']), "yyyy-MM-dd"));
				rdBzp.setValue(data.isBzp);
				rdPzt.setValue(data.pzt);
				txtBzsm.setValue(data.bzsm);
				hdnGllch.setValue(data.gllch);
			}
		},
		failure : function() {
			myMaskLoad.hide();
		}
	})
}
// 保存
function save() {
	if (!cmbPfl.isValid()) {
		alert('票分类不能为空');
		return;
	}
	if (!cmbCjbm.isValid()) {
		alert('创建部门不能为空');
		return;
	}
	if (!txtPmc.isValid()) {
		alert('请选择关联票');
		return;
	}
	if (formatDate(new Date(dtCjsj.getValue()), "yyyy-MM-dd") > formatDate(new Date(), "yyyy-MM-dd")) {
		alert("创建日期不能大于当前日期");
		return;
	}
	if (!txtNm.isValid()) {
		alert('内码输入不正确！');
		return;
	}
	myMaskSave.show();
	Ext.Ajax.request({
		url : link4,
		method : 'POST',
		params : {
			pfl : cmbPfl.getValue(),
			pmc : txtPmc.getValue(),
			gllch : hdnGllch.getValue(),
			cjbm : cmbCjbm.getValue(),
			cjr : txtCjr.getValue(),
			cjsj : formatDate(new Date(dtCjsj.getValue()), "yyyy-MM-dd"),
			isBzp : rdBzp.getGroupValue(),
			pzt : rdPzt.getGroupValue(),
			bzsm : txtBzsm.getValue(),
			id : hdnId.getValue(),
			nm : txtNm.getValue()
		},
		success : function(ajax) {
			myMaskSave.hide();
			var resText = ajax.responseText;
			var obj = Ext.decode(resText);
			if (obj.success) {
				alert('保存成功');
				window.returnValue = "true";
				window.close();
			} else {
				alert(obj.msg)
			}
		},
		failure : function() {
			myMaskSave.hide();
		}
	})
}
