var xhgg = (xhggForQuery=='')?'':" ='"+xhggForQuery+"'";
var SearchPanel = function(config){
	this.clearSearch = function(){
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			//var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(temp){
		 			temp.setValue('');
	 			}
	 		}
	 	});
	}
	//点击查询后的事件
	this.searchClick=function(){
		var oPanel=searchtemp.items.get(0);
		var dtStart=null;
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql='';
		var start ="";
		var end = "";
		var wzmc = "";
		var wzdamc = "";
		var xhggmc = "";
		var flag = false;
		var hasSearchCondition = false;
	 	Ext.each(oItems,function(oItem){
	 		
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
	 				hasSearchCondition=true;
		 			//日期字段
		 			if(temp.triggerClass=="x-form-date-trigger"){
		 				tempValue = formatDate(tempValue,"yyyy-MM-dd");
		 				if(temp.id.split("#")[1]=="Start"){
		 					dtStart = tempValue;
		 					start = tempValue;
		 					//queryParamsSql+="to_char(obj.crksj,'yyyy-mm-dd')>='"+tempValue+"' and ";
		 				}else if(temp.id.split("#")[1]=="End"){
		 					//queryParamsSql+="to_char(obj.crksj,'yyyy-mm-dd')<='"+tempValue+"' and ";
		 					end = tempValue;
		 					if(dtStart!=null){
		 						if(dtStart>tempValue){
		 							alert("发放日期不能大于到期日期");
		 							flag=true;
		 							dtStart=null;
		 							return false;
		 						}
		 					}
		 				}
		 			}else if(temp.id.split("#")[1]=="xhgg"){
	 					xhgg = " like '"+encodeURIComponent("$"+tempValue+"$")+"'";
	 					xhggmc = encodeURIComponent(tempValue);
		 			}else if(temp.id.split("#"[1]=="wz_replace")){
		 				//其他字段查询
		 				//"obj."+temp.id.split("#")[1]+
		 				wzmc = " like '"+encodeURIComponent("$"+tempValue+"$")+"'";
		 				wzdamc = encodeURIComponent(tempValue);
		 			}else{
		 				queryParamsSql+=" like '"+encodeURIComponent("$"+tempValue+"$")+"'";
		 			}
	 			}
	 			/**
	 			else{
	 				 if(temp.id.split("#")[1]== 'wz_replace' && wzmc ==''){
	 					alert('必须选择物资');
	 					return false;
	 				 }
 				}
 				**/
	 		};
	 	});
	 	if(flag){
	 		return;
	 	}
	 	var url=link16;
	 	//if(hasSearchCondition && wzmc!='') {
	 	if(hasSearchCondition) {
	 		var cgdt = "";		//采购时间
 			var yhdt = "";		//验货时间
 			var lydt = "";		//领用时间
 			var dr_dc_dt = "";	//调出调入时间
 			var tldt = "";		//退料时间
 			var xsdt = "";		//销售时间
 			if(start!=''){
 				cgdt = " and to_char(obj.cgjhgl.sxsj,'yyyy-mm-dd')>='"+start+"' ";
 				yhdt = " and to_char(obj.yhdgl.ysrq,'yyyy-mm-dd')>='"+start+"' ";
 				lydt = " and to_char(obj.lydgl.lysj,'yyyy-mm-dd')>='"+start+"' ";
 				dr_dc_dt = " and to_char(obj.dbd.dbsj,'yyyy-mm-dd')>='"+start+"' ";
 				tldt = " and to_char(obj.tldgl.tlsj,'yyyy-mm-dd')>='"+start+"' ";
 				xsdt = " and to_char(obj.wzxsd.xsrq,'yyyy-mm-dd')>='"+start+"' ";
 			}
 			if(end!=''){
 				cgdt += " and to_char(obj.cgjhgl.sxsj,'yyyy-mm-dd')<='"+end+"' ";
 				yhdt += " and to_char(obj.yhdgl.ysrq,'yyyy-mm-dd')<='"+end+"' ";
 				lydt += " and to_char(obj.lydgl.lysj,'yyyy-mm-dd')<='"+end+"' ";
 				dr_dc_dt += " and to_char(obj.dbd.dbsj,'yyyy-mm-dd')<='"+end+"' ";
 				tldt += " and to_char(obj.tldgl.tlsj,'yyyy-mm-dd')<='"+end+"' ";
 				xsdt += " and to_char(obj.wzxsd.xsrq,'yyyy-mm-dd')<='"+end+"' ";
 			}
 			
	 		wzdaTabPanel.getItem('fpGrid').doLayout();
	 		//wzdaTabPanel.doLayout();
	 		/***
	 		if(xhgg == ''){
	 			url=link16.split('?')[0]+"?queryParamsSql=obj.wzmc "+queryParamsSql;
	 		}else{
		 		url=link16.split('?')[0]+"?queryParamsSql=obj.wzmc "+queryParamsSql+" and obj.xhgg"+xhgg;
	 		}
	 		**/
	 		var wzdaUrl = link16.split('?')[0]+"?queryParamsSql=";
	 		var wzdaFlag = false;
	 		if(wzmc != ''){
		 		wzdaUrl+="obj.wzmc"+wzmc;
		 		wzdaFlag = true;
	 		}
	 		if(xhgg != ''){
	 			if(wzdaFlag){
	 				wzdaUrl+=" and obj.xhgg"+xhgg;
	 			}else{
	 				wzdaUrl+="obj.xhgg"+xhgg;
	 			}
	 		}
	 		
	 		wzdaGrid.changeToListDS(wzdaUrl);
 			wzdaGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("wzCenter").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("wzCenter").setTitle("*"+wzdaTabPanel.getItem("wzCenter").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("wzCenter").setTitle(wzdaTabPanel.getItem("wzCenter").title.substring(1));
 				}
 			}});
 			
 			/**
 			if(xhgg == ''){
 				rkGrid.changeToListDS(link17.split('?')[0]+"?queryParamsSql=obj.crkrzgl.crkqf='1' and obj.wzda.wzmc "+queryParamsSql);
 			}else{
	 			rkGrid.changeToListDS(link17.split('?')[0]+"?queryParamsSql=obj.crkrzgl.crkqf='1' and obj.wzda.wzmc "+queryParamsSql+" and obj.wzda.xhgg"+xhgg);
 			}
 			**/
 			/**
 			var rkUrl = link17.split('?')[0]+"?queryParamsSql=obj.crkrzgl.crkqf='1'";
	 		if(wzmc != ''){
	 			rkUrl+=" and obj.wzda.wzmc"+wzmc;
	 		}
 			if(xhgg != ''){
	 			rkUrl+=" and obj.wzda.xhgg"+xhgg;
	 		}
 			rkGrid.changeToListDS(rkUrl);
 			rkGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("rkGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("rkGrid").setTitle("*"+wzdaTabPanel.getItem("rkGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("rkGrid").setTitle(wzdaTabPanel.getItem("rkGrid").title.substring(1));
 				}
 			}});
 			**/
 			/**
 			if(xhgg == ''){
 				fpGrid.changeToListDS(link18.split('?')[0]+"?wzmc="+wzmc);
 			}else{
	 			fpGrid.changeToListDS(link18.split('?')[0]+"?wzmc="+wzmc+" and xhgg"+xhgg);
 			}
 			**/
 			var fpUrl = link18.split('?')[0]+"?";
 			var fpFlag = false;
	 		if(wzmc != ''){
	 			fpUrl += "wzmc="+wzdamc;
	 			fpFlag = true;
	 		}
 			if(xhgg != ''){
 				if(fpFlag){
	 				fpUrl += "&xhgg="+xhggmc;
	 			}else{
	 				fpUrl += "xhgg="+xhggmc;
	 			}
	 		}
 			fpGrid.changeToListDS(fpUrl);
 			fpGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("fpGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("fpGrid").setTitle("*"+wzdaTabPanel.getItem("fpGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("fpGrid").setTitle(wzdaTabPanel.getItem("fpGrid").title.substring(1));
 				}
 			}});
 			
 			/**
 			if(xhgg == ''){
 				xqGrid.changeToListDS(link19.split('?')[0]+"?wzmc="+wzmc+"&dtstart="+start+"&dtend="+end);
 			}else{
	 			xqGrid.changeToListDS(link19.split('?')[0]+"?wzmc="+wzmc+"&dtstart="+start+"&dtend="+end+" and xhgg"+xhgg);
 			}
 			**/
 			var xqUrl = link19.split('?')[0]+"?dtstart="+start+"&dtend="+end;
 			if(wzmc != ''){
 				xqUrl += "&wzmc="+wzdamc;
 			}
 			if(xhgg != ''){
 				xqUrl += "&xhgg="+xhggmc;
 			}
 			xqGrid.changeToListDS(xqUrl);
 			xqGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("xqGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("xqGrid").setTitle("*"+wzdaTabPanel.getItem("xqGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("xqGrid").setTitle(wzdaTabPanel.getItem("xqGrid").title.substring(1));
 				}
 			}});
 			
 			/**
 			if(xhgg == ''){
 				cgGrid.changeToListDS(link20.split('?')[0]+"?queryParamsSql=obj.wzdagl.wzmc "+queryParamsSql+cgdt);
 			}else{
	 			cgGrid.changeToListDS(link20.split('?')[0]+"?queryParamsSql=obj.wzdagl.wzmc "+queryParamsSql+cgdt+" and obj.wzdagl.xhgg"+xhgg);
 			}
 			**/
 			var cgUrl = link20.split('?')[0]+"?queryParamsSql=";
 			var cgFlag = false;
 			if(wzmc != ''){
 				cgUrl += "obj.wzdagl.wzmc"+wzmc;
 				cgFlag = true;
 			}
 			if(xhgg != ''){
 				if(cgFlag){
 					cgUrl += " and obj.wzdagl.xhgg"+xhgg;
 				}else{
 					cgUrl += "obj.wzdagl.xhgg"+xhgg;
 				}
 			}
 			/////////
 			if(wzmc != '' || xhgg != ''){
 				cgUrl += cgdt;
 			}
 			cgGrid.changeToListDS(cgUrl);
 			cgGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("cgGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("cgGrid").setTitle("*"+wzdaTabPanel.getItem("cgGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("cgGrid").setTitle(wzdaTabPanel.getItem("cgGrid").title.substring(1));
 				}
 			}});
 			
 			/**
 			if(xhgg == ''){
 				slGrid.changeToListDS(link22.split('?')[0]+"?queryParamsSql=obj.wzdagl.wzmc "+queryParamsSql + " and obj.zt='1'"+yhdt);
 			}else{
	 			slGrid.changeToListDS(link22.split('?')[0]+"?queryParamsSql=obj.wzdagl.wzmc "+queryParamsSql + " and obj.zt='1'"+yhdt+" and obj.wzdagl.xhgg"+xhgg);
 			}
 			**/
 			var slUrl = link22.split('?')[0]+"?queryParamsSql=obj.zt='1'"+yhdt;
 			if(wzmc != ''){
 				slUrl += " and obj.wzdagl.wzmc"+wzmc;
 			}
 			if(xhgg != ''){
 				slUrl += " and obj.wzdagl.xhgg"+xhgg;
 			}
 			slGrid.changeToListDS(slUrl);
 			slGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("slGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("slGrid").setTitle("*"+wzdaTabPanel.getItem("slGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("slGrid").setTitle(wzdaTabPanel.getItem("slGrid").title.substring(1));
 				}
 			}});
 			
 			/**
 			if(xhgg == ''){
 				llGrid.changeToListDS(link24.split('?')[0]+"?queryParamsSql=obj.wzbm.wzmc "+queryParamsSql + " and obj.zt='1'"+lydt);
 			}else{
	 			llGrid.changeToListDS(link24.split('?')[0]+"?queryParamsSql=obj.wzbm.wzmc "+queryParamsSql + " and obj.zt='1'"+lydt+" and obj.wzbm.xhgg"+xhgg);
 			}
 			**/
 			var llUrl = link24.split('?')[0]+"?queryParamsSql=obj.zt='1'"+lydt;
 			if(wzmc != ''){
 				llUrl += " and obj.wzbm.wzmc"+wzmc;
 			}
 			if(xhgg != ''){
 				llUrl += " and obj.wzbm.xhgg"+xhgg;
 			}
 			llGrid.changeToListDS(llUrl);
 			llGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("llGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("llGrid").setTitle("*"+wzdaTabPanel.getItem("llGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("llGrid").setTitle(wzdaTabPanel.getItem("llGrid").title.substring(1));
 				}
 			}});
 			
 			/**
 			var drUrl = link26.split('?')[0]+"?queryParamsSql=obj.dbd.zt='1' and obj.dbd.dbqf='1'"+dr_dc_dt;
 			if(wzmc != ''){
 				drUrl += " and obj.wzda.wzmc"+wzmc;
 			}
 			if(xhgg != ''){
 				drUrl += " and obj.wzda.xhgg"+xhgg;
 			}
 			drGrid.changeToListDS(drUrl);
 			drGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("drGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("drGrid").setTitle("*"+wzdaTabPanel.getItem("drGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("drGrid").setTitle(wzdaTabPanel.getItem("drGrid").title.substring(1));
 				}
 			}});
 			
 			var dcUrl = link26.split('?')[0]+"?queryParamsSql=obj.dbd.zt='1' and obj.dbd.dbqf='2'"+dr_dc_dt;
 			if(wzmc != ''){
 				drUrl += " and obj.wzda.wzmc"+wzmc;
 			}
 			if(xhgg != ''){
 				drUrl += " and obj.wzda.xhgg"+xhgg;
 			}
 			dcGrid.changeToListDS(dcUrl);
 			dcGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("dcGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("dcGrid").setTitle("*"+wzdaTabPanel.getItem("dcGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("dcGrid").setTitle(wzdaTabPanel.getItem("dcGrid").title.substring(1));
 				}
 			}});
 			
 			var tlUrl = link28.split('?')[0]+"?queryParamsSql=obj.tldgl.zt='1'"+ tldt;
 			if(wzmc != ''){
 				tlUrl += " and obj.wzda.wzmc"+wzmc;
 			}
 			if(xhgg != ''){
 				tlUrl += " and obj.wzda.xhgg"+xhgg;
 			}
 			tlGrid.changeToListDS(tlUrl);
 			tlGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("tlGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("tlGrid").setTitle("*"+wzdaTabPanel.getItem("tlGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("tlGrid").setTitle(wzdaTabPanel.getItem("tlGrid").title.substring(1));
 				}
 			}});
 			
 			var xsUrl = link30.split('?')[0]+"?queryParamsSql=obj.wzxsd.zt='1'"+xsdt;
 			if(wzmc != ''){
 				xsUrl += " and obj.wzda.wzmc"+wzmc;
 			}
 			if(xhgg != ''){
 				xsUrl += " and obj.wzda.xhgg"+xhgg;
 			}
 			xsGrid.changeToListDS(xsUrl);
 			xsGrid.getStore().reload({callback:function(r){
 				var currTitle = wzdaTabPanel.getItem("xsGrid").title;
 				if(r.length>0 && currTitle.indexOf("*")<0){
 					wzdaTabPanel.getItem("xsGrid").setTitle("*"+wzdaTabPanel.getItem("xsGrid").title);
 				}else if(r.length<1 && currTitle.indexOf("*")>=0){
 					wzdaTabPanel.getItem("xsGrid").setTitle(wzdaTabPanel.getItem("xsGrid").title.substring(1));
 				}
 			}});
 			**/
	 	}
			 	
	};
	SearchPanel.superclass.constructor.call(this,config);
}
Ext.extend(SearchPanel, Ext.app.SearchPanel, {
		appendSearchField : function(searchField, bHide) {
			var sLabel = searchField;
			var sId = "sf#" + searchField;
			var sType = searchField;
			if (searchField.indexOf("#") >= 0) {
				var tmp = searchField.split("#");
				sLabel = tmp[0];
				sId = 'sf#' + tmp[1];
				sType = tmp[2]
			}
			var oPanel = this.items.get(0);
			var oSearchField = null;
		if (sType == "textField") { // 文本类型
			var v = "";
			if(sLabel=='型号规格') v = xhggForQuery;
			oSearchField = new Ext.form.TextField( {
				id : sId,
				fieldLabel : sLabel,
				readOnly : false,
				value : v,
				anchor : '90%'
			});
		}if(sType == "wzdagl"){
			var v = "";
			if(sLabel=='物资名称') v = wzmcForQuery;
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				readOnly :false,
				value :v,
				onTriggerClick:function(){
					var field = this;
					var url = contextPath+"/jteap/wz/wzlb/selectWzdaIndex.jsp";
					var result = showModule(url, true, 850, 500,'singleSelect|1');
					if (result != null) {
						//this.displayValue=result.wzmc;
          				//this.setValue(result.id);
						//alert(this.id);
						this.setValue(result.wzmc);
						new Ext.getCmp('sf#xhgg').setValue(result.xhgg);
					} else this.reset();
				},
				anchor : '90%'
			});
		} else if (sType == "dateField") { // 日期类型
			oSearchField = new Ext.form.DateField( {
				id : sId,
				fieldLabel : sLabel,
				format : 'Y-m-d',
				anchor : '90%'
			});
		} else if (sType == "dateFieldShowTime") {
			oSearchField = new Ext.form.DateField( {
				fieldLabel : sLabel,
				format : 'Y-m-d H:i',
				anchor : '90%',
				id : sId,
				menu : new DatetimeMenu()
			});
		} else if (sType == "Czr") {
			oSearchField = new Ext.app.TriggerField({
				id : sId,
				fieldLabel : sLabel,
				onTriggerClick:function(a,b,c){
					var url = contextPath+"/jteap/wz/grgqjtz/personSelect.jsp";
             		var result = showModule2(url, true, 700, 600,{});
             		if (result != null) {
          				this.setValue(result.name);
           			}
				},
				anchor : '90%'
			});
		} else if (sType == "comboCkgl") {
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype:'combo', 
				store:new Ext.data.Store({
					baseParams:{PAGE_FLAG:'PAGE_FLAG_NO'},
					autoLoad : true, 
					proxy: new Ext.data.ScriptTagProxy({url : link3}),
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
		} else if(sType=="comboZt"){
			var cgjhmxList=$dictList("CGJHMX");   
			var comboStatusStore = new Ext.data.Store({
				data: {rows:cgjhmxList},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
					}, ['key', 'value', 'id'])
			});
		
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype : 'combo',
				store : comboStatusStore,
				valueField : "value",
				displayField : "key",
				mode : 'local',
				triggerAction: 'all', 
				forceSelection : true,
				blankText : '',
				emptyText : '',
		//		hiddenName : '',
				editable : false,
		//		name : '',
				anchor : '90%'
			});
		} else if(sType=="comboRkrzlb"){
			var rklbList=$dictList("RKRZLB");   
			var comboStatusStore = new Ext.data.Store({
				data: {rows:rklbList},
				reader : new Ext.data.JsonReader( {
					root : 'rows',
					id : 'id'
					}, ['key', 'value', 'id'])
			});
			oSearchField = new Ext.form.ComboBox({  
				id : sId,
				fieldLabel : sLabel,
				xtype : 'combo',
				store : comboStatusStore,
				valueField : "value",
				displayField : "key",
				mode : 'local',
				triggerAction: 'all', 
				forceSelection : true,
				blankText : '',
				emptyText : '',
		//		hiddenName : '',
				editable : false,
		//		name : '',
				anchor : '90%'
			});
		}
		tmpPanel = new Ext.Panel( {
			labelWidth : this.labelWidth,
			layout : 'form',
			columnWidth : .3,
			id : 'SFP#' + sId,
			items : [oSearchField]
		});
	
		oPanel.add(tmpPanel);
		if (typeof(bHide) == 'boolean' && bHide == true) {
			tmpPanel.hide();
		}
	}
});
