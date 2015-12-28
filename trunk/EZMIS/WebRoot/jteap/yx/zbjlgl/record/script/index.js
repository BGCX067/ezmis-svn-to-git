//审阅人
var shenYueRen = "";
//记录类别
var recordType = "";
//内容颜色
var nrcolorType = "黑色";

//值长切换按钮
var btnAll = new Ext.Button({
	id: 'btnAll',
	text: "<font style='font-weight:bold;'>全部记录</font>",
	handler: function(){
		btnAll.setText("<font style='font-weight:bold;'>全部记录</font>");
		btnYb.setText("<font style='font-weight:bold;color: gray;'>一般记录</font>");
		btnDd.setText("<font style='font-weight:bold;color: gray;'>调度指令</font>");
		recordType = "";
		changeRightGrid(recordType);
	}
})
var btnYb = new Ext.Button({
	id: 'btnYb',
	text: "<font style='font-weight:bold;color: gray;'>一般记录</font>",
	handler: function(){
		btnYb.setText("<font style='font-weight:bold;'>一般记录</font>");
		btnAll.setText("<font style='font-weight:bold;color: gray;'>全部记录</font>");
		btnDd.setText("<font style='font-weight:bold;color: gray;'>调度指令</font>");
		recordType = "一般记录";
		changeRightGrid(recordType);
	}
})
var btnDd = new Ext.Button({
	id: 'btnDd',
	text: "<font style='font-weight:bold;color: gray;'>调度指令</font>",
	handler: function(){
		btnDd.setText("<font style='font-weight:bold;'>调度指令</font>");
		btnAll.setText("<font style='font-weight:bold;color: gray;'>全部记录</font>");
		btnYb.setText("<font style='font-weight:bold;color: gray;'>一般记录</font>");
		recordType = "调度指令";
		changeRightGrid(recordType);
	}
})

//零米切换按钮
var btnAllLm = new Ext.Button({
	id: 'btnAllLm',
	text: "<font style='font-weight:bold;'>全部记录</font>",
	handler: function(){
		btnAllLm.setText("<font style='font-weight:bold;'>全部记录</font>");
		btnJcl.setText("<font style='font-weight:bold;color: gray;'>精处理及炉内</font>");
		btnCh.setText("<font style='font-weight:bold;color: gray;'>除灰记录</font>");
		btnTl.setText("<font style='font-weight:bold;color: gray;'>脱硫记录</font>");
		recordType = "";
		changeRightGrid(recordType);
	}
})
var btnJcl = new Ext.Button({
	id: 'btnJcl',
	text: "<font style='font-weight:bold;color: gray;'>精处理及炉内</font>",
	handler: function(){
		btnJcl.setText("<font style='font-weight:bold;'>精处理及炉内</font>");
		btnAllLm.setText("<font style='font-weight:bold;color: gray;'>全部记录</font>");
		btnCh.setText("<font style='font-weight:bold;color: gray;'>除灰记录</font>");
		btnTl.setText("<font style='font-weight:bold;color: gray;'>脱硫记录</font>");
		recordType = "精处理及炉内";
		changeRightGrid(recordType);
	}
})
var btnCh = new Ext.Button({
	id: 'btnCh',
	text: "<font style='font-weight:bold;color: gray;'>除灰记录</font>",
	handler: function(){
		btnCh.setText("<font style='font-weight:bold;'>除灰记录</font>");
		btnAllLm.setText("<font style='font-weight:bold;color: gray;'>全部记录</font>");
		btnJcl.setText("<font style='font-weight:bold;color: gray;'>精处理及炉内</font>");
		btnTl.setText("<font style='font-weight:bold;color: gray;'>脱硫记录</font>");
		recordType = "除灰记录";
		changeRightGrid(recordType);
	}
})
var btnTl = new Ext.Button({
	id: 'btnTl',
	text: "<font style='font-weight:bold;color: gray;'>脱硫记录</font>",
	handler: function(){
		btnTl.setText("<font style='font-weight:bold;'>脱硫记录</font>");
		btnAllLm.setText("<font style='font-weight:bold;color: gray;'>全部记录</font>");
		btnJcl.setText("<font style='font-weight:bold;color: gray;'>精处理及炉内</font>");
		btnCh.setText("<font style='font-weight:bold;color: gray;'>除灰记录</font>");
		recordType = "脱硫记录";
		changeRightGrid(recordType);
	}
})

