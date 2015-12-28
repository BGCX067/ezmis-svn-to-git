/**
 * 编辑字段FormWindow
 * @tableId 表ID
 */
var GCColumnWindow=function(tableId){
	//已存在的字段名
	var exitsCo;
	
	var formWindow=this;
	
	//字段生成起始数
	var txtGCStart=new Ext.form.NumberField({id:'gcstart',minValue:1,maxValue:1000,fieldLabel:'起始数',name:'gcstart',anchor:'90%',allowBlank:false});
	
	//字段生成结束数
	var txtGCEnd=new Ext.form.NumberField({id:'gcend',minValue:2,maxValue:1001,fieldLabel:'结束数',name:'gcend',anchor:'90%',allowBlank:false});
	
	//列名
	var txtColumnCode={xtype:'textfield',id:'columncode',fieldLabel:'字段名称',vtype:'alphanum',maxLength:30,minLength:1,name:'columncode',allowBlank:false,anchor:'90%'};
	var infoColumnCode=new Ext.app.LabelPanel('字段名，2-30位字母及数字');
	
	//中文名称
	var txtColumnName={xtype:'textfield',id:'columnname',fieldLabel:'中文名称',name:'columnname',maxLength:30,minLength:1,inputType:'textfield',allowBlank:false,anchor:'90%'};
	var infoColumnName=new Ext.app.LabelPanel('中文名称，2-30汉字或有效字符');
	
	//字段类型
	var combColumnField={xtype:'combo',valueField :"retrunValue",displayField: "retrunValue",mode: 'local',
		store: new Ext.data.SimpleStore({fields: ["retrunValue"],data: [['VARCHAR2'],['NUMBER'],['LONG'],['CHAR'],['CLOB'],['BLOB'],['DATE'],['TIMESTAMP'],['RAW'],['LONG RAW']]}),
		triggerAction: 'all',blankText:'请选择数据类型',emptyText:'请选择数据类型',hiddenName:'columntype',forceSelection:'true',
		editable: false,allowBlank:false,id:'hiddenColumnType',fieldLabel: '字段类型',name: 'hiddenColumnType',width:'20px',
		listeners:{beforeselect:function(combobox,record){
			if(record.data.retrunValue == 'DATE' || record.data.retrunValue == 'TIMESTAMP'){
				txtColumnLength.setValue('8');
			}else{
				txtColumnLength.setValue('');
			}
		}}
	}
	var infoColumnField=new Ext.app.LabelPanel('字段类型');
	
	//字段长度
	var txtColumnLength=new Ext.form.NumberField({id:'columnlength',maxValue:4000,fieldLabel:'字段长度',name:'columnlength',anchor:'90%',allowBlank:false});
	var infoColumnLength=new Ext.app.LabelPanel('字段长度，<4000数字');
	
	//小数位数
	var txtColumnPrec=new Ext.form.NumberField({id:'columnprec',maxValue:10,fieldLabel:'小数位数',name:'columnprec',anchor:'90%'});
	var infoColumnPrec=new Ext.app.LabelPanel('小数位数，2-10位数字');
	
	//默认值
	var txtColumnDefault={xtype:'textfield',id:'defaultvalue',fieldLabel:'默认值',name:'defaultvalue',anchor:'90%'};
	var infoColumnDefault=new Ext.app.LabelPanel('字段默认情况下的值，选填');
	
	//是否允许为空
	var chkColumnIsNULL={xtype:'checkbox',fieldLabel: '允许为空',name: 'allownull',checked: true};
	var infoColumnIsNULL=new Ext.app.LabelPanel('是否允许字段为空,默认允许');
	
	//备注
	var txtColumnComm={xtype:'textarea',id:'comm',fieldLabel:'备注',name:'comm',maxLength:1000,anchor:'90%'};
	var infoColumnComm=new Ext.app.LabelPanel('备注信息，1000字以下');
	
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
	  reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'columncode',mapping:'columncode'},{name:'columnname',mapping:'columnname'}
		,{name:'columntype',mapping:'columntype'},{name:'columnlength',mapping:'columnlength'}
		,{name:'columnprec',mapping:'columnprec'},{name:'defaultvalue',mapping:'defaultvalue'}
		,{name:'allownull',mapping:'allownull'},{name:'pk',mapping:'pk'}
		,{name:'columnorder',mapping:'columnorder'},{name:'comm',mapping:'comm'}]),
	    labelAlign: 'left',
	    buttonAlign:'right',
		style:'margin:1px',
	    bodyStyle:'padding:0px',
	    waitMsgTarget: true,
	    width: '99.90%',
	    frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
		items: [{
			layout:'column',
	        border:false,
	        labelSeparator:'：',
	        defaults:{
	        	blankText:'必填字段'
	        },
	        items:[{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtGCStart]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtGCEnd]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnCode]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnCode]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[combColumnField]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnField]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnLength]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnLength]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnPrec]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnPrec]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnDefault]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnDefault]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[chkColumnIsNULL]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnIsNULL]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnComm]
	        }]
		}],
	
	buttons: [{
			text:'保存',
			handler:function(){
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				if($("gcend").value - $("gcstart").value < 1){
					alert("结束数必须大于起始数");
					$("gcend").value = "";
					$("gcend").focus();
					return;
				}
				
				/** 判断是否存在重复字段 */
				Ext.Ajax.request({
					url: contextPath+"/jteap/form/dbdef/web/DefColumnInfoAction!findExistGCColumn.do",
					params: {columncode:$("columncode").value,gcstart:$("gcstart").value, gcend:$("gcend").value},
					method: "post",
					success: function(ajax){
						eval("responsObj="+ajax.responseText);
						if(responsObj.success == true && responsObj.exitsCodes != null){
							alert("字段名: "+responsObj.exitsCodes+" 已存在,保存将忽略这些字段");
							exitsCo = responsObj.exitsCodes;
						}
						/** 保存 */
						simpleForm.form.doAction('submit',{
			        		url: contextPath+"/jteap/form/dbdef/DefColumnInfoAction!saveGCColumn.do",
			        		method: 'post',
			        		waitMsg:'保存数据中,请稍候...',
			        		params:{tableid:tableId, exitsCodes:responsObj.exitsCodes},
			        		success:function(form, action){
			        			formWindow.close();
			        		},
			        		failure:function(){
			        			alert('服务器忙，请稍候操作...');
			        		}
			        	});
					},
					failure: function(){
						alert("服务器忙,请稍后操作...");
					}
				});
				
	        	
				this.disable();
			}
		},{
            text: '取消',
            handler:function(){formWindow.close();}
        }]
	
	});
	
	GCColumnWindow.superclass.constructor.call(this,{
        title: '编辑字段',
        width: 580,
        height:450,
        x:200,
        y:50,
        modal:true,
        plain:true,
        draggable :true,
        resizable :false,
        buttonAlign:'center',
        layout:'column',
        items: [{
	        	//第一行布局
	        	layout:'form',
	        	columnWidth:.9999,
	        	height:30,
	        	width:200,
	        	border:true,
	        	frame:false,
	        	items:[new Ext.app.TitlePanel({caption:'编辑字段信息',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
	 })
};
	   
Ext.extend(GCColumnWindow, Ext.Window, {
	
});