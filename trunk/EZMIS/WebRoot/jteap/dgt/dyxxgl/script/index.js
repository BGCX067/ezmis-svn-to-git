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


//新建党组织
function btnAddDzz_Click(){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	showIFModule(url,"党员信息管理","true",550,310,{},null,null,null,false,"auto");
	leftTree.getRootNode().reload();
}

//修改党组织
function  btnModDzz_Click(){
	var select=leftTree.getSelectionModel().getSelectedNode().id;
	if(select){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select;
		showIFModule(url,"党员信息管理","true",550,310,{},null,null,null,false,"auto");
		rightGrid.getStore().reload();
		leftTree.getRootNode().reload();
	}
}

//新增党员信息
function btnAddDy_Click(){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_DGT_DYXXK";
	showIFModule(url,"党员信息管理","true",576,460,{},null,null,null,false,"auto");
	rightGrid.getStore().reload();
}

//修改党员
function btnModDy_Click(){
	var select=rightGrid.getSelectionModel().getSelected();
	var formSn="TB_DGT_DYXXK";
	if(select){
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id;
		showIFModule(url,"党员信息管理","true",576,460,{},null,null,null,false,"auto");
		rightGrid.getStore().reload();
	}	
}

//删除党组织
function btnDelDzz_Click(){
	var url = contextPath + "/jteap/form/eform/EFormAction!delEFormRecAction.do?formSn="+formSn;

	Ext.Ajax.request({
		url:link5+"?ids="+leftTree.getSelectionModel().getSelectedNode().id,
		success:function(ajax){
	 		var responseText=ajax.responseText;	
	 		var responseObject=Ext.util.JSON.decode(responseText);
	 		if(responseObject.success){
	 			if(window.confirm("确认删除选中的党组织吗？")){
					Ext.Ajax.request({
			 				url:url,
			 				method:'POST',
					 		params: {ids:leftTree.getSelectionModel().getSelectedNode().id,formSn:formSn},
			 				success:function(ajax2){
								alert("删除成功");
								leftTree.getRootNode().reload();
			 				},
			 				failure:function(){
			 					alert(responseObject.msg);
			 				}
		 				});
	 				}
	 		}else{
	 			alert(responseObject.msg);
	 		}				
		},
	 	failure:function(){
	 		alert(responseObject.msg);
	 	}
	});
	
}


//删除党员
function btnDelDy_Click(){
	var url = contextPath + "/jteap/form/eform/EFormAction!delEFormRecAction.do?formSn=TB_DGT_DYXXK";
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	
	if(window.confirm("确认删除选中的表单数据吗？")){
		Ext.Ajax.request({
			url:url,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("删除成功");
		 			rightGrid.getStore().reload();
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
		});
	}		
}



/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if( button.id=='btnModDzz'|| button.id=='btnDelDzz'
	|| button.id=='btnModDy'||button.id=='btnDelDy'	
	) 
		button.setDisabled(true);
}

//左边的树 右边的grid
var searchAllFs="姓名#name#textField,所在党支部#dzz.dangzu_name#textField,性别#sex#comboBox,出生日期#birthday#dateField,民族#minzu#textField,状态#status#comboBox".split(",");
var searchDefaultFs="姓名#name#textField,所在党支部#dzz.dangzu_name#textField,性别#sex#comboBox,出生日期#birthday#dateField,民族#minzu#textField,状态#status#comboBox".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});
	
var leftTree=new LeftTree();
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


