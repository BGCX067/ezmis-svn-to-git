/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link1);
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: 20,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
		items:['-']
	});
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
//	this.getView().on("beforerefresh",function(v){
////		debugger
//		v.scrollLeft = v.scroller.dom.scrollLeft;
//		v.scrollWidth = v.scroller.dom.scrollWidth;
//	});
	this.getView().on("refresh",function(v){
//			debugger
//		   v.scroller.dom.scrollLeft = 1000;
////			v.scroller.dom.scrollTop =2000;
			var store = grid.getStore();
		 	var jsonDatas = store.reader.jsonData;
		 	
		 	if(jsonDatas!=null){
		 		var pageTotal = jsonDatas.pageTotal;
				var mainTotal = jsonDatas.mainTotal;
				//添加到当前页合计数据
	 
				if(pageTotal.last==null&&pageTotal.id!=null){
			  		store.add(new store.recordType({
			  			id:mainTotal.id,
			  			sqjcsl:"<font color='blue'>"+pageTotal.sqjcsl+"</font>",
			  			sqjcje:"<font color='blue'>"+pageTotal.sqjcje+"</font>",
			  			bqsrsl:"<font color='blue'>"+pageTotal.bqsrsl+"</font>",
			  			bqsrje:"<font color='blue'>"+pageTotal.bqsrje+"</font>",
			  			bqzcsl:"<font color='blue'>"+pageTotal.bqzcsl+"</font>",
			  			bqzcje:"<font color='blue'>"+pageTotal.bqzcje+"</font>",
			  			bqzrsl:"<font color='red'>"+pageTotal.bqzrsl+"</font>",
			  			bqzrje:"<font color='red'>"+pageTotal.bqzrje+"</font>",
			  			bqjysl:"<font color='blue'>"+pageTotal.bqjysl+"</font>",
			  			bqjyje:"<font color='blue'>"+pageTotal.bqjyje+"</font>",
			  			wzmc:pageTotal.wzmc
			  			})
			  		);
				}
				//添加总条数合计数据
				if(mainTotal.id!=null){
					store.add(new store.recordType({
			  			id:mainTotal.id,
			  			sqjcsl:"<font color='red'>"+mainTotal.sqjcsl+"</font>",
			  			sqjcje:"<font color='red'>"+mainTotal.sqjcje+"</font>",
			  			bqsrsl:"<font color='red'>"+mainTotal.bqsrsl+"</font>",
			  			bqsrje:"<font color='red'>"+mainTotal.bqsrje+"</font>",
			  			bqzcsl:"<font color='red'>"+mainTotal.bqzcsl+"</font>",
			  			bqzcje:"<font color='red'>"+mainTotal.bqzcje+"</font>",
  						bqzrsl:"<font color='red'>"+mainTotal.bqzrsl+"</font>",
			  			bqzrje:"<font color='red'>"+mainTotal.bqzrje+"</font>",
			  			bqjysl:"<font color='red'>"+mainTotal.bqjysl+"</font>",
			  			bqjyje:"<font color='red'>"+mainTotal.bqjyje+"</font>",
			  			wzmc:mainTotal.wzmc
			  			})
			  		);
				} 
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
	            totalProperty: 'totalCount',
	            id: 'ID'
	        }, 
	        ["id","sqjcsl","sqjcje","bqsrsl","bqsrje","bqzcsl","bqzcje","bqjysl","bqjyje","bqzrsl","bqzrje",
				 "wzmc","xhgg","jldw","jhdj","cwmc","yf"]),
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		      {header: "物资名称", width: 100, sortable: true, dataIndex: 'wzmc'},
        	  {header: "型号规格", width: 100, sortable: true, dataIndex: 'xhgg'},
        	  {header: "计量单位", width: 60, sortable: true, dataIndex: 'jldw'},
        	  {header: "计划单价", width: 60, sortable: true, dataIndex: 'jhdj'},
        	  {header: "库位", width: 80, sortable: true, dataIndex: 'cwmc'},
        	  {header: "上期结存数量", width: 80, sortable: true, dataIndex: 'sqjcsl'},
        	  {header: "上期结存金额", width: 80, sortable: true, dataIndex: 'sqjcje'},
			  {header: "本期收入数量", width: 80, sortable: true, dataIndex: 'bqsrsl'},
			  {header: "本期收入金额", width: 80, sortable: true, dataIndex: 'bqsrje'},
			  {header: "本期支出数量", width: 80, sortable: true, dataIndex: 'bqzcsl'},
			  {header: "本期支出金额", width: 80, sortable: true, dataIndex: 'bqzcje'},
			  {header: "本期转入数量", width: 80, sortable: true, dataIndex: 'bqzrsl'},
			  {header: "本期转入金额", width: 80, sortable: true, dataIndex: 'bqzrje'},
			  {header: "本期结余数量", width: 80, sortable: true, dataIndex: 'bqjysl'},
			  {header: "本期结余金额", width: 80, sortable: true, dataIndex: 'bqjyje'}
			]);
		return cm;
	},
	//实时查询对应的数据源
	getSscxDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'ID'
	        }, 
	        ["id","sqjcsl","zjllgcxm","zjllbz","sqjcje","bqsrsl","bqsrje","bqzcsl","bqzcje","bqjysl","bqjyje",
				 "wzmc","xhgg","jldw","jhdj","cwmc","yf"]),
	        remoteSort: true	
	    });
		return ds;
	},
	//实时查询对应的列模型
	getSscxColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		      {header: "物资名称", width: 100, sortable: true, dataIndex: 'wzmc'},
        	  {header: "型号规格", width: 100, sortable: true, dataIndex: 'xhgg'},
        	  {header: "计量单位", width: 60, sortable: true, dataIndex: 'jldw'},
        	  {header: "计划单价", width: 60, sortable: true, dataIndex: 'jhdj'},
        	  {header: "库位", width: 80, sortable: true, dataIndex: 'cwmc'},
        	  {header: "最近需求班组", width: 100, sortable: true, dataIndex: 'zjllbz'},
        	  {header: "最近需求项目", width: 100, sortable: true, dataIndex: 'zjllgcxm'},
        	  {header: "上期结存数量", width: 80, sortable: true, dataIndex: 'sqjcsl'},
        	  {header: "上期结存金额", width: 80, sortable: true, dataIndex: 'sqjcje'},
			  {header: "本期收入数量", width: 80, sortable: true, dataIndex: 'bqsrsl'},
			  {header: "本期收入金额", width: 80, sortable: true, dataIndex: 'bqsrje'},
			  {header: "本期支出数量", width: 80, sortable: true, dataIndex: 'bqzcsl'},
			  {header: "本期支出金额", width: 80, sortable: true, dataIndex: 'bqzcje'},
			  {header: "本期结余数量", width: 80, sortable: true, dataIndex: 'bqjysl'},
			  {header: "本期结余金额", width: 80, sortable: true, dataIndex: 'bqjyje'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);
		var cm=this.getColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	}

});

