var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'pzid'
})

var cmbPfl = new Ext.form.TextField({
	renderTo : 'divPfl',
	readOnly : true,
	width : 200
})

// 票名称
var txtPmc = new Ext.form.TextField({
	name : 'pmc',
	renderTo : 'divPmc',
	readOnly : true,
	width : 200
})
var hdnGllch = new Ext.form.Hidden({
	name : 'gllch'
})

// 创建部门
var cmbCjbm = new Ext.form.TextField({
	renderTo : 'divCjbm',
	width : 200,
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
var dtCjsj = new Ext.form.TextField({
	name : 'cjsj',
	renderTo : 'divCjsj',
	readOnly : true,
	width : 200
});

// 是否标准票
var rdBzp = new Ext.form.RadioGroup({
	renderTo : 'divIsBzp',
	width : 130,
	columns : 2,
	readOnly : true,
	disabled : true,
	items : [{
		boxLabel : '是',
		name : 'isBzp',
		inputValue : '1'
	}, {
		boxLabel : '否',
		name : 'isBzp',
		inputValue : '2'
	}]
})

// 票状态
var rdPzt = new Ext.form.RadioGroup({
	renderTo : 'divPzt',
	width : 130,
	columns : 2,
	readOnly : true,
	disabled : true,
	items : [{
		boxLabel : '有效',
		name : 'pzt',
		inputValue : '1'
	}, {
		boxLabel : '无效',
		name : 'pzt',
		inputValue : '2'
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
	readOnly : true,
	width : 200
});

// 备注说明
var txtBzsm = new Ext.form.TextArea({
	name : 'bzsm',
	renderTo : 'divBzsm',
	maxLength : 100,
	readOnly : true,
	height : 80,
	width : 473
})

// *********** 功能处理 ***********//
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
				if ('czp' == data.pfl) {
					cmbPfl.setValue('操作票')
				} else if ('gzp' == data.pfl) {
					cmbPfl.setValue('工作票')
				}
				txtPmc.setValue(data.pmc);
				cmbCjbm.setValue(data.cjbm);
				txtNm.setValue(data.nm);
				txtCjr.setValue(data.cjr);
				dtCjsj.setValue(formatDate(new Date(data.cjsj['time']), "yyyy-MM-dd"));
				rdBzp.setValue(data.isBzp);
				rdPzt.setValue(data.pzt);
				txtBzsm.setValue(data.bzsm);
			}
		},
		failure : function() {
			myMaskLoad.hide();
		}
	})
}
