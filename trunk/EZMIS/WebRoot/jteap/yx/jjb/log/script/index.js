
var searchAllFs = "交接时间#doDate#dateField,交班班次#jiaobanbc#comboBox,交班值别#jiaobanzb#comboBox,交班人#jiaobanr#textField,接班班次#jiebanbc#comboBox,接班值别#jiebanzb#comboBox,接班人#jiebanr#textField,岗位类别#gwlb#comboBox".split(",");
var searchDefaultFs = "交接时间#doDate#dateField,交班班次#jiaobanbc#comboBox,交班值别#jiaobanzb#comboBox,交班人#jiaobanr#textField,接班班次#jiebanbc#comboBox,,接班值别#jiebanzb#comboBox,接班人#jiebanr#textField,岗位类别#gwlb#comboBox".split(",");

//用户查询面板								
var searchPanel = new SearchPanel({searchDefaultFs:searchDefaultFs, searchAllFs:searchAllFs});

//用户列表
var rightGrid = new RightGrid();
rightGrid.getStore().load();

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'1 0 0 -1',
	items:[searchPanel,rightGrid]
}
