//工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnQuerySq' || button.id=='btnSlfp' || button.id=='btnWzhz'
	   || button.id=='btnSccg'|| button.id =='btnRb')
		button.setDisabled(true);
}

/**
 *  查看申请
 */
function btnQuerySq_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
			var sqjhsqid  = select.json.XQJHSQID;
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			var url = link9;
						var windowUrl = link10 + "?pid=" + responseObject.processinstance + "&status=false";
						var args = "url|" + windowUrl + ";title|" + '查看流程';
						var retValue = showModule(url, "yes", 700, 600, args);
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {sqjhsqid:sqjhsqid}// Ext.util.JSON.encode(selections.keys)
			});
		}else{
			alert("至少选择一条记录!");
			return;
		}
}

/**
 * 数量分配
 */
function btnSlfp_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var obj = {};
	if(select){
		/********************/
		var sqjhid  = select.json.ID;
		Ext.Ajax.request({
			url:link2,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			obj.xqjhid = responseObject.xqjhid;
					obj.xqjhbh = responseObject.xqjhbh;
					obj.xqjhsqbh = responseObject.xqjhsqbh;
					obj.sxsj = responseObject.sxsj;
					obj.status = responseObject.status;
					obj.operator = responseObject.operator;
					obj.gclb = responseObject.gclb;
					obj.gcxm = responseObject.gcxm;
					obj.sqbm = responseObject.sqbm;
					obj.sqsj = responseObject.sqsj;
					obj.personName = responseObject.personName;
					var url = contextPath + '/jteap/wz/wzxqjh/slfpinfo.jsp';
					showIFModule(url,"需求分配情况","true",800,400,obj);
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {sqjhid:sqjhid}// Ext.util.JSON.encode(selections.keys)
		});
		/********************/
	}
}

/**
 * 物资汇总
 */
function btnWzhz_Click(){
	var rightGridSelect = rightGrid.getSelectionModel().selections;
	if(rightGridSelect){
		var rightGridArray = new Array();
		var obj = {};
		var total = 0.0;    //合计金额
		var totalSL = 0.0;   //合计数量
		for(var i = 0; i < rightGridSelect.length; i++){
			rightGridArray.push(rightGridSelect.items[i].data.XQJHMXID);
			rightGridArray.push(rightGridSelect.items[i].data.XQJHBH);
			rightGridArray.push(encodeURIComponent(rightGridSelect.items[i].data.WZMC)+"("+rightGridSelect.items[i].data.XHGG+")");
			rightGridArray.push(encodeURIComponent(rightGridSelect.items[i].data.JLDW));
			rightGridArray.push(rightGridSelect.items[i].data.PZSL);
			rightGridArray.push(parseFloat(rightGridSelect.items[i].data.PZSL)*parseFloat(rightGridSelect.items[i].data.JHDJ));
			total += parseFloat(rightGridSelect.items[i].data.PZSL)*parseFloat(rightGridSelect.items[i].data.JHDJ);
			totalSL += parseFloat(rightGridSelect.items[i].data.PZSL);
		}
		var url = contextPath + '/jteap/wz/wzxqjh/wzhzinfo.jsp?selectedObj='+rightGridArray+'&total='+total+'&totalSL='+totalSL;
		showIFModule(url,"需求计划物资汇总","true",800,400,obj);
	}else{
		alert("请至少选择一条记录!");
		return;
	}
}

/**
 * 生成采购
 */
function btnSccg_Click(){
	var rightGridSelect = rightGrid.getSelectionModel().selections;
	//需求计划id
	var rightGridArray = new Array();
	//物资id array
	var wzbmArray = new Array();
	//序号 array
	var xhArray = new Array();
	for(var i =0; i < rightGridSelect.length; i++){
		rightGridArray.push(rightGridSelect.items[i].data.ID);
		wzbmArray.push(rightGridSelect.items[i].data.WZBM);
		xhArray.push(rightGridSelect.items[i].data.XH);
	}
	var selectRet = rightGridArray.toString();
	var wzbmRet = wzbmArray.toString();
	var xhRet = xhArray.toString();
	var obj = {};
	var retObjArray = new Array();
	var url = link6+"?sqjhid="+selectRet+"&wzbm="+wzbmRet+"&xh="+xhRet+"&type=all&isChecked=false";
	var returnValue = showIFModule(url,"需求计划生成采购计划","true",800,400,obj);
		if(returnValue){
			rightGrid.getStore().reload();
			return ;
		}
	
}
//回退
function btnRb_Click(){
	var rightGridSelect = rightGrid.getSelectionModel().selections;
	var rightGridArray = new Array();
	var wzbmArray = new Array();
	var xqjhmxId = new Array();
	for(var i =0; i < rightGridSelect.length; i++){
		rightGridArray.push(rightGridSelect.items[i].data.XQJHSQBH);
		wzbmArray.push(rightGridSelect.items[i].data.WZBM);
		xqjhmxId.push("'"+rightGridSelect.items[i].data.XQJHMXID+"'");
	}
	var xqjhmxids = xqjhmxId.toString();
	var wzbms = wzbmArray.toString();
	var xqjhsqbhs = rightGridArray.toString();
	
	Ext.Ajax.request({
			url:link11,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("回退成功");
		 		}else{
		 			alert(responseObject.msg);
		 		}	
		 		rightGrid.getStore().reload();	
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {xqjhmxids:xqjhmxids,wzbms:wzbms,xqjhsqbhs:xqjhsqbhs}// Ext.util.JSON.encode(selections.keys)
		});
}
var rightGrid=new RightGrid();

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
	var gridStore = obj.store.data.items;
	var xqjhMxIdArray = new Array();
	for(var i =0; i < gridStore.length; i++){
		xqjhMxIdArray.push(gridStore[i].data.XQJHMXID);
	}
	var path = contextPath + "/jteap/wz/xqjh/XqjhDetailAction!exportXqjhDetailAction.do?xqjhMxIdArray="+xqjhMxIdArray;
	window.open(path);
}
