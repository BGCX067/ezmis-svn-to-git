
/**
 * 字段列表
 */
RightGrid = function() {
	var defaultDs = this.getDefaultDS();
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
				pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
				store : defaultDs,
				displayInfo : true,
				displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
				emptyMsg : "没有符合条件的数据",
				items : ['-', '<font color="red">*双击查看详细信息</font>']
			});
	RightGrid.superclass.constructor.call(this, {
				ds : defaultDs,
				cm : this.getColumnModel(),
				sm : this.sm,
				margins : '2px 2px 2px 2px',
				width : 600,
				height : 300,
				loadMask : true,
				region : 'center',
				tbar : this.pageToolbar
			});

	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
				var node = leftTree.getSelectionModel().getSelectedNode();
				var btnShenPiHt = mainToolbar.items.get("btnShenPiHt");
				var btnCheXiaoShenPi = mainToolbar.items.get("btnCheXiaoShenPi");
				var btnZuoFeiHt = mainToolbar.items.get("btnZuoFeiHt");
				var btnShanChuHt = mainToolbar.items.get("btnShanChuHt");

				if (btnShenPiHt != null) {
					btnShenPiHt.setDisabled(true);
				}
				if (btnCheXiaoShenPi != null) {
					btnCheXiaoShenPi.setDisabled(true);
				}
				if (btnZuoFeiHt != null) {
					btnZuoFeiHt.setDisabled(true);
				}
				if (btnShanChuHt != null) {
					btnShanChuHt.setDisabled(true);
				}
				if (oCheckboxSModel.getSelections().length > 1) {
					if (node.id == 'cgx') {
						//草稿箱中才能删除合同
						if (btnShanChuHt != null) {
							btnShanChuHt.setDisabled(false);
						}
					}
				}else if (oCheckboxSModel.getSelections().length == 1) {
					if (node.id == 'dsp') {
						if (btnZuoFeiHt != null) {
							btnZuoFeiHt.setDisabled(false);
						}
						if (btnShenPiHt != null) {
							btnShenPiHt.setDisabled(false);
						}
					} else if (node.id == 'cgx') {
						if (btnShanChuHt != null) {
							btnShanChuHt.setDisabled(false);
						}
					} else if (node.id == 'ysp') {
						if (btnCheXiaoShenPi != null) {
							btnCheXiaoShenPi.setDisabled(false);
						}
					}
				}
			});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				if (rightGrid.getSelectionModel().getSelections().length != 1) {
					alert("请选取一条记录查看");
					return;
				}

				var node = leftTree.getSelectionModel().getSelectedNode();
				var select = rightGrid.getSelectionModel().getSelections()[0];
				if (node.id == 'dsp') {
					var id = select.json.TASKTODOID;
					var pid = select.json.FLOW_INSTANCE_ID;
					var token = select.json.TOKEN;

					// 弹出流程查看窗口
					var url = link10 + "?pid=" + pid + "&id=" + id + "&token=" + token + "&isEdit=true";
					showIFModule(url,"查看流程","true",735,582,{});
					
					// 进行释放签收操作
					Ext.Ajax.request({
						url : link4,
						method : 'POST',
						params : {
							pid : pid,
							token : token
						},
						success : function(ajax) {
							var responseText = ajax.responseText;
							var obj = Ext.decode(responseText);
							if (obj.success) {
								rightGrid.getStore().reload();
							} else {
								alert("数据库操作异常，请联系管理员！");
							}
						},
						failure : function() {
							alert("数据库操作异常，请联系管理员！");
						}
					})
					rightGrid.getStore().reload();
				} else if (node.id == 'cgx') {
					var url = link8 + "?pid=" + select.get("ID_");
					showIFModule(url,"查看流程","true",735,582,{});
					rightGrid.getStore().reload();
				} else {
					var url = link10 + "?pid=" + select.get("ID_") + "&status=false";
					showIFModule(url,"查看流程","true",735,582,{});
				}
			});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel(),

	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(status, params) {
		var url = "";
		var procFields = [];
		// 待审批
		if(status == 'dsp') {
			url = link1;
			procFields = ["ID", "HTBH","HTJE","HTMC","HTLX","HTJE","SKDW","BCSQFKJE","SQBMZDR","HTCJSJ","STATUS", 
						"FLOW_TOPIC", "TASKTODOID", "FLOW_NAME","FLOW_INSTANCE_ID",
						"CURRENT_TASKNAME", "POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN"];
		}else{
			procFields = ["ID", "HTBH","HTJE","HTMC","HTLX","HTJE","SKDW","BCSQFKJE","SQBMZDR","HTCJSJ","STATUS",
						"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_", "FLOW_NAME","PROCESS_DATE","FLOW_CONFIG_ID","FLOW_FORM_ID"];
						
			// 已审批
			if (status == 'ysp') {
				url = link2;
			// 草稿箱
			}else if (status == 'cgx') {
				url = link3;
			// 作废
			}else if (status == 'zf') {
				url = link6;
			// 全厂
			}else if (status == 'qc') {
				url = link5;
			//终结
			}else if (status == 'zj'){
				url = link17;
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
	 * 列模型
	 */
	getColumnModel : function(status) {
		var cm;
		var isHiddenHtje = false;
		if(tableName == "TB_HT_RLHTFK"){
			isHiddenHtje = true;	
		}
		// 待审批
		if (status == 'dsp') {
			cm = new Ext.grid.ColumnModel([
				this.sm, 
				{id : 'ID',header : "ID",width : 100,hidden:true, sortable : true,dataIndex : 'ID'},
				{id : 'HTBH',header : "合同编号",width : 100,sortable : true,dataIndex : 'HTBH'}, 
				{id : 'HTMC',header : "合同名称",width : 120,sortable : true,dataIndex : 'HTMC'}, 
				{id : 'HTJE',header : "合同金额(元)",width : 100,hidden:isHiddenHtje,sortable : true,dataIndex : 'HTJE'}, 
				{id : 'HTLX',header : "合同类型",width : 80,sortable : true,dataIndex : 'HTLX'}, 
				{id : 'SKDW',header : "收款单位",width : 100,sortable : true,dataIndex : 'SKDW'}, 
				{id : 'BCSQFKJE',header : "申付金额(元)",width : 80,sortable : true,dataIndex : 'BCSQFKJE'}, 
				{id : 'STATUS',header : "审批状态",width : 140,sortable : true,dataIndex : 'STATUS'},
				{id : 'POST_PERSON',header : "发送人",width : 60,sortable : true,dataIndex : 'POST_PERSON'}, 
				{id : 'POST_TIME',header : "发送时间",width : 130,sortable : true,dataIndex : 'POST_TIME'},
				{id : 'HTCJSJ',header : "创建时间",width : 130,sortable : true,dataIndex : 'HTCJSJ',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value != null){
						return formatDate(new Date(value),"yyyy-MM-dd HH:mm:ss");
					}
				}}]);
		}else{
			var czsj = "";
			if(status == "ysp"){
				czsj = "审批时间";
			}else if(status == "cgx"){
				czsj = "起草时间";
			}else if(status == "zf"){
				czsj = "作废时间";
			}else if(status == "qc"){
				czsj = "操作时间";
			}else if(status == "zj"){
				czsj = "终结时间";
			}
			
			// 其他
			cm = new Ext.grid.ColumnModel([
				this.sm, 
				{id : 'ID',header : "ID",width : 100,hidden:true, sortable : true,dataIndex : 'ID'},
				{id : 'HTBH',header : "合同编号",width : 100,sortable : true,dataIndex : 'HTBH'}, 
				{id : 'HTMC',header : "合同名称",width : 120,sortable : true,dataIndex : 'HTMC'}, 
				{id : 'HTJE',header : "合同金额(元)",width : 100,hidden:isHiddenHtje,sortable : true,dataIndex : 'HTJE'}, 
				{id : 'HTLX',header : "合同类型",width : 80,sortable : true,dataIndex : 'HTLX'},
				{id : 'SKDW',header : "收款单位",width : 100,sortable : true,dataIndex : 'SKDW'}, 
				{id : 'BCSQFKJE',header : "申付金额(元)",width : 80,sortable : true,dataIndex : 'BCSQFKJE'},
				{id : 'STATUS',header : "审批状态",width : 140,sortable : true,dataIndex : 'STATUS'},
				{id : 'PROCESS_DATE',header : czsj,width : 130,sortable : true,dataIndex : 'PROCESS_DATE'},
				{id : 'HTCJSJ',header : "创建时间",width : 130,sortable : true,dataIndex : 'HTCJSJ',
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
	changeToListDS : function(status, params) {
		var ds = this.getDefaultDS(status, params);
		var cm = this.getColumnModel(status);
		this.pageToolbar.bind(ds);
		this.reconfigure(ds, cm);
	}

});
