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



//新增
function btnAdd_Click(){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	showIFModule(url,"团青推优","true",636,360,{},null,null,null,false,"auto");
	rightGrid.getStore().reload();
}

//修改
function btnMod_Click(){
	var select=rightGrid.getSelectionModel().getSelected();
	if(select){
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id;
		showIFModule(url,"团青推优","true",636,360,{},null,null,null,false,"auto");
		rightGrid.getStore().reload();
	}	
}

//删除
function btnDel_Click(){
	var url = contextPath + "/jteap/form/eform/EFormAction!delEFormRecAction.do?formSn="+formSn;
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
	if( button.id=='btnMod'|| button.id=='btnDel') 
		button.setDisabled(true);
}

//左边的树 右边的grid ,学历#xueli#comboBox
var searchAllFs="姓名#name#textField,性别#sex#comboBox,出生日期#birthday#dateField,学历#xueli#comboBox,部门#bumen#textField,职称#bumen_zhiuwu#textField,所在团支部#tuanzu#comboBox,申请入党时间#shengqing#dateField".split(",");
var searchDefaultFs="姓名#name#textField,性别#sex#comboBox,出生日期#birthday#dateField,学历#xueli#comboBox,部门#bumen#textField,职称#bumen_zhiuwu#textField,所在团支部#tuanzu#comboBox,申请入党时间#shengqing#dateField".split(",");
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


