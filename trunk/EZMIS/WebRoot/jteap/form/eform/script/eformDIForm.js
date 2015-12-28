var pWin = window.dialogArguments.opener;			//
var defTableId =window.dialogArguments.defTableId;	//关联表ID
var caption = window.dialogArguments.caption;		//字段描述名称
var title = window.dialogArguments.title;			//表单标题
var comment =window.dialogArguments.comment;		//单元格上已经存在的批注
var row =window.dialogArguments.row;				//单元格行号
var col =window.dialogArguments.col;				//单元格列号
var formSn = window.dialogArguments.formSn;			//表单唯一标识

var attrsData = {};
var evtsData = {};
var attrsShowData = {};
var evtsShowData = {};

//从配置文件中初始化控件配置信息
var editorConfigUrl = contextPath + "/jteap/form/eform/eform-editors-config.xml";
var editorsXmlDom = loadXmlFile(editorConfigUrl);
var editors = editorsXmlDom.selectNodes("/root/editor");

var ComboRecord=Ext.data.Record.create([{id:'id', mapping:'id'},{name:'name',mapping:'name'},{name:'value',mapping:'value'}]);
	/*		
var formatType=[['yyyy-MM-dd HH:mm:ss','yyyy-MM-dd HH:mm:ss'],
				['yyyy年MM月dd日 HH时mm分ss秒','yyyy年MM月dd日 HH时mm分ss秒'],
				['yyyy-MM-dd','yyyy-MM-dd'],
				['yyyy年MM月dd日','yyyy年MM月dd日 '],
				['HH:mm:ss','HH:mm:ss'],
				['HH时mm分ss秒','HH时mm分ss秒']
			];*/
			
var titlePanel=new Ext.app.TitlePanel({caption:'数据项设置',border:false,region:'north'});

//单元格标识
var txtCell=new Ext.form.TextField({disabled:true,width:190,maxLength : 50,allowBlank :false,fieldLabel : "单元格标识",name : "txtCell",readOnly:true,value:''});
var txtWidth=new Ext.form.TextField({disabled:false,width:190,maxLength : 50,allowBlank :false,fieldLabel : "数据项宽度",name : "txtWidth",readOnly:false,value:'50'});

//当前关联表
var txtTableName=new Ext.form.TextField({disabled:true,width:190,maxLength : 50,allowBlank :false,fieldLabel : "关联表",name : "txtDefTableName",readOnly:true,value:''});

//字段名
var comboFDName=new Ext.form.ComboBox({triggerAction: 'all',vtype:'alphanum',editable :false,selectOnFocus :false,forceSelection :false,maxLength : 50,mode:'local',allowBlank :false,fieldLabel : "数据项标识",name : "comboFDName",displayField:'name',valueField:'value',width:135,
	store:new Ext.data.SimpleStore({fields:['name','value']}),
	listeners:{
		select: function(combo,record){
			comboFDName.setValue(record.get("name"));
			comboFDCName.setValue(record.get("value"));
			if(isNewColumn){
				comboFDName.setEditable(false);
				comboFDCName.setEditable(false);
				isNewColumn = false;
			}
		},
		focus: function(){
			if(isNewColumn){
				//全选值,便于编辑
				comboFDName.focus(true);
			}
		}
	}
});

/**
 *  新增时,判断字段是否已存在
 */
function validataFDName(){
	var validate = true;
	if(comboFDName.getRawValue() != ""){
		Ext.Ajax.request({
			url: contextPath+"/jteap/form/dbdef/web/DefColumnInfoAction!isExistColumnAction.do",
			params: {tableId:defTableId, columnCode:comboFDName.getRawValue()},
			method: "post",
			success: function(ajax){
				eval("responsObj="+ajax.responseText);
				if(responsObj.success == true){
					if(responsObj.exist == true){
						alert("该字段名已存在,请重新输入");
						comboFDName.setValue("");
						comboFDName.focus();
						validate = false;
					}else{
						validate = true;
					}
				}
			},
			failure: function(){
				alert("服务器忙,请稍后操作...");
			}
		});
	}
	return validate;
}

