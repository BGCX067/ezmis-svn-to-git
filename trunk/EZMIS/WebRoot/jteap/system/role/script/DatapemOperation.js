var datapermTree;
var datapermwin;
function showDatapermTree(){

	// 获得选择方式
	var chooseType = "multipe";

	// 获得是否选择完整路径
	var checkPath = false;

	// 获得是否只选择末端节点
	var checkLeaf = false;
	
	
	var oNode=roleTree.getSelectionModel().getSelectedNode();

	datapermTree = new DatapermTree(chooseType, checkPath, checkLeaf ,oNode.id);

	datapermwin = new Ext.Window( {
		layout : 'fit',
		title : '权限选择器',
		width : 250,
		height : 350,
		frame : true,
		resizable : false,
		modal : true,
		items : [datapermTree],
		buttons : [ {
			text : '确定',
			handler : saveUserAndDataperm
		}, {
			text : '取消',
			handler : function() {
				datapermwin.hide();
			}
		}]
	});
	datapermwin.show();
}


function saveUserAndDataperm() {
		// 获得选中权限ID
		var result = datapermTree.getRoleNameAndId();
		result = "[" + result + "]";
		var arrays = Ext.decode(result);

		//
		//var userName = "";
		var datapermId = "";
		for (var i = 0;i < arrays.length; i++) {
			//根节点不被包含在内
			if(arrays[i].id.indexOf('ynode')==-1)
			datapermId += arrays[i].id + ",";
		}
		
		var oNode=roleTree.getSelectionModel().getSelectedNode();

		//组装
		var param = {};
		// 将权限ID放入对象
		param.datapermids=datapermId.substr(0, datapermId.length - 1);
		if(oNode.id!=""){
			param.roleid=oNode.id;
		}
		
		AjaxRequest_Sync(link19, param, function(req) {
				var responseText = req.responseText;
				var responseObj = Ext.decode(responseText);
				if (responseObj.success) {
					Ext.Msg.show( {
					msg : '关联成功!',
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.INFO
				});
				datapermwin.hide();
			} else {
				Ext.MessageBox.alert('Status', responseObj.msg);
			}
		});
		
		
}