/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
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
		
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		var node = leftTree.getSelectionModel().getSelectedNode();
		var obj = {};
		if(node.id == "yys"){
			obj.flbs = node.id;
			obj.id = select.data.ID;
			obj.gcxmbh = select.data.GCXMBH;
			obj.gcxmmc = select.data.GCXMMC;
			obj.sgdw = select.data.SGDW;
			obj.jgsj = select.data.JGSJ;
			obj.gcnr = select.data.GCNR;
			obj.xmyj = select.data.XMYJ;
		}else{
			obj.flbs = node.id;
			obj.id = select.data.ID;
			obj.gcys_id = select.data.GCYS_ID;
			obj.gcxmbh = select.data.XMBH;
			obj.gcxmmc = select.data.XMMC;
			obj.cbdw = select.data.CBDW;
			obj.khh = select.data.KHH;
			obj.skdwzh = select.data.SKDWZH;
			obj.xmfyly = select.data.XMFYLY;
			obj.yljyfkje = select.data.YLJYFKJE;
			obj.xmhtje = select.data.XMHTJE;
			obj.gcnr = select.data.XMWCSJHNR;
			obj.ssbm = select.data.SSBM;
		}
		window.returnValue = obj;
		window.close();
	});
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(flbs, params){
	    var url = "";
		var procFields = [];
		// 已验收
		if(flbs == 'yys') {
			url = link18;
			procFields = ["ID","GCXMBH","GCXMMC","SGDW","XMYJ","JGSJ","GCNR","YSNF","CJSJ","STATUS"];
		}else{ // 欠款
			procFields = ["ID","GCYS_ID","XMMC","XMBH","XMWCSJHNR","CBDW","XMFYLY","KHH","SSBM","SKDWZH","XMHTJE","YLJYFKJE","BCYFK","BCSFK","BCKK","CJSJ","ZFZT","FQZT","STATUS"];
						
			// 欠款支付单
			if (flbs == 'qkzfd') {
				url = link19;
			}
		}
		
		url += params;
		var ds = new Ext.data.Store({
				proxy : new Ext.data.ScriptTagProxy({
							url : url
						}),
	
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount',
							id : 'id'
						}, procFields),
				remoteSort : true
		});
		return ds;
	},
    
	/**
	 * DqgzAction 列模型
	 */
	getColumnModel:function(flbs){
		var grid = this;
		var cm;
		// 已验收
		if (flbs == 'yys') {
			  var cm=new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "id", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'GCXMBH',header: "项目编号", width: 100, sortable: true, dataIndex: 'GCXMBH'},
				{id:'GCXMMC',header: "项目名称", width: 150, sortable: true, dataIndex: 'GCXMMC'},
				{id:'SGDW',header: "施工单位", width: 150, sortable: true, dataIndex: 'SGDW'},
				{id:'JGSJ',header: "竣工时间", width: 130, sortable: true, dataIndex: 'JGSJ'},
				{id : 'STATUS',header : "审批状态",width : 140,sortable : true,dataIndex : 'STATUS'}
				]);
		}else{
			// 其他
			cm = new Ext.grid.ColumnModel([
				this.sm, 
				{id : 'ID',header : "ID",width : 100,hidden:true, sortable : true,dataIndex : 'ID'},
				{id : 'XMMC',header : "项目名称",width : 120,sortable : true,dataIndex : 'XMMC'}, 
				{id : 'CBDW',header : "承包单位",width : 100,sortable : true,dataIndex : 'CBDW'}, 
				{id : 'XMHTJE',header : "项目合同金额",width : 100,sortable : true,dataIndex : 'XMHTJE'}, 
				{id : 'YLJYFKJE',header : "累计付款金额",width : 100,sortable : true,dataIndex : 'YLJYFKJE'}, 
				{id : 'ZFZT',header : "支付状态",width : 100,sortable : true,dataIndex : 'ZFZT',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						 	if(value == '支付完成'){
								metadata.attr ="style=color:green";
								return value;
							}else if(value == '全额支付'){
								metadata.attr ="style=color:Orange";
								return value;
							}else{
								metadata.attr ="style=color:red";
								return value;
							}
					}
				},
				{id : 'FQZT',header : "分期状态",width : 100,sortable : true,dataIndex : 'FQZT',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
								metadata.attr ="style=color:red";
								return value;
					}
				},
				{id : 'BCYFK',header : "本次应付款",width : 100,sortable : true,hidden: true ,dataIndex : 'BCYFK'},
				{id : 'BCSFK',header : "本次实付款",width : 100,sortable : true,hidden: true ,dataIndex : 'BCSFK'},
				{id : 'BCKK',header : "本次扣款",width : 100,sortable : true,hidden: true ,dataIndex : 'BCKK'},
				{id : 'STATUS',header : "审批状态",width : 140,sortable : true,dataIndex : 'STATUS'},
				{id : 'CJSJ',header : "创建时间",width : 130,sortable : true,dataIndex : 'CJSJ',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value != null){
						return formatDate(new Date(value),"yyyy-MM-dd HH:mm:ss");
					}
				}}]);
		}
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(flbs, params){
		var ds = this.getDefaultDS(flbs, params);	
		var cm=this.getColumnModel(flbs);
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	}
	
});
