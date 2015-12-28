 
  
Ext.app.IFrameComponent=Ext.extend(Ext.BoxComponent,{   
    onRender:function(ct,position){   
        this.el=ct.createChild({   
            tag:'iframe',   
            id:'iframe-'+this.id,   
            frameBorder:0,   
            src:this.url   
        });   
    },   
  
    load:function(url){   
        Ext.get('iframe-'+this.id).dom.src=url;   
    }   
});   
  
Ext.app.ComboTree=Ext.extend(Ext.form.TriggerField,{   
    shadow:'sides',   
    maxHeight: 300,   
    hideTrigger:false,   
    resizable:true,   
    minListWidth:70,   
    handleHeight:8,   
    //editable:false,   
    lazyInit:true,   
    initComponent:function(){   
        Ext.app.ComboTree.superclass.initComponent.call(this);   
        this.addEvents('expend','collapse','select','loaded');   
    },   
       
    onRender:function(ct, position){   
        Ext.app.ComboTree.superclass.onRender.call(this, ct, position);   
        if(!this.editable){
        	this.el.dom.setAttribute('readOnly', true);
        }
        this.el.on('mousedown', this.onTriggerClick,  this);   
        this.el.addClass('x-combo-noedit');        
           
        this.initTreeLayer();   
    },   
       
    initTreeLayer:function(){
    	var comboTree = this;
        if(!this.treeLayer){   
            var cls='x-combo-list';   
               
            this.list = new Ext.Layer({   
                shadow: this.shadow, cls: [cls].join(' '), constrain:false  
            });   
            var lw = this.listWidth || Math.max(this.wrap.getWidth()-this.trigger.getWidth(), this.minListWidth);   
            this.list.setWidth(lw);   
            this.list.swallowEvent('mousewheel');   
            this.assetHeight = 0;   
  			var root=null;
  			if(this.localData){
  				root=new Ext.tree.TreeNode({   
                   text:this.rootText?this.rootText:"根节点",
                   id:this.rootId?this.rootId:"rootNode"
                });
  			}else{
  				root=new Ext.tree.AsyncTreeNode({   
                   text:this.rootText?this.rootText:"根节点",
                   id:this.rootId?this.rootId:"rootNode"
                });
                root.on("load",function(){
                	comboTree.fireEvent('loaded', this, root);  
                })
  			}
  			var rootVisible=this.rootVisible?true:false;
            this.view=new Ext.tree.TreePanel({   
                applyTo:this.list,   
                autoScroll:true,   
                animate:true,   
                width: this.width | 140,
                listWidth: this.listWidth | 140,
                height:this.listHeight | 200,   
                rootVisible:rootVisible,   
                containerScroll: true,   
                loader:new Ext.tree.TreeLoader({   
                    dataUrl:this.dataUrl
                }),   
                root:root 
            });   
               
            this.view.on('click',this.onViewClick,this);   
  
            this.resizer=new Ext.Resizable(this.list,{   
                pinned:true,   
                handles:'se'   
            });   
            this.resizer.on('resize',function(r,w,h){   
                this.view.setHeight(h-this.handleHeight);   
            },this);   
  
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
       
    onViewClick : function(doFocus){   
        var node=this.view.getSelectionModel().getSelectedNode();   
           
        if(node){   
            this.onSelect(node);   
        }   
           
        if(doFocus !== false){   
            this.el.focus();   
        }   
    },   
  
    onSelect : function(node){   
        this.setValue(node.id);   
        this.collapse();   
        this.fireEvent('select', this, node);   
    },             
       
    /**
     * @id 用于hidden value
     * @text 用于显示
     */
    setValue : function(id,text){
    	this.hideMode = id;
    	var tmpText;
    	if(text){
    		tmpText=text;
    	}else{
	        var node=this.view.getNodeById(id);
	        tmpText=node?node.text:'';
    	}
    	Ext.app.ComboTree.superclass.setValue.call(this, tmpText);
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
  
        Ext.app.ComboTree.superclass.onDestroy.call(this);   
    },    
    getValue : function(){   
        if(this.valueField){   
            return typeof this.value != 'undefined' ? this.value : '';   
        }else{   
            return Ext.app.ComboTree.superclass.getValue.call(this);   
        }   
    },
    getTree:function(){
    	return this.view;
    }       
       
});   
  
Ext.reg('combotree', Ext.app.ComboTree);   