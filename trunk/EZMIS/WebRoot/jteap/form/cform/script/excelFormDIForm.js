var pWin = window.dialogArguments.opener;			//
var row = window.dialogArguments.row;				//单元格行号
var col = window.dialogArguments.col;				//单元格列号
var caption = window.dialogArguments.caption;		//字段描述名称
var defTableId = window.dialogArguments.defTableId;	//关联表ID
var title = window.dialogArguments.title;			//表单标题

var comment = window.dialogArguments.comment;		//单元格上已经存在的批注

var ComboRecord=Ext.data.Record.create([{id:'id', mapping:'id'},{name:'name',mapping:'name'},{name:'value',mapping:'value'}]);

//普通字符串01、日期域02、签名日期域03、意见签名域04、不用验证签名域05、需要验证签名域06、数字07 计算域08
//隐藏域表明不需要用户输入，用户不可编辑，自动计算型,可用于那些隐藏型字段，配合以公式进行计算
var dataType=[	['普通字符串','01'],
				['日期域','02'],
				//['签名日期域','03'],
				['意见签名域','04'],
				['不用验证签名域','05'],
				['需要验证签名域','06'],
				['数字域','07'],
				['计算域','08']
			];
			
//计算类型
var dataType2=[
				['创建时计算','01'],
				['显示时计算','02']
			];	
			
//yyyy-MM-dd hh:mm:ss
//yyyy年MM月dd日 hh时mm分ss秒
//yyyy-MM-dd
//yyyy年MM月dd日 
//hh:mm:ss
//hh时mm分ss秒
var formatType=[['yyyy-MM-dd HH:mm:ss','yyyy-MM-dd HH:mm:ss'],
				['yyyy年MM月dd日 HH时mm分ss秒','yyyy年MM月dd日 HH时mm分ss秒'],
				['yyyy-MM-dd','yyyy-MM-dd'],
				['yyyy年MM月dd日','yyyy年MM月dd日 '],
				['HH:mm:ss','HH:mm:ss'],
				['HH时mm分ss秒','HH时mm分ss秒']
			];
			
var titlePanel=new Ext.app.TitlePanel({caption:'数据项设置',border:false,region:'north'});

//单元格标识
var txtCell=new Ext.form.TextField({disabled:true,width:190,maxLength : 50,allowBlank :false,fieldLabel : "单元格标识",name : "txtCell",readOnly:true,value:'20:10'});
var infoCell=new Ext.app.LabelPanel('当前选中的单元格行列号  行号:列号');

//当前关联表
var txtTableName=new Ext.form.TextField({disabled:true,width:190,maxLength : 50,allowBlank :false,fieldLabel : "关联表",name : "txtDefTableName",readOnly:true,value:'TB_PERSON'});
var infoTableName=new Ext.app.LabelPanel('当前表单关联的数据表名称');

//字段名
var comboFDName=new Ext.form.ComboBox({triggerAction: 'all',editable :false,selectOnFocus :false,forceSelection :false,maxLength : 50,mode:'local',allowBlank :false,fieldLabel : "字段名",name : "comboFDName",displayField:'name',valueField:'value',width:90,
	store:new Ext.data.SimpleStore({fields:['name','value']}),
	listeners:{
		select: function(combo,record){
			comboFDCName.setValue(record.get("value"));
		}
	}
});
var infoFDName=new Ext.app.LabelPanel('选择当前单元格对应字段中文名');


//字段名中文名
var comboFDCName=new Ext.form.ComboBox({triggerAction: 'all',editable :false,selectOnFocus :false,forceSelection :false,maxLength : 50,mode:'local',allowBlank :false,fieldLabel : "字段中文名",name : "comboFDCName",displayField:'value',valueField:'name',width:190,
	store:new Ext.data.SimpleStore({fields:['name','value']}),
	listeners:{
		select: function(combo,record){
			comboFDName.setValue(record.get("name"));
		}
	}
});
var infoFDCName=new Ext.app.LabelPanel('字段中文名称');

