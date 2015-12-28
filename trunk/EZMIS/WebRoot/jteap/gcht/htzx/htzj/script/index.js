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
	if (button.id == 'btnZhongJie')
		button.setDisabled(true);
}

//终结合同
function btnZhongJie_Click() {
	var select = rightGrid.getSelectionModel().getSelected();
	if (select.get('STATUS') != '合同生效') {
		alert('此合同不符合终结标准,无法终结');
		return;
	}
	
	if (window.confirm('是否终结此合同?')) {
		Ext.Ajax.request({
			url : link2,
			method : 'POST',
			params : {
				id : select.get('ID'),
				tableName: tableName,
				childTableName:childTableName
			},
			success : function(ajax) {
				var responseText = ajax.responseText;
				var obj = Ext.decode(responseText);
				if (obj.success) {
					alert('合同终结成功');
					rightGrid.getStore().reload();
				} else {
					alert("数据库操作异常，请联系管理员！");
				}
			},
			failure : function() {
				alert("数据库操作异常，请联系管理员！");
			}
		})
	}
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "合同编号#HTBH#textField,合同名称#HTMC#textField".split(",");
var searchDefaultFs = "合同编号#HTBH#textField,合同名称#HTMC#textField".split(",");

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
