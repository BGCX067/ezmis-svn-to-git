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
	 					queryParamsSql+="birthday=to_date('"+tempValue+"','yyyy-mm-dd')"+"'and'";
	 				}else if(/[@#\$%\^&\*]+/g.test(tempValue)||tempValue.indexOf("'")!=-1){
 						alert("请不要输入非法字符");
 						temp.setValue('');
	 				}else if(temp.id.split("#")[1]=="dangzu"){
	 					queryParamsSql+="obj."+temp.id.split("#")[1]+".dangzu_name like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
	 				}else{
	 					queryParamsSql+="obj."+temp.id.split("#")[1]+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
	 				}
	 			}
	 		};
	 	});
	 	//选中党组织id
		var id;
		var url;
		//如果有选中节点 且不是根节点 带入id作为参数
		if(leftTree.getSelectionModel().getSelectedNode()!=null&&leftTree.getSelectionModel().getSelectedNode()!=leftTree.getRootNode()){
			id =leftTree.getSelectionModel().getSelectedNode().id;
			url=link2+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5)+"&limit=11&id='"+id+"'";
		}else{
			url = link2+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5)+"&limit=11";
		}
		rightGrid.changeToListDS(url);
 		rightGrid.getStore().reload();	 	
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
		
		//选中党组织id
		var id;
		var url;
		//如果有选中节点 且不是根节点 带入id作为参数
		if(leftTree.getSelectionModel().getSelectedNode()!=null&&leftTree.getSelectionModel().getSelectedNode()!=leftTree.getRootNode()){
			id =leftTree.getSelectionModel().getSelectedNode().id;
			url=link2+"?id='"+id+"'";
		}else{
			url = link2;
		}
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
//	 	var txtWidth=30;
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
	 		oSearchField=new Ext.form.TextField({fieldLabel: sLabel,width:90,id:sId,labelStyle:'width:70'});
		//日期类型
		}else if(sType=="dateField"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:90,id:sId,labelStyle:'width:70'});
			
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',labelStyle:'width:70',width:this.txtWidth,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
			//评级级别数据源
			var storeLevel=  new Ext.data.SimpleStore({
				fields: ["retrunValue", "displayText"],
				data: [['0','男'],['1','女']]
			});
			
			var storeStatus=new Ext.data.SimpleStore({
					fields:["retrunValue","displayText"],
					data:[['0','党员'],['1','入党积极分子'],['2','预备党员']]
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
				});}
				
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
				})
				};
				
				if(arrays[1] == 'tuanzu1'){
				oSearchField = new Ext.app.ComboTree( {
					id: sId,
					fieldLabel : sLabel,
					dataUrl : link3,
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
			};
				
			
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