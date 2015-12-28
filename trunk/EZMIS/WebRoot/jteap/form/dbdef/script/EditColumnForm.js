
/**
 * 编辑字段FormWindow
 */
var EditColumnWindow=function(columnId){
		
	var formWindow=this;
	
	//取得表编号
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	
	//列名
	var txtColumnCode={
		xtype:'textfield',
		id:'columncode',
		fieldLabel:'字段名称',
		maxLength:30,
		minLength:2,
		name:'columncode',
		allowBlank:false,
		anchor:'90%',
		vtype:'alphanum',
		listeners : {
			blur: function(){
				if(columnId == null){
					/** 新增时,判断字段是否已存在 */
					if($("columncode").value != ""){
						Ext.Ajax.request({
							url: contextPath+"/jteap/form/dbdef/web/DefColumnInfoAction!isExistColumnAction.do",
							params: {tableid:oNode.id, columnCode:$("columncode").value},
							method: "post",
							success: function(ajax){
								eval("responsObj="+ajax.responseText);
								if(responsObj.success == true && responsObj.exist == true){
									alert("该字段名已存在");
									$("columncode").value = "";
									$("columncode").focus();
								}
							},
							failure: function(){
								alert("服务器忙,请稍后操作...");
							}
						});
					}
				}
			}
		}
	};
	var infoColumnCode=new Ext.app.LabelPanel('表名，2-30位字母及数字');
	
	//中文名称
	var txtColumnName={xtype:'textfield',id:'columnname',fieldLabel:'中文名称',name:'columnname',maxLength:30,minLength:2,inputType:'textfield',allowBlank:false,anchor:'90%'};
	var infoColumnName=new Ext.app.LabelPanel('中文名称，2-30汉字或有效字符');
	
	//字段类型
	var combColumnField={xtype:'combo',valueField :"retrunValue",displayField: "retrunValue",mode: 'local',
		store: new Ext.data.SimpleStore({fields: ["retrunValue"],data: [['VARCHAR2'],['NUMBER'],['LONG'],['CHAR'],['CLOB'],['BLOB'],['DATE'],['TIMESTAMP'],['RAW'],['LONG RAW']]}),
		triggerAction: 'all',blankText:'请选择数据类型',emptyText:'请选择数据类型',hiddenName:'columntype',forceSelection:'true',
		editable: false,allowBlank:false,id:'hiddenColumnType',fieldLabel: '字段类型',name: 'hiddenColumnType',width:'20px',
		listeners:{beforeselect:function(combobox,record){
			if(record.data.retrunValue == 'DATE' || record.data.retrunValue == 'TIMESTAMP'){
				txtColumnLength.setValue('10');
				var fm = (record.data.retrunValue == 'DATE'?"Y-m-d":"Y-m-d H:i:s");
				txtFormat.setValue(fm);
			}else{
				txtColumnLength.setValue('');
				txtFormat.setValue('');
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
	var chkColumnIsNULL=new Ext.form.Checkbox({fieldLabel:'非空字段',name:'allownull'});
	var infoColumnIsNULL=new Ext.app.LabelPanel('是否非空字段');
	
	//是否主键
	var combColumnPK={xtype:'checkbox',fieldLabel: '是否主键',name: 'pk',checked: false};
	var infoColumnPK=new Ext.app.LabelPanel('是否表主键，默认不是');
	
	//排序号
//	var txtColumnOrder={xtype:'textfield',id:'columnorder',fieldLabel:'排序号',name:'columnorder',anchor:'90%',value:0};
//	var infoColumnOrder=new Ext.app.LabelPanel('用于字段排序');
	
	//备注
	var txtColumnComm={xtype:'textarea',id:'comm',fieldLabel:'备注',name:'comm',maxLength:1000,anchor:'90%'};
	var infoColumnComm=new Ext.app.LabelPanel('备注信息，1000字以下');
	
	var hidColumnCode = {xtype:'hidden',id:'beforcolumncode',name:'beforcolumncode'};
	
	
	
	//字段长度
	var txtFormat=new Ext.form.TextField({id:'format',maxValue:50,fieldLabel:'显示格式',name:'format',anchor:'90%',allowBlank:true});
	var infoFormat=new Ext.app.LabelPanel('显示格式，主要用于日期类型的字段');
	
	
	/**
    * 修改,更新
    */
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
	  reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'columncode',mapping:'columncode'},{name:'format',mapping:'format'},{name:'columnname',mapping:'columnname'}
		,{name:'beforcolumncode',mapping:'columncode'}
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
	        	//第一行布局
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
	        	//第二行布局
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
	        	//第三行布局
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
	        	//第四行布局
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
	        	//第五行布局
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
	        	//第六行布局
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
	        	//第六行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtFormat]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoFormat]
	        },{
	        	//第七行布局
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
	        	//第八行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[combColumnPK]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoColumnPK]
	        },{
	        	//第九行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtColumnComm,hidColumnCode]
	        }]
		}],
	
	buttons: [{
			text:'保存',
			handler:function(){
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				
				simpleForm.form.doAction('submit',{
            		url:link8+"?id="+(columnId?columnId:""),
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params:{"tableid":oNode.id},
            		success:function(ajax){
						if(window.confirm('保存成功')){
							formWindow.close();
            				columnGrid.getStore().reload();
            			}
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			simpleForm.buttons[0].enable();
            		}
            	});
            	this.disable();
			}
		},{
            text: '取消',
            handler:function(){formWindow.close();}
        }]
	});
	
	
	EditColumnWindow.superclass.constructor.call(this,{
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
	
	 
	 this.loadData=function(){
		simpleForm.load({url: link7+"?id="+columnId,method:'GET', waitMsg:'等待加载数据'
		}); 
	}
	
};
	   
Ext.extend(EditColumnWindow, Ext.Window, {
	
});
