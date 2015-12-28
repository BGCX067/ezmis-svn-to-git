
// 工具栏
// 1.添加分类
// 2.修改分类
// 3.删除分类
var mainToolbar = new Ext.Toolbar({
			height : 26,
			listeners : {
				render : function(tb) {
					operationsToolbarInitialize(tb);
				}
			}
		});
// 新物资处理
function btnXwzcl_Click() {
	var ops = [];
	var select = mxGrid.getSelectionModel().getSelected();
	if (select && select.json.isnew == '1') {
		var url = contextPath + "/jteap/wz/wzlb/xqjhsqSelectWzdaIndex.jsp";
		url += "?xqjhsqmxid=" + select.json.id + "&wzmc="
				+ encodeURIComponent(select.json.wzmc);
		var wzda = showIFModule(url, "新物资处理", "true", 1000, 600, {}, null,
				null, null, false, "no");
		if (wzda) {
			if (confirm('确定将新物资【' + select.json.wzmc + '('+select.json.xhgg+')】关联 【' + wzda.wzmc
					+ '('+wzda.xhgg+')】吗?')) {
				var param = {
					xqjhsqId : select.json.id,
					wzdaId : wzda.id
				};
				AjaxRequest_Sync(link7, param, function(ajax) {
							var responseText = ajax.responseText;
							var responseObject = Ext.util.JSON
									.decode(responseText);
							if (responseObject.success) {
								alert(select.json.wzmc + "关联成功!");
								mxGrid.getStore().reload();
							}
						});
			}
		}
	} else
		alert('选择一项新物资!');
}
// 回退至计划主管
function btnHtzjhzg_Click() {
	var mxGridSelect = mxGrid.getSelectionModel().selections;
	var rightGridSelect = rightGrid.getSelectionModel().getSelected();
	var mxGridArray = new Array();
	for(var i = 0; i < mxGridSelect.length; i++){
		mxGridArray.push(mxGridSelect.items[i].id);
	}
	var mxGridRet = mxGridArray.toString();
	
	if (rightGridSelect && mxGridSelect.length>0) {
		if(rightGridSelect.data.IS_BACK == "1"){
			if (window.confirm("确定要将这些单据回退给计划主管吗？")) {
				var param = {
					xqjhsqid : rightGridSelect.json.ID,          //需求计划申请主单id
					id : mxGridRet                                     //需求计划申请明细id   
				};
				AjaxRequest_Sync(link5, param, function(ajax) {
							var responseText = ajax.responseText;
							var responseObject = Ext.util.JSON.decode(responseText);
							if (responseObject.success) {
								alert("回退成功!");
								//全部回退  
								if(mxGrid.getSelectionModel().selections.length == mxGrid.getStore().totalLength){
									window.location.href = window.location.href ;
	//								rightGrid.getStore().reload();
	//								rightGrid.getStore().on('load', function() {
	//									var url = link2;
	//									mxGrid.changeToListDS(url);
	//									mxGrid.store.load();
	//								})
								}else{                     //部分回退
									mxGrid.getStore().reload();
	//								rightGrid.getStore().on('load', function() {
	//									var url = link2;
	//									mxGrid.changeToListDS(url);
	//									mxGrid.store.load();
	//								})
								}
							}
						});
			}
		}else{
			alert("此需求计划申请不允许回退!");
			return ;
		}
	} else{
		alert("请选择一条单据!");
	}
}
/**
 * 生效处理
 */
