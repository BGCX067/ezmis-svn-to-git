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

var cmbSszy = new Ext.form.TextField({
			name : 'sszy',
			renderTo : 'divSszy',
			readOnly : true,
			width : 200
		})

// 项目名称
var txtXmmc = new Ext.form.TextArea({
			name : 'xmmc',
			renderTo : 'divXmmc',
			readOnly : true,
			height : 50,
			width : 474
		})

// 记录人
var txtJlr = new Ext.form.TextField({
			name : 'jlr',
			renderTo : 'divJlr',
			readOnly : true,
			width : 200
		})
		
// 记录时间
var dtJlsj = new Ext.form.TextField({
			name : 'jlsj',
			renderTo : 'divJlsj',
			readOnly : true,
			width : 200
		});

// 备注说明
var txtRemark = new Ext.form.TextArea({
			name : 'remark',
			renderTo : 'divRemark',
			readOnly : true,
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
