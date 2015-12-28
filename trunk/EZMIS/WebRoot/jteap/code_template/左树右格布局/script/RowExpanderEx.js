RowExpanderEx = function() {
	var defaultDs = this.getDefaultDS(link1);
	var grid = this;
	var expander = this.getRowExpander();

	RowExpanderEx.superclass.constructor.call(this, {
				ds : defaultDs,
				cm : this.getColumnModel(),
				margins : '2px 2px 2px 2px',
				width : 600,
				height : 300,
				loadMask : true,
				frame : true,
				region : 'center',
				plugins : expander
			});

	/**
	 * 展开事件
	 */
	expander.on("expand", function(expander, r, body, rowIndex) {
				if (Ext.DomQuery.select("div.x-panel-bwrap", body).length == 0) {
					var path = link4 + "?nsrid=" + r.get("nsrid") + "&zlid="
							+ r.get("zlid");
					var param = {};
					param.nsrid = r.get("nsrid");
					param.zlid = r.get("zlid");
					// 同步请求数据
					AjaxRequest_Sync(link5, param, function(req) {
								var responseText = req.responseText;
								var responseObj = Ext.decode(responseText);
								if (responseObj.success) {
									var totalCount = responseObj.totalCount;
									totalCount = parseFloat(totalCount);
									if (totalCount > 10) {
										var rightGrid = new RightGrid(path,
												body, r.get("zlid"), 1);
										rightGrid.getStore().reload();
									} else {
										var rightGrid = new RightGrid(path,
												body, r.get("zlid"), 2);
										rightGrid.getStore().reload();
									}
								} else {
									Ext.MessageBox.alert('Status',
											responseObj.msg);
								}
							});
				}
			});
}
Ext.extend(RowExpanderEx, Ext.grid.GridPanel, {
			/**
			 * 取得默认数据源 返回数据格式
			 */
			getDefaultDS : function(url) {
				var ds = new Ext.data.Store({
							proxy : new Ext.data.ScriptTagProxy({
										url : url
									}),
							reader : new Ext.data.JsonReader({
										root : 'list',
										totalProperty : 'totalCount',
										id : 'id'
									}, ['id', 'nsrid', 'zlid', 'zlmc',
											'nsr.nsrname', 'nsr.nsrsfz']),
							remoteSort : true
						});
				return ds;
			},

			/**
			 * PersonAction 列模型
			 */
			getColumnModel : function() {
				var cm = new Ext.grid.ColumnModel([this.getRowExpander(), {
							id : 'zlmc',
							header : "资料名称",
							width : 220,
							sortable : true,
							dataIndex : 'zlmc'
						}, {
							id : 'zlid',
							header : "资料编号",
							width : 60,
							sortable : true,
							dataIndex : 'zlid'
						}, {
							id : 'nsrname',
							header : "纳税人姓名",
							width : 80,
							sortable : true,
							dataIndex : 'nsr.nsrname'
						}, {
							id : 'nsrid',
							header : "纳税人识别号",
							width : 120,
							sortable : true,
							dataIndex : 'nsrid'
						}, {
							id : 'nsrsfz',
							header : "纳税人身份证号",
							width : 160,
							sortable : true,
							dataIndex : 'nsr.nsrsfz'
						}]);
				return cm;
			},
			/**
			 * 切换数据源->LogAction!showList
			 */
			changeToListDS : function(url) {
				var ds = this.getDefaultDS(url);
				var cm = this.getColumnModel();
				// this.pageToolbar.bind(ds);
				this.reconfigure(ds, cm);
			},
			getRowExpander : function() {
				var expander = new Ext.grid.RowExpander({
							tpl : new Ext.XTemplate('<div class="detailData">',
									'', '</div>')
						});
				return expander;
			}
		});
