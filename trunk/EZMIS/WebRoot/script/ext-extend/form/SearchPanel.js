Ext.app.SearchPanel = function(config){
	
	searchtemp = this;
	//定义一个菜单
	this.searchItemMenu = new Ext.menu.Menu({
		id:'searchItemMenu'
	});
	Ext.apply(this,config);
	if(!config.labelWidth){
		this.labelWidth = 55;
	}
	
	if(!config.txtWidth){
		this.txtWidth = 100;
	}
	
	var pConfig={title: '查询面板',
		region:'north',
	    width: 600,
	    frame:true,
	    margins:'1px 1px 1px 1px',
	    bodyStyle:'padding:5px',
	    fitToFrame:true,
	    autoHeight :true,
	    split:true,
		collapsible: true,
		//collapsed:true,
    	items:[{
    		layout:'column',
    		labelSeparator:' ：'
    	}],
    	
    	bbar:[{text:'添加条件',cls: 'x-btn-text-icon',
       		icon:'icon/icon_9.gif',menu:this.searchItemMenu},{text:'查询',        cls: 'x-btn-text-icon',
       		icon:'icon/icon_8.gif',listeners:{click:this.searchClick?this.searchClick:Ext.emptyFn}
	       	},{text:'清除条件', cls: 'x-btn-text-icon',
	       		icon:'icon/trash-delete.gif',listeners:{click:this.clearSearch?this.clearSearch:Ext.emptyFn}
	       	}]
    };
    Ext.apply(pConfig,config);
    
    Ext.app.SearchPanel.superclass.constructor.call(this, pConfig);
    
    //初始化查询面板
    this.initSearchFieldPanel();
    
    //初始化查询字段菜单
    this.initSearchFieldMenu();
    
 	this.on("resize",function(){
 		
 	})
}

Ext.extend(Ext.app.SearchPanel, Ext.FormPanel, {
	/**
	 * 初始化查询面板
	 */
	initSearchFieldPanel:	function(){
		var thisx=this;
		Ext.each(this.searchAllFs,function(f){
			var bTmp=true;
			
			if(thisx.searchDefaultFs.indexOf(f)>=0)
				bTmp=false;
			thisx.appendSearchField(f,bTmp);
		});
		
	},
	/**
	 * 初始化条件菜单
	 * 菜单相的编号以'mi_'为前缀+查询字段的ID
	 */
	 initSearchFieldMenu:	function(){
	 	var menu=this.searchItemMenu;
	 	var thisx=this;
	 	Ext.each(this.searchAllFs,function(f){
	 		var sLabel=f;
		 	var sId='mi#'+f;
		 	if(f.indexOf("#")>=0){
		 		var tmp=f.split("#");
		 		sLabel=tmp[0];
		 		sId='mi#'+tmp[1];
		 	}
		 	
	 		var bChecked=thisx.searchDefaultFs.indexOf(f)>=0;
	 		//添加菜单项
	 		menu.add({
	 			id:sId,text: sLabel,checked:bChecked,
	 			//菜单项点击事件
                checkHandler: function(oItem,bChecked){
                	var oPanel=thisx.items.get(0);
                	var sId=oItem.id.split("#")[1];
                	var oItem=oPanel.items.get("SFP#sf#"+sId);
                	if(!bChecked){
                		oItem.hide();
                	}else{
                		oItem.show();
                	}
                	var oParent=thisx.ownerCt;
                	var oLayout=oParent.getLayout();
                	oLayout.layout();
                	
                }
            });
            //thisx.appendSearchField("人_ss");
           
	 	})
	 },
	 /**
	  * 向查询面板中追加一个查询字段
	  * @searchField (String) 查询字段的名称,查询字段名称可以是具有一定格式的规则字符串
	  * @bHide	(boolean) 	确定是否添加是是以隐藏状态隐藏的
	  * 
	  * eg：'姓名_name'
	  * 将会生成标签为“姓名”，ID为"sf_name"的文本框，其中"sf_"为查询字段的前缀
	  * 
	  * eg: '姓名'
	  * 将会生成标签为“姓名”，ID为"sf_姓名"的文本框
	  */
	 appendSearchField:		function(searchField,bHide){
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
			//对combox进行扩充 2009-12-21
			var tmp=searchField.split("#");
			var displayField=tmp[3];
		 	var valueField=tmp[4];
		 	var emptyText=tmp[5];
		 	var rev=this.dataStore;
			oSearchField=new Ext.form.ComboBox( {
				hiddenName : sId,// 真正接受的名字
				fieldLabel : sLabel,
				store:this.dataStore(),
				displayField : displayField,// 数据显示列名
				valueField : valueField,
				mode : 'remote',// 默认以'remote'作为数据源
				triggerAction : 'all',// 单击下拉按钮时激发事件
				typeAhead : true,// 自动完成功能
				selectOnFocus : true,
				emptyText : emptyText,
				listeners:{
					select:this.selectLoadData
				}
			});
		}
		tmpPanel=new Ext.Panel({labelWidth:this.labelWidth,layout:'form',columnWidth:.3,id:'SFP#'+sId,
			bodyStyle: 'border:0px',
			items:[oSearchField]
		});
		
	 	oPanel.add(tmpPanel);
	 	if(typeof(bHide)=='boolean' && bHide==true){
	 		tmpPanel.hide();
	 	}

	 },
	 /**
	  * 在查询面板对象中查找出指定名称的searchFeidl对象
	  * @sField	:查询字段的名称
	  * 如果是以"Label_FieldId"的格式生成的查询字段，此处应传入FieldId
	  * 如果是以"Label"的格式生成的查询字段，此处直接传入Label
	  */
	 findSearchField:	function(sField){
	 	var oPanel=this.items.get(0);
	 	var oItem=oPanel.items.find(function(oItem){
	 		if(oItem.items.items[0].id=="sf#"+sField)
	 			return true;
	 		
	 	});
	 	return oItem;
	 },
	 addComponent:function(){
	 	var thisx=this;
	 	thisx.initSearchFieldPanel();
	 	thisx.initSearchFieldMenu();
	 	var oPanel=thisx.items.get(0);
	 	thisx.expand(true);
	 	oPanel.doLayout(true);
	 },
	 removeComponent:function(){
	 	var thisx=this;
	 	var oPanel = thisx.items.get(0);
	 	var oItems = oPanel.items.items;
	 	var size=oItems.length;
		for(var i=0;i<size;i++) {
			oPanel.remove(oItems[0],true);
		//oPanel.remove(oItems[0],true);
		}
		
	 }

});
