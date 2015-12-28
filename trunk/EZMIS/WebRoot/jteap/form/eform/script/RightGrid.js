
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link4);
    var grid=this;
    this.ddText="移动{0}个表单";
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}},'-','<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		//启用拖动
		ddGroup:'GridDD',
		enableDragDrop:true,
		dropAllowed: true, 
		dragAllowed: true,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		
		var btnDelCForm=mainToolbar.items.get('btnDelCForm');
		var btnModifyCForm=mainToolbar.items.get('btnModifyCForm');
		var btnFinalManuscript = mainToolbar.items.get('btnFinalManuscript');
		
		if(oCheckboxSModel.getSelections().length==1){
			if(btnModifyCForm) btnModifyCForm.setDisabled(false);
			if(btnFinalManuscript) btnFinalManuscript.setDisabled(false);
		}else{
			if(btnModifyCForm) btnModifyCForm.setDisabled(true);
			if(btnFinalManuscript) btnFinalManuscript.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length <= 0){
			if(btnDelCForm) btnDelCForm.setDisabled(true);
		}else{
			if(btnDelCForm) btnDelCForm.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		
		var finalManuscript = select.json.finalManuscript;
		if(finalManuscript != 1){
			alert('表单未定稿..-_-!');
			return;
		}
		
		var sn = select.json.sn;
		var cp = select.json.title;
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+sn;
		
		//$mw_if(url,cp,"yes",100,100);
		
		/*
		var fw = new $FW({
			url:url,
			//width:800,
			id:sn,
			type:"T2",
			//toolbar:false,
			//scrollbars:false,
			//menubar:false,
			userIF:false,
			//resizable:true,
			callback:function(retValue){
				alert("操作成功");
			}
			//height:200
		});
		//fw.show();
		 * */
		
		new $FW({url:url}).show();
		//new $FW({url:url,type:'T2',userIF:true}).show();
		//new $FW({url:url,type:'T3',userIF:true}).show();
	 	
	});
	
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
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
	            'title', 'sn', 'version','creator','creatDt','type','catalog','eformUrl','exHtmlUrl','width','height','finalManuscript'
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
		        {id:'title',header: "表单标题", width: 190, sortable: true, dataIndex: 'title'},
		        {id:'sn',header: "表单标识", width: 195, sortable: true, dataIndex: 'sn'},
		        {id:'width',header: "宽度", width: 50, sortable: true, dataIndex: 'width'},
		        {id:'height',header: "高度", width: 50, sortable: true, dataIndex: 'height'},
		        {id:'creator',header: "创建人", width: 80, sortable: true, dataIndex: 'creator'},
				{id:'creatDt',header: "创建时间", width: 125, sortable: true, dataIndex: 'creatDt',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					}},
				{id:'version',header: "版本号", width: 50, sortable: true, dataIndex: 'version'},
				{id:'finalManuscript', header:"定稿", width:50, sortable:true, dataIndex:'finalManuscript',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value != null && value == 1 ){
							return "<img src='icon/icon_14.gif'>";
						}else{
							return "";
						}
					}}
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
	 * 删除用户
	 */
	delForm:function(select){
		var selections = this.getSelections();//获取被选中的行
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的表单吗？")){
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			rightGrid.getStore().reload();
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

