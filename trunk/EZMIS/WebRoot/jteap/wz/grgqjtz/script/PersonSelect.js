var admin = window.dialogArguments.admin;
//check 被选中人或组织或角色的对象数组 [{id:'',name:'',type:'group|person|role'},{id:'',name:'',type:''}]
var check = window.dialogArguments.check;
var publish = window.dialogArguments.publish;

var Model = Ext.data.Record.create([ {
	name : 'key'
}, {
	name : 'id'
}, {
	name : 'type'
}])

/*
 * 封装的从中间GridPanel到ListBox的批量写入 grid:所传入的表格 listBox:需要写入的listBox
 */
var writeListBoxFromGridPanel = function(grid, listBox, onlyOne) {
	var personId;
	var personName;
	var personLoginName;
	// 默认为多选
	if (onlyOne == null) {
		onlyOne = false;
	}
	// 如果为单选，而界面又多选了
	if (onlyOne && grid.getSelectionModel().getSelections().length > 1) {
		alert("此页面为单选模式，请选择单个人员。");
		return;
	}
	// 判断人员是否存在
	var flag = false;
	var arrays = grid.getSelectionModel().getSelections()
	for (var i = 0;i < arrays.length; i++) {
		var obj = arrays[i];
		if (obj.json.person != null) {
			personId = obj.json.person.id;
			personName = obj.json.person.userName;
			personLoginName = obj.json.person.userLoginName;
		} else {
			personId = obj.json.id;
			personName = obj.json.userName;
			personLoginName = obj.json.userLoginName;
		}
		var items = listBox.store.data.items;
		for (var j = 0;j < items.length; j++) {
			if (personId == items[j].data.id) {
				// 如果是单个数据，弹出对话框。
				if (arrays.length == 1) {
					alert("此人员已经存在");
				}
				flag = true;
				break;
			}
		}
		if (flag) {
			flag = false;
			continue;
		}
		if (onlyOne) {
			listBox.store.removeAll();
		}
		var Model = Ext.data.Record.create([ {
			name : 'key'
		}, {
			name : 'id'
		}, {
			name : 'type'
		}])
		var record = new Model( {
			key : personName,
			id : personId,
			type : 'person'
		})
		listBox.view.store.add(record);
	}
	listBox.view.refresh();
}

/**
 * 群组选择
 */
var writeListBoxFromGroupTree = function(node, listBox) {
	var groupId;
	var groupName;
	var oNode = node;
	var ui = oNode.getUI();
	var checked = ui.checkbox.checked;

	groupId = oNode.id;
	groupName = oNode.text;
	
	//groupName = groupName.substring(0, groupName.indexOf("("));
	// 判断群组是否存在
	var items = listBox.store.data.items;
	if (checked) {
		for (var i = 0;i < items.length; i++) {
			if (items[i].data.type == "group") {
				if (groupId == items[i].data.id) {
					alert("此群组已存在");
					return;
				}
			}
		}

		var Model = Ext.data.Record.create([ {
			name : 'key'
		}, {
			name : 'id'
		}, {
			name : 'type'
		}])
		var record = new Model( {
			key : groupName,
			id : groupId,
			type : 'group'
		})
		listBox.view.store.add(record);
	} else {
		for (var i = 0;i < items.length; i++) {
			if (items[i].data.type == "group" && groupId == items[i].data.id) {
				listBox.view.store.remove(items[i]);
				break;
			}
		}
	}

	listBox.view.refresh();
}

// 左边两棵树的TabPanel
var RoleGroupTreeTabPanel = function(config) {
	var roleGroupTreeTabPanel = this;

	this.groupTree = this.getGroupTree();

	this.roleTree = this.getRoleTree();

	RoleGroupTreeTabPanel.superclass.constructor.call(this, {
		region : 'west',
		width : 180,
		border : true,
		margins : '5 5 5 5',
		cmargins : '0 0 0 0',
		activeTab : 0,
		items : [roleGroupTreeTabPanel.groupTree,
				roleGroupTreeTabPanel.roleTree]
	});

	// 为groupTree添加选择改变事件
	this.groupTree.getSelectionModel().on("selectionchange",
			function(oSM, oNode) {
				if (oNode) {
					if (oNode.isRootNode()) {
						var url = oNode.dissociation ? link11 : link10;
						centerPersonGrid.changeToPersonDS(url);
					} else {
						// 改变操作按钮状态
			var url = link12 + "?groupId=" + oNode.id;
			centerPersonGrid.changeToListDS(url);
		}
		centerPersonGrid.getStore().reload();
	}
}	);

	// 树被选中时，将群组加入listbox中
	this.groupTree.on("checkchange", function(node, check) {
		writeListBoxFromGroupTree(node, rightListBox);
	})

	// 为roleTree添加选择改变事件
	this.roleTree.getSelectionModel().on("selectionchange",
			function(oSM, oNode) {
				if (oNode) {
					if (oNode.isRootNode()) {
						var url = oNode.dissociation ? link11 : link10;
						centerPersonGrid.changeToPersonDS(url);
					} else {
						// 改变操作按钮状态
			var url = link14 + "?roleId=" + oNode.id;
			centerPersonGrid.changeToListDS(url);
		}
		centerPersonGrid.getStore().reload();
	}
}	);

}

