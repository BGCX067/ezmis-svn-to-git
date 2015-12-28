
// 流程名称
var flowName = new Ext.form.Field({
			xtype : 'textfield',
			width : 190,
			id : 'txtFlowName',
			allowBlank : false,
			blankText : '请输入流程名称',
			fieldLabel : '流程名称',
			name : 'flowName',
			value : getLabelValue('flowName')
		});
var infoFlowName = new Ext.app.LabelPanel('当前流程名称，必填');
// 流程分类
var flowType = {
	xtype : 'textfield',
	readOnly : true,
	id : 'txtFlowType',
	allowBlank : true,
	blankText : '请输入流程分类',
	fieldLabel : '流程分类',
	name : 'flowType',
	width : 190,
	value : getLabelValue('flowType')
}
var infoFlowType = new Ext.app.LabelPanel('当前流程所属的分类名称,只读');

// 类型编号
var flowTypeId = new Ext.form.Hidden({
			id : 'txtFlowTypeId',
			name : 'flowTypeId',
			value : getLabelValue('flowTypeId')
		});

// 表单类型  01:EFORM,02:CFORM,03:文档型表单,04:无
var radioFormType = new Ext.form.RadioGroup({
			fieldLabel : '表单类型',
			items : [{
						boxLabel : 'EFORM表单',
						name : 'saveType',
						inputValue : '01',
						checked : true,
						listeners : {
							"check" : radioFormTypeChanged
						}
					}, {
						boxLabel : 'CFORM表单',
						name : 'saveType',
						inputValue : '02',
						listeners : {
							"check" : radioFormTypeChanged
						}
					}, {
						boxLabel : '文档型表单',
						name : 'saveType',
						inputValue : '03',
						listeners : {
							"check" : radioFormTypeChanged
						}
					}, {
						boxLabel : '无',
						name : 'saveType',
						inputValue : '04',
						listeners : {
							"check" : radioFormTypeChanged
						}
					}]
		});
var infoFormType = new Ext.app.LabelPanel('表单类型，默认为自定义表单');

/**
 * 表单类型 改变是触发
 */
function radioFormTypeChanged(radio, checked) {
	if (checked) {
		var formType = radio.getGroupValue();
		if (formType == "01" || formType == '02') {
			relFormName.setDisabled(false);
			selButton.setDisabled(false);
		} else {
			relFormName.setDisabled(true);
			selButton.setDisabled(true);
		}
	}
}
// 关联表单名称
var relFormName = new Ext.form.TextField({
			xtype : 'textfield',
			disabled : (getLabelValue('formType') == '01' || getLabelValue('formType') == '02') ? false : true,
			id : 'txtRelFormName',
			allowBlank : true,
			fieldLabel : '关联表单',
			name : 'relFormName',
			width : 190,
			value : getLabelValue('relFormName'),
			readOnly : true
		});
// 关联表单编号
var relFormId = new Ext.form.Hidden({
			id : 'txtRelFormId',
			name : 'relFormId',
			value : getLabelValue('relFormId')
		})
// 选择form项的按钮
var selButton = new Ext.Button({
			xtype : 'button',
			text : '...',
			disabled : (getLabelValue('formType') == '01' || getLabelValue('formType') == '02') ? false : true,
			handler : showCheckEformTreeWindow
		});
// 流程处理页面
var processPage = new Ext.form.TextField({
			xtype : 'textfield',
			id : 'txtProcessPage',
			allowBlank : true,
			readOnly : false,
			fieldLabel : '处理页面',
			name : 'processPage',
			width : 190,
			value : getLabelValue('processPage')
		})
// 待办主题公式
var topicCF = new Ext.form.TextField({
			xtype : 'textfield',
			id : 'txtTopicCF',
			allowBlank : true,
			readOnly : false,
			fieldLabel : '待办主题',
			name : 'topicCF',
			width : 190,
			value : getLabelValue('topicCF')
		})
// 待办主题公式选择按钮
var topicButton = new Ext.Button({
			xtype : 'button',
			text : '...',
			disabled : false,
			handler : showTopicCFWindow
		});

