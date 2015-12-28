
var EditTableWindow=function(tableId){
		
	var formWindow=this;
	this.oTreeNode=leftTree.getSelectionModel().getSelectedNode();
	
	//用户名
	var txtTableCode={xtype:'textfield',id:'tableCode',fieldLabel:'表名',maxLength:30,minLength:2,name:'tableCode',allowBlank:false,anchor:'90%',vtype:'alphanum'};
	var infoTableCode=new Ext.app.LabelPanel('表名，2-30位字母及数字');
	
	//姓名
	var txtTableName={xtype:'textfield',id:'tableName',fieldLabel:'中文名称',name:'tableName',maxLength:30,minLength:2,inputType:'textfield',allowBlank:false,anchor:'90%'};
	var infoTableName=new Ext.app.LabelPanel('中文名称，2-30汉字或有效字符');
	
	//分类
	var comboCatalog = new Ext.form.ComboBox({
		id: 'catalogId',
		valueField: "id",
		displayField: "tableName",
		mode: 'local',
		triggerAction: 'all',
		emptyText: '请选择分类',
		fieldLabel: '表分类',
		width: 125,
		forceSelection: true,
		editable: false,
		allowBlank: false,
		store: new Ext.data.JsonStore({
			url: link23,
			autoLoad: true,
			fields: ["id","tableName"]
		})
	});
	var infoCatalog = new Ext.app.LabelPanel('请选表分类');
	
	/**
    * 修改,更新
    */
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
		region:'center',
	    labelAlign: 'left',
	    buttonAlign:'right',
		style:'margin:1px',
	    bodyStyle:'padding:0px',
	    waitMsgTarget: true,
	    width: '99.70%',
	    frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
		items: [{
			layout:'column',
	        border:false,
	        labelSeparator:'：',
	        defaults:{
	        	blankText:'必填字段'
	        },
	        items:[
	        	{
	        	//第一行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtTableCode]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoTableCode]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtTableName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoTableName]
	        },{
	        	//第三行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[comboCatalog]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoCatalog]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'userConfig',
			value:''
		}],
	
	buttons: [{
			text:'保存',
			handler:function(){
				
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				simpleForm.form.doAction('submit',{
            		url:link4,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params: {id:tableId?tableId:"", tableCode:$("tableCode").value, tableName:$("tableName").value, catalogId:comboCatalog.getValue()},
            		success:function(){
            			if(window.confirm('保存成功')){
            				formWindow.close();
            				if(formWindow.oTreeNode && formWindow.oTreeNode.attributes.type=='table'){
            					formWindow.oTreeNode.parentNode.reload();
            				}else{
            					leftTree.getRootNode().reload();
            				}
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
	
	
	EditTableWindow.superclass.constructor.call(this,{
        title: '添加表',
        width: 500,
        height:200,
        x:200,
        y:50,
        modal:true,
        plain:true,
        draggable :true,
        resizable :false,
        region:'center',
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
	        	items:[new Ext.app.TitlePanel({caption:'添加表信息',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
	 })
	 
	 if(tableId != null){
		 Ext.Ajax.request({
		 	url: link6,
		 	method: 'post',
		 	params: {id:tableId},
		 	success: function(ajax){
		 		eval("responseObj="+ajax.responseText);
		 		if(responseObj.success == true){
		 			$("tableCode").value = responseObj.tableCode;
		 			$("tableName").value = responseObj.tableName;
		 			comboCatalog.setValue(responseObj.catalogId);
		 		}
		 	},
		 	failure: function(){
		 		alert('服务器忙,请稍后再试...');
		 	}
		 });
	 }
	 
};
	   
Ext.extend(EditTableWindow, Ext.Window, {

});