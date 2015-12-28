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
}

/**
 * 新建缺陷统计
 */
function btnAddQxtj_Click() {
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=" + "TB_JX_QXGL_QXTJBJT_TMP";
	var result = showModule(url, "yes", 800, 600,null,"","");
	rightGrid.getStore().reload();
}

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var searchAllFs = "电厂统计期#dctjq#dateField,填报人#tbr#textField".split(",");
var searchDefaultFs = "电厂统计期#dctjq#dateField,填报人#tbr#textField".split(",");

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