//生成连续字段信息
var btnGenerateContinuousColumn = new Ext.Button({
	text: '生成连续',
	handler: function(){
		if(defTableId != ""){
			/** 表已存在 */
			showGCColumn();
		}else{
			Ext.Msg.confirm('操作提示','临时数据表 '+caption+' 未生成,是否建立?',
			function(btn,text){
				if(btn == 'yes'){
					/** 创建表 */
					createTable(showGCColumn);
				}});
		}
	}
});

/*
 * 连续字段
 */
function showGCColumn(){
	var gcColumnWindow = new GCColumnWindow(defTableId);
	gcColumnWindow.show();
	gcColumnWindow.center();
	/** 关闭时,回调函数 */
	gcColumnWindow.on('close',onload);
}

//增加或修改字段
var btnChoose = new Ext.Button({
	text: '..',
	handler: function(){
		if(defTableId != ""){
			/** 表已存在 */
			showEditColumn();
		}else{
			/** 表不存在 */
			Ext.Msg.confirm('操作提示','临时数据表 '+caption+' 未生成,是否建立?',
			function(btn,text){
				if(btn == 'yes'){
					/** 创建表 */
					createTable(showEditColumn);
				}});
		}
	}
});

/**
 * 编辑表字段
 */
function showEditColumn(){
	var columnCode = comboFDName.getValue();
	
	var editColumnWindow=new EditColumnWindow(defTableId,columnCode);
	editColumnWindow.show();
	editColumnWindow.center();
	if(columnCode != ""){
		/** 加载修改 字段信息 */
		editColumnWindow.loadData();
	}
	/** 关闭时,回调函数 */
	editColumnWindow.on('close',editCallBack,editColumnWindow);
}

/*
 * 创建表
 */
function createTable(DoMethod){
	/** 创建表 */
	Ext.Ajax.request({
		url: contextPath+"/jteap/form/dbdef/DefTableInfoAction!saveUpdateReturnIdAction.do",
		method: 'POST',
		params:{tableCode:caption, tableName:title},
		success: function(ajax){
			var temp = ajax.responseText;
			eval("responseObj="+temp);
			if(responseObj.success == true){	
				/** 获得表ID */
				defTableId = responseObj.id;
				/** 设值到父窗口对象 */
				pWin.$("defTableId").value = defTableId;
				alert("表 "+caption+" 已创建");
				DoMethod();
			}else{
				alert('服务器忙，请稍候操作...');
			}
		},
		failure: function(){
			Ext.Msg.alert("操作提示","服务器忙,请稍候操作...");
		}
	});
}
/**
 * 关闭编辑表字段, 回调函数.
 * @param {} editColumnWindow
 */
function editCallBack(editColumnWindow){
	/** 初始化 comboBox等页面控件 */
	onload();
	
	//字段名
	var columncode = $("columncode").value;
	//字段中文名
	var columnname = $("columnname").value;
	
	if(columncode != "" && columnname != ""){
		/** 显示字段信息 */
		comboFDName.setValue(columncode);
		comboFDCName.setValue(columnname);
	}
	
}

//数据类型
var comboType=new Ext.form.ComboBox({triggerAction: 'all',editable :false,selectOnFocus :false,forceSelection :true,maxLength : 50,mode:'local',allowBlank :false,fieldLabel : "数据类型",name : "comboType",displayField:'name',valueField:'value',width:190,listWidth:190,
	store:new Ext.data.SimpleStore({fields:['name','value'],data:dataType}),
	value: '01'
});


//数据类型
var comboType2=new Ext.form.ComboBox({triggerAction: 'all',disabled:true,editable :true,selectOnFocus :false,forceSelection :true,maxLength : 50,mode:'local',allowBlank :false,hideLabel :true,fieldLabel : "数据类型",name : "comboType",displayField:'name',valueField:'value',width:94,listWidth:100,
	store:new Ext.data.SimpleStore({fields:['name','value'],data:dataType2}),
	value:'01'
});
var infoType=new Ext.app.LabelPanel('数据项的数据类型');

//显示格式
var comboFormat=new Ext.form.ComboBox({triggerAction: 'all',editable :true,selectOnFocus :false,maxLength : 50,mode:'local',allowBlank :true,fieldLabel : "显示格式",name : "comboFormat",displayField:'name',valueField:'value',width:190,listWidth:200,
	store:new Ext.data.SimpleStore({fields:['name','value'],data:formatType})
});
var infoFormat=new Ext.app.LabelPanel('指定数据显示时的格式，可自由组合');

