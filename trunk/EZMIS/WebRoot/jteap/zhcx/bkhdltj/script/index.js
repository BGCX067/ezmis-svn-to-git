// 工具栏
var mainToolbar = new Ext.Toolbar({
			height : 26,
			listeners : {
				render : function(tb) {
					operationsToolbarInitialize(tb);
				}
			}
		});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button) {
	if(button.id=="btnDel"||button.id=="btnMod"){
		button.setDisabled(true);
	}
}

/**
 * 新建缺陷统计
 */
function btnAdd_Click() {
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=" + "TB_FORM_SJBZB_BKHDLYTJ";
	var result = showModule(url, "yes", 800, 600,null,"","");
	rightGrid.getStore().reload();
}
//修改
function btnMod_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var id = select.get("ID");
	var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+"TB_FORM_SJBZB_BKHDLYTJ"+"&docid="+id;
	var myTitle = "修改记录";
	
	var fw = new $FW({
		url:url,
		width:750,
		height:582,			
		id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
		type:"T2",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
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

	var url = CONTEXT_PATH+"/jteap/form/eform/EFormAction!delEFormRecAction.do?formSn=TB_FORM_SJBZB_BKHDLYTJ";
	var myTitle = "删除记录";
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

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var searchAllFs = "开始日期#ksrq#dateField,结束日期#jsrq#dateField,填报人#tjr#textField".split(",");
var searchDefaultFs = "开始日期#ksrq#dateField,结束日期#jsrq#dateField,填报人#tjr#textField".split(",");

var searchPanel = new SearchPanel({
			searchDefaultFs : searchDefaultFs,
			searchAllFs : searchAllFs,
			labelWidth : 70,
			txtWidth : 130
		});

// 中间
var lyCenter = {
	layout : 'border',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [rightGrid, searchPanel]
}

// 北边
var lyNorth = {
	id : 'north-panel',
	region : 'north',
	height : 27,
	border : false,
	items : [mainToolbar]
}
