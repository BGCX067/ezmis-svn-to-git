//var editTaskFormWindow = new EditTaskFormWindow() ;

var taskName = {xtype:'textfield' , allowBlank : true , blankText : '请输入环节名称' , fieldLabel : '环节名称' , height :25 , name : 'taskName' , width : 150 ,value : getLabelValue('name') } ;
	
var taskDesc = {xtype:'textfield' , allowBlank : true , blankText : '请输入环节描述' , fieldLabel : '环节描述' , height :25 , name : 'taskDesc' , width : 150 ,value : getLabelValue('description')} ;

var processPage = new Ext.form.TextField({
	allowBlank : true,
	fieldLabel : '处理页面' ,
	height :25 ,
	name : 'processPage' ,
	width : 150 ,
	value : getLabelValue('process_url') , 
	readOnly : true})

var hdnProcPage = new Ext.form.Hidden({
	name : 'process_form_id',
	value : getLabelValue('process_form_id')
})
// 关联表单编号
var relFormId = new Ext.form.Hidden({
			id : 'txtRelFormId',
			name : 'relFormId',
			value : getRootLabelValue('relFormId')
		})

// 选择form项的按钮
var selButton = new Ext.Button({
			xtype : 'button',
			text : '...',
			handler : showCheckEformTreeWindow
		});

//环节变量GridPanel
var taskVariableGrid = new TaskVariableGrid() ;

//获取当前环节没有的环节变量列表
function getVariableData(){
	var dataArr = Ext.decode(getLabelValue('flowVariable',rootCell));
	var retArr = new Array();
	for(var i = 0;i < dataArr.length;i++){
		var gridArray = taskVariableGrid.getStore().data.items;
		var exist = false;
		for(var j = 0;j < gridArray.length;j++){
			if(gridArray[j].data.variableName == dataArr[i].name){
				exist = true;
				break;
			}
		}
		
		if(exist == true)	continue;
		
		var tempData = new Array();
		tempData.push(dataArr[i].name);
		tempData.push(dataArr[i].name_cn);
		tempData.push(dataArr[i].value);
		tempData.push(dataArr[i].kind);
		tempData.push("可编辑");
		tempData.push(dataArr[i].name + "(" + dataArr[i].name_cn + ")");//显示的值
//		tempData.push(dataArr[i].flag);
//		dataArr[i] = "['" + dataArr[i].name + "','" + 
//						   dataArr[i].name_cn + "','" + 
//						   dataArr[i].value + "','" + 
//						   dataArr[i].kind + "','" + 
//						   dataArr[i].isNeed + "','" + 
//						   dataArr[i].flag + 
//					  "']";
		retArr.push(tempData);
	}
	return retArr;
}

var nodeVar = new Ext.ux.Multiselect(
{
	id : 'nodeVar',
	width : 240,
	height : 200,
	store : new Ext.data.SimpleStore({ 
                            data:getVariableData(),
                            fields:["name", "name_cn","value","kind","isNeed","displayfield"],
                            sortInfo : {field: 'name'}
                    },['displayfield','name']) ,
	displayField : 'displayfield',
	valueField:"name",
//	legend : '*双击选择该环节变量',
	allowDup : true,
	copy:true,
	allowTrash:true,
	appendOnly:false,
	isFormField: false
	}
) ;

//nodeVar.on("dblclick",function(vw, index, node, e) {
//	selectWFVar();
//})

/**
 * 选择流程变量
 */
function selectWFVar(){
	var records=nodeVar.view.getSelectedRecords();
	for(var i=0;i<records.length;i++){
		var obj = records[i];
		var data = new Object();
		data.variableName = obj.get('name');
		data.cnName = obj.get('name_cn');
		data.defaultValue = obj.get('value');
		data.type = obj.get('kind');
		data.isNeed = obj.get('isNeed');
		taskVariableGrid.createVariable(data);
	}
	nodeVar.removeSelection();
}

//环节变量选择窗口
var variableSelect = new Ext.Panel({
	id : 'viariable',
	width : 250,
	height : 270,
	margins:'2 2 2 2',
	region : 'west',
	collapsible : true,
	collapsed : false,
	frame : true,
	items:[nodeVar],
	tbar:['!!!<span style="line-height:20px">选择流程变量</span>','->',{
		text:'>>',
		handler:selectWFVar
	}]
})

//环节权限Panel
var taskPowerPanel = new TaskPowerPanel() ;

//可用操作
taskOperateGrid = new TaskOperateGrid();

//事件
var taskEventGrid = new TaskEventGrid();


