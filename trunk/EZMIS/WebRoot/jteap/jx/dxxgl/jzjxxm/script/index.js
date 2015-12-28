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
	if (button.id == 'btnAddXm' || button.id == 'btnModiXm' || button.id == 'btnDelXm' || button.id == 'btnExport')
		button.setDisabled(true);
}

/**
 * 增加项目
 */
function btnAddXm_Click() {
	var node = leftTree.getSelectionModel().getSelectedNode();
	var jhId = node.id
	var url = link5 + "?jhId=" + jhId;
	var retValue = showModule(url, true, 580, 320);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 修改项目
 */
function btnModiXm_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link5 + "?id=" + record.get('id');
	var retValue = showModule(url, true, 580, 320);
	if (retValue == "true") {
		rightGrid.getStore().reload();
	}
}

/**
 * 删除项目
 */
function btnDelXm_Click() {
	rightGrid.delSelected();
}

/**
 * 导出Excel
 */
function btnExport_Click() {
	var node = leftTree.getSelectionModel().getSelectedNode();
	var jhId = node.id;
	window.open(link7+"?jhId="+jhId);
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var leftTree = new LeftTree();
var searchAllFs = "起始时间#startDate#dateField,至#endDate#dateField".split(",");
var searchDefaultFs = "起始时间#startDate#dateField,至#endDate#dateField".split(",");

var searchPanel = new SearchPanel({
			searchDefaultFs : searchDefaultFs,
			searchAllFs : searchAllFs,
			labelWidth : 60,
			txtWidth : 88
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
