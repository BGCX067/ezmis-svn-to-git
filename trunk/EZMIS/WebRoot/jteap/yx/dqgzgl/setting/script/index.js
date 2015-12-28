
//工具栏
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' || button.id == 'btnHanldSend'
	   || button.id=='btnModify' || button.id=='btnDel')
		button.setDisabled(true);
}

/**
 * 添加定期工作
 */
function btnAdd_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var obj = {};
	obj.grid = rightGrid;
	obj.catalogId = oNode.id;
			
	var url = contextPath + '/jteap/yx/dqgzgl/setting/setting.jsp';
	showIFModule(url,"添加定期工作设置","true",600,350,obj);
}

/**
 * 修改定期工作
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var obj = {};
		obj.grid = rightGrid;
		//定期工作Id
		obj.id = select.id;
		obj.catalogId = select.data.dqgzCatalogId;
				
		var url = contextPath + '/jteap/yx/dqgzgl/setting/setting.jsp' + "?modi=m";
		showIFModule(url,"修改定期工作设置","true",600,350,obj);
	}
	
}

/**
 * 删除定期工作
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}

/**
 * 手工发送
 */
function btnHanldSend_Click(){
	var selections=rightGrid.getSelectionModel().getSelections();
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	Ext.Ajax.request({
		url: link10,
		method: 'post',
		params: {ids:ids},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('发送成功');
			}else{
				alert('发送失败');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	leftTree.createNode();
}

/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyNode();
}

var leftTree=new LeftTree();
//左边的树 右边的grid

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="负责部门#fzbm#comboBox,负责岗位#fzgw#comboBox,项目#dqgzNr#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="负责部门#fzbm#comboBox,负责岗位#fzgw#comboBox,项目#dqgzNr#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

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
