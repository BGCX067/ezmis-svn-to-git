
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});


/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	//if(button.id=='btnAdd' || button.id=='btnDel' ) //|| button.id=='btnModify'
	if(button.id=='btnEnable'){
		button.disable(true);	
	} 
}

/**
 * 领用单生效
 */
function btnEnable_Click(){
	var select = xqjhsqMxGrid.getSelectionModel().getSelected();
	var selections = xqjhsqMxGrid.getSelectionModel().getSelections();
	var ids = "";
	if(selections.length>0){
		for(var i = 0;i<selections.length;i++){
			ids += selections[i].data.id+",";
		}
	}
	Ext.Ajax.request({
		url:link14,
		success:function(ajax){
	 		var responseText=ajax.responseText;	
	 		var responseObject=Ext.util.JSON.decode(responseText);
	 		if(responseObject.success){
	 			alert("指定成功");
	 		}else{
	 			alert(responseObject.msg);
	 		}				
		},
	 	failure:function(){
	 		alert("提交失败");
	 	},
	 	method:'POST',
	 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
	});
	xqjhSqGrid.getStore().reload();
	xqjhSqGrid.getSelectionModel().selectFirstRow();
	var jhid = xqjhSqGrid.store.data.items[0].id;
	xqjhsqMxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',id:jhid};
	xqjhsqMxGrid.store.load();
} 


var xqjhSqGrid=new RightGrid();
var xqjhsqMxGrid=new xqjhsqMxGrid();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[xqjhSqGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}

//南边布局
var lySouth={
	layout:'border',
	id:'south-panel',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:250,
	border:false,
	margins:'0 0 0 0',
	items:[xqjhsqMxGrid]
}

