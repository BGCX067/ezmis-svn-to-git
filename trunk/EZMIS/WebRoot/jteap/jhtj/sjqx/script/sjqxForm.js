var SjqxForm = function(flid,bbid) {
	// 获得选择方式
	var chooseType = "multipe";
	// 获得是否选择完整路径
	var checkPath = false;
	// 获得是否只选择末端节点
	var checkLeaf = false;
	var flTree = new QxTree(chooseType, checkPath, checkLeaf ,flid,"数据分类",link6);
	
	var bbTree = new QxTree(chooseType, checkPath, checkLeaf ,bbid,"报表分类",link7);

	var simpleForm = new Ext.FormPanel( {
		labelAlign : 'left',
		buttonAlign : 'right',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
		height : 420,
		waitMsgTarget : true,
		border:false,
		id : 'myForm',
		frame : false, // 圆角风格
		labelWidth : 80, // 标签宽度
		monitorValid : true, // 绑定验证
		items : [{
			frame : false,
			border : false,
			items : [{
				layout : 'column',
				border : false,
				labelSeparator : ':',
				defaults : {
					blankText : '必填字段'
				},
				items : [ {
					// 第一行布局
						columnWidth : .5,
						layout : 'form',
						id:'flTree',
						border : false,
						items : [flTree]
					},{
					// 第一行布局
						columnWidth : .5,
						layout : 'form',
						id:'bbTree',
						border : false,
						items : [bbTree]
					}]
			}]	
		}]
	});

	function save() {
		if(!simpleForm.form.isValid()){
			alert('数据校验失败，请检查填写的数据格式是否正确');
			return;
		}
		var params = Form.serialize($("myForm"));
		var myAjax = new Ajax.Request(link8, {
			method : 'post',
			parameters : params,
			asynchronous : true,// 同步调用
				onComplete : function(req) {
					var responseText = req.responseText;
					var responseObj = responseText.evalJSON();
					if (responseObj.success == true) {
						window.returnValue = "true";
						window.close();
					} else {
						// txtObj.focus(true,true);
						Ext.MessageBox.alert('Status', responseObj.msg);
			}
		},
		onFailure : function(e) {
			alert("验证公式失败：" + e);
		}
		});
	}

	//加载数据
	this.getFlTree = function() {
		return flTree;
	}
	
	this.getBbTree = function() {
		return bbTree;
	}

	SjqxForm.superclass.constructor.call(this, {
		width : '100%',
		height : 420,
		modal : true,
		autoScroll : true,
		layout : 'column',
		plain : false,
		resizable : true,
		region : 'center',
		bodyStyle : 'padding:1px;',
		items : [{
			border : false,
			columnWidth : 1,
			layout : 'form',
			items : simpleForm
		}]
	});

}

Ext.extend(SjqxForm, Ext.Panel, {});
