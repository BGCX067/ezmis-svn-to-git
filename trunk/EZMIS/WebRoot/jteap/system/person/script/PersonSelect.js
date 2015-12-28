/*
 *封装的从中间GridPanel到ListBox的批量写入
 *grid:所传入的表格
 *listBox:需要写入的listBox
 */
var writeListBoxFromGridPanel = function(grid ,listBox,onlyOne) {
	var personId ;
	var personName ;
	var personLoginName ;
	var personLoginName2;
	//默认为多选 
	if(onlyOne == null) {
		onlyOne = false ;
	}
	//如果为单选，而界面又多选了
	if(onlyOne && grid.getSelectionModel().getSelections().length>1) {
		alert("此页面为单选模式，请选择单个人员。") ;
		return ;
	}
	//判断人员是否存在
	var flag = false ;
	var arrays = grid.getSelectionModel().getSelections()
	for(var i=0 ; i< arrays.length ; i++) {
		var obj = arrays[i] ;
		if(obj.json.person != null){
			personId = obj.json.person.id ;
			personName = obj.json.person.userName ;
			personLoginName = obj.json.person.userLoginName ;
			personLoginName2 = obj.json.person.userLoginName2 ;
		} else {
			personId = obj.json.id ;
			personName = obj.json.userName ;
			personLoginName = obj.json.userLoginName ;
			personLoginName2 = obj.json.userLoginName2 ;
		}
		var items = listBox.store.data.items ;
		for(var j=0 ; j < items.length ; j++) {
			 if(personId == items[j].data.value.id) {
			 	//如果是单个数据，弹出对话框。
			 	if(arrays.length == 1){
			 		alert("此人员已经存在") ;
			 	}
			 	flag = true ;
			 	break ;
			 }
		}
		if(flag) {
			flag = false ;
			continue ;
		}
		if(onlyOne){
			listBox.store.removeAll();
		}
		var Model = Ext.data.Record.create([{name:'key'},{name:'value'}])
		var record = new Model(
			{
				key : personName ,
				value : {
							id : personId ,
							loginName : personLoginName,
							loginName2 : personLoginName2
						}  
			}
		)
		listBox.view.store.add(record) ;
	}
	listBox.view.refresh();
}

//左边两棵树的TabPanel
var RoleGroupTreeTabPanel = function(config) {
	var roleGroupTreeTabPanel = this ;
	
	this.groupTree = this.getGroupTree() ;	
	
	this.roleTree = this.getRoleTree() ;
	
	RoleGroupTreeTabPanel.superclass.constructor.call(this,{
		region : 'west',
		width : 150 ,
		border : true ,
		bodyBorder : false ,
		margins:'5 5 5 5',
        cmargins:'0 0 0 0',
		activeTab: 0,
		items:[
			roleGroupTreeTabPanel.groupTree,roleGroupTreeTabPanel.roleTree
		]
	}) ;
	
	//为groupTree添加选择改变事件
	this.groupTree.getSelectionModel().on("selectionchange",function(oSM , oNode){
		if(oNode){
	    	if(oNode.isRootNode()){
		    	var url=oNode.dissociation?link11:link12;
		    	centerPersonGrid.changeToPersonDS(url);
	    	}else{
		    	//改变操作按钮状态
		    	var url = link1+"?groupId="+oNode.id;
		    	centerPersonGrid.changeToListDS(url);
	    	}
	    	centerPersonGrid.getStore().reload();
	    	//gTree.oldSelectedNodeId=oNode.id;
    	}
	}) ;

	//为roleTree添加选择改变事件
	this.roleTree.getSelectionModel().on("selectionchange",function(oSM , oNode){
		if(oNode){
	    	if(oNode.isRootNode()){
		    	var url=oNode.dissociation?link11:link12;
		    	centerPersonGrid.changeToPersonDS(url);
	    	}else{
		    	//改变操作按钮状态
		    	var url = "${contextPath}/jteap/system/person/P2RoleAction!showListAction.do"+"?roleId="+oNode.id;
		    	centerPersonGrid.changeToListDS(url);
	    	}
	    	centerPersonGrid.getStore().reload();
	    	//gTree.oldSelectedNodeId=oNode.id;
    	}
	}) ;
	 
}

