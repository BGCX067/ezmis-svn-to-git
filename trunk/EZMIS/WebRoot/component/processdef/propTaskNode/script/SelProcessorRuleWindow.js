SelProcessorRuleWindow =  function() {
	
	var selProcessorRuleWindow = this ;
	
	//指定工号checkbox
	var chkIndiWorkNo = {xtype:'checkbox',boxLabel : '指定工号' , labelSeparator : '',height:22,listeners:{'check':function(checkbox, checked ){
		btnIndiWorkNo.setDisabled(!checked) ;
		jsonIndiWorkNo.setValue("") ;
		txtIndiWorkNo.setValue("");
	} }};
	
	//工号的TextField
	var txtIndiWorkNo = new Ext.form.TextField({xtype:'textfield',height:22,width : 150,readOnly :true ,name:'indiWorkNo' ,value : '' }) ;
	
	//工号的隐藏域,用于存储loginName
	var jsonIndiWorkNo = new Ext.form.Hidden(
		{
			name:'jsonIndiWorkNo' ,
			value : ''		
		}
	) ;
	
	//选择工号的按钮
	var btnIndiWorkNo = new Ext.Button({text:'选择',disabled : true,handler:function(){
		var url = link3 ;
		var returnValue = showModule(url,true,800,600) ;
		var arrays = Ext.decode(returnValue) ;
		if(arrays==null) {
			return ;
		}
       	//var ids = "" ;
       	var loginNames = "" ;
       	for(var i=0 ; i < arrays.length ; i++) {
       		//ids += arrays[i].id + "," ;
       		loginNames += arrays[i].loginName + "," ;
       	}
       	//ids = ids.substring(0,ids.length-1) ;
       	loginNames = loginNames.substring(0,loginNames.length - 1) ;
       	jsonIndiWorkNo.setValue(loginNames) ;
       	var name = "" ;
       	for(var i=0 ; i<arrays.length ; i++) {
       		name += arrays[i].name + "@" ;
       	}
       	if(name.length >1) {
       		name = name.substring(0,name.length-1) ;
       	}
       	txtIndiWorkNo.setValue(name) ;
	}}) ;
	
	//指定群组的checkbox
	var chkIndiGroup = {xtype:'checkbox',boxLabel : '指定群组' , labelSeparator : '',height:22,listeners:{'check':function(checkbox, checked ){
		btnIndiGroup.setDisabled(!checked) ;
		txtIndiGroup.setValue("") ;
		jsonIndiGroup.setValue("null") ;
	} }};
	
	//群组的TextField
	txtIndiGroup = new Ext.form.TextField({xtype:'textfield',height:22,width : 150,readOnly :true,value:''}) ;
	
	//群组的隐藏域,用于存储群组id
	jsonIndiGroup = new Ext.form.Hidden(
		{
			name:'jsonIndiGroup' ,
			value : 'null'		
		}
	) ;
	
	//群组树按钮
	var btnIndiGroup = new Ext.Button({text:'选择',disabled : true,handler:selProcessorRuleWindow.showCheckGroupTreeWindow}) ;
	
	//指定角色的checkbox
	var chkIndiRole = {xtype:'checkbox',boxLabel : '指定角色' , labelSeparator : '',height:22,listeners:{'check':function(checkbox, checked ){
		btnIndiRole.setDisabled(!checked) ;
		txtIndiRole.setValue("") ;
		jsonIndiRole.setValue("null") ;
	} }};
	
	//指定群组的TextField
	txtIndiRole = new Ext.form.TextField({xtype:'textfield',height:22,width : 150,readOnly :true , value : ''}) ;
	
	//角色的隐藏域,用于存储群组id
	jsonIndiRole = new Ext.form.Hidden(
		{
			name:'jsonIndiRole' ,
			value : 'null'		
		}
	) ;
	
	//角色树按钮
	var btnIndiRole = new Ext.Button({text:'选择',disabled : true,handler:selProcessorRuleWindow.showCheckRoleTreeWindow}) ;
	
	// 本部门的
	var chkSelfdept = new Ext.form.Checkbox({xtype:'checkbox',boxLabel : '本部门内过滤' , labelSeparator : '' ,inputValue : '1'})
	//本部门负责人的checkbox
	var chkDeptPrin = new Ext.form.Checkbox({xtype:'checkbox',boxLabel : '本部门负责人' , labelSeparator : '' ,inputValue : '1'});
	
	//流程创建人的checkbox
	var chkFlowCreator = new Ext.form.Checkbox({xtype:'checkbox',boxLabel : '流程创建人' , labelSeparator : '',inputValue : '1'});
	
	//上级部门负责人的checkbox
	var chkUpDeptPrin = new Ext.form.Checkbox({xtype:'checkbox',boxLabel : '上级部门负责人' , labelSeparator : '',inputValue : '1'});

	//上一环节处理人的checkbox
	var chkUpTaskProcessor = new Ext.form.Checkbox({xtype:'checkbox',boxLabel : '上一环节处理人' , labelSeparator : '',inputValue : '1'});
	
	// 人员规则
	var txtPerson = new Ext.form.TextArea({
		fieldLabel : '人员规则',
		anchor : '90%'
	})
	
	// 组织规则
	var txtGroup = new Ext.form.TextArea({
		fieldLabel : '组织规则',
		anchor : '90%'
	})
	
	// 角色规则
	var txtRole = new Ext.form.TextArea({
		fieldLabel : '角色规则',
		anchor : '90%'
	})

	//确定按钮
	var btnConfirm = {xtype:'button' , text:'确定' , handler : function()
		{
			//拼盘三个text的值
			/*
			var listValue = "+" + txtIndiWorkNo.getValue() + "+" + "+"+ txtIndiGroup.getValue() + "+" + "+"+ txtIndiRole.getValue()+"+"+ "+" + (chkDeptPrin.checked?"本部门负责人":"") + "+" + "+"+ (chkFlowCreator.checked?"流程创建人":"") + "+" + "+"+ (chkUpDeptPrin.checked?"上级部门负责人":"") + "+" + "+"+ (chkUpTaskProcessor.checked?"上一环节处理人":"") + "+" ;
			listValue = listValue.replace(/\+\+\+\+\+\+/g,"+") ;
			listValue = listValue.replace(/\+\+\+\+\+/g,"+") ;
			listValue = listValue.replace(/\+\+\+/g,"+") ;
			listValue = listValue.replace(/\+\+/g,"+") ;
			listValue = listValue.substring(1,listValue.length-1) ;
			*/
			var listValue = "@" + txtIndiWorkNo.getValue() + "@" + "@"+ txtIndiGroup.getValue() + "@" + "@"+ txtIndiRole.getValue()+"@"+ "@" + (chkDeptPrin.checked?"本部门负责人":"")+"@"+ "@" + (chkSelfdept.checked?"本部门内过滤":"") + "@" + "@"+ (chkFlowCreator.checked?"流程创建人":"") + "@" + "@"+ (chkUpDeptPrin.checked?"上级部门负责人":"") + "@" + "@"+ (chkUpTaskProcessor.checked?"上一环节处理人":"") + "@" + "@"+ (txtPerson.getValue()?encodeChars(txtPerson.getValue(),"',<,>,&,\""):"") + "@" + "@"+ (txtGroup.getValue()?encodeChars(txtGroup.getValue(),"',<,>,&,\""):"") + "@" + "@"+ (txtRole.getValue()?encodeChars(txtRole.getValue(),"',<,>,&,\""):"") + "@" ;
			listValue = listValue.replace(/@@@@@@/g,"@") ;
			listValue = listValue.replace(/@@@@/g,"@") ;
			listValue = listValue.replace(/@@@/g,"@") ;
			listValue = listValue.replace(/@@/g,"@") ;
			listValue = listValue.substring(1,listValue.length-1) ;
			if("@"==listValue) {
				alert("您还未选择相应规则") ;
				return ;
			}
			var obj = {
				persons : jsonIndiWorkNo.getValue(),
				groups : jsonIndiGroup.getValue(),
				roles : jsonIndiRole.getValue() ,
				is_self_dept : chkDeptPrin.checked ,
				is_dept_filter : chkSelfdept.checked,
				is_pcreator :  chkFlowCreator.checked ,
				is_up_dept : chkUpDeptPrin.checked ,
				up_node_person : chkUpTaskProcessor.checked,
				personCal : encodeChars(txtPerson.getValue(),"',<,>,&,\""),
				groupCal : encodeChars(txtGroup.getValue(),"',<,>,&,\""),
				roleCal : encodeChars(txtRole.getValue(),"',<,>,&,\"") ,
				name : listValue.replace(/&/g,"+") 
			}
			var Model = Ext.data.Record.create([{name:'key'},{name:'value'}]) ;
			var record = new Model(
				{
					key : obj.name ,
					value : obj
				}
			);
			listBox.view.store.add(record) ;
			listBox.view.refresh();
			selProcessorRuleWindow.close()
		}
	}
	
	//取消按钮
	var btnCancle = {xtype:'button' , text:'取消' , handler : function(){selProcessorRuleWindow.close()}}
	
	var panel = new Ext.Panel(
				{
					buttonAlign : 'right',
					buttons :[btnConfirm , btnCancle],
					bodyStyle : 'padding-top:20px',
					layout:'column',
					border:false,    
					bodyBorder : false ,
					items : [
						{	
							layout : 'column' ,
							border:false,  
							items:[
							{
								bodyStyle :'padding-left:20px',
								columnWidth : .3 ,
								border : false ,
								align : 'center' ,
								items : [chkIndiWorkNo]
							},
							{
								columnWidth : .45 ,
								border : false ,	
								items : [txtIndiWorkNo]
							},
							{
								bodyStyle : 'margin-left:5px',
								columnWidth : .25 ,
								border : false ,
								items : [btnIndiWorkNo]
							}
							]
						},
						{	
							layout : 'column' ,
							border:false,  
							items:[
							{
								bodyStyle :'padding-left:20px',
								columnWidth : .3 ,
								border : false ,
								align : 'center' ,
								items : [chkIndiGroup]
							},
							{
								columnWidth : .45 ,
								border : false ,	
								items : [txtIndiGroup]
							},
							{
								bodyStyle : 'margin-left:5px',
								columnWidth : .25 ,
								border : false ,
								items : [btnIndiGroup]
							}
							]
						},
						{	
							layout : 'column' ,
							border:false,  
							items:[
							{
								bodyStyle :'padding-left:20px',
								columnWidth : .3 ,
								border : false ,
								align : 'center' ,
								items : [chkIndiRole]
							},
							{
								columnWidth : .45 ,
								border : false ,	
								items : [txtIndiRole]
							},
							{
								bodyStyle : 'margin-left:5px',
								columnWidth : .25 ,
								border : false ,
								items : [btnIndiRole]
							}
							]
						},
						{	
							layout : 'column' ,
							border:false,  
							items:[
							{
								bodyStyle :'padding-left:20px',
								columnWidth : .5 ,
								border : false ,
								align : 'center' ,
								items : [chkDeptPrin]
							},
							{
								columnWidth : .5 ,
								border : false ,	
								items : [chkFlowCreator]
							}
							]
						},
						{	
							layout : 'column' ,
							border:false,  
							items:[
							{
								bodyStyle :'padding-left:20px',
								columnWidth : .5 ,
								border : false ,
								align : 'center' ,
								items : [chkUpDeptPrin]
							},
							{
								columnWidth : .5 ,
								border : false ,	
								items : [chkUpTaskProcessor]
							}
							]
						},
						{	
							layout : 'column' ,
							border:false,  
							items:[
							{
								bodyStyle :'padding-left:20px',
								columnWidth : .5 ,
								border : false ,
								align : 'center' ,
								items : [chkSelfdept]
							}
							]
						},
						{	
							bodyStyle :'padding-left:20px;padding-top:5px',
							layout : 'form' ,
							labelWidth:60,
							border:false,  
							items:[txtPerson,txtGroup,txtRole]
						}
					]
				}
	)
	
	
	SelProcessorRuleWindow.superclass.constructor.call(this,{
		title : '选择处理人规则',
		modal : true ,
		width : 350,
		height : 430,
		layout : 'fit',
		items :[panel]
	})
}	


