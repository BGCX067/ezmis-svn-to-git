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
					if (temp.getName().split("#")[1] == "NIAN") {
						queryParamsSql=queryParamsSql+"NIAN,"+tempValue+"!";
					} else if(temp.getName().split("#")[1] == "YUE"){
						queryParamsSql=queryParamsSql+"YUE,"+tempValue+"!";
					}
				}
			};
		});
		if(queryParamsSql!=""){
			queryParamsSql=queryParamsSql.substring(0,queryParamsSql.length-1);
			var oNode=leftTree.getSelectionModel().getSelectedNode();
			var flflag=oNode.attributes.flflag;
	    	var kid=oNode.attributes.kid;
	    	
	    	if(flflag==4){
				leftTree.initTable(kid,oNode.id,queryParamsSql);
			}else{
				leftTree.initTable(kid,"",queryParamsSql);
			}
		}
	};
	
	
	this.back=function(){
	};
	
	var pConfig = {
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
		bbar : [{
					text : '查询',
					cls : 'x-btn-text-icon',
					icon : 'icon/icon_8.gif',
					listeners : {
						click : this.searchClick
								? this.searchClick
								: Ext.emptyFn
					}
				}]
	};
	SearchPanel.superclass.constructor.call(this,pConfig);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	appendSearchField:function(searchField,bHide){
		if(searchField!=""){
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
			}else if(sType=="comboBox"){
				var flflag=tmp[3];
				var type=tmp[4];
				var ds = new Ext.data.Store( {
					proxy : new Ext.data.ScriptTagProxy( {
						url : link20+"?flflag="+flflag
					}),
					reader : new Ext.data.JsonReader( {
					}, ['value', 'displayValue']),
					remoteSort : true
				});
				var oSearchField = new Ext.form.ComboBox( {
					hiddenName : sId,// 真正接受的名字
					store : ds,// 数据源
					displayField : 'displayValue',// 数据显示列名
					valueField : 'value',
					fieldLabel: sLabel,
					width : 80,
					readOnly:true,
					mode : 'local',// 默认以'remote'作为数据源
					triggerAction : 'all',// 单击下拉按钮时激发事件
					typeAhead : true,// 自动完成功能
					selectOnFocus : true
				});
				var loadParam={};
				loadParam.flflag=flflag;
				AjaxRequest_Sync(link20,loadParam,function(request){
					var responseT = request.responseText;
					var responseO = responseT.evalJSON();
					if(responseO.success == true){
						var data={};
						if(type=="1"){
							data=responseO.NIAN[0];
							ds.loadData(data,true);
							oSearchField.setValue(responseO.NIANvalue);
						}else if(type=="2"){
							data=responseO.YUE[0];
							ds.loadData(data,true);
							oSearchField.setValue(responseO.YUEvalue);
						}			
					}
				});
			}
			tmpPanel=new Ext.Panel({labelWidth:20,layout:'form',columnWidth:.2,id:'SFP#'+sId,
				items:[oSearchField]
			});
			
		 	oPanel.add(tmpPanel);
		 	if(typeof(bHide)=='boolean' && bHide==true){
		 		tmpPanel.hide();
		 	}
		 }
	},
	dataStore:function(){
		var ds = new Ext.data.Store( {
			proxy : new Ext.data.ScriptTagProxy( {
				url : link20
			}),
			reader : new Ext.data.JsonReader( {
			}, ['displayValue', 'value']),
			remoteSort : true
		});
		return ds;
	}
});
