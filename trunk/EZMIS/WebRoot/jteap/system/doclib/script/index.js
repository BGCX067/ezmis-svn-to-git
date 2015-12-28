//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
//4.添加字段
//5.修改字段
//6.删除字段
//7.文档级别
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
	/*items:[
		{disabled:false,id:'btnAddCatalog',text:'添加分类',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',listeners:{click:btnAddCatalog_Click}},
		{disabled:true,id:'btnModiCatalog',text:'修改分类',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnModiCatalog_Click}},
		{disabled:true,id:'btnDelCatalog',text:'删除分类',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelCatalog_Click}},
		{disabled:true,id:'btnAddDocument',text:'添加文档',cls: 'x-btn-text-icon',icon:'icon/icon_11.gif',listeners:{click:btnAddDocument_Click}},
		{disabled:true,id:'btnModiDocument',text:'修改文档',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnModiDocument_Click}},
		{disabled:true,id:'btnDelDocument',text:'删除文档',cls: 'x-btn-text-icon',icon:'icon/icon_12.gif',listeners:{click:btnDelDocument_Click}},
		{disabled:false,id:'btnDocumentLevel',text:'文档级别配置',cls: 'x-btn-text-icon',icon:'icon/icon_13.gif',listeners:{click:btnDocumentLevel_Click}}
		]
	*/
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiCatalog' || button.id=='btnDelCatalog' 
		|| button.id=='btnAddDocument' || button.id=='btnModiDocument'|| button.id=='btnAddCatalog'|| button.id=='btnDelDocument')
		button.setDisabled(true);
}

//添加分类
function btnAddCatalog_Click(){
	//leftTree.createNode();
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var doclibCatalogAddWindow=new DoclibCatalogAddWindow('');
	doclibCatalogAddWindow.show();
	doclibCatalogAddWindow.loadDoclibLevel();
	doclibCatalogAddWindow.loadTemplateList();
}

//修改分类
function btnModiCatalog_Click(){
	//leftTree.modifyNode();
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	if(oNode.isRootNode()){
		alert('无效模块，无法修改');
		return;
	}
	var doclibEditForm=new DoclibCatalogEditWindow(oNode.id);
	doclibEditForm.show();
	doclibEditForm.loadDoclibLevel();
	doclibEditForm.loadTemplateList();
	doclibEditForm.loadData();
	/*
	var url=contextPath+"/jteap/doclib/doclibCatalogEditForm.jsp?catalogId="+oNode.id;
	var result=showModule(url,true,800,645);
	if(result=="true"){
		rightGrid.getStore().reload();
	}*/
}

//删除分类
function btnDelCatalog_Click(){
	leftTree.deleteSelectedNode();
}

//添加文档
function btnAddDocument_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	//var doclibAddForm = new DoclibAddWindow(oNode.id);
	//doclibAddForm.show();
	//doclibAddForm.loadData();
    //window.open("./iform/adddoclibinfo.jsp","adddoclib","width=800,height=600,toolbar=no,location=no,menubar=no,top=100,left=100,status=no");
	var url=contextPath+"/jteap/system/doclib/doclibAddForm.jsp?catalogId="+oNode.id+"&catalogCode="+catalogCode;
	var sFeature = "dialogHeight:590px;dialogWidth:600px;resizable:no;status:yes;scroll:yes";
	var result=window.showModalDialog(url,"",sFeature);
	if(result=="true"){
		rightGrid.getStore().reload();
	}
}

//修改文档
function btnModiDocument_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var select=rightGrid.getSelectionModel().getSelections()[0];
	//得到文档分类的ID
	
	if(!oNode){
		Ext.Msg.alert('提示', "请选择一个分类节点!");
	}else if(!select){
		Ext.Msg.alert('提示', "请选择一个文档!");
	}else{
		//Ext.Msg.alert('提示', "马上转向编辑页面!");
		var catalogId = oNode.id;
		var url=contextPath+"/jteap/system/doclib/doclibEditForm.jsp?catalogId="+catalogId+"&doclibId="+select.get("id");
		var result=showModule(url,true,600,560);
		if(result=="true"){
			
		}
	}
	rightGrid.getStore().reload();
	/**
	Ext.Msg.show({
		title:'Are you modify?',
		msg:'You clicked btnModiDocument Button!'
	});
	**/
}

