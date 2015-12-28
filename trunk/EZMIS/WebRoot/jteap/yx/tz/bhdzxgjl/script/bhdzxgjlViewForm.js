var myMaskLoad = new Ext.LoadMask(Ext.getBody(), {
	msg : "正在加载，请稍候..."
});
// ************** 页面设计 ****************//
var hdnId = new Ext.form.Hidden({
	name : 'id',
	renderTo : 'id'
})

// 设备名称
var txtbhmc = new Ext.form.TextField({
	name : 'bhmc',
	renderTo : 'divbhmc',
	readOnly : true,
	width : 190
});

// 更改时间
var dtggsj = new Ext.form.TextField({
	name : 'ggsj',
	renderTo : 'divggsj',
	readOnly : true,
	width : 200
});

// 定值修改列表
var txtbhmcgq1 = new Ext.form.TextField({
	name : 'bhmcgq1',
	renderTo : 'divbhmcgq1',
	readOnly : true,
	width : 170
});
var txtbhmcgq2 = new Ext.form.TextField({
	name : 'bhmcgq2',
	renderTo : 'divbhmcgq2',
	readOnly : true,
	width : 170
});
var txtbhmcgq3 = new Ext.form.TextField({
	name : 'bhmcgq3',
	renderTo : 'divbhmcgq3',
	readOnly : true,
	width : 170
});
var txtbhmcgq4 = new Ext.form.TextField({
	name : 'bhmcgq4',
	renderTo : 'divbhmcgq4',
	readOnly : true,
	width : 170
});
var txtbhmcgq5 = new Ext.form.TextField({
	name : 'bhmcgq5',
	renderTo : 'divbhmcgq5',
	readOnly : true,
	width : 170
});

var txtzdzgq1 = new Ext.form.TextField({
	name : 'zdzgq1',
	renderTo : 'divzdzgq1',
	readOnly : true,
	width : 170
});
var txtzdzgq2 = new Ext.form.TextField({
	name : 'zdzgq2',
	renderTo : 'divzdzgq2',
	readOnly : true,
	width : 170
});
var txtzdzgq3 = new Ext.form.TextField({
	name : 'zdzgq3',
	renderTo : 'divzdzgq3',
	readOnly : true,
	width : 170
});
var txtzdzgq4 = new Ext.form.TextField({
	name : 'zdzgq4',
	renderTo : 'divzdzgq4',
	readOnly : true,
	width : 170
});
var txtzdzgq5 = new Ext.form.TextField({
	name : 'zdzgq5',
	renderTo : 'divzdzgq5',
	readOnly : true,
	width : 170
});

var txtbhmcgh1 = new Ext.form.TextField({
	name : 'bhmcgh1',
	renderTo : 'divbhmcgh1',
	readOnly : true,
	width : 170
});
var txtbhmcgh2 = new Ext.form.TextField({
	name : 'bhmcgh2',
	renderTo : 'divbhmcgh2',
	readOnly : true,
	width : 170
});
var txtbhmcgh3 = new Ext.form.TextField({
	name : 'bhmcgh3',
	renderTo : 'divbhmcgh3',
	readOnly : true,
	width : 170
});
var txtbhmcgh4 = new Ext.form.TextField({
	name : 'bhmcgh4',
	renderTo : 'divbhmcgh4',
	readOnly : true,
	width : 170
});
var txtbhmcgh5 = new Ext.form.TextField({
	name : 'bhmcgh5',
	renderTo : 'divbhmcgh5',
	readOnly : true,
	width : 170
});
var txtzdzgh1 = new Ext.form.TextField({
	name : 'zdzgh1',
	renderTo : 'divzdzgh1',
	readOnly : true,
	width : 170
});
var txtzdzgh2 = new Ext.form.TextField({
	name : 'zdzgh2',
	renderTo : 'divzdzgh2',
	readOnly : true,
	width : 170
});
var txtzdzgh3 = new Ext.form.TextField({
	name : 'zdzgh3',
	renderTo : 'divzdzgh3',
	readOnly : true,
	width : 170
});
var txtzdzgh4 = new Ext.form.TextField({
	name : 'zdzgh4',
	renderTo : 'divzdzgh4',
	readOnly : true,
	width : 170
});
var txtzdzgh5 = new Ext.form.TextField({
	name : 'zdzgh5',
	renderTo : 'divzdzgh5',
	readOnly : true,
	width : 170
});

