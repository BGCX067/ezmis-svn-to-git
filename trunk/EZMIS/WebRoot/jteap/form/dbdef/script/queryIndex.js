var mainToolbar = new Ext.Toolbar({height:26,items:[
		{disabled:true,id:'btnAddTable',text:'添加表',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:Ext.emptyFn}}
	]
});

var queryGrid=new QueryGrid();
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[queryGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}
/**
 *  var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'columncode',header: "字段名", width: 120, sortable: true, dataIndex: 'columncode'},
		        {id:'columnname',header: "字段中文名", width: 200, sortable: true, dataIndex: 'columnname'},
				{id:'columntype',header: "字段类型", width: 120, sortable: true, dataIndex: 'columntype'},
				{id:'columnlength',header: "字段长度", width: 120, sortable: true, dataIndex: 'columnlength'},
				{id:'pk',header: "主键", width: 120, sortable: true, dataIndex: 'pk',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						if(value==true)
							return "<span style='color:red'>★</span>";
						else
							return "";
		   			}
		   		}
			]);
 */
/**
 * 初始化
 */
function initPage(tableid){
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中，请稍等"}); 
	myMask.show();
	var cm;
	//判断是否已经存在表定义对象
 	Ext.Ajax.request({

 		url:link1+"?tableid="+tableid,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			var msg=responseObject.msg;
 			if(responseObject.success){
 				
 				var cols=responseObject.data;
 				var cm=buildCm(cols);
 				var ds=buildDs(cols,tableid);
 				queryGrid.updateData(cm,ds);
 			}else{
 				alert("数据查询失败");
 			}
 			myMask.hide();
 		},
 		failure:function(){
 			alert("连接超时，数据查询失败");
 			myMask.hide();
 		},
 		method:'POST'
 	})
}
//构建列模型对象
function buildCm(cols){
	var tmpArray=new Array();
	tmpArray.push(new Ext.grid.CheckboxSelectionModel());
	for(var i=0;i<cols.length;i++){
		var tmpCm={"id":cols[i].columncode,"header":cols[i].columnname,"width":120,"sortable":true,"dataIndex":cols[i].columncode};
		tmpArray.push(tmpCm);
	}
	
	cm=new Ext.grid.ColumnModel(tmpArray);
	return cm;
}
//构建数据源对象
function buildDs(cols,tableid){
	var colArray=new Array();
	for(var i=0;i<cols.length;i++){
		colArray.push(cols[i].columncode);
	}
	
	var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: link2+"?tableid="+tableid
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount'
        }, colArray),
        remoteSort: true	
    });
	return ds;
}