Ext.extend(SelProcessorRuleWindow , Ext.Window , {
	showCheckGroupTreeWindow : function() {
		var indicatTree=new Ext.tree.TreePanel({
	    	id:'resTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
	        animate:false,
	        ctrlCasecade:false,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
	        enableDD:true,
	        containerScroll: true,
	        defaults: {bodyStyle:'padding:0px'}, 
	        border :false,
	        hideBorders :true,
	        rootVisible :true,
	        lines :false,
	       	bbar:['-','<font color="blue">*按住CTRL键可进行级联选择</font>','-'],
	        bodyBorder :false,
	        root:new Ext.app.CheckboxAsyncTreeNode({
	        	id : 'rootNode',
	        	//ccCheck:true,
	        	text:'所有群组',
	        	loader:new Ext.app.CheckboxTreeNodeLoader({
		            dataUrl:link2,//"/JTEAP//jteap/system/group/GroupAction!showGroupTreeForCheckAction.do"
		            listeners:{load:function(){
		            }}
		        }),
		        expanded :true
	        }),
	        submitChange:function(){
	        	var currentJsons=this.getRootNode().getCheckedIdAndTextJson(true,false);//取得包含第三状态的节点的被选中节点编号
	        	if(currentJsons!="" && currentJsons!=null) {
	        		currentJsons = "["+currentJsons.substring(0,currentJsons.length-1)+"]" ;
	        	}
	        	if(currentJsons.length <2) {
	        		jsonIndiGroup.setValue(null) ;
	        		txtIndiGroup.setValue("") ;
	        		indicatWindow.close() ;
	        		return ;
	        	}
	        	var arrays = Ext.decode(currentJsons) ;
	        	var ids = "" ;
	        	for(var i=0 ; i < arrays.length ; i++) {
	        		ids += arrays[i].id + ","
	        	}
	        	ids = ids.substring(0,ids.length-1) ;

	        	jsonIndiGroup.setValue(ids) ;
	        	
	        	var name = "" ;
	        	for(var i=0 ; i<arrays.length ; i++) {
	        		//name += arrays[i].text + "+" ;
	        		name += arrays[i].text + "@" ;
	        	}
	        	if(name.length >1) {
	        		name = name.substring(0,name.length-1) ;
	        	}
	        	txtIndiGroup.setValue(name) ;
	        	indicatWindow.close() ;
	        }
	    });
	    //资源选择窗口
		 var indicatWindow = new Ext.Window({
            layout:'fit',
            title:'资源选择器',
            width:250,
            height:350,
            frame:true,
            items: indicatTree,
            buttons: [{
                text:'确定',handler:function(){
                	indicatTree.submitChange();
                }
            },{
                text: '取消',
                handler: function(){
                    indicatWindow.close();
                }
            }]
		});
		//显示窗口
		indicatWindow.show();
	},
	showCheckRoleTreeWindow : function() {
		var indicatTree=new Ext.tree.TreePanel({
	    	id:'resTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
	        animate:false,
	        ctrlCasecade:false,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
	        enableDD:true,
	        containerScroll: true,
	        defaults: {bodyStyle:'padding:0px'}, 
	        border :false,
	        hideBorders :true,
	        rootVisible :true,
	        lines :false,
	       	bbar:['-','<font color="blue">*按住CTRL键可进行级联选择</font>','-'],
	        bodyBorder :false,
	        root:new Ext.app.CheckboxAsyncTreeNode({
	        	//ccCheck:false,
	        	text:'所有角色',
	        	loader:new Ext.app.CheckboxTreeNodeLoader({
		            dataUrl:link1//"/JTEAP/jteap/system/role/RoleAction!showRoleTreeForCheckAction.do"
		        }),
		        expanded :true
	        }),
	        submitChange:function(){
	        	var currentJsons=this.getRootNode().getCheckedIdAndTextJson(true,false);//取得包含第三状态的节点的被选中节点编号
	        	if(currentJsons!="" && currentJsons!=null) {
	        		currentJsons = "["+currentJsons.substring(0,currentJsons.length-1)+"]" ;
	        	}
	        	if(currentJsons.length <2) {
	        		jsonIndiRole.setValue(null) ;
	        		txtIndiRole.setValue("") ;
	        		indicatWindow.close() ;
	        		return ;
	        	}
	        	var arrays = Ext.decode(currentJsons) ;
	        	var ids = "" ;
	        	for(var i=0 ; i < arrays.length ; i++) {
	        		ids += arrays[i].id + ","
	        	}
	        	ids = ids.substring(0,ids.length-1) ;
	        	jsonIndiRole.setValue(ids) ;
	        	
	        	var name = "" ;
	        	for(var i=0 ; i<arrays.length ; i++) {
	        		//name += arrays[i].text + "+" ;
	        		name += arrays[i].text + "@" ;
	        	}
	        	if(name.length >1) {
	        		name = name.substring(0,name.length-1) ;
	        	}
	        	txtIndiRole.setValue(name) ;
	        	indicatWindow.close() ;
	        }
	    });
	    //资源选择窗口
		 var indicatWindow = new Ext.Window({
            layout:'fit',
            title:'资源选择器',
            width:250,
            height:350,
            frame:true,
            items: indicatTree,
            buttons: [{
                text:'确定',handler:function(){
                	indicatTree.submitChange();
                }
            },{
                text: '取消',
                handler: function(){
                    indicatWindow.close();
                }
            }]
		});
		//显示窗口
		indicatWindow.show();
	}
})