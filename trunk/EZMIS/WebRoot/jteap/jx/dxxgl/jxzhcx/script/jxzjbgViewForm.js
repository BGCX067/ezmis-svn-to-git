var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在加载，请稍候..."
		});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
			name : 'id',
			rendTo : 'bgid'
		})

// 报告名称
var txtBgmc = new Ext.form.TextField({
			name : 'bgmc',
			readOnly : true,
			renderTo : 'divBgmc',
			width : 110
		})

// 检修性质
var cmbJxxz = new Ext.form.TextField({
			name : 'jxxz',
			readOnly : true,
			renderTo : 'divJxxz',
			width : 110
		})

// 所属机组
var cmbSsjz = new Ext.form.TextField({
			name : 'ssjz',
			readOnly : true,
			renderTo : 'divSsjz',
			width : 110
		})

// 负责人员
var txtFzry = new Ext.form.TextArea({
			name : 'fzry',
			readOnly : true,
			renderTo : 'divFzry',
			width : 480
		})

// 起始日期
var dtQsrq = new Ext.form.TextField({
			name : 'qsrq',
			readOnly : true,
			renderTo : 'divQsrq',
			width : 110
		});

// 终止日期
var dtJsrq = new Ext.form.TextField({
			name : 'jsrq',
			readOnly : true,
			renderTo : 'divJsrq',
			width : 110
		});

// 报告摘要
var txtBgzy = new Ext.form.TextArea({
			name : 'bgzy',
			readOnly : true,
			renderTo : 'divBgzy',
			width : 480
		})

// 验收意见
var txtYsyj = new Ext.form.TextArea({
			name : 'ysyj',
			readOnly : true,
			renderTo : 'divYsyj',
			width : 480
		})

// 验收日期
var dtYsrq = new Ext.form.TextField({
			name : 'ysrq',
			readOnly : true,
			renderTo : 'divYsrq',
			width : 110
		});

// 验收部门
var txtYsbm = new Ext.form.TextField({
			name : 'ysbm',
			readOnly : true,
			renderTo : 'divYsbm',
			width : 110
		})

// 存在问题
var txtCzwt = new Ext.form.TextArea({
			name : 'czwt',
			readOnly : true,
			renderTo : 'divCzwt',
			width : 480
		})

// 备注说明
var txtBzsm = new Ext.form.TextArea({
			name : 'bzsm',
			readOnly : true,
			renderTo : 'divBzsm',
			width : 480
		})

// 报告附件
var txtBgfj = new Ext.form.TextField({
			name : 'bgfjMc',
			readOnly : true,
			renderTo : 'divBgfjMc',
			width : 300
		})
var downLoadBtnBgfj = new Ext.Button({
			text : '下载',
			renderTo : 'divBgfj',
			handler : function() {
				downloadAttach()
			}
		});

// *********** 功能处理 ***********//
// 初始化
function load() {
	myMaskLoad.show();
	Ext.Ajax.request({
				url : link5,
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
						}
						myMaskLoad.hide();
					}
				},
				failure : function() {

				}
			})
}

/**
 * 下载附件
 */
function downloadAttach() {
	var id = hdnId.getValue();
	var link = link6 + "?id=" + id;
	downloadFrame = document.createElement("iframe"); // 通过Iframe 的src
	// 属性调用下载文件的action方法无刷新的下载文件
	downloadFrame.id = "downloadFrame";
	downloadFrame.src = link;
	downloadFrame.style.display = "none";
	document.body.appendChild(downloadFrame);
}