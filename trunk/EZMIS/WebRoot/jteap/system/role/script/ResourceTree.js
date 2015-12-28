/**
 * 传播/不传播 单选事件
 */
function changeRadioState(radio,nodeId,treeId){
	var radio1=$(nodeId+"_a");		//传播
	var radio2=$(nodeId+"_b");		//不传播
	var tree=Ext.getCmp(treeId);	//树形对象
	var node=tree.getNodeById(nodeId);	//当前节点编号
	
	var tmpRadio=(radio.value==radio1.value?radio2:radio1);
	if(confirm("是否确定将指定资源设定为"+(radio.value=='true'?"【传播】":"【不传播】")+"，如果这个资源有子资源，则子资源也将设定同该资源一致")){
		if(radio.value=='true') {
			var ids=node.id+","+changeCascadeState(node,radio.value) + changeUpCascadeState(node,radio.value);
		}else {
			var ids=node.id+","+changeCascadeState(node,radio.value) 
		}
		Ext.Ajax.request({
			url:link15+"?ids="+ids+"&communicable="+radio.value,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.util.JSON.decode(responseText);
	 			if(responseObject.success)
	 				alert("操作成功");
	 		},
	 		failure:function(){
	 			alert("操作失败");
	 		},
	 		method:'POST'
		});
	}else{
		tmpRadio.checked=true;
	}
}
/**
 * 级联更新所有radio的状态
 * @node 当前节点
 * @stateValue 当前状态 1:传播 2:不传播
 * @return 返回被修改的r2r的id逗号分隔字符串
 */
function changeCascadeState(node,stateValue){
	var result="";
	for(var i=0;i<node.childNodes.length;i++){
		var childNode=node.childNodes[i];
		var radio1=$(childNode.id+"_a");		//传播
		var radio2=$(childNode.id+"_b");		//不传播
		var tmpRadio=(radio1.value==stateValue?radio1:radio2);
		tmpRadio.checked=true;
		result+=changeCascadeState(childNode,stateValue)+","+childNode.id;
	}
	return result;
}

/**
 * 级联更新所有父亲radio的状态
 * @node 当前节点
 * @stateValue 当前状态 1:传播 2:不传播
 * @return 返回被修改的r2r的id逗号分隔字符串
 */
function changeUpCascadeState(node,stateValue){
	var result="";
	if(node.parentNode != resourceTree.root){
		var radio1=$(node.parentNode.id+"_a");		//传播
		var radio2=$(node.parentNode.id+"_b");		//不传播
		var tmpRadio=(radio1.value==stateValue?radio1:radio2);
		tmpRadio.checked=true;
		result+=changeUpCascadeState(node.parentNode,stateValue)+","+node.parentNode.id;
	}
	return result;
}



/**
 * @author tantyou
 * @date 2008-1-24
 * 用于显示特定角色的资源的继承至columnTree的专门的column tree
 * @param bReadOnly 是否为只读方式
 */