var radioSaveType=new Ext.form.RadioGroup({
                fieldLabel: '存储方式',
                items: [
                    {boxLabel: '覆盖', name: 'saveType', inputValue: '覆盖', checked: true},
                    {boxLabel: '追加', name: 'saveType', inputValue: '追加'}
                ]
            });
//var infoSaveType=new Ext.app.LabelPanel({label:'数据存入数据字段时的存储方式',height:25,width:190});

//是否允许键盘输入
var radioInput=new Ext.form.RadioGroup({
                fieldLabel: '键盘输入',
                items: [
                    {boxLabel: '允许', name: 'isInput', inputValue: '01', checked: true},
                    {boxLabel: '不允许', name: 'isInput', inputValue: '02'}
                ]
            });

//是否必填项
var radioMust=new Ext.form.RadioGroup({
                fieldLabel: '是否必填项',
                items: [
                    {boxLabel: '必填', name: 'isMust', inputValue: '01', checked: true},
                    {boxLabel: '选填', name: 'isMust', inputValue: '02'}
                ]
            });
var infoMust=new Ext.app.LabelPanel({label:'确定该字段在表单填写的时候是不是必须填写的项目',height:35});

//验证规则
var txtVLRule=new Ext.form.TextArea({maxLength : 50,allowBlank :true,fieldLabel : "验证规则",name : "txtVLRule",width:260	});
var infoVLRule=new Ext.app.LabelPanel({label:'如果数据类型是数字域，则可以以下方式进行验证：x&gt;n，x&lt;n，m&lt;x&lt;n(m,n为常数、x为用户输入的数据)，如果数据类型为字符串型，则比较的是字符串的长度',height:75});

//枚举值
var txtEnumValue=new Ext.form.TextArea({allowBlank :true,fieldLabel : "字段枚举值",name : "txtEnumValue",width:260});
var infoEnumValue=new Ext.app.LabelPanel({label:'单元格中可下拉的枚举值，以逗号分隔,例如：是,否,也可是SQL语句 格式为<br/>@SQL("SELECT value FROM tablename")',height:75});

//默认值
var txtDefValue=new Ext.form.TextArea({maxLength : 50,allowBlank :true,fieldLabel : "默认值",name : "txtDefValue",width:260});
var infoDefValue=new Ext.app.LabelPanel({label:'当表单创建是用于自动产生默认值,可以直接填写默认值',height:75});

var regex = /^('([^']*)'){1}([+|-]*('([^']*)'){1})*$/;
var regex2=/^@[a-zA-Z]*\((('([^']*)'){1}([+|-]*('([^']*)'){1}))*\)$/
var regex3=/^@[a-zA-Z]+\(.*\)$/

//显示时计算公式
var txtCalcFormula_Show=new Ext.form.TextArea({allowBlank :true,disabled:false,fieldLabel : "显示时计算公式",name : "txtCalcFormula_Show",width:390,value:""	});
var txtCalcFormula_Save=new Ext.form.TextArea({allowBlank :true,disabled:false,fieldLabel : "保存时计算公式",name : "txtCalcFormula_Save",width:390,value:""	});
var txtCalcFormula_Create=new Ext.form.TextArea({allowBlank :true,disabled:false,fieldLabel : "创建时计算公式",name : "txtCalcFormula_Create",width:390,value:""	});

var infoCalculateFormula=new Ext.app.LabelPanel({label:'<div style="margin-left:12px;">显示时计算：每次表单打开的时候进行计算<br/>保存时计算：每次保存该数据之前计算<br/>创建时计算：只有当文档在第一次创建的时候计算<br/>可使用的公式：@IF、@SYSDT、@HTTPSESSION、@SQL、@UUID、@PARSENUMBER&nbsp;<a href="cf_help.html" target="_blank">公式帮助</a></div>',height:70});

var txtEvent=new Ext.form.TextArea({allowBlank :true,disabled:false,name : "txtEvent",width:564,height:330,value:""	});

/**
 * 页面初始化
 */
