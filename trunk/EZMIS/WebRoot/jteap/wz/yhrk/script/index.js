

//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel' || button.id=='btnPrint' ||button.id=='btnEnable') 
		button.setDisabled(true);
}


/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var result=showModule(link6,true,650,500);
	rightGrid.getStore().reload();
}
/**
 * 修改表单
 */
function btnModify_Click(){
	var rightGridSelect=rightGrid.getSelectionModel().getSelected();
	var mxRightGridSelects = mxGrid.getSelectionModel().getSelections();
	if(rightGridSelect){
		var ids ="";
		for(var i=0;i<mxRightGridSelects.length;i++){
			if(mxRightGridSelects[i].json.id!=null&&mxRightGridSelects[i].json.id!='')
			ids +="'"+mxRightGridSelects[i].json.id+"',";
		}
		ids=ids.substring(0,ids.length-1);
		var url = contextPath+"/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do?";
		if(responseObject != null){
			var url=link6+"&docid="+rightGridSelect.json.id;
			new $FW({url:url,height:645,width:800,type:'T2',userIF:true,baseParam:{responseObject:responseObject},callback:function(retValue){
				rightGrid.getStore().reload();
				var whereSql = "obj.yhdgl.id ='" + rightGridSelect.json.id +"'";
				mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
				mxGrid.store.reload();
				
			}}).show();	//模式对话框
		}else{
			AjaxRequest_Sync(url,{queryParamsSql:"obj.id in ("+ids+")"},function(obj){
				var strReturn=obj.responseText;
				responseObject=Ext.util.JSON.decode(strReturn);
				var url=link6+"&docid="+rightGridSelect.json.id;
				new $FW({url:url,height:645,width:800,type:'T2',userIF:true,baseParam:{responseObject:responseObject},callback:function(retValue){
					rightGrid.getStore().reload();
					var whereSql = "obj.yhdgl.id ='" + rightGridSelect.json.id +"'";
					mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
					mxGrid.store.reload();
				}}).show();	//模式对话框
			//result=showModule(url,true,800,645);
        	});
		}
        
	}
}

/**
 * 删除表单
 */
function btnDel_Click(){
	var selections = mxGrid.getSelectionModel().getSelections();//获取被选中的行
	if(selections && selections.length > 0){
		var ids="";
		Ext.each(selections,function(selectedobj){
			if(selectedobj.id.length==32){
				ids+=selectedobj.id+",";//取得他们的id并组装
			}
		});
		if(ids!=""){
			AjaxRequest_Sync(link15,{ids:ids},function(obj){
					var strReturn=obj.responseText;
					var responseObject=Ext.util.JSON.decode(strReturn);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert("删除失败");
			 		}
			 		mxGrid.getStore().reload();				
	        	});
		}
	}else{
		alert("请选择要删除的验货单明细!");
		return ;
	}
}

/**
 * 清空子表Grid显示
 */
function clearMxGrid(){
	var whereSql = "obj.yhdgl.id ='" + 0 +"'";
	mxGrid.store.baseParams={queryParamsSql:whereSql};
	mxGrid.store.load();
}

/**
 * 查询
 */
function btnQuery_Click(){
	var result=showModule(link1,true,800,500);
}

/**
 * 待验收单生效
 */
function btnEnable_Click(){
	mxGrid.enableYhd();
}
	
//左边的树 右边的grid

var rightGrid=new RightGrid();
var mxGrid = new mxRightGrid();

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
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[mxGrid]
}

