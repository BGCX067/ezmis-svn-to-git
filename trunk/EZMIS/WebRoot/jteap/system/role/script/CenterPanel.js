


//var formDetailMessage=new FormDetailMessage();
var LV=Ext.app.LabelValuePanel;

/**
 * Detail Panel
 */
CenterPanel =function (){
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
    
    //this.resAssignTree=new ResourceColumnTree("40288147179b76a001179b81d6650001",true);
	CenterPanel.superclass.constructor.call(this,{
		id:'centerpanel',
		title:'角色详细信息',
		region:'center',
		frame:true,
		items: [{
			id:'role',
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
							        "1.角色名称": "",
							        "2.是否继承资源": "",
							        "3.备注": ""
							}); 
			            }}
			        },{
			            xtype:'fieldset',
			            title: '扩展属性',
			            id:'DivExtProperties',
			            autoHeight:true,
			            autoWidth:true,	
			            collapseFirst:false,
			            checkboxToggle:true,
			            collapsed: true
			            
			        }]
		        }]
	 		}]
		}]
	});
}
/**
 * 作用
 * 用来显示角色的详细信息
 */
Ext.extend(CenterPanel,Ext.Panel,{
     /**
      * 显示角色详细信息
      * @param oNode 当前点击的角色的节点对象
      */
	showDetailPanel:function(oNode){
		var centerPanel=this;
		Ext.Ajax.request({
			url:link10+"?roleId="+oNode.id,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			//数据加载完成后，拼接成规则数据填写到propertygrid组件中
		 			var role=responseObject.data[0];
 					var data={
						"1.角色名称": role.rolename,
						"2.是否继承资源": role.inheritable?"继承":"不继承",
						"3.备注": role.remark 
					};
					centerPanel.propertyGrid.setSource(data);
					//加载所拥有的资源
					var ct=Ext.getCmp("DivExtProperties");
					//如果fieldset折叠了,就刷新资源树了
					if(!ct.collapsed){
						if(centerPanel.resTree){
							var resTree=centerPanel.resTree;
							resTree.loader.dataUrl=link12+"?roleId="+oNode.id;
							resTree.getRootNode().reload();
						}else{
							centerPanel.resTree=new ResourceColumnTree(oNode.id,true);
							var ct=Ext.getCmp("DivExtProperties");
							centerPanel.resTree.render(ct.body);
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
})	