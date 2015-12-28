
var LV=Ext.app.LabelValuePanel;


ModuleDetailForm =function (){
	centerPanel=this;
	/**
	 * 创建属性面板
	 */
    createProptyGrid=function(width){
	    var propsGrid = new Ext.grid.PropertyGrid({
	        nameText: '基本属性面板',
	        width:width,
	        autoHeight:true,
	        viewConfig : {
	            forceFit:true,
	            scrollOffset:2 // the grid will never have scrollbars
	        }
	    });
	    propsGrid.on("beforeedit",function(){return false})
	    return propsGrid;
    }
    
    
    createOpsGrid=function(){
 		var cm = new Ext.grid.ColumnModel([{
	           header: "操作名称",
	           dataIndex: 'text',
	           width: 100
	        },{
	        	header: "操作简称",
				dataIndex: 'name',
				width: 100
	        },{
				header: "操作提示",
				dataIndex: 'tip',
				width: 150
	        },{
				header: "按钮文本",
				dataIndex: 'showText',
				width: 60,
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					return value?"显示":"不显示";
				}
	        },{
				header: "管理员资源",
				dataIndex: 'adminOp',
				width: 70,
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					return value?"是":"不是";
				}
	        }
	    ]);
	
		var config={
	        store: new Ext.data.Store(),
	        cm: cm,
	        width:532,
	        height:170,
	        frame:true
	    };
	    
    	var grid=new Ext.grid.GridPanel(config);
    	return grid
    }
    this.opsGrid=createOpsGrid();
     
    
	ModuleDetailForm.superclass.constructor.call(this,{id:'centerpanel',
		title:'模块详细信息',
		region:'center',
		frame:true,
		items: [{
			id:'module',
			authWidth:true,
			border:false,
			autoHeight:true,
			items:[{
				id:'center',
		 		layout:'column',
		 		border:false,
		 		autoHeight:true,
				items:[{
		        	//第一行布局
				    id:'title',
		        	columnWidth:1,
		        	layout:'fit',
		        	border:false,
		        	frame:false,
		        	autoHeight:true,
		        	items:[{
			            xtype:'fieldset',
			            title: '基本属性',
			            autoHeight:true,
			            autoWidth:true,
			            defaultType: 'textfield',
			            collapsed: false,
			            listeners:{
			            	//当fieldset渲染的时候，在当前的 fieldset中创建一个propertygrid
			            	//这么做主要是因为ext grid的宽度的问题，无法到达100%宽
			            	render:function(ct){
				            	var div=document.createElement("div");
								div.id="gridBody";
								div.style.width='100%';
								ct.body.insertFirst(div);
								var gridBody=Ext.get("gridBody");
								width=gridBody.getComputedWidth();
								centerPanel.propertyGrid=createProptyGrid(width);
								centerPanel.propertyGrid.render(div);
								centerPanel.propertyGrid.setSource({
							        "1.模块名称": "",
							        "2.链接": "",
							        "3.图标": "",
							        "4.是否可见": "",
							        "5.备注":""
							}); 
			            }}
			        },{
			            xtype:'fieldset',
			            title: '操作列表',
			            id:'DivExtProperties',
			            autoHeight:true,
			            autoWidth:true,
			            items:this.opsGrid
			        }]
		        }]
	 		}]
		}]
	});  
}

Ext.extend(ModuleDetailForm,Ext.Panel,{
	
	 /**
      * 显示角色详细信息
      * @param oNode 当前点击的角色的节点对象
      */
	showDetailPanel:function(oNode){
		var centerPanel=this;
		Ext.Ajax.request({
			url:link6+"?id="+oNode.id,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			//数据加载完成后，拼接成规则数据填写到propertygrid组件中
		 			var module=responseObject.data;
		 			
 					var data={
 						"1.模块名称": module.resName,
				        "2.链接": module.link,
				        "3.图标": module.icon,
				        "4.是否可见": module.visiabled?"可见":"不可见",
				        "5.备注":module.remark
					};
					centerPanel.propertyGrid.setSource(data);
					//加载所拥有的资源
					var ct=Ext.getCmp("DivExtProperties");
					//如果fieldset折叠了,就刷新资源树了
					var ds=centerPanel.opsGrid.getStore();
					ds.removeAll();
					for(var i=0;i<module.childRes.length;i++){
						var op=module.childRes[i];
						if(op.type=="操作"){
							 var opRec = new Operation({
					 			text: op.resName,
					            name: op.sn,
					            tip:op.tip,
					            icon:op.icon,
					            showText:op.showText,
					            adminOp:op.adminOp
					        });
					        ds.add(opRec);
							
						}
					
					}
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("服务器忙，请稍后访问");
		 	},
		 	method:'POST'	
		});
	}
});	
