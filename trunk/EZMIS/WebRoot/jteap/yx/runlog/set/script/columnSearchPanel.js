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
	 				if(temp.id.split("#")[1] == "columnName"){
						queryParamsSql += "obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("%"+tempValue+"%")+"' and ";					
					}else{
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = link8+"?limit=17&tableId="+tableId+"&queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
		columnInfoGrid.changeToListDS(url);
 		columnInfoGrid.getStore().reload();	 	
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
 		var url = link8+"?limit=17&tableId="+tableId;
 		columnInfoGrid.changeToListDS(url);
 		columnInfoGrid.getStore().reload();
	};
	
	SearchPanel.superclass.constructor.call(this,config);
}

Ext.extend(SearchPanel, Ext.app.SearchPanel, {
		
});