// 流程变量GridPanel
var flowVariableGrid = new FlowVariableGrid();

// // 流程操作GridPanel
// var flowOperateGrid = new FlowOperateGrid();

// 编辑formPanel
var formPanel = new Ext.form.FormPanel({
			reader : new Ext.data.JsonReader({
						successProperty : 'success',
						root : 'data'
					}, [{
								name : 'flowName',
								mapping : 'flowName'
							}, {
								name : 'flowType',
								mapping : 'flowType'
							}, {
								name : 'relFormName',
								mapping : 'relFormName'
							}, {
								name : 'processPage',
								mapping : 'processPage'
							}, {
								name : 'topicCF',
								mapping : 'topicCF'
							}]),
			frame : true, // 圆角风格
			labelWidth : 80, // 标签宽度
			labelAlign : 'left',
			buttonAlign : 'right',
			style : 'margin:2px',
			items : [{
						layout : 'column',
						items : [
								// 第一行布局
								{
							columnWidth : .5,
							layout : 'form',
							items : [flowName]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [infoFlowName]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [flowType]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [infoFlowType]
						}, {
							columnWidth : .8,
							layout : 'form',
							items : [radioFormType]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [relFormName]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [selButton]
						}, {
							columnWidth : 1,
							layout : 'form',
							items : [processPage]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [topicCF]
						}, {
							columnWidth : .5,
							layout : 'form',
							items : [topicButton]
						}, {
							columnWidth : 1,
							activeTab : 0,
							plain : true,
							xtype : 'tabpanel',
							height : 260,
							width : 500,
							defaults : {
								bodyStyle : 'padding:2px,margin: 2px 2px 2px 2px'
							},
							items : [{
										title : '流程变量',
										layout : 'form',
										items : flowVariableGrid
									}]
						}]
					}],
			buttons : [{
						text : '保存并关闭',
						hidden : false,
						handler : saveForm
					}, {
						text : '取消',
						hidden : false,
						handler : function() {
							window.close();
						}
					}]
		});

function showTopicCFWindow() {
	var data = flowVariableGrid.getStore().data.items;
	var textareaValue = topicCF.getValue();
	var simpleData = "[";
	for (var i = 0; i < data.length; i++) {
		var name_cn = "'" + data[i].data.cnName + "'";
		var name = "'${" + data[i].data.variableName + "}'";

		simpleData += "[" + name_cn + "," + name + "],";
	}
	if (simpleData.charAt(simpleData.length - 1) == ",")
		simpleData = simpleData.substring(0, simpleData.length - 1);

	simpleData += "]";

	var store = new Ext.data.SimpleStore({
				data : Ext.decode(simpleData),
				fields : ['name_cn', 'name']
			});
	var multiselect = new Ext.ux.Multiselect({
				width : '95%',
				height : 250,
				store : store,
				displayField : 'name_cn',
				legend : '流程变量',
				allowDup : true,
				copy : true,
				allowTrash : true,
				appendOnly : false,
				isFormField : false
			});
	multiselect.on("dblclick", function(vw, index, node, e) {
				if (!conditionTxt.hasFocus) {
					conditionTxt.focus();
				}
				document.selection.createRange().text = vw.store.data.items[index]
						.get('name');
			})
	var conditionTxt = new Ext.form.TextArea({
				width : '95%',
				height : 250,
				value : textareaValue
			})
	var topicItem = new Ext.Panel({
				layout : 'column',
				bodyStyle : 'padding:2px 2px 2px 2px',
				items : [{
							columnWidth : .5,
							border : false,
							items : [multiselect]
						}, {
							columnWidth : .5,
							border : false,
							bodyStyle : 'padding:8px 0px 0px 0px',
							items : [conditionTxt]
						}]
			});

	var topicCFWindow = new Ext.Window({
				layout : 'fit',
				title : '待办主题生成器',
				width : 450,
				height : 350,
				frame : true,
				items : topicItem,
				buttons : [{
							text : '确定',
							handler : function() {
								// window.returnValue = conditionTxt.getValue();
								topicCF.setValue(conditionTxt.getValue());
								topicCFWindow.close();
							}
						}, {
							text : '取消',
							handler : function() {
								topicCFWindow.close();
							}
						}]
			});
	// 显示窗口
	topicCFWindow.show();
}

