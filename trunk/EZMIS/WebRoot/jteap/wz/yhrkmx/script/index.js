var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

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
//导出Excel
function btnExce_Click(){
	var path = contextPath +"/jteap/wz/yhdmx/YhdmxsAction!exportExcel.do?parWhere="+parWhere+"&role=true";
	window.open(path);
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
//指定库位
function btnZDKW_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.json.wzid+",";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个物资档案管理');
		return;
	}
	var url = contextPath + "/jteap/wz/kwwh/kwwhTree.jsp";
	var str = showIFModule(url,"库位","true",800,600,{},null,null,null,false,"yes");
		if(str==null){
//			alert("请选择一项物资的类别");
			return;
		}
		if(window.confirm("确定要将选中的物资档案指定到库位["+str.split('|')[0]+"]吗？")){
			//var wzlbbm = leftTree.getSelectionModel().getSelectedNode().id;
			
			Ext.Ajax.request({
				url:link15,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("修改成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}
				},
			 	failure:function(){
			 		alert("修改失败");
			 	},
			 	method:'POST',
			 	params: {"ids":ids,"kw":str.split('|')[1]}//Ext.util.JSON.encode(selections.keys)
			});
		}
}

//修改单价
function btnXgdj_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+="'"+selectedobj.json.id+"',";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个入库单明细');
		return;
	}
	var dj = window.prompt("请输入改变后的单价：","");
	if(dj.search("^-?\\d+(\\.\\d+)?$")!=0){
        alert("请输入一个数字!");
        return;
    } 
		
		if(window.confirm("确定要将选中的物资单价修改为"+dj+"吗？")){
			//var wzlbbm = leftTree.getSelectionModel().getSelectedNode().id;
			Ext.Ajax.request({
				url:link16,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("修改成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}
				},
			 	failure:function(){
			 		alert("修改失败");
			 	},
			 	method:'POST',
			 	params: {"ids":ids,"dj":dj}//Ext.util.JSON.encode(selections.keys)
			});
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

exportExcels = function(obj, flag,parWhere,role) {
	var path = "";
	//var path = obj.store.proxy.url;
	path = contextPath +"/jteap/wz/yhdmx/YhdmxsAction!exportExcel.do?parWhere="+parWhere;
	if(role != null){
		path = path +"&role=true";
	}
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
	//alert(path);
	window.open(path);
}


	
//左边的树 右边的grid


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"///验收单编号#yhdgl.bh#textField,
var searchAllFs="验收单编号#yhdgl.bh#textField,物资名称#wzdagl.wzmc#wzdagl,型号规格#wzdagl.xhgg#textField,申请部门#sqbmmc#depts,仓库名称#ckgl#comboCkgl,计划员#jhy#comboJhy,保管员#yhdgl.bgy#comboBgy,验收日期从#dhStart#dateField,至#dhEnd#dateField,供应单位#yhdgl.ghdw#gysgl,状态#zt#comboZt,发票编号#fpbh#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="验收单编号#yhdgl.bh#textField,物资名称#wzdagl.wzmc#wzdagl,型号规格#wzdagl.xhgg#textField,申请部门#sqbmmc#depts,仓库名称#ckgl#comboCkgl,计划员#jhy#comboJhy,保管员#yhdgl.bgy#comboBgy,验收日期从#dhStart#dateField,至#dhEnd#dateField,供应单位#yhdgl.ghdw#gysgl,状态#zt#comboZt,发票编号#fpbh#textField".split(",");
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


