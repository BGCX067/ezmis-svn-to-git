var ComputeForm = function(id,isEdit) {
	var cexpCell = new Ext.form.TextArea( {
		id : 'cexp',
		name : 'cexp',
		fieldLabel : '计算公式',
		width : 195,
		readOnly : false,
		allowBlank : false,
		height:330
		//anchor : '93%'
	});
	
	cexpCell.on("focus",function(field){
		$("saveButton").disabled =true;
	}); 
	
	var filterName=/^\s*[0-9]{1,6}\s*$/;
	
	var corderCell=new Ext.form.TextField( {
		width : 195,
		maxLength : 50,
		fieldLabel : "计算顺序",
		name : "corder",
		allowBlank : false,
		readOnly : false,
		regex:filterName
	});
	
	var computeGrid=new RightGrid(kid);
	
	

	var simpleForm = new Ext.FormPanel( {
		labelAlign : 'left',
		buttonAlign : 'right',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		id : 'myForm',
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 70, // 标签宽度
		//monitorValid : true, // 绑定验证
		items : [{
			frame : false,
			items : [{
				layout : 'column',
				border : false,
				labelSeparator : ':',
				defaults : {
					blankText : '必填字段'
				},
				items : [{
					// 第一行布局
						columnWidth : .6,
						layout : 'form',
						border : false,
						items : [computeGrid]
					},{
					// 第一行布局
						columnWidth : .4,
						layout : 'column',
						border : false,
						items : [{
							columnWidth : 1,
							layout : 'form',
							border : false,
							items : [cexpCell]
						},{
							columnWidth : 1,
							layout : 'form',
							border : false,
							items : [corderCell]
						}]
					},{
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : id
					},{
						xtype : 'hidden',
						name : 'sid',
						id : 'sid',
						value : id
					}]
			}]	
		}],
		buttons : [{
				id : 'testButton',
				text : '测试',
				handler : function() {
					test();
				}
			},{
				id : 'preButton',
				text : '上一步',
				handler : function() {
					back();
				}
			},{
			id : 'saveButton',
			//formBind : true,
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
	
	
	

	
	function test(){
		if(!simpleForm.form.isValid()){
			alert('数据校验失败，请检查填写的数据格式是否正确');
			return;
		}
		var param={};
		param.kid=kid;
		param.cexp=cexpCell.getValue();
		AjaxRequest_Sync(link17,param,function(request){
			var responseText = request.responseText;
			var responseObj = responseText.evalJSON();
			if(responseObj.success){
				alert("公式正确!");
				$("saveButton").disabled =false;
			}else{
				alert("公式错误!");
				$("saveButton").disabled =true;
			}
		});
	}

	
	function back() {
		window.parent.document.all.formFrame.style.display = "inline";
		window.parent.document.all.next.style.display = "none";
	}

	function save() {
		var param=packFormValue();
		AjaxRequest_Sync(link8,param,function(request){
			var responseText = request.responseText;
			var responseObj = responseText.evalJSON();
			if(responseObj.success){
				AjaxRequest_Sync(link12,param,function(res){
					var resText = res.responseText;
					var resObj = resText.evalJSON();
					if(resObj.success){
						alert("保存成功!");
						window.returnValue = "true";
						window.close();
					}else{
						alert("创建表结构失败!");
					}
				});
			}
		});
	}
	
	function packFormValue(){
		//上一页的数据
		var param={};
		param.id=id;
		param.iname=iname;
		param.iorder=iorder;
		param.dw=dw;
		param.sjlx=sjlx;
		if(cd!=""){
			param.cd=cd;
		}
		if(xsw!=""){
			param.xsw=xsw;
		}
		if(id==""){
			param.kid=kid;
			param.item=item;
			param.itype=itype;
		}
		param.qsfs=qsfs;
		param.isvisible=isvisible;
		//当前页的数据
		param.cexp=cexpCell.getValue();
		param.corder=corderCell.getValue();
		//需要清空的数据
		param.dname="";
		param.dfunId="";
		param.sid="";
		param.fname="";
		param.vname="";
		return param;
	}

	//加载数据
	this.loadData = function() {
		//$("testButton").disabled =true;
		if(id!=""){
			var param={};
			param.id=id;
			AjaxRequest_Sync(link7,param,function(request){
				var responseText = request.responseText;
				var responseObj = responseText.evalJSON();
				if(responseObj.success){
					var data=responseObj.data[0];
					kid=data.kid;
					cexpCell.setValue(data.cexp);
					corderCell.setValue(data.corder);
					computeGrid.changeToListDS(link4+"?kid="+kid);
					computeGrid.getStore().reload();
				}
			});
		}else{
			computeGrid.changeToListDS(link4+"?kid="+kid);
			computeGrid.getStore().reload();
			$("saveButton").disabled =true;
		}
	}
	
	
	this.setCexpValue = function(val){
		cexpCell.setValue(cexpCell.getValue()+val);
	}
	
	this.setSaveButtonDisabled=function(bool){
		$("saveButton").disabled =bool;
	}

	ComputeForm.superclass.constructor.call(this, {
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

Ext.extend(ComputeForm, Ext.Panel, {});
