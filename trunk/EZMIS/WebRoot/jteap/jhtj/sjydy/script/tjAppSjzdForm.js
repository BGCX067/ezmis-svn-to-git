var TjAppSjzdForm = function(id) {
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;

	var fnameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "变量名",
		name : "fname",
		readOnly : true
	});
	
	
	var cfnameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "变量中文名",
		name : "cfname",
		readOnly : false
	});
	
	var forderName = /^\s*[0-9]{1,4}\s*$/;
	var forderCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "forder",
		readOnly : false,
		regex:forderName
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
						items : [fnameCell]
					}, {
						// 第一行布局
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [cfnameCell]
					}, {
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [forderCell]
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
		var myAjax = new Ajax.Request(link19, {
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
			AjaxRequest_Sync(link18,loadParam,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				var data=responseObj.data[0];
				fnameCell.setValue(data.fname);
				cfnameCell.setValue(data.cfname);
				forderCell.setValue(data.forder);
			});
		}
	}

	TjAppSjzdForm.superclass.constructor.call(this, {
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

Ext.extend(TjAppSjzdForm, Ext.Panel, {});
