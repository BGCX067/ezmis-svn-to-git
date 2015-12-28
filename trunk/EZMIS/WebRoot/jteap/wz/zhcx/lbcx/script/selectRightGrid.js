
//var columns0 = ['id','wzmc','jldw','jhdj','pjj','dqkc','je','yfpsl','dyx','zgcbde','zdcbde','wzlb.wzlbmc','abcfl','tsfl','kw.cwmc','cskc','tjm'];
var columns0 = ['id','wzmc','xhgg','jldw','jhdj','pjj','dqkc','wzlb.wzlbmc','kw.cwmc'];
/**
 * 字段列表
 */
RightGrid=function(){
    var defaultDs=this.getDefaultDS(link2);
    defaultDs.load();
    var grid=this;
    this.pageToolbar=new Ext.PagingToolbar({
	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
//		,items:['-',{text:'导出Excel',handler:function(){
//		exportExcel(grid,true);
//		}},'-','<font color="red"></font>']
	});
	RightGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getThisColumnModel(),
		sm: this.sm,
	    margins:'2px 2px 2px 2px',
		width:600,

		height:300,
		loadMask: true,
		frame:false,
		region:'center',
		tbar:this.pageToolbar
	});	
	
	//当有用户被选择的时候，显示工具栏的修改和删除按钮
	this.getSelectionModel().on("selectionchange",function(oCheckboxSModel){
		var select=rightGrid.getSelectionModel().getSelected();
		if(select){
			window.returnValue =  select.json; //select.json.wzmc+"|"+select.json.id;
		}
	});
	
	this.on("celldblclick",function(grid, rowIndex, columnIndex, e){
		var select=rightGrid.getSelectionModel().getSelected();
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SWZDA&docid="+select.json.id+"&st=02";
		showIFModule(url,"物资档案维护","true",800,600,{},null,null,null,false,"auto");
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
	            id: 'id'
	        }, columns0
	        ),
	        baseParams :{formSn:formSn},
	        remoteSort: true	
	    });
		return ds;
	},
    
	/**
	 * PersonAction 列模型
	 */
	getThisColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,		    	
		         {id:'wzmc',header: "物资名称", width: 100, sortable: true, dataindex: 'wzmc'},
		        {id:'xhgg',header: "型号规格", width: 100, sortable: true, dataindex: 'xhgg'},
		        {id:'jldw',header: "记量单位", width: 80, sortable: true, dataindex: 'jldw'},
 				{id:'jhdj',header: "计划单价", width: 80, sortable: true, dataindex: 'jhdj'},
	            {id:'pjj',header: "平均价", width: 80, sortable: true, dataindex: 'pjj'},
	            {id:'dqkc',header: "当前库存", width: 80, sortable: true, dataindex: 'dqkc'},
//	            {id:'je',header: "金额", width: 80, sortable: true,dataindex:'je',renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
//	            	return record.data["dqkc"]*record.data["jhdj"];
//	            }},
//	            {id:'yfpsl',header: "已分配量", width: 80, sortable: true, dataindex: 'yfpsl'},
//	            {id:'dyx',header: "代用型", width: 80, sortable: true, dataindex: 'dyx'},
//	            {id:'zgcbde',header: "高储", width: 80, sortable: true, dataindex: 'zgcbde'},
//	            {id:'zdcbde',header: "低储", width: 80, sortable: true, dataindex: 'zdcbde'},
	            {id:'wzlbbm',header: "物资类别", width: 80, sortable: true, dataindex: 'wzlb.wzlbmc'},
//	            {id:'abcfl',header: "abc分类", width: 80, sortable: true, dataindex: 'abcfl'},
//	            {id:'tsfl',header: "特殊分类", width: 80, sortable: true, dataindex: 'tsfl'},
	            {id:'kwbm',header: "库位", width: 80, sortable: true, dataindex: 'kw.cwmc'}
//	            {id:'cskc',header: "初始库存", width: 80, sortable: true, dataindex: 'cskc'},
//	            {id:'tjm',header: "统计码", width: 80, sortable: true, dataindex: 'tjm'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getThisColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	} 
});
//var columns1=  [1,2,3,4,5,6];
//var columns2=  [1,2,5,7];
//var columns3=  [1,7,8,9,10,11,12,13,14,15];

//左边的树 右边的grid
var rightGrid = new RightGrid();