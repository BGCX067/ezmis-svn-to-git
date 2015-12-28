//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id == "btnModi"){
		button.setDisabled(true);
	}
	if(button.id=="btnDel"){
		button.setDisabled(true);
	}
}

//添加
function btnAdd_Click(){
	
	var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&nowYmd="+new Date().format("Y-m-d H:i");
	var myTitle = "添加记录";
	var fw = new $FW({
		url:url,
		width:750,
		height:582,			
		id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
		type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
		title: myTitle,					//标题
		status: false,					//状态栏
		toolbar:false,					//工具栏
		scrollbars:false,				//滚动条
		menubar:false,					//菜单栏
		userIF:false,					//是否采用Iframe套框,为解决模式窗口无法刷新的问题
		resizable:false,				//是否支持调整大小
		callback:function(retValue){	//回调函数
			rightGrid.getStore().reload();
		}
	});
	fw.show();
}

//修改
function btnModi_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var id = select.get("ID");
	var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&nowYmd="+
			new Date().format("Y-m-d H:i")+"&docid="+id;
	var myTitle = "修改记录";
	
	var fw = new $FW({
		url:url,
		width:750,
		height:582,			
		id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
		type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
		title: myTitle,					//标题
		status: false,					//状态栏
		toolbar:false,					//工具栏
		scrollbars:false,				//滚动条
		menubar:false,					//菜单栏
		userIF:false,					//是否采用Iframe套框,为解决模式窗口无法刷新的问题
		resizable:false,				//是否支持调整大小
		callback:function(retValue){	//回调函数
		    rightGrid.getStore().reload();
		}
	});
	fw.show();
}
//删除
function btnDel_Click(){
	var selects=rightGrid.getSelectionModel().getSelections();

	var url = CONTEXT_PATH+"/jteap/form/eform/EFormAction!delEFormRecAction.do?formSn="+formSn;
	var myTitle = "修改记录";
	var ids="";
	Ext.each(selects,function(selectedobj){
		ids+=selectedobj.get("ID")+",";//取得他们的id并组装
	});

	if(window.confirm("确认删除选中的统计数据吗？")){
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

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
//var searchAllFs="开始时间#beginYmd#dateField,结束时间#endYmd#dateField,填写人#cbr#textField".split(",");
var searchAllFs="开始时间#beginYmd#dateField,结束时间#endYmd#dateField,填写人#CBR#textField,机组#JZBH#CobomBoxJZ,性质#XZ#CobomBoxXz,责任部门#ZRDW#CobomBoxZRBM".split(",");
//查询面板中默认显示的条件，格式同上
//var searchDefaultFs="开始时间#beginYmd#dateField,结束时间#endYmd#dateField,填写人#cbr#textField".split(",");
var searchDefaultFs="开始时间#beginYmd#dateField,结束时间#endYmd#dateField,填写人#CBR#textField,机组#JZBH#CobomBoxJZ,性质#XZ#CobomBoxXz,责任部门#ZRBM#CobomBoxZRBM".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//设置查询条件为当前时间、班次
Ext.getCmp("sf#beginYmd").setValue(MonthFirstDay);
Ext.getCmp("sf#endYmd").setValue(nowYmd);

var rightGrid=new RightGrid();
rightGrid.getStore().reload();

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

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}