function onload() {
	txtTableName.setValue(caption);
	txtCell.setValue(row+"行"+col+"列");
	
	/** 根据表ID,获取表 字段信息列表 */
	initDefColumnList();
	/** 初始化下拉菜单数据store */
	initFdCombo();
	/** 根据注解初始化控件值 */
	initValueWithComment();
	
	if(defTableId == ''){
		/** 新建表单时 */
		comboFDName.setDisabled(true);
		comboFDCName.setDisabled(true);
	}else{
		/** 表单建立后 */
		comboFDName.setDisabled(false);
		comboFDCName.setDisabled(false);
	}
}

/**
 * 根据注解初始化各个控件的初始值
 * <di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' />
 */
function initValueWithComment(){
	if(comment!=null && comment.length>0){
		try{
			var oXml=getDom(comment).firstChild;
			var cp=decodeChars(getXmlAttribute(oXml,'cp'),"',<,>,&,\"");		comboFDCName.setValue(cp);
			var fd=decodeChars(getXmlAttribute(oXml,'fd'),"',<,>,&,\"");		comboFDName.setValue(fd);
			var tp=decodeChars(getXmlAttribute(oXml,'tp'),"',<,>,&,\"");		comboType.setValue(tp);
			
			var fm=decodeChars(getXmlAttribute(oXml,'fm'),"',<,>,&,\"");		comboFormat.setValue(fm);
			var st=decodeChars(getXmlAttribute(oXml,'st'),"',<,>,&,\"");		radioSaveType.setGroupValue(st);
			var ip=decodeChars(getXmlAttribute(oXml,'ip'),"',<,>,&,\"");		radioInput.setGroupValue(ip);
			var ev=decodeChars(getXmlAttribute(oXml,'ev'),"',<,>,&,\"");		txtEnumValue.setValue(ev);
			var vl=decodeChars(getXmlAttribute(oXml,'vl'),"',<,>,&,\"");		txtVLRule.setValue(vl);
			var dv=decodeChars(getXmlAttribute(oXml,'dv'),"',<,>,&,\"");		txtDefValue.setValue(dv);
			var mt=decodeChars(getXmlAttribute(oXml,'mt'),"',<,>,&,\""); 		radioMust.setGroupValue(mt);
			
			var cf_sh=decodeChars(getXmlAttribute(oXml,'cf_sh'),"',<,>,&,\""); 	txtCalcFormula_Show.setValue(cf_sh);
			var cf_sa=decodeChars(getXmlAttribute(oXml,'cf_sa'),"',<,>,&,\""); 	txtCalcFormula_Save.setValue(cf_sa);
			var cf_cr=decodeChars(getXmlAttribute(oXml,'cf_cr'),"',<,>,&,\""); 	txtCalcFormula_Create.setValue(cf_cr);
			var evt=decodeChars(getXmlAttribute(oXml,'evt'),"',<,>,&,\""); 		txtEvent.setValue(evt);
		
		}catch(e){
		
		}
	}
}


/**
 * 根据当前的列定义列表初始化组合框对象
 */
function initFdCombo(){
	if(pWin.defColumnList != null){
		/** 清空数据 */
		comboFDName.store.removeAll();
		comboFDCName.store.removeAll();
		
		Ext.each(pWin.defColumnList,function(fd){
			var rec = new ComboRecord({name:fd.columncode, value:fd.columnname});
			comboFDName.store.add(rec);
			comboFDCName.store.add(rec);
		})
	}
}

/**
 * 初始化字段定义对象列表
 */
function initDefColumnList(){
	if(defTableId != ''){
		var url=link3 + "?defTableId="+defTableId;
		AjaxRequest_Sync(url,null,function(req){
			var responseText = req.responseText;
			eval("responseObj="+responseText);
			if(responseObj.success == true){
				pWin.defColumnList=responseObj.list;
			}
		})
	}
/*
 * 下面是Extjs2.2写法，跟Ext3.0不兼容，因为getConnectionObject()在3.0中被改为私有方法了
	var conn = Ext.lib.Ajax.getConnectionObject().conn; 
	conn.open("GET", url,false); 
	conn.send(null); 
	var responseObject=Ext.util.JSON.decode(conn.responseText);
	if(responseObject.success==true){
		pWin.defColumnList=responseObject.list;
	}
*/
}



