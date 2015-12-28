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
var parWhere = "";
function btnExport_Click(){
	var selections = rightGrid.getSelections();//获取被选中的行
	var select = rightGrid.getSelectionModel().selections;
	var idsArr = new Array();
	//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
	var url = link7;
	var f = document.createElement("form");
	f.name = "aaa";
	f.target="newWindow"; 
	f.method="post";
	document.body.appendChild(f);
	var input = document.createElement("input");
	input.type = "hidden";
	if(select && select.length > 0){
		for(var i =0; i < select.length; i++){
			idsArr.push(select.items[i].id);
		}
		input.value = idsArr;
		input.name = "id";	
		f.appendChild(input);
		f.action = url;
		f.onsubmit=function(){
			window.open("about:blank","newWindow","");
		}
		f.submit();	
	}else{
	 	var path = contextPath +"/jteap/wz/yhdmx/YhdmxAction!exportExcel.do"+parWhere;
		window.open(path);
	}
	
	
}

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if( button.id=='btnAdd'|| button.id=='btnDel'|| button.id=='btnExce') 
		button.setDisabled(true);
}
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="仓库名称#ckmc#comboBox,单价大于#ddj#textField,单价小于#xdj#textField,盘点数量#sl#textField,在库时间#zksj#textField".split(',');
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="仓库名称#ckmc#comboBox,单价大于#ddj#textField,单价小于#xdj#textField,盘点数量#sl#textField,在库时间#zksj#textField".split(',');
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchDefaultFs});
	
//左边的树 右边的grid
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


