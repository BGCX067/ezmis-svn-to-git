
/**
 * 创建Grid对象
 */
function createGrid(){
    // create the Grid
    var grid = new Ext.ux.maximgb.treegrid.GridPanel({
      store: getStore(link1),
      border:false,
      master_column_id : 'xmmc',
   	  cm:getColumns(),
      stripeRows: true,
      enableHdMenu:false,
      region:'center',
      viewConfig : {
      	enableRowBody : true
      }
    });
    
    return grid;
}
function getStore(url){
 	var record = Ext.data.Record.create([
   		{name: 'xmmc'},
     	{name: 'jg'},
     	{name: 'bp'},
     	{name: 'sb'},
     	{name: 'dl'},
     	{name: 'gc'},
     	{name: 'wj'},
     	{name: 'gj'},
     	{name: 'zh'},
     	{name: 'by'},
     	{name: 'yq'},
     	{name: 'path'},
     	{name: '_is_leaf', type: 'bool'},
     	{name: '_parent'} 
//     	'xmmc','jg','bp', 'sb','dl', 'gc','wj', 'gj', 'zh','by', 'yq','path','_is_leaf', '_parent'
   	]);
	var store = new Ext.ux.maximgb.treegrid.AdjacencyListStore({  
	    autoLoad : true,  
	    url: url,
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
	store.on('beforeexpandnode',function(st,node){              
//        url = link18;   
//		alert(rc);
		
        store.proxy.conn.url = link1+"?parentId="+node.path;
    }); 
	return store;
}
function getColumns(){
	 var cm=new Ext.grid.ColumnModel( [
			{id:"xmmc",header: "名字",width:200,sortable: false, dataIndex: 'xmmc'},
        	{header: "加工库", align:'right',width: 70, sortable: false, dataIndex: 'jg'},
	     	{header: "备品库", align:'right',width: 70, sortable: false, dataIndex: 'bp'},
	     	{header: "设备库", align:'right',width: 70, sortable: false, dataIndex: 'sb'},
	     	{header: "电料库", align:'right',width: 70, sortable: false, dataIndex: 'dl'},
	     	{header: "钢材库", align:'right',width: 70, sortable: false, dataIndex: 'gc'},
	     	{header: "五金库", align:'right',width: 70, sortable: false, dataIndex: 'wj'},
	     	{header: "工具库", align:'right',width: 70, sortable: false, dataIndex: 'gj'},
	     	{header: "综合库", align:'right',width: 80, sortable: false, dataIndex: 'zh'},
	     	{header: "备品一库", align:'right',width: 90, sortable: false, dataIndex: 'by'},
	     	{header: "仪器仪表库", align:'right',width: 100, sortable: false, dataIndex: 'yq'}
      ])
     return cm;
}
