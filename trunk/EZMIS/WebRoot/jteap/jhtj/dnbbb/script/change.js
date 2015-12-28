var allComponent = new Array();
var isChange = false;
var id = "";
///////////////////////////////////// 第一行 //////////////////////////////////////////
//电能表Id
var storeElecId = new Ext.data.JsonStore({
	url: link16,
	autoLoad : true,
	fields : ["ELECID", "NAME"]
});
var comboElecId = new Ext.form.ComboBox({
	id: 'comboElecId',
	renderTo: 'divElecId',
	valueField: "ELECID",
	displayField: "NAME",
	mode: 'local',
	triggerAction: 'all',
	blankText: '请选择',
	emptyText: '请选择',
	width: 150,
	store: storeElecId,
	forceSelection: true,
	editable: false,
	allowBlank: false,
	listeners: {
		select: function(){
			changeNO();
		}
	}
});
//换表时间
var dateTime = new Ext.form.DateField({
	id: 'dateTime',	
	renderTo: 'divTime',
	format: 'Y-m-d H:i',
	menu: new DatetimeMenu(),
	width: 150,
	allowBlank: false
});
//换表人
var txtHbr = new Ext.form.TextField({
	id: 'txtHbr',
	renderTo: 'divHbr',
	value: curPersonName,
	maxLength: 30,
	width: 150,
	readOnly: true
});
allComponent.push(comboElecId);
allComponent.push(dateTime);
allComponent.push(txtHbr);

