

var SearchPanel = function(config){
	var search = this;
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var title;
		var creator;
		var createdt;
		//var queryParamsJson = {};
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					if(temp.getName().split("#")[1]=="createdt"){
						createdt=formatDate(tempValue,"yyyy-MM-dd");
					}else if(/[@#\$%\^&\*]+/g.test(tempValue)||tempValue.indexOf("'")!=-1){
						alert("请不要输入非法字符");
						temp.setValue('');
						return;
					}if(temp.getName().split("#")[1]=="title"){
						title=encodeURIComponent(tempValue);
					}else if(temp.getName().split("#")[1]=="creator"){
						creator=encodeURIComponent(tempValue);;
					}
				}
			};
		});
		var oNode=leftTree.getSelectionModel().getSelectedNode();
		var catalogId = oNode.isRootNode()?"":oNode.id;
		changeToListDS(catalogId,title!=undefined?title:"",creator!=undefined?creator:"",createdt!=undefined?createdt:"");
	};
	// 清空查询条件
	this.clearSearch = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		Ext.each(oItems, function(oItem) {
					if (oItem.hidden == false) {
						var temp = oItem.items.items[0];
						temp.reset();
						// 日期字段
						if (temp.triggerClass == "x-form-date-trigger") {
							temp.setValue('');
						}
					};
				});
		var oNode=leftTree.getSelectionModel().getSelectedNode();
		var catalogId = oNode.isRootNode()?"":oNode.id;
		changeToListDS(catalogId,"","","");
	};
	SearchPanel.superclass.constructor.call(this,config);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
});