var tabCF = new Ext.Panel({
            title:'计算公式',
            frame:true,
            id:'tabCF',
            items:[{
           		layout:'column',
		        border:false,
		        labelWidth:100,					//标签宽度
		        labelSeparator:'：',
		        defaults:{
		        	blankText:'必填字段'
		        },
		        items:[{
					columnWidth:1,
					layout:'form',
					border:false,
					items:[infoCalculateFormula]
				},{
					columnWidth:1,
					layout:'form',
					border:false,
					items:[txtCalcFormula_Show]
				},{
					columnWidth:1,
					layout:'form',
					border:false,
					items:[txtCalcFormula_Save]
				},{
					columnWidth:1,
					layout:'form',
					border:false,
					items:[txtCalcFormula_Create]
				}]
		    }]
        });
        
var tabEvent = new Ext.Panel({
    title:'事件定义',
    frame:true,
    id:'tabEvent',
    tbar:[{text:'签名事件',tooltip:'为当前域添加签名事件,签名事件主要用于签名域，当签名成功后触发，比如为某些域自动填写上签名时间等',handler:addEvent_Sign},
    {text:'选择数据事件',tooltip:'如果当前域有定义以SQL方式选择枚举数据的话，当选择枚举数据完成后会触发该事件',handler:addEvent_SelectData}],
    disabled:false,
    items:[{
   		layout:'column',
        border:false,
        items:[{
			columnWidth:1,
			border:false,
			items:[txtEvent]
		}]
    }]
});
var tabPanel = {//标签面板
    xtype:'tabpanel',
    activeTab: 0,
    height:430,
    plain:true,
    listeners:{tabchange :function(tPanel, tab ){tab.doLayout();}},
    defaults:{bodyStyle:'padding:2px'},
    items:[{
        title:'基本属性',
        frame:true,
        items:[{
       		layout:'column',
	        border:false,
	        labelSeparator:'：',
	        defaults:{
	        	blankText:'必填字段'
	        },
	        items:[{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtCell]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtTableName]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[comboFDCName]
	        },{
	        	columnWidth:.3,
	        	layout:'form',
	        	border:false,
	        	items:[comboFDName]
	        },{
	        	columnWidth:.13,
	        	layout:'form',
	        	border:false,
	        	items:[btnGenerateContinuousColumn]
	        },{
	        	columnWidth:.07,
	        	layout:'form',
	        	border:false,
	        	items:[btnChoose]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[comboType]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[comboFormat]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[radioMust]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[radioInput]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[radioSaveType]
	        },{
	        	columnWidth:.6,
	        	layout:'form',
	        	border:false,
	        	items:[txtVLRule]
	        },{
	        	columnWidth:.4,
	        	layout:'form',
	        	border:false,
	        	items:[infoVLRule]
	        },{
	        	columnWidth:.6,
	        	layout:'form',
	        	border:false,
	        	items:[txtEnumValue]
	        },{
	        	columnWidth:.4,
	        	layout:'form',
				border:false,
				items:[infoEnumValue]
			},{
				columnWidth:.6,
				layout:'form',
				border:false,
				items:[txtDefValue]
			},{
				columnWidth:.4,
				layout:'form',
				border:false,
				items:[infoDefValue]
			}]
        }]
    },tabCF,tabEvent]
 };
 
var simpleForm = new Ext.FormPanel({
    labelAlign: 'left',
    buttonAlign:'right',
	style:'margin:2px',
    bodyStyle:'padding:0px',
    waitMsgTarget: true,
    width: '100%',
    frame:true, 					//圆角风格
    labelWidth:70,					//标签宽度
    items:tabPanel,
	buttons: [{
		text:'保存',
		handler:function(){
			ok();
		}
	},{
        text: '取消',
            handler:function(){
            	window.close();
            }
    }]
});
	    
var lyCenter = {
	id : 'center-panel',
	region : 'center',
	items:[simpleForm]
}

/**
 * 保存
 */
