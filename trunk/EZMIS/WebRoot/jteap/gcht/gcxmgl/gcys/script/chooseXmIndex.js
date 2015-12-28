var searchAllFs = "项目编号#gcxmbh#textField,项目名称#gcxmmc#textField,创建年份#htcjsj#yearField".split(",");
var searchDefaultFs = "项目编号#gcxmbh#textField,项目名称#gcxmmc#textField,创建年份#htcjsj#yearField".split(",");

var searchPanel = new SearchPanel({
	searchDefaultFs : searchDefaultFs,
	searchAllFs : searchAllFs
});

var rightGrid = new RightGrid();
rightGrid.getStore().reload();

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
