var eformAttachFields = ["id","name","type","path","creator","creatdt","size"];
var EFormAttachRec = Ext.data.Record.create(eformAttachFields);
/**
 * 删除附件全局过渡方法
 */
function eformAttachGrid_doDelete(editorId,fjId){
	var editor = getEditor(editorId);
	editor.grid.doDeleteFj(fjId);
}
/**
 * 调用grid组件的下载附件成员方法
 */
function eformAttachGrid_doDownload(editorId,fjId){
	var editor = getEditor(editorId);
	editor.grid.downLoadFj(fjId);
}
/**
 * ************** 内容附件列表 **************
 */
EFormAttachGrid = function(config) {
	var grid = this;
	this.editorId = config.editorId;
	this.isEdit = config.isEdit;	//是否编辑状态 如果是编辑状态，只能下载，无法删除
	Ext.QuickTips.init();
	var cm = new Ext.grid.ColumnModel([{
		header : "图标",
		dataIndex : 'type',
		width : 22,
		renderer : function(value) {
			var icon = "";
			if (value == ".docx" || value == ".doc") {
				icon = "word.gif";
			} else if (value == ".xlsx" || value == ".xls") {
				icon = "excel.gif";
			} else if (value == ".gif" || value == ".jpg" || value == ".bmp") {
				icon = "img.gif";
			} else if (value == ".pdf") {
				icon = "pdf.gif";
			} else if (value == ".zip" || value == ".rar") {
				icon = "zip.gif";
			} else if (value == ".txt") {
				icon = "txt.gif";
			} else {
				icon = "other.gif";
			}
			var url = contextPath + "/jteap/form/eform/icon/"+icon;
			return "<img align='absmiddle' src='"+url+"' />";
		}
	}, {
		header : "附件名称",
		dataIndex : 'name',
		width : 210,
		renderer:function(value){
			return "<span ext:qtip='"+value+"' ext:qtitle='文件名'>"+value+"</span>";
		}
	}, {
		header : "附件大小",
		dataIndex : 'size',
		width : 60,
		align : 'right',
		renderer:function(value){
			return formatFileSize(value);
		}
	}, {
		header : "操作",
		dataIndex : 'op',
		width : 80,
		renderer:function(value,metadata ,record ){
			var html = "<a href=\"javascript:eformAttachGrid_doDownload('"+grid.editorId+"','"+record.data.id+"')\">下载</a>";
			if(grid.isEdit){
				html = "<a href=\"javascript:eformAttachGrid_doDelete('"+grid.editorId+"','"+record.data.id+"')\">删除</a> &nbsp;" + html;
			}
			return html;
		}
	}]);
	var defaultDs = null;
	var docid = config.docid;
	if(docid!=null && docid!=''){
		var url = contextPath + "/jteap/form/eform/EFormFjAction!showListAction.do?docid="+docid;
		defaultDs = this.getDefaultDS(url);
		defaultDs.load();
	}else{
		defaultDs = this.getDefaultDS(url);
	}
	
	Ext.apply(config,{
		loadMask : true,
		cm : cm,
		store : defaultDs
	});
	EFormAttachGrid.superclass.constructor.call(this, config);
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		if($bool(select.data.isNew) != true){
			var id = select.data.id;
			grid.downLoadFj(id);
		}
	});
}

Ext.extend(EFormAttachGrid, Ext.grid.GridPanel, {
	/**
	 * 取得默认数据源 返回数据格式
	 */
	getDefaultDS : function(url) {
		var ds = new Ext.data.Store({
			proxy : new Ext.data.ScriptTagProxy( {
				url : url
			}),
			baseParams :{PAGE_FLAG:'PAGE_FLAG_NO'},
			reader : new Ext.data.JsonReader( {
				root : 'list',
				totalProperty : 'totalCount',
				id : 'id'
			}, eformAttachFields),
			remoteSort : true
		});
		return ds;
	},
	/**
	 * 删除指定的附件
	 */
	doDeleteFj:function(id){
		var grid = this;
		var items = this.getStore().query("id",id);
		if(items.length>0){
			var fjRecord = items.get(0);
			if(confirm("确认删除当前指定的附件吗？")){
				var url = "";
				if(fjRecord.data.isNew && fjRecord.data.isNew == true){
					url = contextPath + "/jteap/form/eform/EFormFjAction!deleteEFormTempFjAction.do";
				}else{
					url = contextPath + "/jteap/form/eform/EFormFjAction!deleteEFormFjAction.do";
				}
				$ajax_syn(url,{id:fjRecord.data.id},function(ajax){
					var responseObject = ajax.responseText.evalJSON();
					if(responseObject.success == true){
						alert('删除成功');
					}
				});
				this.getStore().remove(fjRecord);
			}
		}
	},
	downLoadFj:function(id){
		var url = contextPath + "/jteap/form/eform/EFormFjAction!downloadFj.do?id="+id;
		window.open(url);		
	}
})
