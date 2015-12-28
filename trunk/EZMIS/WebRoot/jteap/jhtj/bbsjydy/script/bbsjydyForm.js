var BbsjydyForm = function(id,bbindexid,isSql,strSql, isEdit) {
	// this.datapermid=datapermid;//ID
	// this.tablename=tablename;//表名
	// 权限名
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;
	var vnameCell = new Ext.form.TextField( {
		disabled : false,
		width : 190,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "英文名",
		name : "vname",
		readOnly : false,
		maskRe : filterName
	});
	var vnameinfoCell = new Ext.app.LabelPanel('英文名为必填');
	// 权限中文名
	var cvnameCell = new Ext.form.TextField( {
		disabled : false,
		width : 190,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "数据源中文名",
		name : "cvname",
		readOnly : false
	});
	var cvnameInfoCell = new Ext.app.LabelPanel('数据源中文名为必填');

	var sqlWhereCell = new Ext.form.TextArea( {
		name : "sqlstr",
		height : 298,
		width : 290,
		enableKeyEvents : true,
		value:strSql,
		hideLabel:true,
		allowBlank : false
	});

	sqlWhereCell.on("keyup", function(src, evt) {
		$("saveButton").disabled = true;
	});

	var isSaveButton = false;
	
	var queryGrid=new QueryGrid();

	var simpleForm = new Ext.FormPanel({
		labelAlign : 'left',
		buttonAlign : 'right',
		style : 'margin:2px',
		bodyStyle : 'padding:0px',
		waitMsgTarget : true,
		id : 'myForm',
		width : '100%',
		frame : true, // 圆角风格
		labelWidth : 70, // 标签宽度
		monitorValid : true, // 绑定验证
		items:[{
	          	layout:'column',
		        border:false,
		        labelWidth:100,					//标签宽度
		        labelSeparator:'：',
		        defaults:{
		        	blankText:'必填字段'
		        },
		        items:[{
				// 第一行布局
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [vnameCell]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [vnameinfoCell]
				}, {
					// 第一行布局
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [cvnameCell]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [cvnameInfoCell]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [sqlWhereCell]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [queryGrid]
				},{
					xtype : 'hidden',
					name : 'id',
					id : 'id',
					value : id
				},{
					xtype : 'hidden',
					name : 'hidSql',
					id : 'hidSql',
					value : id
				}]
		    }],buttons : [{
				id : 'proButton',
				text : '上一步',
				handler : function() {
					back();
				}
			}, {
				id : 'testButton',
				text : '测试',
				handler : function() {
					dynaCreate();
				}
			}, {
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
	
	
	
	
	
	/**
	 * 初始化
	 */
	function initTable(sql){
		//var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"数据查询中，请稍等"}); 
		//myMask.show();
		var cm;
		var param={};
		param.sql=sql;
		param.bbindexid=bbindexid;
		AjaxRequest_Sync(link13, param, function(req) {
			var responseText=req.responseText;	
 			var responseObject=Ext.util.JSON.decode(responseText);
 			if(responseObject.success){
 				var cols=responseObject.list;
 				var cm=buildCm(cols);
 				var ds=buildDs(cols,sql);
 				queryGrid.updateData(cm,ds);
 			}else{
 				alert("数据查询失败");
 			}
		});
	}
	//构建列模型对象
	function buildCm(cols){
		var tmpArray=new Array();
		//tmpArray.push(new Ext.grid.CheckboxSelectionModel());
		for(var i=0;i<cols.length;i++){
			var data=cols[i];
			for (var p in cols[i]) {
				var tmpCm={};
				//条件字段
				tmpCm={"id":data[p],"header":p,"width":60,"sortable":true,"dataIndex":data[p]};
				tmpArray.push(tmpCm);
			}
		}
		cm=new Ext.grid.ColumnModel(tmpArray);
		return cm;
	}
	//构建数据源对象
	function buildDs(cols,sql){
		var colArray=new Array();
		for(var i=0;i<cols.length;i++){
			var data=cols[i];
			for (var p in cols[i]) {
				var columncode=data[p];
				colArray.push(columncode);
			}
		}
		
		var ds = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: link14+"?sql="+sql+"&bbindexid="+bbindexid
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount'
	        }, colArray),
	        remoteSort: true	
	    });
		return ds;
	}


	/**
	 * 动态查询结果
	 */
	function dynaCreate() {
		//simpleForm.findById('dynagrid').getEl().dom.innerHTML = "";// 清空存放表格的容器
		var sql=sqlWhereCell.getValue();
		var param={};
		param.sql=sql;
		param.bbindexid=bbindexid;
		var star=sql.indexOf("[");
		var end=sql.indexOf("]");
		//sql语句中是否存在接口
		if(star>0 && end>0){
			//存在接口就给接口初始化默认值
			AjaxRequest_Sync(link15, param, function(req) {
				var responseText = req.responseText;
				var responseObj = Ext.decode(responseText);
				if (responseObj.success) {
					var data=responseObj.data;
					//弹出接口页面让用户选择
					var result=showModule(link16+"?initValue="+data,true,300,300);
					if(result!=undefined){
						param.sqlValue=result;
						//把接口替换成真实的值
						AjaxRequest_Sync(link17, param, function(ajax) {
							var resText = ajax.responseText;
							var resObj = Ext.decode(resText);
							if (resObj.success) {
								param.sql=resObj.sql;
								//sql语句是否合法
								AjaxRequest_Sync(link18, param, function(res) {
									var resT = res.responseText;
									var resO = Ext.decode(resT);
									if (resO.success) {
										$("hidSql").value=param.sql;
										createGrid(param.sql);
										isSaveButton=true;
										$("saveButton").disabled = false;
									}else{
										showErrorPage(param.sql);
										isSaveButton=false;
										//alert("请检查sql语句!");
									}
								});
							}else{
								alert("请检查sql语句!");
							}
						});
					}
				}
			});
		}else{
			//sql语句是否合法
			AjaxRequest_Sync(link18, param, function(req) {
				var responseText = req.responseText;
				var responseObj = Ext.decode(responseText);
				if (responseObj.success) {
					$("hidSql").value=param.sql;
					createGrid(param.sql);
					isSaveButton=true;
					$("saveButton").disabled = false;
				}else{
					isSaveButton=false;
					alert("请检查sql语句!");
				}
			});
		}
		
	}
	
	/**
	 * 显示错误提示
	 */
	function showErrorPage(sql){
		Ext.MessageBox.buttonText.ok="复制";
		Ext.Msg.show( {
			title : '错误提示',
			msg : 'sql语句',
			buttons : Ext.Msg.OK,
			multiline :true,
			prompt :true,
			value:sql,
			animEl : 'elId',
			maxWidth:300,
			minWidth:300,
			defaultTextHeight:100,
			closable :false,
			fn:function(btn, text){
			    if (btn == 'ok'){
			        // 处理文本值，把文本复制到剪切板
			    	window.clipboardData.setData('Text',text);
			    }
			}
		});
	}
	
	
	function createGrid(sql){
		initTable(sql);
		isSaveButton = true;// 可以提交
	}

	function back() {
		window.parent.document.all.formFrame.style.display = "inline";
		window.parent.document.all.next.style.display = "none";
	}

	function save() {
		if(!simpleForm.form.isValid()){
			alert('数据校验失败，请检查填写的数据格式是否正确');
			return;
		}
		if(isSaveButton){
			var param={};
			param.id=id;
			param.bbindexid=bbindexid;
			param.vname=vnameCell.getValue();
			param.cvname=cvnameCell.getValue();
			param.sqlstr=sqlWhereCell.getValue();
			param.testsql=$("hidSql").value;
			AjaxRequest_Sync(link3, param, function(req) {
				var responseText = req.responseText;
				var responseObj = Ext.decode(responseText);
				if (responseObj.success) {
					alert("保存成功!");
					window.returnValue = "true";
					window.close();
				}
			});
		}else{
			alert("请先测试sql语句");
		}
/**
			Ext.Msg.show( {
				title : '操作错误',
				msg : '请先测试sql语句的正确性!',
				buttons : Ext.Msg.OK,
				animEl : 'elId',
				icon : Ext.MessageBox.ERROR
			});*/
	}

	this.loadData = function() {
		// 更新的时候
		if (id != "") {
			Ext.Ajax.request( {
				url : link7 + "?id=" + id,
				method : 'post',
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					var data = obj.data[0];
					vnameCell.setValue(data.vname);
					vnameCell.setDisabled(true);
					cvnameCell.setValue(data.cvname);
					if(isSql==1){
						sqlWhereCell.setValue(data.sqlstr);
						//isSaveButton=true;
					}
				}
			});
		}
	}

	BbsjydyForm.superclass.constructor.call(this, {
		width : '100%',
		height : 425,
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

Ext.extend(BbsjydyForm, Ext.Panel, {});
