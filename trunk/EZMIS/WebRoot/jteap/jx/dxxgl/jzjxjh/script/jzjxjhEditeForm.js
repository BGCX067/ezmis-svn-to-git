var distsJxjz = $dictListAjax("JXJZ");
var distsJxxz = $dictListAjax("JXXZ");
var timer = null;
var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在保存，请稍候..."
		});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在加载，请稍候..."
		});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
			name : 'id',
			renderTo : 'jhid'
		})

// 计划名称
var txtJhmc = new Ext.form.TextField({
			name : 'jhmc',
			renderTo : 'divJhmc',
			allowBlank : false,
			maxLength : 25,
			width : 200
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
			renderTo : 'divJxxz',
			store : dsJxxz,
			editable : false,
			displayField : 'value',
			valueField : 'key',
			mode : 'local',
			allowBlank : false,
			triggerAction : 'all',
			typeAhead : true,
			typeAheadDelay : 2000,
			selectOnFocus : true,
			emptyText : '请选择检修性质',
			width : 200
		})

// 机组
var dsJz = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						root : 'rows',
						id : 'id'
					}, ['key', 'value', 'id']),
			data : {
				rows : distsJxjz
			}

		})
var cmbJz = new Ext.form.ComboBox({
			hiddenName : 'jz',
			renderTo : 'divJz',
			store : dsJz,
			editable : false,
			displayField : 'value',
			valueField : 'key',
			mode : 'local',
			allowBlank : false,
			triggerAction : 'all',
			typeAhead : true,
			typeAheadDelay : 2000,
			selectOnFocus : true,
			emptyText : '请选择机组',
			width : 200
		})

// 起始日期
var dtQsrq = new Ext.form.DateField({
			name : 'qsrq',
			renderTo : 'divQsrq',
			format : 'Y-m-d',
			readOnly : true,
			allowBlank : false,
			width : 200
		});

// 结束日期
var dtJsrq = new Ext.form.DateField({
			name : 'jsrq',
			renderTo : 'divJsrq',
			format : 'Y-m-d',
			readOnly : true,
			allowBlank : false,
			width : 200
		});

// 人工费用
var nmRgfy = new Ext.form.NumberField({
			name : 'rgfy',
			renderTo : 'divRgfy',
			style : 'text-align:right',
			allowNegative : false,
			allowDecimals : true,
			allowBlank : false,
			decimalPrecision : 2,
			maxValue : 99999999.99,
			maxLength : 11,
			width : 180,
			listeners : {
				blur : function(t) {
					nmFyhj.setValue(nmRgfy.getValue() + nmClfy.getValue())
				}
			}
		})

// 材料费用
var nmClfy = new Ext.form.NumberField({
			name : 'clfy',
			renderTo : 'divClfy',
			style : 'text-align:right',
			allowNegative : false,
			allowDecimals : true,
			decimalPrecision : 2,
			maxValue : 99999999.99,
			allowBlank : false,
			maxLength : 11,
			width : 180,
			listeners : {
				blur : function(t) {
					nmFyhj.setValue(nmRgfy.getValue() + nmClfy.getValue())
				}
			}
		})

// 费用合计
var nmFyhj = new Ext.form.NumberField({
			name : 'fyhj',
			renderTo : 'divFyhj',
			style : 'text-align:right',
			readOnly : true,
			decimalPrecision : 2,
			width : 180
		})

// 内容概要
var txtNrgy = new Ext.form.TextArea({
			name : 'nrgy',
			renderTo : 'divNrgy',
			maxLength : 500,
			width : 473
		})

// 检修任务书
var txtJxrws = new Ext.form.TextField({
			name : 'jxrwsMc',
			renderTo : 'divJxrwsMc',
			fieldLabel : '检修任务书',
			readOnly : true,
			width : 280
		})
var uploadBtnJxrws = new Ext.ux.UploadDialog.TBBrowseButton({
			input_name : 'jxrws',
			renderTo : 'divJxrws',
			text : '浏览...',
			handler : function onAddButtonFileSelected() {
				fileSelectorChanged(uploadBtnJxrws, txtJxrws);
			},
			scope : this
		});

// 检修项目
var txtJxxm = new Ext.form.TextField({
			name : 'jxxmMc',
			renderTo : 'divJxxmMc',
			fieldLabel : '检修项目',
			readOnly : true,
			width : 280
		})
var uploadBtnJxxm = new Ext.ux.UploadDialog.TBBrowseButton({
			input_name : 'jxxm',
			text : '浏览...',
			renderTo : 'divJxxm',
			handler : function onAddButtonFileSelected() {
				fileSelectorChanged(uploadBtnJxxm, txtJxxm);
			},
			scope : this
		});

// 检修技术协议书
var txtJxjsxy = new Ext.form.TextField({
			name : 'jxjsxyMc',
			renderTo : 'divJxjsxyMc',
			fieldLabel : '检修技术协议书',
			readOnly : true,
			width : 280
		})
