


var GroupEditFormWindow=function(groupId){
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var formWindow=this;
	
	/*
	 * ************************
	 * 控件定义
	 * ************************
	 */
	//组织名
	var txtGroupName = new Ext.form.TextField({id:'txtGroupName',fieldLabel:'组织名称',maxLength:30,minLength:1,maxLengthText:'最长30位',name:'groupName',allowBlank:false,anchor:'90%'})
//	var txtGroupName={xtype:'textfield',id:'txtGroupName',fieldLabel:'组织名称',maxLength:30,minLength:1,maxLengthText:'最长30位',name:'groupName',allowBlank:false,anchor:'90%'};
	var infoGroupName=new Ext.app.LabelPanel('组织名称，1-30个有效字符');

	
	//组织描述
	var txtRemark={xtype:'textfield',fieldLabel: '组织描述',name: 'remark',anchor:'90%',maxLength:300}
	var infoRemark=new Ext.app.LabelPanel('组织描述，最多200字');
	        
	//管理员列表
	var groupAdminGrid=new GroupAdminGrid(groupId);

	var url1 = link45 + "?groupId=" + groupId;
	var groupSn = new Ext.app.UniqueTextField({
		id : 'groupSn',
		name : 'groupSn',
		fieldLabel : '标识',
		regex : /^[a-zA-Z0-9_]+$/,
		allowBlank : true,
		anchor : '90%',
		notUniqueText : '该标识已经被使用过，请使用其他名称',
		url : url1,
		maxLength : 50,
		minLength : 1,
		minLengthText : '最少需要1位字母数字组合',
		maxLengthText : '最长50位字母数字组合'
	})
	var infoGroupSn = new Ext.app.LabelPanel('组织内码');
	
	
	//用户表单定义
	var simpleForm = new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'groupName',mapping:'groupName'},{name:'remark',mapping:'remark'},{name:'groupSn',mapping:'groupSn'}]),
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
	        	items:[txtGroupName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoGroupName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[groupSn]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoGroupSn]
	        },{
	        	//第四行布局
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtRemark]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[infoRemark]
	        }
	        ,{
	        	//第五行布局
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
		        items:[{//标签面板
		            xtype:'tabpanel',
		            plain:true,
		            activeTab: 0,
		            height:200,
		            autoWidth:true,
		            defaults:{bodyStyle:'padding:2px'},
		            items:[{
		                title:'管理员列表',
		                layout:'form',
		                defaultType: 'textfield',
		                items: groupAdminGrid
		            }]
		        }]
	        }]
		},{//用户其他属性
			xtype:'hidden',
			name:'id',
			value:groupId
		}	],
	
	buttons: [{
			text:'保存',
			handler:function(){
				
				if(!simpleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				simpleForm.form.doAction('submit',{
            		url:link7,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params:'',
            		success:function(form,ajax){
            			var responseText=ajax.response.responseText;
            			var responseObject=Ext.util.JSON.decode(responseText);
            			if(responseObject.success){
            				alert("保存成功");
            				formWindow.close();
            				var node = groupTree.getSelectionModel().getSelectedNode();
            				node.setText(txtGroupName.getValue());
            				gridPanel.getStore().reload();
//            				//重新加载
//            				groupTree.getRootNode().reload(function(){
//            					
//            				})
            			}
            			else{
            				alert("保存失败");
            				simpleForm.buttons[0].enable();
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
            text: '重置',
            handler:function(){simpleForm.form.reset();}
        },{
            text: '取消',
            handler:function(){formWindow.close();}
        }]
	
	});
	this.loadData=function(){
		simpleForm.load({url: link6+"?id="+groupId,method:'GET', waitMsg:'等待加载数据' }); 
	}
	/*
	var simpleData = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url: link6+"?groupId="+groupId}), 
        reader: new Ext.data.JsonReader({ 
				root:'data' 
				},['username']), 
		remoteSort: false 
    });
    simpleData.load(); 
    */
	GroupEditFormWindow.superclass.constructor.call(this,{
        title: '组织机构编辑',
        width: 580,
        height:430,
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
	        	items:[new Ext.app.TitlePanel({caption:'组织信息编辑',border:false})]
	        },{
	        	border:false,
	        	columnWidth:1,
	        	layout:'form',
	        	items:simpleForm
	        }]
    });
}

Ext.extend(GroupEditFormWindow, Ext.Window, {
	
});








