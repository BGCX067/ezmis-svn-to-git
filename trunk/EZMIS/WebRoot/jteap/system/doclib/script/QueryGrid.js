
/**
 * 字段列表
 */
QueryGrid=function(){
    var grid=this;
    //alert(grid.store.proxy.url);
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: new Ext.data.Store(),
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red">*双击查看详细信息</font>']
	});
	QueryGrid.superclass.constructor.call(this,{
	 	ds: new Ext.data.Store(),
	 	cm: this.getColumnModel1(),
		//sm: this.sm,
	 	sm:new Ext.grid.CheckboxSelectionModel(),
		width:320,
		height:298,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnModiDocument=mainToolbar.items.get("btnModiDocument");
		var btnDelDocument=mainToolbar.items.get("btnDelDocument");
		
		if(oCheckboxSModel.getSelections().length==1){
			btnModiDocument.setDisabled(false);
			btnDelDocument.setDisabled(false);
		}else{
			btnModiDocument.setDisabled(true);
		}
		if(oCheckboxSModel.getSelections().length <= 0){
			btnModiDocument.setDisabled(true);
			btnDelDocument.setDisabled(true);
		}
	});
	
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select == null){
			return;
		}
		var id=select.json.id;
		var param={};
		param.docid=id;
		AjaxRequest_Sync(link15, param, function(req) {
			var responseText = req.responseText;
			var responseObj = Ext.decode(responseText);
			if (responseObj.success) {
				var url=contextPath+"/jteap/system/doclib/generate/"+responseObj.url;
				var scrWidth=screen.availWidth;
				var scrHeight=screen.availHeight;
				var self=window.open(url,"PowerBOS","");
				//self.moveTo(0,0);
				//self.resizeTo(scrWidth,scrHeight);
			}
		});
	});	
}
Ext.extend(QueryGrid, Ext.grid.GridPanel, {
	//sm:new Ext.grid.CheckboxSelectionModel(),
	/**
	 * 删除选中文档
	 */
	deleteSelectedDoclib:function(){
		var store = this.store;
	    var records = rightGrid.selModel.getSelections();
	    var recordsLen = records.length;                     //得到行数组的长度
	    var ids = "";
	    var id = "";
		if(rightGrid.getSelectionModel().getSelected() == null) {
			Ext.Msg.alert("提示","请先选择要删除的行!");
			return ;
		}
		Ext.MessageBox.confirm('系统提示信息', '确定要删除所选的记录吗?', function(buttonobj){
			if(buttonobj == 'yes'){
				//判断扩展字段是否拥有id，没有就直接在前台删除
				var bFlag=false;
				
				for(var i = 0; i < recordsLen; i++){
					if(records[i].get("id") == "" || records[i].get("id") == null){
						rightGrid.store.commitChanges() ;
						rightGrid.store.remove(rightGrid.getSelectionModel().getSelected()) ;
					}else{
						bFlag=true;
						var id = records[i].get("id");
						if(id == null || id == ""){
							//ids += id ;
						}else{
							if(i != 0){
								ids += ","+id;
							}else{
								ids += id ;
							}
						}
					}
				}
				if(bFlag){
					//如果有id，就直接在数据库删除
					Ext.Ajax.request({
						url:link5,
						method:'post',
						params: {ids:ids},
						success:function(ajax){
							var responseText=ajax.responseText;	
					 		var responseObject=Ext.util.JSON.decode(responseText);
					 		if(responseObject.success){
					 			alert("删除成功!");
					 			rightGrid.getStore().reload();
					 		}else{
					 			alert(responseObject.msg);
					 		}
						},
						failure:function(){
							alert("提交失败!");
						}
					});
				}
			}else{
				//Ext.Msg.alert("提示","请先选择要删除的行!");
	    	}
		});
	},
	/**
	 * default 列模型
	 */
	getColumnModel1:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	//this.sm,
		        {id:'columncode',header: " ", width: 2, sortable: true}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
		this.store.reload();
	}

});