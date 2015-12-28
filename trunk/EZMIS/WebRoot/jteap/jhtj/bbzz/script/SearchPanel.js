var SearchPanel = function(config){
	var search = this;
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		//var queryParamsJson = {};
		var queryParamsSql = "";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					// key,value字段,可以进行模糊查询
					if (temp.getName().split("#")[1] == "cname") {
						queryParamsSql=queryParamsSql+"cname="+tempValue+"&";
					}else if(temp.getName().split("#")[1] =="zbrq"){
						tempValue=temp.getRawValue();
						//tempValue = formatDate(tempValue,"yyyy-MM-dd");
						queryParamsSql=queryParamsSql+"zbrq="+tempValue+"&";
					}else if(temp.getName().split("#")[1] =="status"){
						queryParamsSql=queryParamsSql+"status="+tempValue+"&";
					}
				}
			};
		});
		if(queryParamsSql!=""){
			queryParamsSql=queryParamsSql.substring(0,queryParamsSql.length-1);
		}
		var oNode=leftTree.getSelectionModel().getSelectedNode();
		if(oNode){
			var bbindexid=oNode.id;
			if(oNode.isRootNode()){
				alert("该分类下不能进行查询!");
			}else{
				// 获取当前状态
				var url = link4 + "?bbindexid="+ bbindexid + "&" +queryParamsSql;
				rightGrid.changeToListDS(url);
		    	rightGrid.getStore().reload();
			}
		}else{
			alert("该分类下不能进行查询!");
		}
	};
	SearchPanel.superclass.constructor.call(this,config);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	appendSearchField:function(searchField,bHide){
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
 			oSearchField=new Ext.form.TextField({fieldLabel: sLabel,width:60,id:sId});
		}else if(sType=="dateField"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d',width:80,id:sId});
		}else if(sType=="comboBox"){
			var sjlxds = new Ext.data.Store( {
				data: {rows:[{'key':'全部','value':''},{'key':'生成','value':'2'},{'key':'发布','value':'1'}]},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id'])
			});
			oSearchField = new Ext.form.ComboBox( {
				hiddenName : sId,// 真正接受的名字
				store : sjlxds,// 数据源
				fieldLabel : '状态',
				displayField : 'key',// 数据显示列名
				valueField : 'value',
				mode : 'local',// 默认以'remote'作为数据源
				triggerAction : 'all',// 单击下拉按钮时激发事件
				typeAhead : true,// 自动完成功能
				editable :false,
				selectOnFocus : true,
				fieldLabel: sLabel,
				width:60,
				emptyText : '选择状态'
			});
		}
		
		tmpPanel=new Ext.Panel({labelWidth:70,layout:'form',columnWidth:.33,id:'SFP#'+sId,
			items:[oSearchField]
		});
		
	 	oPanel.add(tmpPanel);
	 	if(typeof(bHide)=='boolean' && bHide==true){
	 		tmpPanel.hide();
	 	}
	}
});
