EditConnectFormWindow = function() {
	//提示语句显示的位置 side为在右边显示
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var connName = {xtype:'textfield' , allowBlank : true , blankText : '请输入路由名称' , fieldLabel : '路由名称' , height :25 , name : 'connName' , width : 250 , value : getLabelValue('connName',cell) }
	
	var connDesc = {xtype:'textfield' , allowBlank : true , blankText : '请输入路由描述' , fieldLabel : '路由描述' , height :25 , name : 'connDesc' , width : 250 , value : getLabelValue('connDesc',cell)}
	
	var aimTask = {xtype:'textfield' , allowBlank : true , blankText : '请输入目标环节' , fieldLabel : '目标环节' , height :25 , name : 'aimTask' , width : 250 , value : getLabelValue('aimTask',cell) , readOnly : true }
	
	//路由条件Panel
	var connConditionPanel = new ConnConditionPanel() ;
	
	//编辑formPanel	
	var formPanel  = new Ext.form.FormPanel( {
		reader : new Ext.data.JsonReader({
				successProperty : 'success',
				root : 'data'
			},[{name:'connName',mapping:'connName'},{name:'connDesc',mapping:'connDesc'},{name:'aimTask',mapping:'aimTask'}]),
		height : 410 , 
		width : 540 ,
		bodyStyle:'padding:20px 10px 0px 20px',
		frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
	    labelAlign: 'left',
	    buttonAlign:'center',
	    items: [
			{
				layout: 'column',
				items : [
					{	
						columnWidth:1 ,
						layout : 'form',
						items : [connName]
					} ,
					{
						columnWidth:1 ,
						layout : 'form' ,
						items : [connDesc]
					} , 
					{
						columnWidth:1 ,
						layout : 'form' ,
						items : [aimTask]
					} , 
					{
						columnWidth:1 ,
						bodyStyle : 'margin-top:10px',
						height : 310,
						width : 540 ,
						defaults:{bodyStyle:'padding:2px,margin: 2px 2px 2px 2px'},
						items: [connConditionPanel]
					}
				]
			}
	    ],
	    buttons : [
	    	{
	    		text:'确定',
	    		hidden : false ,
	    		handler : function() {
	    			
	    		}
	    	} ,
	    	{
	    		text : '取消' ,
	    		hidden : false ,
	    		handler : function() {
	    			editConnectFormWindow.close() ;
					window.close() ;	    			
	    		}
	    	}
	    ]
	}) 
	
	EditConnectFormWindow.superclass.constructor.call(this,{
		bodyStyle : 'margin-top:0px',
		draggable : false ,
		resizable : false ,
		title : '路由属性',
		width : 540,
		height : 440 ,
		closable : false ,
		items : formPanel
	})
}

Ext.extend(EditConnectFormWindow , Ext.Window , {
	//相关操作
});