//删除文档
function btnDelDocument_Click(){
	rightGrid.deleteSelectedDoclib();
}
//文档级别
function btnDocumentLevel_Click(){
	var url=contextPath+"/jteap/system/doclib/doclevel/index.jsp" ;
	var result=showModule(url,true,580,500);
}



/**
 * 初始化
 */
function changeToListDS(catalogId,title,creator,createdt){
	//var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中，请稍等"}); 
	//myMask.show();
	//判断是否已经存在动态表定义对象
	var link = contextPath + "/jteap/doclib/DoclibAction!findDynaColumnsAction.do?catalogId="+catalogId+"&catalogCode='"+catalogCode+"'";
 	Ext.Ajax.request({
 		url:link,
 		success:function(ajax){
 			var responseText=ajax.responseText;	
 			//alert(responseText);
 			var responseObject=Ext.util.JSON.decode(responseText);
 			var msg=responseObject.msg;
 			if(responseObject.success){	
 				var cols=responseObject.data[0];
 				var cm=buildCm(cols);
 				var ds=buildDs(cols,catalogId,title,creator,createdt);
 				rightGrid.pageToolbar.bind(ds);
				rightGrid.reconfigure(ds,cm);
				rightGrid.store.reload();
 				//rightGrid.updateData(cm,ds);
 			}else{
 				alert("数据查询失败");
 			}
 			//myMask.hide();
 		},
 		failure:function(){
 			alert("连接超时，数据查询失败");
 			//myMask.hide();
 		},
 		method:'POST'
 	})
}
//构建列模型对象
function buildCm(cols){
	var tmpArray=new Array();
	tmpArray.push(new Ext.grid.CheckboxSelectionModel());
	for(var i=0;i<cols.length;i++){
		var data=cols[i];
		var tmpCm={};
		//条件字段
		if("id"==data.code){
			tmpCm={"id":data.code,"header":data.name,"width":60,"sortable":true,"dataIndex":data.code,"hidden":true,"menuDisabled":true};
		}else if("title"==data.code||"creator"==data.code||"createdt"==data.code){
			tmpCm={"id":data.code,"header":data.name,"width":data.len,"sortable":true,"dataIndex":data.code,"menuDisabled":true};
		}else{
			tmpCm={"id":data.name,"header":data.name,"width":data.len,"sortable":true,"dataIndex":data.name,"menuDisabled":true};
		}
		tmpArray.push(tmpCm);
	}
	cm=new Ext.grid.ColumnModel(tmpArray);
	return cm;
}

//构建数据源对象
function buildDs(cols,catalogId,title,creator,createdt){
	var colArray=new Array();
	for(var i=0;i<cols.length;i++){
		var data=cols[i];
		var columncode=data.code;
		columncode=columncode==""?data.name:columncode;
		colArray.push(columncode);
	}
	//alert(link4+"?catalogId="+catalogId+(title!=""?("&title="+title):"")+(creator!=""?("&creator="+creator):"")+(createdt!=""?("&createdt="+createdt):""));
	var ds = new Ext.data.Store({
        proxy: new Ext.data.ScriptTagProxy({
            url: link4+"?catalogCode="+catalogCode+"&catalogId="+catalogId+(title!=""?("&title="+title):"")+(creator!=""?("&creator="+creator):"")+(createdt!=""?("&createdt="+createdt):"")
        }),
        reader: new Ext.data.JsonReader({
            root: 'list',
            totalProperty: 'totalCount'
        }, colArray),
        remoteSort: true	
    });
	return ds;
}




//左边的树 右边的grid
var leftTree=new LeftTree();
//var rightGrid=new RightGrid();
var rightGrid=new QueryGrid();

//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});

//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rightGrid,searchPanel]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}
