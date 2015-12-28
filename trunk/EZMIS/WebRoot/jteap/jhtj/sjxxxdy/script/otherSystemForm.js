var OtherSystemForm = function(id,isEdit) {
	var myForm=this;
	
	var sjyid; //记录系统ID
	var fname; //记录数据字段名
	var cfname;//记录数据字段中文名
	
	var systemNameCell=new Ext.form.TextField( {
		disabled : false,
		width : 355,
		maxLength : 50,
		fieldLabel : "应用系统",
		name : "systemName",
		readOnly : true
	});
	
	
	var serverCell=new Ext.form.TextField( {
		disabled : false,
		width : 120,
		maxLength : 50,
		fieldLabel : "服务器",
		name : "server",
		readOnly : true
	});
	
	
	var vnameCell=new Ext.form.TextField( {
		width : 120,
		maxLength : 50,
		fieldLabel : "数据源名",
		name : "vname",
		readOnly : true
	});
	
	
	var dbNameCell=new Ext.form.TextField( {
		width : 120,
		maxLength : 50,
		fieldLabel : "数据库",
		name : "dbName",
		readOnly : true
	});
	

	var cvnameCell = new Ext.form.TextField( {
		width : 120,
		maxLength : 50,
		fieldLabel : "数据源中文名",
		name : "cvname",
		readOnly : true
	});
	
	
	var userIdCell=new Ext.form.TextField( {
		width : 120,
		maxLength : 50,
		fieldLabel : "用户名",
		name : "userId",
		readOnly : true
	});
	
	
	
	var fnameds = new Ext.data.Store( {
		proxy : new Ext.data.ScriptTagProxy( {
			url : link15
		}),
		reader : new Ext.data.JsonReader( {
		}, ['fname', 'cfname']),
		remoteSort : true
	});
	
	
	
	// ComboBox取数
	var fnameCell = new Ext.form.ComboBox( {
		hiddenName : 'fnames',// 真正接受的名字
		store : fnameds,// 数据源
		width : 120,
		fieldLabel : '数据字段名',
		displayField : 'fname',// 数据显示列名
		valueField : 'fname',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		selectOnFocus : true,
		editable:false
	});
	
	
	fnameCell.on("select",function(combo,record,index){
		var val=combo.getValue();
		setCfnameByFname(val);
	}) ;
	
	
	
	
	var userPwdCell=new Ext.form.TextField( {
		width : 120,
		maxLength : 50,
		fieldLabel : "密码",
		name : "userPwd",
		readOnly : true,
		inputType : 'password'
	});
	
	
	
	var cfnameCell=new Ext.form.TextField( {
		width : 120,
		maxLength : 50,
		fieldLabel : "数据字段中文名",
		name : "cfname",
		readOnly : true
	});
	
	
	var sqlstrCell = new Ext.form.TextArea( {
		id : 'sqlstr',
		name : 'sqlstr',
		fieldLabel : '数据源sql信息',
		width : 355,
		readOnly : true,
		height:200
		//anchor : '93%'
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
		labelWidth : 100, // 标签宽度
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
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [systemNameCell]
					}, {
					// 第二行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [serverCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [vnameCell]
					},{
						// 第三行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dbNameCell]
					}, {
						
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [cvnameCell]
					},{
						// 第四行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [userIdCell]
					},{
						
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [fnameCell]
					},{
						// 第五行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [userPwdCell]
					},{
						
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [cfnameCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [sqlstrCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : id
					},{
						xtype : 'hidden',
						name : 'sid',
						id : 'sid',
						value : id
					}]
			}]	
		}],
		buttons : [{
				id : 'preButton',
				text : '上一步',
				handler : function() {
					back();
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

	function save() {
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
			}
		});
	}
	
	function packFormValue(){
		//上一页的数据
		var param={};
		param.id=id;
		param.iname=iname;
		param.iorder=iorder;
		param.dw=dw;
		param.sjlx=sjlx;
		if(cd!=""){
			param.cd=cd;
		}
		if(xsw!=""){
			param.xsw=xsw;
		}
		if(id==""){
			param.kid=kid;
			param.item=item;
			param.itype=itype;
		}
		param.qsfs=qsfs;
		param.isvisible=isvisible;
		//当前页的数据
		param.vname=vnameCell.getValue();
		param.sid=$("sid").value;
		param.fname=fnameCell.getValue();
		//需要清空的数据
		param.cexp="";
		param.corder="";
		param.dname="";
		param.dfunId="";
		return param;
	}

	//加载数据
	this.loadData = function(sysid,sjyid) {
		$("sid").value=sysid;//把选择的系统ID赋到当前form中
		var param={};
		param.sysid=sysid;//系统ID
		param.sjyid=sjyid;//数据源ID
		AjaxRequest_Sync(link15,param,function(request){
			var responseText = request.responseText;
			var responseObj = responseText.evalJSON();
			if(responseObj.success){
				fnameds.removeAll();
				fnameds.loadData(responseObj.list[0],true);
				var record=fnameds.getAt(0);
				if(record!=null){
					fnameCell.setValue(record.json.fname);
					cfnameCell.setValue(record.json.cfname);
				}
				var data=responseObj.data[0];
				systemNameCell.setValue(data.systemName);
				serverCell.setValue(data.server);
				dbNameCell.setValue(data.dbName);
				userIdCell.setValue(data.userId);
				userPwdCell.setValue(data.userPwd);
				vnameCell.setValue(data.vname);
				cvnameCell.setValue(data.cvname);
				sqlstrCell.setValue(data.sqlstr);
			}
		});
	}
	
	this.initData=function(){
		if(id!=""){
			var param={};
			param.id=id;
			//加载初始化数据
			AjaxRequest_Sync(link7,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					var data=responseObj.data[0];
					var vname=data.vname;
					param.vname=vname;
					//根据vname查找appio对象
					AjaxRequest_Sync(link18,param,function(res){
						var resText = res.responseText;
						var resObj = resText.evalJSON();
						if(resObj.success){
							var sjy=resObj.data[0];
							//取得系统ID和数据源ID初始化页面数据
							myForm.loadData(data.sid,sjy.id);
							//var myNode=leftTree.getNodeById(data.sid);
							//alert(myNode);
							//myNode.select();
							fnameCell.setValue(data.fname);
							setCfnameByFname(data.fname);
							
							sjyid=sjy.id;
							fname=data.fname;
						}
					});
				}
			});
		}
	}
	
	/*
	 *修改的时候如果选了别的数据源，然后又选回来的时候还原它的默认值
	 */
	this.setDefaultData=function(sjy){
		if(sjy==sjyid){
			fnameCell.setValue(fname);
			cfnameCell.setValue(cfname);
		}
	}
	
	
	/**
	 * 设置中文字段名
	 */
	function setCfnameByFname(fname){
		var param={};
		param.fname=fname;
		AjaxRequest_Sync(link19,param,function(request){
			var responseText = request.responseText;
			var responseObj = responseText.evalJSON();
			if(responseObj.success){
				cfnameCell.setValue(responseObj.cfname);
				cfname=responseObj.cfname;//记录数据字段中文名
			}
		});
	}

	OtherSystemForm.superclass.constructor.call(this, {
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

Ext.extend(OtherSystemForm, Ext.Panel, {});
