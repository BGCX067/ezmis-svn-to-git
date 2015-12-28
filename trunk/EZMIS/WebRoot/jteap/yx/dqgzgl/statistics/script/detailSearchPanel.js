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
	 				if(temp.triggerClass=="x-form-date-trigger"){
	 					//日期字段
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				
	 					queryParamsSql+="to_char(obj."+temp.id.split("#")[1]+",'yyyy-mm-dd')='"+tempValue+"' and ";
		 			}else{
		 				//其他字段模糊查询
		 				queryParamsSql+="obj."+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
					}
	 			}
	 		};
	 	});
	 	var url = link2+"?limit=23&queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
		detailGrid.changeToListDS(url);
 		detailGrid.getStore().reload();	 	
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
 		var url = link2+"?limit=23";
 		detailGrid.changeToListDS(url);
 		detailGrid.getStore().reload();
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
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:this.txtWidth,id:sId});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',width:this.txtWidth,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
			
			//执行状态数据源
			var storeStatus=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['已完成','已完成'],['未完成','未完成']]
			});
			
			//数据源-工作规律
			var storeGzgl =  new Ext.data.Store({
				data: {rows:dict_dqgzgl},
				reader: new Ext.data.JsonReader( {
					root: 'rows',
					id: 'id'
				}, ['key', 'value', 'id'])
			});
			
			//执行状态
			if(arrays[1] == 'status'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择状态',
					emptyText: '请选择状态',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeStatus
				});
			}
			
			//周期
			if(arrays[1] == 'gzgl'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "value",
					displayField: "key",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择规律',
					emptyText: '请选择规律',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeGzgl
				});
			}
			
			//负责部门
			if(arrays[1] == 'fzbm'){
				oSearchField = new Ext.app.ComboTree( {
					id: sId,
					fieldLabel : sLabel,
					dataUrl : link7,
					width: this.txtWidth,
					blankText:'请选择部门',
					emptyText:'请选择部门',
					triggerClass:'comboTree',
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
					width: this.txtWidth,
					blankText:'请选择岗位',
					emptyText:'请选择岗位',
					triggerClass:'comboTree',
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