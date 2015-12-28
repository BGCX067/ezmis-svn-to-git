var rdzyzf = new Ext.form.RadioGroup({
			fieldLabel : '转发专业',
			columns : 3,
			items : [{
						boxLabel : '锅炉',
						name : 'zfzy',
						inputValue : 'gl'
					}, {
						boxLabel : '电气',
						name : 'zfzy',
						inputValue : 'dq'
					}, {
						boxLabel : '汽机',
						name : 'zfzy',
						inputValue : 'qj'
					}, {
						boxLabel : '化水',
						name : 'zfzy',
						inputValue : 'hs'
					}, {
						boxLabel : '燃料',
						name : 'zfzy',
						inputValue : 'rl'
					}, {
						boxLabel : '脱硫',
						name : 'zfzy',
						inputValue : 'tl'
					}]

		})

var titlePanel = new Ext.app.TitlePanel({
			region : 'north',
			caption : '异动转发',
			border : true
		});

var radioPanel = new Ext.Panel({
			layout : 'form',
			region : 'center',
			frame : true,
			border : true,
			autoWidth : true,
			labelWidth : 75,
			items : [rdzyzf],
			buttons : [{
						text : '保存',
						handler : function() {
							var zfValue = rdzyzf.getGroupValue();
							if (zfValue == qxzy) {
								alert('不能转发给自己专业');
								return;
							}
							Ext.Ajax.request({
										url : link11,
										method : 'POST',
										params : {
											zfValue : zfValue,
											qxdbh : qxdbh
										},
										success : function(ajax) {
											var responseText = ajax.responseText;
											var obj = Ext.decode(responseText);
											if (!obj.success) {
												alert('数据库操作异常，请联系管理员');
												window.returnValue = "false";
												window.close();
											} else {
												window.returnValue = "true";
												window.close();
											}
										},
										failure : function() {
											alert('数据库操作异常，请联系管理员');
											window.returnValue = "false";
											window.close();
										}
									})
						}
					}, {
						text : '关闭',
						handler : function() {
							window.close();
						}
					}]
		})

function onload(qxzy) {
	rdzyzf.setGroupValue(qxzy);
}