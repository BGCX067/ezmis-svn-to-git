var DatapermForm = function(datapermid, tablename, isEdit) {
	// this.datapermid=datapermid;//ID
	// this.tablename=tablename;//表名
	// 权限名
	var filterName = /^\s*[A-Z0-9_]{1,30}\s*$/;
	var datapermCell = new Ext.form.TextField( {
		disabled : false,
		width : 190,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "权限名",
		name : "datapermname",
		readOnly : false,
		maskRe : filterName
	});
	var dataperminfoCell = new Ext.app.LabelPanel('权限名为必填,字母大写');
	// 权限中文名
	var datapermCnameCell = new Ext.form.TextField( {
		disabled : false,
		width : 190,
		maxLength : 50,
		allowBlank : false,
		fieldLabel : "权限中文名",
		name : "datapermcname",
		readOnly : false
	});
	var datapermCnameInfoCell = new Ext.app.LabelPanel('权限中文名为必填');

	this.defaultSql = 'select * from ' + tablename;

	var sqlCell = new Ext.form.TextField( {
		disabled : true,
		width : 250,
		maxLength : 330,
		fieldLabel : "查询语句",
		name : "sql",
		readOnly : false,
		value : this.defaultSql
	});

	var sqlWhereCell = new Ext.form.TextArea( {
		name : "qualification",
		height : 250,
		width : 245,
		fieldLabel : "sql条件",
		enableKeyEvents : true
	});

	sqlWhereCell.on("keyup", function(src, evt) {
		isSaveButton = false;
	});

	// 这里主要实现一个从后台获取数据列，然后动态添加到ColumnModel中，再也不用手动配置的方式
	function ready() {
		// 声明函数变量
		var data;
		var falg = 1;
		var firstVar = "";
		// 动态添加列，这是关键代码
		var addColumn = function() {
			this.fields = '';
			this.columns = '';
			this.addColumns = function(name, caption) {
				if (this.fields.length > 0) {
					this.fields += ',';
				}
				if (this.columns.length > 0) {
					this.columns += ',';
				}
				this.fields += '{name:"' + name + '"}';
				this.columns += '{header:"' + caption + '",dataIndex:"' + name
						+ '",width:100,sortable:true}';
				if (falg == 2) {
					firstVar = name;
				}
				falg++;
			};
		};
		// 从服务器端获取列，然后动态添加到ColumnModel中
		var params = tablename;
		var myAjax = new Ajax.Request(link10, {
			method : 'post',
			parameters : {
				'tablename' : params
			},
			asynchronous : true,// 同步调用
			onComplete : function(req) {
				var responseText = req.responseText;
				var responseObj = responseText.evalJSON();
				if (responseObj.success == true) {
					data = new addColumn();
					// alert(responseObj.list.length);
					for (var i = 0;i < responseObj.list.length; i++) {
						for (var p in responseObj.list[i]) {
							data.addColumns(p, p);
						}
					}
				}
			// 动态生成GridPanel
				makeGrid();
			},
			onFailure : function(e) {
				alert("验证公式失败：" + e);
			}
	}	);

		// 动态生成GridPanel
		var makeGrid = function() {
			var cm = new Ext.grid.ColumnModel(eval('([' + data.columns + '])'));
			// cm.defaultSortable = true;
			var fields = eval('([' + data.fields + '])');
			var sqlparams = $("sql").value;
			var whereparams = $("qualification").value;
			var newStore = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : link11,
					method:'post'
				}),
				reader : new Ext.data.JsonReader( {
					totalProperty : "totalCount",
					root : "list",
					fields : fields
				}),
				remoteSort : true
			});

			newStore.load( {
				params : {
					sql : sqlparams,
					where : whereparams
				},
				callback : function(r, options, success) {
					if(!success){	
						alert("结果出错!");
						isSaveButton=false;
					}
				}
			});
			// newStore.load({params:{start:0,limit:9}});
			/*
			 * var pagingBar = new Ext.PagingToolbar ({ displayInfo:true,
			 * emptyMsg:"没有数据显示", displayMsg:"显示从{0}条数据到{1}条数据，共{2}条数据",
			 * store:newStore, pageSize:9 });
			 */

			gridPanel = new Ext.grid.GridPanel( {
				// title:"动态生成ColumnModel",
				cm : cm,
				id : "grid_panel",
				renderTo : 'dynagrid',
				store : newStore,
				frame : true,
				// border:true,
				// layout:"fit",
				// pageSize:16,
				autoScroll : true,
				autoWidth : true,
				height : 280,
				width : 315
			// autoExpandColumn:true
			// viewConfig:{forceFit:true}
			// bbar:pagingBar
			});
		};
	}

	var isSaveButton = false;

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
						items : [datapermCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [dataperminfoCell]
					}, {
						// 第一行布局
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [datapermCnameCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						items : [datapermCnameInfoCell]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						labelSeparator : ':',
						// hideLabels : true,
						items : [ {
							layout : 'column',
							border : true,
							items : [{
								columnWidth : 1,
								layout : 'form',
								border : false,
								labelSeparator : ':',
								items : [sqlCell]
							}]
						}, {
							layout : 'column',
							border : false,
							labelSeparator : ':',
							items : [{
								columnWidth : 1,
								layout : 'form',
								border : false,
								labelSeparator : ':',
								items : [sqlWhereCell]
							}]
						}]
					}, {
						columnWidth : .5,
						layout : 'form',
						border : false,
						labelWidth : 0,
						labelSeparator : '',
						hideLabels : true,
						items : [{
							id : 'dynagrid'
						}]
					}, {
						xtype : 'hidden',
						name : 'id',
						id : 'id',
						value : datapermid
					}, {
						xtype : 'hidden',
						name : 'tablename',
						id : 'tablename',
						value : tablename
					}]
			}]
		}],
		buttons : [ {
			id : 'proButton',
			text : '上一步',
			handler : function() {
				back();
			}
		}, {
			id : 'proButton',
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
	 * 动态查询结果
	 */
	function dynaCreate() {
		simpleForm.findById('dynagrid').getEl().dom.innerHTML = "";// 清空存放表格的容器
		ready();// 开始创建
		isSaveButton = true;// 可以提交
	}

	function back() {
		window.parent.document.all.formFrame.style.display = "inline";
		window.parent.document.all.next.style.display = "none";
	}

	function save() {
		// 点了测试按钮了
		if (isSaveButton) {
			// 保存的时候
			if (datapermid == "" || undefined == datapermid) {
				var name = $("datapermname").value;
				Ext.Ajax.request( {
					url : link12,
					method : 'post',
					params : {
						datapermname : '' + name + ''
					},
					success : function(ajax) {
						var responseT = ajax.responseText;
						var obj = responseT.evalJSON();
						if (obj.success) {
							saveOrUpdate();
						} else {
							Ext.Msg.alert("Status", "权限名重复");
						}
					}
				});
			} else {
				saveOrUpdate();
			}
		} else {
			Ext.Msg.show( {
				title : '操作错误',
				msg : '请先测试sql语句的正确性!',
				buttons : Ext.Msg.OK,
				animEl : 'elId',
				icon : Ext.MessageBox.ERROR
			});
		}
	}

	/**
	 * 保存或更新
	 */
	function saveOrUpdate() {
		var param = {};
		param.datapermname = $("datapermname").value;
		param.datapermcname = $("datapermcname").value;
		param.sql = $("sql").value;
		param.qualification = $("qualification").value;
		param.tablename = $("tablename").value;
		if (undefined == datapermid) {
			param.id = "";
		} else {
			param.id = datapermid;
		}
		//param.charset = true;

		// 同步请求数据
		AjaxRequest_Sync(link13, param, function(req) {
			var responseText = req.responseText;
			var responseObj = Ext.decode(responseText);
			if (responseObj.success) {
				window.returnValue = "true";
				window.close();
			} else {
				Ext.MessageBox.alert('Status', responseObj.msg);
			}
		});
	}

	this.loadData = function() {
		// 更新的时候
		if (datapermid != "") {
			// simpleForm.load({url:link14+"?id="+datapermid,method:'POST',waitMsg:'等待加载数据'});
			Ext.Ajax.request( {
				url : link14 + "?id=" + datapermid,
				method : 'post',
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					var data = obj.data[0];
					// 如果选中的表改变时相应的条件就不增加到条件框中
					if (data.tablename == tablename) {
						$("qualification").value = data.qualification;
					}
					$("datapermname").value = data.datapermname;
					$("datapermname").disabled = true;
					$("datapermcname").value = data.datapermcname;
				}
			});
		}
		// simpleForm.load({url:link6+"?id="+tableId,method:'GET',waitMsg:'等待加载数据'});
	}

	DatapermForm.superclass.constructor.call(this, {
		width : '98%',
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

Ext.extend(DatapermForm, Ext.Panel, {});
