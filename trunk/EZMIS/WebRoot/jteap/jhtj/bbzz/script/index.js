
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
//	items:[
//		{disabled:true,id:'btnAdd',text:'新建',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnModify',text:'修改',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnDel',text:'删除',cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:mainToolBarButtonClick}}
//		]
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnAddCatalog'||
	   button.id=='btnModiCatalog'||
	   button.id=='btnDelCatalog'||
	   button.id=='btnFindCatalog'||
	   button.id=='btnStatusCatalog'){ 
		button.setDisabled(true);
	}
}
/**
 * 添加EForm表单
 */
function btnAddCatalog_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var param={};
	param.bbindexid=oNode.id;
	AjaxRequest_Sync(link9, param, function(req) {
		var responseText = req.responseText;
		var responseObj = Ext.decode(responseText);
		if (responseObj.success) {
			var data=responseObj.data;
			//初始化接口页面,供用户选择
			var result=showModule(link11+"?initValue="+data,true,300,300);
			if(result!=undefined){
				param.sqlValue=result;
				//日期是否属于发布状态
				AjaxRequest_Sync(link17, param, function(isstateRes){
					var isstateText = isstateRes.responseText;
					var isstateObj = Ext.decode(isstateText);
					if (isstateObj.res) {
						AjaxRequest_Sync(link14, param, function(isExist) {
							var isExistText = isExist.responseText;
							var isExistObj = Ext.decode(isExistText);
							if (isExistObj.success) {
								var exist=isExistObj.res;
								if(!exist){
									if(confirm("当前制作的报表已经存在,确定要重新制作吗?")){
										toBbzzPage(param,oNode.id);
									}
								}else{
									toBbzzPage(param,oNode.id);
								}
							}
						});
					}else{
						alert("当前日期下的报表已经属于发布状态,不能制作！");
					}
				});
			}
		}else{
			alert("该报表不存在!");
		}
	});
}

/**
 * 开始制作
 */
function toBbzzPage(param,bbindexid){
	result=showModule2(link12,"yes",700,600,param);
	if(result!=undefined){
		alert("制作成功!");
		var url=link4+"?bbindexid="+bbindexid;
    	rightGrid.changeToListDS(url);
    	rightGrid.getStore().reload();	
	}
}


/**
 * 修改表单
 */
function btnModiCatalog_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link12+"?id="+select.json.id+"&isUpdate=true";
		result=showModule(url,true,700,550);
		if(result=="true"){
			alert("修改成功!");
			rightGrid.getStore().reload();
		}
	}
	
}

/**
 * 删除表单
 */
function btnDelCatalog_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}


/**
 * 报表查看
 */
function btnFindCatalog_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link12+"?id="+select.json.id+"&isUpdate=false";
		result=showModule(url,true,700,550);
		if(result=="true"){
			alert("修改成功!");
			rightGrid.getStore().reload();
		}
	}
}

/**
 * 状态设定
 */
function btnStatusCatalog_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link15+"?id="+select.json.id;
		result=showModule(url,true,300,150);
		if(result=="true"){
			alert("设定成功!");
			rightGrid.getStore().reload();
		}
	}
}

	
//左边的树 右边的grid

var leftTree=new LeftTree();
var rightGrid=new RightGrid();

//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,rightGrid]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


