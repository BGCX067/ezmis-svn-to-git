var rdzyzf = new Ext.form.RadioGroup({
	fieldLabel : '转发专业',
	columns : 2,
	items : [{
		boxLabel : '燃运机务',
		name : 'zfzy',
		inputValue : 'ryjw'
	}, {
		boxLabel : '燃运电试',
		name : 'zfzy',
		inputValue : 'ryds'
	}]
})

var titlePanel = new Ext.app.TitlePanel({
	region : 'north',
	caption : '缺陷转发',
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
		text : '确认',
		handler : function() {
			var zfValue = rdzyzf.getGroupValue();
			if (zfValue == qxzy) {
				alert('不能转发给自己专业');
				return;
			}
			var zfzy;
			
			if (zfValue == 'ryjw') {
				zfzy = '机械检修班';
			} else if (zfValue == 'ryds') {
				zfzy = '电控班';
			}
			Ext.Ajax.request({
				url : link11,
				method : 'POST',
				params : {
					zfValue : zfValue,
					zfzy : zfzy,
					qxdbh : qxdbh
				},
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					if (!obj.success) {
						alert('数据库操作异常，请联系管理员');
						window.returnValue = false;
						window.close();
					} else {
						window.returnValue = true;
						window.close();
					}
				},
				failure : function() {
					alert('数据库操作异常，请联系管理员');
					window.returnValue = false;
					window.close();
				}
			})
		}
	}, {
		text : '关闭',
		handler : function() {
			window.returnValue = false;
			window.close();
		}
	}]
})

function onload(qxzy) {
	rdzyzf.setGroupValue(qxzy);
}
