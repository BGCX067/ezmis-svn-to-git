var IndividualDetailForm = function() {
	centerPanel = this;

	// 模块名
	var txtModuleName = new Ext.form.TextField( {
		id : 'txtModuleName',
		fieldLabel : '模块名',
		maxLength : 30,
		minLength : 1,
		minLengthText : '最少需要1个字符（包括汉子和合法字符）',
		maxLengthText : '最长30个字符',
		name : 'resName',
		allowBlank : false,
		anchor : '90%',
		editable : true
	// 可编辑
	});
	var infoModuleName = new Ext.app.LabelPanel('模块名称，1-30个字符');

	// 模块链接
	var txtModuleLink = new Ext.form.TextField( {
		fieldLabel : '模块链接',
		id : 'txtModuleLink',
		name : 'link',
		maxLength : 500,
		minLength : 1,
		anchor : '90%'
	});
	var infoModuleLink = new Ext.app.LabelPanel('模块链接地址，不可编辑');

	// 模块描述
	var txtRemark = new Ext.form.TextField( {
		fieldLabel : '模块描述',
		id : 'txtRemark',
		name : 'remark',
		anchor : '90%',
		maxLength : 300
	})
	var infoRemark = new Ext.app.LabelPanel('模块描述，最多200字');

	// 是否是系统模块
	var isSysModule = new Ext.form.RadioGroup( {
		fieldLabel : '系统模块',
		items : [ {
			boxLabel : '是',
			readOnly : true,
			name : 'resStyle',
			inputValue : '0'

		}, {
			boxLabel : '否',
			readOnly : true,
			name : 'resStyle',
			inputValue : '1',
			checked : true
		}]
	})
	var infoSysModule = new Ext.app.LabelPanel('');

	var hdnParentId = new Ext.form.Hidden( {
		name : 'parentId'
	})

	var hdnResourceId = new Ext.form.Hidden( {
		name : 'resourceId'
	})

	var simpleForm = new Ext.FormPanel( {
		id : 'simpleForm',
		title : '模块详细信息',
		labelAlign : 'left',
		autoHeight : true,
		buttonAlign : 'right',
		frame : true,
		labelWidth : 80, // 标签宽度
		items : [{
			layout : 'column',
			style : 'padding-left:8px;',
			border : false,
			labelSeparator : '：',
			defaults : {
				blankText : '必填字段'
			},
			items : [ {
				columnWidth : .6,
				layout : 'form',
				border : false,
				items : [txtModuleName, hdnParentId, hdnResourceId]
			}, {
				columnWidth : .4,
				layout : 'form',
				border : false,
				items : [infoModuleName]
			}, {
				columnWidth : .6,
				layout : 'form',
				border : false,
				items : [txtModuleLink]
			}, {
				columnWidth : .4,
				layout : 'form',
				border : false,
				items : [infoModuleLink]
			}, {
				columnWidth : .6,
				layout : 'form',
				border : false,
				items : [txtRemark]
			}, {
				columnWidth : .4,
				layout : 'form',
				border : false,
				items : [infoRemark]
			}, {
				columnWidth : .6,
				layout : 'form',
				border : false,
				items : [isSysModule]
			}, {
				columnWidth : .4,
				layout : 'form',
				border : false,
				items : [infoSysModule]
			}]
		}],
		buttons : [{
			text : '保存',
			handler : function() {
				if (!simpleForm.form.isValid()) {
					alert('数据校验失败，请检查填写的数据格式是否正确');
					return;
				}

				if (window.confirm('确认要保存吗?')) {
					var link = "";
					if (isSysModule.getValue() == 0) {
						link = link13;
					} else {
						link = link14;
					}

					simpleForm.form.doAction('submit', {
						url : link,
						method : 'post',
						waitMsg : '保存数据中，请稍候...',
						success : function() {
							alert('保存成功');
							individualTree.getRootNode().reload();
						},
						failure : function() {
							alert('服务器忙，请稍候操作...');
							centerPanel.buttons[0].enable();
						}
					});
				}
			}
		}]
	})

	/**
	 * 加载修改数据
	 */
	this.loadData = function(oNode) {
		if (oNode.attributes.resStyle == 0) {
			Ext.getCmp("txtModuleLink").getEl().dom.readOnly = true;
		} else {
			Ext.getCmp("txtModuleLink").getEl().dom.readOnly = false;
		}
		isSysModule.setValue(oNode.attributes.resStyle);
		txtModuleName.setValue(oNode.text);
		txtModuleLink.setValue(oNode.attributes.link);
		txtRemark.setValue(oNode.attributes.remark);
		hdnResourceId.setValue(oNode.id)
	}

	this.reset = function(oNode) {
		simpleForm.getForm().reset();
		Ext.getCmp("txtModuleLink").getEl().dom.readOnly = false;
		hdnParentId.setValue(oNode.id);
	}

	IndividualDetailForm.superclass.constructor.call(this, {
		region : 'center',
		autoHeight : true,
		border : false,
		frame : true,
		labelWidth : 80, // 标签宽度
			items : [simpleForm]
		});
}

