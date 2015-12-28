//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
	/*items:[
		{disabled:false,id:'btnAddIp',text:'添加IP规则',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnAddIp_Click}},
		{disabled:true,id:'btnModiIpLock',text:'修改规则',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnModiIpLock_Click}},
		{disabled:true,id:'btnDelIpLock',text:'删除规则',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnDelIpLock_Click}}
		]*/
});


var formSn = "TB_WZ_SKWGL";
//添加库位信息
function btnAddModule_Click(){
	var ckid = leftTree.getSelectionModel().getSelectedNode().id;
	var kwid = '';
	if(kwTreeGrid.getSelectionModel().getSelected())
		kwid = kwTreeGrid.getSelectionModel().getSelected().json.id;
	var kwlj = kwTreeGrid.root_title+"/";
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&ckid="+ckid+"&sjid="+kwid;
	showIFModule(url,"自定义表单","true",400,200,{},null,null,null,false,"no");
	kwTreeGrid.getStore().reload();
}
//修改库位信息
function btnModiModule_Click(){
	var select=kwTreeGrid.getSelectionModel().getSelected();
	if(select){
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id;
		showIFModule(url,"自定义表单","true",400,200,{},null,null,null,false,"no");
		kwTreeGrid.getStore().reload();
	}else{
		alert("请选择一条记录");
		return ;
	}
}
//删除库位信息
function btnDelModule_Click(){
	var url = contextPath + "/jteap/wz/kwwh/KwwhAction!delKWAction.do";
	var select = kwTreeGrid.getSelectionModel().getSelected();//获取被选中的行
	if(select){
		var id = select.json.id;	
		if(window.confirm("确认删除选中的表单数据吗？")){
			Ext.Ajax.request({
				url:url,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			//kwTreeGrid.store.proxy.conn.url = link19+"?parentId="+select.parentNode.id;
			 			kwTreeGrid.getStore().reload();
			 			kwTreeGrid.getStore().remove(select);
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {"id":id}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}else{
		alert("请选择一条记录");
		return ;
	}
}
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel') 
		button.setDisabled(true);
}

//左边的树
var leftTree=new LeftTree();
	
//左边的树 右边的grid
var kwTreeGrid = new kwTreeGrid();
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[kwTreeGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}

