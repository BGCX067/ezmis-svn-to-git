var distsJxjz = $dictListAjax("JXJZ");
var distsJxxz = $dictListAjax("JXXZ");
var jhId = ""
var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在保存，请稍候..."
		});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在加载，请稍候..."
		});
// ************** 页面设计 ****************//
RightForm = function() {
	var form = this;
	var hdnId = new Ext.form.Hidden({
				name : 'id'
			})

	var hdnNodeId = new Ext.form.Hidden({
				name : 'jhId',
				value : jhId
			})

	// 报告名称
	var txtBgmc = new Ext.form.TextField({
				name : 'bgmc',
				fieldLabel : '报告名称',
				disabled : true,
				allowBlank : false,
				maxLength : 25,
				anchor : '90%'
			})

	// 检修性质
	var dsJxxz = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
							root : 'rows',
							id : 'id'
						}, ['key', 'value', 'id']),
				data : {
					rows : distsJxxz
				}
			})
	var cmbJxxz = new Ext.form.ComboBox({
				hiddenName : 'jxxz',
				store : dsJxxz,
				editable : false,
				fieldLabel : '检修性质',
				displayField : 'value',
				valueField : 'key',
				mode : 'local',
				disabled : true,
				triggerAction : 'all',
				typeAhead : true,
				typeAheadDelay : 2000,
				selectOnFocus : true,
				emptyText : '请选择检修性质',
				anchor : '90%'
			})

	// 所属机组
	var dsSsjz = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
							root : 'rows',
							id : 'id'
						}, ['key', 'value', 'id']),
				data : {
					rows : distsJxjz
				}

			})
	var cmbSsjz = new Ext.form.ComboBox({
				hiddenName : 'ssjz',
				store : dsSsjz,
				editable : false,
				fieldLabel : '所属机组',
				displayField : 'value',
				valueField : 'key',
				mode : 'local',
				disabled : true,
				triggerAction : 'all',
				typeAhead : true,
				typeAheadDelay : 2000,
				selectOnFocus : true,
				emptyText : '请选择机组',
				anchor : '90%'
			})

	// 负责人员
	var txtFzry = new Ext.form.TextArea({
				name : 'fzry',
				fieldLabel : '负责人员',
				maxLength : 250,
				disabled : true,
				anchor : '95%'
			})

	// 起始日期
	var dtQsrq = new Ext.form.DateField({
				name : 'qsrq',
				fieldLabel : '起始日期',
				format : 'Y-m-d',
				readOnly : true,
				disabled : true,
				anchor : '90%'
			});

	// 终止日期
	var dtJsrq = new Ext.form.DateField({
				name : 'jsrq',
				fieldLabel : '终止日期',
				format : 'Y-m-d',
				readOnly : true,
				disabled : true,
				anchor : '90%'
			});

	// 报告摘要
	var txtBgzy = new Ext.form.TextArea({
				name : 'bgzy',
				fieldLabel : '报告摘要',
				maxLength : 250,
				disabled : true,
				anchor : '95%'
			})

	// 验收意见
	var txtYsyj = new Ext.form.TextArea({
				name : 'ysyj',
				fieldLabel : '验收意见',
				maxLength : 250,
				disabled : true,
				anchor : '95%'
			})

	// 验收日期
	var dtYsrq = new Ext.form.DateField({
				name : 'ysrq',
				fieldLabel : '验收日期',
				format : 'Y-m-d',
				readOnly : true,
				disabled : true,
				anchor : '95%'
			});

	// 验收部门
	var txtYsbm = new Ext.form.TextField({
				name : 'ysbm',
				fieldLabel : '验收部门',
				maxLength : 16,
				disabled : true,
				anchor : '95%'
			})

	// 存在问题
	var txtCzwt = new Ext.form.TextArea({
				name : 'czwt',
				fieldLabel : '存在问题',
				maxLength : 250,
				disabled : true,
				anchor : '95%'
			})

	// 备注说明
	var txtBzsm = new Ext.form.TextArea({
				name : 'bzsm',
				fieldLabel : '备注说明',
				maxLength : 250,
				disabled : true,
				anchor : '95%'
			})

	// 报告附件
	var txtBgfj = new Ext.form.TextField({
				name : 'bgfjMc',
				fieldLabel : '报告附件',
				readOnly : true,
				disabled : true,
				anchor : '95%'
			})
	var downLoadBtnBgfj = new Ext.Button({
				text : '下载',
				disabled : true,
				handler : function() {
					downloadAttach()
				}
			});
	var uploadBtnBgfj = new Ext.ux.UploadDialog.TBBrowseButton({
				input_name : 'bgfj',
				text : '浏览...',
				disabled : true,
				handler : function onAddButtonFileSelected() {
					fileSelectorChanged(uploadBtnBgfj);
				},
				scope : this
			});

	RightForm.superclass.constructor.call(this, {
				frame : true,
				border : false,
				defaults : {
					bodyStyle : 'padding:2px'
				},
				region : 'center',
				labelWidth : 60,
				items : [{
							layout : 'column',
							border : false,
							labelSeparator : '：',
							items : [{
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [txtBgmc, hdnId, hdnNodeId]
									}, {
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [cmbJxxz]
									}, {
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [cmbSsjz]
									}, {
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [txtFzry]
									}, {
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [dtQsrq]
									}, {
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [dtJsrq]
									}, {
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [txtBgzy]
									}, {
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [txtYsyj]
									}, {
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [dtYsrq]
									}, {
										columnWidth : .33,
										layout : 'form',
										border : false,
										items : [txtYsbm]
									}, {
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [txtCzwt]
									}, {
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [txtBzsm]
									}, {
										columnWidth : .75,
										layout : 'form',
										border : false,
										items : [txtBgfj]
									}, {
										columnWidth : .1,
										layout : 'form',
										border : false,
										items : [uploadBtnBgfj]
									}, {
										columnWidth : .15,
										layout : 'form',
										border : false,
										items : [downLoadBtnBgfj]
									}]
						}]
			})

	this.initData = function() {
		if (hdnId.getValue() == null || hdnId.getValue() == '') {
			Ext.Ajax.request({
						url : link6,
						method : 'POST',
						params : {
							id : jhId
						},
						success : function(ajax) {
							var responseText = ajax.responseText;
							var obj = Ext.decode(responseText);
							var data = obj.data[0];
							cmbJxxz.setValue(data.jxxz);
							cmbSsjz.setValue(data.jz);
							dtQsrq.setValue(formatDate(new Date(data.qsrq['time']), "yyyy-MM-dd"));
							dtJsrq.setValue(formatDate(new Date(data.jsrq['time']), "yyyy-MM-dd"));
						},
						failure : function() {

						}
					})
		}
	}

	this.loadData = function load() {
		myMaskLoad.show();
		this.getForm().reset();
		Ext.Ajax.request({
					url : link2,
					method : 'POST',
					params : {
						jhId : jhId
					},
					success : function(ajax) {
						var responseText = ajax.responseText;
						var obj = Ext.decode(responseText);
						if (obj.success == true) {
							var data = obj.data;
							if (data) {
								hdnId.setValue(data.id);
								txtBgmc.setValue(data.bgmc);
								cmbJxxz.setValue(data.jxxz);
								cmbSsjz.setValue(data.ssjz);
								txtFzry.setValue(data.fzry);
								if (data.qsrq) {
									dtQsrq.setValue(formatDate(new Date(data.qsrq['time']), "yyyy-MM-dd"));
								}
								if (data.jsrq) {
									dtJsrq.setValue(formatDate(new Date(data.jsrq['time']), "yyyy-MM-dd"));
								}
								txtBgzy.setValue(data.bgzy);
								txtYsyj.setValue(data.ysyj);
								if (data.ysrq) {
									dtYsrq.setValue(formatDate(new Date(data.ysrq['time']), "yyyy-MM-dd"));
								}
								txtYsbm.setValue(data.ysbm);
								txtCzwt.setValue(data.czwt);
								txtBzsm.setValue(data.bzsm);
								txtBgfj.setValue(data.bgfjMc);
								downLoadBtnBgfj.setDisabled(false);
							}
							myMaskLoad.hide();
						}
					},
					failure : function() {

					}
				})
		hdnNodeId.setValue(jhId);
	}

	this.setFormDisabled = function(flag) {
		txtBgmc.setDisabled(flag);
		cmbJxxz.setDisabled(flag);
		cmbSsjz.setDisabled(flag);
		txtFzry.setDisabled(flag);
		dtQsrq.setDisabled(flag);
		dtJsrq.setDisabled(flag);
		txtBgzy.setDisabled(flag);
		txtYsyj.setDisabled(flag);
		dtYsrq.setDisabled(flag);
		txtYsbm.setDisabled(flag);
		txtCzwt.setDisabled(flag);
		txtBzsm.setDisabled(flag);
		txtBgfj.setDisabled(flag);
		downLoadBtnBgfj.setDisabled(!flag);
		uploadBtnBgfj.setDisabled(flag);
	}

	this.saveForm = function() { 
		if (txtBgmc.getValue() == null || txtBgmc.getValue() == '') {
			alert("报告名称不能为空");
			return;
		}
		if (!form.getForm().isValid()) {
			alert('表单验证不通过');
			return;
		}
		myMask.show();
		form.getForm().submit({
					url : link3,
					success : function(form, ajax) {
						myMask.hide();
						var responseText = ajax.response.responseText;
						var obj = Ext.decode(responseText);
						if (obj.success == true) {
							hdnId.setValue(obj.bgId);
							alert("保存成功");
						} else {
							alert(obj.msg)
						}
					}
				})
	}

	this.delSelected = function() {
		if (hdnId.getValue() == null || hdnId.getValue() == '') {
			return;
		}
		if (window.confirm("确认删除吗")) {
			Ext.Ajax.request({
						url : link5,
						success : function(ajax) {
							var responseText = ajax.responseText;
							var responseObject = Ext.util.JSON.decode(responseText);
							if (responseObject.success) {
								alert("删除成功");
								form.getForm().reset();
								form.initData();
							} else {
								alert(responseObject.msg);
							}
						},
						failure : function() {
							alert("提交失败");
						},
						method : 'POST',
						params : {
							ids : hdnId.getValue()
						}
					});
		}
	}

	// 文件上传控件选择文件
	// 文件选择
	function fileSelectorChanged(browse_btn) {
		var attachFile = browse_btn.getInputFile();
		// 链接
		var myUrl = attachFile.dom.value;
		var aa = myUrl.lastIndexOf("\\");
		// 链接名
		var attachName = myUrl.substring(aa + 1);

		var cc = myUrl.lastIndexOf(".");
		// 文档类型
		var attachType = myUrl.substring(cc + 1);

		var input_file = browse_btn.detachInputFile();
		input_file.dom.style.left = "-200000";
		input_file.appendTo(document.forms[0]);
		var myMask = new Ext.LoadMask(Ext.getBody(), {
					msg : "文件验证中，请稍候..."
				});
		myMask.show();
		validateFile(input_file, myMask, myUrl);
	}

	function validateFile(input_file, myMask, myUrl) {
		var link = contextPath + "/servlet/ValidateFileServlet";
		var myForm = document.forms[0];
		Ext.Ajax.request({
					url : link,
					method : 'POST',
					form : myForm,
					isUpload : true,
					success : function(ajax) {
						var responseText = ajax.responseText;
						var responseObject = Ext.util.JSON.decode(responseText);
						if (responseObject.success) {
							var data = responseObject.fdList;
							var maxsize = data.maxsize;
							myMask.hide();
							if (data.filesize > maxsize) {
								alert("上传文件最大为10M！");
								myForm.removeChild(myForm.lastChild);
								return;
							}
							txtBgfj.setValue(myUrl);
						}
					},
					failure : function() {
						alert('服务器忙，请稍候操作...');
					}
				});
	}

	/**
	 * 下载附件
	 */
	function downloadAttach() {
		var id = hdnId.getValue();
		var link = link4 + "?id=" + id;
		downloadFrame = document.createElement("iframe"); // 通过Iframe 的src
		// 属性调用下载文件的action方法无刷新的下载文件
		downloadFrame.id = "downloadFrame";
		downloadFrame.src = link;
		downloadFrame.style.display = "none";
		document.body.appendChild(downloadFrame);
	}
}
Ext.extend(RightForm, Ext.FormPanel, {

})
