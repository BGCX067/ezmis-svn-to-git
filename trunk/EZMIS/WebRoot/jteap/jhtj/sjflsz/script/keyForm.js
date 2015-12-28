var KeyForm = function(id,kid,isEdit) {
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;
	
	
	
	var icodeCell=new Ext.app.UniqueTextField( {
						id : 'icode',
						name : 'icode',
						fieldLabel : '关键字编码',
						allowBlank : false,
						maxLength : 20,
						width : 150,
						notUniqueText : '该名称称已被使用，请使用其他名称',
						url : link10+"?kid=" + kid,
						regex:filterName
					});
	
	var icodeInfo=new Ext.app.LabelPanel('请填写关键字编码(大写字母加数字)');

	
	var inameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "关键字名称",
		name : "iname",
		readOnly : false
	});
	
	var inameInfo=new Ext.app.LabelPanel('请填写关键字名称');
	
	
	var dict_1=$dictList("SJLX");
	var lxds = new Ext.data.Store( {
		data: {rows:dict_1},
		reader : new Ext.data.JsonReader( {
			root : 'rows',
			id : 'id'
		}, ['key', 'value', 'id'])
	});
	
	// 
	var lxCell = new Ext.form.ComboBox( {
		hiddenName : 'lx',// 真正接受的名字
		store : lxds,// 数据源
		width : 150,
		fieldLabel : '数据类型',
		displayField : 'key',// 数据显示列名
		valueField : 'value',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		selectOnFocus : true,
		emptyText : '选择数据类型'
	});
	
	var cdName = /^\s*[0-9]{1,4}\s*$/;
	
	var cdCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "长度",
		name : "cd",
		readOnly : false,
		regex:cdName
	});
	
	
	var iorderName = /^\s*[0-9]{1,4}\s*$/;
	var iorderCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "iorder",
		readOnly : false,
		regex:iorderName
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
						items : [icodeCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [icodeInfo]
					}, {
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [inameCell]
					},{
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [inameInfo]
					}, {
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [lxCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [cdCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [iorderCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : id
					},{
						xtype : 'hidden',
						name : 'kid',
						id : 'kid',
						value : kid
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
		
		var param = Form.serialize($("myForm"));
		AjaxRequest_Sync(link11,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					alert("保存成功");
					window.returnValue = "true";
					window.close();
				}
		});
	}

	//加载数据
	this.loadData = function() {
		if(id!=""){
			var param={};
			param.id=id;
			AjaxRequest_Sync(link12,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					var data=responseObj.data[0];
					icodeCell.setValue(data.icode);
					icodeCell.setDisabled(true);
					inameCell.setValue(data.iname);
					lxCell.setValue(data.lx);
					cdCell.setValue(data.cd);
					iorderCell.setValue(data.iorder);
				}
			});
		}
	}

	KeyForm.superclass.constructor.call(this, {
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

Ext.extend(KeyForm, Ext.Panel, {});
