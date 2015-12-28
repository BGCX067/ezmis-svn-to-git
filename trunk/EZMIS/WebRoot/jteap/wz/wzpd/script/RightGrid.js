/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    var grid=this;
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,
		height:300,
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar,
		stripeRows: true
	});	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelections()[0];
		if(select){
		    if(select.json==null){
		    	return;
		    }
			var xhgg = "";
			if(select.json.xhgg!=null){
				xhgg = select.json.xhgg;
			}
			var url=link6+"?wzmc="+encodeURIComponent(select.json.wzmc)+"&xhgg="+encodeURIComponent(xhgg);
			new $FW({url:url,height:645,width:800,type:'T1',userIF:true,baseParam:{}}).show();	//模式对话框
        
		}
	});
}
Ext.extend(RightGrid, Ext.grid.GridPanel, {
	sm:new Ext.grid.CheckboxSelectionModel(),
	
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            //totalProperty: 'totalCount',
	            id: 'wzid'
	        }, 
	        ["wzid","wzmc","xhgg","kw","pjj","dqkc","dqje","zksj"]),
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
	    		this.sm,
		      {header: "物资名称", width: 200, sortable: false, dataIndex: 'wzmc'},
        	  {header: "型号规格", width: 200, sortable: false, dataIndex: 'xhgg'},
        	  {header: "库位", width: 130, sortable: false, dataIndex: 'kw'},
        	  {header: "平均价", width: 130, sortable: false, dataIndex: 'pjj'},
        	  {header: "当前库存", width: 80, sortable: false, dataIndex: 'dqkc'},
			  {header: "库存金额", width: 130, sortable: false, dataIndex: 'dqje'},
			  {header: "在库时间", width: 80, sortable: false, dataIndex: 'zksj'}
			]);
		return cm;
	} ,
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);
		var cm=this.getColumnModel();
		this.reconfigure(ds,cm);
	}

});

