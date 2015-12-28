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
			readOnly : true,
			maxLength : 50,
			width : 160
		})

// 型式及规格
var txtXsjgg = new Ext.form.TextField({
			name : 'xsjgg',
			renderTo : 'divXsjgg',
			readOnly : true,
			maxLength : 50,
			width : 160
		})

// 单位
var txtDw = new Ext.form.TextField({
			name : 'dw',
			renderTo : 'divDw',
			style : 'text-align:right',
			readOnly : true,
			maxLength : 16,
			width : 160
		})

// 额定数量
var txtEdsl = new Ext.form.NumberField({
			name : 'edsl',
			renderTo : 'divEdsl',
			style : 'text-align:right',
			readOnly : true,
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
			readOnly : true,
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
			readOnly : true,
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
			readOnly : true,
			maxLength : 500,
			width : 410
		})

// *********** 功能处理 ***********//
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
