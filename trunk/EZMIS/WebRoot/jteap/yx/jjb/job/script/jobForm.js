//当前接班时间
var nowJieBanSJ = lastJjbsj;
//当前接班班次
var nowJieBanBC = lastJiebanbc;

if(lastJiebanbc == "夜班"){
	nowJieBanBC = "白班";
}else if(lastJiebanbc == "白班"){
	nowJieBanBC = "中班";	
}else if(lastJiebanbc == "中班"){
	nowJieBanBC = "夜班";	
	nowJieBanSJ = getDataByDay(nowJieBanSJ,1);
}

var dict_zbzb = $dictList("zbzb");
//值班值别数据源
var storeZB = new Ext.data.Store({
	data: {rows:dict_zbzb},
	reader: new Ext.data.JsonReader( {
		root: 'rows',
		id: 'id'
	}, ['key', 'value', 'id'])
});

//交接人数据源
var storeJJR = new Ext.data.JsonStore({
	url : link3 + jjrParams,
	autoLoad : true,
	fields : ['id', 'userName']
});

///////////////////////////////////// 第一行 //////////////////////////////////////////////////

//交班日期
var lblJiaoBTime = new Ext.form.Label({
	id: 'lblJiaoBTime',
	renderTo: 'fnJiaoBTime',
	text: lastJjbsj
});
//交班班次
var lblJiaoBBanCi = new Ext.form.Label({
	id: 'lblJiaoBBanCi',
	renderTo: 'fnJiaoBBanCi',
	text: lastJiebanbc
});

//接班日期
var lblJieBTime = new Ext.form.Label({
	id: 'lblJieBTime',
	renderTo: 'fnJieBTime',
	text: nowJieBanSJ
});
//接班班次
var lblJieBBanCi = new Ext.form.Label({
	id: 'lblJieBBanCi',
	renderTo: 'fnJieBBanCi',
	text: nowJieBanBC
});

//交班值别
var comboJiaoBanZB = new Ext.form.ComboBox({
	id: 'comboJiaoBanZB',
	applyTo: 'txtJiaoBanZB',
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择交班值别',
	emptyText: '请选择交班值别',
	width: 120,
	forceSelection: true,
	editable: false,
	allowBlank: false,
	store: storeZB
});

//接班值别
var comboJieBanZB = new Ext.form.ComboBox({
	id: 'comboJieBanZB',
	applyTo: 'txtJieBanZB',
	valueField: "value",
	displayField: "value",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择接班值别',
	emptyText: '请选择接班值别',
	width: 120,
	forceSelection: true,
	editable: false,
	allowBlank: false,
	store: storeZB
});

///////////////////////////////////// 第二行 //////////////////////////////////////////////////	

//交班人
var comboJiaoBanRen = new Ext.form.ComboBox({
	id: 'comboJiaoBanRen',
	applyTo: 'txtJiaoBanRen',
	valueField: "id",
	displayField: "userName",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择交班人',
	emptyText: '请选择交班人',
	width: 100,
	forceSelection: true,
	editable: false,
	allowBlank: false,
	store: storeJJR,
	listeners: {
		focus: function(){
			initJjr();
		}
	}
});
if(gwlb == "值长"){
	//预备值长数据源
	var storeYbzz = new Ext.data.JsonStore({
		url : link3 + "?gwlb=" + encodeURIComponent("预备值长"),
		autoLoad : true,
		fields : ['id', 'userName']
	});
	var comboJiaoBanRen2 = new Ext.form.ComboBox({
		id: 'comboJiaoBanRen2',
		applyTo: 'txtJiaoBanRen2',
		valueField: "id",
		displayField: "userName",
		mode: 'local',
		triggerAction: 'all',
		blankText: '预备值长',
		emptyText: '预备值长',
		width: 100,
		forceSelection: true,
		editable: false,
		allowBlank: true,
		store: storeYbzz,
		listeners: {
			focus: function(){
				initJjr();
			}
		}
	});
	var comboJieBanRen2 = new Ext.form.ComboBox({
		id: 'comboJieBanRen2',
		applyTo: 'txtJieBanRen2',
		valueField: "id",
		displayField: "userName",
		mode: 'local',
		triggerAction: 'all',
		fieldLabel: '接班人',
		blankText: '预备值长',
		emptyText: '预备值长',
		width: 100,
		forceSelection: true,
		editable: false,
		allowBlank: true,
		store: storeYbzz
	});
}

//交班人密码
var passJiaoBanRen = new Ext.form.TextField({
	id: 'passJiaoBanRen',
	inputType: 'password',
	applyTo: 'txtJiaoBanPass',
	minLengthText: '最少需要4位字母数字组合',
	maxLengthText: '最长10位字母数字组合',
	blankText: '请输入交班人密码',
	width: 80,
	maxLength: 10,
	minLength: 4,
	allowBlank: false
});

//接班人
var comboJieBanRen = new Ext.form.ComboBox({
	id: 'comboJieBanRen',
	applyTo: 'txtJieBanRen',
	valueField: "id",
	displayField: "userName",
	mode: 'local',
	triggerAction: 'all',
	fieldLabel: '接班人',
	blankText: '请选择接班人',
	emptyText: '请选择接班人',
	width: 100,
	forceSelection: true,
	editable: false,
	allowBlank: false,
	store: storeJJR
});

//接班人密码
var passJieBanRen = new Ext.form.TextField({
	id: 'passJieBanRen',
	inputType: 'password',
	applyTo: 'txtJieBanPass',
	minLengthText: '最少需要4位字母数字组合',
	maxLengthText: '最长10位字母数字组合',
	blankText: '请输入接班人密码',
	width: 80,
	maxLength: 10,
	minLength: 4,
	allowBlank: false
});