Ext
		.extend(
				RoleGroupTreeTabPanel,
				Ext.TabPanel,
				{
					getGroupTree : function() {
						var rootGroupLoader = new Ext.tree.AsyncTreeNode( {
							text : '群组',
							loader : new Ext.tree.TreeLoader( {
								dataUrl : link9,
								listeners : {
									beforeload : function(loader, node,
											callback) {
										this.baseParams.parentId = (node.isRoot
												? ""
												: node.id);
									}
								}
							}),
							expanded : true,
							allowDrag : true,
							allowDrop : true
						});

						var groupTree = new Ext.tree.TreePanel( {
							id : 'groupTree',
							title : '组织',
							dropAllowed : true,
							dragAllowed : true,
							split : true,
							width : 180,
							minSize : 180,
							maxSize : 400,
							collapsible : false,
							margins : '0 0 5 5',
							cmargins : '0 5 5 5',
							enableDD : true,
							rootVisible : true,
							lines : false,
							autoScroll : true,
							root : rootGroupLoader,
							tbar : [{
								text : '刷新',
								handler : function() {
									groupTree.getRootNode().reload();
								}
							}],
							collapseFirst : true
						})
						return groupTree;
					},
					getRoleTree : function() {
						var rootLoader = new Ext.tree.AsyncTreeNode( {
							text : '角色',
							loader : new Ext.tree.TreeLoader( {
								dataUrl : contextPath + "/jteap/system/role/RoleAction!showRoleTreeAction.do",
								listeners : {
									beforeload : function(loader, node,
											callback) {
										this.baseParams.parentId = (node.isRoot
												? ""
												: node.id);
									}
								}

							}),
							expanded : true,
							allowDrag : true,
							allowDrop : true
						});

						var roleTree = new Ext.tree.TreePanel( {
							id : 'roleTree',
							title : '角色',
							dropAllowed : true,
							dragAllowed : true,
							split : true,
							width : 180,
							minSize : 180,
							maxSize : 400,
							collapsible : true,
							margins : '0 0 5 5',
							cmargins : '0 5 5 5',
							enableDD : true,
							rootVisible : true,
							lines : false,
							autoScroll : true,
							root : rootLoader,
							tbar : [{
								text : '刷新',
								handler : function() {
									roleTree.getRootNode().reload();
								}
							}],
							collapseFirst : true
						})
						return roleTree;
					}
				})

// 中间的SercherPanel
//var CenterSearchPanel = function(config) {
//	// 点击查询后的事件
//	this.searchClick = function() {
//		// 获取当前节点
//		var oNode = roleGroupTreeTabPanel.getActiveTab().getSelectionModel()
//				.getSelectedNode();
//		var whereHeader = "obj.";
//		if (!oNode.isRootNode() && !oNode.dissociation) {
//			whereHeader = "obj.person.";
//		}
//		var oPanel = searchtemp.items.get(0);
//		var oItems = oPanel.items.items;
//		var queryParamsJson = {};
//		var queryParamsSql = "";
//		Ext.each(oItems, function(oItem) {
//			if (oItem.hidden == false) {
//				var temp = oItem.items.items[0];
//				var tempValue = temp.getValue();
//				// 值不为空才作为参数
//				if (tempValue != null && tempValue != "") {
//					// 日期字段,就相应的处理为字符串
//				if (temp.triggerClass == "x-form-date-trigger") {
//					tempValue = formatDate(tempValue, "yyyy-MM-dd");
//					queryParamsSql += whereHeader + temp.id.split("#")[1]
//							+ "='" + encodeURIComponent(tempValue) + "' and ";
//					// 文本字段模糊查询
//				} else {
//					queryParamsSql += whereHeader + temp.id.split("#")[1]
//							+ " like '"
//							+ encodeURIComponent("$" + tempValue + "$")
//							+ "' and ";
//				}
//			}
//		};
//	}	);
//		// 根组织
//		if (oNode.isRootNode()) {
//			var url = link10 + "?queryParamsSql="
//					+ queryParamsSql.substring(0, queryParamsSql.length - 5);
//			centerPersonGrid.changeToPersonDS(url);
//			// 游离
//		} else if (oNode.dissociation) {
//			var url = link11 + "?queryParamsSql="
//					+ queryParamsSql.substring(0, queryParamsSql.length - 5);
//			centerPersonGrid.changeToPersonDS(url);
//			// 其他组织
//		} else {
//			if (roleGroupTreeTabPanel.getActiveTab() == roleGroupTreeTabPanel.groupTree) {
//				var url = link12
//						+ "?queryParamsSql="
//						+ queryParamsSql
//								.substring(0, queryParamsSql.length - 5);
//				url += "&groupId=" + oNode.id;
//				centerPersonGrid.changeToListDS(url);
//			} else {
//				var url = link12
//						+ "?queryParamsSql="
//						+ queryParamsSql
//								.substring(0, queryParamsSql.length - 5);
//				url += "&roleId=" + oNode.id;
//				centerPersonGrid.changeToListDS(url);
//			}
//		}
//		centerPersonGrid.getStore().reload();
//	};
//	CenterSearchPanel.superclass.constructor.call(this, config);
//};
//
//Ext.extend(CenterSearchPanel, Ext.app.SearchPanel, {});

