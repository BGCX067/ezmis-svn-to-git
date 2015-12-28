
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
  			var root = new Ext.app.CheckboxAsyncTreeNode({
  					id: 'rootNode',
		        	text:'根节点',
		        	loader:new Ext.app.CheckboxTreeNodeLoader({
			            dataUrl:this.dataUrl,
			            listeners:{
			            	beforeload:function(loader, node, callback ){
			            		this.baseParams.parentId=(node.isRoot?"":node.id);
			            	}
			            }
			        }),
			        expanded :true
	        });
				        
  			var rootVisible=this.rootVisible?true:false;
            this.view=new Ext.tree.TreePanel({   
                applyTo:this.list,   
                autoScroll:true,   
                animate:true,
                tbar:['<font style=color:blue;font-size:12px;>*按住Ctrl键可进行级联选择</font>'],
                bbar:[{
                		id:'btnSure',text:'确定',
                		listeners: {
                			click: function(){
                				comboTree.onSure();
                			}
                		}
                	},'-',{
                		id: 'btnClear',text:'清空',
                		listeners: {
                			click: function(){
                				comboTree.clearValue();
                			}
                		}
                	}],
                height:this.listHeight | 200,   
                rootVisible:true,   
                containerScroll: true,   
                ctrlCasecade:true,	//是否只支持 按住ctrl键进行勾选的时候是级联勾选
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
  	
    onSure: function(selects){
    	this.collapse();   
        this.el.focus();
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
        	if(node.getUI().isChecked()){
				node.getUI().toggleCheck(false);
        	}else{
        		node.getUI().toggleCheck(true);
        	}
        	
        	this.collapse(); 
        }
           
        if(doFocus !== false){   
            this.el.focus();   
        }   
    },   
  
    /**
     * 将勾选的树节点node的id,text设值为comboTree的hideMode,value属性.
     */
    setValue : function(){
    	var id = "";
    	var text = "";
    	
    	var strJson = this.view.getRootNode().getCheckedIdAndTextJson(false,false);
    	strJson = "[" + strJson.substring(0,strJson.length-1) + "]";
    	var json = Ext.util.JSON.decode(strJson);
    	
    	for(var i=0; i<json.length; i++){
    		id += json[i].id ;
    		text += json[i].text ;
    		if(i<json.length-1){{
	    		id +=   ",";
	    		text +=  ",";
    		}
    	}}
    	
    	
    	this.hideMode = id;
    	Ext.app.ComboTree.superclass.setValue.call(this, text);
    },
    setData : function(id,text){
    	this.hideMode = id;
    	Ext.app.ComboTree.superclass.setValue.call(this, text);
    },
    
    isExpanded : function(){   
        return this.list && this.list.isVisible();   
    },   
  
    expand : function(){   
        if(this.isExpanded() || !this.hasFocus){   
            return;   
        }   
       	var nodes = this.view.getRootNode().childNodes;
       	for(var i=0;i<nodes.length;i++){
       		if(this.getValue().indexOf(nodes[i].text)!=-1){
       			nodes[i].attributes.checked=true;
       			nodes[i].ui.checkbox.checked=true;
       		}
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
        this.setValue();
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
    	var rootNode = this.view.getRootNode();
		this.recursionClearCk(rootNode);
    	
        this.hideMode = "";
    	Ext.app.ComboTree.superclass.setValue.call(this, "");
        this.applyEmptyText();   
    },
    /**
     * 递归取消node及其子节点的勾选
     * @param {} node
     */
    recursionClearCk: function(node){
    	node.getUI().toggleCheck(false);
    	
    	var childs = node.childNodes;
    	for(var i=0; i<childs.length; i++){
    		this.recursionClearCk(childs[i]);
    	}
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