//初始化交班值别
if(lastJiebanzb != ""){
	comboJiaoBanZB.setValue(lastJiebanzb);
}

//初始化接班人
function initJjr(){
	if(lastJiebanid != ""){
		var arrayJiebanrid = lastJiebanid.split(",");
		var arrayJiebanren = lastJiebanr.split(",");
		if(arrayJiebanrid.length == 2){
			comboJiaoBanRen.setValue(arrayJiebanrid[0]);
			comboJiaoBanRen2.setValue(arrayJiebanrid[1]);
			comboJiaoBanRen.setRawValue(arrayJiebanren[0]);
			comboJiaoBanRen2.setRawValue(arrayJiebanren[1]);
		}else{
			comboJiaoBanRen.setValue(arrayJiebanrid);
			comboJiaoBanRen.setRawValue(lastJiebanr);
		}
	}
}
initJjr();

//交班
function save(){
		
	/** 数据验证*/
	if(!comboJiaoBanZB.validate()){
		alert("请选择交班值别");
		comboJiaoBanZB.focus();
		return;
	}
	if(!comboJiaoBanRen.validate()){
		alert("请选择交班人");
		comboJiaoBanRen.focus();		
		return;
	}
	if(!passJiaoBanRen.validate()){
		alert("请输入正确的交班人密码");
		passJiaoBanRen.setValue("");
		passJiaoBanRen.focus();		
		return;
	}
	if(!comboJieBanZB.validate()){
		alert("请选择接班值别");
		comboJieBanZB.focus();
		return;
	}
	if(!comboJieBanRen.validate()){
		alert("请选择接班人");
		comboJieBanRen.focus();		
		return;
	}
	if(!passJieBanRen.validate()){
		alert("请输入正确的接班人密码");
		passJieBanRen.setValue("");
		passJieBanRen.focus();
		return;
	}
	
	/** 如果当前时间不在 当前班次交接班时间合法范围  则弹出 confirm 
	 * "当前交班时间未到,是否继续交接班?"  
	 * 只有中班才能在交班时间大于当前时间1天时交班 如2010-09-09的夜班 可以在14:00~00:00 交班到2010-09-10的夜班
	 */
	var nowJieBanData = parseDate(nowJieBanSJ);
	if(nowJieBanData < nowDate){
		if(!window.confirm('当前交班时间已过,是否继续交接班?')){
			return;
		}
	}else if(nowJieBanData > nowDate){
		//只有中班才能在交班时间大于当前时间1天时交班 如2010-09-09的夜班 可以在14:00~00:00 交班到2010-09-10的夜班
		var dayOyNowData = nowDate.getDayOfYear();
		var dayOyNowJieData = nowJieBanData.getDayOfYear();
		
		if(dayOyNowJieData-dayOyNowData > 1 && lastJiebanbc != "中班"){
			alert('交班时间未到! 当前时间为:'+nowDate.format("Y-m-d H:i:s"));
			return;
		}
	}
	
	//预备值长
	var jiaobanYz = "";
	var jiebanYz = "";
	if("值长" == gwlb){
		jiaobanYz = comboJiaoBanRen2.getValue();
		jiebanYz = comboJieBanRen2.getValue();
	}
	
	Ext.Ajax.request({
		url: link2,
		method: 'post',
		params: { jiaobanbc:lastJiebanbc, jiaobanzb:comboJiaoBanZB.getValue(),
				  jiaobanr:comboJiaoBanRen.getValue(), jiebanbc:nowJieBanBC, 
				  jiebanzb:comboJieBanZB.getValue(), jiebanr:comboJieBanRen.getValue(),
				  jjbsj: nowJieBanSJ,jiaobrpass: passJiaoBanRen.getValue(),
				  jiebrpass: passJieBanRen.getValue(),gwlb:gwlb,
				  jiaobanYz:jiaobanYz,jiebanYz:jiebanYz},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success){
				if(responseObj.jiaobrpass == false){
					alert('交班人密码错误');
					passJiaoBanRen.setValue("");
					passJiaoBanRen.focus();
					return;
				}
				if(responseObj.jiebrpass == false){
					alert('接班人密码错误');
					passJieBanRen.setValue("");
					passJieBanRen.focus();
					return;
				}
				
				if(responseObj.isright == 'yes'){
					alert('交班成功');
					window.location.reload();
				}else if(responseObj.isright == 'no'){
					alert('该班次已完成交接班,不能重复交接班');
				}
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
	
}

//添加交班运行方式
var addJbyxfs = function(url,docid,jzHao){
	/** 逻辑判断最后接班班次、时间 是否为当前时间、班次,如否 不能添加记录 */
//	if(nowYmd != lastJjbsj || nowBc != lastJiebanbc){
//		alert("【"+gwlb+"】最后接班时间、班次为：【"+lastJjbsj+" "+lastJiebanbc+"】  与当前时间、班次不匹配,请核对交接班!");
//		return;
//	}
	
	url += "&lastJjbsj="+lastJjbsj+"&lastJieBanbc="+encodeURIComponent(lastJiebanbc)+
				"&zbzb="+encodeURIComponent(lastJiebanzb)+"&firstJjbsj="+firstJjbsj+"&firstJieBanbc="+encodeURIComponent(firstJiebanbc);
	if(jzHao != "" && jzHao != null){
		url += "&jizhangtype="+jzHao;		
	}
				
	var myTitle = "添加交班运行方式";
	if(docid != ""){
		url += "&docid=" + docid;
		myTitle = "修改交班运行方式";
	}
	
    showIFModule(CONTEXT_PATH+url,myTitle,"true",800,600,{});
    window.location.reload();
}