///////////////////////////////////// 第二行 //////////////////////////////////////////
//旧表表名
var txtElecIdO = new Ext.form.TextField({
	id: 'txtElecIdO',
	renderTo: 'divElecIdO',
	maxLength: 30,
	width: 120,
	readOnly: true
});
//旧表_正向有功_峰
var txtFpzO = new Ext.form.NumberField({
	id: 'txtFpzO',
	renderTo: 'divFpzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向有功_峰
var txtFpfO = new Ext.form.NumberField({
	id: 'txtFpfO',
	renderTo: 'divFpfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_正向无功_峰
var txtFqzO = new Ext.form.NumberField({
	id: 'txtFqzO',
	renderTo: 'divFqzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向无功_峰
var txtFqfO = new Ext.form.NumberField({
	id: 'txtFqfO',
	renderTo: 'divFqfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtElecIdO);
allComponent.push(txtFpzO);
allComponent.push(txtFpfO);
allComponent.push(txtFqzO);
allComponent.push(txtFqfO);

///////////////////////////////////// 第三行 //////////////////////////////////////////
//旧表表号
var txtElecbhO = new Ext.form.TextField({
	id: 'txtElecbhO',
	renderTo: 'divElecbhO',
	maxLength: 30,
	width: 120,
	readOnly: true
});
//旧表_正向有功_平
var txtPpzO = new Ext.form.NumberField({
	id: 'txtPpzO',
	renderTo: 'divPpzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向有功_平
var txtPpfO = new Ext.form.NumberField({
	id: 'txtPpfO',
	renderTo: 'divPpfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_正向无功_平
var txtPqzO = new Ext.form.NumberField({
	id: 'txtPqzO',
	renderTo: 'divPqzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向无功_平
var txtPqfO = new Ext.form.NumberField({
	id: 'txtPqfO',
	renderTo: 'divPqfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtElecbhO);
allComponent.push(txtPpzO);
allComponent.push(txtPpfO);
allComponent.push(txtPqzO);
allComponent.push(txtPqfO);

///////////////////////////////////// 第四行 //////////////////////////////////////////
//旧表CT
var txtCtO = new Ext.form.TextField({
	id: 'txtCtO',
	renderTo: 'divCtO',
	maxLength: 30,
	width: 120,
	readOnly: true
});
//旧表_正向有功_谷
var txtGpzO = new Ext.form.NumberField({
	id: 'txtGpzO',
	renderTo: 'divGpzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向有功_谷
var txtGpfO = new Ext.form.NumberField({
	id: 'txtGpfO',
	renderTo: 'divGpfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_正向无功_谷
var txtGqzO = new Ext.form.NumberField({
	id: 'txtGqzO',
	renderTo: 'divGqzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向无功_谷
var txtGqfO = new Ext.form.NumberField({
	id: 'txtGqfO',
	renderTo: 'divGqfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtCtO);
allComponent.push(txtGpzO);
allComponent.push(txtGpfO);
allComponent.push(txtGqzO);
allComponent.push(txtGqfO);

///////////////////////////////////// 第五行 //////////////////////////////////////////
//旧表PT
var txtPtO = new Ext.form.TextField({
	id: 'txtPtO',
	renderTo: 'divPtO',
	maxLength: 30,
	width: 120,
	readOnly: true
});
//旧表_正向有功_总
var txtPzO = new Ext.form.NumberField({
	id: 'txtPzO',
	renderTo: 'divPzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向有功_总
var txtPfO = new Ext.form.NumberField({
	id: 'txtPfO',
	renderTo: 'divPfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_正向无功_总
var txtQzO = new Ext.form.NumberField({
	id: 'txtQzO',
	renderTo: 'divQzO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//旧表_反向无功_总
var txtQfO = new Ext.form.NumberField({
	id: 'txtQfO',
	renderTo: 'divQfO',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtPtO);
allComponent.push(txtPzO);
allComponent.push(txtPfO);
allComponent.push(txtQzO);
allComponent.push(txtQfO);

///////////////////////////////////// 第六行 //////////////////////////////////////////
//新表表名
var txtElecIdN = new Ext.form.TextField({
	id: 'txtElecIdN',
	renderTo: 'divElecIdN',
	maxLength: 30,
	width: 120,
	readOnly: true
});
//新表_正向有功_峰
var txtFpzN = new Ext.form.NumberField({
	id: 'txtFpzN',
	renderTo: 'divFpzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向有功_峰
var txtFpfN = new Ext.form.NumberField({
	id: 'txtFpfN',
	renderTo: 'divFpfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_正向无功_峰
var txtFqzN = new Ext.form.NumberField({
	id: 'txtFqzN',
	renderTo: 'divFqzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向无功_峰
var txtFqfN = new Ext.form.NumberField({
	id: 'txtFqfN',
	renderTo: 'divFqfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtElecIdN);
allComponent.push(txtFpzN);
allComponent.push(txtFpfN);
allComponent.push(txtFqzN);
allComponent.push(txtFqfN);

///////////////////////////////////// 第七行 //////////////////////////////////////////
//新表表号
var txtElecbhN = new Ext.form.TextField({
	id: 'txtElecbhN',
	renderTo: 'divElecbhN',
	maxLength: 30,
	width: 120,
	allowBlank: false
});
//新表_正向有功_平
var txtPpzN = new Ext.form.NumberField({
	id: 'txtPpzN',
	renderTo: 'divPpzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向有功_平
var txtPpfN = new Ext.form.NumberField({
	id: 'txtPpfN',
	renderTo: 'divPpfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_正向无功_平
var txtPqzN = new Ext.form.NumberField({
	id: 'txtPqzN',
	renderTo: 'divPqzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向无功_平
var txtPqfN = new Ext.form.NumberField({
	id: 'txtPqfN',
	renderTo: 'divPqfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtElecbhN);
allComponent.push(txtPpzN);
allComponent.push(txtPpfN);
allComponent.push(txtPqzN);
allComponent.push(txtPqfN);

///////////////////////////////////// 第八行 //////////////////////////////////////////
//新表CT
var txtCtN = new Ext.form.TextField({
	id: 'txtCtN',
	renderTo: 'divCtN',
	maxLength: 30,
	width: 120,
	allowBlank: false
});
//新表_正向有功_谷
var txtGpzN = new Ext.form.NumberField({
	id: 'txtGpzN',
	renderTo: 'divGpzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向有功_谷
var txtGpfN = new Ext.form.NumberField({
	id: 'txtGpfN',
	renderTo: 'divGpfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_正向无功_谷
var txtGqzN = new Ext.form.NumberField({
	id: 'txtGqzN',
	renderTo: 'divGqzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向无功_谷
var txtGqfN = new Ext.form.NumberField({
	id: 'txtGqfN',
	renderTo: 'divGqfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtCtN);
allComponent.push(txtGpzN);
allComponent.push(txtGpfN);
allComponent.push(txtGqzN);
allComponent.push(txtGqfN);

///////////////////////////////////// 第九行 //////////////////////////////////////////
//新表PT
var txtPtN = new Ext.form.TextField({
	id: 'txtPtN',
	renderTo: 'divPtN',
	maxLength: 30,
	width: 120,
	allowBlank: false
});
//新表_正向有功_总
var txtPzN = new Ext.form.NumberField({
	id: 'txtPzN',
	renderTo: 'divPzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向有功_总
var txtPfN = new Ext.form.NumberField({
	id: 'txtPfN',
	renderTo: 'divPfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_正向无功_总
var txtQzN = new Ext.form.NumberField({
	id: 'txtQzN',
	renderTo: 'divQzN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
//新表_反向无功_总
var txtQfN = new Ext.form.NumberField({
	id: 'txtQfN',
	renderTo: 'divQfN',
	maxLength: 30,
	width: 120,
	decimalPrecision: 4,
	allowBlank: false
});
allComponent.push(txtPtN);
allComponent.push(txtPzN);
allComponent.push(txtPfN);
allComponent.push(txtQzN);
allComponent.push(txtQfN);
/////////////////////////////////////////////////////////////////////////////////////

//选择电能表,获取旧表信息
function changeNO(){
	isChange = false;
	id = "";
	
	//根据表编号获取换表记录
	var url = link17+"?elecid="+comboElecId.getValue();
	var conn = Ext.lib.Ajax.getConnectionObject().conn; 
	conn.open("post", url,false);
	conn.send(null);
	var responseObject = Ext.util.JSON.decode(conn.responseText);
	if(responseObject.exits != false){
		//有换表记录
		isChange = true;
//"ID":"8a65adf42d58bc7f012d58c644b50006","TIMES":"2011-01-06 00:00","HBR":"系统管理员","ELECID":1,
//"CT_N":4,"PT_N":4,"CT_O":5000,"PT_O":220,
//"ELECBH_N":"4","ELECBH_O":"96113733",
//"PZ_N":4,"PF_N":4,"QZ_N":4,"QF_N":4,
//"FPZ_N":4,"FPF_N":4,"FQZ_N":4,"FQF_N":4,
//"PPZ_N":4,"PPF_N":4,"PQZ_N":4,"PQF_N":4,
//"GPZ_N":4,"GPF_N":4,"GQZ_N":4,"GQF_N":4,
//"PZ_O":4,"PF_O":4,"QZ_O":4,"QF_O":4,
//"FPZ_O":4,"FPF_O":4,"FQZ_O":4,"FQF_O":4,
//"PPZ_O":4,"PPF_O":4,"PQZ_O":4,"PQF_O":4,
//"GPZ_O":4,"GPF_O":4,"GQZ_O":4,"GQF_O":4,
		//新旧表名
		txtElecIdO.setValue(comboElecId.getRawValue());
		txtElecIdN.setValue(comboElecId.getRawValue());
		
		id = responseObject.ID;
		dateTime.setValue(responseObject.TIMES);
		txtHbr.setValue(responseObject.HBR);
		
		txtCtN.setValue(responseObject.CT_N);
		txtPtN.setValue(responseObject.PT_N);
		txtCtO.setValue(responseObject.CT_O);
		txtPtO.setValue(responseObject.PT_O);
		
		txtElecbhN.setValue(responseObject.ELECBH_N);
		txtElecbhO.setValue(responseObject.ELECBH_O);
		
		txtPzN.setValue(responseObject.PZ_N);
		txtPfN.setValue(responseObject.PF_N);
		txtQzN.setValue(responseObject.QZ_N);
		txtQfN.setValue(responseObject.QF_N);
		
		txtFpzN.setValue(responseObject.FPZ_N);
		txtFpfN.setValue(responseObject.FPF_N);
		txtFqzN.setValue(responseObject.FQZ_N);
		txtFqfN.setValue(responseObject.FQF_N);
		
		txtPpzN.setValue(responseObject.PPZ_N);
		txtPpfN.setValue(responseObject.PPF_N);
		txtPqzN.setValue(responseObject.PQZ_N);
		txtPqfN.setValue(responseObject.PQF_N);
		
		txtGpzN.setValue(responseObject.GPZ_N);
		txtGpfN.setValue(responseObject.GPF_N);
		txtGqzN.setValue(responseObject.GQZ_N);
		txtGqfN.setValue(responseObject.GQF_N);
		
		//////////////////旧表/////////////////////
		txtPzO.setValue(responseObject.PZ_O);
		txtPfO.setValue(responseObject.PF_O);
		txtQzO.setValue(responseObject.QZ_O);
		txtQfO.setValue(responseObject.QF_O);
		
		txtFpzO.setValue(responseObject.FPZ_O);
		txtFpfO.setValue(responseObject.FPF_O);
		txtFqzO.setValue(responseObject.FQZ_O);
		txtFqfO.setValue(responseObject.FQF_O);
		
		txtPpzO.setValue(responseObject.PPZ_O);
		txtPpfO.setValue(responseObject.PPF_O);
		txtPqzO.setValue(responseObject.PQZ_O);
		txtPqfO.setValue(responseObject.PQF_O);
		
		txtGpzO.setValue(responseObject.GPZ_O);
		txtGpfO.setValue(responseObject.GPF_O);
		txtGqzO.setValue(responseObject.GQZ_O);
		txtGqfO.setValue(responseObject.GQF_O);
		
	}else{
		//没有换表记录,查询出该表初始信息
		var url = link18+"?elecid="+comboElecId.getValue();
		var conn = Ext.lib.Ajax.getConnectionObject().conn; 
		conn.open("post", url,false);
		conn.send(null);
		var responseObject = Ext.util.JSON.decode(conn.responseText);
		if(responseObject.exits != false){
			for (var i=0; i<allComponent.length; i++) {
				if(allComponent[i] != comboElecId){
					allComponent[i].setValue("");
					allComponent[i].clearInvalid(); 
				}	
			}
			
			//新旧表名
			txtElecIdO.setValue(comboElecId.getRawValue());
			txtElecIdN.setValue(comboElecId.getRawValue());
			txtHbr.setValue(curPersonName);
			
			var elecBh = responseObject.ELECBH;
			elecBh = elecBh.substring(3,elecBh.length);
			//旧表表号、ct、pt
			txtElecbhO.setValue(elecBh);
			
			var cts = responseObject.CTS;
			cts = cts.substring(3,cts.length);
			var pts = responseObject.PTS;
			pts = pts.substring(3,pts.length);
			
			txtCtO.setValue(cts);
			txtPtO.setValue(pts);
		}
	}
}

//保存
function save(){
	/** 数据验证 */
	for (var i=0; i<allComponent.length; i++) {
		if(!allComponent[i].validate()){
			allComponent[i].focus();
			alert("数据验证失败");
			return;
		}
	}
	
	var ctnArray = txtCtN.getValue().split("/");
	var ptnArray = txtPtN.getValue().split("/");
	var ctnIs = false;
	var ptnIs = false;
	if(ctnArray.length == 2){
		if(isNaN(ctnArray[0]) || ctnArray[0].trim() == ""){
			ctnIs = true;
		}
		if(isNaN(ctnArray[1]) || ctnArray[1].trim() == ""){
			ctnIs = true;
		}
	}else{
		ctnIs = true;
	}
	if(ctnIs){
		alert("新表CT必须为 '100/1' 格式");
		txtCtN.focus(true);
		return;
	}
	
	if(ptnArray.length == 2){
		if(isNaN(ptnArray[0]) || ptnArray[0].trim() == ""){
			ptnIs = true;
		}
		if(isNaN(ptnArray[1]) || ptnArray[1].trim() == ""){
			ptnIs = true;
		}
	}else{
		ptnIs = true;
	}
	if(ptnIs){
		alert("新表PT必须为 '100/1' 格式");
		txtPtN.focus(true);
		return;
	}
	
	var curTime = dateTime.getValue().format("Y-m-d H:i");
	var curHbr = txtHbr.getValue();
	if(isChange){
		curHbr = curPersonName;
	}
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link19,
		method: 'post',
		params: {
				id:id, elecId:comboElecId.getValue(), times:curTime, hbr:curHbr, ctN:txtCtN.getValue(), ptN:txtPtN.getValue(), 
				ctO:txtCtO.getValue(), ptO:txtPtO.getValue(), elecbhN:txtElecbhN.getValue(), elecbhO:txtElecbhO.getValue(),
				pzN:txtPzN.getValue(), pfN:txtPfN.getValue(), qzN:txtQzN.getValue(), qfN:txtQfN.getValue(),
				fpzN:txtFpzN.getValue(), fpfN:txtFpfN.getValue(), fqzN:txtFqzN.getValue(), fqfN:txtFqfN.getValue(),
				ppzN:txtPpzN.getValue(), ppfN:txtPpfN.getValue(), pqzN:txtPqzN.getValue(), pqfN:txtPqfN.getValue(),
				gpzN:txtGpzN.getValue(), gpfN:txtGpfN.getValue(), gqzN:txtGqzN.getValue(), gqfN:txtGqfN.getValue(),
				pzO:txtPzO.getValue(), pfO:txtPfO.getValue(),  qzO:txtQzO.getValue(), qfO:txtQfO.getValue(),
				fpzO:txtFpzO.getValue(), fpfO:txtFpfO.getValue(), fqzO:txtFqzO.getValue(), fqfO:txtFqfO.getValue(),
				ppzO:txtPpzO.getValue(), ppfO:txtPpfO.getValue(), pqzO:txtPqzO.getValue(), pqfO:txtPqfO.getValue(),
				gpzO:txtGpzO.getValue(), gpfO:txtGpfO.getValue(), gqzO:txtGqzO.getValue(), gqfO:txtGqfO.getValue()
				},
		success: function(ajax){
			eval("responseObj=" + ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				window.close();
			}else{
				alert('保存失败,请联系管理员...');
				window.close();
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})
}