/**
 * 值班记事Grid 重新加入条件查询
 */
function changeRightGrid(recordType){
	//记录类别、岗位类别、页大小、值班时间、值班班次
	var url = link1+"?limit=14&zbsj="+dateZbsj.getValue().format("Y-m-d")+
				"&zbbc="+encodeURIComponent(comboZbbc.getValue())+"&jllb="+
				encodeURIComponent("记事")+"&gwlb="+encodeURIComponent(gwlb);
	if(recordType != null && recordType != ""){	
		url += "&zzjlType="+encodeURIComponent(recordType);
	}else{
		if(gwlb == "值长"){
			btnAll.setText("<font style='font-weight:bold;'>全部记录</font>");
			btnYb.setText("<font style='font-weight:bold;color: gray;'>一般记录</font>");
			btnDd.setText("<font style='font-weight:bold;color: gray;'>调度指令</font>");
		}else if(gwlb == "零米"){
			btnAllLm.setText("<font style='font-weight:bold;'>全部记录</font>");
			btnJcl.setText("<font style='font-weight:bold;color: gray;'>精处理及炉内</font>");
			btnCh.setText("<font style='font-weight:bold;color: gray;'>除灰记录</font>");
			btnTl.setText("<font style='font-weight:bold;color: gray;'>脱硫记录</font>");
		}
	}
 	rightGrid.changeToListDS(url);
 	rightGrid.getStore().reload();
 	
 	getShenYueRen();
 	var txtShenYueRen = mainToolbar.items.get('txtShenYueRen');
 	txtShenYueRen.setValue(shenYueRen);
}

function getShenYueRen(){
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("post", link9 + "?zbsj=" + dateZbsj.getValue().format("Y-m-d") + "&zbbc=" + encodeURIComponent(comboZbbc.getValue()) + "&gwlb=" + encodeURIComponent(gwlb),false); 
	conn.send(null); 
	var responseObject = Ext.util.JSON.decode(conn.responseText);
	if(responseObject.success == true){
		shenYueRen = responseObject.jlr;
	}
}

//值班时间
var dateZbsj = new Ext.form.DateField({
	id: 'dateZbsj',
	format: 'Y-m-d',
	//默认显示 最后一个交接班日期
	value: parseDate(lastJjbsj),
	listeners: {
		 change: function(){
			//所选值班时间
			var choiceTime = dateZbsj.getValue().format("Y-m-d");
			
			if(choiceTime < firstJjbsj){
				alert(firstJjbsj + " " + firstJieBanbc + " 之前没有交接班记录");
				dateZbsj.setValue(firstJjbsj);
				comboZbbc.setValue(firstJieBanbc);
			}else if(choiceTime > lastJjbsj){
				alert(lastJjbsj + " " + lastJieBanbc + " 之后没有交接班记录");
				dateZbsj.setValue(lastJjbsj);
				comboZbbc.setValue(lastJieBanbc);
			}
		 	changeRightGrid();
		 }
	}
});

var dict_zbbc = $dictList("zbbc");
//值班班次数据源
var storeBC = new Ext.data.Store({
	data: {rows:dict_zbbc},
	reader: new Ext.data.JsonReader({
		root: 'rows',
		id: 'id'
	},['key','value','id'])
});

//值班班次
var comboZbbc = new Ext.form.ComboBox({
	id: 'comboZbbc',
	value: lastJieBanbc,
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选值班班次',
	emptyText: '请选值班班次',
	width: 60,
	forceSelection: true,
	editable: false,
	allowBlank: true,
	store: storeBC,
	listeners: {
		select: function(){
			changeRightGrid();
		}
	}
});

//工具栏  
var mainToolbar = new Ext.Toolbar({height:26,listeners:{render:function(tb){operationsToolbarInitialize(tb);}}
});

/**
 * 初始化操作工具栏
 */
