/**
 * 查询面板
 */
SearchPanel = function(config) {
	var ckgl ="";
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
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				queryParamsSql+="to_char("+temp.id.split("#")[1]+",'yyyy-mm-dd')='"+tempValue+"' and ";
		 			//其他字段模糊查询
		 			}else if(temp.id.split("#")[1]=="wzmc"){
		 				queryParamsSql+=temp.id.split("#")[1]+" like '"+encodeURIComponent(tempValue+"%")+"' and ";
		 			}else if(temp.id.split("#")[1]=="ckgl"){
		 					ckgl = tempValue;
		 			}else{
		 				queryParamsSql+=temp.id.split("#")[1]+" like '"+encodeURIComponent("%"+tempValue+"%")+"' and ";
		 			}
	 			}
	 		};
	 	});
	 	if(flag){
	 		return;
	 	}
	 	var wzlbbm = (leftTree.getSelectionModel().getSelectedNode()==null || leftTree.getSelectionModel().getSelectedNode().text == "物资类别")?"":leftTree.getSelectionModel().getSelectedNode().id;
	 	if(ckgl!=""){
			queryParamsSql += " obj.kw.ckid='"+ckgl+"' and ";
		}
	 	var url = link2+"?wzlbbm="+wzlbbm+"&queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
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
 		var url = link2;
 		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();
	};
	
    SearchPanel.superclass.constructor.call(this, config);
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
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,readOnly : true,id:sId});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,readOnly:true,id:sId,menu:new DatetimeMenu()});
		}else if (sType == "comboCkgl") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link10}),
					reader: new Ext.data.JsonReader({root: 'list'},
							['ckmc','id'])
				}),  
                 valueField:'id',  
                 displayField:'ckmc',  
                 mode:'local',  
                 blankText : '', 
                 emptyText:'',            
//                 hiddenName:'userName', 
                 editable:false,
//         		 editable:true,selectOnFocus:true,
                 forceSelection:true,  
                 triggerAction:'all',  
	   			 triggerClass:'comboCkgl',
                 anchor:'90%' 
			});
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