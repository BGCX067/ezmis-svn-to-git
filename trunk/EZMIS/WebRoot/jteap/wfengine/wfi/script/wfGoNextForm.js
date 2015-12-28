//返回的JSON值
//[{default:true/false,taskType:Fork,taskName:Fork,actors:角色A},{default:true/false,taskType:Task,taskName:环节A,actors:xiao,userLoginName:xiao,processMode:..,processKind:...,isOneProcessActor:...,isChooseActor:...}]

var perms = window.dialogArguments.perms;	//下一步环节的权限信息
var ncList = perms.ncList;
var actorIdList = perms.actorIdList;
var actorNameList = perms.actorNameList;
var defaultNodeIdx = 0;	//默认路由索引
var ActorModel = Ext.data.Record.create([{name:'text'},{name:'value'}]) ;
var defaultRouter = []		//默认路由
var actorData 	= [];		//处理人集合
var username	= [];		//处理人名称集合	
var tasktype    = [];		//环节类型
var nodeArr = new Array();	//环节集合
var NODE_SUFFIX_DEFAULT = "[*]";
var NODE_SUFFIX_BACK = "[退回]";

/**
 * 删除节点名称的后缀
 */
function delNodeSuffix(value){
	value = value.replace(NODE_SUFFIX_DEFAULT,"");
	value = value.replace(NODE_SUFFIX_BACK,"");
	return value;
}


//标题面板
var titlePanel = new Ext.app.TitlePanel( {caption : '转到下一环节',border : true,region : 'north'});
//描述面板
var descPanel ={xtype:'panel',region:'east',border:false,bodyStyle:'padding-right:20',width:180,items:[
	{xtype : 'fieldset',title  : '描述信息',bodyStyle: 'padding:2 2 2 2',height : 260,items : [{	id : 'descript',	xtype : 'panel',	border : false}]	}
]}

for(var i = 0;i < ncList.length;i++){
	var nodeData = [];
	var obj = ncList[i];
	var nodeName = obj.name;
	//如果是默认环节,则在环节后加上退回后缀
	if(obj.defaultNode == true){
		defaultNodeIdx = i;
		nodeName = nodeName + NODE_SUFFIX_DEFAULT;
	}
	//如果因不同意而退回,则在环节后加上退回后缀
	if(obj.isBackNode == true){
		nodeName = nodeName + NODE_SUFFIX_BACK;
	}
	//环节名称
	nodeData.push(nodeName);
	//环节列表
	nodeArr.push(nodeData);
}

//下一步环节数据集
var nodeStore = new Ext.data.SimpleStore({data:nodeArr,fields:['value']});
var selNodes = new Ext.ux.Multiselect({width:'95%',height:80,store : nodeStore,displayField : 'value',legend : '下一环节',allowDup : true,copy:true,	allowTrash:true,appendOnly:false,isFormField: false})
//设置只能单选
selNodes.multiSelect = false;
selNodes.singleSelect = true;
//环节渲染的时候设置默认选中的行
selNodes.on("render",function(){
	setTimeout(function(){
		selNodes.selectByIdx(defaultNodeIdx);
	},100)
})
//当选择的环节改变是，对应改变描述信息和处理人信息
selNodes.on("change",onSelectNodeChange);


//var tempArr = actorIdList[defaultNodeIdx].split(',');
//var tempUsername = actorNameList[defaultNodeIdx].split(',');
//for(var j = 0;j < tempArr.length;j++){
//	var obj = [tempUsername[j],tempArr[j]];
//	actorData.push(obj);
//}
//var actorStore = new Ext.data.SimpleStore({data:actorData,fields:['text','value']});
//处理人选择组件
var selActors = new Ext.ux.Multiselect({width:'95%',height:160,	displayField : 'text',valueField : 'value',
	legend : '环节处理人',	allowDup : true,copy:true,	allowTrash:true,appendOnly:false,isFormField: false,
	tbar:[{text:" ＋ ",handler:addPerson,id:'btnAddPerson'},{text:" － ",id:'btnRemovePerson',handler:removePerson}]
})


/**
 * 添加处理人
 */
function addPerson(){
	var url = link2;
	var returnValue = showModule(url,true,800,600) ;
	var arrays = Ext.decode(returnValue) ;
	if(arrays==null) {
		return ;
	}
	
   	for(var i=0 ; i < arrays.length ; i++) {
   		var username = arrays[i].name;
   		var loginName = arrays[i].loginName;
   		
		var record = new ActorModel({
			text : username,
			value : loginName
		});
		
		selActors.view.store.add(record);
		selActors.view.refresh();
   	}
}

/**
 * 移除处理人
 */
function removePerson(){
	var selectionsArray = selActors.view.getSelectedIndexes();
	if(selectionsArray.length ==0) {
		return ;
	}
	//按选择索引从大到小排序
	selectionsArray.sort(function compare(a,b){return b-a;});
	for(var i=0 ; i<selectionsArray.length ; i++){
		var record = selActors.view.store.getAt(selectionsArray[i]);
		selActors.view.store.remove(record);
	}
	selActors.view.refresh();
}
	
	
	
