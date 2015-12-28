var dicts_sb = $dictListAjax("QXZY_SB");
var dicts_ry = $dictListAjax("QXZY_RY");
var dicts = dicts_sb.concat(dicts_ry);
function getQxzyCnName(qxzy) {
	for (var i = 0;i < dicts.length; i++) {
		var dict = dicts[i];
		if (dict.value == qxzy) {
			return dict.key;
		}
	}
}

// 当前时间
var nowDate = new Date();

// 当前日期
var nowYmd = nowDate.format('Y-m-d H:i');

var year = nowDate.getFullYear();
var month = nowDate.getMonth()+1;
var endmonth =month;
if (endmonth < 10) {
	endmonth = "0" + endmonth;
}
var endDay = year + "-" + endmonth + "-" + "25 00:00";
if(month=='01'){
	year=year-1;
	month=12;
}else{
	month=month-1;
}
if(month<10){
	month="0"+month;
}
var firstDay = year + "-" + month + "-" + "25 00:00";

/**
 * 字段列表
 */
RightGrid = function() {
	var url = link1 + "?queryParamsSql= a.fxsj <= to_date('" + endDay
			+ "','yyyy-MM-dd hh24:mi') and a.fxsj >=to_date('" + firstDay + "','yyyy-MM-dd hh24:mi')";
	var defaultDs = this.getDefaultDS(url);
	defaultDs.load();
	var grid = this;
	this.pageToolbar = new Ext.PagingToolbar({
		pageSize : CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据",
		items : ['-', {
			text : '导出Excel',
			handler : function() {
				exportExcel(grid, false);
			}
		}, '-', '<font color="red">*双击查看详细信息</font>','-',
		'<a href="#" target="_self" title="绿色：及时消缺的缺陷，即验收时间小于预期消缺时间的缺陷；&#10橘黄色：未及时完成的缺陷，即验收时间大于预期消缺时间的缺陷；&#10红色：在预期消缺时间内未及时消缺的缺陷；&#10灰色：已完成，且延期过的缺陷；&#10蓝色：缺陷延期申请中的缺陷；&#10宝蓝色：已延期的缺陷；&#10黑色：未超过预期消缺时间的待消缺的缺陷；&#10酱红色：待验收且超过预期消缺时间的缺陷；&#10深紫色：作废的缺陷">颜色示意</a>'
		]
	});
	///jteap/system/doclib/generate/13106997119469.html
	RightGrid.superclass.constructor.call(this, {
		ds : defaultDs,
		cm : this.getColumnModel(),
		sm : this.sm,
		margins : '2px 2px 2px 2px',
		width : 600,
		height : 300,
		loadMask : true,
		frame : true,
		region : 'center',
		tbar : this.pageToolbar
	});

	this.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var select = grid.getSelectionModel().getSelections()[0];
//		var url = link2;
		var windowUrl = link3 + "?pid=" + select.get("ID_") + "&status=false";
//		var args = "url|" + windowUrl + ";title|" + '查看流程';
//		var retValue = showModule(url, "yes", 800, 600, args);
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
	});

}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm : new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	}),

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
			}, ["ID_", "VERSION_", "START_", "END_", "PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID", "FLOW_FORM_ID",
					"ZRBM", "SBMC", "QXMC", "QXFL", "FXR", "FXSJ", "XQSJ","STATUS", "ID", "YQXQSJ", "YQBZ","JZBH","QXDBH","JXBQRJG","XQR","SPSJ"]),
			remoteSort : true
		});
		ds.setDefaultSort('FXSJ', 'desc');
		return ds;
	},

	/**
	 * PersonAction 列模型
	 */
	getColumnModel : function() {
		var cm = new Ext.grid.ColumnModel([
	
		{
			id : 'QXDBH',
			header : "缺陷单编号",
			width : 130,
			sortable : true,
			dataIndex : 'QXDBH',
			renderer : function(value, meta, record) {
				return renderFont(record, value);
			}
		},{
			id : 'JZBH',
			header : "机组编号",
			width : 60,
			sortable : true,
			dataIndex : 'JZBH',
			renderer : function(value, meta, record) {
			return value==null?'':renderFont(record, value);
			}
		},{
			id : 'SBMC',
			header : "设备名称",
			width : 180,
			sortable : true,
			dataIndex : 'SBMC',
			renderer : function(value, meta, record) {
				return renderFont(record, value);
			}
		}, {
			id : 'QXMC',
			header : "缺陷名称",
			width : 200,
			sortable : true,
			dataIndex : 'QXMC',
			renderer : function(value, meta, record) {
				return renderFont(record, value);
			}
		}, {
			id : 'ZRBM',
			header : "责任班组",
			width : 70,
			sortable : true,
			dataIndex : 'ZRBM',
			renderer : function(value, meta, record) {
    //  		var zrbmCnName = getZrbmCnName(value);
				return renderFont(record, value);
			}
		}, 
		{
			id : 'XRQ',
			header : "消缺人",
			width : 70,
			sortable : true,
			dataIndex : 'XQR',
			renderer : function(value, meta, record) {
			    if(value == null) return '';
			    else
				return renderFont(record, value);
			}
		},{
			id : 'QXFL',
			header : "缺陷分类",
			width : 70,
			sortable : true,
			dataIndex : 'QXFL',
			renderer : function(value, meta, record) {
				return renderFont(record, value);
			}
		}, {
			id : 'FXR',
			header : "发现人",
			width : 70,
			sortable : true,
			dataIndex : 'FXR',
			renderer : function(value, meta, record) {
				return renderFont(record, value);
			}
		}, {
			id : 'FXSJ',
			header : "发现时间",
			width : 150,
			sortable : true,
			dataIndex : 'FXSJ',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				var dt = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
				return renderFont(record, dt);
			}
		},{
			id : 'XQSJ',
			header : "消缺时间",
			width : 150,
			sortable : true,
			dataIndex : 'XQSJ',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
			var time = formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");
			if(value<31536000000){ 
				return '';
			}else{
				return renderFont(record, time);
				}
			}
		}, {
			id : 'STATUS',
			header : "缺陷状态",
			width : 70,
			sortable : true,
			dataIndex : 'STATUS',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				return renderFont(record, value);
			}
		}
		//JXBQRJG
		, {
			id : 'JXBQRJG',
			header : "转发结果",
			width : 300,
			sortable : true,
			dataIndex : 'JXBQRJG',
			renderer : function(value, metadata, record, rowIndex, colIndex, store) {
				return value == null?'':renderFont(record, value);
			}
		}
		]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS : function(url) {
		var ds = this.getDefaultDS(url);
		var cm = this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds, cm);
	}

});
function renderFont(record, value) {
	var yxqsj = record.get('YQXQSJ');
	var xqsj = record.get('XQSJ');
	var status = record.get('STATUS');
	var dateFormat = "yyyy-MM-dd HH:mm:ss";
	var now = formatDate(new Date(), dateFormat);
	var dtYxqsj = yxqsj ? formatDate(new Date(yxqsj), dateFormat) : "";
	var dtXqsj = xqsj ? formatDate(new Date(xqsj), dateFormat) : "";
	if (status == '已完成') {
		//if (compareDates(dtXqsj, dateFormat, dtYxqsj, dateFormat) == 1) {
		if(yxqsj>xqsj){
			return "<font color='green'>" + value + "</font>";
		} else {
		    if (record.get('SPSJ') == null)
			return "<font color='B8860B'>" + value + "</font>";
			else return "<font color='#778899'>" + value + "</font>";
		}
	} else {if(record.get('STATUS')=='已作废') return "<font color='330066'>" + value + "</font>";
			 else if(record.get('STATUS')=='已延期') return "<font color='#00CED1'>" + value + "</font>";
			  		else if (record.get('YQBZ') == '延期') 
						 return "<font color='blue'>" + value + "</font>";
					            else if (compareDates(now, dateFormat, dtYxqsj, dateFormat) == 1) 
					                      if(record.get('STATUS') == '待验收')
					                         return "<font color='#800000'>" + value + "</font>";
					                          else 
						                          return "<font color='red'>" + value + "</font>";
	 }
	return value;
}
