/**
 * author:sds
 * checkbox Tree
 * 级联	支持
 * 三态	仅支持IE
 * 动态加载节点：同级联选择相冲突，所以不提供该功能
 */
Ext.app.CheckboxTreeNode=function (config){
	if(typeof(config)=='string'){
		config={text:config};
	}
	
	//给节点加上checkbox
	if(typeof(config.checked)=='undefined'){
		Ext.apply(config,{checked:false});
		Ext.apply(config,{ccCheck:true});
	}
	//设置是否级联属性
	if(typeof(config.ccCheck)=='boolean'){
		this.ccCheck=config.ccCheck;
	}
	
	config=Ext.apply(config,{uiProvider:Ext.app.CheckboxTreeNodeUI})

	//调用父类构造函数
	Ext.app.CheckboxTreeNode.superclass.constructor.call(this,config);
	
	//checkchange 事件
	this.on("checkchange",function(oNode,checked,event){
		if(typeof(event) == 'undefined') {
					return
		}
		if(!oNode.isLeaf()){
			if(oNode.childrenRendered==false){
				this.renderChildren();
			}
		}
		var tree=oNode.getOwnerTree();
		
		//处理级联选择
		if(this.ccCheck){
			//针对儿子节点 是否支持ctrl级联 否则直接级联
			if(!oNode.isChecked() || !tree.ctrlCasecade){
				this.eachChild(function(oNode){
					oNode.getUI().toggleCheck(checked);
					oNode.getUI().onCheckChange(event);
				});	
			}else{
				if(event.ctrlKey){//ctrl键是否按下
					this.expand();
					this.eachChild(function(oNode){
						oNode.getUI().toggleCheck(checked);
						oNode.getUI().onCheckChange(event);
					});	
				}
			}
			//针对父节点
			this._cascadeParentNode(oNode);
		}
	});
	/**
	 * 私有函数,用于级联处理父节点
	 */
	this._cascadeParentNode=function(oNode){
		var oParentNode=oNode.parentNode;
		var bAllChecked=true;		//全选
		var bAllUnchecked=true;		//非全选
		
		if(oParentNode!=null && oParentNode.isIndeterminate){
			if(oNode.isIndeterminate()){
				oParentNode.setIndeterminate(true);
			}else{
				//循环判断子节点，确定当前节点的状态
				for(var i=0;i<oParentNode.childNodes.length;i++){
					var oChildNode=oParentNode.childNodes[i];
					if(!oChildNode.isChecked()){
						bAllChecked=false;
					}else{
						bAllUnchecked=false;
					}
				}
				
				if(bAllChecked || bAllUnchecked){//全部选中或未选中的状态
					oParentNode.setIndeterminate(false);//仅仅IE有效
					oParentNode.setChecked(oNode.isChecked());
				}else{	//部分选中状态
					oParentNode.setIndeterminate(true);
				}
			}
			this._cascadeParentNode(oParentNode);
		}
	};
}
Ext.extend(Ext.app.CheckboxTreeNode,Ext.tree.TreeNode,{
	ccCheck:	false,		//确定是否级联check,
	isChecked:function(){
		var ui=this.getUI();
		return ui.checkbox.checked;
	},
	setChecked:function(checked,cascadeCheck){
		var ui=this.getUI();
		ui.checkbox.checked=checked;
		if(cascadeCheck!=null && cascadeCheck==true)
			this._cascadeParentNode(this);
	},
	/**
	 * 设定第三状态
	 */
	setIndeterminate:function(bool){
		var ui=this.getUI();
		ui.checkbox.indeterminate=bool;
	},
	/**
	 * 判断是否为第三状态
	 */
	isIndeterminate:function(){
		var ui=this.getUI();
		return ui.checkbox.indeterminate;
	},
	
	
	/**
	 * 取得被选中的节点编号 不带路径
	 * @bIndeterminate 是否需要返回第三状态的节点
	 * @bIncludeRoot 	是否包含根节点
	 */
	getCheckedIds:function(bIndeterminate,bIncludeRoot){
		var c=this;
		var sResult="";
		if(bIncludeRoot==undefined){
			bIncludeRoot=true;
		}
		var isRoot=(this.getOwnerTree().getRootNode().id==this.id);
		if(this.isChecked() || (bIndeterminate && this.isIndeterminate())){ 
			//是不是需要将根节点屏蔽掉
			if(!isRoot || (isRoot && bIncludeRoot)){
				sResult=this.id+",";
			}
		}
		for(var i=0;i<this.childNodes.length;i++){
			
			var tmp=this.childNodes[i].getCheckedIds(bIndeterminate,bIncludeRoot);
			if(tmp!="")
				sResult+=tmp;
		}
		return sResult;
	},
	getCheckedIdAndTextJson:function(bIndeterminate,bIncludeRoot){
		var c=this;
		var sResult="";
		if(bIncludeRoot==undefined){
			bIncludeRoot=true;
		}
		var isRoot=(this.getOwnerTree().getRootNode().id==this.id);
		if(this.isChecked() || (bIndeterminate && this.isIndeterminate())){ 
			//是不是需要将根节点屏蔽掉
			if(!isRoot || (isRoot && bIncludeRoot)){
				sResult+="{";
				sResult += "'id':'" + this.id+"',";
				sResult += "'text':'" + this.text+"'";
				sResult+="},"
			}
		}
		for(var i=0;i<this.childNodes.length;i++){
			
			var tmp=this.childNodes[i].getCheckedIdAndTextJson(bIndeterminate,bIncludeRoot);
			if(tmp!="")
				sResult+=tmp;
		}
		return sResult;
	}
});
