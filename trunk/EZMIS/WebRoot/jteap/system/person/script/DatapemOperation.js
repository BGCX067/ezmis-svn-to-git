var datapermTree;
var datapermwin;
function showDatapermTree(){

	// 获得选择方式
	var chooseType = "multipe";

	// 获得是否选择完整路径
	var checkPath = false;

	// 获得是否只选择末端节点
	var checkLeaf = false;
	
	var select=gridPanel.getSelectionModel().getSelections()[0];
	var personid="";
	if(select!=undefined){
		personid=select.id;
	}
	
	var oNode=groupTree.getSelectionModel().getSelectedNode();
	var isDissociation="";
	//标识游离用户
	if(oNode.dissociation){
		isDissociation="yes";
	}else{
		isDissociation="no";
	}

	datapermTree = new DatapermTree(chooseType, checkPath, checkLeaf,personid,isDissociation);

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
			//userName += arrays[i].text + ",";
			//根节点不被包含在内
			if(arrays[i].id.indexOf('ynode')==-1)
			datapermId += arrays[i].id + ",";
		}
		
		var oNode=groupTree.getSelectionModel().getSelectedNode();
		var selects=gridPanel.getSelectionModel().getSelections();
		var userids="";
		for(var i=0;i<selects.length;i++){
		    var temp=selects[i];
		    var id=temp.id;
			userids+=temp.id+",";
		}
		//组装
		var param = {};
		// 将权限ID放入对象
		param.datapermids=datapermId.substr(0, datapermId.length - 1);
		if(selects.length==0&&oNode.id!=""){
			param.groupid=oNode.id;
		}
		if(selects.length>0){
			param.users=userids.substring(0,userids.length-1);
		}
		//标识游离用户
		if(oNode.dissociation){
			param.dissociation="yes";
		}else{
			param.dissociation="no";
		}
		AjaxRequest_Sync(link35, param, function(req) {
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