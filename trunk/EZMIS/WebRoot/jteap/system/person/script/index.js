//主工具栏,在渲染的时候，初始化操作按钮   operationsToolbarInitialize from common.js
var mainToolbar=new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}});
/*									{items:[
										{id:'btnAddGroup',text:'添加组织',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',listeners:{click:btnAddGroup_Click}},
										{id:'btnModiGroup',text:'修改组织',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',listeners:{click:btnModiGorup_Click},tooltip: '选中需要修改的组织，再执行该操作'},
										{id:'btnDelGroup',text:'删除组织',disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',listeners:{click:btnDelGorup_Click},tooltip:'选中需要删除的组织，再执行该操作'},
										{id:'btnAddPerson',text:'新增用户',cls: 'x-btn-text-icon',icon:'icon/icon_6.gif',listeners:{click:btnAddUser_Click}},
										{id:'btnModiPerson',text:'修改用户',listeners:{click:btnModifyUser_Click},disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_5.gif',tooltip:'选中需要修改的用户，再执行该操作'},
										{id:'btnDelPerson',text:'删除用户',listeners:{click:btnDelUser_Click},disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',tooltip:'选中需要删除的用户，再执行该操作'},
										{id:'btnRemovePerson',text:'移除用户',listeners:{click:btnMoveUser_Click},disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',tooltip:'将选中的用户从当前组织中移除'},
										{id:'btnResetPassword',text:'初始化密码',listeners:{click:btnInitPassWord_Click},disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',tooltip:'初始化被勾选的用户密码，初始密码："888888"'},
										{id:'btnLockUser',text:'锁定用户',listeners:{click:btnLockUser_Click},disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',tooltip: '锁定用户的状态'},
										{id:'btnUnLockUser',text:'解除用户锁',listeners:{click:btnMoveLockUser_Click},disabled:true,cls: 'x-btn-text-icon',icon:'icon/icon_7.gif',tooltip:'解除锁定用户的状态'}
									]}
								);
*/

/**
 * 工具栏渲染的时候，添加按钮

mainToolbar.on("render",function(tb){
	operationsToolbarInitialize
});
 */
/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModifyUser' || button.id=='btnDelUser' 
		|| button.id=='btnRemoveUser' || button.id=='btnInitPwd' 
		|| button.id=='btnLockUser' || button.id=='btnUnlockUser'
		||button.id=='btnSetupAdmin'||button.id=='btnDataperm'
		||button.id=='btnRemoveAdmin') 
		button.setDisabled(true);
}
/**
 * 添加组织按钮
 */
function btnAddGroup_Click(oButton,e){
	groupTree.createGroup();
}

/**
 * 修改组织按钮
 */	
function btnModifyGroup_Click(){
	groupTree.modifyGroup();
}
/**
 * 删除组织按钮
 */
function btnDelGroup_Click(){
	groupTree.delGroup();
}
/**
 * 添加用户
 */
function btnCreateUser_Click(oButton,e){
	var oNode=groupTree.getSelectionModel().getSelectedNode();
	var createUserForm=new EditFormWindow();
}
/**
 * 修改用户
 */
function btnModifyUser_Click(){
    var select=gridPanel.getSelectionModel().getSelections()[0];
    //p2g的id和person的id两种情况,如果p2g的person'id未取到，就直接取id
    var id=select.get("person.id");
    if(!id) id=select.id;
    //本人不能在此处修改自己的个人信息，涉及到角色及组织问题
    if(id==curPersonId){
    	alert("请使用主工具栏的【个人信息修改】功能修改您个人的资料");
    	return;
    }
    var  modifyUserForm=new EditPresonFormWindow(id);
	modifyUserForm.showDetailFrom(id);
}

/**
 * 删除用户
 */
function btnDelUser_Click(){
	var select=gridPanel.getSelectionModel().getSelections();
    gridPanel.delPerson(select);
    
    
    
}
/**
 * 解除人员和组织的关系
 */
function btnRemoveUser_Click(){
    var select=gridPanel.getSelectionModel().getSelections();
    gridPanel.movePerson(select);
}
/**
 * 初始化用户密码
 */
