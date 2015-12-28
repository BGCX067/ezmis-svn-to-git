var searchAllFs = "设备名称#SBMC#textField,缺陷名称#QXMC#textField,责任班组#ZRBM#comboBoxZr,缺陷分类#QXFL#comboBoxFl,查询时间从#startDate#dateFieldShowTime,到#endDate#dateFieldShowTime,发现人#FXR#textField,缺陷单编号#QXDBH#textField,机组#JZBH#CobomBoxJZ,发现部门#FXBM#comboBoxFxbm"
		.split(",");
var searchDefaultFs = "设备名称#SBMC#textField,缺陷名称#QXMC#textField,责任班组#ZRBM#comboBoxZr,缺陷分类#QXFL#comboBoxFl,查询时间从#startDate#dateFieldShowTime,到#endDate#dateFieldShowTime,发现人#FXR#textField,缺陷单编号#QXDBH#textField,机组#JZBH#CobomBoxJZ,发现部门#FXBM#comboBoxFxbm"
		.split(",");

// 左边的树 右边的grid
var rightGrid = new RightGrid();
var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs,
	labelWidth : 70,
	txtWidth : 130
});

Ext.getCmp('sf#startDate').setValue(firstDay);
Ext.getCmp('sf#endDate').setValue(endDay);

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
