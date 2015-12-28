//返回的JSON值
//[{default:true/false,taskType:Fork,taskName:Fork,actors:角色A},{default:true/false,taskType:Task,taskName:环节A,actors:xiao,userLoginName:xiao,processMode:..,processKind:...,isOneProcessActor:...,isChooseActor:...}]

var perms = window.dialogArguments.perms;	//下一步环节的权限信息
var ActorModel = Ext.data.Record.create([{name:'text'},{name:'value'}]) ;
var defaultRouter = []		//默认路由
var actorData 	= [];		//处理人集合
var username	= [];		//处理人名称集合	
var tasktype    = [];		//环节类型
var nodeArr = new Array();	//环节集合

//标题面板
var titlePanel = new Ext.app.TitlePanel( {caption : '转到下一步',border : true,region : 'north'});
//描述面板
var descPanel ={xtype:'panel',region:'east',border:false,bodyStyle:'padding-right:20',width:180,items:[
	{xtype : 'fieldset',title  : '描述信息',bodyStyle: 'padding:2 2 2 2',height : 260,items : [{	id : 'descript',	xtype : 'panel',	border : false}]	}
]}

for(var i = 0;i < perms.length;i++){
	var nodeData = [];
	var obj = perms[i];
	//默认路由
	defaultRouter.push(obj['default']);
	//环节名称
	nodeData.push(obj.taskName);
	//用户登录名
	actorData.push(obj.actors);
	//用户名称
	username.push(obj.username);
	//环节类型
	tasktype.push(obj.taskType);
	//环节列表
	nodeArr.push(nodeData);
}
//下一步环节数据集
var nodeStore = new Ext.data.SimpleStore({data:nodeArr,fields:['value']});
var selNodes = new Ext.ux.Multiselect({width:'95%',height:80,store : nodeStore,displayField : 'value',legend : '下一步',allowDup : true,copy:true,	allowTrash:true,appendOnly:false,isFormField: false})
//设置只能单选
selNodes.multiSelect = false;
selNodes.singleSelect = true;

//设置默认选中的环节索引
selNodes.index = -1;
for(var i = 0;i < defaultRouter.length;i++){
	if(defaultRouter[i] == true){
		selNodes.index = i;
		break;
	}
}
//如果没有默认选中的环节,则第一个环节默认选中
if(selNodes.index == -1){
	selNodes.index = 0;
}

//环节渲染的时候设置默认选中的行
selNodes.on("render",function(){
	setTimeout(function(){
		selNodes.selectByIdx(selNodes.index);
	},100)
})

//当选择的环节改变是，对应改变描述信息和处理人信息
selNodes.on("change",function(vw, val, node, e) {
	if(val == ""){
		selNodes.selectByIdx(selNodes.index);
		return;
	}
	for(var i = 0;i < perms.length;i++){
		if(perms[i].taskName == val){
			selNodes.index = i;
			break;
		}
	}
	//如果是"Fork",就禁用处理人选择框	
	if(tasktype[selNodes.index] == "Fork")	selActors.disable();
	//否则就启用处理人选择框	
	else if(tasktype[selNodes.index] == "Task")	selActors.enable();
	
	//根据所选择的环节刷新当前环节的处理人列表
	var tempArr = perms[selNodes.index].actors.split(',');
	var tempUsername = perms[selNodes.index].username.split(',');
	selActors.view.store.removeAll();
	for(var j = 0;j < tempArr.length;j++){
		var loginName = tempArr[j];
		var username = tempUsername[j];
		var record = new ActorModel({
			text : username ,
			value : loginName
		});
		selActors.view.store.add(record);
	}
	selActors.view.refresh();
	
	var processKind = "";			//single/multi
	var isOneProcessActor = "";		//是否必须指定一个处理人
	var processMode = "";			//1-串行处理  0-并行处理
	var isChooseActor = "";			//是否允许选择处理人
	
	if(perms[selNodes.index].processKind == "single"){
		processKind = "单人处理";
	}
	else if(perms[selNodes.index].processKind == "multi"){
		processKind = "多人处理";
	}
	isOneProcessActor = perms[selNodes.index].isOneProcessActor?"是":"否";
	if(perms[selNodes.index].processMode == "1"){
		processMode = "串行处理";
	}
	else if(perms[selNodes.index].processMode == "0"){
		processMode = "并行处理";
	}
	isChooseActor = perms[selNodes.index].isChooseActor?"是":"否";
	
	//如果允许自由选择处理人,则启用"添加处理人"和"移除处理人"按钮,否则禁用
	if(perms[selNodes.index].isChooseActor){
		//启用
		Ext.getCmp("btnAddPerson").enable();
		Ext.getCmp("btnRemovePerson").enable();
	}
	else{
		//禁用
		Ext.getCmp("btnAddPerson").disable();
		Ext.getCmp("btnRemovePerson").disable();
	}
	Ext.getDom('descript').innerHTML = "处理类别:" + processKind + "<br><br>" + "必须指定一个处理人:" + isOneProcessActor + "<br><br>" + "处理方式:" + processMode + "<br><br>" + "是否可以自由选择处理人:" + isChooseActor;
	Ext.getCmp('descript').doLayout();
})

var tempArr = perms[selNodes.index].actors.split(',');
var tempUsername = perms[selNodes.index].username.split(',');

for(var j = 0;j < tempArr.length;j++){
	var obj = [tempUsername[j],tempArr[j]];
	actorData.push(obj);
}

var actorStore = new Ext.data.SimpleStore({data:actorData,fields:['text','value']});
//处理人选择组件
var selActors = new Ext.ux.Multiselect({width:'95%',height:160,store : actorStore,	displayField : 'text',valueField : 'value',
	legend : '人员选择',	allowDup : true,copy:true,	allowTrash:true,appendOnly:false,isFormField: false,
	tbar:[{text:" ＋ ",handler:addPerson,id:'btnAddPerson'},{text:" － ",	handler:removePerson,id:'btnRemovePerson'}]
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
	bbar:["->",{text:"提交",handler:submit	},{text:"取消",handler:function(){window.close();}}],
	items:[leftPanel,descPanel]
}

/**
 * 提交
 */
function submit(){
	var nodesArray = selNodes.view.getSelectedIndexes();
	var actorArray = selActors.view.getSelectedIndexes();
	
	//必须选择一个环节
	if(nodesArray.length == 0 || nodesArray.length > 1){
		alert("必须指定一个环节!");
		return;
	}
	
	var node = "";
	if(nodesArray.length > 0){
		node = selNodes.view.store.getAt(nodesArray[0]).data.value;
	}
	var actorValues = "";
	for(var i=0 ; i<actorArray.length ; i++){
		var record =selActors.view.store.getAt(actorArray[i]);
		actorValues += record.data.value + "|";
	}
	if(actorValues.length > 0){
		actorValues = actorValues.substr(0,actorValues.length - 1);
	}
	
	var retVal = {"node": node ,"actors":actorValues};
	window.returnValue = retVal;
	window.close();
}
