/**
 * 查询面板
 */
Ext.form.Field.prototype.msgTarget = 'side';
SearchPanel = function(config) {
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
					if (oItem.hidden == false) {
						var temp = oItem.items.items[0];
						var tempValue = temp.getValue();
						// 值不为空才作为参数
						if (tempValue != null && tempValue != "") {
							if(temp.triggerClass=="x-form-date-trigger"){
				 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
				 				if(temp.id.split("#")[1]=="beginYmd"){
				 					queryParamsSql+=" to_char(b.bwsj,'yyyy-MM-dd')>='"+tempValue + "' and ";
				 				}else if(temp.id.split("#")[1]=="endYmd"){
				 					queryParamsSql+=" to_char(b.bwsj,'yyyy-MM-dd')<='"+tempValue + "' and ";
				 				}
				 			}else{
								if (temp.id.split("#")[1] == "HTMC") {
									queryParamsSql += " a." + temp.id.split("#")[1] + " like '"
											+ encodeURIComponent("%" + tempValue + "%") + "' and ";
								}else{
									queryParamsSql += " a." + temp.id.split("#")[1] + " = '"
											+ encodeURIComponent(tempValue) + "' and ";
								}
				 			}
						}
					};
				});
		
		var url = link1+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5)+"&tableName="+tableName;
		
		var node = leftTree.getSelectionModel().getSelectedNode();
		if(node.id != 'rootNode'){
    		url += "&htlx="+node.text;
		}
		rightGrid.changeToListDS(url);
		rightGrid.store.reload();
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
		var url = link1+"?tableName="+tableName;
		var node = leftTree.getSelectionModel().getSelectedNode();
		if(node.id != 'rootNode'){
    		url += "&htlx="+node.text;
		}
		rightGrid.changeToListDS(url);
		rightGrid.store.reload();
	};

	SearchPanel.superclass.constructor.call(this,config);
};
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	
	/**
	 * 覆盖父类方法，添加comboBox类型的查询框
	 */
	appendSearchField: function(searchField,bHide){
	 	var sLabel=searchField;
	 	var sId="sf#"+searchField;
	 	var sType = searchField;
	 	if(searchField.indexOf("#")>=0){
	 		var tmp=searchField.split("#");
	 		sLabel=tmp[0];
	 		sId='sf#'+tmp[1];
	 		sType = tmp[2]
	 	}
	 	var oPanel=this.items.get(0);
	 	var oSearchField=null;
	 	//文本类型
		if(sType == "textField"){
	 		oSearchField=new Ext.form.TextField({fieldLabel: sLabel,width:this.txtWidth,id:sId});
		//日期类型
		}else if(sType=="dateField"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,id:sId});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
									
		}
		tmpPanel=new Ext.Panel({labelWidth:this.labelWidth,layout:'form',columnWidth:.3,id:'SFP#'+sId,
			items:[oSearchField]
		});
		
	 	oPanel.add(tmpPanel);
	 	if(typeof(bHide)=='boolean' && bHide==true){
	 		tmpPanel.hide();
	 	}
	 }
	 
});