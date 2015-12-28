//主工具栏
var mainToolbar=new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});
                                   //  {items:[
                                   //    {id:'btnAddRole',text:'添加角色',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',listeners:{click:btnAddRole_Click}},
                                   // {id:'btnModiRole',text:'修改角色',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnModiRole_Click},tooltip: {title:'修改角色',text:'选中需要修改的角色，再执行该操作'}},
                                   //     {id:'btnDelRole',text:'删除角色',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelRole_Click},tooltip: {title:'删除角色',text:'选中需要删除的角色，再执行该操作'}}
                             	   // ]}                
                        
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id == 'btnModifyUser' || button.id == 'btnDelUser'
		|| button.id == 'btnInitPwd' || button.id == 'btnAddUser'
		|| button.id == 'btnDelUser' || button.id == 'btnRemoveUser'
		|| button.id == 'btnLockUser' || button.id == 'btnUnlockUser') 
		button.setDisabled(true);
}

/**
 * 添加角色按钮
 */
function btnAddRole_Click(oButton,e){
    roleTree.createRole(false);
}
/**
 * 修改角色按钮
 */         
function btnModiRole_Click(){
	var oNode=roleTree.getSelectionModel().getSelectedNode();
	if(oNode!=null){
    var roleform=  new RoleEditFormWindow(oNode.id);
    roleform.show();
    roleform.loadData();
	}else{
	alert("请选择一个节点!");
	}
}
/**
 * 删除角色按钮
 */
function btnDelRole_Click(){
    roleTree.delRole();
}

/**
 * 数据权限
 */
function btnDataperm_Click(){
	showDatapermTree();
}

/**
 * 新建用户
 */
function btnCreateUser_Click() {
	var createUserForm=new EditFormWindow();
}

/**
 * 添加用户
 */
function btnAddUser_Click() {
	var url = link24 ;
	var returnValue = showModule(url,true,800,600) ;
	var arrays = Ext.decode(returnValue) ;
	if(arrays==null) {
		return ;
	}
	var oNode=roleTree.getSelectionModel().getSelectedNode();
	var roleId = oNode.id;
   	var ids = "" ;
   	for(var i=0 ; i < arrays.length ; i++) {
   		ids += arrays[i].id + "," ;
   	}
   	ids = ids.substring(0,ids.length-1) ;
   	Ext.Ajax.request({
   		url : link25,
   		method : 'POST',
   		params : {
   			personIds : ids,
   			roleId : roleId
   		},
   		success : function(ajax) {
   			var responseText = ajax.responseText;
   			var obj = Ext.decode(responseText);
   			if (obj.success == true) {
   				alert('添加用户成功');
   				rightGrid.getStore().reload();
   			} else {
   				alert(obj.msg);
   			}
   		},
   		failure : function() {
   			alert("数据库异常，请联系管理员");
   		}
   	})
}


function btnModifyUser_Click(){
    var select=rightGrid.getSelectionModel().getSelections()[0];
    //p2g的id和person的id两种情况,如果p2g的person'id未取到，就直接取id
    var id=select.get("person.id");
    if(!id) id=select.id;
    //本人不能在此处修改自己的个人信息，涉及到角色及组织问题
    //if(id==curPersonId){
    	//alert("请使用主工具栏的【个人信息修改】功能修改您个人的资料");
    	//return;
    //}
    var  modifyUserForm=new EditPresonFormWindow(id);
	modifyUserForm.showDetailFrom(id);
}

/**
 * 删除用户
 */
function btnDelUser_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.delPerson(select);
}
/**
 * 解除人员和组织的关系
 */
function btnRemoveUser_Click(){
    var select=rightGrid.getSelectionModel().getSelections();
    rightGrid.movePerson(select);
}


/**
 * 初始化用户密码
 */
function btnInitPwd_Click(){
	  var select=rightGrid.getSelectionModel().getSelections();
	  rightGrid.initPersonPassWord(select);
}

/**
 * 锁定用户状态
 */
function btnLockUser_Click(){
	  var select=rightGrid.getSelectionModel().getSelections();
	  rightGrid.lockPerson(select);
}
/**
 * 解除用户锁
 */
function btnUnlockUser_Click(){
  	  var select=rightGrid.getSelectionModel().getSelections();
	  rightGrid.moveLockPerson(select);
}



//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});
var rightGrid=new RightGrid();


var lyCenter={
    layout:'border',
    id:'center-panel',
    region:'center',
    minSize: 175,
    maxSize: 400,
    border:false,
    margins:'1 0 0 -5',
    items:[searchPanel,rightGrid]
}

//北边
var lyNorth={
    id:'north-panel',
    region:'north',
    border:false,
    height:25,
    items:[mainToolbar]
}