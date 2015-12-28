//工具栏  
//1.添加分类
//2.修改分类
//3.删除分类
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
	/*items:[
		{disabled:false,id:'btnAddIp',text:'添加IP规则',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnAddIp_Click}},
		{disabled:true,id:'btnModiIpLock',text:'修改规则',cls: 'x-btn-text-icon',icon:'icon/icon_4.gif',listeners:{click:btnModiIpLock_Click}},
		{disabled:true,id:'btnDelIpLock',text:'删除规则',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnDelIpLock_Click}}
		]*/
});

var flag  = "";

//增加
function btnAdd_Click(){
	var btnAdd=mainToolbar.items.get('btnAdd');
	if(btnAdd){
		var text = btnAdd.getText();
		if(text=='新建统计'){
			create_sfjc();
		}
		if(text=='重新统计'){
			reload_sfjc();
		}
	}
}
//新建报表
function create_sfjc(){
	var win = new Ext.Window({
		title:"请选择要新建报表的年月：",
		width:250,
		height:200,
		items:[{xtype:"datefield",id:"dates",format:"Y年m月",x:40,y:50,width:160,height:120}],
		buttons:[{text:"确定",handler:function(){
			if($('dates').value!=""){
				var year = $('dates').value.split("年");
				var nf = year[0];
				var yf = year[1].split("月")[0];
				Ext.Ajax.request({
					url:contextPath +"/jteap/wz/tjrw/tjrwAction!addTjrwAction.do",
					success:function(ajax){
						var responseText=ajax.responseText;	
		 				var responseObject=Ext.util.JSON.decode(responseText);
		 				if(responseObject.success){
	 						if(responseObject.msg=="y"){
				 				reload_sfjc();
				 			}else{
								alert("新建任务成功,请到统计任务中查看任务详情!");				 			
				 			}
				 		}else{
				 			alert("新建失败"+":"+responseObject.text);
				 		}	
				 		win.close();
 						rightGrid.changeToListDS(link1);
 						rightGrid.getStore().reload();
 						leftTree.getRootNode().reload();
					},
					failure:function(){
				 		alert("提交失败");
				 	},
				 	method:'POST',
				 	params: {nf:nf,yf:yf,rwlb:'sfjc'}//Ext.util.JSON.encode(selections.keys)		
				});
			}else{
				alert("请选择年月！");
				return;
			}
		}},{text:"取消",handler:function(){win.close();}}],
		buttonAlign:"center"
	});
	win.show();
}
//重新统计报表
function reload_sfjc(){
	Ext.MessageBox.confirm("重新生成报表","选中报表已经存在，您确认重新统计？确认之后，当前选中月份报表数据将会删除，重新统计！",function(e){
		if(e=="yes"){
			var year = leftTree.getSelectionModel().selNode.text.split("年");
			var nf = year[0];
			var yf = year[1].split("月")[0];
			Ext.Ajax.request({
						url:contextPath +"/jteap/wz/tjrw/tjrwAction!updateTjrwAction.do",
					success:function(ajax){
						var responseText=ajax.responseText;	
		 				var responseObject=Ext.util.JSON.decode(responseText);
		 				if(responseObject.success){
							alert("重新统计任务已经成功发出,请到统计任务中查看任务详情!");				 			
				 		}else{
				 			alert("重新统计失败"+":"+responseObject.text);
				 		}	
				 		rightGrid.changeToListDS(link1);
 						rightGrid.getStore().reload();
 						leftTree.getRootNode().reload();
					},
					failure:function(){
				 		alert("提交失败");
				 	},
				 	method:'POST',
				 	params: {nf:nf,yf:yf,rwlb:'sfjc'}//Ext.util.JSON.encode(selections.keys)		
				});
		}
	})
}
 
//导出报表
function btnExce_Click(){
	var year = leftTree.getSelectionModel().selNode.text.split("年");
	var nf = year[0];
	var yf = year[1].split("月")[0];
	//取得仓库ids
	var ckid = new Ext.getCmp('sf#ckmc').value;
	
	//exportExcels(rightGrid,true,nf,yf);		
	var path = contextPath +"/jteap/wz/sfjctj/SfjcAction!exportSelectedExcelAction.do?nf="+nf+"&yf="+yf+"&ckid="+ckid;
	//条件查询
	if(flag!=""){
		path = path+"&flag="+flag;
	}
	//alert(path);
	window.open(path);
}
//结存实时查询
function btnSscx_Click(){
	var url = "";
	var nyObj = $('sf#ny');
	//取得仓库id
	var ckid = new Ext.getCmp('sf#ckmc').value;
	url = link1;
	if(nyObj.value!=""){
		url = url+"&ny="+nyObj.value;
	}
	if(ckid != undefined){
		url = url+"&ckmc="+ckid;
	}
	
	url = url+"&ssflag=ss";
	
	var ds = rightGrid.getSscxDS(url);
	var cm=rightGrid.getSscxColumnModel();
	rightGrid.pageToolbar.bind(ds);
	rightGrid.reconfigure(ds,cm);
 	rightGrid.getStore().reload();
}