//字段名中文名
var comboFDCName=new Ext.form.ComboBox({triggerAction: 'all',editable :false,selectOnFocus :false,forceSelection :false,maxLength : 50,mode:'local',allowBlank :false,fieldLabel : "数据项名称",name : "comboFDCName",displayField:'value',valueField:'name',width:190,
	store:new Ext.data.SimpleStore({fields:['name','value']}),
	listeners:{
		select: function(combo,record){
			comboFDName.setValue(record.get("name"));
			comboFDCName.setValue(record.get("value"));
			if(isNewColumn){
				comboFDName.setEditable(false);
				comboFDCName.setEditable(false);
				isNewColumn = false;
			}
		},		
		render  : function(field){
			var xx =field.getEl();
			xx.dom.attachEvent("onkeyup",function(){
				var value = hzToPy(field.getRawValue()+"");
				if(value == null){
					value = "";
				}
				comboFDName.setValue(value+"");
			})
		}
	}
});
var isFDCFocus = false;//用于汉字转拼音功能
var regex = /^('([^']*)'){1}([+|-]*('([^']*)'){1})*$/;
var regex2=/^@[a-zA-Z]*\((('([^']*)'){1}([+|-]*('([^']*)'){1}))*\)$/
var regex3=/^@[a-zA-Z]+\(.*\)$/

//显示时计算公式
var txtCalcFormula_Show=new Ext.form.TextArea({allowBlank :true,disabled:false,fieldLabel : "显示时计算公式",name : "txtCalcFormula_Show",width:390,value:""	});
var txtCalcFormula_Save=new Ext.form.TextArea({allowBlank :true,disabled:false,fieldLabel : "保存时计算公式",name : "txtCalcFormula_Save",width:390,value:""	});
var txtCalcFormula_Create=new Ext.form.TextArea({allowBlank :true,disabled:false,fieldLabel : "创建时计算公式",name : "txtCalcFormula_Create",width:390,value:""	});

var infoCalculateFormula=new Ext.app.LabelPanel({label:'<div style="margin-left:12px;">显示时计算：每次表单打开的时候进行计算<br/>保存时计算：每次保存该数据之前计算<br/>创建时计算：只有当文档在第一次创建的时候计算<br/>可使用的公式：@IF、@SYSDT、@HTTPSESSION、@SQL、@UUID、@PARSENUMBER&nbsp;<a href="cf_help.html" target="_blank">公式帮助</a></div>',height:70});

var radioDataType=new Ext.form.RadioGroup({
	fieldLabel: '数据类型',
	columns :3,
	items: 
		[
			{boxLabel: '字符串(VARCHAR2)', name: 'dataType', inputValue: 'VARCHAR2', checked: true,listeners:{check:radioDataTypeCheck}},
			{boxLabel: '日期时间(TIMESTAMP)', name: 'dataType', inputValue: 'TIMESTAMP',listeners:{check:radioDataTypeCheck}},
			{boxLabel: '二进制(BLOB)', name: 'dataType', inputValue: 'BLOB',listeners:{check:radioDataTypeCheck}},
			{boxLabel: '数字型(NUMBER)', name: 'dataType', inputValue: 'NUMBER',listeners:{check:radioDataTypeCheck}},
			{boxLabel: '大字符串(CLOB)', name: 'dataType', inputValue: 'CLOB',listeners:{check:radioDataTypeCheck}},
			{boxLabel: '非映射项', name: 'dataType', inputValue: 'NONE',listeners:{check:radioDataTypeCheck}}
		]
});
function radioDataTypeCheck(radioData,checked){
	/** 输入控件value
	 * 
	 * 	TXT   COMB	 CAL    RAD    CHK  
		V2T   BTN    HTML   DOC    UPL
		SIGN  TXTE   SUB
	 * */
	if(checked){
		var data = radioData.inputValue;
		if('VARCHAR2' == data){
			//radioEditorType.setValue('TXT');
		}else if('TIMESTAMP' == data){
			radioEditorType.setValue('CAL');
		}else if('BLOB' == data){
			radioEditorType.setValue('DOC');
		}else if('NUMBER' == data){
			radioEditorType.setValue('TXT');
		}else if('CLOB' == data){
			radioEditorType.setValue('HTML');
		}
	}
}

/**
 * 编辑器单选框选择触发事件
 */
function onEditorTypeCheck(radioEditor,checked){
	if(checked){
		var editorId = radioEditor.inputValue;
		var defaultParamJson = getDefaultParamJson(editorId);
		txtEditorParam.setValue(defaultParamJson);
		if(editorId == 'BTN'){
			radioDataType.setValue("NONE");
		}
	}
}

/**
 * [
{boxLabel: '文本框', name: 'editorType', inputValue: 'TXT', checked: true},
{boxLabel: '下拉框', name: 'editorType', inputValue: 'COMB'},
.....
  ]
 */
var radioEditorType=new Ext.form.RadioGroup({fieldLabel: '输入控件',columns :5,
    items: function(){
		var items = [];
		for(var i=0;i<editors.length;i++){
			var et = editors[i];
			var name = getXmlAttribute(et,"name");
			var id = getXmlAttribute(et,"id");
			items.push({boxLabel:name,name:'editorType',inputValue:id,checked:i==0,listeners:{
				check:onEditorTypeCheck
			}});
		}
		return items;
    }()
});

