var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'divid'
})
var hdnpzzzmc = new Ext.form.Hidden({
	name : 'pzzzmc',
	renderTo : 'divpzzzmc'
})
var hdnxkrmc = new Ext.form.Hidden({
	name : 'xkrmc',
	renderTo : 'divxkrmc'
})
var hdnzjrmc = new Ext.form.Hidden({
	name : 'zjrmc',
	renderTo : 'divzjrmc'
})
var hdnpzyqzzmc = new Ext.form.Hidden({
	name : 'pzyqzzmc',
	renderTo : 'divpzyqzzmc'
})
var hdnzfrmc = new Ext.form.Hidden({
	name : 'zfrmc',
	renderTo : 'divzfrmc'
})

// 工作票编号
var txtgzpbh = new Ext.form.TextField({
	name : 'gzpbh',
	renderTo : 'divgzpbh',
	readOnly : true,
	width : 170
});

var cmbgzpzt = new Ext.form.TextField({
	name : 'gzpzt',
	renderTo : 'divgzpzt',
	readOnly : true,
	width : 170
})

// 地点及任务
var txtddjrw = new Ext.form.TextArea({
	name : 'ddjrw',
	renderTo : 'divddjrw',
	readOnly : true,
	width : 480
});

// 工作负责人
var txtgzfzr = new Ext.form.TextField({
	name : 'gzfzr',
	renderTo : 'divgzfzr',
	readOnly : true,
	width : 170
});

// 工作票登记人
var txtgzpdjr = new Ext.form.TextField({
	name : 'gzpdjr',
	renderTo : 'divgzpdjr',
	readOnly : true,
	width : 170
});

// 工作班人员
var txtgzbry = new Ext.form.TextField({
	name : 'gzbry',
	renderTo : 'divgzbry',
	readOnly : true,
	width : 480
});

// 收票时间
var dtspsj = new Ext.form.TextField({
	name : 'spsj',
	renderTo : 'divspsj',
	readOnly : true,
	width : 170
});

// 收票人
var txtspr = new Ext.form.TextField({
	name : 'spr',
	renderTo : 'divspr',
	readOnly : true,
	width : 170
});

// 批准工作期限
var dtpzgzqx = new Ext.form.TextField({
	name : 'pzgzqx',
	renderTo : 'divpzgzqx',
	readOnly : true,
	width : 170
});

// 批准值长
var cmbpzzz = new Ext.form.TextField({
	name : 'pzzz',
	renderTo : 'divpzzz',
	readOnly : true,
	width : 170
})

// 许可开工时间
var dtxksj = new Ext.form.TextField({
	name : 'xksj',
	renderTo : 'divxksj',
	readOnly : true,
	width : 170
});

// 许可人
var cmbxkr = new Ext.form.TextField({
	name : 'xkr',
	renderTo : 'divxkr',
	readOnly : true,
	width : 170
})

// 延期期限
var dtyqsj = new Ext.form.TextField({
	name : 'yqsj',
	renderTo : 'divyqsj',
	readOnly : true,
	width : 170
});

// 批准延期值长
var cmbpzyqzz = new Ext.form.TextField({
	name : 'pzyqzz',
	renderTo : 'divpzyqzz',
	readOnly : true,
	width : 170
})

// 办理延期手续时间
var dtyqsxsj = new Ext.form.TextField({
	name : 'yqsxsj',
	renderTo : 'divyqsxsj',
	readOnly : true,
	width : 170
});

// 终结时间
var dtzjsj = new Ext.form.TextField({
	name : 'zjsj',
	renderTo : 'divzjsj',
	readOnly : true,
	width : 170
});

// 终结人
var cmbgzpzjr = new Ext.form.TextField({
	name : 'gzpzjr',
	renderTo : 'divgzpzjr',
	readOnly : true,
	width : 170
})

// 工作票作废人
var cmbgzpzfr = new Ext.form.TextField({
	name : 'gzpzfr',
	renderTo : 'divgzpzfr',
	readOnly : true,
	width : 170
})

// 作废时间
var dtzfsj = new Ext.form.TextField({
	name : 'zfsj',
	renderTo : 'divzfsj',
	readOnly : true,
	width : 170
});

// 作废原因
var txtzfyy = new Ext.form.TextArea({
	name : 'zfyy',
	renderTo : 'divzfyy',
	readOnly : true,
	width : 480
});

// 终结交待
var txtzjjd = new Ext.form.TextArea({
	name : 'zjjd',
	renderTo : 'divzjjd',
	readOnly : true,
	width : 480
});

// 终结检查情况
var txtzjjcqk = new Ext.form.TextArea({
	name : 'zjjcqk',
	renderTo : 'divzjjcqk',
	readOnly : true,
	width : 480
});

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

				txtgzpbh.setValue(data.gzpbh);
				txtddjrw.setValue(data.ddjrw);
				cmbgzpzt.setValue(data.gzpzt);
				txtgzfzr.setValue(data.gzfzr);
				txtgzpdjr.setValue(data.gzpdjr);
				txtgzbry.setValue(data.gzbry);
				txtspr.setValue(data.spr);
				txtzfyy.setValue(data.zfyy);
				txtzjjd.setValue(data.zjjd);
				txtzjjcqk.setValue(data.zjjcqk);

				if (data.spsj) {
					dtspsj.setValue(formatDate(new Date(data.spsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.pzgzqx) {
					dtpzgzqx.setValue(formatDate(new Date(data.pzgzqx['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.xksj) {
					dtxksj.setValue(formatDate(new Date(data.xksj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.yqsj) {
					dtyqsj.setValue(formatDate(new Date(data.yqsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.yqsxsj) {
					dtyqsxsj.setValue(formatDate(new Date(data.yqsxsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.zjsj) {
					dtzjsj.setValue(formatDate(new Date(data.zjsj['time']), "yyyy-MM-dd hh:mm"));
				}
				if (data.zfsj) {
					dtzfsj.setValue(formatDate(new Date(data.zfsj['time']), "yyyy-MM-dd hh:mm"));
				}

				hdnpzzzmc.setValue(data.pzzzmc);
				hdnxkrmc.setValue(data.xkrmc);
				hdnzjrmc.setValue(data.zjrmc);
				hdnpzyqzzmc.setValue(data.pzyqzzmc);
				hdnzfrmc.setValue(data.zfrmc);

				cmbpzzz.setValue(data.pzzzmc);
				cmbxkr.setValue(data.xkrmc);
				cmbpzyqzz.setValue(data.pzyqzzmc);
				cmbgzpzjr.setValue(data.zjrmc);
				cmbgzpzfr.setValue(data.zfrmc);
			}
		},
		failure : function() {

		}
	})
}
