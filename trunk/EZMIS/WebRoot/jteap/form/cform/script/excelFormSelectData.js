var sql = window.dialogArguments.sql;		//查询Sql语句

/**
 * 字段列表
 */
DataGrid=function(){
    var grid=this;
	DataGrid.superclass.constructor.call(this,{
	 	ds: new Ext.data.Store(),
	 	cm: this.getColumnModel(),
		sm: this.sm,
		width:497,
		margin:"2px 2px 2px 2px",
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		bbar:['->',{text:'确定',handler:ok},{text:'取消',handler:function(){window.close();}}]
	});	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		window.returnValue = select.json;
		window.close();
	});
}
Ext.extend(DataGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.RowSelectionModel(),
	
	/**
	 * default 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	updateData:function(cm,ds){
		this.reconfigure(ds,cm);
		//this.store.reload();
	}

});

function ok(){
	var select=dataGrid.getSelectionModel().getSelections()[0];
	if(select==null){
		alert("请选择一条数据");
		return;
	}
	window.returnValue = select.json;
	window.close();
}



var readerModel = []; 
//构建列模型对象
function buildCm(responseObject){
	var tmpArray=new Array();
	var list = responseObject.list;
	if(list.length>0){
		readerModel = [];
		var item = list[0];
		for(var prop in item){
			var tmpCm={"id":prop,"header":prop,"width":120,"sortable":false,"dataIndex":prop};
			if(item.isDate !=null && item.isDate == true){
				tmpCm.renderer=function(value,metadata,record,rowIndex,colIndex,store){
						var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd hh:mm:ss"); 
						return dt;         
					};
			}
			readerModel.push({name:prop});
			tmpArray.push(tmpCm);
		}
		
	}
	cm=new Ext.grid.ColumnModel(tmpArray);
	return cm;
}
//构建数据源对象
function buildDs(responseObject){
	var list = responseObject.list;
	var record = Ext.data.Record.create(readerModel);
	var myReader = new Ext.data.JsonReader({
	    totalProperty: "totalCount",             // The property which contains the total dataset size (optional)
	    root: "list"                         // The property which contains an Array of row objects
	}, record);
	
    var store = new Ext.data.Store({
            reader: myReader,
            data: responseObject
        });
	return store;
}

var dataGrid=new DataGrid();
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	items:[dataGrid]
}
/**
 * 初始化
 */
function onload(){
	var cm;
	var link1 = contextPath + "/jteap/form/dbdef/PhysicTableAction!showTableDataBySqlAction.do";
	//判断是否已经存在表定义对象
 	Ext.Ajax.request({
 		url:link1,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			var msg=responseObject.msg;
 			if(responseObject.success){
 				var list=responseObject.list;
 				var cm=buildCm(responseObject);
 				var ds=buildDs(responseObject);
 				dataGrid.updateData(cm,ds);
 			}else{
 				alert("数据查询失败");
 			}
 			
 		},
 		failure:function(){
 			alert("连接超时，数据查询失败");
 		},
 		params:{sql:sql},
 		method:'POST'
 	})
}

var titlePanel=new Ext.app.TitlePanel({caption:'数据选择器',border:false,region:'north'});