var txtEditorParam=new Ext.form.TextField({
	width: 440,
	fieldLabel: "编辑器参数",
	name: "txtEditorParam",
	disabled: false,
	allowBlank: true,
	readOnly: false,
	listeners: {
		blur: function(){
			try{
				var editorParam = txtEditorParam.getValue();
				if(editorParam.trim() != ""){
					var json = editorParam.evalJSON();
				}
				Ext.util.JSON.encode(json);
			}catch(e){
				alert("参数格式必须为JSON");
				txtEditorParam.focus();
			}
		}
	}
});
var btnEditorParam = new Ext.Button({
	text: '...',
	handler: function(){
		if(comboFDName.getValue().trim() != ""){
			//fdname,params,evt
			var fd = comboFDName.getValue();
			var edr_param = txtEditorParam.getValue();
			var edr = radioEditorType.getValue();
			var xpath = "/root/editor[@id='"+edr+"']";
			var oEditorXmlNode = editorsXmlDom.selectNodes(xpath)[0];
			var editorPropertyForm = new EditorPropertyForm(fd,edr_param,oEditorXmlNode);
			editorPropertyForm.on('close',editorCallBack,editorPropertyForm);
			editorPropertyForm.show();
		}
	}
});
function editorCallBack(editorPropertyForm){
	if(editorPropertyForm.ok == true){
		//var attrsData  defined in eformDIFormEditorProperty.js的全局变量
		//var evtsData defined in eformDIFormEditorProperty.js的全局变量
		
		for(att in attrsData){
			eval("var value=attrsData."+att);
			if(typeof(value) != 'boolean' && value == ""){
				eval("delete attrsData."+att+";");
			}
		}
		var bHaveEvent = false;
		for(evt in evtsData){
			eval("var value=evtsData."+evt);
			if(value == ""){
				eval("delete evtsData."+evt+";");
			}else{
			bHaveEvent = true;
			}
		}
		if(bHaveEvent){
			attrsData.evts = evtsData;
		}
		
		var editorValue = Ext.util.JSON.encode(attrsData);
		txtEditorParam.setValue(editorValue);
		
	}
}

var txtValidat=new Ext.form.TextField({disabled:false,width:443,maxLength : 200,allowBlank :true,fieldLabel : "数据验证",name : "txtValidat",readOnly:false});
var txtValidatTip=new Ext.form.TextField({disabled:false,width:443,maxLength : 200,allowBlank :true,fieldLabel : "验证提示",name : "txtValidatTip",readOnly:false});
var chkNotNull = new Ext.form.Checkbox({fieldLabel:'非空字段',name:'chkNotNull'});
var chkUnique = new Ext.form.Checkbox({fieldLabel:'唯一性',name:'chkUnique'});
var txtSortNo=new Ext.form.TextField({disabled:false,width:190,maxLength : 50,allowBlank :true,fieldLabel : "排序号",name : "txtSortNo",readOnly:false,value:(new Date()).getTime()});

//新增数据项按钮
var btnNewColumn = new Ext.Button({text:'新增数据项',handler:function(){
	isNewColumn = true;
	comboFDCName.setEditable(true);
	comboFDName.setEditable(true);
	var temp = new Date().format('ymdHis');
	comboFDCName.setValue('DI'+temp);
	comboFDName.setValue('DI'+temp);
	comboFDCName.focus(true);
	
}});

var isNewColumn = false;


/**
 * 页面初始化
 */
function onload() {
	//初始化相关控件值
	txtTableName.setValue(caption);
	txtCell.setValue(row+"行"+col+"列");
	
	//初始化默认控件参数
	var curEditor = radioEditorType.getValue();
	var defaultParamJson = getDefaultParamJson(curEditor);
	txtEditorParam.setValue(decodeChars(defaultParamJson,"',\""));

	//初始化下拉菜单数据store
	initFdCombo();
	
	// 根据注解初始化控件值
	initValueWithComment();
}

/**
 * 获取控件 属性、事件.
 * @param {} editorId 控件类型ID.
 * @return {} 显示的 json.
 */
