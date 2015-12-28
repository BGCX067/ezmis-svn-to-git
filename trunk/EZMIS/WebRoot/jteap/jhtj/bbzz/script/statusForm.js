var StatusForm = function(id,isEdit) {

	var dict_1=$dictList("BBZT");
	var statusds = new Ext.data.Store( {
		data: {rows:dict_1},
		reader : new Ext.data.JsonReader( {
			root : 'rows',
			id : 'id'
		}, ['key', 'value', 'id'])
	});
	
	// 
	var statusCell = new Ext.form.ComboBox( {
		hiddenName : 'status',// 真正接受的名字
		store : statusds,// 数据源
		width : 150,
		fieldLabel : '报表状态',
		displayField : 'key',// 数据显示列名
		valueField : 'value',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		editable :false,
		selectOnFocus : true,
		emptyText : '选择报表状态'
	});

	var simpleForm = new Ext.FormPanel( {
		labelAlign : 'left',
		buttonAlign : 'right',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		id : 'myForm',
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 80, // 标签宽度
		monitorValid : true, // 绑定验证
		items : [{
			frame : true,
			items : [{
				layout : 'column',
				border : false,
				labelSeparator : ':',
				defaults : {
					blankText : '必填字段'
				},
				items : [ {
					// 第一行布局
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [statusCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : id
					}]
			}]	
		}],
		buttons : [{
			id : 'saveButton',
			formBind : true,
			text : '保存',
			handler : function() {
				save();
			}
		}, {
			text : '取消',
			handler : function() {
				window.close();
			}
		}]
	});

	function save() {
		if(!simpleForm.form.isValid()){
			alert('数据校验失败，请检查填写的数据格式是否正确');
			return;
		}
		var params = Form.serialize($("myForm"));
		var myAjax = new Ajax.Request(link16, {
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
	this.loadData = function() {
		if(id!=""){
			var loadParam={};
			loadParam.id=id;
			AjaxRequest_Sync(link7,loadParam,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				var data=responseObj.data[0];
				statusCell.setValue(data.status);
			});
		}
	}

	StatusForm.superclass.constructor.call(this, {
		width : '100%',
		height : 420,
		modal : true,
		autoScroll : true,
		layout : 'column',
		plain : true,
		draggable : false,
		resizable : false,
		bodyStyle : 'padding:1px;',
		items : [{
			border : false,
			columnWidth : 1,
			layout : 'form',
			items : simpleForm
		}]
	});

}

Ext.extend(StatusForm, Ext.Panel, {});
