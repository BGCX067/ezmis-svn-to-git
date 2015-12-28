var distsJxxm_Sszy = $dictListAjax("JXXM_SSZY");
var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在保存，请稍候..."
		});
var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在加载，请稍候..."
		});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
			name : 'id',
			renderTo : 'jxxmid'
		})
var hdnNodeId = new Ext.form.Hidden({
			name : 'jhId',
			renderTo : 'jhId'
		})

// 所属专业
var dsSszy = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						root : 'rows',
						id : 'id'
					}, ['key', 'value', 'id']),
			data : {
				rows : distsJxxm_Sszy
			}
		})
var cmbSszy = new Ext.form.ComboBox({
			hiddenName : 'sszy',
			renderTo : 'divSszy',
			store : dsSszy,
			editable : false,
			displayField : 'value',
			valueField : 'key',
			mode : 'local',
			allowBlank : false,
			triggerAction : 'all',
			typeAhead : true,
			typeAheadDelay : 2000,
			selectOnFocus : true,
			emptyText : '请选择所属专业',
			width : 200
		})

// 项目名称
var txtXmmc = new Ext.form.TextArea({
			name : 'xmmc',
			renderTo : 'divXmmc',
			allowBlank : false,
			maxLength : 100,
			height : 50,
			width : 474
		})

// 记录人
var txtJlr = new Ext.form.TextField({
			name : 'jlr',
			renderTo : 'divJlr',
			allowBlank : false,
			maxLength : 16,
			width : 200
		})
		
// 记录时间
var dtJlsj = new Ext.form.DateField({
			name : 'jlsj',
			renderTo : 'divJlsj',
			format : 'Y-m-d H:i:s',
			menu:new DatetimeMenu(),
			readOnly : true,
			allowBlank : false,
			width : 200
		});

// 备注说明
var txtRemark = new Ext.form.TextArea({
			name : 'remark',
			renderTo : 'divRemark',
			maxLength : 250,
			height : 150,
			width : 474
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
						cmbSszy.setValue(data.sszy);
						txtXmmc.setValue(data.xmmc);
						txtJlr.setValue(data.jlr);
						dtJlsj.setValue(formatDate(new Date(data.jlsj['time']), "yyyy-MM-dd"));
						txtRemark.setValue(data.remark);
					}
				}
			})
}

// 保存
function save() {
	if (!cmbSszy.isValid()) {
		alert('所属专业不能为空');
		return;
	}
	if (!txtXmmc.isValid()) {
		alert('项目名称验证不正确');
		return;
	}
	if (!txtJlr.isValid()) {
		alert('记录人验证不正确');
		return;
	}
	if (!dtJlsj.isValid()) {
		alert('记录时间不能为空');
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