function ok(){
	if(!simpleForm.form.isValid()){
		alert('数据校验失败，请检查填写的数据格式是否正确');
		return;
	}
	
	var retVal={};
	retVal.cp=comboFDCName.getDisplayValue();
	retVal.fd=comboFDName.getDisplayValue();
	
	retVal.tp=comboType.getValue();
	retVal.fm=comboFormat.getDisplayValue();
	retVal.st=radioSaveType.getGroupValue();
	retVal.ip=radioInput.getGroupValue();
	retVal.vl=txtVLRule.getValue();
	retVal.dv=txtDefValue.getValue();
	retVal.ev=txtEnumValue.getValue();
	retVal.mt=radioMust.getGroupValue();

	retVal.cf_sh=txtCalcFormula_Show.getValue();	//计算公式
	retVal.cf_sa=txtCalcFormula_Save.getValue();	//计算公式
	retVal.cf_cr=txtCalcFormula_Create.getValue();	//计算公式
	retVal.evt = txtEvent.getValue();
	//如果是计算域，则必须验证计算公式的正确性

	if(!validateFormula(retVal.cf_sh,txtCalcFormula_Show)) return;
	if(!validateFormula(retVal.cf_sa,txtCalcFormula_Save)) return;
	if(!validateFormula(retVal.cf_cr,txtCalcFormula_Create)) return;
	
	window.returnValue=retVal;
	window.close();
}

/**
 * 验证计算公式
 * @cf 公式字符串
 * @txtObj 文本框控件对象 如果验证不通过，则将光标焦点移到该对象
 */
function validateFormula(cf,txtObj){
	if(cf == null || cf == '') return true;
	cf = encodeChars(cf,'@,>,<,+,-,?,(,), ,.,:,",\'');
	var url = contextPath + "/jteap/form/cform/CFormAction!calcFormulaValidatorAction.do"
	var bResult = false;
	var myAjax = new Ajax.Request(url,{
		method: 'post', 
    	parameters: {cf:cf}, 
    	asynchronous:false,//同步调用
    	onComplete: function(req){
    		var responseText = req.responseText;
    		var responseObj = responseText.evalJSON();
    		if(responseObj.success == true){
    			bResult = true;
    		}else{
    			txtObj.focus(true,true);
    			Ext.MessageBox.alert('Status', responseObj.msg);
    		}
    	},
    	onFailure: function(e){
    		alert("验证公式失败："+e);
    	}
    });
    return bResult;
}


/**
 * 添加签名事件
 * function [fdname]_evt_sign(di,signWinRet)
 * 以字段名_evt_sig作为函数名
 * di:数据项的xml 对象
 * signWinRet:签名窗口返回的数据对象 如果签名验证成功，则会返回一个person的json对象
 */
function addEvent_Sign(){
	var fd = comboFDName.getValue();
	var evt = "/**\r\n" +
			"*事件名称：签名事件\r\n" +
			"*事件描述：如果当前域为签名域，则当签名验证完成后将会触发该事件\r\n" +
			"*参数：\r\n" +
			"*    di:数据项的xml 对象\r\n" +
			"*    signWinRet:签名窗口返回的数据对象 如果签名验证成功，\r\n" +
			"*                则会返回一个person的json对象\r\n" +
			"*/\r\n" +
			"function "+fd+"_evt_sign(di,person){\r\n" +
			"\r\n\t\r\n" +
			"}\r\n";
	txtEvent.setValue(txtEvent.getValue() + evt);
	
}
/**
 * 添加选择数据事件
 * function [fdname]_evt_SelectData(di,record)
 * 以字段名_evt_SelectData作为函数名
 * di:数据项的xml 对象
 * record:被选中的数据json对象
 */
function addEvent_SelectData(){
	var fd = comboFDName.getValue();
	var evt = "/**\r\n" +
			"*事件名称：选择数据事件\r\n" +
			"*事件描述：如果当前域的枚举数据采取SQL语句的方式取枚举值，则该事件将会触发\r\n" +
			"*参数：\r\n" +
			"*    di:数据项的xml 对象\r\n" +
			"*    record:被选中的数据json对象\r\n" +
			"*/\r\n" +
			"function "+fd+"_evt_SelectData(di,record){\r\n" +
			"\r\n\t\r\n" +
			"}\r\n";
	txtEvent.setValue(txtEvent.getValue() + evt);
}