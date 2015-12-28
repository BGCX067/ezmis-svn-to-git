/**
 * 字段列表
 */
RightGrid=function(){
    var url = link1 + "?queryParamsSql= to_char(a.CBSJ,'yyyy-MM-dd')>= '"+ MonthFirstDay + "' and to_char(a.CBSJ,'yyyy-MM-dd')<= '"+nowYmd+" ' "+"&formSn="+formSn;
    var defaultDs=this.getDefaultDS(url);
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据",
			items : ['-', {
			text : '导出Excel',
			handler : function() {
				exportExcel(grid, false);
			}
		}, '-', '<font color="red">*双击查看详细信息</font>']
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
		tbar:this.pageToolbar
	});	
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=grid.getSelectionModel().getSelections()[0];
		var id = select.get("ID");
		var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&nowYmd="+nowYmd+
					"&docid="+id+"&st=02";
		var myTitle = "查询记录";
		var fw = new $FW({
			url:url,
			width:750,
			height:582,			
			id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
			type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
			title: myTitle,					//标题
			status: false,					//状态栏
			toolbar:false,					//工具栏
			scrollbars:false,				//滚动条
			menubar:false,					//菜单栏
			userIF:false,					//是否采用Iframe套框,为解决模式窗口无法刷新的问题
			resizable:false,				//是否支持调整大小
			callback:function(retValue){	//回调函数
			    rightGrid.getStore().reload();
			}
		});
		fw.show();
	});
	
	// 当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange", function(oCheckboxSModel) {
		var btnModi = mainToolbar.items.get('btnModi');
		var btnDel = mainToolbar.items.get('btnDel');
		
		if (oCheckboxSModel.getSelections().length < 1) {
			if (btnModi){
				btnModi.setDisabled(true);
			}
			if(btnDel){
				btnDel.setDisabled(true);
			}
		} else {
			if (oCheckboxSModel.getSelections().length == 1) {
				if (btnModi){
					btnModi.setDisabled(false);
				}
				if(btnDel){
					btnDel.setDisabled(false);
				}
			} else {
				if (btnModi){
					btnModi.setDisabled(true);
				}
				if(btnDel){
					btnDel.setDisabled(false);
				}
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
		var proFilds = ["ID","CBR","CBSJ","JZ","ZBZB","ZBBC","BKHFDL","YY"];
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'ID'
	        }, proFilds),
	        remoteSort: true
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm =  new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'id',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'cbr',header: "填写人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'cbsj',header: "填写时间", width: 150, sortable: true, dataIndex: 'CBSJ',
				    renderer:function(value){var time=formatDate(new Date(value), "yyyy-MM-dd hh:mm:ss");return time;}},
				{id:'jz',header: "机组", width: 80, sortable: true, dataIndex: 'JZ'},
				{id:'zbzb',header: "值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'zbbc',header: "班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'bkhfdl',header: "被考核发电量", width: 100, sortable: true, dataIndex: 'BKHFDL'},
				{id:'yy',header: "原因", width: 150, sortable: true, dataIndex: 'YY'}
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
