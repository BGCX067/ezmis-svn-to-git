//主工具栏,在渲染的时候，初始化操作按钮
var mainToolbar=new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiDictCatalog' || button.id=='btnDelDictCatalog'
	|| button.id=='btnModiDict' || button.id=='btnDelDict') 
	button.setDisabled(true);
}

/**
 * 添加字典类型按钮
 */
function btnAddDictCatalog_Click(oButton,e){
	var oNode=dictCatalogTree.getSelectionModel().getSelectedNode();
	parentId = oNode!=null?oNode.id:"";
	if(parentId=='rootNode') parentId = '';
	var catalogEditForm=new CatalogEditForm("",parentId);
	catalogEditForm.show();
}

/**
 * 修改字典类型按钮
 */			
function btnModiDictCatalog_Click(){
	var oNode=dictCatalogTree.getSelectionModel().getSelectedNode();
	var catalogEditForm=new CatalogEditForm(oNode.id);
	
	catalogEditForm.show();
	catalogEditForm.loadData();
}

/**
 * 删除字典类型按钮
 */
function btnDelDictCatalog_Click(){
	dictCatalogTree.delDictCatalog();
	if(dictCatalogTree.getRootNode().childNodes==''){
		mainToolbar.items.get("btnModiDictCatalog").setDisabled(true);
	    mainToolbar.items.get("btnDelDictCatalog").setDisabled(true);
	}
}

/**
 * 添加字典
 */
function btnAddDict_Click(){
	var oNode=dictCatalogTree.getSelectionModel().getSelectedNode();
	if(oNode==null){
		return alert('请选择一个字典类型');
	}
	var dictEditForm = new DictEditForm(oNode.id,'');
	dictEditForm.show();
}

/**
 * 修改字典
 */
function btnModiDict_Click(){
	var oNode=dictCatalogTree.getSelectionModel().getSelectedNode();
	var selectId = gridPanel.getSelectionModel().getSelected().id;
	var dictEditForm = new DictEditForm(oNode.id,selectId);
	dictEditForm.show();
	dictEditForm.loadData();
}


/**
 * 删除字典
 */
function btnDelDict_Click(){
	gridPanel.delSelectedDict();
}

//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//用户列表
var gridPanel=new DictGrid();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'1 0 0 -3',
	items:[searchPanel,gridPanel]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
	items:[mainToolbar]
}
