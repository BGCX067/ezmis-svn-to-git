
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});


/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	//if(button.id=='btnAdd' || button.id=='btnDel' ) //|| button.id=='btnModify'
	if(button.id=='btnEnable' || button.id=='btnModify' || button.id=='btnDel'|| button.id=='btnZf'){
		button.disable(true);	
	} 
}

/**
 * 领用单生效
 */
function btnEnable_Click(){
	mxGrid.enableLydgl();
} 

/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	
	var result=showModule(link6,true,800,645);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
}
/**
 * 修改表单
 */
function btnModify_Click(){
	var select = "";
	//if(tabPanel.getActiveTab().title == "老系统领用单"){
	//	select = historyGrid.getSelectionModel().getSelections()[0];
	//}else{
		select = rightGrid.getSelectionModel().getSelections()[0];
	//}
	if(select){
		var url=link6+"&docid="+select.json.ID;
		var sqbh = select.json.SQBH;
		result=showModule(url,true,800,645,'sqbh|'+sqbh);
		
		//if(result=="true"){
		//if(nowGrid.getSelectionModel().getSelections()[0]){
			rightGrid.getStore().on('load',function(){
				var whereSql = "obj.lydgl.id ='" + select.json.ID +"' and obj.zt=0";
				mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
				//mxGrid.changeToListDS(link2);
				mxGrid.store.load();
			});
			rightGrid.getStore().reload();
			/**
		}else{
			historyGrid.getStore().on('load',function(){
				var whereSql = "obj.lydgl.id ='" + select.json.ID +"' and obj.zt=0";
				mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
				//mxGrid.changeToListDS(link2);
				mxGrid.store.load();
			});
			historyGrid.getStore().reload();
		}
		**/
		//}
	}
	
}
/**
 *	查询已作废的 领料单以及明细
 **/
function btnYzf_Click(){
	var btnZf = mainToolbar.items.get('btnZf');
	var btnYzf = mainToolbar.items.get('btnYzf');
	if(btnZf){
		if(btnZf.getText()=="作废"){
			btnZf.setText("撤销作废");
		}else{
			btnZf.setText("作废");
		}
	};
	var zt = 0;
	if(btnYzf){
		if(btnYzf.getText()=="未作废领料单"){
			btnYzf.setText("已作废领料单");
			rightGrid.store.baseParams={};
		}else{
			btnYzf.setText("未作废领料单");
			rightGrid.store.baseParams={zf:'1'};
			zt = 9;
		}
	};
	rightGrid.store.load();
}
/*
 导出excel
*/
function export_Excel(){
	var select=rightGrid.getSelectionModel().getSelections();
	var mxselect=mxGrid.getSelectionModel().getSelections();
	var lydid = "";
	var mxid = "";
	for(var j=0;j<select.length;j++){
		lydid  = lydid+"'"+select[j].id+"',";
	}
	alert(lydid);
	alert(select.length);
	var path = contextPath +"/jteap/wz/lydgl/LydmxAction!exportExcelByLyd.do";
	if(mxselect.length==0){
		path = path+"?lydid="+lydid;
	}else{
		for(var i = 0; i < mxselect.length; i++){
			//mxid.push("'"+mxselect[i].id+"'");//取得他们的id并组装
			mxid  = mxid+"'"+mxselect[i].id+"',";
		}
		path = path+"?mxid="+mxid;
	}
	window.open(path);
}
/**
 * 删除
 */
 function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
	var mxselect=mxGrid.getSelectionModel().getSelections();
	var ids="";
	var mxIds="";
	if(mxselect && mxselect.length > 0){
		for(var i = 0; i < mxselect.length; i++){
			mxIds+=mxselect[i].id+",";
		}
	}
	if(select && select.length>0){
		for(var i = 0; i < select.length; i++){
			ids+=select[i].id+",";//取得他们的id并组装
		}
	}
   	mxGrid.deleteSelect(ids,mxIds,1);
}
/**
 * 作废
 */
function btnZf_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
	var mxselect=mxGrid.getSelectionModel().getSelections();
	var ids="";
	var mxIds="";
	if(mxselect && mxselect.length > 0){
		for(var i = 0; i < mxselect.length; i++){
			mxIds+=mxselect[i].id+",";
		}
	}
	if(select && select.length>0){
		for(var i = 0; i < select.length; i++){
			ids+=select[i].id+",";//取得他们的id并组装
		}
	}
		
	var btnZf = mainToolbar.items.get('btnZf');
	if(btnZf){
		if(btnZf.getText()=='撤销作废'){
			if(select){
			    mxGrid.rbSelect(ids,mxIds);
			}else{
				alert("请选择要撤销的领料单!");
				return;
			}
		}else{
			if(select){
			    mxGrid.deleteSelect(ids,mxIds,0);
			}else{
				alert("请选择要作废的领料单!");
				return;
			}
		}
	}
}


/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	leftTree.createNode();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyNode();
}




var rightGrid=new RightGrid();
var mxGrid=new MxRightGrid();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rightGrid]
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
	items:[mxGrid]
}