//编辑formPanel	
var formPanel  = new Ext.form.FormPanel( {
	reader : new Ext.data.JsonReader({
			successProperty : 'success',
			root : 'data'
		},[{name:'taskName',mapping:'taskName'},{name:'taskDesc',mapping:'taskDesc'},{name:'processPage',mapping:'processPage'}
	]),
	bodyBorder:false ,
	border : false ,
	frame:true, 					//圆角风格
    labelWidth:80,					//标签宽度
    labelAlign: 'left',
    buttonAlign:'right',
    items: [{
		layout: 'column',
		items : [{	
			columnWidth:1 ,
			layout : 'form',
			items : [taskName]
		},{
			columnWidth:1 ,
			layout : 'form' ,
			items : [taskDesc]
		},{
			columnWidth:.41 ,
			layout : 'form' ,
			items : [processPage]					
		}, {
			columnWidth : .3,
			layout : 'form',
			items : [selButton]
		},{
			columnWidth:1 ,
			activeTab : 0 ,
			plain:true,
			xtype : 'tabpanel' ,
			height : 270,
			width : 490 ,
			listeners : {'tabchange':function(tabPanel, tab ) {
				tab.doLayout();
			}},
			items: [{
				title : '环节变量',
				layout: 'border' ,
				items : [taskVariableGrid,variableSelect]	
			},{
				title : '环节权限',
				items : taskPowerPanel
			},{
				title : '可用操作',
				layout: 'form' ,
				items : taskOperateGrid
			},{
				title : '事件',
				layout: 'form' ,
				items : taskEventGrid
			}]
		}]
	}],
    buttons : [{
		text:'确定',
		hidden : false ,
		handler : function() {
			submitForm();
		}
	},{
		text : '取消' ,
		hidden : false ,
		handler : function() {
			window.close() 
		}
    }]
})

/**
 * 选择自定义表单
 */
