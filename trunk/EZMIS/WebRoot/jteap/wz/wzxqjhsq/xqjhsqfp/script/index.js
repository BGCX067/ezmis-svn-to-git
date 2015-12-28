
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});
/**
 * 需求计划申请分配
 */
function btnFp_Click(){
	var ops=[];
	var ds=mxGrid.getStore();
	var isReturn = false;
	if(rightGrid.getSelectionModel().selections.items[0].data.IS_BACK == ""){
		alert("请选择是否允许回退到主管!");
		return;
	}else if(mxGrid.getSelectionModel().selections){
		ds.each(function(r){
			if(!r.data.jhy){
				alert("【"+r.data.wzmc+"】 没有分配归属，请分配完物资!");
				isReturn = true;
			//	return;  这里return不起作用
			}
			ops.push(r.data);
		});
	}else{
		alert("请选择需要分配的全部物资!");
		reutrn ;
	}
	if(isReturn)
		return ;
	var param={};
	var isBack = rightGrid.getSelectionModel().selections.items[0].data.IS_BACK;
	param.ops=Ext.encode(ops);
	param.isBack = isBack;
	AjaxRequest_Sync(link4,param,function(ajax){
		var responseText=ajax.responseText;	
 		var responseObject=Ext.util.JSON.decode(responseText);
 		if(responseObject.success){
 			//initPage("");
 			//searchPanel.searchClick();
 			alert("分配成功!");
 			window.location.href = window.location.href ;
// 			rightGrid.getStore().reload();
// 			mxGridn.getStore().removeAll();
 		}
	});
//	}g
}
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	//if(button.id=='btnFp') 
	//	button.setDisabled(true);
}
//物资grid
var rightGrid = new RightGrid();
//物资明细grid
var mxGrid = new MxRightGrid();

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [rightGrid]
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

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}

/**
 * 点击导出按钮 obj是要导出表的GridPanel对象（有一约定：obj的exportLink属性）
 * flag标志GridPanel中是否有checkbox
 */
exportExcel = function(obj, flag) {
	var path = obj.store.proxy.url;
	var paths = path.split("?")
	var temp1 = paths[0]
	var temp3 = paths[1]
	temp1 = temp1.split("!")[0]
	path = contextPath + "/jteap/wz/xqjhsq/XqjhsqDetailAction!exportExcelForXqjhsqmx.do?"+temp3;
	var cm = obj.getColumnModel();
	var sum = cm.getColumnCount();
	var j = 1;
	if (!flag) {
		j = 0
	}
	var paraHeader = "";
	var paraDataIndex = "";
	var paraWidth = ""
	for (var i = j;i < sum; i++) {
		if(!cm.isHidden(i)){
			paraHeader += cm.getColumnHeader(i) + ","
			paraDataIndex += cm.getDataIndex(i) + ","
			paraWidth += cm.getColumnWidth(i) + ","
		}
	}
	paraHeader = paraHeader.substr(0, paraHeader.length - 1)
	paraDataIndex = paraDataIndex.substr(0, paraDataIndex.length - 1)
	paraWidth = paraWidth.substr(0, paraWidth.length - 1)
	path = path + "&paraHeader=" + encodeURIComponent(paraHeader)
			+ "&paraDataIndex=" + encodeURIComponent(paraDataIndex)
			+ "&paraWidth=" + encodeURIComponent(paraWidth);
	// alert(path);
	window.open(path)
}

//更新数据到数据库
function updateDate() {
	var dataStore = mxGrid.getStore();
	var modifyData = dataStore.modified.slice(0);
	var modifyDataArray = [];
	Ext.each(modifyData, function(item) {
		modifyDataArray.push(item.data);
	});
	//var modifyCount = modifyDataArray.length;
	for(var i =0; i < modifyDataArray.length; i++){
		var xysj = modifyDataArray[i].xysj;
		if(xysj == "" || xysj == null){
			xysj = null;
			modifyDataArray[i].xysj = null;
		}else{
			if(typeof xysj == 'object' && xysj.time){
				xysj = xysj.time;
				modifyDataArray[i].xysj = formatDate(new Date(xysj), "yyyy-MM-dd");
			}
		}
	}
	var gridData = Ext.encode(modifyDataArray);
	Ext.Ajax.request({
		url:link11,
		success:function(ajax){
	 		var responseText=ajax.responseText;	
	 		var responseObject=Ext.util.JSON.decode(responseText);
	 		if(responseObject.success){
	 			exportExcel(mxGrid,true);
	 			//mxGrid.getStore().reload();
	 			window.location.href = window.location.href;
	 		}else{
	 			alert(responseObject.msg);
	 		}				
		},
	 	failure:function(){
	 		alert("提交失败");
	 	},
	 	method:'POST',
	 	params: {gridData:gridData}//Ext.util.JSON.encode(selections.keys)			
	});
}