function btnInitPwd_Click(){
	  var select=gridPanel.getSelectionModel().getSelections();
	  gridPanel.initPersonPassWord(select);
}
/**
 * 锁定用户状态
 */
function btnLockUser_Click(){
	  var select=gridPanel.getSelectionModel().getSelections();
	  gridPanel.lockPerson(select);
}
/**
 * 解除用户锁
 */
function btnUnlockUser_Click(){
  	  var select=gridPanel.getSelectionModel().getSelections();
	  gridPanel.moveLockPerson(select);
}
/**
 * 设定管理员
 */
function btnSetupAdmin_Click(){
	  var select=gridPanel.getSelectionModel().getSelections();
	  gridPanel.setupAdmin(select);
}

/**
 * 移除管理员
 */
function btnRemoveAdmin_Click() {
	  var select=gridPanel.getSelectionModel().getSelections();
	  gridPanel.removeAdmin(select);
}

/**
 * 指定角色
 */
function btnSetupRole_Click(){
	  var select=gridPanel.getSelectionModel().getSelections();
	  gridPanel.setupRole(select);
}

function btnSetupRes_Click(){
	var select=gridPanel.getSelectionModel().getSelections();
	gridPanel.setupRes(select);
}

function btnClearRes_Click(){
	var select=gridPanel.getSelectionModel().getSelections();
	gridPanel.clearRes(select);
}

/**
 * 数据权限
 */
function btnDataperm_Click(){
	showDatapermTree();
}

/**
 * 用户登录名生成
 */
function btnHz2py_Click() {
	if (window.confirm('此操作用于生成所有用户的汉语拼音登录名，将会重置所有人员的登录名，你确定执行此操作？')) {
		var url = contextPath+"/jteap/system/person/PersonAction!hz2PyAction.do";
		Ext.Ajax.request({
			url : url,
			success:function(ajax) {
				var responseText = ajax.responseText;
				var obj = Ext.decode(responseText);
				if (obj.success == true) {
					alert('登录名转换成功');
					gridPanel.getStore().reload();
				} else {
					alert('数据库异常，请联系管理员');
				}
			}
		})
	}
}
/**
 * 角色关联
 */
function btnRoleLink_Click(){
    var select=gridPanel.getSelectionModel().getSelections()[0];
	var url = link46;
	var returnValue = showModule(url,true,800,600,"onlyOne|true") ;
	var arrays = Ext.decode(returnValue) ;
	if(arrays[0]==null) {
		alert('没有选中任何人');
		return ;
	}
	var targetPersonId=arrays[0].id;
	var targetPersonName=arrays[0].name;
	var srcPersonId=select.get("person.id")?select.get("person.id"):select.id;
	var srcPersonName=select.get("person.userName")?select.get("person.userName"):select.data.userName;
	var alerm="是否修改<"+srcPersonName+">与<"+targetPersonName+">同样的角色";
	var url= contextPath+"/jteap/system/person/P2GAction!linkRoleAction.do";
	if(window.confirm(alerm))
	{
		Ext.Ajax.request({
			url : url,
			success:function(ajax) {
				var responseText = ajax.responseText;
				var obj = Ext.decode(responseText);
				if (obj.success == true) {
					alert('关联成功');
					gridPanel.getStore().reload();
				} else {
					alert('数据库异常，请联系管理员');
				}
			},
		   failure:function(){
		   		alert("数据库异常，请联系管理员");
		   },
		   method:"POST",
		   params:{targetPersonId:targetPersonId,srcPersonId:srcPersonId}
		});
	}
}

//用户查询面板								
var searchPanel=new SearchPanel({searchDefaultFs:searchDefaultFs,searchAllFs:searchAllFs});
//用户列表
var gridPanel=new PersonGrid();


//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'1 0 0 -1',
	items:[searchPanel,gridPanel]
}

//北边
var lyNorth={
	id:'north-panel',
	region:'north',
	height:27,
	border:false,
 	items: [mainToolbar]
}