Ext.extend(RoleGroupTreeTabPanel , Ext.TabPanel , {
	getGroupTree : function() {
		var rootLoader=new Ext.tree.AsyncTreeNode({
        	text:'群组',
        	loader:new Ext.tree.TreeLoader({
	            dataUrl:link2,
	            listeners:{
	            	beforeload:function(loader, node, callback ){
	            		this.baseParams.parentId=(node.isRoot?"":node.id);
	            	} ,
	            	load : function() {
	            		roleGroupTreeTabPanel.groupTree.root.select() ;
	            	}
	            }
	        
	        }),
	        expanded :true,
	        allowDrag :true,
	        allowDrop :true
       });
       
       var groupTree = new Ext.tree.TreePanel(
       		{
				id:'groupTree',
		        title:'组织',
		        dropAllowed: true, 
				dragAllowed: true,
		        split:true,
		        width: 180,
		        minSize: 180,
		        maxSize: 400,
		        collapsible: true,
		        margins:'0 0 5 5',
		        cmargins:'0 5 5 5',
		        enableDD:true,
		        rootVisible:true,
		        lines:false,
		        autoScroll:true,
		 		root:rootLoader,
		 		tbar:[{text:'刷新',handler:function(){gTree.getRootNode().reload();}}],
		        collapseFirst:true     
       		}
       )
       return groupTree ;
	},
	getRoleTree : function() {
		var rootLoader=new Ext.tree.AsyncTreeNode({
	      	text:'角色',
	      	loader:new Ext.tree.TreeLoader({
	           dataUrl:contextPath + "/jteap/system/role/RoleAction!showRoleTreeAction.do",
	           listeners:{
	           	beforeload:function(loader, node, callback ){
	           		this.baseParams.parentId=(node.isRoot?"":node.id);
	           	},
	           	load : function() {
	            		roleGroupTreeTabPanel.roleTree.root.select() ;
	            }
	           }
	       
	       }),
	       expanded :true,
	       allowDrag :true,
	       allowDrop :true
       });
       
       var roleTree = new Ext.tree.TreePanel(
       		{
				id:'roleTree',
		        title:'角色',
		        dropAllowed: true, 
				dragAllowed: true,
		        split:true,
		        width: 180,
		        minSize: 180,
		        maxSize: 400,
		        collapsible: true,
		        margins:'0 0 5 5',
		        cmargins:'0 5 5 5',
		        enableDD:true,
		        rootVisible:true,
		        lines:false,
		        autoScroll:true,
		 		root:rootLoader,
		 		tbar:[{text:'刷新',handler:function(){gTree.getRootNode().reload();}}],
		        collapseFirst:true     
       		}
       )
       return roleTree ;
	}
}) 
 

//中间的SercherPanel
var CenterSearchPanel = function(config) {
		//点击查询后的事件
	this.searchClick=function(){
		//获取当前节点
		var oNode=roleGroupTreeTabPanel.getActiveTab().getSelectionModel().getSelectedNode();
		var whereHeader = "obj.";
		if(!oNode.isRootNode()&&!oNode.dissociation){
			whereHeader = "obj.person.";
		}
		var oPanel=searchtemp.items.get(0);
		var oItems = oPanel.items.items;
		var queryParamsJson = {};
		var queryParamsSql = "";
	 	Ext.each(oItems,function(oItem){
	 		if(oItem.hidden==false){
	 			var temp = oItem.items.items[0];
	 			var tempValue = temp.getValue();
	 			//值不为空才作为参数
	 			if(tempValue!=null&&tempValue!=""){
	 				//日期字段,就相应的处理为字符串
			 		if(temp.triggerClass=="x-form-date-trigger"){
			 			tempValue = formatDate(tempValue,"yyyy-MM-dd");
			 			queryParamsSql+=whereHeader+temp.id.split("#")[1]+"='"+encodeURIComponent(tempValue)+"' and ";
			 		//文本字段模糊查询
			 		}else{
			 			queryParamsSql+=whereHeader+temp.id.split("#")[1]+" like '"+encodeURIComponent("$"+tempValue+"$")+"' and ";
			 		}
	 			}
	 		};
	 	});
	 	//根组织
	 	if(oNode.isRootNode()){
	 		var url = link12+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
	 		centerPersonGrid.changeToPersonDS(url);
	 	//游离
	 	}else if(oNode.dissociation){
	 		var url = link11+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
	 		centerPersonGrid.changeToPersonDS(url);
	 	//其他组织
	 	}else{
	 		if(roleGroupTreeTabPanel.getActiveTab() == roleGroupTreeTabPanel.groupTree){
		 		var url = link1+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
		 		url+="&groupId="+oNode.id;
		 		centerPersonGrid.changeToListDS(url);
	 		} else {
	 			var url = "${contextPath}/jteap/system/person/P2RoleAction!showListAction.do"+"?queryParamsSql="+queryParamsSql.substring(0,queryParamsSql.length-5);
		 		url+="&roleId="+oNode.id;
		 		centerPersonGrid.changeToListDS(url);
	 		}
	 	}
 		centerPersonGrid.getStore().reload();	 	
	};  
    CenterSearchPanel.superclass.constructor.call(this, config);
}  ;

