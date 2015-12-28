var CatalogEditForm=function(id,parentId){
	var formWindow=this;
	var isNew = ((id!=null && id !='')?false:true);

	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	
	//类型名称
	var txtCatalogName={xtype:'textfield',id:'txtCatalogName',fieldLabel:'类型名称',maxLength:30,minLength:1,minLengthText:'最少输入1位字母数字组合',maxLengthText:'最长30位字母数字组合',name:'catalogName',allowBlank:false,anchor:'90%'};
	var infoCatalogName=new Ext.app.LabelPanel('数据字典类型名称，1-30位字母及数字');
	
	//简称
	var txtUniqueName={xtype:'uniquetextfield',readOnly:!isNew,notUniqueText:'该简称已经被使用过，请使用其他名称',url:link13,id:'txtUniqueName',fieldLabel:'分类简称',maxLength:30,minLength:1,minLengthText:'最少需要1位字母数字组合',maxLengthText:'最长30位字母数字组合',name:'uniqueName',allowBlank:false,anchor:'90%',
						listeners:{blur:function(txtField){
	}}};
	var infoUniqueName=new Ext.app.LabelPanel('数据字典类型简称，1-30位字母及数字，该简称具有唯一性');
	
	//描述
	var txtRemark={xtype:'textfield',fieldLabel:'描述',name:'remark',maxLength:200,minLength:0,maxLengthText:'最多只能输入200个字符',inputType:'textfield',allowBlank:true,anchor:'90%'};
	var infoRemark=new Ext.app.LabelPanel('字典类型描述信息，200个以内汉字或有效字符');
	
	//字典类型表单定义
	var simpleForm = new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
				success:'success',
				root:'data'
			},[{name:'catalogName',mapping:'catalogName'},{name:'uniqueName',mapping:'uniqueName'},{name:'remark',mapping:'remark'}]),
		labelAlign: 'left',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    waitMsgTarget: true,
	    width: 560,
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
	        	items:[txtCatalogName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoCatalogName]
	        },{
	        	//第一行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtUniqueName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoUniqueName]
	        },{
	        	//第二行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtRemark]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoRemark]
	        },{
	        	//字典类型编号，隐藏域
				xtype:'hidden',
				name:'id',
				value:id
			},{
	        	//字典类型编号，隐藏域
				xtype:'hidden',
				name:'parentId',
				value:parentId
			}],
		buttons: [{
				text:'保存',
				handler:function(){
					var tmpObj=this;
					if(!simpleForm.form.isValid()){
						alert('数据校验失败，请检查填写的数据格式是否正确');
						return;
					}
					simpleForm.form.doAction('submit',{
	            		url:link7,
	            		method:'post',
	            		waitMsg:'保存数据中，请稍候...',
	            		params:{},
	            		success:function(){
	            			alert('保存成功');
	        				tmpObj.enable();
	        				var oNode=dictCatalogTree.getSelectionModel().getSelectedNode();
	        				if(!isNew){
	        					oNode.setText(Ext.getCmp('txtCatalogName').getValue());
	        				}else{
	        					dictCatalogTree.getRootNode().reload();
	        				}
	        				simpleForm.form.reset();
	            			formWindow.close();
	            		},
	            		failure:function(){
	            			alert('服务器忙，请稍候操作...');
	            			tmpObj.enable();
	            		}
	            	});
	            	this.disable();
				}
			},{
	            text: '重置',
	            handler:function(){simpleForm.form.reset();}
	        },{
	            text: '取消',
	            handler:function(){formWindow.close();}
	        }]
		}]
	});
	/**
	 * 加载修改数据
	 */
	this.loadData=function(){
		simpleForm.load({url:link6+"?id="+id,method:'GET',waitMsg:'等待加载数据'}); 
	}
	
	CatalogEditForm.superclass.constructor.call(this,{
        title: '字典类型编辑',
        width: 580,
        height:250,
        modal:true,
        layout: 'column',
        plain:true,
        draggable :false,
        resizable :false,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [{
        	//第一行布局
        	columnWidth:1,
        	layout:'form',
        	height:40,
        	width:600,
        	border:false,
        	frame:true,
        	items:[new Ext.app.TitlePanel({caption:'字典类型编辑',border:false})]
        },{
        	border:false,
        	columnWidth:1,
        	layout:'form',
        	items:simpleForm
        }]
    });
}

Ext.extend(CatalogEditForm, Ext.Window, {
	
});








