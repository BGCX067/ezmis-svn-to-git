
var RoleEditFormWindow=function(roleId){
	var formWindow=this;
	Ext.form.Field.prototype.msgTarget='side';
	//当前角色所拥有的资源
	var resAssignTree=new ResourceColumnTree(roleId);
	
	var txtRoleName={xtype:'textfield',id:'txtRoleName',fieldLabel:'角色名称',maxLength:30,minLength:1,maxLengthText:'最长30位',name:'rolename',allowBlank:false,anchor:'90%'};
	var infoRoleName=new Ext.app.LabelPanel('角色名称，1-30个有效字符');
	
	var dsc={xtype:'textfield',fieldLabel: '排序',name: 'sortNo',anchor:'90%',maxLength:30};
	var infodsc=new Ext.app.LabelPanel('用于角色排序');
	
	var extend={xtype:'checkbox',fieldLabel: '可继承',name: 'inheritable',checked: false};
	var infoextend=new Ext.app.LabelPanel('该角色是否可从父角色继承资源');
	
    var roleRemark={xtype:'textfield',fieldLabel: '备注',name: 'remark',maxLength:200,anchor:'90%'}
	var inforoleRemark=new Ext.app.LabelPanel('角色角色等');
	
	var url = link33 + "?roleId=" + roleId;
	var roleSn = new Ext.app.UniqueTextField({
		id : 'roleSn',
		name : 'roleSn',
		fieldLabel : '标识',
		regex : /^[a-zA-Z0-9_]+$/,
		allowBlank : true,
		anchor : '90%',
		notUniqueText : '该标识已经被使用过，请使用其他名称',
		url : url,
		maxLength : 50,
		minLength : 1,
		minLengthText : '最少需要1位字母数字组合',
		maxLengthText : '最长50位字母数字组合',
		listeners : {
			blur : function() {
				
			}
		}
	})
	var infoRoleSn = new Ext.app.LabelPanel('角色内码');
	
	/**
	 * 编辑面板
	 */
	 
	var roleForm=new Ext.FormPanel({
		reader:new Ext.data.JsonReader({
			success:'success',
			root:'data'
		},[{name:'rolename',mapping:'rolename'},{name:'remark',mapping:'remark'},{name:'sortNo',mapping:'sortNo'},{name:'inheritable',mapping:'inheritable'},{name:'roleSn',mapping:'roleSn'}]),
		labelAlign:'left',
		buttonAlign:'right',
		bodyStyle:'padding:5px',
		waitMsgTarget:true,
		width:550,
		frame:true,
		labelWidth:80,
		items:[{
	 	   id:'formx',
		   layout:'column',
		   items:[{
		   columnWidth:.5,
		   layout:'form',
		   border:false,
		   items:[txtRoleName]
		   },{
			columnWidth:.5,
			border:false,
			items:[infoRoleName]
		    },{
		   columnWidth:.5,
		   layout:'form',
		   border:false,
		   items:[roleSn]
		   },{
			columnWidth:.5,
			border:false,
			items:[infoRoleSn]
		    },{
			columnWidth:.5,
			layout:'form',
			border:false,
			items:[roleRemark]
		    },{
			columnWidth:.5,
			layout:'form',
			border:false,
			items:[inforoleRemark]
		    },{
			columnWidth:.5,
			layout:'form',
			border:false,
			items:[extend]
		    },{
			columnWidth:.5,
			border:false,
			items:[infoextend]
		    },{
	        	//第五行布局
	        	columnWidth:1,
	        	border:false,
		        items:[{
		            xtype:'fieldset',
		            title: '资源',
		            autoHeight:true,
		            defaults: {width: 210},
		            defaultType: 'textfield',
		            items :[resAssignTree]
		        }]
	        }]
		}],
		buttons: [{
			text:'保存',
			handler:function(){
				//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
				if(!roleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				roleForm.form.doAction('submit',{
            		url:link7,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params:{id:roleId},	
            		success:function(form,ajax){
            			var responseText=ajax.response.responseText;
            			var responseObject=Ext.util.JSON.decode(responseText);
            			if(responseObject.success){
            				alert("保存成功");
            				formWindow.close();
            				// 树重新加载
            			    roleTree.getRootNode().reload(function(){
            			  	})
            			}
            			else{
            				alert("保存失败");
            				simpleForm.buttons[0].enable();
            			}
            			
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			roleForm.buttons[0].enable();
            		}
            	});
            	this.disable();
			}
		}
/**		,{
			text:'保存&关闭',
			handler:function(){
				//提交表单的时候将属性列表中的数据转换成JSON字符串传入隐藏域中
			
				if(!roleForm.form.isValid()){
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}
				roleForm.form.doAction('submit',{
            		url:link7,
            		method:'post',
            		waitMsg:'保存数据中，请稍候...',
            		params:{id:roleId},	
            		success:function(form,ajax){
            			var responseText=ajax.response.responseText;
            			var responseObject=Ext.util.JSON.decode(responseText);
            			if(responseObject.success){
            				alert("保存成功");
            				formWindow.close();
            				// 树重新加载
            			    roleTree.getRootNode().reload(function(){
            			  	})
            			}
            			else{
            				alert("保存失败");
            				simpleForm.buttons[0].enable();
            			}
            			
            		},
            		failure:function(){
            			alert('服务器忙，请稍候操作...');
            			roleForm.buttons[0].enable();
            		}
            	});
            	this.disable();
				//alert('保存成功');
				//formWindow.close();
			}
		}**/
		,{
            text: '重置',
            handler:function(){roleForm.form.reset();}
        },{
            text: '取消',
            handler:function(){formWindow.close();}
        }]
	
	
	});
	this.loadData=function(){
		roleForm.load({url: link6+"?id="+roleId,method:'GET', waitMsg:'等待加载数据' }); 
	}
	/**
	 * 弹出窗口
	 */
	RoleEditFormWindow.superclass.constructor.call(this,{
		title:'角色分配编辑',
		width:580,
		height:500,
		modal:true,
		layout:'column',
		plain:true,
		draggable:false,
		resizable:false,
		bodyStyle:'padding:5px',
		buttonAlign:'center',
		items:[{
			columnWidth:1,
			height:40,
			width:600,
			border:false,
			frame:true,
			items:[new Ext.app.TitlePanel({caption:'角色信息编辑',border:false})]
		},{
		  border:false,
		  columnWidth:1,
		  items:[roleForm]
	   	}]
	});

}

Ext.extend(RoleEditFormWindow,Ext.Window,{
});