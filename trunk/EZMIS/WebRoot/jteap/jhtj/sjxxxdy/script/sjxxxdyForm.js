var SjxxxdyForm = function(id,kid,isEdit) {
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;
	
	var itemCell=new Ext.app.UniqueTextField( {
						id : 'item',
						name : 'item',
						fieldLabel : '信息项编码',
						allowBlank : false,
						maxLength : 20,
						width : 150,
						notUniqueText : '该名称称已被使用，请使用其他名称',
						url : link9+"?kid="+kid,
						regex:filterName
					});
	
	
	var inameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "信息项名称",
		name : "iname",
		readOnly : false
	});
	
	
	var iorderCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "信息项顺序",
		name : "iorder",
		readOnly : false
	});
	
	
	var dwCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		fieldLabel : "单位",
		name : "dw",
		readOnly : false
	});

	
	var dict_1=$dictList("YWLX");
	var sjlxds = new Ext.data.Store( {
		data: {rows:dict_1},
		reader : new Ext.data.JsonReader( {
			root : 'rows',
			id : 'id'
		}, ['key', 'value', 'id'])
	});
	
	// 
	var sjlxCell = new Ext.form.ComboBox( {
		hiddenName : 'sjlx',// 真正接受的名字
		store : sjlxds,// 数据源
		width : 150,
		fieldLabel : '业务类型',
		displayField : 'key',// 数据显示列名
		valueField : 'value',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		editable :false,
		selectOnFocus : true,
		emptyText : '选择业务类型'
	});
	
	
	var cdCell=new Ext.form.TextField( {
		width : 150,
		maxLength : 50,
		disabled :true,
		fieldLabel : "数据长度",
		name : "cd",
		readOnly : false
	});
	
	
	var xswCell=new Ext.form.TextField( {
		width : 150,
		maxLength : 50,
		disabled :true,
		fieldLabel : "小数位数",
		name : "xsw",
		readOnly : false
	});
	
	
	var dict_2=$dictList("SJLX2");
	var itypeds = new Ext.data.Store( {
		data: {rows:dict_2},
		reader : new Ext.data.JsonReader( {
			root : 'rows',
			id : 'id'
		}, ['key', 'value', 'id'])
	});
	
	// 
	var itypeCell = new Ext.form.ComboBox( {
		hiddenName : 'itype',// 真正接受的名字
		store : itypeds,// 数据源
		width : 150,
		fieldLabel : '数据类型',
		displayField : 'key',// 数据显示列名
		valueField : 'value',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		editable :false,
		selectOnFocus : true,
		emptyText : '选择数据类型'
	});
	
	
	itypeCell.on("select",function(combo,record,index){
		var val=combo.getValue();
		setFormStatesByItype(val);
	}) ;
	
	
	
	var dict_3=$dictList("QSFS");
	var qsfsds = new Ext.data.Store( {
		data: {rows:dict_3},
		reader : new Ext.data.JsonReader( {
			root : 'rows',
			id : 'id'
		}, ['key', 'value', 'id'])
	});
	
	// 
	var qsfsCell = new Ext.form.ComboBox( {
		hiddenName : 'qsfs',// 真正接受的名字
		store : qsfsds,// 数据源
		width : 150,
		fieldLabel : '取数方式',
		displayField : 'key',// 数据显示列名
		valueField : 'value',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		editable :false,
		selectOnFocus : true,
		emptyText : '选择取数方式'
	});
	
	
	qsfsCell.on("select",function(combo,record,index){
		var val=combo.getValue();
		setFormStatesByQsfs(val);
	}) ;
	
	
	
	var dnameds = new Ext.data.Store( {
		proxy : new Ext.data.ScriptTagProxy( {
			url : link10
		}),
		reader : new Ext.data.JsonReader( {
		}, ['dname', 'dcname']),
		remoteSort : true
	});
	
	dnameds.load();
	
	// ComboBox取数
	var dnameCell = new Ext.form.ComboBox( {
		hiddenName : 'dname',// 真正接受的名字
		store : dnameds,// 数据源
		width : 150,
		fieldLabel : '应用接口dll',
		displayField : 'dcname',// 数据显示列名
		valueField : 'dname',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		selectOnFocus : true,
		disabled:true
	});
	
	
	var dfunIdCell=new Ext.form.TextField( {
		width : 150,
		maxLength : 50,
		fieldLabel : "dll功能号",
		name : "dfunId",
		readOnly : false,
		disabled :true
	});
	
	
	var isvisibleCell=new Ext.form.ComboBox({
		hiddenName : 'isvisible',// 真正接受的名字
		store: new Ext.data.SimpleStore({fields: ["retrunValue", "displayText"],data: [['1','正常'],['2','停用']]}),
		valueField :"retrunValue",
		displayField: "displayText",
		mode: 'local',
		triggerAction: 'all',
		selectOnFocus :true,
		fieldLabel: '使用选项',
		width : 150,
		editable :false
	});
	
	isvisibleCell.setValue(1);

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
		//monitorValid : true, // 绑定验证
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
						items : [itemCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [inameCell]
					}, {
						// 第二行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [iorderCell]
					},{
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dwCell]
					}, {
						// 第三行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [itypeCell]
					},{
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [cdCell]
					},{
						// 第四行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [xswCell]
					},{
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [sjlxCell]
					},{
						// 第五行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [qsfsCell]
					},{
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dnameCell]
					},{
						// 第六行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dfunIdCell]
					},{
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [isvisibleCell]
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
				id : 'nextButton',
				text : '下一步',
				handler : function() {
					go();
				}
			},{
			id : 'saveButton',
			//formBind : true,
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
	

	
	function back() {
		window.parent.document.all.formFrame.style.display = "inline";
		window.parent.document.all.next.style.display = "none";
	}
	
	function go(){
		var param=packFormValue();
		var qsfs=param.qsfs;
		var url="";
		if(qsfs==2){
			url=link13;
		}else if(qsfs==3){
			url=link16;
		}
		window.parent.parent.document.all.formFrame.style.display="none";
		window.parent.parent.document.all.next.style.display="inline";
		simpleForm.getForm().getEl().dom.target="next";
		simpleForm.getForm().getEl().dom.action=url;
		simpleForm.getForm().getEl().dom.submit();// 提交！
	}

	function save() {
		if(id==""){
			if(!simpleForm.form.isValid()){
				alert('数据校验失败，请检查填写的数据格式是否正确');
				return;
			}
		}
		//var param = Form.serialize($("myForm"));
		var param=packFormValue();
		AjaxRequest_Sync(link8,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					AjaxRequest_Sync(link12,param,function(res){
						var resText = res.responseText;
						var resObj = resText.evalJSON();
						if(resObj.success){
							alert("保存成功!");
							window.returnValue = "true";
							window.close();
						}else{
							alert("创建表结构失败!");
						}
					});
				}else{
					alert("保存失败!");
				}
		});
	}
	
	function packFormValue(){
		var param={};
		param.id=$("id").value;
		param.iname=inameCell.getValue();
		param.iorder=iorderCell.getValue();
		param.dw=dwCell.getValue();
		param.sjlx=sjlxCell.getValue();
		param.cd=cdCell.getValue();
		param.xsw=xswCell.getValue();
		if(id==""){
			param.kid=kid;
			param.item=itemCell.getValue();
			param.itype=itypeCell.getValue();
		}
		param.qsfs=qsfsCell.getValue();
		param.dname=dnameCell.getValue();
		param.dfunId=dfunIdCell.getValue();
		param.isvisible=isvisibleCell.getValue();
		//需要清空的值
		param.sid="";
		param.fname="";
		param.vname="";
		param.cexp="";
		param.corder="";

		return param;
	}

	//加载数据
	this.loadData = function() {
		$("saveButton").disabled=true;
		$("nextButton").disabled=true;
		if(id!=""){
			var param={};
			param.id=id;
			AjaxRequest_Sync(link7,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					var data=responseObj.data[0];
					var qsfs=data.qsfs;
					var itype=data.itype;
					//基本值
					itemCell.setValue(data.item);
					inameCell.setValue(data.iname);
					iorderCell.setValue(data.iorder);
					dwCell.setValue(data.dw);
					itypeCell.setValue(itype);
					cdCell.setValue(data.cd);
					xswCell.setValue(data.xsw);
					sjlxCell.setValue(data.sjlx);
					qsfsCell.setValue(qsfs);
					isvisibleCell.setValue(data.isvisible);
					//不能改变的项
					itemCell.setDisabled(true);
					itypeCell.setDisabled(true);
					//设置form的状态
					setFormStatesByQsfs(qsfs);
					setFormStatesByItype(itype);
				}
			});
		}
	}
	
	function setFormStatesByQsfs(qsfs){
		if(qsfs==1){
			$("saveButton").disabled=false;
			$("nextButton").disabled=true;
			dnameCell.setDisabled(true);
			dnameCell.setValue("");
			dfunIdCell.setDisabled(true);
			dfunIdCell.setValue("");
		}else if(qsfs==2){
			$("saveButton").disabled=true;
			$("nextButton").disabled=false;
			dnameCell.setDisabled(true);
			dnameCell.setValue("");
			dfunIdCell.setDisabled(true);
			dfunIdCell.setValue("");
		}else if(qsfs==3){
			$("saveButton").disabled=true;
			$("nextButton").disabled=false;
			dnameCell.setDisabled(true);
			dnameCell.setValue("");
			dfunIdCell.setDisabled(true);
			dfunIdCell.setValue("");
		}else if(qsfs==4){
			$("saveButton").disabled=false;
			$("nextButton").disabled=true;
			dnameCell.setDisabled(false);
			dfunIdCell.setDisabled(false);
		}
	}
	
	
	function setFormStatesByItype(itype){
		if(itype=="FLOAT"){
			xswCell.setDisabled(false);
			cdCell.setDisabled(true);
			cdCell.setValue("");
		}else if(itype=="INT"){
			xswCell.setDisabled(true);
			cdCell.setDisabled(true);
			xswCell.setValue("");
			cdCell.setValue("");
		}else if(itype=="VARCHAR"){
			xswCell.setDisabled(true);
			cdCell.setDisabled(false);
			xswCell.setValue("");
		}
	}

	SjxxxdyForm.superclass.constructor.call(this, {
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

Ext.extend(SjxxxdyForm, Ext.Panel, {});
