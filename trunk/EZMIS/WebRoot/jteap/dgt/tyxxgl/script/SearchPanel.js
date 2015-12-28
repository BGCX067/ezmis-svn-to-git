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
	 				if(temp.id.split("#")[1]=="birthday"){
	 					//日期字段
	 					tempValue = formatDate(tempValue,"yyyy-MM-dd");
	 					queryParamsSql+="birthday=to_date('"+tempValue+"','yyyy-mm-dd')"+" and ";
	 				}else{
	 					if(/[@#\$%\^&\*]+/g.test(tempValue)||tempValue.indexOf("'")!=-1){
	 						alert("请不要输入非法字符");
	 						temp.setValue('');
	 					}else{
	 						queryParamsSql+="obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
	 					}
	 				}
	 			}
	 		};
	 	});
	 	var url = link2+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5)+"&limit=11";
		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload({params:{start:0,limit:11}});	 	
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
		var url = link2;
		rightGrid.changeToListDS(url);
		rightGrid.getStore().reload({params:{start:0,limit:11}});
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
			//评级级别数据源
			var storeLevel=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['0','男'],['1','女']]
			});
			
			var storeTuanZu=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['001','发电团支部'],['002','燃运团支部'],['003','检修团支部'],['004','机关团支部']]
			});
			
			var storeZhengZhi=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['001','团员'],['002','一般群众']]
			});
			
			var storeStatus=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['0','在团'],['1','离团']]
			});
			
			//评级级别
			if(arrays[1] == 'sex'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择性别',
					emptyText: '请选择性别',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeLevel
				});
			}
			//团支部
			if(arrays[1] == 'tuanzu'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择团支部',
					emptyText: '请选择团支部',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeTuanZu
				});
			}
			//政治面貌
			if(arrays[1] == 'zhengzhi'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					valueField: "retrunValue",
					displayField: "displayText",
					mode: 'local',
					triggerAction: 'all',
					blankText: '请选择政治面貌',
					emptyText: '请选择政治面貌',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeZhengZhi
				});
			}
			
			//状态
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