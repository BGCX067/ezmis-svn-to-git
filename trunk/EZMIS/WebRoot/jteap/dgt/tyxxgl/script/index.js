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

//新增团员信息
function btnAdd_Click(){
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	showIFModule(url,"团员信息管理","true",650,340,{},null,null,null,false,"auto");
	rightGrid.getStore().reload();
}

//修改团员
function btnMod_Click(){
	var select=rightGrid.getSelectionModel().getSelected();
	if(select){
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.json.id;
		showIFModule(url,"团员信息管理","true",650,340,{},null,null,null,false,"auto");
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
//离团人员整理
function btnLtgl_Click(){
	var url = contextPath+"/jteap/dgt/tyxx/TyxxAction!showListAction.do?lt=1&zt=0&limit=11";
	rightGrid.changeToListDS(url);
	rightGrid.getStore().reload();
}

//离团
function btnLt_Click(){
	var url = contextPath + "/jteap/dgt/tyxx/TyxxAction!updateTyxxAction.do";
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(window.confirm("确认让选中的人员离团吗？")){
		Ext.Ajax.request({
			url:url,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("离团成功");
		 			var urls = contextPath+"/jteap/dgt/tyxx/TyxxAction!showListAction.do?zt=0";
		 			rightGrid.changeToListDS(urls);
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
	if( button.id=='btnMod'|| button.id=='btnDel'||button.id=='btnLt') 
		button.setDisabled(true);
}

//左边的树 右边的grid
var searchAllFs="姓名#name#textField,部门#bumen#textField,性别#sex#comboBox,团内职务#tuan_zhiwu#textField,团支部#tuanzu#comboBox,出生日期#birthday#dateField,政治面貌#zhengzhi#comboBox,状态#status#comboBox".split(",");
var searchDefaultFs="姓名#name#textField,部门#bumen#textField,性别#sex#comboBox,团内职务#tuan_zhiwu#textField,团支部#tuanzu#comboBox,出生日期#birthday#dateField,政治面貌#zhengzhi#comboBox,状态#status#comboBox".split(",");
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