var uploadBtnJxjsxy = new Ext.ux.UploadDialog.TBBrowseButton({
			input_name : 'jxjsxy',
			text : '浏览...',
			renderTo : 'divJxjsxy',
			handler : function onAddButtonFileSelected() {
				fileSelectorChanged(uploadBtnJxjsxy, txtJxjsxy);
			},
			scope : this
		});

// 其他文件1
var txtQt1 = new Ext.form.TextField({
			name : 'qtfjMc1',
			renderTo : 'divQtfjMc1',
			fieldLabel : '其他附件1',
			readOnly : true,
			width : 280
		})
var uploadBtnQt1 = new Ext.ux.UploadDialog.TBBrowseButton({
			input_name : 'qtfj1',
			text : '浏览...',
			renderTo : 'divQtfj1',
			handler : function onAddButtonFileSelected() {
				fileSelectorChanged(uploadBtnQt1, txtQt1);
			},
			scope : this
		});

// 其他文件2
var txtQt2 = new Ext.form.TextField({
			name : 'qtfjMc2',
			renderTo : 'divQtfjMc2',
			fieldLabel : '其他附件2',
			readOnly : true,
			width : 280
		})
var uploadBtnQt2 = new Ext.ux.UploadDialog.TBBrowseButton({
			input_name : 'qtfj2',
			text : '浏览...',
			renderTo : 'divQtfj2',
			handler : function onAddButtonFileSelected() {
				fileSelectorChanged(uploadBtnQt2, txtQt2);
			},
			scope : this
		});

// *********** 功能处理 ***********//

// 文件上传控件选择文件
// 文件选择
function fileSelectorChanged(browse_btn, obj) {

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
	validateFile(input_file, myMask, myUrl, obj);
}

function validateFile(input_file, myMask, myUrl, obj) {
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
						obj.setValue(myUrl);
					}
				},
				failure : function() {
					alert('服务器忙，请稍候操作...');
				}
			});
}

// 初始化
function load() {
	if (id == null || id == '') {
		return;
	}
	myMaskLoad.show();
	Ext.Ajax.request({
				url : link3,
				method : 'POST',
				params : {
					id : id
				},
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					myMaskLoad.hide();
					if (obj.success) {
						var data = obj.data[0];
						txtJhmc.setValue(data.jhmc);
						cmbJxxz.setValue(data.jxxz);
						cmbJz.setValue(data.jz);
						dtQsrq.setValue(formatDate(new Date(data.qsrq['time']), "yyyy-MM-dd"));
						dtJsrq.setValue(formatDate(new Date(data.jsrq['time']), "yyyy-MM-dd"));
						nmRgfy.setValue(data.rgfy);
						nmClfy.setValue(data.clfy);
						nmFyhj.setValue(data.fyhj);
						txtNrgy.setValue(data.nrgy);
						txtJxrws.setValue(data.jxrwsMc);
						txtJxxm.setValue(data.jxxmMc);
						txtJxjsxy.setValue(data.jxjsxyMc);
						txtQt1.setValue(data.qtfjMc1);
						txtQt2.setValue(data.qtfjMc2);
					}
				},
				failure : function() {

				}
			})
}
// 保存
function save() {
	if (!txtJhmc.isValid()) {
		alert('计划名称验证不正确');
		return;
	}
	if (!cmbJxxz.isValid()) {
		alert('检修性质不能为空');
		return;
	}
	if (!cmbJz.isValid()) {
		alert('机组不能为空');
		return;
	}
	if (!dtQsrq.isValid()) {
		alert('起始日期不能为空');
		return;
	}
	if (!dtJsrq.isValid()) {
		alert('结束日期不能为空');
		return;
	}
	if (dtJsrq.getValue() < dtQsrq.getValue()) {
		alert('结束日期不能小于起始日期');
		return;
	}
	if (formatDate(new Date(dtJsrq.getValue()), "yyyy") != formatDate(new Date(dtQsrq.getValue()), "yyyy")) {
		alert("检修计划不能跨年");
		return;
	}
	if (!nmRgfy.isValid()) {
		alert('人工费用验证不正确');
		return;
	}
	if (!nmClfy.isValid()) {
		alert('材料费用验证不正确');
		return;
	}
	if (txtJxrws.getValue() == null || txtJxrws.getValue() == '') {
		alert('必须上传检修任务书');
		return;
	}
	if (txtJxxm.getValue() == null || txtJxxm.getValue() == '') {
		alert('必须上传检修项目');
		return;
	}
	if (txtJxjsxy.getValue() == null || txtJxjsxy.getValue() == '') {
		alert('必须上传检修技术协议书');
		return;
	}
	myMask.show();
	$('form').action = link4;
	$('form').target = "ftarget";
	$("form").submit();
	timer = window.setInterval("monitor()", 1000);
}

function monitor() {
	var iFrameTarget = $('ftarget')
	if (iFrameTarget.readyState == 'complete') {
		var iFrameCnt = $ifContent(iFrameTarget);
		if (iFrameCnt != null && iFrameCnt != '') {
			window.clearInterval(timer);
			eval("var result =" + (iFrameCnt) + "");
			myMask.hide();
			if (result.success == true) {
				alert('保存成功');
				window.returnValue = "true";
				window.close();
			} else {
				alert(result.msg);
			}
		}
	}
}