/**
 * 选择自定义表单
 */
function showCheckEformTreeWindow() {
	var formType = radioFormType.getGroupValue();
	var link = formType == "01" ? link3 : link1
	
	var indicatTree = new Ext.tree.TreePanel({
				id : 'resTree',
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
				root : new Ext.tree.AsyncTreeNode({
							text : '所有表单',
							loader : new Ext.tree.TreeLoader({
										dataUrl : link
									}),
							expanded : true
						}),
				submitChange : function() {
					var attrs = indicatTree.getSelectionModel().selNode.attributes;
					if (!attrs.isItem) {
						alert("您选择的是分类,请选择分类下的项");
						return;
					}
					relFormId.setValue(attrs.id);
					relFormName.setValue(attrs.text);
					processPage.setValue(attrs.eformUrl);
					indicatWindow.close();
				}
			});
	var indicatWindow = new Ext.Window({
				layout : 'fit',
				title : '资源选择器',
				width : 250,
				height : 350,
				frame : true,
				items : indicatTree,
				buttons : [{
							text : '确定',
							handler : function() {
								indicatTree.submitChange();
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

/**
 * 保存表单
 */
function saveForm() {
	// 判断是否填写了流程名称。
	if (Ext.get("txtFlowName").getValue() == null
			|| Ext.get("txtFlowName").getValue() == "") {
		alert("流程名称必填");
		return;
	}
	// 判断流程变量是否有同名现象
	var variableItems = flowVariableGrid.store.data.items;
	for (var i = 0; i < variableItems.length; i++) {
		var firstName = variableItems[i].data.variableName;
		if (firstName == "") {
			alert("变量名不能为空");
			return;
		}
		for (var j = i + 1; j < variableItems.length; j++) {
			var secName = variableItems[j].data.variableName;
			if (firstName == secName) {
				alert("请先处理好变量名称同名现象");
				return;
			}
		}
	}

	// 开始写入内存的xml中
	var value = model.getValue(cell);
	model.beginUpdate();
	try {

		flowVariableGrid.getStore().commitChanges();
		flowVariableGrid.stopEditing();

		// flowOperateGrid.getStore().commitChanges() ;
		// flowOperateGrid.stopEditing() ;

		// 修改流程名称的xml
		var edit = new mxCellAttributeChange(cell, "flowName", Ext
						.get("txtFlowName").getValue());
		model.execute(edit);

		// 修改流程分类的xml
		edit = new mxCellAttributeChange(cell, "flowType", Ext
						.get("txtFlowType").getValue());
		model.execute(edit);

		// 修改表单类型的xml
		/*
		 * var formType = "" ; var radios =
		 * document.getElementsByName("formType") ; for(var i=0 ; i<radios.length ;
		 * i++) { if(radios[i].checked) { formType = radios[i].value ; break ; } }
		 */
		formType = radioFormType.getGroupValue();
		edit = new mxCellAttributeChange(cell, "formType", formType == null
						? ""
						: formType);
		model.execute(edit);

		// 修改关联表单Name的xml
		edit = new mxCellAttributeChange(cell, "relFormName", Ext
						.get("txtRelFormName").getValue());
		model.execute(edit);

		// 修改关联表单ID的xml
		edit = new mxCellAttributeChange(cell, "relFormId", relFormId
						.getValue());
		model.execute(edit);

		// 修改处理页面的xml
		edit = new mxCellAttributeChange(cell, "processPage", Ext
						.get("txtProcessPage").getValue());
		model.execute(edit);

		// 修改主题的xml
		edit = new mxCellAttributeChange(cell, "topicCF", Ext.get("txtTopicCF")
						.getValue());
		model.execute(edit);

		// 同步所有任务节点的处理页面的xml
		for (var i = 0; i < model.nextId; i++) {
			var taskCell = model.cells[i];
			if (taskCell == null) {
				continue;
			}
			if (taskCell.value.tagName != "task") {
				continue;
			}
			// 同步环节的process_url
			edit = new mxCellAttributeChange(taskCell, "process_url", Ext
							.get("txtProcessPage").getValue());
			model.execute(edit);

			// 同步环节中task属性的process_url
			var jsonObj = Ext.decode(getLabelValue("task", taskCell).replace(
					/%26quot;/g, "\""))
			jsonObj.process_url = Ext.get("txtProcessPage").getValue();

			edit = new mxCellAttributeChange(taskCell, "task", Ext
							.encode(jsonObj).replace(/"/g, "'").replace(/'\[/g,
									"%26quot;[").replace(/\]'/g, "]%26quot;")
							.replace(/'null'/g, "null"));
			model.execute(edit);
		}
		// 修改流程变量的xml
		var flowVariable = "[";
		var variable = "";
		var items = flowVariableGrid.getStore().data.items;
		for (var i = 0; i < items.length; i++) {
			var reJson = items[i];
			variable = "{";
			variable += "'name_cn':'" + items[i].get("cnName") + "',";
			variable += "'storemode':'" + items[i].get("storeMode") + "',";
			variable += "'kind':'" + items[i].get("type") + "',";
			variable += "'name':'" + items[i].get("variableName") + "',";
			variable += "'type':'" + items[i].get("variableType") + "',";
			variable += "'value':'" + items[i].get("variableValue") + "'";
			variable += "},";
			flowVariable += variable;
		}
		if (flowVariable.length > 1) {
			flowVariable = flowVariable.substring(0, flowVariable.length - 1)
		}
		flowVariable += "]"
		edit = new mxCellAttributeChange(cell, "flowVariable", flowVariable);
		model.execute(edit);

		// //修改流程操作的xml
		// var flowOperate = "["
		// var operate = ""
		// var items = flowOperateGrid.getStore().data.items
		// for(var i=0 ; i<items.length ; i++) {
		// var reJson = items[i] ;
		// operate = "{"
		// operate += "'name':'"+items[i].get("operaName")+"',"
		// operate += "'mark':'"+items[i].get("btnSn")+"'"
		// operate += "},"
		// flowOperate += operate
		// }
		// if(flowOperate.length > 1) {
		// flowOperate = flowOperate.substring(0,flowOperate.length-1)
		// }
		// flowOperate += "]"
		// edit = new mxCellAttributeChange(cell,"flowOperate",flowOperate) ;
		// model.execute(edit) ;

		// 写入workflow的xml值
		var workflow = {
			name : Ext.get("txtFlowName").getValue() == "" ? "" : Ext
					.get("txtFlowName").getValue(),
			formtype : formType == "" ? null : formType,
			flowCatalog : {
				id : flowTypeId.getValue() == "" ? null : flowTypeId.getValue()
			},
			formId : relFormId.getValue() == "" ? null : relFormId.getValue(),
			process_url : Ext.get("txtProcessPage").getValue() == ""
					? null
					: Ext.get("txtProcessPage").getValue(),
			flowVariables : Ext.decode(flowVariable)
			// ,
			// flowOperations : Ext.decode(flowOperate)
		}
		workflow = Ext.encode(workflow).replace(/"/g, "'");
		edit = new mxCellAttributeChange(cell, "workflow", workflow);
		model.execute(edit);
		
	} catch (e) {
		alert("错误:" + e);
	} finally {
		model.endUpdate();
		window.close();
	}
}

function onload() {
	var cFormType = getLabelValue('formType');
	radioFormType.setGroupValue(cFormType);
}

var titlePanel = new Ext.app.TitlePanel({
			caption : '流程属性设置',
			border : false,
			region : 'north'
		});
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	border : true,

	items : [formPanel]
}