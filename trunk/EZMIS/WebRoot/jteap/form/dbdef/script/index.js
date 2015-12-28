//工具栏  
//1.添加表
//2.删除表
//3.修改表
//4.添加字段
//5.修改字段
//6.删除字段
//7.导入物理表
//8.构造物理表
//9.查询表数据
//10.清除表数据
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
	/*items:[
		{disabled:true,id:'btnAddTable',text:'添加表',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnAddTable_click}},
		{disabled:true,id:'btnModiTable',text:'修改表',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnModifyTable_click}},
		{disabled:true,id:'btnDelTable',text:'删除表',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnDelTable_click}},
		{disabled:false,id:'btnImportTable',text:'导入物理表',cls: 'x-btn-text-icon',icon:'icon/icon_10.gif',listeners:{click:btnImportTable_click}},
		{disabled:true,id:'btnRebuildTable',text:'重建物理表',cls: 'x-btn-text-icon',icon:'icon/icon_10.gif',listeners:{click:btnRebuildTable_click}},
		{disabled:true,id:'btnQueryTable',text:'查看表数据',cls: 'x-btn-text-icon',icon:'icon/icon_10.gif',listeners:{click:btnQueryTable_click}},

		{disabled:true,id:'btnAddColumn',text:'新增字段',cls: 'x-btn-text-icon',icon:'icon/icon_5.gif',listeners:{click:btnAddColumn_click}},
		{disabled:true,id:'btnModifyColumn',text:'修改字段',cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:btnModifyColumn_click}},
		{disabled:true,id:'btnDelColumn',text:'删除字段',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:btnDelColumn_click}}
	]*/
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiTable' 
		|| button.id=='btnDelTable' || button.id=='btnRebuildTable' || button.id=='btnDelColumn'
		|| button.id=='btnQueryTable' || button.id=='btnAddColumn'||button.id=='btnModifyColumn'
		|| button.id=='btnAddCatalog' || button.id=='btnModiCatalog' || button.id=="btnDelCatalog") 
		button.setDisabled(true);
}

function btnQueryTable_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var url=contextPath+"/jteap/form/dbdef/queryIndex.jsp?tableid="+oNode.id;
	showModule(url,true,800,600);
}
/**
 * 重建物理表
 */
function btnRebuildTable_Click(){
	
	if(!window.confirm("重建物理表将会删除该表中已存在的所有数据，属于危险操作，是否确定需要重建该物理表?")){
		return;
	}
	
	
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"物理表重建中，请稍等"}); 
	myMask.show();
	
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	//判断是否已经存在表定义对象
 	Ext.Ajax.request({
 		url:link13+"?tableid="+oNode.id,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			var msg=responseObject.msg;
 			if(responseObject.success){
 				alert("物理表重建成功");
 			}else{
 				alert("物理表重建失败"+responseObject.msg);
 				
 			}
 			myMask.hide();
 		},
 		failure:function(){
 			alert("连接超时，重建失败");
 			myMask.hide();
 		},
 		method:'POST'
 	})
}	
/**
 * 导入物理表
 */
function btnImportTable_Click(){
		var searchField=new Ext.app.SearchField({width:160});
		var loader=new Ext.app.CheckboxTreeNodeLoader({dataUrl:link10});
		var tableTree=new Ext.tree.TreePanel({
	    	id:'tableTree',
	        autoScroll:true,
	        autoHeight:false,
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
	        tbar:['-',searchField],
	       	bbar:['-','<font color="blue">*按住CTRL键可进行级联选择</font>','-'],
	        bodyBorder :false,
	        root:new Ext.app.CheckboxAsyncTreeNode({
	        	ccCheck:true,
	        	text:'物理表',
	        	loader:loader,
		        expanded :true
	        }),
	        submitChange:function(){
	        	alert('ok');
	         }
	    });
	    
	    searchField.on("query",function(){
	    	loader.baseParams.key=this.getValue();
	    	tableTree.root.reload();
		});
		searchField.on("cancel",function(){
			loader.baseParams.key=this.getValue();
			tableTree.root.reload();
		})
	    
	 var importWindow = new Ext.Window({
        layout:'fit',
        title:'导入物理表',
        width:400,
        height:350,
        frame:true,
        items:tableTree,
        buttons: [{
            text:'确定',handler:function(){
            	var ids=tableTree.getRootNode().getCheckedIds(false,false);
            	
            	importTable(ids,importWindow);
            	//tableTree.submitChange();
            }
        },{
            text: '取消',
            handler: function(){
                importWindow.close();
            }
        }]
	});
	//显示窗口
	importWindow.show();
}
/**
 * 新增字段
 */
