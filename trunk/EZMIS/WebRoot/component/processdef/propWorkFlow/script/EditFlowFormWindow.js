EditFlowFormWindow = function() {
	var editFlowFormWindow = this ;  
	//提示语句显示的位置 side为在右边显示
	Ext.form.Field.prototype.msgTarget = 'side';
	var formWindow=this;
	
	var flowName = {xtype:'textfield' ,id:'txtFlowName', allowBlank : false , blankText : '请输入流程名称' , fieldLabel : '流程名称' , height :25 , name : 'flowName' , width : 150 , value : getLabelValue('flowName')}
	
	var flowType = {xtype:'textfield' ,id:'txtFlowType', allowBlank : true , blankText : '请输入流程分类' , fieldLabel : '流程分类' , height :25 , name : 'flowType' , width : 150 , value : getLabelValue('flowType')}
	
	flowTypeId = new Ext.form.Hidden({
		id:'txtFlowTypeId', 
		name : 'flowTypeId',
		value : getLabelValue('flowTypeId')
	}) ; 
	
	//var formType = {xtype:'fieldset' , title : '表单类型' ,border:'column',items : [{xtype:'radio',boxLabel :'EFORM表单',name:'formType',value :'EFORM表单',labelSeparator:''},{xtype:'radio',boxLabel :'Excel表单',name:'formType',value:'Excel表单',labelSeparator:''},{xtype:'radio',boxLabel :'文档',name:'formType',value:'文档',labelSeparator:''},{xtype:'radio',boxLabel :'自定义表单',name:'formType',value:'自定义表单',labelSeparator:''}]}
	var formType = {xtype:'panel',layout:'column',items : [
						{
							columnWidth:.17,
							html:'表单类型:'
						},
						{
							columnWidth:.83,
							layout:'column',
							items : [{columnWidth:.25,xtype:'radio',boxLabel :'文档',name:'formType',inputValue:'02',labelSeparator:'',checked : getLabelValue('formType')=='02'?true:false,listeners : {"check" : function(radio , checked) {
								if(checked) {
									relFormName.setValue("") ;
									relFormId.setValue("") ;
									relFormName.setDisabled(true) ;
									selButton.setDisabled(true) ;
								}
							}} },{columnWidth:.25,xtype:'radio',boxLabel :'自定义表单',name:'formType',inputValue:'01',labelSeparator:'',checked : getLabelValue('formType')=='01'?true:false,listeners : {"check" : function(radio , checked) {
								if(checked) {
									relFormName.setValue("") ;
									relFormId.setValue("") ;
									relFormName.setDisabled(false) ;
									selButton.setDisabled(false) ;
								}
							}}},{columnWidth:.25,xtype:'radio',boxLabel :'无',name:'formType',inputValue :'03',labelSeparator:'',checked : getLabelValue('formType')=='03'?true:false,listeners : {"check" : function(radio , checked) {
								if(checked) {
									relFormName.setValue("") ;
									relFormId.setValue("") ;
									relFormName.setDisabled(true) ;
									selButton.setDisabled(true) ;
								}
							}}}]
						}
					]}
	/*var formType = {layout:'column',items:[{layout: 'form',items: [new Ext.form.Radio({fieldLabel: '横向Radio Group',boxLabel:'opt1', name:'opt1'})]},{items: [new Ext.form.Radio({boxLabel:'opt2', name:'opt1'})]},{items: [new Ext.form.Radio({boxLabel:'opt3', name:'opt1'})]},{items: [new Ext.form.Radio({boxLabel:'opt3', name:'opt1'})]}]}*/
	
	relFormName = new Ext.form.TextField( {xtype:'textfield',disabled : getLabelValue('formType')=='01'?false:true  ,id:'txtRelFormName', allowBlank : true  , fieldLabel : '关联表单' , height :25 , name : 'relFormName' , width : 150 , value : getLabelValue('relFormName'),readOnly : true});
	
	relFormId = new Ext.form.Hidden(
		{
			id : 'txtRelFormId' ,
			name : 'relFormId' ,
			value : getLabelValue('relFormId')
		}
	)
	
	//选择form项的按钮
	var selButton = new Ext.Button({xtype:'button',text:'选择' ,disabled : getLabelValue('formType')=='01'?false:true , handler:editFlowFormWindow.showCheckEformTreeWindow}) ;
	
	processPage = new Ext.form.TextField({xtype:'textfield' ,id:'txtProcessPage', allowBlank : true  ,readOnly:false ,  fieldLabel : '处理页面' , height :25 , name : 'processPage' , width : 150  , value : getLabelValue('processPage')})
	
	//流程变量GridPanel
	//var fvgDatas = getLabelValue('flowVariable')
	var flowVariableGrid = new FlowVariableGrid() ;
	
//	//流程操作GridPanel
//	var flowOperateGrid = new FlowOperateGrid() ;
	
	//编辑formPanel	
	var formPanel  = new Ext.form.FormPanel( {
		reader : new Ext.data.JsonReader({
				successProperty : 'success',
				root : 'data'
			},[{name:'flowName',mapping:'flowName'},{name:'flowType',mapping:'flowType'},{name:'relFormName',mapping:'relFormName'},{name:'processPage',mapping:'processPage'}
		]),
		height : 520 , 
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
						items : [flowName]
					} ,
					{
						columnWidth:1 ,
						layout : 'form' ,
						items : [flowType]
					} , 
					{
						columnWidth:1 ,
						layout : 'form' ,
						items : [formType]
					} , 
					{
						columnWidth:.5,
						layout : 'form' ,
						items : [relFormName]
					} ,
					{
						columnWidth:.5,
						layout : 'form' ,
						items : [selButton]
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
						height : 312,
						width : 500 ,
						defaults:{bodyStyle:'padding:2px,margin: 2px 2px 2px 2px'},
						items: [
							{
								title : '流程变量',
								layout: 'form' ,
								items : flowVariableGrid	
							}
//							,
//							{
//								title : '流程操作',
//								layout: 'form' ,
//								items : flowOperateGrid
//							}		
						]
					}
				]
			}
	    ],
	    buttons : [
	    	{
	    		text:'保存并关闭',
	    		hidden : false ,
	    		handler : function() {
	    			//判断是否填写了流程名称。
	    			if(Ext.get("txtFlowName").getValue()==null || Ext.get("txtFlowName").getValue() =="") {
						alert("流程名称必填") ;
						return ;  
					}
					//判断流程变量是否有同名现象
					var variableItems = flowVariableGrid.store.data.items ;
					for(var i=0 ; i<variableItems.length ; i++) {
						var firstName = variableItems[i].data.variableName ;
						if(firstName == "") {
							alert("变量名不能为空") ;
							return ;
						}
						for(var j=i+1 ; j<variableItems.length ; j++) {
							var secName = variableItems[j].data.variableName ;
							if(firstName == secName) {
								alert("请先处理好变量名称同名现象") ;
								return ;
							}
						}
					}
					
					//开始写入内存的xml中
					var value = model.getValue(cell);
					model.beginUpdate();
					try {
						flowVariableGrid.getStore().commitChanges() ;
						flowVariableGrid.stopEditing();
						
//						flowOperateGrid.getStore().commitChanges() ;
//						flowOperateGrid.stopEditing() ;
						
						//修改流程名称的xml
						var edit = new mxCellAttributeChange(cell,"flowName",Ext.get("txtFlowName").getValue());
						model.execute(edit) ;
						
						//修改流程分类的xml
						edit = new mxCellAttributeChange(cell,"flowType",Ext.get("txtFlowType").getValue()) ;
						model.execute(edit) ;
						
						//修改表单类型的xml
						var formType = "" ;
						var radios = document.getElementsByName("formType") ;
						for(var i=0 ; i<radios.length ; i++) {
							if(radios[i].checked) {
								formType = radios[i].value ; 
								break ;								
							}
						}						
						edit = new mxCellAttributeChange(cell,"formType",formType==null?"":formType) ;
						model.execute(edit) ;
						
						//修改关联表单Name的xml
						edit = new mxCellAttributeChange(cell,"relFormName",Ext.get("txtRelFormName").getValue()) ;
						model.execute(edit) ;
						
						//修改关联表单ID的xml
						edit = new mxCellAttributeChange(cell,"relFormId",relFormId.getValue()) ;
						model.execute(edit) ;
						
						//修改处理页面的xml
						edit = new mxCellAttributeChange(cell,"processPage",Ext.get("txtProcessPage").getValue()) ;
						model.execute(edit) ;
						//同步所有任务节点的处理页面的xml
						for(var i=0 ; i<model.nextId ; i++) {
							var taskCell = model.cells[i] ; 
							if(taskCell.value.tagName != "task") {
								continue ;
							}
							//同步process_url
							edit = new mxCellAttributeChange(taskCell,"process_url",Ext.get("txtProcessPage").getValue()) ;
							model.execute(edit) ;
							
							//同步task中的process_url
							var jsonObj = Ext.decode(getLabelValue("task",taskCell).replace(/%26quot;/g,"\"")) 
							jsonObj.process_url = Ext.get("txtProcessPage").getValue() ;
							
							edit = new mxCellAttributeChange(taskCell,"task",Ext.encode(jsonObj).replace(/"/g,"'").replace(/'\[/g,"%26quot;[").replace(/\]'/g,"]%26quot;").replace(/'null'/g,"null")) ;
							model.execute(edit) ;
							
						}
						//修改流程变量的xml
						var flowVariable = "[" ;
						var variable = "" ;
						var items = flowVariableGrid.getStore().data.items ;
						for(var i=0 ; i<items.length ; i++) {
							var reJson = items[i] ;
							variable = "{" ;
							variable += "'name_cn':'"+items[i].get("cnName")+"'," ; 
							variable += "'storemode':'"+items[i].get("storeMode")+"'," ;
							variable += "'kind':'"+items[i].get("type")+"'," ;
							variable += "'name':'"+items[i].get("variableName")+"'," ;
							variable += "'type':'"+items[i].get("variableType")+"'," ;
							variable += "'value':'"+items[i].get("variableValue")+"'" ;
							variable += "}," ;
							flowVariable += variable ;
						}
						if(flowVariable.length > 1) {
							flowVariable = flowVariable.substring(0,flowVariable.length-1)
						}
						flowVariable += "]"
						edit = new mxCellAttributeChange(cell,"flowVariable",flowVariable) ;
						model.execute(edit) ;
						
//						//修改流程操作的xml
//						var flowOperate = "["
//						var operate = ""
//						var items = flowOperateGrid.getStore().data.items
//						for(var i=0 ; i<items.length ; i++) {
//							var reJson = items[i] ;
//							operate = "{"
//							operate += "'name':'"+items[i].get("operaName")+"'," 
//							operate += "'mark':'"+items[i].get("btnSn")+"'"
//							operate += "},"
//							flowOperate += operate
//						}
//						if(flowOperate.length > 1) {
//							flowOperate = flowOperate.substring(0,flowOperate.length-1)
//						}
//						flowOperate += "]"
//						edit = new mxCellAttributeChange(cell,"flowOperate",flowOperate) ;
//						model.execute(edit) ;
//						debugger;
						//写入workflow的xml值
						var workflow = {
							name : Ext.get("txtFlowName").getValue()==""?"":Ext.get("txtFlowName").getValue() ,
							formtype : formType==""?null:formType,
							flowCatalog :{
								id : flowTypeId.getValue()==""?null:flowTypeId.getValue()
							},
							cform : {
								id:relFormId.getValue()==""?null:relFormId.getValue()
							},
							process_url : Ext.get("txtProcessPage").getValue()==""?null:Ext.get("txtProcessPage").getValue(),
							flowVariables : Ext.decode(flowVariable)
//							,
//							flowOperations : Ext.decode(flowOperate)
						}
						workflow = Ext.encode(workflow).replace(/"/g,"'") ;
						edit = new mxCellAttributeChange(cell,"workflow",workflow) ;
						model.execute(edit) ;
						
					} 
					catch(e){
						
						
					}
					finally {
						model.endUpdate();
						editFlowFormWindow.close();
						window.close() ;
						
						
					}
	    		}
	    	} ,
	    	/*
	    	{
	    		text : '保存并设计流程' ,
	    		hidden : false ,
	    		handler : function() {
	    			
	    		}
	    	} ,
	    	*/
	    	{
	    		text : '取消' ,
	    		hidden : false ,
	    		handler : function() {
	    			editFlowFormWindow.close();
					window.close() ;
	    		}
	    	}
	    ]
	}) 
	
	EditFlowFormWindow.superclass.constructor.call(this,{
		bodyStyle : 'margin-top:0px',
		draggable : false ,
		resizable : false ,
		title : '创建流程',
		width : 495,
		height : 550 ,
		closable : false ,
		items : formPanel
	})
}

