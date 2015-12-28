TaskPowerPanel = function() {
	var taskPowerPanel = this ;
	
	//从xml中取出taskPower的值，并拼凑成数组data[0][0]=name,data[0][1]=obj的形式
	var data = new Array();
	var arrayTaskPower = Ext.decode(getLabelValue("taskPower",cell)) ;
	for(var i=0;i<arrayTaskPower.length;i++){
		if(typeof(arrayTaskPower[i]) =="string") {
			data[i] = new Array(Ext.decode(arrayTaskPower[i]).name,arrayTaskPower[i]) ;			
		} else {
			data[i] = new Array(arrayTaskPower[i].name,Ext.encode(arrayTaskPower[i]).replace(/"/g,"'")) ;
		}
	}
	
	//listBox所需要的store
	var store = new Ext.data.SimpleStore({    
                                    data:data,    
                                    fields:["key","value"]    
                            },['key','value']) ;
	
	listBox = new Ext.ux.Multiselect(
					{
						tbar:["->",
							{
								text:" ＋ ",handler:function()
								{
									var window = new SelProcessorRuleWindow();
									window.show() ;
								}
							},
							{
								text:" － ",handler:function()
								{
									var selectionsArray = listBox.view.getSelectedIndexes();
									if(selectionsArray.length ==0) {
										return ;
									}
									//按选择索引从大到小排序
									selectionsArray.sort(function compare(a,b){return b-a;});
									for(var i=0 ; i<selectionsArray.length ; i++){
										var record = listBox.view.store.getAt(selectionsArray[i]);
										listBox.view.store.remove(record) ;
									}
									listBox.view.refresh();
								}
							}
						],
						width:200,
						height:208,
						displayField : 'key',
						//legend : '权限组合',
						store : store,
						allowDup : true,
						copy:true,
						allowTrash:true,
						appendOnly:false,
						isFormField: false
					}
				) ;
	
	
	//单人处理
	this.singleProce = new Ext.form.Radio({boxLabel : '单人处理' ,labelSeparator : '',name :'opt1' , inputValue : "single" , checked : getLabelValue('processKind',cell)!="single"?false:true}) ;
	//单人处理添加check事件
	this.singleProce.on("check",function(radio , checked) {
		//同步串行,并行
		taskPowerPanel.chkSerial.setDisabled(checked) ;
		taskPowerPanel.chkIndiOne.setDisabled(!checked) ;
		personSelOption.doLayout() ;
	}) ;
	
	//多人处理
	this.multiProce = new Ext.form.Radio({boxLabel : '多人处理' ,labelSeparator  : '',name :'opt1' , inputValue : "multi" , checked : getLabelValue('processKind',cell)!="multi"?false:true}) ;
	
	//是否只允许指定一个处理人
	this.chkIndiOne =new Ext.form.Checkbox({boxLabel : '只允许指定一个处理人',labelSeparator :'' , inputValue : "1",checked : getLabelValue('isOneProcessActor',cell)!="1"?false:true,disabled:getLabelValue('processKind',cell)=="multi"?true:false}) ;
	
	//是否串行
	this.chkSerial = new Ext.form.Checkbox({boxLabel : '按顺序串行处理,否则并处理',labelSeparator :'' , inputValue : "1",checked : getLabelValue('processMode',cell)!="1"?false:true,disabled:getLabelValue('processKind',cell)=="single"?true:false}) ;
	
	//是否可自由选择处理人
	this.chkFreeSelPerson = new Ext.form.Checkbox({boxLabel :'可自由选择处理人',labelSeparator :'' , inputValue : "1",checked : getLabelValue('isChooseActor',cell)!="1"?false:true}) ;
	
	var personSelOption = new Ext.form.FieldSet({
								xtype : 'fieldset' ,
								width:280,
								height:120,
								layout : 'form' ,
								title : '人员选择选项' , 
								items : [
									{
										layout:'column',
										border : false ,
										items:[
											{
												columnWidth:.3,
												border : false ,
												items:this.singleProce
											},
											{
												columnWidth:.7,
												border : false ,
												items:this.chkIndiOne
											}
										]
									},
									{
										bodyStyle : 'margin-top:10px',
										border : false ,
										layout:'column',
										items:[
											{
												columnWidth:.3,
												border : false ,
												items:this.multiProce
											},
											{
												columnWidth:.7,
												border : false,
												items:this.chkSerial
											}
										]
									}
								]
							}) ;
	
	var otherOption = {xtype : 'fieldset' ,width:280,height:60,layout : 'column' , title : '其他选项' , items : [this.chkFreeSelPerson]}
	
	TaskPowerPanel.superclass.constructor.call(this,{
		width:550,
        height:450,
        layout : 'column' ,
        border : true ,
        bodyStyle:'margin-top:15px' ,
		items : [
			{
				bodyBorder : false ,
				bodyStyle : 'margin-left:40px;margin-top:5px',
				border : false ,
				//region : 'west',
				columnWidth : .45,
				items : [listBox]
			},
			{	
				bodyBorder : false ,
				bodyStyle : 'margin-left:10px',
				border : false ,
				//region : 'center',
				columnWidth : .55,
				layout : 'column' ,
				border : false ,
				items : [
					{
						bodyBorder : false,
						columnWidth : 1 ,
						layout : 'form' ,
						items:personSelOption
					},
					{
						bodyStyle : 'margin-top:5px' ,
						bodyBorder :false, 
						columnWidth : 1 ,
						layout : 'form' ,
						items:otherOption
					}
				]
			}
		]
	})
	
}

Ext.extend(TaskPowerPanel , Ext.Panel , {
	/*
	 * 获得是单人处理还是多人处理的封装方法
	 */
	getProcessKindValue : function() {
		if(this.singleProce.checked){
			return this.singleProce.inputValue ;
		}
		if(this.multiProce.checked) {
			return this.multiProce.inputValue ;
		}
		return  "" ;
	},
	
	/*
	 * 获得是否串行的封装方法
	 */
	getProcessModeValue : function() {
		return this.chkSerial.checked?this.chkSerial.inputValue:"0"
	},
	
	/*
	 * 获得可自由选择处理人的封装方法
	 */
	getIsChooseActorValue : function() {
		return this.chkFreeSelPerson.checked?this.chkFreeSelPerson.inputValue:"0"
	},
	
	/*
	 * 获得是只允许指定一个处理人的封装方法
	 */
	getOnlyOnePersonValue : function() {
		return this.chkIndiOne.checked?this.chkIndiOne.inputValue:"0"
	}
})