// 更改原因
var txtggyy = new Ext.form.TextArea({
	name : 'ggyy',
	renderTo : 'divggyy',
	readOnly : true,
	width : 570
});

// 发令人
var txtggflr = new Ext.form.TextField({
	name : 'ggflr',
	renderTo : 'divggflr',
	readOnly : true,
	width : 150
});

// 执行人
var txtggzhr = new Ext.form.TextField({
	name : 'ggzhr',
	renderTo : 'divggzhr',
	readOnly : true,
	width : 150
});

// 运行检查
var txtggyhjc = new Ext.form.TextField({
	name : 'ggyhjc',
	renderTo : 'divggyhjc',
	readOnly : true,
	width : 150
});

var txtbhmchfq1 = new Ext.form.TextField({
	name : 'bhmchfq1',
	renderTo : 'divbhmchfq1',
	readOnly : true,
	width : 170
});
var txtbhmchfq2 = new Ext.form.TextField({
	name : 'bhmchfq2',
	renderTo : 'divbhmchfq2',
	readOnly : true,
	width : 170
});
var txtbhmchfq3 = new Ext.form.TextField({
	name : 'bhmchfq3',
	renderTo : 'divbhmchfq3',
	readOnly : true,
	width : 170
});
var txtbhmchfq4 = new Ext.form.TextField({
	name : 'bhmchfq4',
	renderTo : 'divbhmchfq4',
	readOnly : true,
	width : 170
});
var txtbhmchfq5 = new Ext.form.TextField({
	name : 'bhmchfq5',
	renderTo : 'divbhmchfq5',
	readOnly : true,
	width : 170
});

var txtzdzhfq1 = new Ext.form.TextField({
	name : 'zdzhfq1',
	renderTo : 'divzdzhfq1',
	readOnly : true,
	width : 170
});
var txtzdzhfq2 = new Ext.form.TextField({
	name : 'zdzhfq2',
	renderTo : 'divzdzhfq2',
	readOnly : true,
	width : 170
});
var txtzdzhfq3 = new Ext.form.TextField({
	name : 'zdzhfq3',
	renderTo : 'divzdzhfq3',
	readOnly : true,
	width : 170
});
var txtzdzhfq4 = new Ext.form.TextField({
	name : 'zdzhfq4',
	renderTo : 'divzdzhfq4',
	readOnly : true,
	width : 170
});
var txtzdzhfq5 = new Ext.form.TextField({
	name : 'zdzhfq5',
	renderTo : 'divzdzhfq5',
	readOnly : true,
	width : 170
});

var txtbhmchfh1 = new Ext.form.TextField({
	name : 'bhmchfh1',
	renderTo : 'divbhmchfh1',
	readOnly : true,
	width : 170
});
var txtbhmchfh2 = new Ext.form.TextField({
	name : 'bhmchfh2',
	renderTo : 'divbhmchfh2',
	readOnly : true,
	width : 170
});
var txtbhmchfh3 = new Ext.form.TextField({
	name : 'bhmchfh3',
	renderTo : 'divbhmchfh3',
	readOnly : true,
	width : 170
});
var txtbhmchfh4 = new Ext.form.TextField({
	name : 'bhmchfh4',
	renderTo : 'divbhmchfh4',
	readOnly : true,
	width : 170
});
var txtbhmchfh5 = new Ext.form.TextField({
	name : 'bhmchfh5',
	renderTo : 'divbhmchfh5',
	readOnly : true,
	width : 170
});
var txtzdzhfh1 = new Ext.form.TextField({
	name : 'zdzhfh1',
	renderTo : 'divzdzhfh1',
	readOnly : true,
	width : 170
});
var txtzdzhfh2 = new Ext.form.TextField({
	name : 'zdzhfh2',
	renderTo : 'divzdzhfh2',
	readOnly : true,
	width : 170
});
var txtzdzhfh3 = new Ext.form.TextField({
	name : 'zdzhfh3',
	renderTo : 'divzdzhfh3',
	readOnly : true,
	width : 170
});
var txtzdzhfh4 = new Ext.form.TextField({
	name : 'zdzhfh4',
	renderTo : 'divzdzhfh4',
	readOnly : true,
	width : 170
});
var txtzdzhfh5 = new Ext.form.TextField({
	name : 'zdzhfh5',
	renderTo : 'divzdzhfh5',
	readOnly : true,
	width : 170
});

