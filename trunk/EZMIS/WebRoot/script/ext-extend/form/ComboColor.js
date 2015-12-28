Ext.app.ComboColor=Ext.extend(Ext.form.TriggerField,{   
    shadow:'sides',   
    maxHeight: 300,   
    hideTrigger:false,   
    resizable:true,   
    minListWidth:70,   
    handleHeight:8,   
    lazyInit:true,   
    initComponent:function(){   
        Ext.app.ComboColor.superclass.initComponent.call(this);   
        this.addEvents('expend','collapse','select','loaded');   
    },   
       
    onRender:function(ct, position){   
        Ext.app.ComboColor.superclass.onRender.call(this, ct, position);   
        if(!this.editable){
        	this.el.dom.setAttribute('readOnly', true);
        }
        this.el.on('mousedown', this.onTriggerClick,  this);   
        this.el.addClass('x-combo-noedit');        
        this.initColorLayer();   
    },   
    initColorLayer:function(){
    	var comboColor = this;
        if(!this.treeLayer){   
            var cls='x-combo-list';
            this.list = new Ext.Layer({
                shadow: this.shadow, cls: [cls].join(' '), constrain:false  
            });   
            this.list.setWidth(150);   
            this.list.setHeight(120); 
            this.list.swallowEvent('mousewheel');   
            this.assetHeight = 0;
            
            this.view= new Ext.Panel({
            	applyTo:this.list,
            	items:[{
            		id:'ColorPal',
            		xtype:'colorpalette',
            		combo:comboColor,
            		listeners:{
            			select : this.onViewClick
            		}
            	}]
            }); 
  
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
            this.el.focus();   
        }   
    },   
       
    onViewClick : function(cp, color){   
    	cp.combo.setValue("#"+color);
    	cp.combo.onTriggerClick();
    },   
       
    /**
     * @id 用于hidden value
     * @text 用于显示
     */
    setValue : function(text){
    	this.value = text;
    	Ext.app.ComboColor.superclass.setValue.call(this, text);
    },       
  
    isExpanded : function(){   
        return this.list && this.list.isVisible();   
    },   
  
    expand : function(){   
        if(this.isExpanded() || !this.hasFocus){   
            return;   
        }   
        this.list.alignTo(this.wrap, this.listAlign);   
        this.list.show();   
        //Ext.getDoc().on('mousewheel', this.collapseIf, this);   
        Ext.getDoc().on('mousedown', this.collapseIf, this);   
        this.fireEvent('expand', this);   
    },   
  
    collapse : function(){   
        if(!this.isExpanded()){   
            return;   
        }   
        this.list.hide();   
        //Ext.getDoc().un('mousewheel', this.collapseIf, this);   
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
        setValue('');   
        this.applyEmptyText();   
    },    
    onDestroy : function(){   
        if(this.view){   
            this.view.el.removeAllListeners();   
            this.view.el.remove();   
            this.view.purgeListeners();   
        }   
        if(this.list){   
            this.list.destroy();   
        }   
  
        Ext.app.ComboColor.superclass.onDestroy.call(this);   
    },    
    getValue : function(){   
        if(this.valueField){   
            return typeof this.value != 'undefined' ? this.value : '';   
        }else{   
            return Ext.app.ComboColor.superclass.getValue.call(this);   
        }   
    },
    getColorPanel:function(){
    	return this.view;
    }       
       
});   
  
Ext.reg('combocolor', Ext.app.ComboColor);