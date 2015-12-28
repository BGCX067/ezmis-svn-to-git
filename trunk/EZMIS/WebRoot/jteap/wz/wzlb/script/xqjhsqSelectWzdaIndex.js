
var mainToolbar = null;
//左边的树
var leftTree=new LeftTree();
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="物资名称#wzmc#textField,型号规格#xhgg#textField,物资编码#wzbm#textField".split(",");
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="物资名称#wzmc#textField,型号规格#xhgg#textField,物资编码#wzbm#textField".split(",");
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs,labelWidth:60,txtWidth:120});

var docid = "";   //新增的物资id
var wzda = {};    //新增的物资对象
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,rightGrid],
//	items:[rightPanel],
	buttons:[{text:'新增物资',handler:function(){
		var select = leftTree.getSelectionModel().getSelectedNode();
		var obj = {};
		var wzlbbm = "";
		var wzlbmc = "";
		if(select){
			wzlbbm = select.id;
			wzlbmc = select.text;
		}
		obj.wzlbbm = wzlbbm;
		obj.wzlbmc = wzlbmc;
		
		//通过物资类别编码获取当前物资对应仓库的缺省库位编码
		Ext.Ajax.request({
			url:link12,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			obj.cwbm = responseObject.cwbm;
		 			obj.cwmc = responseObject.cwmc;
					var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SWZDA&wzlbbm="+wzlbbm+"&xqjhsqmxid="+xqjhsqmxid;
					docid = showIFModule(url,"新增物资处理","true",680,500,obj,null,null,null,false,"no");
					rightGrid.getStore().reload();
		 		}else{
		 			if(typeof(responseObject.cwbm) == 'undefined' || typeof(responseObject.cwmc) == 'undefined'){
		 				obj.cwbm = "";
			 			obj.cwmc = "";
						var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SWZDA&wzlbbm="+wzlbbm+"&xqjhsqmxid="+xqjhsqmxid;
						docid = showIFModule(url,"新增物资处理","true",680,500,obj,null,null,null,false,"no");
						rightGrid.changeToListDS(link2+"?docid="+docid);
						rightGrid.getStore().reload();
						rightGrid.store.on("load",function(){ rightGrid.getSelectionModel().selectFirstRow();});
						//window.setTimeout("rightGrid.getSelectionModel().selectFirstRow();", 2000);
		 			}else{
			 			alert("没有选择匹配物资!");
			 			return ;
		 			}
		 		}				
			},
		 	failure:function(){
		 		alert("没有选择匹配物资!");
		 		return ;
		 	},
		 	method:'POST',
		 	params: {wzlbbm:wzlbbm}//Ext.util.JSON.encode(selections.keys)			
		});
		
	}},{text:'确定',handler:function(){
		//选择了匹配的物资
		if(typeof(rightGrid.getSelectionModel().getSelected()) != "undefined"){
			window.returnValue = rightGrid.getSelectionModel().getSelected().data;
			window.close();
		}else{    //没有选择匹配的物资
			if(typeof(docid) == "undefined" || docid == ""){
				alert("请选择匹配的物资!");
				return ;
			}else{
				Ext.Ajax.request({
					url:link11,
					success:function(ajax){
				 		var responseText=ajax.responseText;	
				 		var responseObject=Ext.util.JSON.decode(responseText);
				 		if(responseObject.success){
							var obj = {};
							obj.wzmc = responseObject.wzmc;
							obj.xhgg = responseObject.xhgg;
							obj.id = responseObject.wzdaid;
							window.returnValue = obj;
							window.close();
				 		}else{
				 			alert("没有选择匹配物资!");
				 			return ;
				 		}				
					},
				 	failure:function(){
				 		alert("没有选择匹配物资!");
				 		return ;
				 	},
				 	method:'POST',
				 	params: {docid:docid}//Ext.util.JSON.encode(selections.keys)			
				});
			}
		}
		//return;
		//rightGrid.getSelectionModel().selectRow(0);
		//window.returnValue = rightGrid.getSelectionModel().getSelected();
	}}]
}
