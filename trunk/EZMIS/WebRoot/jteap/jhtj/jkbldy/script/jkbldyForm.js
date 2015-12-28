var JkbldyForm = function(jkbldyid,isEdit) {
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;

	var vnameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "变量名",
		name : "vname",
		readOnly : false,
		regex:filterName
	});
	
	var vnameInfo=new Ext.app.LabelPanel('请填写变量名(大写字母加数字)');
	
	var cvnameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "变量中文名",
		name : "cvname",
		readOnly : false
	});
	
	var cvnameInfo=new Ext.app.LabelPanel('请填写变量中文名');

	var comboFieldType=new Ext.form.ComboBox({
		xtype:'combo',
		store: new Ext.data.SimpleStore({fields: ["retrunValue", "displayText"],data: [['1','字符串'],['2','数值'],['3','日期']]}),
		valueField :"retrunValue",
		displayField: "displayText",
		mode: 'local',
		triggerAction: 'all',
		blankText:'请选择类型',
		emptyText:'请选择类型',
		hiddenName:'vtype',
		selectOnFocus :true,
		fieldLabel: '变量类型',
		width : 80
	});
	
	var vorderName=/^\s*[0-9]{1,6}\s*$/;
	var vorderCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "vorder",
		readOnly : false,
		regex:vorderName
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
						items : [vnameCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [vnameInfo]
					}, {
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [cvnameCell]
					},{
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [cvnameInfo]
					}, {
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [comboFieldType]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [vorderCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : jkbldyid
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
		if(jkbldyid!=""){
			vnameCell.setDisabled(true);
			var loadParam={};
			loadParam.id=jkbldyid;
			AjaxRequest_Sync(link7,loadParam,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				var data=responseObj.data[0];
				vnameCell.setValue(data.vname);
				cvnameCell.setValue(data.cvname);
				comboFieldType.setValue(data.vtype);
				vorderCell.setValue(data.vorder);
			});
		}
	}

	JkbldyForm.superclass.constructor.call(this, {
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

Ext.extend(JkbldyForm, Ext.Panel, {});
