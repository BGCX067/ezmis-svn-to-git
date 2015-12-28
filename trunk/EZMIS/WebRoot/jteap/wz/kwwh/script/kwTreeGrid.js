
/**
 * 创建Grid对象
 */
function kwTreeGrid(){
	
    var record = Ext.data.Record.create([
   		{name: 'id'},
     	{name: 'text'},
     	{name: 'bz'},
     	{name: 'wzzjm'},
     	{name: '_parent'},
     	{name: '_is_leaf', type: 'bool'}
   	]);
	var store = new Ext.ux.maximgb.treegrid.AdjacencyListStore({  
	    autoLoad : true,  
	    url: link19,
	    reader: new Ext.data.JsonReader(  
	                {    
	                	id:'id',
	              		total:'total',
	                    root: 'data'
	                },   
	                record  
	    )  
	});
	store.on('beforeexpandnode',function(st,node){              
//        url = link18;   
//		alert(rc);
        store.proxy.conn.url = link19+"?parentId="+node.id;
    });  
	var sm=new Ext.grid.CheckboxSelectionModel();
     
    // create the Grid
    var grid = new Ext.ux.maximgb.treegrid.GridPanel({
      store: store,
      border:false,
      region:'center',
      loadMask: false,
      margins:'2px 2px 2px 2px',
      master_column_id : 'text',
      columns: [
      		 sm,
			{id:'text',header: "库位名称", width: 200, sortable: false, dataIndex: 'text'},
			{id:'wzzjm',header: "位置助记码", width: 200, sortable: false, dataIndex: 'wzzjm'},	
			{id:'bz',header: "备注", width: 200, sortable: false, dataIndex: 'bz'}			
      ],
      stripeRows: true,
   //   enableHdMenu:false,
      root_title: "仓库", 
      autoExpandColumn: 'text',
      viewConfig : {enableRowBody : true}
    });
    
     /**
     * 当选中节点的时候，触发事件
     */
    grid.getSelectionModel().on("selectionchange",function(oSM,oNode){    			
    	if(oSM.getSelected())
    	window.returnValue=oSM.getSelected().json.text+'|'+oSM.getSelected().json.id;
    
    });
    return grid;
}