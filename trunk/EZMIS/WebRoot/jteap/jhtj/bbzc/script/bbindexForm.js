var BbindexForm = function(id,flid,isEdit) {
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;

	var bbbmCell=new Ext.app.UniqueTextField( {
		id : 'bbbm',
		name : 'bbbm',
		fieldLabel : '报表编码',
		allowBlank : false,
		width : 150,
		maxLength : 50,
		notUniqueText : '该名称称已被使用，请使用其他名称',
		url : link14+"?flid="+flid,
		regex:filterName
	});
	
	//var flbmInfo=new Ext.app.LabelPanel('请填写分类编码(大写字母加数字)');
	
	var bbmcCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "报表名称",
		name : "bbmc",
		readOnly : false
	});
	
	//var flmcInfo=new Ext.app.LabelPanel('请填写分类名称');
	
	var bzCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		fieldLabel : "备注",
		name : "bz",
		readOnly : false
	});
	
	var sortnoName = /^\s*[0-9]{1,4}\s*$/;
	var sortnoCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "sortno",
		readOnly : false,
		regex:sortnoName
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
						items : [bbbmCell]
					},{
						// 第一行布局
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [bbmcCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [sortnoCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [bzCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : id
					},{
						xtype : 'hidden',
						name : 'flid',
						id : 'flid',
						value : flid
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
		if(id==""){
			if(!simpleForm.form.isValid()){
				alert('数据校验失败，请检查填写的数据格式是否正确');
				return;
			}
		}
		var params = Form.serialize($("myForm"));
		var myAjax = new Ajax.Request(link10, {
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
			AjaxRequest_Sync(link11,loadParam,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				var data=responseObj.data[0];
				bbbmCell.setValue(data.bbbm);
				bbbmCell.setDisabled(true);
				bbmcCell.setValue(data.bbmc);
				bzCell.setValue(data.bz);
				sortnoCell.setValue(data.sortno);
			});
		}
	}

	BbindexForm.superclass.constructor.call(this, {
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

Ext.extend(BbindexForm, Ext.Panel, {});