// 中间的Grid
var CenterPersonGrid = function() {
	var centerPersonGrid = this
	var defaultDs = this.getPersonActionDS(link10);
	this.pageToolbar = new Ext.PagingToolbar( {
		// pageSize: CONSTANTS_PAGE_DEFAULT_LIMIT,
		store : defaultDs,
		displayInfo : true,
		//displayMsg : '共{2}条数据，目前为 {0} - {1} 条',
		emptyMsg : "没有符合条件的数据"
	});
	CenterPersonGrid.superclass.constructor.call(this, {
		ds : defaultDs,
		cm : this.getPersonActionColumnModel(),
		sm : this.sm,
		ddGroup : 'GridDD',
		enableDragDrop : true,
		dropAllowed : true,
		dragAllowed : true,
		border : false,
		bodyBrder : false,
		height : 100,
		loadMask : true,
		frame : true,
		region : 'center',
		tbar : this.pageToolbar
	});
	centerPersonGrid.on("celldblclick",
			function(grid, rowIndex, columnIndex, e) {
				var obj = grid.store.getAt(rowIndex);
				var result={};
			    if(obj.data.id)  result.loginName = obj.data.id;
			   	   else result.loginName = obj.data['person.id'];
			    if(obj.data.userName) result.name = obj.data.userName;
			       else result.name = obj.data['person.userName'];
				window.returnValue = result ;
				Ext.getCmp('oWindow').close();
				window.close();
			})
}

Ext
		.extend(
				CenterPersonGrid,
				Ext.grid.GridPanel,
				{
					sm : new Ext.grid.RowSelectionModel(),
					/**
					 * 切换数据源->P2GAction!showList
					 */
					changeToListDS : function(url) {
						var ds = this.getP2GActionDS(url);
						var cm = this.getP2GActionColumnModel();
						this.pageToolbar.bind(ds);
						this.reconfigure(ds, cm);
					},
					/**
					 * 切换数据源->PersonAction!showList 主要显示游离用户
					 */
					changeToPersonDS : function(url) {
						var ds = this.getPersonActionDS(url);
						var cm = this.getPersonActionColumnModel();
						this.pageToolbar.bind(ds);
						this.reconfigure(ds, cm);
					},
					/**
					 * 取得P2G数据源
					 */
					getP2GActionDS : function(url) {
						var ds = new Ext.data.Store( {
							proxy : new Ext.data.ScriptTagProxy( {
								url : url
							}),
							reader : new Ext.data.JsonReader( {
								root : 'list',
								totalProperty : 'totalCount',
								id : 'id'
							}, ['person.userLoginName', 'person.id',
									'person.userName', 'person.sex',
									'person.status']),
							remoteSort : true
						});
						ds.setDefaultSort('person.userName', 'desc');
						return ds;
					},
					/**
					 * 取得PersonAction数据源
					 */
					getPersonActionDS : function(url) {
						var ds = new Ext.data.Store( {
							proxy : new Ext.data.ScriptTagProxy( {
								url : url
							}),
							reader : new Ext.data.JsonReader( {
								root : 'list',
								totalProperty : 'totalCount',
								id : 'id'
							}, ['id','userLoginName', 'userName', 'sex', 'status']),
							remoteSort : true
						});
						ds.setDefaultSort('userName', 'desc');
						return ds;
					},

					/**
					 * P2GAction 列模型
					 */
					getP2GActionColumnModel : function() {
						var cm = new Ext.grid.ColumnModel([
//								this.sm,
								{
									id : 'userName',
									header : "昵称",
									width : 120,
									sortable : true,
									dataIndex : 'person.userName',
									renderer : function(value, metadata,
											record, rowIndex, colIndex, store) {
										if (record.json.admin == true)
											return "<span style='color:blue;font-weight:bold;'>"
													+ value + "</span>";
										else
											return value;
									}
								}, {
									id : 'userLoginName',
									header : "用户名",
									width : 120,
									sortable : true,
									dataIndex : 'person.userLoginName'
								}, {
									id : 'sex',
									header : "性别",
									width : 100,
									sortable : true,
									dataIndex : 'person.sex'
								}]);
						return cm;
					},
					/**
					 * PersonAction 列模型
					 */
					getPersonActionColumnModel : function() {
						var cm = new Ext.grid.ColumnModel([
						{id:'id',header:'id',hidden:'true',dataIndex:'id'},
						{
							id : 'userName',
							header : "昵称",
							width : 120,
							sortable : true,
							dataIndex : 'userName'
						}, {
							id : 'userLoginName',
							header : "用户名",
							width : 120,
							sortable : true,
							dataIndex : 'userLoginName'
						}, {
							id : 'sex',
							header : "性别",
							width : 100,
							sortable : true,
							dataIndex : 'sex'
						}]);
						return cm;
					},
					getPersonIds : function(select) {
						var personids = "";
						for (var i = 0;i < select.length; i++) {
							var temp = select[i];
							var id = temp.get("person.id");
							if (!id) {
								personids += temp.id + ",";
							} else {
								personids += id + ",";
							}
						}
						return personids;
					},
					getP2GIds : function(select) {
						var p2gids = "";
						for (var i = 0;i < select.length; i++) {
							var temp = select[i];
							var id = temp.id;
							p2gids += temp.id + ",";
						}
						return p2gids;
					}

				})

