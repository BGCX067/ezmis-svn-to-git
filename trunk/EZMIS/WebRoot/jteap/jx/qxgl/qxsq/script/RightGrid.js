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
				var btnProcess = mainToolbar.items.get("btnProcess");
				var btnCancel = mainToolbar.items.get("btnCancel");
				var btnDelSq = mainToolbar.items.get("btnDelSq");
				var btnUndo = mainToolbar.items.get("btnUndo");

				if (btnProcess != null) {
					btnProcess.setDisabled(true);
				}
				if (btnCancel != null) {
					btnCancel.setDisabled(true);
				}
				if (btnDelSq != null) {
					btnDelSq.setDisabled(true);
				}
				if (btnUndo != null) {
					btnUndo.setDisabled(true);
				}
				if (oCheckboxSModel.getSelections().length > 1) {
					if (node.id == 'cgx') {
						if (btnDelSq != null) {
							btnDelSq.setDisabled(false);
						}
					}
				} else if (oCheckboxSModel.getSelections().length == 1) {
					if (btnProcess != null) {
						btnProcess.setDisabled(false);
					}
					if (node.id == 'dcl') {
						if (btnCancel != null) {
							btnCancel.setDisabled(false);
						}
					} else if (node.id == 'cgx') {
						if (btnDelSq != null) {
							btnDelSq.setDisabled(false);
						}
					} else if (node.id == 'ycl') {
						if (btnUndo != null) {
							btnUndo.setDisabled(false);
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
				if (node.id == 'dcl') {
					var id = select.json.TASKTODOID;
					var pid = select.json.FLOW_INSTANCE_ID;
					var token = select.json.TOKEN;

					// 弹出流程查看窗口
					var windowUrl = link10 + "?pid=" + pid + "&id=" + id + "&token=" + token + "&isEdit=true";
					var url = windowUrl+"&formSn=TB_JX_QXGL_QXD_SB"+"&docid="+id+"&st=02";
							var myTitle = "查询记录";
							var fw = new $FW({
								url:url,
								width:750,
								height:582,			
								id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
								type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
								title: myTitle,					//标题
								status: false,					//状态栏
								toolbar:false,					//工具栏
								scrollbars:false,				//滚动条
								menubar:false,					//菜单栏
								userIF:false,					//是否采用Iframe套框,为解决模式窗口无法刷新的问题
								resizable:false,				//是否支持调整大小
								callback:function(retValue){	//回调函数
								    rightGrid.getStore().reload();
								}
							});
							fw.show();

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
					var url = link9;
					var windowUrl = link8 + "?pid=" + select.get("ID_");
					var args = "url|" + windowUrl + ";title|" + '查看流程';
					var retValue = showModule(url, "yes", 800, 600, args);
					rightGrid.getStore().reload();
				} else {
					var url = link9;
					var windowUrl = link10 + "?pid=" + select.get("ID_") + "&status=false";
					var args = "url|" + windowUrl + ";title|" + '查看流程';
					var retValue = showModule(url, "yes", 800, 600, args);
				}
			});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
		sm : new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		}),

			/**
			 * 取得默认数据源 返回数据格式
			 */
			getDefaultDS : function(status, params) {
				var url = "";
				var procFields = [];
				// 待处理
				if (status == 'dcl') {
					url = link1;
					procFields = ['ID', 'QXZY', 'SBMC', 'QXMC', 'STATUS', "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
							"FLOW_INSTANCE_ID", "CURRENT_TASKNAME", "POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN",
							"curSignInName","ROWNUM"];
					// 已处理
				} else if (status == 'ycl') {
					url = link2;
					procFields = ['ID', 'QXZY', 'SBMC', 'QXMC', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
							'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
					// 草稿箱
				} else if (status == 'cgx') {
					url = link3;
					procFields = ['ID', 'QXZY', 'SBMC', 'QXMC', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
							'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
					// 作废
				} else if (status == 'zf') {
					url = link6;
					procFields = ['ID', 'QXZY', 'SBMC', 'QXMC', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
							'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
					// 全厂
				} else if (status == 'qc') {
					url = link5;
					procFields = ['ID', 'QXZY', 'SBMC', 'QXMC', 'STATUS', 'ID_', 'FLOW_NAME', 'START_', 'END_',
							'PROCESSINSTANCE_', 'FLOW_CONFIG_ID'];
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
			 * PersonAction 列模型
			 */
			getColumnModel : function(status) {
				var cm;
				// 待处理
				if (status == 'dcl') {
					cm = new Ext.grid.ColumnModel([{
								id : 'XH',
								header : "序号",
								width : 80,
								sortable : true,
								renderer:function(value,metadata,record,rowIndex){ 
   										return 1 + rowIndex; 
  								} 
							},{
								id : 'SBMC',
								header : "设备名称",
								width : 200,
								sortable : true,
								dataIndex : 'SBMC'
							}, {
								id : 'QXMC',
								header : "缺陷名称",
								width : 120,
								sortable : true,
								dataIndex : 'QXMC'
							}, {
								id : 'STATUS',
								header : "缺陷状态",
								width : 80,
								sortable : true,
								dataIndex : 'STATUS'
							}, {
								id : 'POST_PERSON',
								header : "发送人",
								width : 80,
								sortable : true,
								dataIndex : 'POST_PERSON'
							}, {
								id : 'POST_TIME',
								header : "发送时间",
								width : 130,
								sortable : true,
								dataIndex : 'POST_TIME'
							}, {
								id : 'curSignInName',
								header : "当前签收人",
								width : 75,
								sortable : true,
								dataIndex : 'curSignInName'
							}]);
					// 其他
				} else {
					cm = new Ext.grid.ColumnModel([{
								id : 'XH',
								header : "序号",
								width : 80,
								sortable : true,
								renderer:function(value,metadata,record,rowIndex){ 
   										return 1 + rowIndex; 
  								} 
							},{
								id : 'SBMC',
								header : "设备名称",
								width : 200,
								sortable : true,
								dataIndex : 'SBMC'
							}, {
								id : 'QXMC',
								header : "缺陷名称",
								width : 120,
								sortable : true,
								dataIndex : 'QXMC'
							}, {
								id : 'STATUS',
								header : "缺陷状态",
								width : 80,
								sortable : true,
								dataIndex : 'STATUS'
							}, {
								id : 'START_',
								header : "开始时间",
								width : 140,
								sortable : true,
								dataIndex : 'START_',
								renderer : function(value, metadata, record, rowIndex, colIndex, store) {
									var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
									return dt;
								}
							}, {
								id : 'END_',
								header : "结束时间",
								width : 140,
								sortable : true,
								dataIndex : 'END_',
								renderer : function(value, metadata, record, rowIndex, colIndex, store) {
									if (value == null) {
										return "";
									}
									var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
									return dt;
								}
							}]);
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
