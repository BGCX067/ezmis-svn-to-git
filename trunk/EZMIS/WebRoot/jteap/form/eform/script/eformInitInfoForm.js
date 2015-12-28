var pWin = $mw_param("pWin");
var onloadScript = $mw_param("onloadScript");

var titlePanel=new Ext.app.TitlePanel({caption:'表单信息初始化',border:false,region:'north'});
var initScript = (onloadScript == null || onloadScript =='')?"/*************************************\r\n" +
		"*初始化脚本将会在表单初始化完成后执行\r\n" +
		"*************************************/\r\n":onloadScript;
var initCss = "/*************************************\r\n" +
		"*页面CSS样式定义，您可以在此处定义CSS样式，然后在控件中引用该样式\r\n" +
		"*************************************/\r\n";

//脚本文本域
var txtInitScript = new Ext.form.TextArea({
		name: 'txtInitScript',
		labelSeparator: '',
		region:'center',
		height: 320,
		value:initScript,
		listeners: { 
			render: function(){
				//initFn(value,txtEditParam);
			}
		}
	});
//样式文本域	
var txtInitCss = new Ext.form.TextArea({
	name: 'txtInitCss',
	labelSeparator: '',
	region:'center',
	height: 320,
	value:initCss,
	listeners: { 
		render: function(){
			//initFn(value,txtEditParam);
		}
	}
});

var lyCenter={
	xtype:'tabpanel',
	id:'center-panel',
	region:'center',
	activeTab: 0,
	listeners:{tabchange :function(tPanel, tab ){tab.doLayout();}},
	plain:true,
	border:false,
	defaults:{bodyStyle:'padding:0px'},
	margins:'0 0 0 0',
	items:[{
		buttonAlign: 'right',
		style: 'margin:0px',
		bodyStyle:'padding:0px',
		border:true,
		region:'center',
        title:'初始化事件',
        frame:false,
        layout:'border',
        tbar:[{text:'恢复',handler:function(){alert('ok');}}],
        items:[txtInitScript]
    },{
       buttonAlign: 'right',
		style: 'margin:0px',
		bodyStyle:'padding:0px',
		border:true,
		region:'center',
        title:'初始化样式',
        frame:false,
        layout:'border',
        tbar:[{text:'恢复',handler:function(){alert('ok');}}],
        items:[txtInitCss]
    }],
    buttons:[{text:'确定',handler:ok},{text:'取消',handler:function(){window.close();}}]
}

/**
 * 确定提交
 */
function ok(){
	window.returnValue = {onloadScript:txtInitScript.getValue()} 
	window.close();
}