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
					if(temp.id.split("#")[1] == "dqgzMc"){
						queryParamsSql += "obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("%"+tempValue+"%")+"' and ";					
					}else{
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = link4+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5)+"&limit=20";
		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();	 	
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
 		var url = link4;
 		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();
	};
	
	SearchPanel.superclass.constructor.call(this,config);
}

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
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,readOnly : true,id:sId});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,readOnly:true,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
									
			//负责部门
			if(arrays[1] == 'fzbm'){
				oSearchField = new Ext.app.ComboTree( {
					id: sId,
					fieldLabel : sLabel,
					dataUrl : link7,
					listWidth:200,
					blankText:'请选择部门',
					emptyText:'请选择部门',
					triggerClass:'comboTree',
					anchor : '90%',
					autoScroll:true,
					localData : false,
					rootVisible : false,
					allowBlank: false,
					listeners : {
						select : function(t, node) {
							this.hiddenName = node.id;
						}
					}
				})
			}
			
			//负责岗位
			if(arrays[1] == 'fzgw'){
				oSearchField = new Ext.app.ComboTree( {
					id: sId,
					fieldLabel : sLabel,
					dataUrl : link8,
					listWidth:200,
					blankText:'请选择岗位',
					emptyText:'请选择岗位',
					triggerClass:'comboTree',
					anchor : '90%',
					autoScroll:true,
					localData : false,
					rootVisible : false,
					allowBlank: false,
					listeners : {
						select : function(t, node) {
							this.hiddenName = node.id;
						}
					}
				})
			}
			
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