// 中间右边的Panel
var btnPanel = new Ext.Panel( {
	margins : '205 0 230 5',
	region : "east",
	border : true,
	bodyBorder : true,
	width : 30,
	height : 500,
	items : [
			{
				xtype : 'tbbutton',
				cls : 'x-btn-text-icon',
				icon : "icon/right2.gif",
				minWidth : 20,
				handler : function() {
					writeListBoxFromGridPanel(centerPersonGrid, rightListBox,
							onlyOne);
				}
			},
			{
				xtype : 'tbbutton',
				cls : 'x-btn-text-icon',
				icon : "icon/left2.gif",
				handler : function() {
					var selectionsArray = rightListBox.view
							.getSelectedIndexes();
					if (selectionsArray.length == 0) {
						return;
					}
					// 按选择索引从大到小排序
					selectionsArray.sort(function compare(a, b) {
						return b - a;
					});
					for (var i = 0;i < selectionsArray.length; i++) {
						var record = rightListBox.view.store
								.getAt(selectionsArray[i]);
						rightListBox.view.store.remove(record);

						if (record.data.type == 'group') {
							var node = roleGroupTreeTabPanel.groupTree
									.getNodeById(record.data.id);
							node.getUI().checkbox.checked = false;
						}
					}
					rightListBox.view.refresh();
				}
			}

	]

});

// 右边的ListBox
var store = new Ext.data.SimpleStore( {
	data : [],
	// expandData:true,
		fields : ["key", "value"]
	}, ['key', 'value']);
var rightListBox = new Ext.ux.Multiselect( {
	tbar : ["被选人员"],
	margins : '5 5 5 5',
	cmargins : '0 5 5 5',
	region : 'east',
	width : 130,
	height : 501,
	displayField : 'key',
	store : store,
	allowDup : true,
	copy : true,
	allowTrash : true,
	appendOnly : false,
	isFormField : false,
	listeners : {
		render : function() {
			if (admin != null) {
				for (var i = 0;i < admin.length; i++) {
					var record = new Model( {
						key : admin[i].name,
						id : admin[i].id,
						type : admin[i].type
					})
					rightListBox.view.store.add(record);
				}
			}
			if (check != null) {
				for (var i = 0;i < check.length; i++) {
					var record = new Model( {
						key : check[i].name,
						id : check[i].id,
						type : check[i].type
					})
					rightListBox.view.store.add(record);
				}
			}
			if (publish != null) {
				for (var i = 0;i < publish.length; i++) {
					var record = new Model( {
						key : publish[i].name,
						id : publish[i].id,
						type : publish[i].type
					})
					rightListBox.view.store.add(record);
				}
			}
		}
	}
});

var PersonSelectWindow = function(config) {

}

Ext.extend(PersonSelectWindow, Ext.Window, {})