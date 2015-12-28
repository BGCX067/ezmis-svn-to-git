
/**
 * 通知Grid
 */
var TzGrid=function(){
    var defaultDs=this.getDefaultDS(link4 + "?jllb="+encodeURIComponent("通知")+"&gwlb="+encodeURIComponent(superGwlb)+"&limit=2");
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 2,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}}]
	});
	TzGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		height: 200,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar
	});	
	
	//当有数据被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var selects = oCheckboxSModel.getSelections();
//		for(var i=0; i<selects.length; i++){
//			var jlr = selects[i].get("jlr");
			//平台管理员具有所有权限
//			if(!isRoot){
//				if(curPersonName != jlr){
//					return;
//				}
//			}
//	}
		
		
		var btnDelTz=tzTool.items.get('btnDelTz');
		var btnModiTz=tzTool.items.get('btnModiTz');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModiTz) btnModiTz.setDisabled(false);
		}else{
			if(btnModiTz) btnModiTz.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length<=0){
			if(btnDelTz) btnDelTz.setDisabled(true);
		}else{
			if(btnDelTz) btnDelTz.setDisabled(false);
		}
	});
}
Ext.extend(TzGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
			"id","jlsj","jlnr","jlr","tzgw","time"
	        ]),
	        remoteSort: true
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "id", width: 100, hidden:true, sortable: true, dataIndex: 'id'},
				{id:'jlnr',header: "通知内容", width: 480, sortable: true, dataIndex: 'jlnr',editor:{xtype:'poptextarea'},
							renderer:function(value, metadata, record){ 
							metadata.attr ='style="white-space:normal;"'; 
							return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"; 
							}
				},
				{id:'tzgw',header: "通知岗位", width: 260, sortable: true, dataIndex: 'tzgw',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
			    		return "<span ext:qtip='"+value+"'>"+Ext.util.Format.htmlEncode(value)+"</span>"
			    	}},
				{id:'jlsj',header: "通知时间", width: 120, sortable: true, dataIndex: 'jlsj',
				    renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(value["time"] != null){
				    		var dt = new Date(value["time"]).format("Y-m-d H:i");
							return dt;         
						}else{
							return new Date(value).format("Y-m-d H:i:s");
						}
					}
				},
				{id:'jlr',header: "发布人", width: 80, sortable: true, dataIndex: 'jlr'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 删除
	 */
	deleteSelect:function(select){
		var selections = this.getSelections();//获取被选中的行
		var grid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link3,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			grid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}

});
