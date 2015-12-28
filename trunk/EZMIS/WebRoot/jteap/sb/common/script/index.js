/**
 * 选择设备名称
 */
function btnYxtz_Click(){
	var selections = rightGrid.getSelections();//获取被选中的行
	if(selections == null || selections == ""){
		alert("请选择一条记录!");
		return;
	}
	var obj = {};
	//设备编码
	obj.sbbm = selections[0].data.sbbm;
	obj.sbmc = selections[0].data.ybmc;
	obj.sbgg = selections[0].data.xsjgf;
	obj.azdd = selections[0].data.azdd;
	obj.xsjgf = selections[0].data.xsjgf;
	obj.xtth = selections[0].data.xtth;
	//obj.sbtzCatalogId = selections[0].data.sbtzCatalog.id;
	window.returnValue = obj;
	window.close();
}


//设备台帐查询面板
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="设备编码#sbbm#textField,设备名称#ybmc#textField,安装位置#azdd#textField,型式及规范#xsjgf#textField,系统图号#xtth#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="设备编码#sbbm#textField,设备名称#ybmc#textField,安装位置#azdd#textField,型式及规范#xsjgf#textField,系统图号#xtth#textField".split(",");
//var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:70});

//左边的树 右边的grid
var leftTree=new LeftTree();
var rightGrid=new RightGrid();

