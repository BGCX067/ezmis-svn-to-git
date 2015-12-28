var SearchPanel = function(config){
	var search = this;
	// 点击查询后的事件
	this.searchClick = function() {
		var oPanel = searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		//var queryParamsJson = {};
		var keyList="";
		var radioValue="1";
		Ext.each(oItems, function(oItem) {
			if (oItem.hidden == false) {
				var temp = oItem.items.items[0];
				var tempValue = temp.getValue();
				// 值不为空才作为参数
				if (tempValue != null && tempValue != "") {
					var name=temp.getName();
					if(name.indexOf("#")>=0){
						name=name.split("#")[1];
						keyList=keyList+name+","+tempValue+"!";
					}else if(name=="radioSel"){
						radioValue=temp.getValue();
					}
				}
			};
		});
		if(keyList!=""){
			keyList=keyList.substring(0,keyList.length-1);
		}
		var param={};
		param.keyList=keyList;
		param.kid=kid;
		param.item=item;
		param.flflag=flflag;
		param.chartType=radioValue;

		AjaxRequest_Sync(link10, param, function(req) {
			var responseText=req.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
				renderChart(responseObject.chartData,responseObject.tableData,radioValue);
 			}else{
 				alert(responseObject.msg);
 			}
		});
	};
	this.back=function(){
		$("backForm").submit();
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
					icon : contextPath+'/jteap/jhtj/zbqsfx/icon/icon_9.gif',
					listeners : {
						click : this.searchClick
								? this.searchClick
								: Ext.emptyFn
					}
				},{
					text : '返回',
					cls : 'x-btn-text-icon',
					icon : contextPath+'/jteap/jhtj/zbqsfx/icon/icon_8.gif',
					listeners : {
						click : this.back
					}
				}]
	};
	SearchPanel.superclass.constructor.call(this,pConfig);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
	appendSearchField:function(searchField,bHide){
		var thisx=this;
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
	 			oSearchField=new Ext.form.TextField({fieldLabel: sLabel,width:60,id:sId});
			}else if(sType=="comboBox"){
				var flflag=tmp[3];
				var type=tmp[4];
				var hideLabel=tmp[5];
				var columnwidth=tmp[6];
				if(hideLabel=="true"){
					hideLabel=true;
				}else{
					hideLabel=false;
				}
				var ds = new Ext.data.Store( {
					proxy : new Ext.data.ScriptTagProxy( {
						url : link7+"?flflag="+flflag
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
					fieldLabel: (sLabel==""?"":sLabel),
					width : 60,
					readOnly:true,
					hideLabel :hideLabel,
					mode : 'local',// 默认以'remote'作为数据源
					triggerAction : 'all',// 单击下拉按钮时激发事件
					typeAhead : true,// 自动完成功能
					selectOnFocus : true
				});
				var loadParam={};
				loadParam.flflag=flflag;
				loadParam.kid=kid;
				AjaxRequest_Sync(link7,loadParam,function(request){
					var responseT = request.responseText;
					var responseO = responseT.evalJSON();
					if(responseO.success == true){
						var data={};
						if(type=="1"){
							data=responseO.SNIAN[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.SNIANvalue);
								oSearchField.on("select",function(combo,record,index){
									var val=combo.getValue();
									thisx.setDay("SNIAN",val);
								}) ;
							}else{
								oSearchField=null;
							}
						}else if(type=="2"){
							data=responseO.SYUE[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.SYUEvalue);
								oSearchField.on("select",function(combo,record,index){
									var val=combo.getValue();
									thisx.setDay("SYUE",val);
								}) ;
							}else{
								oSearchField=null;
							}
						}else if(type=="3"){
							data=responseO.SRI[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.SRIvalue);
							}else{
								oSearchField=null;
							}
						}else if(type=="4"){
							data=responseO.ENIAN[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.ENIANvalue);
								oSearchField.on("select",function(combo,record,index){
									var val=combo.getValue();
									thisx.setDay("ENIAN",val);
								}) ;
							}else{
								oSearchField=null;
							}
						}else if(type=="5"){
							data=responseO.EYUE[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.EYUEvalue);
								oSearchField.on("select",function(combo,record,index){
									var val=combo.getValue();
									thisx.setDay("EYUE",val);
								}) ;
							}else{
								oSearchField=null;
							}
						}else if(type=="6"){
							data=responseO.ERI[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.ERIvalue);
							}else{
								oSearchField=null;
							}
						}else if(type=="7"){
							data=responseO.JZ[0];
							if(data!=null){
								ds.loadData(data,true);
								oSearchField.setValue(responseO.JZvalue);
								oSearchField.setWidth(72);
							}else{
								oSearchField=null;
							}
						}
					}
				});
			}else if(sType=="radio"){
				columnwidth=tmp[3];
				value=tmp[4];
				oSearchField = new Ext.form.RadioGroup({   
                            hideLabel :true,  
                            items : [new Ext.form.Radio({   
	                                    name : "radioSel",   
	                                    inputValue : "1",   
	                                    boxLabel : "折线图",   
	                                    width:'60',
	                                    checked:true,
	                                    listeners : {   
	                                        check : this.radiochange   
	                                    }   
                                     }),   
                                     new Ext.form.Radio({   
                                        name : "radioSel",   
                                        inputValue : "2",   
                                        boxLabel : "柱状图", 
                                        width:'100',
                                        listeners : {   
                                            check : this.radiochange   
                                        }   
                                     })]
				}); 
			}
			tmpPanel=new Ext.Panel({labelWidth:50,layout:'form',columnWidth:columnwidth,id:'SFP#'+sId,
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
	},
	setDay:function(selName,selValue){
		var thisx=this;
		if(selName=="SNIAN"||selName=="SYUE"){
			var sri=$("sf#SRI");
			if(sri!=null){
				var param={};
				param.curDate=$("sf#SNIAN").value+"-"+$("sf#SYUE").value+"-1";
				AjaxRequest_Sync(link9,param,function(lastDateRes){
					var lastDateText = lastDateRes.responseText;
					var lastDateObj = lastDateText.evalJSON();
					if(lastDateObj.success){
						var lastDate=lastDateObj.dayList[0];
						if(lastDate!=""){
							var oPanel = thisx.items.get(0);
							var oItems = oPanel.items.items;
							Ext.each(oItems, function(oItem) {
								var temp = oItem.items.items[0];
								var name=temp.getName();
								if(name=="sf#SRI"){
									temp.store.removeAll();
									temp.store.loadData(lastDate,true);
								}
							});
						}else{
							alert("初始日期失败!");
						}
					}
				});
			}
		}else if(selName=="ENIAN"||selName=="EYUE"){
			var sri=$("sf#ERI");
			if(sri!=null){
				var param={};
				param.curDate=$("sf#ENIAN").value+"-"+$("sf#EYUE").value+"-1";
				AjaxRequest_Sync(link9,param,function(lastDateRes){
					var lastDateText = lastDateRes.responseText;
					var lastDateObj = lastDateText.evalJSON();
					if(lastDateObj.success){
						var lastDate=lastDateObj.dayList[0];
						if(lastDate!=""){
							var oPanel = thisx.items.get(0);
							var oItems = oPanel.items.items;
							Ext.each(oItems, function(oItem) {
								var temp = oItem.items.items[0];
								var name=temp.getName();
								if(name=="sf#ERI"){
									temp.store.removeAll();
									temp.store.loadData(lastDate,true);
								}
							});
						}else{
							alert("初始日期失败!");
						}
					}
				});
			}
		}
	},
	radiochange:function(checkbox,checked){
		var thisx=this;
		if(checkbox.getValue()){
			searchtemp.searchClick();
		}
	}
});