function getDefaultParamJson(editorId){
	var editorsXmlDom = loadXmlFile(contextPath + '/jteap/form/eform/eform-editors-config.xml');
	var xpath = "/root/editor[@id='"+editorId+"']";
	var attrsPath = xpath + '/attrs/attr';
	var evtsPath = xpath + '/evts/evt';
	var attrs = editorsXmlDom.selectNodes(attrsPath);
	var evts = editorsXmlDom.selectNodes(evtsPath);
	
	//初始化全局变量
	attrsData = {};
	attrsShowData = {};
	evtsData = {};
	evtsShowData = {};
	
	//基本属性
	for(var i=0; i<attrs.length; i++){
		var attrNode = attrs[i];
		var attrName = getXmlAttribute(attrNode,"name");
		var must = getXmlAttribute(attrNode,"must");
		var dv = getXmlAttribute(attrNode,"dv");
		var many = getXmlAttribute(attrNode,"many");
		dv = encodeChars(dv,"',\"");
		attrsData[attrName] = dv;
		if(must == "true"){
			attrsShowData[attrName] = dv; 
		}
	}
	//事件
	for(var i=0; i<evts.length; i++){
		var evtNode = evts[i];
		var evtName = getXmlAttribute(evtNode,"name");
		evtsData[evtName] = "";
	}
	
	var attrsDS = Ext.util.JSON.encode(attrsShowData);
	var evtsDS = Ext.util.JSON.encode(evtsShowData);
	
	var json = {};
	if(attrsDS != "{}"){
		json = attrsDS;
	}
	if(evtsDS != "{}"){
		json.evts = evtsDS;
	}
	if(Ext.util.JSON.encode(json) == "{}"){
		json = "";
	}
	return json;
}

/**
 * 根据注解初始化各个控件的初始值
 * <di cp='' fd='' ce='' tp=''fm='' st='' ev='' vl='' />
 */
function initValueWithComment(){
	if(comment!=null && comment.length>0){
		try{
			var oXml=getDom(comment).firstChild;
			var cp=decodeChars(getXmlAttribute(oXml,'cp'),"',<,>,&,\"");   		comboFDCName.setValue(cp);
			var fd=decodeChars(getXmlAttribute(oXml,'fd'),"',<,>,&,\"");		comboFDName.setValue(fd);
			var tp=decodeChars(getXmlAttribute(oXml,'tp'),"',<,>,&,\""); 		radioDataType.setValue(tp);		
			var edr=decodeChars(getXmlAttribute(oXml,'edr'),"',<,>,&,\""); 		radioEditorType.setValue(edr);
			var sort=decodeChars(getXmlAttribute(oXml,'sort'),"',<,>,&,\""); 	txtSortNo.setValue(sort);
			var wid=decodeChars(getXmlAttribute(oXml,'wid'),"',<,>,&,\""); 		txtWidth.setValue(wid);
			var edr_pa=decodeChars(getXmlAttribute(oXml,'edr_pa'),"',<,>,&,\"");txtEditorParam.setValue(edr_pa);
			var vl=decodeChars(getXmlAttribute(oXml,'vl'),"',<,>,&,\"");		txtValidat.setValue(vl);
			var vltip=decodeChars(getXmlAttribute(oXml,'vltip'),"',<,>,&,\"");	txtValidatTip.setValue(vltip);
			var cf_sh=decodeChars(getXmlAttribute(oXml,'cf_sh'),"',<,>,&,\""); 	txtCalcFormula_Show.setValue(cf_sh);
			var cf_sa=decodeChars(getXmlAttribute(oXml,'cf_sa'),"',<,>,&,\""); 	txtCalcFormula_Save.setValue(cf_sa);
			var cf_cr=decodeChars(getXmlAttribute(oXml,'cf_cr'),"',<,>,&,\""); 	txtCalcFormula_Create.setValue(cf_cr);
			var nn=decodeChars(getXmlAttribute(oXml,'nn'),"',<,>,&,\""); 		chkNotNull.setValue(nn);
			var unq=decodeChars(getXmlAttribute(oXml,'unq'),"',<,>,&,\""); 		chkUnique.setValue(unq);
			
			//将批注中的信息 设置到JSON对象中
			var attrsJson = edr_pa.evalJSON();
			var evtsJson = edr_pa.evalJSON();
			
			delete attrsJson.evts;
			attrsShowData = attrsJson;
			evtsShowData = evtsJson.evts;
			
			/*
			 * 
			var fm=decodeChars(getXmlAttribute(oXml,'fm'),"',<,>,&,\"");		comboFormat.setValue(fm);
			var st=decodeChars(getXmlAttribute(oXml,'st'),"',<,>,&,\"");		radioSaveType.setGroupValue(st);
			var ip=decodeChars(getXmlAttribute(oXml,'ip'),"',<,>,&,\"");		radioInput.setGroupValue(ip);
			var ev=decodeChars(getXmlAttribute(oXml,'ev'),"',<,>,&,\"");		txtEnumValue.setValue(ev);
			var dv=decodeChars(getXmlAttribute(oXml,'dv'),"',<,>,&,\"");		txtDefValue.setValue(dv);
			var mt=decodeChars(getXmlAttribute(oXml,'mt'),"',<,>,&,\""); 		radioMust.setGroupValue(mt);
			
			var evt=decodeChars(getXmlAttribute(oXml,'evt'),"',<,>,&,\""); 		txtEvent.setValue(evt);
		*/
		}catch(e){
			alert(e.description)
		}
	}
}

