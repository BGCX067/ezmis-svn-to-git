var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在保存，请稍候..."
		});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在加载，请稍候..."
		});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
			name : 'id',
			renderTo : 'id'
		})
var hdnNodeId = new Ext.form.Hidden({
			name : 'zyflId',
			renderTo : 'zyflId'
		})

// 设备编码
var txtSbbm = new Ext.form.TextField({
			name : 'sbbm',
			renderTo : 'divSbbm'
		})

// 设备名称
var txtSbmc = new Ext.form.TextField({
			name : 'sbmc',
			renderTo : 'divSbmc',
			allowBlank : false,
			readOnly : true,
			maxLength : 25,
			width : 120
		})

// 型式及规格
var txtXsjgg = new Ext.form.TextField({
			name : 'xsjgg',
			renderTo : 'divXsjgg',
			maxLength : 25,
			width : 160
		})

// 单位
var txtDw = new Ext.form.TextField({
			name : 'dw',
			renderTo : 'divDw',
			style : 'text-align:right',
			maxLength : 8,
			width : 160
		})

// 额定数量
var txtEdsl = new Ext.form.NumberField({
			name : 'edsl',
			renderTo : 'divEdsl',
			style : 'text-align:right',
			allowNegative : false,
			allowDecimals : false,
			maxValue : 999999,
			width : 160
		});

// 实际数量
var txtSjsl = new Ext.form.NumberField({
			name : 'sjsl',
			renderTo : 'divSjsl',
			style : 'text-align:right',
			allowNegative : false,
			allowDecimals : false,
			maxValue : 999999,
			width : 160
		});

// 预警数量
var txtYjsl = new Ext.form.NumberField({
			name : 'yjsl',
			renderTo : 'divYjsl',
			style : 'text-align:right',
			allowNegative : false,
			allowDecimals : false,
			maxValue : 999999,
			width : 160
		});

// 备注说明
var txtRemark = new Ext.form.TextArea({
			id : 'remark',
			name : 'remark',
			renderTo : 'divRemark',
			maxLength : 250,
			width : 410
		})

// *********** 功能处理 ***********//

function showSbmc() {
	var obj = {};
	var url = contextPath + "/jteap/sb/common/index.jsp";
	var result = showIFModule(url, "设备名称选择", "true", 800, 600, obj);
	if (typeof(result) == "undefined") {
		txtSbbm.setValue("");
		txtSbmc.setValue("");
		txtXsjgg.setValue("");
	} else {
		txtSbbm.setValue(result.sbbm);
		txtSbmc.setValue(result.sbmc);
		txtXsjgg.setValue(result.sbgg);
	}
	txtSbmc.blur();
}

// 初始化
function load() {
	if (id == null || id == '') {
		return;
	}
	myMaskLoad.show();
	Ext.Ajax.request({
				url : link5,
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
						txtSbmc.setValue(data.sbmc);
						txtXsjgg.setValue(data.xsjgg);
						txtDw.setValue(data.dw);
						txtEdsl.setValue(data.edsl);
						txtSjsl.setValue(data.sjsl);
						txtYjsl.setValue(data.yjsl);
						txtRemark.setValue(data.remark);
					}
				}
			})
}

// 保存
function save() {
	if (!txtSbmc.isValid()) {
		alert('设备名称验证不正确');
		return;
	}
	if (!txtXsjgg.isValid()) {
		alert('型式及规格验证不正确');
		return;
	}
	if (!txtDw.isValid()) {
		alert('单位验证不正确');
		return;
	}
	if (!txtEdsl.isValid()) {
		alert('额定数量验证不正确');
		return;
	}
	if (!txtSjsl.isValid()) {
		alert('实际数量验证不正确');
		return;
	}
	if (!txtYjsl.isValid()) {
		alert('预警数量验证不正确');
		return;
	}
	myMask.show();
	$('form').action = link6;
	$('form').target = "ftarget";
	$('form').method = 'POST';
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
				hdnId.setValue(result.id);
				alert('保存成功');
				window.returnValue = "true";
				window.close();
			} else {
				alert(result.msg);
			}
		}
	}
}