// 恢复原因
var txthfyy = new Ext.form.TextArea({
	name : 'hfyy',
	renderTo : 'divhfyy',
	readOnly : true,
	width : 570
});

// 发令人
var txthfflr = new Ext.form.TextField({
	name : 'hfflr',
	renderTo : 'divhfflr',
	readOnly : true,
	width : 150
});

// 执行人
var txthfzhr = new Ext.form.TextField({
	name : 'hfzhr',
	renderTo : 'divhfzhr',
	readOnly : true,
	width : 150
});

// 运行检查
var txthfyhjc = new Ext.form.TextField({
	name : 'hfyhjc',
	renderTo : 'divhfyhjc',
	readOnly : true,
	width : 150
});

// 备注
var txtbz = new Ext.form.TextField({
	name : 'bz',
	renderTo : 'divbz',
	readOnly : true,
	width : 190
});

// 恢复时间
var dthfsj = new Ext.form.TextField({
	name : 'hfsj',
	renderTo : 'divhfsj',
	readOnly : true,
	width : 200
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
				txtbhmc.setValue(data.bhmc);
				dtggsj.setValue(formatDate(new Date(data.ggsj['time']), "yyyy-MM-dd hh:mm"));
				txtbhmcgq1.setValue(data.bhmcgq1);
				txtbhmcgq2.setValue(data.bhmcgq2);
				txtbhmcgq3.setValue(data.bhmcgq3);
				txtbhmcgq4.setValue(data.bhmcgq4);
				txtbhmcgq5.setValue(data.bhmcgq5);
				txtzdzgq1.setValue(data.zdzgq1);
				txtzdzgq2.setValue(data.zdzgq2);
				txtzdzgq3.setValue(data.zdzgq3);
				txtzdzgq4.setValue(data.zdzgq4);
				txtzdzgq5.setValue(data.zdzgq5);
				txtbhmcgh1.setValue(data.bhmcgh1);
				txtbhmcgh2.setValue(data.bhmcgh2);
				txtbhmcgh3.setValue(data.bhmcgh3);
				txtbhmcgh4.setValue(data.bhmcgh4);
				txtbhmcgh5.setValue(data.bhmcgh5);
				txtzdzgh1.setValue(data.zdzgh1);
				txtzdzgh2.setValue(data.zdzgh2);
				txtzdzgh3.setValue(data.zdzgh3);
				txtzdzgh4.setValue(data.zdzgh4);
				txtzdzgh5.setValue(data.zdzgh5);
				txtggyy.setValue(data.ggyy);
				txtggflr.setValue(data.ggflr);
				txtggzhr.setValue(data.ggzhr);
				txtggyhjc.setValue(data.ggyhjc);
				txtbhmchfq1.setValue(data.bhmchfq1);
				txtbhmchfq2.setValue(data.bhmchfq2);
				txtbhmchfq3.setValue(data.bhmchfq3);
				txtbhmchfq4.setValue(data.bhmchfq4);
				txtbhmchfq5.setValue(data.bhmchfq5);
				txtzdzhfq1.setValue(data.zdzhfq1);
				txtzdzhfq2.setValue(data.zdzhfq2);
				txtzdzhfq3.setValue(data.zdzhfq3);
				txtzdzhfq4.setValue(data.zdzhfq4);
				txtzdzhfq5.setValue(data.zdzhfq5);
				txtbhmchfh1.setValue(data.bhmchfh1);
				txtbhmchfh2.setValue(data.bhmchfh2);
				txtbhmchfh3.setValue(data.bhmchfh3);
				txtbhmchfh4.setValue(data.bhmchfh4);
				txtbhmchfh5.setValue(data.bhmchfh5);
				txtzdzhfh1.setValue(data.zdzhfh1);
				txtzdzhfh2.setValue(data.zdzhfh2);
				txtzdzhfh3.setValue(data.zdzhfh3);
				txtzdzhfh4.setValue(data.zdzhfh4);
				txtzdzhfh5.setValue(data.zdzhfh5);
				txthfyy.setValue(data.hfyy);
				txthfflr.setValue(data.hfflr);
				txthfzhr.setValue(data.hfzhr);
				txthfyhjc.setValue(data.hfyhjc);
				txtbz.setValue(data.bz);
				if (data.hfsj) {
					dthfsj.setValue(formatDate(new Date(data.hfsj['time']), "yyyy-MM-dd hh:mm"));
				}
			}
		},
		failure : function() {

		}
	})
}
