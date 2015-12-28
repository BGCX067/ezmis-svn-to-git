var AppSystemConnForm = function(dbsId) {
	Ext.form.Field.prototype.msgTarget = 'side';

	var addDbsWindow = this;
	this.dbsId = dbsId;
	
	
	this.dbUrl ="";

	var hdnId = new Ext.form.Hidden( {
		id : 'id',
		name : 'id',
		value:dbsId
	})
	
	var txtDbsName = new Ext.app.UniqueTextField( {
		id : 'name',
		name : 'name',
		fieldLabel : '连接名称',
		allowBlank : false,
		maxLength : 20,
		anchor : '93%',
		notUniqueText : '该名称称已被使用，请使用其他名称',
		url : link7+"?id=" + addDbsWindow.dbsId
	})
	var lbldbsName = new Ext.app.LabelPanel('连接名称,必填数据');
	
	//数据库配置对象
	var combStore = new Ext.data.SimpleStore({
       fields : ['typeid','dbShortName','driver','port','url'],
       data : [	['1', 'ORACLE','oracle.jdbc.driver.OracleDriver','1521','jdbc:oracle:thin:@$ComputerNameOrIP:$port:$dbName'], 
	       			['2', 'SQLSERVER','net.sourceforge.jtds.jdbc.Driver','1433','jdbc:jtds:sqlserver://$ComputerNameOrIP:$port/$dbName'],
	       			['3', 'DB2','com.ibm.db2.jdbc.net.DB2Driver','6789','jdbc:db2://@$ComputerNameOrIP:$port/$dbName'], 
	       			['4', 'MySql','com.mysql.jdbc.Driver','3306','jdbc:mysql://@$ComputerNameOrIP:$port/$dbName'], 
	       			['5', 'Postgresql','org.postgresql.Driver','5432','jdbc:postgresql://@$ComputerNameOrIP:$port/$dbName'],
	       			['6','Sybase','com.sybase.jdbc2.jdbc.SybDriver','2638','jdbc:sybase:Tds:@$ComputerNameOrIP:$port:$dbName']
       		  ]
   	});
 
   // simple combobox test
   var combLinkTypeName = new Ext.form.ComboBox({
   		id : 'dbType',
		name : 'dbType',
       	store : combStore,//数据源
      	fieldLabel : '连接类型',
       	displayField :'dbShortName',//数据显示列名
       	valueField :'typeid',
       	mode :'local',//默认以'remote'作为数据源
       	triggerAction :'all',//单击下拉按钮时激发事件
       	typeAhead :true,//自动完成功能
       	selectOnFocus :true,
       	emptyText :'选择数据库'
  	 });
  	 
  	 //切换选择数据库类型时
  	 Ext.getCmp('dbType').addListener('beforeselect', function(comb, record, index) {
       addDbsWindow.dbUrl = record.data.url;
       txtDriveName.setValue(record.data.driver);
       txtPort.setValue(record.data.port);
       
       generateDbUrl(); 
    });

    
	generateDbUrl  = function	(){
		var url = addDbsWindow.dbUrl;
		if(txtAddress.getValue() != null  && txtAddress.getValue() != ""){
			url = url.replace("$ComputerNameOrIP",txtAddress.getValue());
		}
		
		if(txtDbName.getValue() != null  && txtDbName.getValue() != ""){
			url = url.replace("$dbName",txtDbName.getValue());
		}
		
		if(txtPort.getValue() != null  && txtPort.getValue() != ""){
			url = url.replace("$port",txtPort.getValue());
		}
		txtLinkString.setValue(url);
	}
	
    
	/*var txtLinkTypeName = new Ext.form.TextField( {
		id : 'linkTypeName',
		name : 'linkTypeName',
		fieldLabel : '数据库类型',
		allowBlank : false,
		maxLength : 50,
		anchor : '93%'
	})*/
	var lblLinkTypeName = new Ext.app.LabelPanel('连接类型,必填数据');
	
	var txtDriveName = new Ext.form.TextField( {
		id : 'className',
		name : 'className',
		fieldLabel : '驱动名称',
		allowBlank : false,
		maxLength : 100,
		anchor : '93%'
	})
	var lblDriveName = new Ext.app.LabelPanel('驱动名称,必填数据');
	
	var txtAddress = new Ext.form.TextField( {
		id : 'server',
		name : 'server',
		fieldLabel : '数据库地址',
		allowBlank : false,
		maxLength : 100,
		anchor : '93%'
	})
	var lblAddress= new Ext.app.LabelPanel('数据库地址,IP或者机器名');
	
	//修改地址
  	 Ext.getCmp('server').addListener('change', function(box, newV, oldV) {
      generateDbUrl(); 
    });
	
	
	var txtDbName = new Ext.form.TextField( {
		id : 'dbName',
		name : 'dbName',
		fieldLabel : '数据库名称',
		allowBlank : false,
		maxLength : 100,
		anchor : '93%'
	})
	var lblDbName = new Ext.app.LabelPanel('数据库名称,必填数据');
	//修改数据库名时
  	 Ext.getCmp('dbName').addListener('change', function(box, newV, oldV) {
      generateDbUrl(); 
    });
    
	var txtPort = new Ext.form.TextField( {
		id : 'port',
		name : 'port',
		fieldLabel : '连接端口',
		allowBlank : false,
		maxLength : 100,
		anchor : '93%'
	})
	var lblPort = new Ext.app.LabelPanel('数据库连接端口,必填数据');
	//修改端口时
  	 Ext.getCmp('port').addListener('change', function(box, newV, oldV) {
      generateDbUrl(); 
    });
    
	var txtUserName = new Ext.form.TextField( {
		id : 'userId',
		name : 'userId',
		fieldLabel : '用户名',
		allowBlank : false,
		maxLength : 100,
		anchor : '93%'
	})
	var lblUserName = new Ext.app.LabelPanel('用户名,必填数据');

	var txtPassword = new Ext.form.TextField( {
		id : 'userPwd',
		name : 'userPwd',
		fieldLabel : '密码',
		allowBlank : false,
		maxLength : 100,
		anchor : '93%',
		inputType : 'password'
	})
	var lblPassword = new Ext.app.LabelPanel('密码,必填数据');
	
	var txtLinkString = new Ext.form.TextArea( {
		id : 'url',
		name : 'url',
		fieldLabel : '连接字符串',
		allowBlank : false,
		maxLength : 100,
		readOnly : true,
		anchor : '93%'
	})
	//var lblLinkString = new Ext.app.LabelPanel('连接字符串,必填数据');
	
	
	var sortnoCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "sortno",
		readOnly : false
	});

	//var lblRemark = new Ext.app.LabelPanel('备注,必填数据');
	
	
	var myForm = new Ext.FormPanel( {
		labelAlign : 'left',
		buttonAlign : 'center',
		autoWidth : true,
		autoHeight : true,
		id : 'myForm',
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		monitorValid : true, // 绑定验证
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 80, // 标签宽度
		items : [{
			layout : 'column',
			border : false,
			labelSeparator : '：',
			defaults : {
				style : 'padding-left:15px'
			},
			items : [ {
				// 第一行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [hdnId, txtDbsName]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lbldbsName]
				}, {
					// 第二行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [combLinkTypeName]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblLinkTypeName]
				}, {
					// 第三行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtDriveName]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblDriveName]
				}, {
					// 第四行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtAddress]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblAddress]
				}, {
					// 第五行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtDbName]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblDbName]
				},{
					// 第七行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtUserName]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblUserName]
				},{
					// 第八行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtPassword]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblPassword]
				},{
					// 第六行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtPort]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					items : [lblPort]
				},{
					// 第九行布局
					columnWidth : .6,
					layout : 'form',
					border : false,
					items : [txtLinkString]
				},{columnWidth : 1,
					layout : 'form',
					border : false,
					items : [sortnoCell]
				},{columnWidth : 1,
					layout : 'form',
					border : false,
					items : [hdnId]
				}]
		}],
		buttons : [{
						text:'测试连接',
						handler:function(){
							//var params = Form.serialize($("myForm"));
							var params={};
							params.userId=$("userId").value;
							params.userPwd=$("userPwd").value;
							params.className=$("className").value;
							params.url=$("url").value;
							var myAjax = new Ajax.Request(link8, {
								method : 'post',
								waitMsg : '数据库尝试连接中，请稍候...',
								parameters : params,
								asynchronous : true,// 同步调用
									onComplete : function(req) {
										var responseText = req.responseText;
										var responseObj = responseText.evalJSON();
										if (responseObj.success == true) {
											alert("测试连接成功");
										}
								},
								onFailure : function(e) {
									alert('数据库连接失败');
								}
							});
						}
					},
					{
					text : '保存并关闭',
					formBind : true,
					handler : function() {
						if(!myForm.form.isValid()){
							alert('数据校验失败，请检查填写的数据格式是否正确');
							return;
						}
						
						var params = {};
						params.id=$("id").value;
						params.name=txtDbsName.getValue();
						params.dbType=combLinkTypeName.getValue();
						params.server=txtAddress.getValue();
						params.dbName=txtDbName.getValue();
						params.userId=txtUserName.getValue();
						params.userPwd=txtPassword.getValue();
						params.port=txtPort.getValue();
						params.url=txtLinkString.getValue();
						params.className=txtDriveName.getValue();
						params.sortno=sortnoCell.getValue();
						var myAjax = new Ajax.Request(link3, {
								method : 'post',
								waitMsg : '数据保存中，请稍候...',
								parameters : params,
								asynchronous : true,// 同步调用
									onComplete : function(req) {
										var responseText = req.responseText;
										var responseObj = responseText.evalJSON();
										if (responseObj.success == true) {
											alert("保存成功");
											window.returnValue = "true";
											window.close();
										}
								},
								onFailure : function(e) {
									alert('数据库连接失败');
								}
							});
					}
				}, {
					text : '取消',
					handler : function() {
						window.close();
					}
				}]
	})

	this.loadData = function() {
		if(dbsId!=""){
			Ext.Ajax.request( {
				url : link9 + "?id=" + dbsId,
				method : 'POST',
				waitMsg : '等待加载数据',
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					var data = obj.data[0];
					txtDbsName.setValue(data.name);
					combLinkTypeName.setValue(data.dbType);
					txtDriveName.setValue(data.className);
					txtAddress.setValue(data.server);
					txtDbName.setValue(data.dbName);
					txtPort.setValue(data.port);
					txtUserName.setValue(data.userId);
					txtPassword.setValue(data.userPwd);
					txtLinkString.setValue(data.url);
					sortnoCell.setValue(data.sortno);
					txtDbsName.setDisabled(true);
				}
			});
		}
	}

	AppSystemConnForm.superclass.constructor.call(this, {
		width : '100%',
		height : 415,
		modal : true,
		layout : 'column',
		plain : true,
		draggable : false,
		resizable : false,
		border : false,
		items : [{
			border : false,
			columnWidth : 1,
			layout : 'form',
			items : myForm
		}]
	})
}

Ext.extend(AppSystemConnForm, Ext.Panel, {});