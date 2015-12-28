var attrsData = {};
var evtsData = {};
/**
 * 子表控件的subTableName属性编辑控件
 * 用于选择定义表进而生成列模型
 */
var SubTableTriggerField = Ext.extend(Ext.form.TriggerField, {
	onTriggerClick:function(){
		var field = this;
		var url = "eformDefTableSelector.jsp";
		var result = showModule(url, true, 300, 500);
		if (result != null) {
			var subTableName = result.tableCode;
			var tableId = result.id;
			field.setValue(subTableName);
			//选中了指定的表定义之后，需要取得该表定义的所有列模型
			var url = contextPath + "/jteap/form/dbdef/DefColumnInfoAction!showListAction.do?tableid="+tableId;
			AjaxRequest_Sync(url,{},function(ajax){
				eval("var obj = "+ajax.responseText);
				if(obj.list){
					if(obj.list.length>0){
						var cm = field._buildColumnModel(obj.list);
						attrsData.subTableCM = cm;
						attrsData.subTableName = subTableName;
						Ext.getCmp("attrsGrid").setSource(attrsData);
					}
				}else{
					alert("无效的表定义对象,请重新选择");
				}
			});
		}
	},
	/**
	 * 构建列模型
	 */
	_buildColumnModel:function(defColumnList){
		var cm = "";
		for(var i=0;i<defColumnList.length;i++){
			var col = defColumnList[i];
			var width = col.columnlength || 100;
			var type = col.columntype;
			var ispk = col.pk;
			var editor = "{xtype:'textfield'}";
			//if('DATE,TIMESTAMP'.indexOf(type)>=0)
			if(type.indexOf('DATE')>=0 || type.indexOf('TIMESTAMP')>=0){
				var fm = col.format;
				if(fm == null || fm == ''){
					if(type.indexOf('DATE')>=0){
						fm = "Y-m-d";
					}else{
						fm = "Y-m-d H:i:s";	
					}
				}
				editor = "{xtype:'datetimefield',format:'"+fm+"'}";
			}
			
			var hidden = ispk?"true":"false";
			
			var dh = "";
			if(i<defColumnList.length-1){
				dh = ",";
			}
			cm += "/*******第"+(i+1)+"列*******/\r\n{header:'"+col.columnname+"',dataIndex:'"+col.columncode+"',width:"+width+",hidden:'"+hidden+"',sortable:true,editor:"+editor+"}"+dh+"\r\n\r\n";
		}
		cm = "[\r\n"+cm+"\r\n]";
		return cm;
	}
	
});

/**
 * 控件参数设置
 * fd,edr_param,oEditorXmlNode
 */
