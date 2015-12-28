/**
 * 字段列表
 */
RightGrid=function(){
	var url = link1+"?formSn="+formSn+"&beginYmd="+Ext.getCmp("sf#beginYmd").getValue().format("Y-m-d")+
				"&endYmd="+Ext.getCmp("sf#endYmd").getValue().format("Y-m-d");
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
				exportExcel(grid, true);
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
		
		/*var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&nowYmd="+nowYmd+"&nowBc="+encodeURIComponent(nowBc)+"&docid="+id;
		var myTitle = "修改记录";
		var chooseSj = formatDate(new Date((select.data.CBSJ)),"yyyy-MM-dd");
		if(chooseSj != nowYmd || select.data.ZBBC != nowBc){
			url += "&st=02";
			myTitle = "查询记录";
		}*/
		
		var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&nowYmd="+nowYmd+
					"&nowBc="+encodeURIComponent(nowBc)+"&docid="+id+"&st=02";
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
		var proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR"];
		if(formSn == "TB_YX_FORM_LRLTJ1"){
			proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR",
			"A1GML","A2GML","B1GML","B2GML","C1GML","C2GML","D1GML","D2GML","RYLLJ"];
		}else if(formSn == "TB_YX_FORM_LRLTJ2"){
			proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR",
			"A1GML","A2GML","B1GML","B2GML","C1GML","C2GML","D1GML","D2GML","RYLLJ"];
		}else if(formSn == "TB_YX_FORM_LRLTJ3"){
			proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR",
			"A1GML","A2GML","B1GML","B2GML","C1GML","C2GML","D1GML","D2GML","E1GML","E2GML","F1GML","F2GML","RYLLJ","JYYB","HYYB"];
		}else if(formSn == "TB_YX_FORM_LRLTJ4"){
			proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR",
			"A1GML","A2GML","B1GML","B2GML","C1GML","C2GML","D1GML","D2GML","E1GML","E2GML","F1GML","F2GML","RYLLJ","JYYB","HYYB"];
		}else if(formSn == "TB_YX_FORM_FDTJ300"){
			proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR",
			"FDJDL_1","FDJDL_2"];
		}else if(formSn == "TB_YX_FORM_FDTJ600"){
			proFilds = ["ID","CBSJ","ZBBC","ZBZB","CBR","TIANXIEREN","TIANXIESHIJIAN","COMBOCBR",
			"FDJDL_3","FDJDL_4"];
		}
		
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, proFilds),
	        remoteSort: true
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getColumnModel:function(){
	    var cm = null;
	    
	    if(formSn == "TB_YX_FORM_LRLTJ1"){
			cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'CBSJ',header: "抄表时间", width: 120, sortable: true, dataIndex: 'CBSJ'},
				{id:'ZBBC',header: "值班班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'ZBZB',header: "值班值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'CBR',header: "抄表人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'A1GML',header: "A1给煤量", width: 80, sortable: true, dataIndex: 'A1GML'},
				{id:'A2GML',header: "A2给煤量", width: 80, sortable: true, dataIndex: 'A2GML'},
				{id:'B1GML',header: "B1给煤量", width: 80, sortable: true, dataIndex: 'B1GML'},
				{id:'B2GML',header: "B2给煤量", width: 80, sortable: true, dataIndex: 'B2GML'},
				{id:'C1GML',header: "C1给煤量", width: 80, sortable: true, dataIndex: 'C1GML'},
				{id:'C2GML',header: "C2给煤量", width: 80, sortable: true, dataIndex: 'C2GML'},
				{id:'D1GML',header: "D1给煤量", width: 80, sortable: true, dataIndex: 'D1GML'},
				{id:'D2GML',header: "D2给煤量", width: 80, sortable: true, dataIndex: 'D2GML'},
				{id:'RYLLJ',header: "燃油流量计", width: 80, sortable: true, dataIndex: 'RYLLJ'}
			]);
		}else if(formSn == "TB_YX_FORM_LRLTJ2"){
			cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'CBSJ',header: "抄表时间", width: 120, sortable: true, dataIndex: 'CBSJ'},
				{id:'ZBBC',header: "值班班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'ZBZB',header: "值班值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'CBR',header: "抄表人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'A1GML',header: "A1给煤量", width: 80, sortable: true, dataIndex: 'A1GML'},
				{id:'A2GML',header: "A2给煤量", width: 80, sortable: true, dataIndex: 'A2GML'},
				{id:'B1GML',header: "B1给煤量", width: 80, sortable: true, dataIndex: 'B1GML'},
				{id:'B2GML',header: "B2给煤量", width: 80, sortable: true, dataIndex: 'B2GML'},
				{id:'C1GML',header: "C1给煤量", width: 80, sortable: true, dataIndex: 'C1GML'},
				{id:'C2GML',header: "C2给煤量", width: 80, sortable: true, dataIndex: 'C2GML'},
				{id:'D1GML',header: "D1给煤量", width: 80, sortable: true, dataIndex: 'D1GML'},
				{id:'D2GML',header: "D2给煤量", width: 80, sortable: true, dataIndex: 'D2GML'},
				{id:'RYLLJ',header: "燃油流量计", width: 80, sortable: true, dataIndex: 'RYLLJ'}
			]);
		}else if(formSn == "TB_YX_FORM_LRLTJ3"){
			cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'CBSJ',header: "抄表时间", width: 120, sortable: true, dataIndex: 'CBSJ'},
				{id:'ZBBC',header: "值班班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'ZBZB',header: "值班值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'CBR',header: "抄表人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'A1GML',header: "A1给煤量", width: 80, sortable: true, dataIndex: 'A1GML'},
				{id:'A2GML',header: "A2给煤量", width: 80, sortable: true, dataIndex: 'A2GML'},
				{id:'B1GML',header: "B1给煤量", width: 80, sortable: true, dataIndex: 'B1GML'},
				{id:'B2GML',header: "B2给煤量", width: 80, sortable: true, dataIndex: 'B2GML'},
				{id:'C1GML',header: "C1给煤量", width: 80, sortable: true, dataIndex: 'C1GML'},
				{id:'C2GML',header: "C2给煤量", width: 80, sortable: true, dataIndex: 'C2GML'},
				{id:'D1GML',header: "D1给煤量", width: 80, sortable: true, dataIndex: 'D1GML'},
				{id:'D2GML',header: "D2给煤量", width: 80, sortable: true, dataIndex: 'D2GML'},
				{id:'E1GML',header: "E1给煤量", width: 80, sortable: true, dataIndex: 'E1GML'},
				{id:'E2GML',header: "E2给煤量", width: 80, sortable: true, dataIndex: 'E2GML'},
				{id:'F1GML',header: "F1给煤量", width: 80, sortable: true, dataIndex: 'F1GML'},
				{id:'F2GML',header: "F2给煤量", width: 80, sortable: true, dataIndex: 'F2GML'},
				{id:'RYLLJ',header: "燃油流量计", width: 80, sortable: true, dataIndex: 'RYLLJ'},
				{id:'JYYB',header: "进油油表", width: 80, sortable: true, dataIndex: 'JYYB'},
				{id:'HYYB',header: "回油油表", width: 80, sortable: true, dataIndex: 'HYYB'}
			]);
		}else if(formSn == "TB_YX_FORM_LRLTJ4"){
			cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'CBSJ',header: "抄表时间", width: 120, sortable: true, dataIndex: 'CBSJ'},
				{id:'ZBBC',header: "值班班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'ZBZB',header: "值班值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'CBR',header: "抄表人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'A1GML',header: "A1给煤量", width: 80, sortable: true, dataIndex: 'A1GML'},
				{id:'A2GML',header: "A2给煤量", width: 80, sortable: true, dataIndex: 'A2GML'},
				{id:'B1GML',header: "B1给煤量", width: 80, sortable: true, dataIndex: 'B1GML'},
				{id:'B2GML',header: "B2给煤量", width: 80, sortable: true, dataIndex: 'B2GML'},
				{id:'C1GML',header: "C1给煤量", width: 80, sortable: true, dataIndex: 'C1GML'},
				{id:'C2GML',header: "C2给煤量", width: 80, sortable: true, dataIndex: 'C2GML'},
				{id:'D1GML',header: "D1给煤量", width: 80, sortable: true, dataIndex: 'D1GML'},
				{id:'D2GML',header: "D2给煤量", width: 80, sortable: true, dataIndex: 'D2GML'},
				{id:'E1GML',header: "E1给煤量", width: 80, sortable: true, dataIndex: 'E1GML'},
				{id:'E2GML',header: "E2给煤量", width: 80, sortable: true, dataIndex: 'E2GML'},
				{id:'F1GML',header: "F1给煤量", width: 80, sortable: true, dataIndex: 'F1GML'},
				{id:'F2GML',header: "F2给煤量", width: 80, sortable: true, dataIndex: 'F2GML'},
				{id:'RYLLJ',header: "燃油流量计", width: 80, sortable: true, dataIndex: 'RYLLJ'},
				{id:'JYYB',header: "进油油表", width: 80, sortable: true, dataIndex: 'JYYB'},
				{id:'HYYB',header: "回油油表", width: 80, sortable: true, dataIndex: 'HYYB'}
			]);
		}else if(formSn == "TB_YX_FORM_FDTJ300"){
			cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'CBSJ',header: "抄表时间", width: 120, sortable: true, dataIndex: 'CBSJ'},
				{id:'ZBBC',header: "值班班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'ZBZB',header: "值班值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'CBR',header: "抄表人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'FDJDL_1',header: "#1发电机电量", width: 120, sortable: true, dataIndex: 'FDJDL_1'},
				{id:'FDJDL_2',header: "#2发电机电量", width: 120, sortable: true, dataIndex: 'FDJDL_2'}
			]);
		}else if(formSn == "TB_YX_FORM_FDTJ600"){
			cm = new Ext.grid.ColumnModel([
		    	this.sm,
				{id:'ID',header: "ID", width: 100, sortable: true, hidden:true, dataIndex: 'ID'},
				{id:'CBSJ',header: "抄表时间", width: 120, sortable: true, dataIndex: 'CBSJ'},
				{id:'ZBBC',header: "值班班次", width: 80, sortable: true, dataIndex: 'ZBBC'},
				{id:'ZBZB',header: "值班值别", width: 80, sortable: true, dataIndex: 'ZBZB'},
				{id:'CBR',header: "抄表人", width: 80, sortable: true, dataIndex: 'CBR'},
				{id:'FDJDL_3',header: "#3发电机电量", width: 120, sortable: true, dataIndex: 'FDJDL_3'},
				{id:'FDJDL_4',header: "#4发电机电量", width: 120, sortable: true, dataIndex: 'FDJDL_4'}
			]);
		}
		
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
