
/**
 * 查询面板
 */
SearchPanel = function(config) {
	
	Ext.apply(config,{txtWidth:150});
	
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var startDt=null;
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql = "";
		var flag = false;
		var ddj = 0;
		var xdj = 0;
		var sl = 0;
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
						 
		 			//其他字段模糊查询
		 			}else  if(temp.id.split("#")[1]=="ckmc"){
	 					queryParamsSql+=" t.wzdagl.kw.ckid = '"+encodeURIComponent(tempValue)+"' and "
		 			}else if(temp.id.split("#")[1]=="zksj"){
	 					queryParamsSql+=" MONTHS_BETWEEN(sysdate,t.rksj)>= "+encodeURIComponent(tempValue)+" and "
		 			}else if(temp.id.split("#")[1]=="ddj"){
		 				ddj = encodeURIComponent(tempValue);
		 			} else if(temp.id.split("#")[1]=="xdj"){
		 				xdj = encodeURIComponent(tempValue);
		 			}else if(temp.id.split("#")[1]=="sl"){
						sl = encodeURIComponent(tempValue);
		 			}
	 			}
	 		};
	 	});
	 	var url = link1+"?a=1";
	 	if(queryParamsSql!=""){
	 		url = url+"&parWhere="+queryParamsSql.substring(0,queryParamsSql.length-5);
	 	}
	 	if(ddj!=0){
	 		url = url+"&ddj="+ddj;
	 	}
	 	if(xdj!=0){
	 		url = url+"&xdj="+xdj;
	 	}
	 	if(sl!=0){
	 		url = url+"&sl="+sl;
	 	}
	 	parWhere = "?"+url.split("?")[1];
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
						 
					};
				});
		 
		rightGrid.changeToListDS(link1);
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
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth()+1;
			var defdate = year+'年'+month+'月';
			oSearchField =new Ext.app.DateField({fieldLabel : sLabel,format : 'Y年m月',width:90,id:sId,labelStyle:'width:70',value:defdate});
		}else if(sType=="dateFieldShowTime"){
			oSearchField =new Ext.form.DateField({fieldLabel: sLabel,format:'Y-m-d H:i',labelStyle:'width:70',width:this.txtWidth,id:sId,menu:new DatetimeMenu()});
		}else if(sType=="comboBox"){
			var arrays = searchField.split("#");
			//评级级别数据源
			var url=contextPath + "/jteap/wz/ckgl/CkglAction!getCkmc.do";
			var storeLevel = new Ext.data.JsonStore({
				url : url,
				root:'data',
				fields:['id','ckmc']
			})
			//评级级别
			if(arrays[1] == 'ckmc'){
				oSearchField = new Ext.form.ComboBox({
					id: sId,
					fieldLabel : sLabel,
					mode: 'local',
					triggerAction: 'all',
					emptyText: '请选择仓库',
					width : this.txtWidth,
					forceSelection: true,
					editable: false,
					allowBlank: true,
					store: storeLevel,
					valueField :'id',
					displayField:'ckmc'
				});
				storeLevel.load();
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
