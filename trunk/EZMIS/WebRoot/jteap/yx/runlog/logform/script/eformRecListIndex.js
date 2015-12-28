
//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){}

var nowDate = new Date();
var nowYm = nowDate.format("Y-m");
var beginYmd = nowYm + "-01";
var nowYmd = nowDate.format("Y-m-d");

/**
 * 添加运行方式
 */
function btnAdd_Click(){
	var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	var mylink = link2+"?formSn="+formSn+"&nowYmd="+nowYmd;
	
	var myTitle = "添加运行日志";
	//根据时间查询是否存在该条记录,存在就进行弹出修改窗口
	var conn = Ext.lib.Ajax.getConnectionObject().conn; 
	conn.open("post", mylink,false); 
	conn.send(null); 
	var responseObject = Ext.util.JSON.decode(conn.responseText);
	if(responseObject.success == true){
		var id = responseObject.id;
		if(id != ""){
			url += "&docid=" + id;
			myTitle = "修改运行日志";
		}
	}
	
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
//  showIFModule(CONTEXT_PATH+url,myTitle,"true",800,600,{});
	
}

//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="开始时间#beginYmd#dateField,结束时间#endYmd#dateField,填写人#tianxieren#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="开始时间#beginYmd#dateField,结束时间#endYmd#dateField,填写人#tianxieren#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//设置查询条件为最后一个交接班时间
Ext.getCmp("sf#beginYmd").setValue(beginYmd);
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