var operationsToolbarInitialize = function(toolbar) {
	// ops来自<jteap:operations>标签，如果定义了该标签，标签内部将会定义该变量，用于存放所有具有权限的操作的json对象
	if (ops && ops.length > 0) {
		for (var i = 0;i < ops.length; i++) {
			if(i==0){
				toolbar.add('值班时间:');
				toolbar.add(dateZbsj);
				toolbar.add('值班班次:');
				toolbar.add(comboZbbc);
				toolbar.add('-');
			}
			var op = ops[i];
			if (op.sn == "->" || op.sn == "-" || op.sn == "separator"
					|| op.sn == " ") {
				toolbar.add(op.sn);
				continue;
			}
			var btConfig = {
				id : op.sn,
				cls : 'x-btn-text-icon',
				icon : (op.icon && op.icon != '') ? op.icon : "icon/icon_1.gif"
			};
			if (op.tip && op.tip != '')
				btConfig.tooltip = op.tip;
			btConfig.text = ((op.showText) ? op.resName : "");
			var opBt = toolbar.addButton(btConfig);
			opBt.on("click", mainToolBarButtonClick);
			
			if(i == ops.length-1){
				toolbar.add(new Ext.form.TextField({
											id:'txtShenYueRen',
											value:shenYueRen,
											style:'color:red;font-weight: bold;',
											width:60,
											readOnly: true})
								);	
				if(shenYueRen != ""){
					mainToolbar.items.get('btnLingDaoSY').setDisabled(true);
				}
			}
			// 是否需要初始化工具栏按钮的状态，在各自模块的js文件中定义initToolbarButtonStatus(Ext.Toolbar.Button)函数
			if (initToolbarButtonStatus) {
				initToolbarButtonStatus(opBt);
			}
		}
	}
}
getShenYueRen();

/**
 * 初始化工具栏按钮状态
 */
function initToolbarButtonStatus(button){
	if(button.id=='btnModiRecord' || button.id=='btnDelRecord'|| button.id =='btnLockRecord') 
		button.setDisabled(true);
}

/**
 * 上一班
 */
function btnShangYB_Click(){
	//上一班时间
	var leftTime = getDataByDay(dateZbsj.getValue(),-1);
	//所选值班时间
	var choiceTime = dateZbsj.getValue().format("Y-m-d"); 
	
	if(choiceTime == firstJjbsj){
		//判断 第一个交接班的 接班班次是否 == 页面所选的班次
		if(firstJieBanbc == comboZbbc.getValue()){
			alert(firstJjbsj + " " + firstJieBanbc + " 之前没有交接班记录");
			return;
		}else{
			if(comboZbbc.getValue() == '夜班'){
				alert(firstJjbsj + " " + firstJieBanbc + " 之前没有交接班记录");
				return;
			}else if(comboZbbc.getValue() == '白班'){
				comboZbbc.setValue('夜班');
			}else if(comboZbbc.getValue() == '中班'){
				comboZbbc.setValue('白班');
			}
		}
	}else if(choiceTime < firstJjbsj){
		alert(firstJjbsj + " " + firstJieBanbc + " 之前没有交接班记录");
		return;
	}else{
		if(comboZbbc.getValue() == '夜班'){
			comboZbbc.setValue('中班');
			dateZbsj.setValue(leftTime);
		}else if(comboZbbc.getValue() == '白班'){
			comboZbbc.setValue('夜班');
		}else if(comboZbbc.getValue() == '中班'){
			comboZbbc.setValue('白班');
		}
	}
	changeRightGrid();
}

/**
 * 下一班
 */
function btnXiaYB_Click(){
	//下一班时间
	var rightTime = getDataByDay(dateZbsj.getValue(),1);
	//所选值班时间
	var choiceTime = dateZbsj.getValue().format("Y-m-d"); 
	
	if(choiceTime == lastJjbsj){
		//判断 第一个交接班的 接班班次是否 == 页面所选的班次
		if(lastJieBanbc == comboZbbc.getValue()){
			alert(lastJjbsj + " " + lastJieBanbc + " 之后没有交接班记录");
			return;
		}else{
			if(comboZbbc.getValue() == '夜班'){
				comboZbbc.setValue('白班');
			}else if(comboZbbc.getValue() == '白班'){
				comboZbbc.setValue('中班');
			}else if(comboZbbc.getValue() == '中班'){
				alert(lastJjbsj + " " + lastJieBanbc + " 之后没有交接班记录");
				return;
			}
		}
	}else if(choiceTime > lastJjbsj){
		alert(lastJjbsj + " " + lastJieBanbc + " 之后没有交接班记录");
		return;
	}else{
		if(comboZbbc.getValue() == '夜班'){
			comboZbbc.setValue('白班');
		}else if(comboZbbc.getValue() == '白班'){
			comboZbbc.setValue('中班');
		}else if(comboZbbc.getValue() == '中班'){
			comboZbbc.setValue('夜班');
			dateZbsj.setValue(rightTime);
		}
	}
	changeRightGrid();
}