ResourceColumnTree =function (roleId,bReadOnly){
	this.roleId=roleId;
	resourceTree=this;

	config={
       	autoWidth:true,
        height:210,
        rootVisible:false,
        autoScroll:true,
        bbar:['*<font color="red">红色</font>资源为直接指定的资源，<font color="blue">蓝色</font>为从父角色继承的资源'],

        containerScroll: true,
        columns:[{
            header:'拥有的资源',
            width:360
        },{
            header:'传播',
            width:130,
            dataIndex:'communicable',
            //渲染该列为是否传播列
            renderer:function(value,n,a){
            	if(bReadOnly){
            		return value?"<image src='icon/icon_11.gif'/>":"<image src='icon/icon_10.gif'/>"
            	}else{
	            	var checked1="",checked2="";
	            	if(value){
	            		checked1="checked";checked2="";
	            	}else{
	            		checked1="";checked2="checked";
	            	}
	            	var result= "<input type='radio' id='"+n.id+"_a' name='Res"+n.id+"' value='true' "+checked1+" onclick='changeRadioState(this,\""+n.id+"\",\""+n.getOwnerTree().id+"\");'/>传播 &nbsp;&nbsp;";
	            	result+="<input type='radio' id='"+n.id+"_b' name='Res"+n.id+"' value='false' onclick='changeRadioState(this,\""+n.id+"\",\""+n.getOwnerTree().id+"\");' "+checked2+"/>不传播"; 	
	            	return result;
            	}
            }
        }],
		//动态数据加载器，加载当前角色的资源
        loader: new Ext.tree.TreeLoader({
            dataUrl:link12+"?roleId="+roleId,
            uiProviders:{
                'col': ResourceNodeUI
            }
        }),
		//根节点
        root: new Ext.tree.AsyncTreeNode({
            text:'roles'
        })
	};
	
	if(!bReadOnly){
		Ext.apply(config,{
			tbar:['->',{text:'指定资源',handler:resourceTree.showIndicatResourceWindow,tooltip:'直接指定您所拥有的资源给这个角色'},
					   {text:'继承资源',handler:resourceTree.showExtendResourceWindow,tooltip:'选择性继承资源'}]
		})
	}
	ResourceColumnTree.superclass.constructor.call(this,config);
}
Ext.extend(ResourceColumnTree,Ext.tree.ColumnTree,{
	/**
	 * 从该角色所拥有的资源中找到所有"指定资源"的编号
	 */
	findIndicatResourceIds:function(oNode){
		sResult="";
		if(oNode==null){	
			oNode=this.getRootNode();
		}else{
			if(!oNode.attributes.inherit){
				//因为在R2RAction中添加了resourceId属性，所以在此处可以取得资源的编号
				sResult=oNode.attributes.resourceId+",";
			}
		}
		//递归所有的儿子节点
		if(oNode.childNodes.length>=0){
			for(var i=0;i<oNode.childNodes.length;i++){	
				sResult+=this.findIndicatResourceIds(oNode.childNodes[i]);
			}
		}
		return sResult;
	},	
	/**
	 * 从该角色所拥有的资源中找到所有"继承资源"的编号
	 */
	findExtendResourceIds:function(oNode){
		sResult="";
		if(oNode==null){	
			oNode=this.getRootNode();
		}else{
			if(oNode.attributes.inherit){
				//因为在R2RAction中添加了resourceId属性，所以在此处可以取得资源的编号
				sResult=oNode.attributes.resourceId+",";
			}
		}
		//递归所有的儿子节点
		if(oNode.childNodes.length>=0){
			for(var i=0;i<oNode.childNodes.length;i++){	
				sResult+=this.findExtendResourceIds(oNode.childNodes[i]);
			}
		}
		return sResult;
	},
	/**
	 * 显示“指定资源“选择树
	 */
	showIndicatResourceWindow:function(){
		//“指定资源”资源树
		var indicatTree=new Ext.tree.TreePanel({
	    	id:'resTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
	        animate:false,
	        ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
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
	        	
	        	ccCheck:true,
	        	text:'我所拥有的资源',
	        	loader:new Ext.app.CheckboxTreeNodeLoader({
		            dataUrl:link13,listeners:{load:function(){
		            	//将树加载完之后将已经指定的资源勾选上
		            	var ids=resourceTree.findIndicatResourceIds().split(",");
		            	for(var i=0;i<ids.length;i++){
		            		if(ids[i]!=""){
		            			var oNode=indicatTree.getNodeById(ids[i]);
		            			if(oNode)
		            				oNode.setChecked(true);
		            		}
		            	}
		            	
		            	//包括第3态节点的被选中节点id集合
		            	indicatTree.originalValue=indicatTree.getRootNode().getCheckedIds(true,false);
		            }}
		        }),
		        expanded :true
	        }),
	        submitChange:function(){
	        	var thisx=this;
	        	var currentIds=this.getRootNode().getCheckedIds(true,false);//取得包含第三状态的节点的被选中节点编号
	        	
	        	//提交数据
			 	Ext.Ajax.request({
			 		url:link14,
			 		params:{roleId:resourceTree.roleId,original:thisx.originalValue,now:currentIds},
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.util.JSON.decode(responseText);
			 			if(responseObject.success)
			 				alert("资源指定成功");
			 			else
			 				alert("资源指定失败："+responseObject.msg);
			 			
						indicatWindow.close();
						resourceTree.getRootNode().reload();
			 		},
			 		failure:function(){
			 			alert("资源指定失败");
			 		},
			 		method:'POST'
			 	})
	        	
	        	
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
                	//var x=indicatTree.getRootNode().getCheckedIds(true,false);
                	
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
		/**
	 * 显示“继承资源“选择树
	 */
	showExtendResourceWindow:function(){
		//“指定资源”资源树
		var extendTree=new Ext.tree.TreePanel({
	    	id:'resTree2',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
	        animate:false,
	        ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
	        enableDD:true,
	        containerScroll: true,
	        defaults: {bodyStyle:'padding:0px'}, 
	        border :false,
	        hideBorders :true,
	        rootVisible :true,
	        lines :false,
	       	bbar:['-','<font color="blue">*按住CTRL键可进行级联选择</font>','-'],
	        bodyBorder :false,
	        
	        /**
	         * 创建根节点，如果当前角色无父角色，表明该角色无可继承的角色
	         * 否则执行数据请求，请求父亲节点所拥有的可传播的资源
	         */
	        createRootNode:function(){
	        	var curRoleNode=roleTree.getSelectionModel().getSelectedNode();
	        	var parentNode=curRoleNode.parentNode;
	        	if(!parentNode.isRootNode()){
	        		return new Ext.app.CheckboxAsyncTreeNode({
			        	ccCheck:true,
			        	text:'可继承的资源',
			        	loader:new Ext.app.CheckboxTreeNodeLoader({
				            dataUrl:link16+"?roleId="+parentNode.id,
				            listeners:{
				            	load:function(){
					            	//将树加载完之后将已经指定的资源勾选上
					            	var ids=resourceTree.findExtendResourceIds().split(",");
					            	for(var i=0;i<ids.length;i++){
					            		if(ids[i]!=""){
					            			var oNode=extendTree.getNodeById(ids[i]);
					            			if(oNode)
					            				oNode.setChecked(true);
					            		}
					            	}
					            	//包括第3态节点的被选中节点id集合
		            				extendTree.originalValue=extendTree.getRootNode().getCheckedIds(true,false);
					            }
				            }
				        }),
				        expanded :true
			        });
	        	}else{
	        		return new Ext.tree.TreeNode({text:'无父角色，无可继承资源'});
	        	}
	        	
	        },
	        /**
	         * 提交更新
	         */
	        submitChange:function(){
	        	var thisx=this;
	        	var currentIds=this.getRootNode().getCheckedIds(true,false);//取得包含第三状态的节点的被选中节点编号
	        		
	        	//提交数据
			 	Ext.Ajax.request({
			 		url:link17+"?roleId="+resourceTree.roleId+"&original="+thisx.originalValue+"&now="+currentIds,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.util.JSON.decode(responseText);
			 			if(responseObject.success)
			 				alert("继承资源指定成功");
			 			else
			 				alert("继承资源指定失败："+responseObject.msg);
			 			
						extendWindow.close();
						resourceTree.getRootNode().reload();
			 		},
			 		failure:function(){
			 			alert("资源指定失败");
			 		},
			 		method:'POST'
			 	})
	        	/*
	        	var ids=this.getRootNode().getCheckedIds(true,false);//取得包含第三状态的节点的被选中节点编号
	        	//提交数据
			 	Ext.Ajax.request({
			 		url:link14+"?roleId="+resourceTree.roleId+"&resIds="+ids,
			 		success:function(ajax){
			 			var responseText=ajax.responseText;	
			 			var responseObject=Ext.util.JSON.decode(responseText);
			 			if(responseObject.success)
			 				alert("资源指定成功");
			 			else
			 				alert("资源指定失败："+responseObject.msg);
			 			
						indicatWindow.close();
						resourceTree.getRootNode().reload();
			 		},
			 		failure:function(){
			 			alert("提交失败");
			 		},
			 		method:'POST'
			 	})
	        	
	        	*/
	        }
	    });
		//设定根节点
	    var oRootNode=extendTree.createRootNode();
	    extendTree.setRootNode(oRootNode);
	    //资源选择窗口
		 var extendWindow = new Ext.Window({
            layout:'fit',
            title:'资源选择器',
            width:250,
            height:350,
            frame:true,
            items: extendTree,
            buttons: [{
                text:'确定',handler:function(){
                	extendTree.submitChange();
                }
            },{
                text: '取消',
                handler: function(){
                    extendWindow.close();
                }
            }]
		});
		//显示窗口
		extendWindow.show();
	}
})	