Ext.extend(CenterSearchPanel, Ext.app.SearchPanel, {
});


//中间的Grid
var CenterPersonGrid = function() {	
	var centerPersonGrid = this
    var defaultDs=this.getPersonActionDS("${contextPath}/jteap/system/person/PersonAction!showListAction.do");
    this.pageToolbar=new Ext.PagingToolbar({
	    //pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
	    store: defaultDs,
	    displayInfo: true,
	    displayMsg: '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg: "没有符合条件的数据"
	});
	CenterPersonGrid.superclass.constructor.call(this,{
	 	ds: defaultDs,
	 	cm: this.getPersonActionColumnModel(),
		sm: this.sm,
	 	ddGroup:'GridDD',
	 	enableDragDrop:true,
	 	dropAllowed: true, 
		dragAllowed: true,
	   	border : false ,
	   	bodyBrder : false ,
		height:200,
		margins:'0px 0px 4px 0px',
		loadMask: true,
		frame:true,
		region:'center',
		tbar:this.pageToolbar 
	});	
	centerPersonGrid.on("celldblclick" , function(grid,  rowIndex, columnIndex,e ) {
		writeListBoxFromGridPanel(grid , rightListBox ,onlyOne) ;
	})
}

Ext.extend(CenterPersonGrid , Ext.grid.GridPanel , {
	sm:new Ext.grid.CheckboxSelectionModel(),
	/**
	 * 切换数据源->P2GAction!showList
	 */
	changeToListDS:function(url){
		var ds = this.getP2GActionDS(url);	
		var cm=this.getP2GActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 切换数据源->PersonAction!showList 主要显示游离用户
	 */
	changeToPersonDS:function(url){
		var ds=this.getPersonActionDS(url);
		var cm=this.getPersonActionColumnModel();
		this.pageToolbar.bind(ds);
		this.reconfigure(ds,cm);
	},
	/**
	 * 取得P2G数据源
	 */
	getP2GActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'person.userLoginName','person.id','person.userName', 'person.sex','person.status','person.userLoginName2'
	        ]),
	        remoteSort: true	
	    });
	    ds.setDefaultSort('person.userName', 'desc');
	   	 return ds;
	},	
	/**
	 * 取得PersonAction数据源
	 */
	getPersonActionDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            'userLoginName', 'userName', 'sex','status','userLoginName2'
	        ]),
	        remoteSort: true	
	    });
	    ds.setDefaultSort('userName', 'desc');
		return ds;
	},
    
	/**
	 * P2GAction 列模型
	 */
	getP2GActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'userName',header: "用户名", width: 80, sortable: true, dataIndex: 'person.userName',
		         renderer:function(value,metadata,record,rowIndex,colIndex,store){
			    	if(record.json.admin==true)
			    		return "<span style='color:blue;font-weight:bold;'>"+value+"</span>";
			    	else
			    		return value;
			    }},
		        {id:'userLoginName2',header: "登陆账号", width: 60, sortable: true, dataIndex: 'person.userLoginName2'},
		        {id:'userLoginName',header: "工号", width: 60, sortable: true, dataIndex: 'person.userLoginName'},
				{id:'sex',header: "性别", width: 35, sortable: true, dataIndex: 'person.sex'}
				/*
				{id:'status',header: "状态", width: 120, sortable: true, dataIndex: 'person.status',					
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						var result=record.json.person.status;
						if(result=='锁定'){
							result="<span style='font-weight:bold;color:red'>"+result+"</span>"
						}
						return result;
					}
				}	
				*/
			]);
		return cm;
	},
	/**
	 * PersonAction 列模型
	 */
	getPersonActionColumnModel:function(){
	    var cm=new Ext.grid.ColumnModel([
		    	this.sm,
		        {id:'userName',header: "用户名", width: 120, sortable: true, dataIndex: 'userName'},
		        {id:'userLoginName2',header: "登陆账号", width: 120, sortable: true, dataIndex: 'userLoginName2'},
		        {id:'userLoginName',header: "工号", width: 100, sortable: true, dataIndex: 'userLoginName'},
				{id:'sex',header: "性别", width: 55, sortable: true, dataIndex: 'sex'}
				/*
				{id:'status',header: "状态", width: 120, sortable: true, dataIndex: 'status',
					renderer:function(value,metadata,record,rowIndex,colIndex,store){
						result=record.json.status;
						if(result=='锁定'){
							result="<span style='font-weight:bold;color:red'>"+result+"</span>"
						}
						return result;
					}
				}	
				*/
			]);
		return cm;
	},
	getPersonIds:function(select){
		  var personids="";
			for(var i=0;i<select.length;i++){
			    var temp=select[i];
			    var id=temp.get("person.id");
				if(!id){
				   personids+=temp.id+",";
				}else{
				   personids+=id+",";
				}
			}
		return personids;
	},
	getP2GIds:function(select){
		var p2gids="";
		for(var i=0;i<select.length;i++){
		    var temp=select[i];
		    var id=temp.id;
			p2gids+=temp.id+",";
		}
		return p2gids;
	}

})

