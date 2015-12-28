var SearchPanel = function(config){
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var startDt=null;
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql = "";
		var flag = false;
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd hh:mm");
		 				if(temp.id.split("#")[1]=="startDate"){
		 					startDt = tempValue;
		 					queryParamsSql+="to_char(obj.dt,'yyyy-mm-dd hh24:mi')>='"+tempValue+"' and ";
		 				}else if(temp.id.split("#")[1]=="endDate"){
		 					queryParamsSql+="to_char(obj.dt,'yyyy-mm-dd hh24:mi')<='"+tempValue+"' and ";
		 					if(startDt!=null){
		 						if(startDt>tempValue){
		 							alert("开始日期不能大于结束");
		 							flag=true;
		 							startDt=null;
		 							return false;
		 						}else{
		 							startDt=null;
		 						}
		 					}
		 				}

		 			//其他字段模糊查询
		 			}else{
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
		 			}
	 			}
	 		};
	 	});
	 	if(flag){
	 		return;
	 	}
	 	var url = link1+"?type="+type+"&queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
		gridPanel.changeToListDS(url)
 		gridPanel.getStore().reload();	 	
	};
	SearchPanel.superclass.constructor.call(this,config);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
});