function showCheckEformTreeWindow() {
	var link = link6;
	
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
										dataUrl : link + "?formId="+relFormId.getValue()
									}),
							expanded : true
						}),
				submitChange : function() {
					var attrs = indicatTree.getSelectionModel().selNode.attributes;
					if (!attrs.isItem) {
						alert("您选择的是分类,请选择分类下的项");
						return;
					}
					hdnProcPage.setValue(attrs.id);
					processPage.setValue(attrs.text);
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

function submitForm(){
	//判断环节变量是否有同名现象
	var variableItems = taskVariableGrid.store.data.items ;
	for(var i=0 ; i<variableItems.length ; i++) {
		var firstName = variableItems[i].data.variableName ;
		if(firstName == "" && i<variableItems.length-1) {
			alert("变量名不能为空") ;
			return ;
		}
		for(var j=i+1 ; j<variableItems.length ; j++) {
			var secName = variableItems[j].data.variableName ;
			if(firstName == secName) {
				alert("请先处理好变量名称同名现象") ;
				return ;
			}
		}
	}
	var value = model.getValue(cell);
	model.beginUpdate();
	try {
		//修改环节名称的xml
		var edit = new mxCellAttributeChange(cell,"name",Ext.get("taskName").getValue());
		model.execute(edit) ;
		
		//修改环节label的xml
		var edit = new mxCellAttributeChange(cell,"label",Ext.get("taskName").getValue());
		model.execute(edit) ;
		
		//同步所指向改节点的edge名称
		if(cell.edges != null) {
			for(var i=0 ; i<cell.edges.length ; i++) {
				if(cell.edges[i].target == cell) {
					var edit = new mxCellAttributeChange(cell.edges[i],"aimTask",Ext.get("taskName").getValue()) ;
					model.execute(edit) ;
				}
			}	
		}
								
		//修改环节权限ListBox的xml
		var records = new Array() ;
			for(var i=0 ; i<listBox.store.data.items.length ; i++){
				if(typeof(listBox.store.data.items[i].data.value) == 'string'){
					records.push(Ext.decode(listBox.store.data.items[i].data.value)) ;
					continue ;
				}
				records.push(listBox.view.store.data.items[i].data.value) ;
		}
		edit = new mxCellAttributeChange(cell,"taskPower",Ext.encode(records).replace(/"/g,"'")) ;
		model.execute(edit) ;
		
		//修改环节描述的xml
		edit = new mxCellAttributeChange(cell,"description",Ext.get("taskDesc").getValue()) ;
		model.execute(edit) ;
		
		//修改处理页面的xml
		edit = new mxCellAttributeChange(cell,"process_url",Ext.get("processPage").getValue()) ;
		model.execute(edit) ;

		//修改处理页面ID的xml
		edit = new mxCellAttributeChange(cell,"process_form_id",hdnProcPage.getValue()) ;
		model.execute(edit) ;

		//修改处理类别的xml
		edit = new mxCellAttributeChange(cell,"processKind",taskPowerPanel.getProcessKindValue()) ;
		model.execute(edit) ;
		
		//修改处理模式的xml
		edit = new mxCellAttributeChange(cell,"processMode",taskPowerPanel.getProcessModeValue()) ;
		model.execute(edit) ;
		
		//修改是否指定一个处理人的xml
		edit = new mxCellAttributeChange(cell,"isOneProcessActor",taskPowerPanel.getOnlyOnePersonValue()) ;
		model.execute(edit) ;
		
		//修改可自由选择处理人的xml
		edit = new mxCellAttributeChange(cell,"isChooseActor",taskPowerPanel.getIsChooseActorValue()) ;
		model.execute(edit) ;
		//修改环节变量的xml,调用封装的方法writeXmlFromGrid();该方法在本页面最底下
		var nodeVariables = writeXmlFromGrid(taskVariableGrid,model,cell,"taskVariable")
		//修改环节操作的xml
		var taskOperations = writeXmlFromGrid(taskOperateGrid,model,cell,"taskOperate")
		
		//修改环节事件的xml
		writeXmlFromGrid(taskEventGrid,model,cell,"event") ;
		
		//将nodeVariables的字符串变成json对象并提出最后一个元素
		nodeVariables = Ext.decode(nodeVariables) ;
		//nodeVariables.pop() ;

		//修改WorkFlow的xml变量相关的xml
		var workflow = Ext.decode(getLabelValue("workflow",rootCell)) ;
		var flowVariables = workflow.flowVariables
		//遍历环节变量,并默认从环节中添加的流程变量storemode为追加，变量类型为String
		/*for(var i=0 ; i < nodeVariables.length ; i++){
			if(flowVariables.length==0){
				nodeVariables[i].flag = undefined ;
				nodeVariables[i].storemode = '追加' ;
				nodeVariables[i].type = 'String' ;
				flowVariables.push(nodeVariables[i]) ;
			}else{
				for(var j=0 ; j < flowVariables.length ; j++) {
					if(nodeVariables[i].flag){																		
						nodeVariables[i].flag = undefined ;
						nodeVariables[i].storemode = '追加' ;
						nodeVariables[i].type = 'String' ;
						flowVariables.push(nodeVariables[i]) ;
						break ;
					}
					if(flowVariables[j].name == nodeVariables[i].name){
						nodeVariables[i].flag = undefined ;
						flowVariables[j].kind = nodeVariables[i].kind ;
						flowVariables[j].name_cn = nodeVariables[i].name_cn ;
						break ;
					}
				}
			}
		}*/
		//重新写入xml中的flowVariable，workflow 属性值
		workflow.flowVariables = flowVariables ;
		edit = new mxCellAttributeChange(rootCell,"flowVariable",Ext.encode(flowVariables).replace(/"/g,"'")) ;
		model.execute(edit) ;
		edit = new mxCellAttributeChange(rootCell,"workflow",Ext.encode(workflow).replace(/"/g,"'")) ;
		model.execute(edit) ;
		
		//修改task的xml
		//nodeVariables = Ext.encode(nodeVariables) ;
		var newNodeVariables = "[" ;
		for(var i=0 ; i<nodeVariables.length ; i++) {
			newNodeVariables += "{'variable':{" ;
			newNodeVariables +="'name':'" ;
			newNodeVariables +=nodeVariables[i].name ;
			newNodeVariables +="'," ;
			newNodeVariables +="'name_cn':'" ;
			newNodeVariables +=nodeVariables[i].name_cn ;
			newNodeVariables +="'," ;
			newNodeVariables +="'kind':'" ;
			newNodeVariables +=nodeVariables[i].kind ;
			newNodeVariables +="'}," ;
			newNodeVariables +="'isNeed':'" ;
			newNodeVariables +=nodeVariables[i].isNeed ;
			newNodeVariables +="','value':'" ;
			newNodeVariables +=nodeVariables[i].value ;
			newNodeVariables += "'}," ;
		}
		if(newNodeVariables.length > 1) {
			newNodeVariables = newNodeVariables.substring(0,newNodeVariables.length-1) ;
		}
		newNodeVariables += "]" ;
		
		//将flowOperations的字符串变成json对象并提出最后一个元素
		taskOperations = Ext.decode(taskOperations) ;
		taskOperations.pop() ;
		
		//遍历records中的name得到权限的name 
		var name = "" ;
		for(var i=0 ; i<records.length ; i++) {
			name += records[i].name + "|" ;
		}
		name = name.substring(0 , name.length - 1) ;
		var task = {
			name : Ext.get("taskName").getValue()==""?null:Ext.get("taskName").getValue(),
			description : Ext.get("taskDesc").getValue()==""?null:Ext.get("taskDesc").getValue() ,
			process_url : Ext.get("processPage").getValue()==""?null:Ext.get("processPage").getValue() ,
			process_form_id : hdnProcPage.getValue()==""?null:hdnProcPage.getValue(),
			flowConfig : {
				name : getLabelValue("flowName",rootCell)==""?null:getLabelValue("flowName",rootCell)
			},
			nodeVariables : Ext.decode(newNodeVariables),//Ext.decode(nodeVariables) ,
//			taskOperations : taskOperations ,
			nodePermission : {
				name : name,
				processMode : taskPowerPanel.getProcessModeValue()==""?null:taskPowerPanel.getProcessModeValue(),
				processKind : taskPowerPanel.getProcessKindValue()==""?null:taskPowerPanel.getProcessKindValue(),
				isChooseActor : taskPowerPanel.getIsChooseActorValue()=="0"?false:true,
				isOneProcessActor : taskPowerPanel.getOnlyOnePersonValue()=="0"?false:true,
				expression : ("%26quot;"+Ext.encode(records)+"%26quot;").replace(/"/g,"'")
			}
		} ;
		
		task = Ext.encode(task).replace(/"/g,"'") ;
		//由于&在url中有特殊的意义，而xml中又要用转义字符，则用%26代替&
		task = task.replace(/'%26quot/g,"%26quot") ;
		task = task.replace(/%26quot;'/g,"%26quot;") ;
		//将'null'变成null ;
		task = task.replace(/'null'/g,"null") ;
		edit = new mxCellAttributeChange(cell,"task",task) ;
		model.execute(edit) ;
	} finally {
		model.endUpdate();
		window.close() 
	}
}
function onload(){
}
	
/**
 * 将相应的EditGrid的内容以json字符串的形式写入Xml，grid为数据源，model为mxGraph的model，cell为对应的Node，varName为xml中的属性
 * 使用此方法有一定的规则.
 * 既:grid要有Model属性。1.Model为Ext.data.Record.create(...)的返回值 或 2.为有name,maping属性的二维数组 或 3.为一维数组时候每个项为string
 * 返回json字符串
 */
function writeXmlFromGrid(grid,model,cell,varName) {
	grid.getStore().commitChanges() ;
	grid.stopEditing();
	var taskVariable = "["
		var variable = ""
		var items = grid.getStore().data.items
		for(var i=0 ; i<items.length ; i++) {
			var reJson = items[i] ;
			variable = "{"
			var arrayModel ;
			if(typeof(grid.Model) == "function") {
				arrayModel = grid.Model.prototype.fields.items ;
			} else if(typeof(grid.Model) == "object") {
				arrayModel = grid.Model
			}
			for(var j=0 ; j<arrayModel.length ; j++) {
				var temp = arrayModel[j].mapping==null?arrayModel[j].name:arrayModel[j].mapping ;
				if(typeof(arrayModel[j]) == 'object'){
					variable += "'"+temp+"':'"+items[i].get(arrayModel[j].name)+"'," ;
				} else if(typeof(arrayModel[j]) == 'string') {
					variable += "'"+arrayModel[j]+"':'"+items[i].get(arrayModel[j])+"'," ;
				}
			}
			variable = variable.substring(0,variable.length-1) ;
			variable += "},"
			taskVariable += variable
		}
		if(taskVariable.length > 1) {
			taskVariable = taskVariable.substring(0,taskVariable.length-1)
		}
		taskVariable += "]";
		
		//改变json的正确格式
		taskVariable = taskVariable.replace(/"/g,"'") ;
		taskVariable = taskVariable.replace(/'true'/g,"true") ;
		taskVariable = taskVariable.replace(/'false'/g,"false") ;
		taskVariable = taskVariable.replace(/'undefined'/g,"undefined") ;
		var edit = new mxCellAttributeChange(cell,varName,taskVariable) ;
		model.execute(edit) ;
		return taskVariable ;
}



//标题面板
var titlePanel = new Ext.app.TitlePanel( {	caption : '环节属性设置',	border : false,	region : 'north'});
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	border : true,
	items : [formPanel]
}