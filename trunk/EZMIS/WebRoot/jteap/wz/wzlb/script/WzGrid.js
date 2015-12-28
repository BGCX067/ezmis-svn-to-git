/*****************comboGrid********************/

var columns0 = ['id','wzmc','jldw','jhdj','pjj','dqkc','je','yfpsl','dyx','zgcbde','zdcbde','wzlb.wzlbmc','abcfl','tsfl','kw.cwmc','cskc','csjg','tjm'];
/**
 * 字段列表
 */
WzGrid=function(){
	var url = contextPath + "/jteap/wz/wzda/WzdaAction!showListAction.do";
    var defaultDs=this.getDefaultDS(url);
    defaultDs.load();
    var wzGrid=this;
//    this.pageToolbar=new Ext.PagingToolbar({
//	    pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
//	    store: defaultDs,
//	    displayInfo: true,
//	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
//		emptyMsg: "没有符合条件的数据",
//		items:['-',{text:'导出Excel',handler:function(){
//		//exportExcel(wzGrid,true);
//			var path = wzGrid.store.proxy.url;
//			var paths = path.split("?")
//			var temp1 = paths[0]
//			var temp3 = paths[1]
//			temp1 = temp1.split("!")[0]
//			path = temp1 + "!exportWzDAExcel.do?" + temp3
//			window.open(path);
//		}},'-','<font color="red">*双击查看详细信息</font>']
//	});
//	WzGrid.superclass.constructor.call(this,{
//	 	ds: defaultDs,
//	 	cm: this.getThisColumnModel(),
//		sm: this.sm,
//	    margins:'2px 2px 2px 2px',
//		width:600,
//
//		height:300,
//		loadMask: true,
//		frame:false,
//		region:'center',
//		tbar:this.pageToolbar
//	});	
	
//    var fieldTxt=new Ext.form.TextField();
//	fieldTxt.on("specialkey",function(field,e){
//		if(e.getKey()==Ext.EventObject.ENTER){
//			wzGrid.combogrid.grid.getStore().filter("person.userLoginName",field.getValue(),true);	
//		}
//	})
	
    
    
//	this.combogrid=new Ext.app.ComboGrid({
	WzGrid.superclass.constructor.call(this,{
		xtype:'combogrid',
		ds:defaultDs,
		cm:this.getThisColumnModel(),
		dropDownRefreshData:true,//每次下拉框都需要更新数据
		gridWidth:380,
		gridHeight:250,
		valueField:'person.userLoginName',
		displayField:'person.userName',
		//listWidth:300,
		fieldLabel:'父角色',
		name:'parentRole',
		anchor:'90%',
		dataUrl:'combogrid.json',
		tbar:["用户名:",{text:'查询',
		handler:function(){
			wzGrid.combogrid.grid.getStore().filter("person.userLoginName",fieldTxt.getValue(),true);									  
	  	}},'-','<font color="red">*双击条目选择</font>']
	});
}
Ext.extend(WzGrid, Ext.grid.GridPanel, {
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
	       // baseParams :{formSn:formSn},
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
		        {id:'jldw',header: "记量单位", width: 80, sortable: true, dataindex: 'jldw'},
	            {id:'jhdj',header: "计划单价", width: 80, sortable: true, dataindex: 'jhdj'},
	            {id:'pjj',header: "平均价", width: 80, sortable: true, dataindex: 'pjj'},
	            {id:'dqkc',header: "当前库存", width: 80, sortable: true, dataindex: 'dqkc'},
	            {id:'je',header: "金额", width: 80, sortable: true,dataindex:'je',renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	            	return record.data["dqkc"]*record.data["jhdj"];
	            }},
	            {id:'yfpsl',header: "已分配量", width: 80, sortable: true, dataindex: 'yfpsl'},
	            {id:'dyx',header: "代用型", width: 80, sortable: true, dataindex: 'dyx'},
	            {id:'zgcbde',header: "高储", width: 80, sortable: true, dataindex: 'zgcbde'},
	            {id:'zdcbde',header: "低储", width: 80, sortable: true, dataindex: 'zdcbde'},
	            {id:'wzlbbm',header: "物资类别", width: 80, sortable: true, dataindex: 'wzlb.cwmc'},
	            {id:'abcfl',header: "abc分类", width: 80, sortable: true, dataindex: 'abcfl'},
	            {id:'tsfl',header: "特殊分类", width: 80, sortable: true, dataindex: 'tsfl'},
	            {id:'kwbm',header: "库位", width: 80, sortable: true, dataindex: 'kw.kwmc'},
	            {id:'cskc',header: "初始库存", width: 80, sortable: true, dataindex: 'cskc'},
	            {id:'csjg',header: "初始价格", width: 80, sortable: true, dataindex: 'csjg'},
	            {id:'tjm',header: "统计码", width: 80, sortable: true, dataindex: 'tjm'}
			]);
		return cm;
	},
	/**
	 * 切换数据源->LogAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getDefaultDS(url);	
		var cm=this.getThisColumnModel();
//		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	} 
});
var columns1=  [1,2,3,4,5,6];
var columns2=  [1,2,5,7];
var columns3=  [1,7,8,9,10,11,12,13,14,15,16];

