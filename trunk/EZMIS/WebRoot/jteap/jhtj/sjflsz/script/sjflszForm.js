var SjflszForm = function(parentId,id,isEdit) {
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;
	
	var kidCell=new Ext.app.UniqueTextField( {
						id : 'kid',
						name : 'kid',
						fieldLabel : '分类编码',
						allowBlank : false,
						maxLength : 20,
						width : 150,
						notUniqueText : '该名称称已被使用，请使用其他名称',
						url : link15,
						regex:filterName
					});
	
	var kidInfo=new Ext.app.LabelPanel('请填写分类编码(大写字母加数字)');
	
	var knameCell=new Ext.form.TextField( {
		disabled : false,
		width : 150,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "分类名称",
		name : "kname",
		readOnly : false
	});
	
	var knameInfo=new Ext.app.LabelPanel('请填写分类名称');

	
	var dict_1=$dictList("FLBS");
	var flflagds = new Ext.data.Store( {
		data: {rows:dict_1},
		reader : new Ext.data.JsonReader( {
			root : 'rows',
			id : 'id'
		}, ['key', 'value', 'id'])
	});
	
	// 
	var flflagCell = new Ext.form.ComboBox( {
		hiddenName : 'flflag',// 真正接受的名字
		store : flflagds,// 数据源
		width : 150,
		fieldLabel : '分类标志',
		displayField : 'key',// 数据显示列名
		valueField : 'value',
		mode : 'local',// 默认以'remote'作为数据源
		triggerAction : 'all',// 单击下拉按钮时激发事件
		typeAhead : true,// 自动完成功能
		editable :false,
		selectOnFocus : true,
		emptyText : '选择分类标志'
	});
	
	
	flflagCell.on("select",function(combo,record,index){
		var val=combo.getValue();
		var jzs=document.getElementsByName("jz");
		if(val==0){
			for(var i=0;i<jzs.length;i++){
				jzs[i].checked=false;
				jzs[i].disabled=true;
			}
		}else{
			for(var i=0;i<jzs.length;i++){
				jzs[i].disabled=false;
			}
		}
	}) ;
	
	var sortnoName = /^\s*[0-9]{1,4}\s*$/;
	var sortnoCell=new Ext.form.TextField( {
		disabled : false,
		width : 30,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "排序",
		name : "sortno",
		readOnly : false,
		regex:sortnoName
	});
	

	var simpleForm = new Ext.FormPanel( {
		labelAlign : 'left',
		buttonAlign : 'right',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		id : 'myForm',
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 80, // 标签宽度
		monitorValid : true, // 绑定验证
		items : [{
			frame : true,
			items : [{
				layout : 'column',
				border : false,
				labelSeparator : ':',
				defaults : {
					blankText : '必填字段'
				},
				items : [ {
					// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [kidCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [kidInfo]
					}, {
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [knameCell]
					},{
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [knameInfo]
					}, {
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [flflagCell]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						html:'<div id="jzCheck" style="width:500px;margin:2px;"><div>'
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						items : [sortnoCell]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : id
					}]
			}]	
		}],
		buttons : [{
			id : 'saveButton',
			formBind : true,
			text : '保存',
			handler : function() {
				save();
			}
		}, {
			text : '取消',
			handler : function() {
				window.close();
			}
		}]
	});
	
	function loadJz(){
		var result="";
		var dict_2=$dictList("JZMC");
		for(var i=0;i<dict_2.length;i++){
			result=result+"<input type='checkbox' name='jz' value='"+dict_2[i].key+"_"+dict_2[i].value+"'>"+dict_2[i].key+"&nbsp;&nbsp;";
		}
		$("jzCheck").innerHTML="所属机组:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+result;
	}

	function save() {
		//if(!simpleForm.form.isValid()){
		//	alert('数据校验失败，请检查填写的数据格式是否正确');
		//	return;
		//}
		
		var param={};
		param.id=$("id").value;
		param.kid=$("kid").value;
		param.kname=$("kname").value;
		param.flflag=$("flflag").value;
		param.sortno=$("sortno").value;
		if(parentId!=""){
			param.parentId=parentId;
		}
		var jzcode="";
		var jzs=document.getElementsByName("jz");
		for(var i=0;i<jzs.length;i++){
			if(jzs[i].checked){
				jzcode=jzcode+jzs[i].value+",";
			}
		}
		if(jzcode!=""){
			jzcode=jzcode.substring(0,jzcode.length-1);
			param.jzcode=jzcode;
		}
		AjaxRequest_Sync(link3,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					alert("保存成功");
					window.returnValue = "true";
					window.close();
				}
		});
	}

	//加载数据
	this.loadData = function() {
		loadJz();
		if(id!=""){
			var param={};
			param.id=id;
			AjaxRequest_Sync(link8,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					var data=responseObj.data[0];
					$("kid").value=data.kid;
					$("kname").value=data.kname;
					flflagCell.setValue(data.flflag);
					$("sortno").value=data.sortno;
					var jzcode=data.jzcode;
					var isDisabled=false;
					if(data.flflag==0){
						isDisabled=true;
					}
					if(jzcode!=undefined){
						var jzs=jzcode.split(",");
						var jz=document.getElementsByName("jz");
						for(var j=0;j<jz.length;j++){
							for(var i=0;i<jzs.length;i++){
								if(jz[j].value==jzs[i]){
									jz[j].checked=true;
								}
							}
						}
					}else{
						if(isDisabled){
							var jz=document.getElementsByName("jz");
							for(var j=0;j<jz.length;j++){
								jz[j].disabled=true;
							}
						}
					}
					flflagCell.setDisabled(true);
					kidCell.setDisabled(true);
				}
			});
		}
	}

	SjflszForm.superclass.constructor.call(this, {
		width : '100%',
		height : 420,
		modal : true,
		autoScroll : true,
		layout : 'column',
		plain : true,
		draggable : false,
		resizable : false,
		bodyStyle : 'padding:1px;',
		items : [{
			border : false,
			columnWidth : 1,
			layout : 'form',
			items : simpleForm
		}]
	});

}

Ext.extend(SjflszForm, Ext.Panel, {});
