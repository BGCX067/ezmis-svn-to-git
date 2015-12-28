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
			readOnly : true,
			width : 200
		})

// 检修性质
var txtJxxz = new Ext.form.TextField({
			name : 'jxxz',
			renderTo : 'divJxxz',
			readOnly : true,
			width : 200
		})

// 机组
var txtJz = new Ext.form.TextField({
			name : 'jz',
			renderTo : 'divJz',
			readOnly : true,
			width : 200
		})

// 起始日期
var txtQsrq = new Ext.form.TextField({
			name : 'qsrq',
			renderTo : 'divQsrq',
			readOnly : true,
			width : 200
		});

// 结束日期
var txtJsrq = new Ext.form.TextField({
			name : 'jsrq',
			renderTo : 'divJsrq',
			readOnly : true,
			width : 200
		});

// 人工费用
var txtRgfy = new Ext.form.TextField({
			name : 'rgfy',
			renderTo : 'divRgfy',
			style : 'text-align:right',
			readOnly : true,
			width : 180
		})

// 材料费用
var txtClfy = new Ext.form.TextField({
			name : 'clfy',
			renderTo : 'divClfy',
			style : 'text-align:right',
			readOnly : true,
			width : 180
		})

// 费用合计
var txtFyhj = new Ext.form.TextField({
			name : 'fyhj',
			renderTo : 'divFyhj',
			style : 'text-align:right',
			readOnly : true,
			width : 180
		})

// 内容概要
var txtNrgy = new Ext.form.TextArea({
			name : 'nrgy',
			renderTo : 'divNrgy',
			readOnly : true,
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
var uploadBtnJxrws = new Ext.Button({
			text : '下载',
			renderTo : 'divJxrws',
			handler : function() {
				downloadAttach("jxrws")
			}
		});

// 检修项目
var txtJxxm = new Ext.form.TextField({
			name : 'jxxmMc',
			renderTo : 'divJxxmMc',
			fieldLabel : '检修项目',
			readOnly : true,
			width : 280
		})
var uploadBtnJxxm = new Ext.Button({
			text : '下载',
			renderTo : 'divJxxm',
			handler : function() {
				downloadAttach("jxxm")
			}
		});

// 检修技术协议书
var txtJxjsxy = new Ext.form.TextField({
			name : 'jxjsxyMc',
			renderTo : 'divJxjsxyMc',
			fieldLabel : '检修技术协议书',
			readOnly : true,
			width : 280
		})
var uploadBtnJxjsxy = new Ext.Button({
			text : '下载',
			renderTo : 'divJxjsxy',
			handler : function() {
				downloadAttach("jxjsxy")
			}
		});

// 其他文件1
var txtQt1 = new Ext.form.TextField({
			name : 'qtfjMc1',
			renderTo : 'divQtfjMc1',
			fieldLabel : '其他附件1',
			readOnly : true,
			width : 280
		})
var uploadBtnQt1 = new Ext.Button({
			text : '下载',
			renderTo : 'divQtfj1',
			handler : function() {
				downloadAttach("qtfj1")
			}
		});

// 其他文件2
var txtQt2 = new Ext.form.TextField({
			name : 'qtfjMc2',
			renderTo : 'divQtfjMc2',
			fieldLabel : '其他附件2',
			readOnly : true,
			width : 280
		})
var uploadBtnQt2 = new Ext.Button({
			text : '下载',
			renderTo : 'divQtfj2',
			handler : function() {
				downloadAttach("qtfj2")
			}
		});

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
						txtJhmc.setValue(data.jhmc);
						txtJxxz.setValue(data.jxxz);
						txtJz.setValue(data.jz);
						txtQsrq.setValue(formatDate(new Date(data.qsrq['time']), "yyyy-MM-dd"));
						txtJsrq.setValue(formatDate(new Date(data.jsrq['time']), "yyyy-MM-dd"));
						txtRgfy.setValue(data.rgfy);
						txtClfy.setValue(data.clfy);
						txtFyhj.setValue(data.fyhj);
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

/**
 * 下载附件
 */
function downloadAttach(name) {
	var id = hdnId.getValue();
	var link = link6 + "?id=" + id + "&name=" + name;
	downloadFrame = document.createElement("iframe"); // 通过Iframe 的src
	// 属性调用下载文件的action方法无刷新的下载文件
	downloadFrame.id = "downloadFrame";
	downloadFrame.src = link;
	downloadFrame.style.display = "none";
	document.body.appendChild(downloadFrame);
}