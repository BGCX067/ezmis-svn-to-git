var mainToolbar = new Ext.Toolbar({height:26,
	items:[]
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' 
		|| button.id=='btnAddEFormCForm' || button.id=='btnAddExcelCForm'
		|| button.id=='btnModifyCForm' || button.id=='btnDelCForm') 
		button.setDisabled(true);
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
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link6+"?id="+select.json.id;
		result=showModule(url,true,800,645);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
	
}

/**
 * 删除表单
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
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
		var path = contextPath + "/jteap/wz/yhdmx/YhdmxsAction!exportSelectedExcelAction.do?";
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
		var path = contextPath + "/jteap/wz/yhdmx/YhdmxsAction!exportSelectedExcelAction.do?";
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



	
//左边的树 右边的grid


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="物资名称#wzdagl.wzmc#wzdagl,型号规格#wzdagl.xhgg#textField,保管员#yhdgl.bgy#comboBgy,仓库名称#wzdagl.kw.ckid#comboCkgl,验收单编号#yhdgl.bh#textField,状态#zt#comboZt,到货时间起#dhStart#dateField,到货时间止#dhEnd#dateField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="物资名称#wzdagl.wzmc#wzdagl,型号规格#wzdagl.xhgg#textField,保管员#yhdgl.bgy#comboBgy,仓库名称#wzdagl.kw.ckid#comboCkgl,验收单编号#yhdgl.bh#textField,状态#zt#comboZt,到货时间起#dhStart#dateField,到货时间止#dhEnd#dateField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:80});

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


