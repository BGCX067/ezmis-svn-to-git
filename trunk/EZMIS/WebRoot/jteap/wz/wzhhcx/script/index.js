
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,
	items:[]
});

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
}

/**
 * 添加EForm表单
 */
function btnAdd_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	
	var result=showModule(link6,true,800,645);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
}
/**
 * 修改表单
 */
function btnModify_Click(){
	var select=rightGrid.getSelectionModel().getSelections()[0];
	if(select){
		var url=link6+"?id="+select.json.id;
		result=showModule(url,true,800,645);
		if(result=="true"){
			rightGrid.getStore().reload();
		}
	}
	
}

/**
 * 删除表单
 */
function btnDel_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.deleteSelect(select);
}


/**
 * 删除分类
 */
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

/**
 * 添加分类
 */
function btnAddCatalog_Click(){
	leftTree.createNode();
}

/**
 * 修改分类
 */
function btnModiCatalog_Click(){
	leftTree.modifyNode();
}





	
//左边的树 右边的grid


//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="物资名称#wz_replace#wzdagl,型号规格#xhgg#textField,时间#Start#dateField,至#End#dateField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="物资名称#wz_replace#wzdagl,型号规格#xhgg#textField,时间#Start#dateField,至#End#dateField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,title:'物资混合查询',labelWidth:80});

var rightGrid=new RightGrid();

//物资grid
var wzdaGrid = new WzdaGrid();	
var wzCenter={
	layout:'border',
	id:'wzCenter',
	title:'基本信息',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[wzdaGrid]
}

//入库grid
var rkGrid = new RkGrid();
var rkCenter={
	layout:'border',
	id:'rkGrid',
	title:'入库',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rkGrid]
}

//分配grid
var fpGrid = new FpGrid();
var fpCenter={
	layout:'border',
	id:'fpGrid',
	title:'分配',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[fpGrid]
}

//需求grid
var xqGrid = new XqGrid();
var xqCenter={
	layout:'border',
	id:'xqGrid',
	title:'需求',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[xqGrid]
}

//采购grid
var cgGrid = new CgGrid();
var cgCenter={
	layout:'border',
	id:'cgGrid',
	title:'采购',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[cgGrid]
}

//验货单grid
var slGrid = new SlGrid();
var slCenter={
	layout:'border',
	id:'slGrid',
	title:'收料',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[slGrid]
}

//领料单grid
var llGrid = new LlGrid();
var llCenter={
	layout:'border',
	id:'llGrid',
	title:'领料',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[llGrid]
}

//调入单grid
var drGrid = new DrGrid();
var drCenter={
	layout:'border',
	id:'drGrid',
	title:'调入',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[drGrid]
}

//调出单grid
var dcGrid = new DcGrid();
var dcCenter={
	layout:'border',
	id:'dcGrid',
	title:'调出',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[dcGrid]
}

//退料单grid
var tlGrid = new TlGrid();
var tlCenter={
	layout:'border',
	id:'tlGrid',
	title:'退料',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[tlGrid]
}

//销售单grid
var xsGrid = new XsGrid();
var xsCenter={
	layout:'border',
	id:'xsGrid',
	title:'销售',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[xsGrid]
}


//中间
var showCenter={
	layout:'border',
	id:'center-panel',
	title:'基本信息',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rkGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [searchPanel]
}
var WzdaTabPanel = function(config){
	var wzdaTabPanel = this;
	this.wzCenter = wzCenter;
	//this.rkCenter = rkCenter;
	this.fpCenter = fpCenter;
	this.xqCenter = xqCenter;
	this.cgCenter = cgCenter;
	this.slCenter = slCenter;
	this.llCenter = llCenter;
	//this.drCenter = drCenter;
	//this.dcCenter = dcCenter;
	//this.tlCenter = tlCenter;
	//this.xsCenter = xsCenter;
	
	WzdaTabPanel.superclass.constructor.call(this, {
		region : 'center',
		width : 180,
		border : true,
		margins : '5 5 5 5',
		cmargins : '0 0 0 0',
		activeTab : 0,
		autoTabs  :true,
		layoutOnTabChange :true,
		items : [	wzdaTabPanel.wzCenter,
					//wzdaTabPanel.rkCenter,
					wzdaTabPanel.fpCenter,
					wzdaTabPanel.xqCenter,
					wzdaTabPanel.cgCenter,
					wzdaTabPanel.slCenter,
					wzdaTabPanel.llCenter
					//wzdaTabPanel.drCenter,
					//wzdaTabPanel.dcCenter,
					//wzdaTabPanel.tlCenter,
					//wzdaTabPanel.xsCenter
				]
	});
}
Ext.extend(	WzdaTabPanel,Ext.TabPanel,{})
var wzdaTabPanel = new WzdaTabPanel();
var setTab = function(tabPanel,currenttab,curIndex){
	var items = tabPanel.items.items;
	while(true){
		if(curIndex<1)return ;
		currenttab.on('render',function(tmp){
			setTab(tabPanel,items[curIndex-1],curIndex-1);
		});
		tabPanel.setActiveTab(0);
	}
}
wzdaTabPanel.on("render",function(a){
	//var items = a.items.items;
	//var lnegth = a.items.length;
	//setTab(a,items[lnegth-1],lnegth-1);	
	
		var b = a.getItem("wzCenter");
		b.on('render',function(c){
			//a.add(rkCenter);
			//var rk = a.getItem("rkGrid");
			//rk.on('render',function(crk){
				var fp = a.getItem("fpGrid");
				fp.on('render',function(crk){
					var xq = a.getItem("xqGrid");
					xq.on('render',function(crk){
						var cg = a.getItem("cgGrid");
						cg.on('render',function(crk){
							var sl = a.getItem("slGrid");
							sl.on('render',function(crk){
								var ll = a.getItem("llGrid");
								ll.on('render',function(crk){
								/**
									var dr = a.getItem("drGrid");
									dr.on('render',function(crk){
										var dc = a.getItem("dcGrid");
										dc.on('render',function(crk){
											var tl = a.getItem("tlGrid");
											tl.on('render',function(crk){
												var xs = a.getItem("xsGrid");
												xs.on('render',function(crk){
													
												});
												a.setActiveTab(xs);
											});
											a.setActiveTab(tl);
										});
										a.setActiveTab(dc);
									});
									a.setActiveTab(dr);
									**/
								});
								a.setActiveTab(ll);
							});
							a.setActiveTab(sl);
						});
						a.setActiveTab(cg);
					});
					a.setActiveTab(xq);
				});
				a.setActiveTab(fp);
			//});
			//a.setActiveTab(rk);
		});
		//
		 
		 
	}
	
);
/*
wzdaTabPanel.activate('llGrid');
wzdaTabPanel.activate('slGrid');
wzdaTabPanel.activate('cgGrid');
wzdaTabPanel.activate('xqGrid');
wzdaTabPanel.activate('fpGrid');
wzdaTabPanel.activate('rkGrid');
wzdaTabPanel.activate('wzCenter');
*/
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,wzdaTabPanel]
}
