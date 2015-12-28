//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id == 'btnModi' || button.id == 'btnDel'){
		button.setDisabled(true);
	}
}

var nowDate = new Date();
var nowYm = nowDate.format("Y-m");
var beginYmd = nowYm + "-01";
var nowYmd = nowDate.format("Y-m-d");

//添加台账表单
function btnAdd_Click(){
	var url = "/jteap/form/eform/eformRec.jsp?formSn="+formSn;
	var myTitle = "添加台账表单";
    showIFModule(CONTEXT_PATH+url,myTitle,"true",800,600,{});
    rightGrid.getStore().reload();
}

//修改台账表单
function btnModi_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	var id = select.get("id");
	var url = "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+id;
	var myTitle = "修改台账表单";
	showIFModule(CONTEXT_PATH+url,myTitle,"true",800,600,{});
	rightGrid.getStore().reload();
}

//删除台账表单
function btnDel_Click(){
	if(window.confirm("确认删除选中的台账表单吗?")){
		var select=rightGrid.getSelectionModel().getSelections();
		var ids = "";
		for(var i=0; i<select.length; i++){
			ids += select[i].get("id") + ",";
		}
		
		Ext.Ajax.request({
			url: link2,
			params: {formSn:formSn, ids:ids},
			method: 'post',
			success: function(ajax){
				eval("responseObj=" + ajax.responseText);
				if(responseObj.success){
					alert('删除成功');
					rightGrid.getStore().reload();
				}else{
					alert('删除失败');
				}
			},
			failure: function(){
				alert('服务器忙,请稍后再试...');
			}
		})
	}
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
