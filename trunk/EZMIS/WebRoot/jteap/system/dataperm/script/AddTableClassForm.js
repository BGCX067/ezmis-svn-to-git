var AddTableClassForm = function(id) {
	// ID
	var idCell = new Ext.form.Hidden( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "ID",
		name : "id",
		readOnly : false
	});
	var tablenameCell = new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "表名",
		name : "tablename",
		readOnly : false
	});

	var tablecnameCell = new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "表中文名",
		name : "tablecname",
		readOnly : false
	});

	var classnameCell = new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "类名",
		name : "classname",
		readOnly : false
	});

	var classcnameCell = new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "类中文名",
		name : "classcname",
		readOnly : false
	});

	var classpathCell = new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "包路径",
		name : "classpath",
		readOnly : false
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
		labelWidth : 70, // 标签宽度
		items : [ {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [{
				columnWidth : 1,
				layout : 'form',
				border : false,
				items : [idCell]
			}]
		}, {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [tablenameCell]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [tablecnameCell]
			}]
		}, {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [classnameCell]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [classcnameCell]
			}]
		}, {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [{
				columnWidth : 1,
				layout : 'form',
				border : false,
				items : [classpathCell]
			}]
		}],
		buttons : [ {
			id : 'saveButton',
			text : '确定',
			handler : function() {
				ok();
			}
		}, {
			text : '取消',
			handler : function() {
				window.close();
			}
		}]
	});

	/**
	 * 保存
	 */
	function ok() {
		// var select=tableGrid.getSelectionModel().getSelections()[0];//选择信息
		// document.location.href=link6+"&id="+select.json.id;
		var param = {};
		param.id=$("id").value;
		param.tablename=$("tablename").value;
		param.tablecname=$("tablecname").value;
		param.classname=$("classname").value;
		param.classcname=$("classcname").value;
		param.classpath=$("classpath").value;
		//param.charset=true;
		var bResult = false;
		var myAjax = new Ajax.Request(link17, {
			method : 'post',
			parameters : param,
			asynchronous : true,// 同步调用
				onComplete : function(req) {
					var responseText = req.responseText;
					var responseObj = responseText.evalJSON();
					if (responseObj.success == true) {
						bResult = true;
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
	
	this.loadData = function() {
		// 更新的时候
		if (id != "") {
			Ext.Ajax.request( {
				url : link16 + "?id=" + id,
				method : 'post',
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					if(obj.success){
						var data = obj.data[0];
						$("id").value=data.id;
						$("tablename").value=data.tablename;
						$("tablecname").value=data.tablecname;
						$("classname").value=data.classname;
						$("classcname").value=data.classcname;
						$("classpath").value=data.classpath;
					}
				}
			});
		}
	}

	AddTableClassForm.superclass.constructor.call(this, {
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

Ext.extend(AddTableClassForm, Ext.Panel, {});