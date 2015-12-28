/**
 * 查询面板
 */
var SearchPanel = function(config){
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "";
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
					if(temp.id.split("#")[1] == "directiveName"){
						queryParamsSql += "obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("%"+tempValue+"%")+"' and ";					
					}else{
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = link8+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
	 	var oNode = leftTree.getSelectionModel().getSelectedNode();
	 	if(oNode != null && oNode.id != "rootNode"){
	 		url += "&directiveId="+oNode.id;
	 	}else{
	 		url += "&directiveId=null";
	 	}
	 	
		columnGrid.changeToListDS(url);
 		columnGrid.getStore().reload();
	};
	
	//清空查询条件
	this.clearSearch=function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			temp.reset();
	 			//日期字段
	 			if(temp.triggerClass=="x-form-date-trigger"){
					temp.setValue('');	 				
	 			}
	 		};
	 	});
 		var url = link8;
 		
 		var oNode = leftTree.getSelectionModel().getSelectedNode();
	 	if(oNode != null && oNode.id != "rootNode"){
	 		url += "?directiveId="+oNode.id;
	 	}else{
	 		url += "?directiveId=null";
	 	}
 		
 		columnGrid.changeToListDS(url);
 		columnGrid.getStore().reload();
	};
	
	SearchPanel.superclass.constructor.call(this,config);
}

Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	
});