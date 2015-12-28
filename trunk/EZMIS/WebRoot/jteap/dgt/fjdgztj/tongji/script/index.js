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


//录入
function btnAdd_Click(){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	showIFModule(url,"工会分季度统计","true",590,590,{},null,null,null,false,"auto");
	rightGrid.getStore().reload();
}

//修改
function  btnMod_Click(){
	var select=rightGrid.getSelectionModel().getSelected();
	if(select){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.id;
		showIFModule(url,"工会分季度统计","true",590,590,{},null,null,null,false,"auto");
	}
	rightGrid.getStore().reload();
}

//删除
function btnDel_Click(){
	var url = contextPath + "/jteap/dgt/fjdgztj/TongJiAction!delEFormRecAction.do?formSn="+formSn;
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
	if( button.id=='btnMod'|| button.id=='btnDel'
	) 
		button.setDisabled(true);
}

//左边的树 右边的grid
var searchAllFs="工会#gonghui#comboBox,年#year#comboBox,季度#jidu#comboBox".split(",");
var searchDefaultFs="工会#gonghui#comboBox,年#year#comboBox,季度#jidu#comboBox".split(",");
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