var leftPanel = {
	xtype : 'panel',
	layout : 'form',
	region:'center',
	border : false,
	height : 280,
	bodyStyle : 'padding-left:20px',
	items:[
		selNodes,selActors
	]
}
	
	
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	buttons:[{text:"提交",handler:submit},{text:"取消",handler:function(){window.close();}}],
	items:[leftPanel]
}

var curIdx = 0;
/**
 * 当选择下一步节点改变是触发
 */
function onSelectNodeChange(vw, val, node, e){
	if(val == ""){
		selNodes.selectByIdx(defaultNodeIdx);
		return;
	}
	var value = delNodeSuffix(val);
	
	for(var i = 0;i < ncList.length;i++){
		if(ncList[i].name == value){
			curIdx = i;
			break;
		}
	}
	//如果是"Fork",就禁用处理人选择框	
	if(ncList[curIdx].nodeType == "Fork" || ncList[curIdx].nodeType == "Join" || ncList[curIdx].nodeType == "EndState"){
		//禁止选择人员
		selActors.disable();
		//禁用"添加人员","删除人员"按钮
		Ext.getCmp("btnAddPerson").disable();
		Ext.getCmp("btnRemovePerson").disable();
	}else if(ncList[curIdx].nodeType == "Task"){
		//启用选择人员
		selActors.enable();
		//显示描述信息
		showDetail();
	}
	
	//根据所选择的环节刷新当前环节的处理人列表
	var tempArr = actorIdList[curIdx].split(',');
	var tempUsername = actorNameList[curIdx].split(',');
	//移除所有处理人
	selActors.view.store.removeAll();
	//添加处理人
	for(var j = 0;j < tempArr.length;j++){
		var actorId = tempArr[j];
		var actorName = tempUsername[j];
		var record = new ActorModel({
			text : actorName ,
			value : actorId
		});
		selActors.view.store.add(record);
	}
	//刷新
	selActors.view.refresh();
//	showDetail();
}
/**
 * 显示描述信息
 */
function showDetail(){
	var processKind = "";			//single/multi
	var isOneProcessActor = "";		//是否必须指定一个处理人
	var processMode = "";			//1-串行处理  0-并行处理
	var isChooseActor = "";			//是否允许选择处理人
	
	if(ncList[curIdx].nodePermission.processKind == "single"){
		processKind = "单人处理";
	}
	else if(ncList[curIdx].nodePermission.processKind == "multi"){
		processKind = "多人处理";
		//多人处理时不能选择处理人
		selActors.disable();
	}
	isOneProcessActor = ncList[curIdx].nodePermission.isOneProcessActor?"是":"否";
	if(ncList[curIdx].nodePermission.processMode == "1"){
		processMode = "串行处理";
	}
	else if(ncList[curIdx].nodePermission.processMode == "0"){
		processMode = "并行处理";
	}
	isChooseActor = ncList[curIdx].nodePermission.isChooseActor?"是":"否";
	
	//如果允许自由选择处理人,则启用"添加处理人"和"移除处理人"按钮,否则禁用
	if(ncList[curIdx].nodePermission.isChooseActor){
		//启用
		Ext.getCmp("btnAddPerson").enable();
		Ext.getCmp("btnRemovePerson").enable();
	}
	else{
		//禁用
		Ext.getCmp("btnAddPerson").disable();
		Ext.getCmp("btnRemovePerson").disable();
	}
	
	//Ext.getDom('descript').innerHTML = "处理类别:" + processKind + "<br><br>" + "必须指定一个处理人:" + isOneProcessActor + "<br><br>" + "处理方式:" + processMode + "<br><br>" + "是否可以自由选择处理人:" + isChooseActor;
	//Ext.getCmp('descript').doLayout();
}

/**
 * 提交
 */
function submit(){
	var curNodeIdx = selNodes.view.getSelectedIndexes()[0];
	var actorArray = selActors.view.getSelectedIndexes();
	var nodePermission = ncList[curNodeIdx].nodePermission;
	if(nodePermission!=null){
		//如果"必须指定一个处理人"
		if(nodePermission.isOneProcessActor == true){
			if(actorArray.length == 0 || actorArray.length > 1){
				alert("必须指定一个处理人!");
				return;
			}
		}
	}

	var val = selNodes.getRawValue("value")[0];
	var nodeName = val.replace(NODE_SUFFIX_DEFAULT,"").replace(NODE_SUFFIX_BACK,"");
	
	var selNode = "";
	var actorValues = "";
	for(var i=0 ; i<actorArray.length ; i++){
		var record =selActors.view.store.getAt(actorArray[i]);
		actorValues += record.data.value + "|";
	}
	if(actorValues.length > 0){
		actorValues = actorValues.substr(0,actorValues.length - 1);
	}
	var selNode = ncList[curNodeIdx];
	//返回值
	var retVal = {
		"nodeName"	: 	nodeName ,				//节点名称
		"nodeType"	:	selNode.nodeType,		//节点类型 Task/Fock/Join
		"isBackNode":	selNode.isBackNode,		//是否是条件不满足而退回的节点
		"actorIds"	:	actorValues				//处理人编号集合
	};
	window.returnValue = retVal;
	window.close();
}