/**
 * 添加记录
 */
function btnAddRecord_Click(){
	/** 逻辑判断最后接班班次、时间 是否为当前时间、班次,如否 不能添加记录 */
//	if(nowYmd != lastJjbsj || nowBc != lastJieBanbc){
//		alert("【"+gwlbStr+"】最后接班时间、班次为：【"+lastJjbsj+" "+lastJieBanbc+"】  与当前时间、班次不匹配,请核对交接班!");
//		return;
//	}
	
	if(lastJjbsj != dateZbsj.getValue().format("Y-m-d") || lastJieBanbc != comboZbbc.getValue()){
		alert("只能对当前班次("+lastJjbsj+" "+lastJieBanbc+")进行添加操作");
		return;
	}
	
	rightGrid.addPlant();
}

/**
 * 删除记录
 */
function btnDelRecord_Click(){
		if(lastJjbsj != dateZbsj.getValue().format("Y-m-d") || lastJieBanbc != comboZbbc.getValue()){
			alert("只能对当前班次("+lastJjbsj+" "+lastJieBanbc+")进行删除操作");
			return;
		}
	if(gwlb == "值长"){
 		flag = 'del';
  		var lockForm = new LockFormWindow();
		lockForm.show();
	}else{
		DelRecord_Click();
	}
}
/**
 * 删除记录
 */
function DelRecord_Click(){
	var select=rightGrid.getSelectionModel().getSelections();
   	rightGrid.deleteSelect(select);
}
//获取接班人登录名
function setUserloginName(){
	Ext.Ajax.request({
        		url:link12,
        		params: {gwlb:gwlb},
        		method:'post',
        		success:function(ajax){
        			var responseObj = Ext.util.JSON.decode(ajax.responseText);
        			if(responseObj.success == true){
        				username = responseObj.username;
					}
        		},
        		failure:function(){
        			alert('服务器忙，请稍候操作...');
        			//simpleForm.buttons[0].disable();
        		}
    		});
}

function addJbyxfs(myFormSn){
	/** 逻辑判断最后接班班次、时间 是否为当前时间、班次,如否 不能添加记录 */
//	if(lastJjbsj != dateZbsj.getValue().format("Y-m-d") || lastJieBanbc != comboZbbc.getValue()){
//		alert("【"+gwlbStr+"】最后接班时间、班次为：【"+lastJjbsj+" "+lastJieBanbc+"】  与当前时间、班次不匹配,请核对交接班!");
//		return;
//	}
	
	var url = CONTEXT_PATH+"/jteap/form/eform/eformRec.jsp?formSn="+myFormSn+"&lastJjbsj="+lastJjbsj+"&lastJieBanbc="+encodeURIComponent(lastJieBanbc)+
				"&zbzb="+encodeURIComponent(zbzb)+"&firstJjbsj="+firstJjbsj+"&firstJieBanbc="+encodeURIComponent(firstJieBanbc);
	var mylink = link10+"?formSn="+myFormSn+"&zbsj="+lastJjbsj+"&zbbc="+encodeURIComponent(lastJieBanbc);
	
	if(jizhangtype != ""){
		url += "&jizhangtype="+jizhangtype;
		mylink += "&jizhangtype="+jizhangtype;
	}
	var myTitle = "添加"+gwlbStr+"交班运行方式";
	var id = null;
	
	//根据时间、班次查询是否存在该条记录,存在就进行弹出修改窗口
	var conn = Ext.lib.Ajax.getConnectionObject().conn; 
	conn.open("post", mylink,false); 
	conn.send(null); 
	var responseObject = Ext.util.JSON.decode(conn.responseText);
	if(responseObject.success == true){
		id = responseObject.id;
		if(id != ""){
			url += "&docid=" + id;
			myTitle = "修改"+gwlbStr+"交班运行方式";
		}
	}
		
	var fw = new $FW({
		url:url,
		width:750,
		height:582,			
		id:id,							//id,相同id的窗口只会打开一个,没设置id时该值为Date().getTime()
		type:"T1",						//窗口类型  T1  T2  T3  分别代表普通窗口、模式对话框、非模式对话框
		title: myTitle,					//标题
		status: false,					//状态栏
		toolbar:false,					//工具栏
		scrollbars:false,				//滚动条
		menubar:false,					//菜单栏
		userIF:false,					//是否采用Iframe套框,为解决模式窗口无法刷新的问题
		resizable:false,				//是否支持调整大小
		callback:function(retValue){	//回调函数
			
		}
	});
	fw.show();
}
//锁定
function btnLockRecord_Click(){
	var btnLockRecord=mainToolbar.items.get('btnLockRecord');
	var btnAddRecord=mainToolbar.items.get('btnAddRecord');
	if(btnLockRecord){
		var text = btnLockRecord.getText();
		if(text=="锁定"){
			if(btnAddRecord){
				btnAddRecord.setDisabled(true);
			}
			btnLockRecord.setText("解锁");
		}
		if(text=="解锁"){
			unlockRecord_Click();
		}
	}
}
//解锁
function unlockRecord_Click(){
	var lockForm = new LockFormWindow();
	lockForm.show();
}
//添加运行方式
function btnAddJbyxfs_Click(){
	addJbyxfs(formSn);
}

