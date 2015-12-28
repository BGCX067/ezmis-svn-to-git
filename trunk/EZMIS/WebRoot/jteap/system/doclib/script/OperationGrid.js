/***
* 扩展字段列表
*
****/
OperationGrid = function(resourceId) {

	operationGrid = this
	//被修改了得记录
	this.dirtyRecords=[];
	//默认DS
	foDefaultDS = this.getDefaultDS(link7+"?id="+resourceId);
	var fm = Ext.form;	
	var dataType2=[	['字符串','01'],
				['数字','02'],
				['日期','03']
			];		
	var comboType = new fm.ComboBox({triggerAction: 'all',editable :false,selectOnFocus :false,forceSelection :true,mode:'local',displayField:'name',valueField:'name',
				allowBlank: false,
				store:new Ext.data.SimpleStore({fields:['name','value'],data:dataType2}),
				value:'01'		
			}) ;
	var cm = new Ext.grid.ColumnModel([{
           header: "字段名称",
           dataIndex: 'name',
           width: 115,
           editor: new fm.TextField({
               allowBlank: false
           })
        },{
        	header: "字段类型",
			dataIndex: 'type',
			width: 100,
			editor: comboType    //字段数据类型选择框 
        },{
			header: "枚举值",
			dataIndex: 'emunValue',
			width: 100,
			editor: new fm.TextField({
				allowBlank: false
			})
        },{
			header: "字段格式",
			dataIndex: 'format',
			width: 100,
            editor: new fm.TextField({
				allowBlank: false
			})
        },{
			header: "字段长度",
			dataIndex: 'len',
			width: 100,
            editor: new fm.TextField({
				allowBlank: false
			})
        },{
			header: "序列编号",
			dataIndex: 'sortno',
			width: 100,
            editor: new fm.TextField({
				allowBlank: false
			})
        }
    ]);


	OperationGrid.superclass.constructor.call(this,{
		
		tbar : ['->',{text:'添加字段',handler : this.createOperate},{text:'删除字段',handler : this.deteleOperate}],
		loadMask : true ,
		cm : cm ,
		store : foDefaultDS,
		width:536,
        height:170,
        frame:true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),  //设置单行选中模式
        clicksToEdit:2
	})
	foDefaultDS.load();
	
	//为选择模型添加事件，操作列上不需要选择操作
	var sm=this.getSelectionModel();
    var ds=this.getStore();
    
    this.on("beforeedit",function(e){
    	var id =e.record.data.id ;
    	if(id!=undefined && !canModify){	
    	//	alert(111);
			return false;
    	}
    });
	this.on("afteredit",function(e){
		if(e.value!=e.originalValue){
			this.dirtyRecords.push(e.record.data);
		}
	})
}


Ext.extend(OperationGrid , Ext.grid.EditorGridPanel , {
	//data : {'totalCount':Ext.decode(getLabelValue('flowVariable')).length,'list':Ext.decode(getLabelValue('flowOperate'))},
	/**
	 * 取得默认数据源
	 * 返回数据格式
	 * {success:true,totalCount:20,list:[{id:'123',field1:'111',...,field5'222'},{...},...]} 
	 */
	getDefaultDS:function(url){
	    var ds = new Ext.data.Store({
	        proxy: new Ext.data.ScriptTagProxy({
	            url: url
	        }),
	        reader: new Ext.data.JsonReader({
	            root: 'list',
	            totalProperty: 'totalCount',
	            id: 'id'
	        }, [
	            "id","name","type","emunValue","format","len","sortno"
	        ]),
	        remoteSort: true	
	    });
		return ds;
	},
	
	
	//添加字段
	createOperate : function() {
		var Model = Ext.data.Record.create([{name:'name'},{name:'type'},{name:'emunValue'},{name:'format'},{name:'sortno'}])
		var record = new Model(
			{
				'name' : '' ,
				'type' : '',
				'emunValue' : '' ,
				'format' : '',
				'len':'60',
				'sortno' : '0'
			}
		);
		 operationGrid.stopEditing();
         foDefaultDS.insert(0, record);
         operationGrid.startEditing(0, 0);
	} ,
	//删除字段
	deteleOperate : function() {
	    var store = this.store;
	    var records = operationGrid.selModel.getSelections();
	    var recordsLen = records.length;                     //得到行数组的长度
	    var ids = "";
	    var id = "";
		if(operationGrid.getSelectionModel().getSelected() == null) {
			Ext.Msg.alert("提示","请先选择要删除的行!");
			return ;
		}
		Ext.MessageBox.confirm('系统提示信息', '确定要删除所选的记录吗?', function(buttonobj){
			if(buttonobj == 'yes'){
				//判断扩展字段是否拥有id，没有就直接在前台删除
				var bFlag=false;
				
				for(var i = 0; i < recordsLen; i++){
					if(records[i].get("id") == "" || records[i].get("id") == null){
						operationGrid.store.commitChanges() ;
						operationGrid.store.remove(operationGrid.getSelectionModel().getSelected()) ;
					}else{
						bFlag=true;
						var id = records[i].get("id");
						if(id == null || id == ""){
							//ids += id ;
						}else{
							if(i != 0){
								ids += ","+id;
							}else{
								ids += id ;
							}
						}
					}
				}
				if(bFlag){
					//如果有id，就直接在数据库删除
					Ext.Ajax.request({
						url:link9,
						method:'post',
						params: {ids:ids},
						success:function(ajax){
							var responseText=ajax.responseText;	
					 		var responseObject=Ext.util.JSON.decode(responseText);
					 		if(responseObject.success){
					 			alert("删除成功!");
					 			operationGrid.getStore().reload();
					 		}else{
					 			alert(responseObject.msg);
					 		}
						},
						failure:function(){
							alert("提交失败!");
						}
					});
				}
			}else{
				Ext.Msg.alert("提示","请先选择要删除的行!");
	    	}
		});
	} 
})



