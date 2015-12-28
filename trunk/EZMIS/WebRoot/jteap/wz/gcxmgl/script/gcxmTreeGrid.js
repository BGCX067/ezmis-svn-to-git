
/**
 * 创建Grid对象
 */
function kwTreeGrid(){
	
    var record = Ext.data.Record.create([
   		{name: 'id'},
     	{name: 'text'},
     	{name: 'bz'},
     	{name: 'wzzjm'},
     	{name: 'projcode'},
     	{name: 'finished'},
     	{name: 'execdept'},
     	{name: 'c_c_fybm'},
     	{name: 'starttime_plan'},
     	{name: 'endtime_plan'},
     	{name: 'starttime_fact'},
     	{name: 'endtime_fact'},
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
			{id:'text',header: "项目名称", width: 600, sortable: false, dataIndex: 'text'},
			{id:'execdept',header: "执行部门", width: 100, sortable: false, dataIndex: 'execdept'},	
			{id:'c_c_fybm',header: "费用归属", width: 100, sortable: false, dataIndex: 'c_c_fybm'},	
			{id:'finished',header: "状态", width: 80, sortable: false, dataIndex: 'finished',
				renderer:function(value,metadata,record,rowIndex,colIndex,store){
					if(value=='0'){
						return '未结束';
					}else{
						return '已结束';
					}
				}
			},
			{id:'starttime_plan',header: "计划开始时间", width: 80, sortable: false, dataIndex: 'starttime_plan',
				 renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;    
					}
			},
			{id:'endtime_plan',header: "计划结束时间", width: 80, sortable: false, dataIndex: 'endtime_plan',
				 renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;    
					}
			},
			{id:'starttime_fact',header: "实际开始时间", width: 80, sortable: false, dataIndex: 'starttime_fact',
				 renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;    
					}
			},
			{id:'endtime_fact',header: "实际结束时间", width: 80, sortable: false, dataIndex: 'endtime_fact',
				 renderer:function(value,metadata,record,rowIndex,colIndex,store){
				    	if(!value) return;
				    	var dt = formatDate(new Date(value["time"]),"yyyy-MM-dd"); 
						return dt;    
					}
			}
      ],
      stripeRows: true,
   //   enableHdMenu:false,
      root_title: "类别", 
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