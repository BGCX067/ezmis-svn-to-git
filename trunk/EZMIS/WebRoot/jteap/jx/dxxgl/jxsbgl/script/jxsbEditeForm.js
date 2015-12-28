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
			renderTo : 'id'
		})

var hdnZyId = new Ext.form.Hidden({
			name : 'zyId',
			renderTo : 'zyId'
		})

// 设备名称
var txtSbmc = new Ext.form.TextField({
			name : 'sbmc',
			renderTo : 'divSbmc',
			allowBlank : false,
			maxLength : 50,
			width : 200
		})

// 检修周期
//var txtJxzq = new Ext.form.NumberField({
var txtJxzq = new Ext.form.TextField({
			name : 'jxzq',
			renderTo : 'divJxzq',
			style : 'text-align:right',
//			allowNegative : false,
//			allowDecimals : false,
//			maxValue : 99,
//			maxLength : 2,
			width : 200
		})

// 项目级别
var txtXmjb = new Ext.form.NumberField({
			name : 'xmjb',
			renderTo : 'divXmjb',
			allowBlank : false,
			maxLength : 1,
			width : 200
		})

// 项目序号
var txtXmxh = new Ext.form.TextField({
			name : 'xmxh',
			renderTo : 'divXmxh',
			regex : /^[0-9.]+$/,
			regexText : '只能输入.和数字',
			maxLength : 10,
			width : 200
		})

// *********** 功能处理 ***********//
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
						txtSbmc.setValue(data.sbmc);
						txtXmjb.setValue(data.xmjb);
						txtJxzq.setValue(data.jxzq);
						txtXmxh.setValue(data.xmxh);
					}
				},
				failure : function() {
				}
			})
}

// 保存
function save() {
	if (!txtSbmc.isValid()) {
		alert('设备名称验证不正确');
		return;
	}
	if (!txtJxzq.isValid()) {
		alert('检修周期验证不正确');
		return;
	}
	if (!txtXmjb.isValid()) {
		alert('项目级别验证不正确');
		return;
	}
	if (!txtXmxh.isValid()) {
		alert('项目序号验证不正确');
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