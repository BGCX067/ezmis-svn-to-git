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
		items:['-',{text:'打印封面',handler:function(){
			var selections = rightGrid.getSelections();//获取被选中的行
			var select = rightGrid.getSelectionModel().selections;
			var idsArr = new Array();
			//Excel导出（创建一个表单以"POST"方式提交，避免ids多长）
			var url = link7;
			var f = document.createElement("form");
			f.name = "aaa";
			f.target="newWindow"; 
			f.method="post";
			document.body.appendChild(f);
			var input = document.createElement("input");
			input.type = "hidden";
			if(select && select.length > 0){
				for(var i =0; i < select.length; i++){
					idsArr.push("'"+select.items[i].json.id+"'");
				}
				input.value = idsArr;
				input.name = "id";		
			}else{
			 	input.value = parWhere;
				input.name = "parWhere";	
			}
			f.appendChild(input);
			f.action = url;
			f.onsubmit=function(){
				window.open("about:blank","newWindow","");
			}
			f.submit();
		}}]
	});
	/*this.pageToolbar.on("beforerender",function(){
	alert('xxx');
			var store = grid.getStore();
			var record = new store.recordType({id:'11111'});
			store.add(record);
		//store.insert(2, new store.recordType({id: "11111", yckc: 222,rkd:2,rkje:2,ckd:2,ckje:2,ymkc:2,ckmc:2,yf:2}));
	});*/
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
	
	this.getView().on("refresh",function(){
			var store = grid.getStore();
		 	var jsonDatas = store.reader.jsonData;
		 	if(jsonDatas!=null){
		 		var pageTotal = jsonDatas.pageTotal;
				var mainTotal = jsonDatas.mainTotal;
				
				//添加到当前页合计数据
	 
				if(pageTotal.last==null&&pageTotal.id!=null){
			  		store.add(new store.recordType({
			  			id:pageTotal.id,
			  			yckc:"<font color='blue'>"+pageTotal.yckc+"</font>",
			  			ymkc:"<font color='blue'>"+pageTotal.ymkc+"</font>",
			  			rkd:"<font color='blue'>"+pageTotal.rkd+"</font>",
			  			ckd:"<font color='blue'>"+pageTotal.ckd+"</font>",
			  			rkje:"<font color='blue'>"+pageTotal.rkje+"</font>",
			  			ckje:"<font color='blue'>"+pageTotal.ckje+"</font>",
			  			zrje:"<font color='red'>"+pageTotal.zrje+"</font>",
			  			ckmc:pageTotal.ckmc
			  			})
			  		);
				}
				//添加总条数合计数据
				if(mainTotal.id!=null){
					store.add(new store.recordType({
			  			id:mainTotal.id,
			  			yckc:"<font color='red'>"+mainTotal.yckc+"</font>",
			  			ymkc:"<font color='red'>"+mainTotal.ymkc+"</font>",
			  			rkd:"<font color='red'>"+mainTotal.rkd+"</font>",
			  			ckd:"<font color='red'>"+mainTotal.ckd+"</font>",
			  			rkje:"<font color='red'>"+mainTotal.rkje+"</font>",
			  			ckje:"<font color='red'>"+mainTotal.ckje+"</font>",
			  			zrje:"<font color='red'>"+mainTotal.zrje+"</font>",
			  			ckmc:mainTotal.ckmc
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
	        ["id","yckc","rkd","rkje","ckd","ckje","ymkc","ckmc","yf","zrje"]),
	        remoteSort: true	
	    });
	    
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		      {id:'ck.ckmc',header: "仓库名称", width: 100, sortable: true, dataIndex: 'ckmc'},
        	  {id:'ycrk',header: "月初库存", width: 110, sortable: true, dataIndex: 'yckc'},
        	  {id:'rkd',header: "入库单(张)", width: 110, sortable: true, dataIndex: 'rkd'},
        	  {id:'rkje',header: "入库金额(元)", width: 110, sortable: true, dataIndex: 'rkje'},
        	  {id:'ckd',header: "出库单(张)", width: 110, sortable: true, dataIndex: 'ckd'},
        	  {id:'ckje',header: "出库金额(元)", width: 110, sortable: true, dataIndex: 'ckje'},
        	  {id:'zrje',header: "本期转入(元)", width: 110, sortable: true, dataIndex: 'zrje'}, //此列 2011-10月份统计使用 后续统计注释 并删除相对应的 dataindex
        	  {id:'ymkc',header: "月末库存", width: 110, sortable: true, dataIndex: 'ymkc'}
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

