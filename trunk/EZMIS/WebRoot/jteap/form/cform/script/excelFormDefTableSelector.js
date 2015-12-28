var pWin = window.dialogArguments.opener;

/**
 * 页面初始化
 */
function onload() {

	

}

var tableTree=new DefTableTree();

var lyCenter = {
	id : 'center-panel',
	region : 'center',
	layout:'border',
	border : true,
	//layout:"fit",
	items : [tableTree]
}
