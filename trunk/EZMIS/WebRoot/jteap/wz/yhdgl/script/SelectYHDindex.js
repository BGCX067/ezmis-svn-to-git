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
	var select=rightGrid.getSelectionModel().getSelections();
    var result=rightGrid.deleteSelect(select);
    if(result!='cancle') clearMxGrid();
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

//南边布局
var slectSouth={
	layout:'border',
	id:'south-panel',
	region:'south',
	minSize: 175,
	maxSize: 400,
	height:300,
	border:false,
	margins:'0 0 0 0',
	items:[mxGrid],
	buttons:[{text:'确定',handler:function(){
		var select  =rightGrid.getSelectionModel().getSelected();
		if(select){
			var mxstore = mxGrid.getStore();
			var newStore = new Ext.data.Store();

			//newStore.add(mxstore.getAt(0));
			//var n = mxstore.find('wzlysqDetail.id','40288a542d0bc112012d0bcdc9c00003');
			for(i=0;i<mxstore.getCount();i++){
				var res = newStore.getRange();
				var res1 = mxstore.getRange(i,i);
				var index = newStore.find('yhdmxs.id',mxstore.getAt(i).get('yhdmxs.id'));
				if(index>-1){
					var record = newStore.getAt(index);
					var sxsl = record.get('sxsl');
					record.set('sxsl',sxsl+mxstore.getAt(i).get('sxsl'));
				}else{
					newStore.add(res1);
				}
			}
			window.returnValue = newStore.data;
		}
		window.close();
	}}]
}