function btnSx_Click() {
	var select = mxGrid.getSelectionModel().selections;
	if(mxGrid.getSelectionModel().selections.length>0){
		for(var i = 0; i < select.length; i++){
			if(select.items[i].data.isnew == '1'&&select.items[i].data.isCancel == '1'){
				alert("还有新物资没有处理!");
				return;
			}
		}
		var ops = [];
		var ds = mxGrid.getSelectionModel().selections;
		ds.each(function(r) {
					ops.push(r.data);
				});
		var param = {};
		param.ops = Ext.encode(ops);
		AjaxRequest_Sync(link4, param, function(ajax) {
					var responseText = ajax.responseText;
					var responseObject = Ext.util.JSON.decode(responseText);
					if (responseObject.success) {
						alert("物资生效成功!");
						//rightGrid.getStore().reload();
						window.location.href = window.location.href;
						rightGrid.getStore().on('load', function() {
									var url = link2;
									mxGrid.changeToListDS(url);
									mxGrid.store.load();
								})
					}
				});
	}else{
		alert("请选择要生效的物资!");
		return;			
	}
}
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button) {
	if (button.id == 'btnFp')
		button.setDisabled(true);
}
// 物资grid
var rightGrid = new RightGrid();
// 物资明细grid
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

// 南边布局
var lySouth = {
	layout : 'border',
	id : 'south-panel',
	region : 'south',
	minSize : 175,
	maxSize : 400,
	height : 300,
	border : false,
	margins : '0 0 0 0',
	items : [mxGrid]
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
 * 点击导出按钮 path是Ajax请求的路径 cm是所要导出的GridPanel的ColumnModel,用来动态的获取header
 * flag标志GridPanel中是否有checkbox
 */
// exportExcel = function(path,cm,flag) {
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
	path = contextPath + "/jteap/wz/xqjhsq/XqjhsqDetailAction!exportXqjhsqSxDetailAction.do?"+temp3;
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
	var dataStore = mxGrid.getStore().data.items;
	//var modifyData = dataStore.modified.slice(0);
	var modifyDataArray = [];
	Ext.each(dataStore, function(item) {
		modifyDataArray.push(item.data);
	});
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
		url:link13,
		success:function(ajax){
	 		var responseText=ajax.responseText;	
	 		var responseObject=Ext.util.JSON.decode(responseText);
	 		if(responseObject.success){
	 			exportExcel(mxGrid,true);
	 			mxGrid.getStore().reload();
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

//Excel导出
function exportSelectedExcel(grid){
	var select = grid.getSelectionModel().selections;
	var allSelect = grid.getStore().data.items;
	//所有选中的需求计划申请id
	var idsArr = new Array();
	//选择性导出Excel
	if(select.length > 0){
		for(var i =0; i < select.length; i++){
			idsArr.push(select.items[i].id);
		}
		//var path = contextPath + "/jteap/wz/cgjhmx/CgjhmxAction!exportSelectedExcelAction.do?idsArr="+idsArr;
		//window.open(path);
		var path = contextPath + "/jteap/wz/xqjhsq/XqjhsqDetailAction!exportSelectedExcelAction.do?";
		//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
		var url = path;
		var f = document.createElement("form");
		f.name = "aaa";
		f.target="newWindow"; 
		f.method="post";
		document.body.appendChild(f);
		var i = document.createElement("input");
		i.type = "hidden";
		f.appendChild(i);
		i.value = idsArr;
		i.name = "idsArr";
		f.action = url;
		f.onsubmit=function(){
			window.open("about:blank","newWindow","");
		}
		f.submit();
		
		
	}else{ //表示没有选择，将全部导出
		for(var i =0; i < allSelect.length; i++){
			idsArr.push(allSelect[i].data.id);
		}
		//var path = contextPath + "/jteap/wz/cgjhmx/CgjhmxAction!exportSelectedExcelAction.do?idsArr="+idsArr;
		//window.open(path);
		var path = contextPath + "/jteap/wz/xqjhsq/XqjhsqDetailAction!exportSelectedExcelAction.do?";
		//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
		var url = path;
		var f = document.createElement("form");
		f.name = "aaa";
		f.target="newWindow"; 
		f.method="post";
		document.body.appendChild(f);
		var i = document.createElement("input");
		i.type = "hidden";
		f.appendChild(i);
		i.value = idsArr;
		i.name = "idsArr";
		f.action = url;
		f.onsubmit=function(){
			window.open("about:blank","newWindow","");
		}
		f.submit();
	}
}