//查看有出入库记录及有库存的物资
function btnCrkkc_Click(){
	var url;
	 
	var nyObj = $('sf#ny');
	//取得仓库id
	var ckid = new Ext.getCmp('sf#ckmc').value;
	
	url = link1;
	if(nyObj.value!=""){
		url = url+"&ny="+nyObj.value;
	}
	if(ckid != undefined){
		url = url+"&ckmc="+ckid;
	}
	url = url+"&flag=crkkc";
	flag = "crkkc";
	rightGrid.changeToListDS(url);
	rightGrid.getStore().reload();
		 
}
//查看有库存的物资
function btnKc_Click(){
	var url;
 
	var nyObj = $('sf#ny');
	//取得仓库id
	var ckid = new Ext.getCmp('sf#ckmc').value;
	
	url = link1;
	if(nyObj.value!=""){
		url = url+"&ny="+nyObj.value;
	}
	if(ckid != undefined){
		url = url+"&ckmc="+ckid;
	}
	url = url+"&flag=kc";
	flag = "kc";
	rightGrid.changeToListDS(url);
 	rightGrid.getStore().reload();
			 
}
//查看没有出入库记录的物资
function btnNcrk_Click(){
	var url;
	var nyObj = $('sf#ny');
	//取得仓库id
	var ckid = new Ext.getCmp('sf#ckmc').value;
	
	url = link1;
	if(nyObj.value!=""){
		url = url+"&ny="+nyObj.value;
	}
	if(ckid != undefined){
		url = url+"&ckmc="+ckid;
	}
	url = url+"&flag=ncrk";
	flag = "ncrk";
	rightGrid.changeToListDS(url);
 	rightGrid.getStore().reload();
}
//月末库存为负的物资
function btnNkc_Click(){
	var url;
	var nyObj = $('sf#ny');
	//取得仓库id
	var ckid = new Ext.getCmp('sf#ckmc').value;
	
	url = link1;
	if(nyObj.value!=""){
		url = url+"&ny="+nyObj.value;
	}
	if(ckid != undefined){
		url = url+"&ckmc="+ckid;
	}
	url = url+"&flag=nkc";
	flag = "nkc";
	rightGrid.changeToListDS(url);
 	rightGrid.getStore().reload();
}
//删除
function btnDel_Click(){
	Ext.MessageBox.confirm("删除报表","您确认删除选中月的报表？",function(e){
		if(e=='yes'){
			var year = leftTree.getSelectionModel().selNode.text.split("年");
			var nf = year[0];
			var yf = year[1].split("月")[0];
			Ext.Ajax.request({
				url:contextPath +"/jteap/wz/tjny/TjnyTreeAction!removeTj.do",
				success:function(ajax){
					var responseText=ajax.responseText;	
					var responseObject=Ext.util.JSON.decode(responseText);
					if(responseObject.success){
			 			alert("删除成功");
			 		}else{
			 			alert("删除失败"+":"+responseObject.text);
			 		}	
			 		rightGrid.changeToListDS(link1);
					rightGrid.getStore().reload();
					leftTree.getRootNode().reload();
				},
				failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {nf:nf,yf:yf,lb:'sfjc'}//Ext.util.JSON.encode(selections.keys)		
			});
		}
	})
	
}

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if( button.id=='btnAdd'|| button.id=='btnDel'|| button.id=='btnExce') 
		button.setDisabled(true);
}
//查询面板中 所有的查询条件 格式："标签_属性名称_属性类型,标签_属性名称_属性类型,......标签_属性名称_属性类型"
var searchAllFs="选择月份#ny#dateField,仓库名称#ckmc#comboBox".split(',');
//查询面板中默认显示的条件，格式同上
var searchDefaultFs="选择月份#ny#dateField,仓库名称#ckmc#comboBox".split(',');
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchDefaultFs});
	
//左边的树 右边的grid
var rightGrid=new RightGrid();
var leftTree = new LeftTree();
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[searchPanel,rightGrid]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}