Ext.extend(IndividualDetailForm, Ext.Panel, {
	/**
	 * 批量设置快速链接
	 */
	setupLink : function(select) {
		this.showIndicatLinkWindow();
	},
	showIndicatLinkWindow : function() {
		var oldIds = "";
		// 指定角色树
		var indicatTree = new Ext.tree.TreePanel( {
			id : 'setupLinkTree',
			autoScroll : true,
			autoHeight : false,
			height : 170,
			width : 150,
			originalValue : "",
			animate : false,
			ctrlCasecade : true, // 是否只支持 按住ctrl键进行勾选的时候是级联勾选
			enableDD : true,
			containerScroll : true,
			defaults : {
				bodyStyle : 'padding:0px'
			},
			border : false,
			hideBorders : true,
			rootVisible : true,
			lines : false,
			bbar : ['-', '<font color="blue">*按住CTRL键可进行级联选择</font>', '-'],
			bodyBorder : false,
			root : new Ext.app.CheckboxAsyncTreeNode( {
				ccCheck : true,
				text : '我的模块',
				loader : new Ext.app.CheckboxTreeNodeLoader( {
					dataUrl : link16
				}),
				listeners : {
					expand : function() {
						oldIds = this.getCheckedIds(true, false);
					}
				},
				expanded : true
			}),
			submitChange : function() {
				var thisx = this;
				// 取得包含第三状态的节点的被选中节点编号
				var linkids = this.getRootNode().getCheckedIds(true, false);
				// 提交数据
				Ext.Ajax
						.request( {
							url : link17,
							method : 'POST',
							params : {
								linkids : linkids,
								oldIds : oldIds
							},
							success : function(ajax) {
								var responseText = ajax.responseText;
								var responseObject = Ext.util.JSON
										.decode(responseText);
								if (responseObject.success)
									alert("快速链接指定成功");
								else
									alert("快速链接指定失败");
								indicatWindow.close();
							},
							failure : function() {
								alert("快速链接指定失败");
							}
						})
			}
		});
		// 资源选择窗口
		var indicatWindow = new Ext.Window( {
			layout : 'fit',
			title : '快速链接选择器',
			width : 250,
			height : 350,
			frame : true,
			modal : true,
			items : indicatTree,
			buttons : [ {
				text : '确定',
				handler : function() {
					if (window.confirm('确认要选择这些模块吗?')) {
						indicatTree.submitChange();
					}

				}
			}, {
				text : '取消',
				handler : function() {
					indicatWindow.close();
				}
			}]
		});
		// 显示窗口
		indicatWindow.show();
	},
	/**
	 * 批量设置文档链接
	 */
	setupDocLink : function(select) {
		this.showDocLinkWindow();
	},
	showDocLinkWindow : function() {
		var oldIds = "";
		// 指定文档树
		var indicatTree = new Ext.tree.TreePanel( {
			id : 'setupDocLinkTree',
			autoScroll : true,
			autoHeight : false,
			height : 170,
			width : 150,
			originalValue : "",
			animate : false,
			ctrlCasecade : true, // 是否只支持 按住ctrl键进行勾选的时候是级联勾选
			enableDD : true,
			containerScroll : true,
			defaults : {
				bodyStyle : 'padding:0px'
			},
			border : false,
			hideBorders : true,
			rootVisible : true,
			lines : false,
			bodyBorder : false,
			root : new Ext.app.CheckboxAsyncTreeNode( {
				ccCheck : true,
				text : '所有文档',
				loader : new Ext.app.CheckboxTreeNodeLoader( {
					dataUrl : link19
				}),
				listeners : {
					expand : function() {
						oldIds = this.getCheckedIds(true, false);
					}
				},
				expanded : true
			}),
			submitChange : function() {
				var thisx = this;
				// 取得包含第三状态的节点的被选中节点编号
				var linkids = this.getRootNode().getCheckedIds(true, false);
				// 提交数据
				Ext.Ajax
						.request( {
							url : link18,
							method : 'POST',
							params : {
								linkids : linkids,
								oldIds : oldIds
							},
							success : function(ajax) {
								var responseText = ajax.responseText;
								var responseObject = Ext.util.JSON
										.decode(responseText);
								if (responseObject.success)
									alert("文档链接指定成功");
								else
									alert("文档链接指定失败");
								indicatWindow.close();
							},
							failure : function() {
								alert("文档链接指定失败");
							}
						})
			}
		});
		// 资源选择窗口
		var indicatWindow = new Ext.Window( {
			layout : 'fit',
			title : '文档链接选择器',
			width : 250,
			height : 350,
			frame : true,
			modal : true,
			items : indicatTree,
			buttons : [ {
				text : '确定',
				handler : function() {
					if (window.confirm('确认要选择这些文档分类吗?')) {
						indicatTree.submitChange();
					}

				}
			}, {
				text : '取消',
				handler : function() {
					indicatWindow.close();
				}
			}]
		});
		// 显示窗口
		indicatWindow.show();
	}
});