Ext.extend(EditFlowFormWindow , Ext.Window , {
	//相关操作
	showCheckEformTreeWindow : function() {
		var indicatTree=new Ext.tree.TreePanel({
	    	id:'resTree',
	        autoScroll:true,
	        autoHeight:false,
	        height:170,
	        width:150,
	        originalValue:"",
	        animate:false,
	        ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
	        enableDD:true,
	        containerScroll: true,
	        defaults: {bodyStyle:'padding:0px'}, 
	        border :false,
	        hideBorders :true,
	        rootVisible :true,
	        lines :false,
	        bodyBorder :false,
	        root:new Ext.tree.AsyncTreeNode({
	        	text:'所有表单',
	        	loader:new Ext.tree.TreeLoader({
		            dataUrl:link1,//"/JTEAP/jteap/cform/CFormCatalogAction!showCatalogAndItemTreeForCheckAction.do",
		            	listeners:{load:function(){
		            	//将树加载完之后将已经指定的资源勾选上
		            	/*
		            	var ids=resourceTree.findIndicatResourceIds().split(",");
		            	for(var i=0;i<ids.length;i++){
		            		if(ids[i]!=""){
		            			var oNode=indicatTree.getNodeById(ids[i]);
		            			if(oNode)
		            				oNode.setChecked(true);
		            		}
		            	}
		            	
		            	//包括第3态节点的被选中节点id集合
		            	indicatTree.originalValue=indicatTree.getRootNode().getCheckedIds(true,false);
		            	*/
		            }}
		        }),
		        expanded :true
	        }),
	        submitChange:function(){
	        	var attrs = indicatTree.getSelectionModel().selNode.attributes ;
	        	if(!attrs.isItem) {
	        		alert("您选择的是分类,请选择分类下的项") ;
	        		return ;
	        	}relFormId
	        	relFormId.setValue(attrs.id)
	        	relFormName.setValue(attrs.text) ;
	        	processPage.setValue(attrs.eformUrl) ;
	        	indicatWindow.close();
	        }
	    });
	    //资源选择窗口
		 var indicatWindow = new Ext.Window({
            layout:'fit',
            title:'资源选择器',
            width:250,
            height:350,
            frame:true,
            items: indicatTree,
            buttons: [{
                text:'确定',handler:function(){
                	indicatTree.submitChange();
                }
            },{
                text: '取消',
                handler: function(){
                    indicatWindow.close();
                }
            }]
		});
		//显示窗口
		indicatWindow.show();
	}
});