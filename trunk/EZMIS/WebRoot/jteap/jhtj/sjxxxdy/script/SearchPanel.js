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
					if (temp.getName().split("#")[1] == "item") {
						queryParamsSql=queryParamsSql+"item="+tempValue+"&";
					} else if(temp.getName().split("#")[1] == "iname"){
						queryParamsSql=queryParamsSql+"iname="+tempValue+"&";
					} else if(temp.getName().split("#")[1] == "sjlx"){
						queryParamsSql=queryParamsSql+"sjlx="+tempValue+"&";
					} else if(temp.getName().split("#")[1] == "qsfs"){
						queryParamsSql=queryParamsSql+"qsfs="+tempValue+"&";
					}
				}
			};
		});
		if(queryParamsSql!=""){
			queryParamsSql=queryParamsSql.substring(0,queryParamsSql.length-1);
		}
		var oNode=leftTree.getSelectionModel().getSelectedNode();
		if(oNode){
			var kid=oNode.attributes.kid;
			var flflag=oNode.attributes.flflag;
			if(oNode.isRootNode()){
				alert("该分类下不能进行查询!");
			}else{
				if(flflag!=0){
					// 获取当前状态
					var url = link4 + "?kid="+ kid + "&" +queryParamsSql;
					//url += "&ywflzId=" + oNode.id;
					rightGrid.changeToListDS(url);
			    	rightGrid.getStore().reload();
				}else{
					alert("该分类下不能进行查询!");
				}
			}
		}else{
			alert("该分类下不能进行查询!");
		}
	};
	/**var pConfig = {
		title : '查询面板',
		region : 'north',
		width : 600,
		frame : true,
		margins : '1px 1px 1px 1px',
		bodyStyle : 'padding:5px',
		fitToFrame : true,
		autoHeight : true,
		split : true,
		collapsible : true,
		//collapsed :true,
		searchDefaultFs : config.searchDefaultFs,
		searchAllFs : config.searchAllFs,
		labelWidth:80,
		txtWidth:config.txtWidth,
		items : [{
					layout : 'column',
					labelSeparator : ' ：'
				}],
		bbar : [{text:'添加条件',cls: 'x-btn-text-icon',
       			 icon:'icon/icon_9.gif',menu:this.searchItemMenu},
       			{
					text : '查询',
					cls : 'x-btn-text-icon',
					icon : 'icon/icon_8.gif',
					listeners : {
						click : this.searchClick
								? this.searchClick
								: Ext.emptyFn
					}
				}]
	};**/
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
		}else if(sType=="ywlxComboBox"){
			var dict_1=$dictList("YWLX");
			var sjlxds = new Ext.data.Store( {
				data: {rows:dict_1},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id'])
			});
			var data={'key':'全部','value':''};
			var record=new Ext.data.Record(data);
			sjlxds.insert(0,record);
			oSearchField = new Ext.form.ComboBox( {
				hiddenName : sId,// 真正接受的名字
				store : sjlxds,// 数据源
				fieldLabel : '业务类型',
				displayField : 'key',// 数据显示列名
				valueField : 'value',
				mode : 'local',// 默认以'remote'作为数据源
				triggerAction : 'all',// 单击下拉按钮时激发事件
				typeAhead : true,// 自动完成功能
				editable :false,
				selectOnFocus : true,
				fieldLabel: sLabel,
				width:60,
				emptyText : '选择业务类型'
			});
		}else if(sType=="qsfsComboBox"){
			var dict_1=$dictList("QSFS");
			var sjlxds = new Ext.data.Store( {
				data: {rows:dict_1},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
				}, ['key', 'value', 'id'])
			});
			var data={'key':'全部','value':''};
			var record=new Ext.data.Record(data);
			sjlxds.insert(0,record);
			oSearchField = new Ext.form.ComboBox( {
				hiddenName : sId,// 真正接受的名字
				store : sjlxds,// 数据源
				fieldLabel : '取数方式',
				displayField : 'key',// 数据显示列名
				valueField : 'value',
				mode : 'local',// 默认以'remote'作为数据源
				triggerAction : 'all',// 单击下拉按钮时激发事件
				typeAhead : true,// 自动完成功能
				editable :false,
				selectOnFocus : true,
				fieldLabel: sLabel,
				width:60,
				emptyText : '选择取数方式'
			});
		}
		tmpPanel=new Ext.Panel({labelWidth:70,layout:'form',columnWidth:.25,id:'SFP#'+sId,
			items:[oSearchField]
		});
		
	 	oPanel.add(tmpPanel);
	 	if(typeof(bHide)=='boolean' && bHide==true){
	 		tmpPanel.hide();
	 	}
	}
});
