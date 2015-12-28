Ext.onReady(function() {
			Ext.QuickTips.init();
			// 标题面板
			var titlePanel = new Ext.app.TitlePanel({
						caption : '回退原因',
						border : false,
						region : 'north'
					});

			var txtBackReason = new Ext.form.TextArea({
						fieldLabel : '回退原因',
						anchor : '95%',
						height : 180
					})

			var lyCenter = new Ext.Panel({
						layout : 'form',
						region : 'center',
						frame : true,
						height : '100%',
						width : '100%',
						labelWidth : 60,
						items : [txtBackReason],
						buttons : [{
									text : '确定',
									handler : function() {
										if (txtBackReason.getValue() == '') {
											alert('请填写回退原因！');
											return;
										}
										window.returnValue=txtBackReason.getValue();
										window.close();
									}
								}, {
									text : '取消',
									handler : function() {
										window.close();
									}
								}]
					})

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [titlePanel, lyCenter]
					});

			// 程序加载完成后撤除加载图片
			setTimeout(function() {
						Ext.get('loading').remove();
						Ext.get('loading-mask').fadeOut({
									remove : true
								});
					}, 250);
		});