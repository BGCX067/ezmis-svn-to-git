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

function btnAddLBModule_Click(){
	var select = leftTree.getSelectionModel().getSelectedNode();
	if(select){
		var flbbm = (select.id=='root'?'':select.id);
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&flbbm="+flbbm;
		window.open(url);
		showIFModule(url,"自定义表单","true",380,280,{},null,null,null,false,"no");
		leftTree.getRootNode().reload();
	}
}
function btnModiLBModule_Click(){
	var select=leftTree.getSelectionModel().getSelectedNode();
	if(select){
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn="+formSn+"&docid="+select.id;
		showIFModule(url,"自定义表单","true",380,280,{},null,null,null,false,"no");
		leftTree.getRootNode().reload();
	}
}

function btnDelLBModule_Click(){
	var url = contextPath + "/jteap/wz/kwwh/KwwhAction!delKWAction.do";
	var select = leftTree.getSelectionModel().getSelectedNode();//获取被选中的行
	if(select){
		var id = select.json.id;	
		if(window.confirm("确认删除选中的表单数据吗？")){
			Ext.Ajax.request({
				url:url,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("删除成功");
			 			kwTreeGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("提交失败");
			 	},
			 	method:'POST',
			 	params: {"id":id}//Ext.util.JSON.encode(selections.keys)			
			});
		}
	}		
}

function btnAddDAModule_Click(){
	var select = leftTree.getSelectionModel().getSelectedNode();
	var wzlbbm = "";
	if(select){
		wzlbbm = select.id;
	}
	var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SWZDA&wzlbbm="+wzlbbm;
	showIFModule(url,"自定义表单","true",680,430,{},null,null,null,false,"no");
	rightGrid.getStore().reload();
}
function btnModiDAModule_Click(){
	var select=rightGrid.getSelectionModel().getSelected();
	if(select){
	//	alert(select.json.id);
		var url = contextPath + "/jteap/form/eform/eformRec.jsp?formSn=TB_WZ_SWZDA&docid="+select.json.id;
		showIFModule(url,"自定义表单","true",680,430,{},null,null,null,false,"no");
	//	window.open(url);
		rightGrid.getStore().reload();
	}
}
function btnDelDAModule_Click(){
	var selections =  rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(window.confirm("确认删除选中的条目吗？")){
		Ext.Ajax.request({
			url:link3,
			success:function(ajax){
		 		var responseText=ajax.responseText;	
		 		var responseObject=Ext.util.JSON.decode(responseText);
		 		if(responseObject.success){
		 			alert("删除成功");
		 			rightGrid.getStore().reload();
		 		}else{
		 			alert(responseObject.msg);
		 		}				
			},
		 	failure:function(){
		 		alert("提交失败");
		 	},
		 	method:'POST',
		 	params: {ids:ids}//Ext.util.JSON.encode(selections.keys)			
		});
	}
}


function btnGGLB_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个物资档案管理');
		return;
	}
	var url = contextPath + "/jteap/wz/wzlb/wzlbTree.jsp";
		var str = showIFModule(url,"物资类别","true",280,580,{},null,null,null,false,"yes");
		if(str==null){
//			alert("请选择一项物资的类别");
			return;
		}
		var wzlbbm = leftTree.getSelectionModel().getSelectedNode().id;
		var wzlbmc = leftTree.getSelectionModel().getSelectedNode().text;
		if(window.confirm("确定要将选中的物资档案移动到类别为["+str.split('|')[0]+"]吗？")){
			Ext.Ajax.request({
				url:link4,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("修改成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}				
				},
			 	failure:function(){
			 		alert("修改失败");
			 	},
			 	method:'POST',
			 	params: {"ids":ids,"newwzlb":str.split('|')[1]}//Ext.util.JSON.encode(selections.keys)			
			});
		}
}
function btnZDKW_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个物资档案管理');
		return;
	}
	var url = contextPath + "/jteap/wz/kwwh/kwwhTree.jsp";
	var str = showIFModule(url,"库位","true",800,600,{},null,null,null,false,"yes");
		if(str==null){
//			alert("请选择一项物资的类别");
			return;
		}
		if(window.confirm("确定要将选中的物资档案指定到库位["+str.split('|')[0]+"]吗？")){
			var wzlbbm = leftTree.getSelectionModel().getSelectedNode().id;
			
			Ext.Ajax.request({
				url:link5,
				success:function(ajax){
			 		var responseText=ajax.responseText;	
			 		var responseObject=Ext.util.JSON.decode(responseText);
			 		if(responseObject.success){
			 			alert("修改成功");
			 			rightGrid.getStore().reload();
			 		}else{
			 			alert(responseObject.msg);
			 		}
				},
			 	failure:function(){
			 		alert("修改失败");
			 	},
			 	method:'POST',
			 	params: {"ids":ids,"kw":str.split('|')[1]}//Ext.util.JSON.encode(selections.keys)
			});
		}
}

function btnExportCard_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个物资档案管理');
		return;
	}
	var url = link7+"?ids="+ids;
	window.open(url);
}

function btnTsfl_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个物资档案管理');
		return;
	}
	tsflWin.show();
	tsflForm.getForm().findField("ids_textfield").setValue(ids)
}
var tsflWin =  new Ext.Window({
        title: '批量修改特殊分类',
      //  applyTo:'hello-win',
        width: 400,
        height:130,
        plain:true, // 表示为渲染window body的背景为透明的背景
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        closeAction:"hide",
        items: [tsflForm],  //这里放你窗体的内容，为空就是这样的格式
        buttons:[{
                    text:'确定',
                    handler: function(){
                    	tsflForm.getForm().submit({
					         success : function() {
						          Ext.Msg.alert("成功", "修改成功");
						          tsflWin.hide();
						          rightGrid.getStore().reload();
					         },
					         failure : function() {
						          Ext.Msg.alert("失败", "修改出现错误");
					         }
					    });
                    }
                },{
                    text: '关闭',
                    handler: function(){
                     tsflWin.hide();
                    }
                }]
    });
function btnKcl_Click(){
	var selections = rightGrid.getSelectionModel().getSelections();//获取被选中的行
	var ids="";
	Ext.each(selections,function(selectedobj){
		ids+=selectedobj.id+",";//取得他们的id并组装
	});
	if(ids==''){
		alert('请选择一个物资档案管理');
		return;
	}
	dqkcWin.show();
	dqkcForm.getForm().findField("ids_textfield").setValue(ids)
}
var dqkcWin =  new Ext.Window({
        title: '批量修改库存量',
      //  applyTo:'hello-win',
        width: 400,
        height:130,
        plain:true, // 表示为渲染window body的背景为透明的背景
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        closeAction:"hide",
        items: [dqkcForm],  //这里放你窗体的内容，为空就是这样的格式
        buttons:[{
                    text:'确定',
                    handler: function(){
                    	dqkcForm.getForm().submit({
					         success : function() {
						          Ext.Msg.alert("成功", "修改成功");
						          dqkcWin.hide();
						          rightGrid.getStore().reload();
					         },
					         failure : function() {
						          Ext.Msg.alert("失败", "修改出现错误");
					         }
					    });
                    }
                },{
                    text: '关闭',
                    handler: function(){
                     dqkcWin.hide();
                    }
                }]
    });    
   
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModify' || button.id=='btnDel') 
		button.setDisabled(true);
}
//左边的树
var leftTree=new LeftTree();

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