var EditorPropertyForm = function(fd,edr_param,oEditorXmlNode){
	if(edr_param.trim() == ''){
		edr_param = '{}';
	}
	//参数字符串转换为对象
	var edr_param = edr_param.evalJSON();
	//属性<attr> 节点集合
	var attrNodeList = oEditorXmlNode.firstChild.childNodes;
	//事件<evt> 节点集合
	var evtNodeList = oEditorXmlNode.lastChild.childNodes;
	
	var formWindow = this;
	formWindow.ok = false;
	
	//初始化属性初始值attrsData
	for(var i=0;i<attrNodeList.length;i++){
		var attrNode = attrNodeList[i];
		var attrName = getXmlAttribute(attrNode,"name");
		var must = getXmlAttribute(attrNode,"must");
		var dv = getXmlAttribute(attrNode,"dv");
		var many = getXmlAttribute(attrNode,"many");
		var type = getXmlAttribute(attrNode,"type");
		var value = eval("edr_param."+attrName);
		if(value == null && must == 'true'){
			value = dv;
		}
		if(value!=null){
		if(type == 'boolean'){
			value = (value == 'true' || value == true);
			
		}else if(type == 'date'){
			value = parseDate(value);
		}}else{value=''}
		
		attrsData[attrName] = value;
	}
	//初始化事件初始值evtsData
	for(var i=0;i<evtNodeList.length;i++){
		var evtNode = evtNodeList[i];
		var evtName = getXmlAttribute(evtNode,"name");
		var value = null;
		if(edr_param.evts){
			value = eval("edr_param.evts."+evtName);
		}
		if(value == null) value = '';
		evtsData[evtName]=value;
	}
	
	
	/**
	 * 基本属性 grid
	 */
	this.attrsGrid = new Ext.grid.PropertyGrid({
		id: 'attrsGrid',
		width: 563,
		height: 180,
		footer: true,
		viewConfig: {
            forceFit: true,
            autoFill: true
           // scrollOffset: 17 // the grid will never have scrollbars
        },
         source: attrsData,
		listeners: {
			beforeedit: function(e){
				type = e.record.id;
				var xPath = "attrs/attr[@name='"+type+"']";
				var oNode =oEditorXmlNode.selectNodes(xPath)[0];
				var many = getXmlAttribute(oNode,"many");
				if(many == 'true'){
					var value = eval("attrsData."+type);
					var editParamsWindow = new PropertyManyWindow(value,function(value,txt){
						txt.setValue(value);
					});
					editParamsWindow.show();
					editParamsWindow.on("close",function(win){
						if(win.ok == true){
							var value = win.getValue();
							eval("attrsData."+type+"=value");
							formWindow.attrsGrid.setSource(attrsData);
						}
					});
					return false;
				}
				
			}
		}
	});
	
    /**
     * 自定义属性操作控件
     */
	this.attrsGrid.customEditors = {
		subTableName:new Ext.grid.GridEditor(new SubTableTriggerField({
			triggerClass :'x-form-search-trigger',
			readOnly :true
		}))
	};
	
	/**
	 * 事件 grid
	 */
	this.evtsGrid = new Ext.grid.PropertyGrid({
		id: 'evtsGrid',
		width: 595,
		height: 200,
		footer: true,
		viewConfig: {
			forceFit: true,
			autoFill: true,
			scrollOffset: 17
		},
		 source: evtsData,
		listeners: {
			beforeedit: function(e){
				type = e.record.id;
				var value = eval("evtsData."+type);
				var editParamsWindow = new PropertyManyWindow(value,function(value,txt){
					if(value==''){
						txt.setValue("function evt_#{fd}_" + type + "(edr,args){\r\n" + "\r\n\t\r\n" + "}\r\n");
					}else{
						txt.setValue(value);
					}
				});
				editParamsWindow.show();
				editParamsWindow.on("close",function(win){
					if(win.ok == true){
						var value = win.getValue();
						eval("evtsData."+type+"=value");
						formWindow.evtsGrid.setSource(evtsData);
					}
				});
				return false;
			}
		}
	});
	
	var simpleForm = new Ext.FormPanel({
		labelAlign: 'left',
		buttonAlign: 'right',
		style: 'margin:2px',
		bodyStyle:'padding:0px',
		border:false,
	    width: '100%',
	    labelWidth: 70,					//标签宽度
		waitMsgTarget: true,
	    frame: true, 					//圆角风格
	    items: [{
	    	xtype: 'tabpanel',
	    	id:'tabPropertyEvtPanel',
	    	height: 220,
	    	border:false,
			activeTab: 0,				//指定活动的tab索引
		    defaults: {bodyStyle:'padding:2px'},
		    plain: true,
		    items: [{
				id:'tabPropertyPanel',
				title: '基本属性',
				layout: 'form',
				defaultType: 'textfield',
				items: formWindow.attrsGrid
		    },{
		    	id:'tabEvtPanel',
		    	title: '事件定义',
		    	layout: 'form',
		    	defaultType: 'textarea',
		    	items: formWindow.evtsGrid
		    }]
	    }],
	    buttons: [{
	    	text: '保存',
	    	handler: function(){
	    		formWindow.ok = true;
	    		formWindow.close();
	    	}
	    },{
			text: '取消',
			handler: function(){
				formWindow.close();
			}
	    }]
	});
	
	EditorPropertyForm.superclass.constructor.call(this,{
        title: '控件属性事件设置',
        width: 600,
        height: 320,
        id :'editorPropertyWindow',
        buttonAlign: 'center',
        region : 'center',
        layout: 'column',
        modal: true,
        plain: true,
        draggable: true,
        resizable: false,
        items: [{
        	border:false,
        	columnWidth:1,
        	layout:'form',
        	items:simpleForm
        }]
	 });
	 
}

Ext.extend(EditorPropertyForm, Ext.Window, {
	
	
})