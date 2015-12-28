var flag = false;
if(type == "show"){
	flag = true;
}

//当前日期
var year = new Date().getFullYear();
var month = new Date().getMonth()+1;
var day = new Date().getDate();
if(month < 10){
	month = "0" + month;
}else{
	month = month;
}
if(day < 10){
	day = "0" + day;
}else{
	day = day;
}
var currentDate = year+"-"+month+"-"+day;

//反馈人
var txtCreatePerson= new Ext.form.TextField({
	id: 'txtCreatePerson',
	renderTo: 'divCreatePerson',
	maxLength: 32,
	width: 100,
	readOnly:true,
	value: curPersonName, 
	maxLengthText: '最长32个字符',
	blankText: '请输入反馈人',
	allowBlank: false
});

//反馈时间
var txtCreateDate= new Ext.form.DateField({
	id: 'txtCreateDate',
	renderTo: 'divCreateDate',
	format: 'Y-m-d',
	value:currentDate,
	readOnly:true,
	width: 110,
	blankText: '请输入反馈时间',
	allowBlank: flag,
	listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtCreateDate').readOnly = true;
			}else{
				Ext.getCmp('txtCreateDate').readOnly = false;
			}
		}
	}
});

//反馈内容
var txtContent= new Ext.form.TextArea({
	id: 'txtContent',
	renderTo: 'divContent',
	maxLength: 500,
	height:130,
	width: 395,
	maxLengthText: '最长2000个字符',
	listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtContent').readOnly = true;
			}else{
				Ext.getCmp('txtContent').readOnly = false;
			}
		}
	}
});

//备注
var txtRemark= new Ext.form.TextArea({
	id: 'txtRemark',
	renderTo: 'divRemark',
	maxLength: 500,
	height:100,
	width: 395,
	maxLengthText: '最长250个字符',
	listeners:{
		"beforerender":function(param){
			if(type == "show"){
				Ext.getCmp('txtRemark').readOnly = true;
			}else{
				Ext.getCmp('txtRemark').readOnly = false;
			}
		}
	}
});

var id = window.dialogArguments.id;
var tableId = window.dialogArguments.tableId;

if(id != null){
	//修改时 赋值
	txtCreatePerson.setValue(window.dialogArguments.createPerson);
	txtCreateDate.setValue((window.dialogArguments.createDate==null)?"":formatDate(new Date(window.dialogArguments.createDate.time),"yyyy-MM-dd"));
	txtContent.setValue(window.dialogArguments.content);
	txtRemark.setValue(window.dialogArguments.remark);
	if(type == "show"){
		txtCreatePerson.disabled = true;
		txtCreateDate.disabled = true;
		txtContent.disabled = true;
		txtRemark.disabled = true;
	}
}

/**
 * 保存
 */
var save = function(){
	/** 数据验证 */
	if(!txtCreatePerson.validate()){
		alert('请输入正确的反馈人!');
		txtCreatePerson.focus(true);
		return;
	}
	if(!txtCreateDate.validate()){
		alert('请输入 反馈时间!');
		txtCreateDate.focus(true);
		return;
	}
	if(!txtContent.validate()){
		alert('请输入正确的反馈内容!');
		txtContent.focus(true);
		return;
	}
	
	/** 保存 */	
	Ext.Ajax.request({
		url: link2,
		method: 'post',
		params: {id:id, tableId:tableId,
				createPerson:txtCreatePerson.getValue().trim(),createDate:txtCreateDate.getValue().format('Y-m-d'),content:txtContent.getValue().trim(), 
				remark:txtRemark.getValue().trim()},
		success: function(ajax){
			eval("responseObj="+ajax.responseText);
			if(responseObj.success == true){
				alert('保存成功');
				window.close(); 
			}else{
				alert('保存失败');
			}
		},
		failure: function(){
			alert('服务器忙,请稍后再试...');
		}
	})	
}

	
	
	
	
	