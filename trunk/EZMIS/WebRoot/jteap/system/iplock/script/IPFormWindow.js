// IP Form
var IPFormWindow = function(id) {
	if(typeof id != 'undefined'){
		this.IpLockId = id;
	}else{
		this.IpLockId= "";
	}
	
	var formWindow = this;

	// 备注
	var txtComm = new Ext.form.TextField( {
		id : 'comm',
		fieldLabel : '备注',
		name : 'comm',
		width:165,
		allowBlank : true
	})
	
	//数据类型
	var comboRule=new Ext.form.ComboBox({triggerAction: 'all',editable :false,hiddenName:'rule',selectOnFocus :false,forceSelection :true,maxLength : 50,mode:'local',allowBlank :false,fieldLabel : "数据类型",id : "comboRule",displayField:'name',valueField:'value',width:165,
		store:new Ext.data.SimpleStore({fields:['name','value'],data:[['允许','1'],['阻止','0']]}),
		value:'0'
	});

	var ipForm = new Ext.FormPanel( {
		labelAlign : 'left',
		buttonAlign : 'right',
		autoWidth : true,
		autoHeight : true,
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 70, // 标签宽度
		items : [{
			layout : 'column',
			border : false,
			labelSeparator : '：',
			defaults : {
				style : 'padding-left:15px'
			},
			items : [{
				// 第一行布局
					columnWidth : 1,
					layout : 'form',
					border : false,
					items : [{
							xtype: 'uxipfield',
							fieldLabel: '开始IP',
							id: 'startIp',
							name:'startIp'
						},{
							xtype: 'uxipfield',
							fieldLabel: '结束IP',
							id: 'endIp',
							name:'endIp'
						},comboRule,txtComm]
				}]
		}],
		buttons : [ {
			text : '保存',
			handler : function() {
				if(!ipForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确,可能是IP地址无效');
					return;
				}
				ipForm.buttons[0].disable();
				ipForm.form.doAction('submit',{
            		url:link2+"?id="+formWindow.IpLockId,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		success:function(form,ajax){
            			alert('保存成功');
            			formWindow.close();
            			rightGrid.store.reload();
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			ipForm.buttons[0].enable();
            			
            		}
            	});
			}
		},{
			text : '取消',
			handler : function() {
				formWindow.close();
			}
		}]
	})
	this.showData = function(ipLock){
		var txtComm=this.findById("comm");
		txtComm.setValue(ipLock.comm);
		
		var txtStartIp=this.findById("startIp");
		txtStartIp.setValue(ipLock.startIp);
		
		var txtEndIp=this.findById("endIp");
		txtEndIp.setValue(ipLock.endIp);
		
		var comboRule = this.findById("comboRule")
		comboRule.setValue(ipLock.rule);
	}
	
	
	// 修改时读取记录
	this.loadData = function() {
		var formWindow = this;
		
		Ext.Ajax.request({
			url: link4+"?id="+formWindow.IpLockId,
			success: function(ajax,options){
				var responseText=ajax.responseText;	
				var responseObject=Ext.util.JSON.decode(responseText);
				var ipLock = responseObject.data[0];
				formWindow.showData(ipLock);
			}
		});
	}

	IPFormWindow.superclass.constructor.call(this, {
		title : 'IP编辑',
		width : 400,
		height : 250,
		modal : true,
		layout : 'column',
		plain : true,
		draggable : true,
		resizable : false,
		bodyStyle : 'padding:3px;',
		items : [ {
			// 第一行布局
				columnWidth : 1,
				layout : 'form',
				height : 40,
				width : 600,
				border : false,
				frame : true,
				items : [new Ext.app.TitlePanel( {
					caption : 'IP编辑',
					border : false
				})]
			}, {
				border : false,
				columnWidth : 1,
				layout : 'form',
				items : [ipForm]
			}]
	});

}

Ext.extend(IPFormWindow, Ext.Window, {});