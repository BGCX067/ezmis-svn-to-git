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
	if (button.id == 'btnShowJh' || button.id == 'btnShowXm' || button.id == 'btnShowZj') {
		button.setDisabled(true);
	}
}

/**
 * 显示检修计划详细信息
 */
function btnShowJh_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link2 + "?id=" + record.get('id');
	showModule(url, true, 620, 380);
}

/**
 * 显示检修项目详细信息
 */
function btnShowXm_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link4 + "?jhId=" + record.get('id');
	showModule(url, true, 650, 400);
}

/**
 * 显示检修总结报告详细信息
 */
function btnShowZj_Click() {
	var record = rightGrid.getSelectionModel().getSelected();
	var url = link7 + "?jhId=" + record.get('id');
	showModule(url, true, 635, 500);
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var searchAllFs = "起始日期#startDate#dateField,至#endDate#dateField,检修性质#jxxz#comboBoxXz,机组#jz#comboBoxJz".split(",");
var searchDefaultFs = "起始日期#startDate#dateField,至#endDate#dateField,检修性质#jxxz#comboBoxXz,机组#jz#comboBoxJz".split(",");
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
