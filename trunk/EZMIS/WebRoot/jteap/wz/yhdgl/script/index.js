

//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});
var i = 0;
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
//过期方法
function ajaxSave(result){
	i++;
	Ext.Ajax.request({
           		url:contextPath+"/jteap/wz/cgjhgl/CgjhglAction!createCgdAndMxAction.do",
           		params: {BH:result},
           		method:'post',
           		success:function(ajax){
           			var responseObj = Ext.util.JSON.decode(ajax.responseText);
           			if(responseObj.success == true){ 
           				alert("自由入库成功");
          				}else{
          				 if(i<=3){
          				 	ajaxSave(result);
          				 }else{
							alert("自由入库失败"); 
          				 }
          				}
          			rightGrid.getStore().reload();
           		} 
       		});

}
/**
 * 修改表单
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelected();
	 
	
	if(select){
		var yhdmxs = select.json.yhdmxs;
		var ids = "";
		if(yhdmxs.length > 0){
			for (var i = 0; i < yhdmxs.length; i++) {
				if(yhdmxs[i]!=null){
					if(ids.indexOf(yhdmxs[i].cgjhmx.id)<0)
						ids = ids + "'"+yhdmxs[i].cgjhmx.id + "',";
				}else{
					if(ids.indexOf(select.json.cgjhmx.id)<0)
						ids = ids + "'"+select.json.cgjhmx.id + "',";
				}
			}
			ids = "obj.id in ("+ids.substring(0,ids.length-1)+")";;
		}else{
			ids = "";
		}
		var url = contextPath+"/jteap/wz/cgjhmx/CgjhmxAction!showListAction.do?";
		
		responseObject = null;
		 
		if(responseObject != null){
			var url=link8+"&docid="+select.json.id;
			new $FW({url:url,height:645,width:800,type:'T2',userIF:true,baseParam:{responseObject:responseObject},callback:function(retValue){
				rightGrid.getStore().reload();
				var whereSql = "obj.yhdgl.id ='" + select.json.id +"'";
				mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
				mxGrid.store.reload();
				
			}}).show();	//模式对话框
		}else{
			AjaxRequest_Sync(url,{queryParamsSql:ids},function(obj){
				var strReturn=obj.responseText;
				responseObject=Ext.util.JSON.decode(strReturn);
				var url=link8+"&docid="+select.json.id;
				new $FW({url:url,height:645,width:800,type:'T2',userIF:true,baseParam:{responseObject:responseObject},callback:function(retValue){
					rightGrid.getStore().reload();
					var whereSql = "obj.yhdgl.id ='" + select.json.id +"'";
					mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
					mxGrid.store.reload();
				}}).show();	//模式对话框
			//result=showModule(url,true,800,645);
        	});
		}
        
	}
	
	
	
	/*
	if(select){
		var url=link8+"&docid="+select.json.id;
		result=showModule(url,true,650,500);
		rightGrid.getStore().reload();
		
		var whereSql = "obj.yhdgl.id ='" + select.json.id +"'";
		mxGrid.store.baseParams={PAGE_FLAG:'PAGE_FLAG_NO',queryParamsSql:whereSql};
		mxGrid.store.reload();
	}
	*/
}

/**
 * 删除表单
 */
function btnDel_Click(){
	var mxselect=mxGrid.getSelectionModel().getSelections();
	var select = rightGrid.getSelectionModel().getSelections();
	//如果是自由入库 则先行删除 虚拟出来的采购单及明细

	if(mxselect.length==mxGrid.store.data.length){
		var result=rightGrid.deleteSelect(select);
		if(result!='cancle') clearMxGrid();
	}else{
		var result=mxGrid.deleteSelect(mxselect);
		if(result!='cancle') clearMxGrid();
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