function btnAddColumn_Click(){
	var editColumnWindow=new EditColumnWindow(null);
	editColumnWindow.show();
	editColumnWindow.center();
}


/**
 * 删除字段
 */
function btnDelColumn_Click(){
	if(window.confirm("确认删除当前选中的字段吗？")){
		var select=columnGrid.getSelectionModel().getSelections();
		var ids="";
		for(var i=0;i<select.length;i++){
		   ids+=(select[i].id+",");
		}
		//提交数据
	 	Ext.Ajax.request({
	 		url:link9,
	 		success:function(ajax){
	 			var responseText=ajax.responseText;	
	 			var responseObject=Ext.decode(responseText);
	 			if(responseObject.success){
	 				alert("删除成功");
	 				columnGrid.getStore().reload();
	 			}else{
	 				var msg=responseObject.msg;
	 				if(msg!=""){
	 					alert(msg); return;
	 				}
	 				else
	 					alert("删除失败");
	 			}
	 		},
	 		failure:function(){
	 			alert("提交失败");
	 		},
	 		method:'POST',
	 		params:{ids:ids}
	 	})	
   }
}

/**
 * 修改字段
 */
function btnModifyColumn_Click(){
	var select=columnGrid.getSelectionModel().getSelections()[0];
    var id=select.id;
    var editColumnWindow=new EditColumnWindow(id);
	editColumnWindow.show();
	editColumnWindow.center();
	editColumnWindow.loadData();
	
}


/**
 * 修改表
 */
function btnModiTable_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	
	var editTableWindow=new EditTableWindow(oNode.id);
	editTableWindow.show();
	editTableWindow.loadData();
}
/**
 * 添加表
 */
function btnAddTable_Click(){
	var editTableWindow=new EditTableWindow();
	editTableWindow.show();
}
/**
 * 删除表
 */
function btnDelTable_Click(){
	leftTree.delTable();
}

/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedCatalog();
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	leftTree.createCatalog(true);
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyCatalog();
}

/**
 * 选择物理表，导入选择的物理表
 * 1.判断是否存在已经已经定义过的表,并给予提示是否覆盖
 * 2.导入表
 */
function importTable(ids,importWindow){
	var myMask = new Ext.LoadMask(importWindow.getEl(), {msg:"导入中，请稍等"}); 
	myMask.show();
	
	
	//判断是否已经存在表定义对象
 	Ext.Ajax.request({
 		url:link11+"?tableids="+ids,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			var msg=responseObject.msg;
 			if(responseObject.success){
 				if(msg && msg!=''){
 					Ext.MessageBox.show({
			           title:'覆盖已存在的定义表?',
			           msg: "【"+msg+"】已经存在定义表"+'，\r\n您是否希望覆盖已存在的定义表对象？如果选择【覆盖】，表示重新创建对应的定义表，如果点击【取消】则不做任何操作',
			           buttons: {yes:'覆盖',cancel:'取消'},
			           width:400,
			           fn: function(bt){
			           		if(bt=='yes'){
			           			//覆盖
			  					importTable2(ids,importWindow,myMask);
			           		}
			           },
			           icon: Ext.MessageBox.QUESTION
			       });
 				}else{
 					importTable2(ids,importWindow,myMask);
 				}
 			}else{
 				alert("导入失败,原因不明");
 				importWindow.close();
 			}
 		},
 		failure:function(){
 			alert("连接超时，导入失败");
 		},
 		method:'POST',
 		params: {}
 	})
}
/**
 * 导入物理表
 */
function importTable2(ids,importWindow,mask){
	Ext.Ajax.request({
 		url:link12+"?tableids="+ids ,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			var msg=responseObject.msg;
 			if(responseObject.success){
 				if(msg && msg!=''){
 					alert(msg);
 				}else{
 					alert("导入成功");
 				}
 				leftTree.getRootNode().reload();
 				importWindow.close();
 			}else{
 				alert("导入失败,原因不明");
 				importWindow.close();
 			}
 		},
 		failure:function(){
 			alert("连接超时，导入失败");
 		},
 		method:'POST'
 	})
}

	
	
	

var columnGrid=new ColumnGrid();

//左边的树
var leftTree=new LeftTree();

//查询面板
var searchPanel=new SearchPanel();
			

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[columnGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


