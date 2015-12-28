var TjdllioForm = function(tjdllioid,isEdit) {
	var filterName = /^\s*[a-zA-Z0-9_]{1,30}\s*$/;

	var dnameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "dll编码",
		name : "dname",
		readOnly : false,
		regex:filterName
	});
	
	var dnameInfo=new Ext.app.LabelPanel('请填写dll编码(大写字母或小写字母或数字)');
	
	var dcnameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "dll名称",
		name : "dcname",
		readOnly : false
	});
	
	var dcnameInfo=new Ext.app.LabelPanel('请填写dll名称');

	
	var dmsCell = new Ext.form.TextArea( {
		xtype : "textarea",
		fieldLabel : "dll描述",
		name : "dms",
		height : 50
	});
	
	var dorderName=/^\s*[0-9]{1,6}\s*$/;
	var dorderCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "dorder",
		readOnly : false,
		regex:dorderName
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
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dnameCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dnameInfo]
					}, {
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dcnameCell]
					},{
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dcnameInfo]
					}, {
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [dmsCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [dorderCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : tjdllioid
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
	this.loadData = function() {
		if(tjdllioid!=""){
			dnameCell.setDisabled(true);
			var loadParam={};
			loadParam.id=tjdllioid;
			AjaxRequest_Sync(link7,loadParam,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				var data=responseObj.data[0];
				dnameCell.setValue(data.dname);
				dcnameCell.setValue(data.dcname);
				dmsCell.setValue(data.dms);
				dorderCell.setValue(data.dorder);
			});
		}
	}

	TjdllioForm.superclass.constructor.call(this, {
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

Ext.extend(TjdllioForm, Ext.Panel, {});