//中间右边的Panel
var btnPanel = new Ext.Panel(
	{
		margins : '205 0 230 5' ,
		region : "east" ,
		border : true ,
		bodyBorder : true ,
		width : 30 ,
		height : 500 ,
		items : [
			{
				xtype : 'tbbutton' ,
				cls: 'x-btn-text-icon',
				icon : "icon/right2.gif",
				minWidth  : 20 ,
				handler : function() {
					writeListBoxFromGridPanel(centerPersonGrid , rightListBox , onlyOne ) ;
				}
			} ,
			{
				xtype : 'tbbutton' ,
				cls: 'x-btn-text-icon',
				icon : "icon/left2.gif",
				handler : function() {
					var selectionsArray = rightListBox.view.getSelectedIndexes();
					if(selectionsArray.length == 0) {
						return ;
					}
					//按选择索引从大到小排序
					selectionsArray.sort(function compare(a,b){return b-a;});
					for(var i=0 ; i<selectionsArray.length ; i++){
						var record = rightListBox.view.store.getAt(selectionsArray[i]);
						rightListBox.view.store.remove(record) ;
					}
					rightListBox.view.refresh();
				}
			}
		
		]
		
	}
) ;


//右边的ListBox
var store = new Ext.data.SimpleStore({    
	data:[],    
	//expandData:true,    
	fields:["key","value"]    
},['key','value']) ;
var rightListBox = new Ext.ux.Multiselect(
	{
		tbar:["被选人员"],
		margins:'5 5 5 5',
		cmargins:'0 5 5 5',
		region : 'east' ,
		width:100,
		height:501,
		displayField : 'key',
		store : store,
		allowDup : true,
		copy:true,
		allowTrash:true,
		appendOnly:false,
		isFormField: false
	}
) ; 



var PersonSelectWindow = function(config) {
	
}

Ext.extend(PersonSelectWindow,Ext.Window,{
})