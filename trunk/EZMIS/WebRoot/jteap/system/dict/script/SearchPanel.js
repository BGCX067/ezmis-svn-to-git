
/**
 * 查询面板
 */
SearchPanel = function(config) {
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql = "";
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
			 		//key,value字段,可以进行模糊查询
			 		if(temp.id.split("#")[1]=="key"){
			 			queryParamsSql+="key"+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
			 		}else if(temp.id.split("#")[1]=="value"){
			 			queryParamsSql+="value"+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
			 		}
	 			}
	 		};
	 	});
	 	//获取当前字典类型节点
	 	var oNode=dictCatalogTree.getSelectionModel().getSelectedNode();
	 	var url = link1+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
	 	if(dictCatalogTree.getRootNode().id!=oNode.id){
	 		url+="&catalogId="+oNode.id;
	 	}
 		gridPanel.changeToListDS(url);
 		gridPanel.getStore().reload();	 	
	};  
    SearchPanel.superclass.constructor.call(this, config);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
});