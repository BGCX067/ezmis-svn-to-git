
// 栏目新增和修改form
var TableForm = function(dmid,tablename) {
	// debugger;
	// this.dmid=dmid;//权限ID
	var ds = new Ext.data.Store({
	    proxy: new Ext.data.ScriptTagProxy({
	        url: link8
	    }),
	    reader: new Ext.data.JsonReader({
	        root: 'list',
	        totalProperty: 'totalCount',
	        id: 'id'
	    }, [
		'id','tablename','tablecname','torder'
	    ]),
	    remoteSort: true,
	    listeners:{
	    	load:loadCallBack
	    }
	});
	
	
    var pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: ds,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",      
		items:[{text:'增加',handler:function(){
			var result=showModule(link15,"yes",700,200);
			if(result=="true"){
				tableGrid.getStore().reload();
			}
		}},{text:'修改',handler:function(){
			var select=tableGrid.getSelectionModel().getSelections()[0];
			if(select!=null){
				var result=showModule(link15+"?id="+select.json.id,"yes",700,200);
				if(result=="true"){
					tableGrid.getStore().reload();
				}
			}
			//
		}},{text:'删除',handler:function(){
			var select=tableGrid.getSelectionModel().getSelections();
    		deleteSelect(select);
		}}]
	});
	
	
	var cm=new Ext.grid.ColumnModel([
    	new Ext.grid.CheckboxSelectionModel({singleSelect:true}),
		{id:'id',header: "id", width: 100, sortable: true,hidden:true, dataIndex: 'id'},
		{id:'tablename',header: "表名", width: 210, sortable: true, dataIndex: 'tablename'},
		{id:'tablecname',header: "表中文名", width: 210, sortable: true, dataIndex: 'tablecname'},
		{id:'torder',header: "排序", width: 210, sortable: true, dataIndex: 'torder'}
	]);
	
	var sm=new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	
	var tableGrid=new Ext.grid.GridPanel({
	 	ds: ds,
	 	cm: cm,
		sm: sm,
	    margins:'2px 2px 2px 2px',
		width:675,
		height:360,
		loadMask: true,
		frame:true,
		tbar:pageToolbar 
	});
	
	
	

var simpleForm = new Ext.FormPanel({
    labelAlign: 'left',
    buttonAlign:'right',
	style:'margin:2px',
    bodyStyle:'padding:0px',
    waitMsgTarget: true,
    id:'myForm',
    width: '100%',
    frame:true, 					// 圆角风格
    labelWidth:70,					// 标签宽度
    items:[tableGrid],
    submit: function(){
    	window.parent.document.all.formFrame.style.display="none";
		window.parent.document.all.next.style.display="inline";
		var select=tableGrid.getSelectionModel().getSelections()[0];// 选择信息
		if(select!=null){
			if(dmid==""||dmid==undefined){
	        	this.getEl().dom.action = link6+"?tableid="+select.json.id+"&tablename="+select.json.tablename;// 转向页面地址
			}else{
				this.getEl().dom.action = link6+"?tableid="+select.json.id+"&tablename="+select.json.tablename+"&dmid="+dmid;// 转向页面地址
			}
	        this.getEl().dom.target='next';
	        this.getEl().dom.method='POST';// 方式
	        this.getEl().dom.submit();// 提交！
		}else{
			alert("选择");
		}
    },
	buttons: [{
		id:'saveButton',
		text:'下一步',
		handler:function(){
			ok();
		}
	},{
        text: '取消',
            handler:function(){
            	window.close();
            }
    }]
});


/**
 * 保存
 */
function ok(){
	// var select=tableGrid.getSelectionModel().getSelections()[0];//选择信息
	// document.location.href=link6+"&id="+select.json.id;
	simpleForm.getForm().submit();
}

/**
 * 加载列表内容
 */
this.loadData=function(){
	tableGrid.getStore().load();
}

/**
 * 定义数据加载完成事件的处理函数
 */
function loadCallBack(store,records,options){
	var arr=new Array();
	store.each(function(record){ 
		var tn = record.get('tablename'); 
		if(tablename==tn){
			// debugger;
			arr.push(record);
		}
	});
	sm.selectRecords(arr,true);
}

/**
 * 删除
 */
	function deleteSelect(select){
		var selections = tableGrid.getSelections();// 获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";// 取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link18,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			tableGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}// Ext.util.JSON.encode(selections.keys)
			});
		}
	}

	TableForm.superclass.constructor.call(this, {
		width : '100%',
		height : 420,
		modal : true,
		autoScroll : true,
		layout : 'column',
		plain : true,
		draggable : false,
		resizable : false,
		bodyStyle : 'padding:1px;',
		items : [{
			border : false,
			columnWidth : 1,
			layout : 'form',
			items : simpleForm
		}]
	});

}

Ext.extend(TableForm, Ext.Panel, {

});