//脱硫运行方式
function btnTlJbyxfs_Click(){
	addJbyxfs("TB_YX_FORM_TLJBYXFS");
}

//精处理运行方式
function btnJclJbyxfs_Click(){
	addJbyxfs("TB_YX_FORM_JCLJLNJBYXFS");
}

/**
 * 定期工作
 */
function btnDiQiGZ_Click(){
	var obj = {};
	var url = contextPath + '/jteap/yx/dqgzgl/handle/quickHandle.jsp';
	showIFModule(url,"定期工作处理","true",950,500,obj);
}

/**
 * 领导审阅
 */
function btnLingDaoSY_Click(){
	var shenYueForm = new ShenYueFormWindow(dateZbsj.getValue().format("Y-m-d"), comboZbbc.getValue(),gwlbStr);
	shenYueForm.show();
}

var rightGrid=new RightGrid();
rightGrid.getStore().reload();

//保存方法
function saveRecode(){
//		if(lastJjbsj != dateZbsj.getValue().format("Y-m-d") || lastJieBanbc != comboZbbc.getValue()){
//			alert("只能对当前班次("+lastJjbsj+" "+lastJieBanbc+")进行保存操作");
//			return;
//		}
		//锁定功能
	//	var btnLockRecord = mainToolbar.items.get('btnLockRecord');
	//	if(btnLockRecord){
	//		if(btnLockRecord.getText()=="解锁"){
	//			alert("您没有修改权限！");
	//			return;
	//		}
	//	} 
		
		
		//获得被修改的记录
		var modiRecords = rightGrid.getStore().getModifiedRecords();
		var records = [];
		
		var infoJson = {};
		//岗位
		infoJson.gwlb = gwlb;
		//记录类别： 记事  通知  审阅
		infoJson.jllb = '记事';
		//值班时间
		infoJson.zbsj = dateZbsj.getValue().format("Y-m-d H:i:s");
		//值班班次
		infoJson.zbbc = comboZbbc.getValue();
		//值班值别
		infoJson.zbzb = zbzb;
		
		records.push(infoJson);
		 
		for(var i=0; i<modiRecords.length; i++){
			var subRecord = {};
			//id
			subRecord.id = modiRecords[i].data.id;
			//机组号
			subRecord.jzh = modiRecords[i].data.jzh;
			var tempsj = modiRecords[i].data.jlsj;
			if(tempsj["time"] != null){
				tempsj = tempsj["time"];
			}
			//记录时间
			subRecord.jlsj = new Date(tempsj).format("Y-m-d H:i:s");
			//记录内容
			subRecord.jlnr = modiRecords[i].data.jlnr;
			//记录人
			subRecord.jlr = modiRecords[i].data.jlr;
			//专业
			subRecord.zy = modiRecords[i].data.zy;
			if(modiRecords[i].data.zzjlType==""){
				subRecord.zzjlType ="一般记录";
			}else{
				//值长、零米记录类型
				subRecord.zzjlType = modiRecords[i].data.zzjlType;
			}
			//内容颜色
			subRecord.nrcolor = modiRecords[i].data.nrcolor;
			
			if(subRecord.zzjlType==''){
				alert("请选择记录类型");
				return;
			}
			if(subRecord.nrcolor==''){
				alert("请选择内容颜色");
				return ;
			}
			records.push(subRecord);
		}
		
		if(records.length == 1){
			alert('无新增或修改的值班记事');
			return;
		}else if(records.length > 1000){
			alert('记录内容应少于500个汉字');
			return;
		}
		
		
		var json = Ext.util.JSON.encode(records);
//		alert(json.toString());
		
		Ext.Ajax.request({
			url: link2,
			method: 'post',
			params: {json: json},
			success: function(ajax){
				eval("responseObj="+ajax.responseText);
				if(responseObj.success == true){
					alert('保存成功');
					rightGrid.getStore().reload();
				}else{
					alert('保存失败');
				}
			},
			failure: function(){
				alert('服务器忙,请稍后再试...');
			}
		})
	}
