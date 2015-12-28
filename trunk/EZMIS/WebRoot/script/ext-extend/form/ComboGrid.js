

/**
 * 下拉式网格选择控件
 * @author tantyou
 * @date 2008-1-10
 * 
 */
Ext.app.ComboGridWediget=function(config){
    var sm=new Ext.grid.RowSelectionModel();
	config=Ext.apply(config,{
		autoScroll:true, 
		containerScroll: true,
		sm: sm,
		loadMask: true,
		frame:true,
		bbar: new Ext.PagingToolbar({
		    pageSize: 25,
		    store: config.ds,
		    displayInfo: true,
		    displayMsg: '共{2}条，目前为 {0} - {1} 条',
			emptyMsg: "没有符合条件的数据"
			
		})
	});
	
	Ext.app.ComboGridWediget.superclass.constructor.call(this,config);
}
Ext.extend(Ext.app.ComboGridWediget,Ext.grid.GridPanel,{});


Ext.app.ComboGrid=Ext.extend(Ext.form.TriggerField,{
    shadow:'frame',   
    maxHeight: 300,   
    hideTrigger:false,   
    resizable:true,   
    minListWidth:70,   
    handleHeight:8,   
    editable:false,   
    lazyInit:true,   
    hiddenValue:'',
    
    initComponent:function(){   
        Ext.app.ComboGrid.superclass.initComponent.call(this);   
        this.addEvents('expend','collapse','select');   
    },   
       
    onRender:function(ct, position){   
        Ext.app.ComboGrid.superclass.onRender.call(this, ct, position);   
         
        this.el.dom.setAttribute('readOnly', true);   
        this.el.on('mousedown', this.onTriggerClick,  this);   
        this.el.addClass('x-combo-noedit');        
        this.initGridLayer();   
    },   
       
    initGridLayer:function(){
    	cg=this;
    	
        if(!this.gridLayer){
            var cls='x-combo-list';   
               
            this.list = new Ext.Layer({   
                shadow: this.shadow, cls: [cls].join(' '), constrain:false  
            });   
  
            var lw = this.listWidth || Math.max(this.wrap.getWidth()-this.trigger.getWidth(), this.minListWidth);   
            this.list.setWidth(100);   
            this.list.swallowEvent('mousewheel');   
            this.assetHeight = 0;  
           
    		
			
			
            this.grid=new Ext.app.ComboGridWediget({
            	applyTo:this.list,
            	ds:this.initialConfig.ds,
            	cm:this.initialConfig.cm,
            	tbar:this.initialConfig.tbar,
            	height:this.initialConfig.gridHeight,
            	width:this.initialConfig.gridWidth
            });
           	
            this.grid.on("celldblclick",this.onGridDBClick,this);
           	
            
        }   
    },   
  
    onTriggerClick : function(){   
        if(this.disabled){   
            return;   
        }
        if(this.isExpanded()){   
            this.collapse();   
            this.el.focus();   
        }else {   
            this.onFocus({});   
            this.expand();
            
            if(this.dropDownRefreshData)
            	this.grid.getStore().reload();
            this.el.focus();   
        }   
    },   
       
  
    
  	onGridDBClick:function(grid, rowIndex, columnIndex, e){
  		var record=grid.getStore().getAt(rowIndex);
  		//alert();
  		this.onSelect(record);   
  		this.list.hide();
  		this.el.focus();  
  	},
    onSelect : function(record){
    	
    	this.hiddenValue = record.get(this.valueField);   
        this.setValue(record.get(this.displayField));   
        
        this.fireEvent('select', this, record);   
    },     
    /*
    setValue : function(value){
    	
        //var node=this.view.getNodeById(id);   
        Ext.app.ComboGrid.superclass.setValue.call(this, node?node.text:'');   
    },*/
    isExpanded : function(){   
        return this.list && this.list.isVisible();   
    },   
  
    expand : function(){   
        if(this.isExpanded() || !this.hasFocus){   
            return;   
        }   
        this.list.alignTo(this.wrap, this.listAlign);   
        this.list.show();
        Ext.getDoc().on('mousewheel', this.collapseIf, this);   
        Ext.getDoc().on('mousedown', this.collapseIf, this);   
        this.fireEvent('expand', this);   
    },   
  
    collapse : function(){   
        if(!this.isExpanded()){   
            return;   
        }   
        this.list.hide();   
        Ext.getDoc().un('mousewheel', this.collapseIf, this);   
        Ext.getDoc().un('mousedown', this.collapseIf, this);   
        this.fireEvent('collapse', this);   
    },   
  
    collapseIf : function(e){   
        if(!e.within(this.wrap) && !e.within(this.list)){   
            this.collapse();   
        }   
    },   
    clearValue : function(){   
        if(this.hiddenField){   
            this.hiddenField.value = '';   
        }   
        this.setValue('');   
        this.applyEmptyText();   
    },    
    onDestroy : function(){   
    	
        if(this.grid){   
            this.grid.el.removeAllListeners();   
            this.grid.el.remove();   
            this.grid.purgeListeners();   
        }   
        if(this.grid){   
            this.grid.destroy();   
        }   
        Ext.app.ComboGrid.superclass.onDestroy.call(this);   
    },    
    getValue : function(){   
        if(this.valueField){
            return typeof this.value != 'undefined' ? this.value : '';   
        }else{   
            return Ext.app.ComboGrid.superclass.getValue.call(this);   
        }   
    },
    getHiddenValue:function(){
    	return this.hiddenValue;
    }
       
});   
  
Ext.reg('combogrid', Ext.app.ComboGrid);   