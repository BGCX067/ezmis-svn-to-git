EditTaskFormWindow = function() {
	//提示语句显示的位置 side为在右边显示
	Ext.form.Field.prototype.msgTarget = 'side';
	var formWindow=this;
	
	var taskName = {xtype:'textfield' , allowBlank : true , blankText : '请输入环节名称' , fieldLabel : '环节名称' , height :25 , name : 'taskName' , width : 150 ,value : getLabelValue('name') } ;
	
	var taskDesc = {xtype:'textfield' , allowBlank : true , blankText : '请输入环节描述' , fieldLabel : '环节描述' , height :25 , name : 'taskDesc' , width : 150 ,value : getLabelValue('description')} ;
	
	var processPage = {xtype:'textfield' , allowBlank : true  ,  fieldLabel : '处理页面' , height :25 , name : 'processPage' , width : 150 ,value : getLabelValue('process_url') , readOnly : true} ;
	
	//环节变量GridPanel
	var taskVariableGrid = new TaskVariableGrid() ;
	
	//环节权限Panel
	var taskPowerPanel = new TaskPowerPanel() ;
	
	//可用操作
	taskOperateGrid = new TaskOperateGrid();
	
	//事件
	var taskEventGrid = new TaskEventGrid();
	
	//编辑formPanel	
	var formPanel  = new Ext.form.FormPanel( {
		reader : new Ext.data.JsonReader({
				successProperty : 'success',
				root : 'data'
			},[{name:'taskName',mapping:'taskName'},{name:'taskDesc',mapping:'taskDesc'},{name:'processPage',mapping:'processPage'}
		]),
		bodyBorder:false ,
		border : false ,
		height : 480 , 
		width : 495 ,
		frame:true, 					//圆角风格
	    labelWidth:80,					//标签宽度
	    labelAlign: 'left',
	    buttonAlign:'right',
	    items: [
			{
				layout: 'column',
				items : [
					{	
						columnWidth:1 ,
						layout : 'form',
						items : [taskName]
					} ,
					{
						columnWidth:1 ,
						layout : 'form' ,
						items : [taskDesc]
					} , 
					{
						columnWidth:1 ,
						layout : 'form' ,
						items : [processPage]					
					} ,
					{
						columnWidth:1 ,
						activeTab : 0 ,
						plain:true,
						xtype : 'tabpanel' ,
						height : 300,
						width : 490 ,
						listeners : {'tabchange':function(tabPanel, tab ) {
							tab.doLayout();
						}},
						items: [
							{
								title : '环节变量',
								layout: 'form' ,
								items : taskVariableGrid	
							} ,
							{
								title : '环节权限',
								items : taskPowerPanel
							},
							{
								title : '可用操作',
								layout: 'form' ,
								items : taskOperateGrid
							},
							{
								title : '事件',
								layout: 'form' ,
								items : taskEventGrid
							}		
						]
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
	    			editTaskFormWindow.close();
					window.close() 
	    		}
	    	}
	    ]
	}) 
	
	EditTaskFormWindow.superclass.constructor.call(this,{
		bodyStyle : 'margin-top:0px',
		closable : false ,
		draggable : false ,
		resizable : false ,
		title : '环节属性',
		width : 495,
		height : 510 ,
		items : formPanel
	})
}

Ext.extend(EditTaskFormWindow , Ext.Window , {
	/**
	 * 将相应的EditGrid的内容以json字符串的形式写入Xml，grid为数据源，model为mxGraph的model，cell为对应的Node，varName为xml中的属性
	 * 使用此方法有一定的规则.
	 * 既:grid要有Model属性。1.Model为Ext.data.Record.create(...)的返回值 或 2.为有name,maping属性的二维数组 或 3.为一维数组时候每个项为string
	 * 返回json字符串
	 */
	writeXmlFromGrid : function(grid,model,cell,varName) {
		grid.getStore().commitChanges() ;
		grid.stopEditing();
		var taskVariable = "["
			var variable = ""
			var items = grid.getStore().data.items
			for(var i=0 ; i<items.length ; i++) {
				var reJson = items[i] ;
				variable = "{"
				var arrayModel ;
				if(typeof(grid.Model) == "function") {
					arrayModel = grid.Model.prototype.fields.items ;
				} else if(typeof(grid.Model) == "object") {
					arrayModel = grid.Model
				}
				for(var j=0 ; j<arrayModel.length ; j++) {
					var temp = arrayModel[j].mapping==null?arrayModel[j].name:arrayModel[j].mapping ;
					if(typeof(arrayModel[j]) == 'object'){
						variable += "'"+temp+"':'"+items[i].get(arrayModel[j].name)+"'," ;
					} else if(typeof(arrayModel[j]) == 'string') {
						variable += "'"+arrayModel[j]+"':'"+items[i].get(arrayModel[j])+"'," ;
					}
				}
				variable = variable.substring(0,variable.length-1) ;
				variable += "},"
				taskVariable += variable
			}
			if(taskVariable.length > 1) {
				taskVariable = taskVariable.substring(0,taskVariable.length-1)
			}
			taskVariable += "]"
			//改变json的正确格式
			taskVariable = taskVariable.replace(/"/g,"'") ;
			taskVariable = taskVariable.replace(/'true'/g,"true") ;
			taskVariable = taskVariable.replace(/'false'/g,"false") ;
			taskVariable = taskVariable.replace(/'undefined'/g,"undefined") ;
			var edit = new mxCellAttributeChange(cell,varName,taskVariable) ;
			model.execute(edit) ;
			return taskVariable ;
	}
	//相关操作
});