//通知工具条
var tzTool = new Ext.Toolbar({
	height:26,
	items:["<font style='font-size:14px;'>值班通知</font>","->",
			{id:'btnAddTz',text:'添加通知',cls: 'x-btn-text-icon',icon:'icon/icon_1.gif',
				listeners: {
					click: function(){
					//	if(!isRoot && curPersonRoles.indexOf("值班通知") == -1 && curPersonRoles.indexOf("系统管理员") == -1){
					//		alert('您不具有该权限,请联系统管理员!');
					//		return;
					//	}		
 						flag = 'tzadd';
 						flagUser = '1';
  						var lockForm = new LockFormWindow();
						lockForm.show();
						
					}
				} 
			},
			{id:'btnModiTz',text:'修改通知',cls: 'x-btn-text-icon',icon:'icon/icon_3.gif',disabled:true,
				listeners: {
					click: function(){
						flag = 'tzmod';
						flagUser = '1';
  						var lockForm = new LockFormWindow();
						lockForm.show();
					}
				}
			},
			{id:'btnDelTz',text:'删除通知',cls: 'x-btn-text-icon',icon:'icon/icon_2.gif',disabled:true,
				listeners: {
					click: function(){
						flag = 'tzdel';
						flagUser = '1';
  						var lockForm = new LockFormWindow();
						lockForm.show();
					}
				} 
			}]
});
//添加通知
function tzAdd(){
	var obj = {};
	obj.lastJjbsj = lastJjbsj;
	obj.gwlb = superGwlb;
	obj.userName = curPersonName;
	if(curPersonName==""){
		alert("请联系管理员");
		return;
	}
	var url = contextPath + '/jteap/yx/zbjlgl/record/tongZhi.jsp';
	showIFModule(url,"添加通知","true",500,267,obj);
	tzGrid.getStore().reload();
}
//修改通知
function tzMod(){
	var select = tzGrid.getSelectionModel().getSelections()[0];
	var id = select.get("id");
	var obj = {};
							
	var url = contextPath + '/jteap/yx/zbjlgl/record/tongZhi.jsp?id='+id;
	showIFModule(url,"修改通知","true",500,267,obj);
	tzGrid.getStore().reload();
}
//删除通知
function tzDel(){
	var select = tzGrid.getSelectionModel().getSelections();
	tzGrid.deleteSelect(select);
}
var tzGrid = new TzGrid();
tzGrid.getStore().load();

var gwlbStr = gwlb;
if(gwlb == "1"){
	gwlbStr = "#1机长";
}else if(gwlb == "2"){
	gwlbStr = "#2机长";
}else if(gwlb == "3"){
	gwlbStr = "#3机长";
}else if(gwlb == "4"){
	gwlbStr = "#4机长";
}

var myTitle =  "<font style='font-weight:bold;color: red;'>"+gwlbStr+"</font> 值班记事列表";
if(zbzb != ""){
	myTitle += 	"　当班值次: <font style='font-weight:bold;color: red;'>"+zbzb+"</font>";
}
			
//中间
var lyCenter={
	layout:'border',
	id:'center-panel',
	region:'center',
	title: myTitle,
	minSize: 175,
	maxSize: 400,
	border:false,
	margins:'0 0 0 0',
	items:[rightGrid]
}

//南边
var lySouth={
	id:'south-panel',
	region:'south',
	height:200,
	border:false,
	items: [tzTool,tzGrid]
}
