



var DictEditForm=function(catalogId,dictId){

	Ext.form.Field.prototype.msgTarget = 'side';
	
	var formWindow=this;
	
	this.dictId = dictId
	
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	
	//键
	var txtKey={xtype:'textfield',id:'txtKey',fieldLabel:'键',maxLength:32,minLength:1,minLengthText:'最少输入1位字母数字组合',maxLengthText:'最长32位字母数字组合',name:'key',allowBlank:false,anchor:'90%',
						listeners:{blur:function(txtField){						
						}}};
	var infoKey=new Ext.app.LabelPanel('键名称，1-32位字母及数字');
	
	this.firstInput=txtKey;
	
	//值
	var txtValue={xtype:'textfield',fieldLabel:'值',name:'value',maxLength:200,minLength:1,maxLengthText:'最多只能输入200个有效字符',inputType:'textfield',allowBlank:true,anchor:'90%'};
	var infoValuek=new Ext.app.LabelPanel('请输入键对应的值');

	//描述
	var txtRemark={xtype:'textfield',fieldLabel:'描述',name:'remark',maxLength:200,minLength:0,maxLengthText:'最多只能输入200个字符',inputType:'textfield',allowBlank:true,anchor:'90%'};
	var infoRemark=new Ext.app.LabelPanel('字典描述信息，200个以内汉字或有效字符');
	
	//排序号 
	var txtSortno= new Ext.form.NumberField({
		id: 'sortNo',
		fieldLabel: '排序号',
		value: 0,
		minValue: 0,
		width: 150,
		blankText: '请输入排序号',
		allowBlank: false
	});
	var infoSortno=new Ext.app.LabelPanel('排序号,数字类型');
	
	//字典表单定义
	var simpleForm = new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
				success:'success',
				root:'data'
			},[{name:'key',mapping:'key'},{name:'value',mapping:'value'},{name:'remark',mapping:'remark'},{name:'sortNo',mapping:'sortNo'}]),
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
	        	columnWidth:.7,
	        	layout:'form',
	        	border:false,
	        	items:[txtKey]
	        },{
	        	columnWidth:.3,
	        	layout:'form',
	        	border:false,
	        	items:[infoKey]
	        },{
	        	//第二行布局
	        	columnWidth:.7,
	        	layout:'form',
	        	border:false,
	        	items:[txtValue]
	        },{
	        	columnWidth:.3,
	        	layout:'form',
	        	border:false,
	        	items:[infoValuek]
	        },{
	        	//第三行布局
	        	columnWidth:.7,
	        	layout:'form',
	        	border:false,
	        	items:[txtRemark]
	        },{
	        	columnWidth:.3,
	        	layout:'form',
	        	border:false,
	        	items:[infoRemark]
	        },{
	        	//第四行布局
	        	columnWidth:.7,
	        	layout:'form',
	        	border:false,
	        	items:[txtSortno]
	        },{
	        	columnWidth:.3,
	        	layout:'form',
	        	border:false,
	        	items:[infoSortno]
	        }],
	buttons: [{
			text:'保存',
			handler:function(){
				tmpObj=this;
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				simpleForm.form.doAction('submit',{
            		url:link8,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params:{catalogId:catalogId,id:formWindow.dictId},
            		success:function(){
            			if(window.confirm('保存成功,还需要新增吗？')){
            				formWindow.dictId = ''
            				simpleForm.form.reset();
            				tmpObj.enable();
            				gridPanel.getStore().reload();
            			}else{
            				formWindow.close();
            				gridPanel.getStore().reload();
            			}
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			tmpObj.enable();
            		}
            	});
            	this.disable();
			}				
			},{
			text:'保存&关闭',
			handler:function(){
				tmpObj=this;
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				simpleForm.form.doAction('submit',{
            		url:link8,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params:{catalogId:catalogId,id:dictId},
            		success:function(){
            			alert('保存成功');
            			formWindow.close();
            			gridPanel.getStore().reload();
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
		simpleForm.load({url: link9+"?id="+dictId,method:'GET', waitMsg:'等待加载数据'}); 
	}

	DictEditForm.superclass.constructor.call(this,{
        title: '字典编辑',
        width: 580,
        height:280,
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
	        	items:[new Ext.app.TitlePanel({caption:'字典编辑',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(DictEditForm, Ext.Window, {

});








