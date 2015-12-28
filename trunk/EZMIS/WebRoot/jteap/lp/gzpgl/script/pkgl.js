GzpGrid = function() {
	var ds = new Ext.data.Store({
		proxy : new Ext.data.ScriptTagProxy({
			url : link13 + "?pfl=gzp&pzt=1"
		}),

		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount',
			id : 'id'
		}, ["id", "pfl", "pmc", "cjbm", "cjr", "cjsj", "pzt", "gllch", "bzsm"]),
		remoteSort : true
	});
	ds.load();

	var cm = new Ext.grid.ColumnModel([{
		id : 'pmc',
		header : "票名称",
		width : 250,
		sortable : true,
		dataIndex : 'pmc'
	}, {
		id : 'cjbm',
		header : "创建部门",
		width : 100,
		sortable : true,
		dataIndex : 'cjbm'
	}, {
		id : 'cjr',
		header : "创建人",
		width : 80,
		sortable : true,
		dataIndex : 'cjr'
	}, {
		id : 'cjsj',
		header : "创建时间",
		width : 140,
		sortable : true,
		dataIndex : 'cjsj',
		renderer : function(value, metadata, record, rowIndex, colIndex, store) {
			var cjsj = (value == null) ? "" : formatDate(new Date(value["time"]), "yyyy-MM-dd");
			return cjsj;
		}
	}, {
		id : 'pzt',
		header : "票状态",
		width : 80,
		sortable : true,
		dataIndex : 'pzt',
		renderer : function(value, metadata, record, rowIndex, colIndex, store) {
			if (value == "1") {
				return "有效";
			} else if (value == "2") {
				return "无效"
			}
		}
	}]);
	GzpGrid.superclass.constructor.call(this, {
		ds : ds,
		cm : cm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		region : 'center'
	});
}

Ext.extend(GzpGrid, Ext.grid.GridPanel, {})

var gzpGrid = new GzpGrid();
var panel = new Ext.Panel({
	layout : 'fit',
	region : 'center',
	items : [gzpGrid],
	buttons : [{
		text : '确定',
		handler : function() {
			var record = gzpGrid.getSelectionModel().getSelected();
			if (!record) {
				alert('请选择一张工作票!');
				return;
			}
			var flowConfigNm = record.get('gllch');
			Ext.Ajax.request({
				url : link14,
				params : {
					flowConfigNm : flowConfigNm
				},
				method : 'POST',
				success : function(ajax) {
					var resText = ajax.responseText;
					var obj = Ext.decode(resText);
					if (obj.success) {
						var flowConfigId = obj.flowConfigId;
						var url = link8;
						var url = url + "?flowConfigId=" + flowConfigId;
						var title = "工作票申请";
						var args = "url|" + url + ";title|" + title;
						var result = showModule(link9, "yes", 1100, 650, args);
						window.returnValue = true;
						window.close()
					} else {
						alert(obj.msg);
					}
				},
				failure : function() {
					alert('数据库异常，请联系管理员！');
				}
			})
		}
	}, {
		text : '取消',
		handler : function() {
			window.close();
		}
	}]
})