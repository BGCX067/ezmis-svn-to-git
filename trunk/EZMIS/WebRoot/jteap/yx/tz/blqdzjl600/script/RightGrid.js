/**
 * 字段列表
 */
RightGrid=function(){
	
    var defaultDs=this.getDefaultDS(link1);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-',{text:'导出Excel',handler:function(){
		exportExcel(grid,true);
		}}, '-', '<font color="red">*双击查看详细信息</font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var btnDel=mainToolbar.items.get('btnDel');
		var btnModi=mainToolbar.items.get('btnModi');
		
		if(oCheckboxSModel.getSelections().length == 1){
			if(btnModi) btnModi.setDisabled(false);
		}else{
			if(btnModi) btnModi.setDisabled(true);
		}
		
		if(oCheckboxSModel.getSelections().length < 1){
			if(btnDel) btnDel.setDisabled(true);
		}else{
			if(btnDel) btnDel.setDisabled(false);
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		var obj = {};
		obj.grid = grid;
		obj.select = select;
				
		var url = contextPath + '/jteap/yx/tz/blqdzjl600/editForm.jsp?query=q';
		showIFModule(url,"查看记录","true",600,550,obj);
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
				"id","tianXieRen","tianXieShiJian","cbr","cbsj",
				"zbgycA3","zbgycB3","zbgycC3","zbgyczx3","zbgycA4","zbgycB4","zbgycC4","zbgyczx4",
				"qbbgycA02","qbbgycB02","qbbgycC02","mxA4","mxB4","mxC4","mxA5","mxB5","mxC5",
				"egxA","egxB","egxC","efxA","efxB","efxC","tsyhA","tsyhB","tsyhC","tsehA","tsehB","tsehC"
	        ]),
	        remoteSort: true,
	        //如果为true,每次store在加载时或者一条记录被删除时, 清除所有改动过的记录信息
	        pruneModifiedRecords: true
	    });
		return ds;
	},
    
	/**
	 * DqgzAction 列模型
	 */
	getColumnModel:function(){
		var grid = this;
				
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'id'},
				{id:'cbr',header: "检查人", width: 100, sortable: true, dataIndex: 'cbr'},
				{id:'cbsj',header: "检查时间", width: 100, sortable: true, dataIndex: 'cbsj'},
				{id:'tianXieShiJian',header: "填写时间", width: 110, sortable: true, hidden:true, dataIndex: 'tianXieShiJian'},
				{id:'tianXieRen',header: "填写人", width: 100, sortable: true, hidden:true, dataIndex: 'tianXieRen'},
				{id:'zbgycA3',header: "#3主变高压侧A相", width: 110, sortable: true, dataIndex: 'zbgycA3'},
				{id:'zbgycB3',header: "#3主变高压侧B相", width: 110, sortable: true, dataIndex: 'zbgycB3'},
				{id:'zbgycC3',header: "#3主变高压侧C相", width: 110, sortable: true, dataIndex: 'zbgycC3'},
				{id:'zbgyczx3',header: "#3主变高压侧中性点", width: 120, sortable: true, dataIndex: 'zbgyczx3'},
				{id:'zbgycA4',header: "#4主变高压侧A相", width: 110, sortable: true, dataIndex: 'zbgycA4'},
				{id:'zbgycB4',header: "#4主变高压侧B相", width: 110, sortable: true, dataIndex: 'zbgycB4'},
				{id:'zbgycC4',header: "#4主变高压侧C相", width: 110, sortable: true, dataIndex: 'zbgycC4'},
				{id:'zbgyczx4',header: "#4主变高压侧中性点", width: 120, sortable: true, dataIndex: 'zbgyczx4'},
				{id:'qbbgycA02',header: "#02启备变高压侧A相", width: 125, sortable: true, dataIndex: 'qbbgycA02'},
				{id:'qbbgycB02',header: "#02启备变高压侧B相", width: 125, sortable: true, dataIndex: 'qbbgycB02'},
				{id:'qbbgycC02',header: "#02启备变高压侧C相", width: 125, sortable: true, dataIndex: 'qbbgycC02'},
				{id:'mxA4',header: "220KV #4母线A相", width: 110, sortable: true, dataIndex: 'mxA4'},
				{id:'mxB4',header: "220KV #4母线B相", width: 110, sortable: true, dataIndex: 'mxB4'},
				{id:'mxC4',header: "220KV #4母线C相", width: 110, sortable: true, dataIndex: 'mxC4'},
				{id:'mxA5',header: "220KV #5母线A相", width: 110, sortable: true, dataIndex: 'mxA5'},
				{id:'mxB5',header: "220KV #5母线B相", width: 110, sortable: true, dataIndex: 'mxB5'},
				{id:'mxC5',header: "220KV #5母线C相", width: 110, sortable: true, dataIndex: 'mxC5'},
				{id:'egxA',header: "220KV 鄂光线A相", width: 110, sortable: true, dataIndex: 'egxA'},
				{id:'egxB',header: "220KV 鄂光线B相", width: 110, sortable: true, dataIndex: 'egxB'},
				{id:'egxC',header: "220KV 鄂光线C相", width: 110, sortable: true, dataIndex: 'egxC'},
				{id:'efxA',header: "220KV 鄂凤线A相", width: 110, sortable: true, dataIndex: 'efxA'},
				{id:'efxB',header: "220KV 鄂凤线B相", width: 110, sortable: true, dataIndex: 'efxB'},
				{id:'efxC',header: "220KV 鄂凤线C相", width: 110, sortable: true, dataIndex: 'efxC'},
				{id:'tsyhA',header: "220KV 杜山一回A相", width: 120, sortable: true, dataIndex: 'tsyhA'},
				{id:'tsyhB',header: "220KV 杜山一回B相", width: 120, sortable: true, dataIndex: 'tsyhB'},
				{id:'tsyhC',header: "220KV 杜山一回C相", width: 120, sortable: true, dataIndex: 'tsyhC'},
				{id:'tsehA',header: "220KV 杜山二回A相", width: 120, sortable: true, dataIndex: 'tsehA'},
				{id:'tsehB',header: "220KV 杜山二回B相", width: 120, sortable: true, dataIndex: 'tsehB'},
				{id:'tsehC',header: "220KV 杜山二回C相", width: 120, sortable: true, dataIndex: 'tsehC'}
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
		var rightGrid=this;
		var ids="";
		Ext.each(selections,function(selectedobj){
			ids+=selectedobj.id+",";//取得他们的id并组装
		});
		if(window.confirm("确认删除选中的条目吗？")){
			Ext.Ajax.request({
				url:link2,
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