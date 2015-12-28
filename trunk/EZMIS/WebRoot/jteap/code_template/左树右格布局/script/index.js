// 左边的树 右边的grid
var rowExpanderEx = new RowExpanderEx();
var searchAllFs = "业务主分类#ywz#comboYWZ,业务分类#yw#comboYW,纳税人识别号#nsrsbh#numberField"
		.split(",");
var searchDefaultFs = "业务主分类#ywz#comboYWZ,业务分类#yw#comboYW,纳税人识别号#nsrsbh#numberField"
		.split(",");
var searchPanel = new SearchPanel({
			searchAllFs : searchAllFs,
			searchDefaultFs : searchDefaultFs,
			labelWidth : 80,
			txtWidth : 125
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
	items : [rowExpanderEx, searchPanel]
}
