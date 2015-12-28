var SJFL_NIAN="NIAN";
var SJFL_YUE="YUE";
var SJFL_RI="RI";
var SJFL_JZ="JZ";
//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
//	items:[
//		{disabled:true,id:'btnAdd',text:'新建',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnModify',text:'修改',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:mainToolBarButtonClick}},
//		{disabled:true,id:'btnDel',text:'删除',cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',listeners:{click:mainToolBarButtonClick}}
//		]
});
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnAddCatalog'||
	   button.id=='btnModiCatalog'){ 
		button.setDisabled(true);
	}
}

/**
 * 新建数据源
 */
function btnAddCatalog_Click(){
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	var flid=oNode.id;
	var param={};
	param.kid=kid;
	param.flid=flid;
	//查看该模块是否能操作
	AjaxRequest_Sync(link10,param,function(stateRes){
		var stateText = stateRes.responseText;
		var stateObj = stateText.evalJSON();
		if(stateObj.success){
			var operate=stateObj.operate;
			//状态可用
			if(operate){
				//查看数据表是否有数据
				AjaxRequest_Sync(link11,param,function(isExistRes){
					var isExistText = isExistRes.responseText;
					var isExistObj = isExistText.evalJSON();
					if(isExistObj.success){
						var oper=isExistObj.operate;
						//有数据的情况下
						if(oper){
							AjaxRequest_Sync(link13,param,function(dateRes){
								var dateText = dateRes.responseText;
								var dateObj = dateText.evalJSON();
								if(dateObj.success){
									var initData=dateObj.initData;
									showSaveOrUpdatePage(initData,kid,flid);
								}else{
									alert("初始化日期失败!");
								}
							});
						}else{
						//没数据的情况
							//弹出接口页面让用户选择
							var result=showModule(link12+"?kid="+kid,"yes",300,150);
							if(result!=undefined){
								showSaveOrUpdatePage(result,kid,flid);
							}else{
								AjaxRequest_Sync(link15,param,function(operRes){});
							}
						}
					}
				});
			}else{
				alert("该模块正被使用，请稍后再试!");
			}
		}else{
			alert("操作异常!");
		}
	});
}
/**
 * 补录数据
 */
function btnloadDate_Click(){
	var win = new Ext.Window({
		title:"请选择日期：",
		width:250,
		height:200,
		items:[{xtype:"datefield",id:"dates",format:"Y-m-d",x:40,y:50,width:160,height:120}],
		buttons:[{text:"确定",handler:function(){
			if($('dates').value!=""){
				Ext.Ajax.request({
					url:contextPath +"/jteap/jhtj/dnb/dnbBbAction!saveDnbBbAction.do",
					success:function(ajax){
						var responseText=ajax.responseText;	
		 				var responseObject=Ext.util.JSON.decode(responseText);
		 				if(responseObject.success){
				 			 alert("补录成功");
				 		}else{
				 			 alert("补录失败");
				 		}	
				 		win.close();
					},
					failure:function(){
				 		alert("提交失败");
				 	},
				 	method:'POST',
				 	params: {strDt:$('dates').value}//Ext.util.JSON.encode(selections.keys)		
				});
			}else{
				alert("请选择日期！");
				return;
			}
		}},{text:"取消",handler:function(){win.close();}}],
		buttonAlign:"center"
	});
	win.show();
}


function showSaveOrUpdatePage(data,kid,flid){
	var result=showModule(link14+"?kid="+kid+"&initdata="+data+"&flid="+flid,"yes",800,550);
	if(result!=undefined){
		leftTree.initTable(kid,flid,"");
	}
	var param={};
	param.kid=kid;
	AjaxRequest_Sync(link15,param,function(operRes){});
}

/**
 * 修改数据源
 */
function btnModiCatalog_Click(){
	var title="数据源维护";
	var oNode=leftTree.getSelectionModel().getSelectedNode();
	var kid=oNode.attributes.kid;
	var flid=oNode.id;
	var param={};
	param.kid=kid;
	param.flid=flid;
	
	//查看该模块是否能操作
	AjaxRequest_Sync(link10,param,function(stateRes){
		var stateText = stateRes.responseText;
		var stateObj = stateText.evalJSON();
		if(stateObj.success){
			var operate=stateObj.operate;
			//状态可用
			if(operate){
				var select=queryGrid.getSelectionModel().getSelections()[0];
				var initData="";
				if(select.json.NIAN!=undefined){
					initData=initData+SJFL_NIAN+","+select.json.NIAN+"!";
				}
				if(select.json.YUE!=undefined){
					initData=initData+SJFL_YUE+","+select.json.YUE+"!";
				}
				if(select.json.RI!=undefined){
					initData=initData+SJFL_RI+","+select.json.RI+"!";
				}
				if(select.json.JZ!=undefined){
					initData=initData+SJFL_JZ+","+select.json.JZ+"!";
				}
				if(initData!=""){
					initData=initData.substring(0,initData.length-1);
				}
				showSaveOrUpdatePage(initData,kid,flid);
			}else{
				alert("该模块正被使用，请稍后再试!");
			}
		}
	});
}



	
//左边的树 右边的grid

var leftTree=new LeftTree();
var queryGrid=new QueryGrid();

//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:"",searchAllFs:""});


//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,queryGrid]
}
//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