/**
 * 根据当前的列定义列表初始化组合框对象
 */
function initFdCombo(){
	var defColumnList = defColumns.data;
	if(defColumnList != null){
		/** 清空数据 */
		comboFDName.store.removeAll();
		comboFDCName.store.removeAll();
		
		Ext.each(defColumnList,function(fd){
			var rec = new ComboRecord({name:fd.columncode, value:fd.columnname});
			comboFDName.store.add(rec);
			comboFDCName.store.add(rec);
		})
	}
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
	        	name: 'itFD',
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[comboFDCName]
	        },{
	        	name: 'itFDC',
	        	columnWidth:.35,
	        	layout:'form',
	        	border:false,
	        	items:[comboFDName]
	        },{
	        	columnWidth: .15,
	        	layout: 'form',
	        	border: false,
	        	items: [btnNewColumn]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtWidth]
	        },{
	        	columnWidth:.5,
	        	layout:'form',
	        	border:false,
	        	items:[txtSortNo]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[radioDataType]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[radioEditorType]
	        },{
	        	columnWidth:.85,
	        	layout:'form',
	        	border:false,
	        	items:[txtEditorParam]
	        },{
	        	columnWidth:.15,
	        	layout:'form',
	        	border:false,
	        	items:[btnEditorParam]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtValidat]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[txtValidatTip]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[chkNotNull]
	        },{
	        	columnWidth:1,
	        	layout:'form',
	        	border:false,
	        	items:[chkUnique]
	        }]
        }]
    },tabCF]
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
			if(!simpleForm.form.isValid()){
				alert('数据校验失败，请检查填写的数据格式是否正确');
				return;
			}
			
			if(radioDataType.getValue() != 'NONE'){
				if(!validataFDName()){
					return;
				}
				saveColumnInfo();
			}
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
 * 保存字段
 */
function saveColumnInfo(){
	if(!isNewColumn){
		return;
	}
	
	var dataType = radioDataType.getValue();
	var width = txtWidth.getValue();
	var sortNo = txtSortNo.getValue();
	Ext.Ajax.request({
		url: contextPath+"/jteap/form/eform/EFormAction!saveColumnInfoAction.do",
		method: 'post',
		waitMsg: '保存数据中,请稍后...',
		params: {tableid:defTableId,formSn:formSn,columncode:comboFDName.getRawValue(),columnname:comboFDCName.getRawValue(),columntype:dataType,
				columnlength:width,columnorder:sortNo, allownull:chkNotNull.getValue(), chkUnique:chkUnique.getValue()},
		success: function(ajax){
			eval('responseObj='+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后操作...');
		}
	});
}

/**
 * 保存
 */
function ok(){
	var retVal={};
	retVal.cp=comboFDCName.getDisplayValue();
	retVal.fd=comboFDName.getDisplayValue();
	retVal.tp=radioDataType.getValue();
	retVal.vl=txtValidat.getValue();
	retVal.vltip = txtValidatTip.getValue();
	retVal.edr = radioEditorType.getValue();
	retVal.edr_pa = txtEditorParam.getValue();
	retVal.wid = txtWidth.getValue();
	retVal.sort = txtSortNo.getValue();
	retVal.cf_sh=txtCalcFormula_Show.getValue();	//计算公式
	retVal.cf_sa=txtCalcFormula_Save.getValue();	//计算公式
	retVal.cf_cr=txtCalcFormula_Create.getValue();	//计算公式
	//retVal.row = row;
	//retVal.col = col;
	retVal.nn = chkNotNull.checked;
	retVal.unq = chkUnique.checked;
	
	//如果是计算域，则必须验证计算公式的正确性
	//if(!validateFormula(retVal.cf_sh,txtCalcFormula_Show)) return;
	//if(!validateFormula(retVal.cf_sa,txtCalcFormula_Save)) return;
	//if(!validateFormula(retVal.cf_cr,txtCalcFormula_Create)) return;
	
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
	var url = contextPath + "/jteap/form/eform/EFormAction!calcFormulaValidatorAction.do"
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
