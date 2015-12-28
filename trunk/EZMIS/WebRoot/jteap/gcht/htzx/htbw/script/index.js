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
	if (button.id == 'btnModi' || button.id == 'btnDel')
		button.setDisabled(true);
}

function btnAdd_Click() {
	var obj = {};
	obj.grid = rightGrid;
	obj.tableName = tableName;
	
	var url = contextPath + '/jteap/gcht/htzx/htbw/bwForm.jsp';
	showIFModule(url,"添加合同备忘","true",500,290,obj);
}

function btnModi_Click(){
	var select = rightGrid.getSelectionModel().getSelections()[0];
	var obj = {};
	obj.grid = rightGrid;
	obj.tableName = tableName;
	obj.htid = select.get('HTID');	
	obj.htmc = select.get('HTMC');
	obj.htbh = select.get('HTBH');
	obj.bwid = select.get('BWID');
	obj.bwnr = select.get('BWNR');
	
	var url = contextPath + '/jteap/gcht/htzx/htbw/bwForm.jsp?modi=m';
	showIFModule(url,"添加合同备忘","true",500,290,obj);
}

function btnDel_Click(){
	if(window.confirm("确认删除选中的备忘信息吗?")){
		var select=rightGrid.getSelectionModel().getSelections();
		var ids = "";
		for(var i=0; i<select.length; i++){
			ids += select[i].get("BWID") + ",";
		}
		
		Ext.Ajax.request({
			url: link5,
			params: {ids:ids},
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

var leftTree = new LeftTree();
var rightGrid = new RightGrid();

var searchAllFs = "合同名称#HTMC#textField,合同编号#HTBH#textField,开始时间#beginYmd#dateField,结束时间#endYmd#dateField".split(",");
var searchDefaultFs = "合同名称#HTMC#textField,合同编号#HTBH#textField,开始时间#beginYmd#dateField,结束时间#endYmd#dateField".split(",");
var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs
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
