
var nsrsbh = "370112749888888";
/**
 * 创建Grid对象
 */
function createGrid(){
    var record = Ext.data.Record.create([
   		{name: 'name'},
     	{name: 'size'},
     	{name: 'path'},
     	{name: 'count'},
     	{name: '_is_leaf', type: 'bool'},
     	{name: '_parent'}
   	]);
	var store = new Ext.ux.maximgb.treegrid.AdjacencyListStore({  
	    autoLoad : true,  
	    baseParams :{nsrsbh:nsrsbh},
	    url: contextPath+'/jteap/yx/nsrinfo/NsrInfoAction!showNsrYxDirInfoAction.do',
	    reader: new Ext.data.JsonReader(  
	                {  
	                    id: 'path',  
	                    root: 'data',  
	                    totalProperty: 'total',  
	                    successProperty: 'success'
	                },   
	                record  
	    )  
	});  
    // create the Grid
    var grid = new Ext.ux.maximgb.treegrid.GridPanel({
      store: store,
      border:false,
      master_column_id : 'name',
      columns: [
			{id:'name',header: "文件结构", width: 100, sortable: false, dataIndex: 'name',renderer:function(value, metadata, record,
									rowIndex, colIndex, store){
				var isdir = record.json.isdir;
				var imgsrc;
				if(isdir==true){
					imgsrc = contextPath+"/resources/icon/folder.gif";
				}else{
					imgsrc = contextPath+"/resources/icon/icon_16.gif";
				}
				return "<img src='"+imgsrc+"' align='absmiddle' height=15></img>&nbsp;"+record.json.name;
			}},
        	{header: "大小", align:'right',width: 100, sortable: false, dataIndex: 'size',renderer:function(value){
        		return formatFileSize(value);
        	}},
        	{header: "文件个数", align:'right', width: 80, sortable: false, dataIndex: 'count'}
      ],
      stripeRows: true,
      enableHdMenu:false,
      root_title: nsrsbh, 
      autoExpandColumn: 'name',
      viewConfig : {
      	enableRowBody : true
      }
    });
    
    return grid;
}

var grid = createGrid();
// 中间
var lyCenter = {
	layout : 'fit',
	id : 'center-panel',
	region : 'center',
	minSize : 175,
	maxSize : 400,
	border : false,
	margins : '0 0 0 0',
	items : [grid]
}


