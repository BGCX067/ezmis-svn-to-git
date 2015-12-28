//"操作"结构
var Operation = Ext.data.Record.create([
	{name:'text'},{name:'name'},{name:'tip'},{name:'icon'},{name:'showText'},{name:'adminOp'}
]);

OperationGrid=function(resourceId){
	//被修改了得记录
	this.dirtyRecords=[];
	//数据源
	var ds = new Ext.data.Store({
	    	
	});
    var showTextData=[["显示",true],["不显示",false]];
     var showTextStore = new Ext.data.SimpleStore({
        fields: ['title', 'value'],
        data : showTextData // from states.js
    });
    var combo = new Ext.form.ComboBox({
        store: showTextStore,
        displayField:'title',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        selectOnFocus:true
    });
	var fm=Ext.form;
	var cm = new Ext.grid.ColumnModel([{
           header: "操作名称",
           dataIndex: 'text',
           width: 100,
           editor: new fm.TextField({
               allowBlank: false
           })
        },{
        	header: "操作简称",
			dataIndex: 'name',
			width: 100,
			editor: new fm.TextField({
				allowBlank: false
			})
        },{
			header: "操作提示",
			dataIndex: 'tip',
			width: 150,
			editor: new fm.TextField({
				allowBlank: false
			})
        },{
			header: "按钮文本",
			dataIndex: 'showText',
			width: 60,
            editor:new Ext.form.ComboBox({
		        store: showTextStore,
		        allowBlank:false,
		        displayField:'title',
		        valueField:'value',
		        typeAhead: false,
		        forceSelection: true,
		        mode: 'local',
		        editable: false,
		        triggerAction: 'all',
		        selectOnFocus:true
		    }),
		    renderer:function(value){
		    	if(value=="true" || value==true){
		    		return "显示";
		    	}else{
		    		return "不显示"
		    	}
		    	
		    }
        },{
			header: "管理员资源",
			dataIndex: 'adminOp',
			width: 70,
            editor:new Ext.form.ComboBox({
		        store:new Ext.data.SimpleStore({
			        fields: ['title', 'value'],
			        data : [['是',true],['不是',false]] // from states.js
			    }),
		        allowBlank:false,
		        displayField:'title',
		        valueField:'value',
		        typeAhead: false,
		        forceSelection: true,
		        mode: 'local',
		        editable: false,
		        triggerAction: 'all',
		        selectOnFocus:true
		    }),
		    renderer:function(value){
		    	if(value=="true" || value==true){
		    		return "是";
		    	}else{
		    		return "不是"
		    	}
		    	
		    }
        }
    ]);

    
	var config={
        store: new Ext.data.Store(),
        cm: cm,
        width:532,
        height:170,
        frame:true,
        clicksToEdit:1
    };
    
	OperationGrid.superclass.constructor.call(this,config);
	
	//为选择模型添加事件，操作列上不需要选择操作
	var sm=this.getSelectionModel();
    var ds=this.getStore();
    
    this.on("beforeedit",function(e){
    	if(e.column==1)
			return false;
    });
	this.on("afteredit",function(e){
		if(e.value!=e.originalValue){
			this.dirtyRecords.push(e.record.data);
		}
	})
    
};

Ext.extend(OperationGrid,Ext.grid.EditorGridPanel,{
	
	/**
	 * 插入操作
	 * @param op 操作对象
	 */
	insertOperation:function(op){
		 var op = new Operation({
 			text: op.text || op.resName,
            name: op.sn || op.name,
            tip:op.tip,
            icon:op.icon,
            showText:op.showText,
            adminOp:op.adminOp
        });
        var store=this.getStore();
        store.add(op);
	},
	/**
	 * 清除所有操作
	 */
	clearOperation:function(){
		var store=this.getStore();
		store.removeAll();
	}
})