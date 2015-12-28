var rdzyzf = new Ext.form.RadioGroup({
	fieldLabel : '转发专业',
	columns : 3,
	items : [{
		boxLabel : '热工一班',
		name : 'zfzy',
		inputValue : '热工一班'
	}, {
		boxLabel : '热工二班',
		name : 'zfzy',
		inputValue : '热工二班'
	}, {
		boxLabel : '电试班',
		name : 'zfzy',
		inputValue : '电试班'
	}, {
		boxLabel : '电作业班',
		name : 'zfzy',
		inputValue : '电作业班'
	}, {
		boxLabel : '炉作业班',
		name : 'zfzy',
		inputValue : '炉作业班'
	}, {
		boxLabel : '机作业班',
		name : 'zfzy',
		inputValue : '机作业班'
	}, {
		boxLabel : '检修办公室',
		name : 'zfzy',
		inputValue : '检修办公室'
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
			if (zfValue == zrbm) {
				alert('不能转发给自己专业');
				return;
			}
			Ext.Ajax.request({
				url : link11,
				method : 'POST',
				params : {
					zfValue : zfValue,
					qxdbh : qxdbh,
					zrbm : zrbm
				},
				success : function(ajax) {
					var responseText = ajax.responseText;
					var obj = Ext.decode(responseText);
					if (!obj.success) {
						alert('数据库操作异常，请联系管理员');
						window.returnValue =false;
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

function onload(zrbm) {
	rdzyzf.setGroupValue(zrbm);
}