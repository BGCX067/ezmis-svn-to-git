//主工具栏
var mainToolbar=new Ext.Toolbar({height:26,
									items:[
										{id:'btnAddPerson',text:'添加模块',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:btnAddModule_Click}},
										{id:'btnModiModule',text:'修改模块',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_5.gif',listeners:{click:btnModiModule_Click},tooltip: {title:'修改模块',text:'选中需要修改的模块，再执行该操作'}},
										{id:'btnDelModule',text:'删除模块',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelModule_Click},tooltip: {title:'删除模块',text:'选中需要删除的模块，再执行该操作'}}
									]}
								);
								
var operationGrid = new OperationGrid();

/**
 * 初始化工具栏按钮状态

function initToolbarButtonStatus(button){
	if(button.id=='btnModifyUser' || button.id=='btnDelUser' 
		|| button.id=='btnRemoveUser' || button.id=='btnInitPwd' 
		|| button.id=='btnLockUser' || button.id=='btnUnlockUser') 
		button.setDisabled(true);
}
 */

/**
 * 修改模块按钮
 */			
function btnModiModule_Click(){
	var oNode=moduleTree.getSelectionModel().getSelectedNode();
	if(oNode.isRootNode()){
		alert('无效模块，无法修改');
		return;
	}
	var moduleEditForm=new ModuleEditFormWindow(oNode.id);
	moduleEditForm.show();
	moduleEditForm.loadData();
}
/**
 * 删除组织按钮
 */
function btnDelModule_Click(){
	moduleTree.delModule();
}
/**
 * 添加模块
 */
function btnAddModule_Click(oButton,e){
	
	var createModuleForm=new ModuleEditFormWindow();
	createModuleForm.show();
}



//模块信息
var moduleDetailForm=new ModuleDetailForm();

/*
searchPanel.on("render",function(xx){
xx.render();
})
*/

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'2 0 0 -3',
	items:[moduleDetailForm],
	check:function(oNode){ 
        moduleDetailForm.showDetailPanel(oNode);
    	operationGrid.changeToListDS(oNode);
		operationGrid.getStore().reload(); 
    }
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
	items:[mainToolbar]
}