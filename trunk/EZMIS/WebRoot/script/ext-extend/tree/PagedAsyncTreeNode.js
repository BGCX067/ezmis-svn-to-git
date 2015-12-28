
/**
 * 支持翻页功能树形节点
 */
Ext.app.PagedAsyncTreeNode=function(config){
	
	Ext.apply(config,{uiProvider:Ext.app.PagedTreeNodeUI})
	
	Ext.app.PagedAsyncTreeNode.superclass.constructor.apply(this, arguments);
	
	this.pageCtrl=new Ext.app.Page({});
}

Ext.extend(Ext.app.PagedAsyncTreeNode, Ext.tree.AsyncTreeNode, {
	/**
	 * 更新分页控制器
	 * 每当翻页的时候需要更新分页控制器
	 */
	updatePageCtrlBar : function(){
		var nodeEl=this.ui.getEl();
		var nodeDiv=nodeEl.childNodes[0];

		if(this.pageCtrl.totalCount>1){
			
			//移除原分页控制器
			var oldPageCtrl=nodeDiv.lastChild;
			if(oldPageCtrl && oldPageCtrl.attributes['type'] && oldPageCtrl.attributes['type'].value=='pagectrl'){
				nodeDiv.removeChild(oldPageCtrl);
			}
			
			//装上新的分页控制器
			var pageHTML=this.pageCtrl.getPageHTML();
			Ext.DomHelper.insertHtml("beforeEnd", nodeDiv, pageHTML);
		}	
	},
	/**
	 * 做翻页动作
	 * 参数a代表当前点击的翻页链接对象
	 */
	doPageAction:function(a){
		var ptype=a.attributes['ptype'].value;
		if(ptype=='prepage'){
			this.pageCtrl.prePage();
		}else if(ptype=='nextpage'){
			this.pageCtrl.nextPage();
		}else if(ptype=='num'){
			var pno=a.attributes['pno'].value;
			
			this.pageCtrl.pageNo=pno;
		}
		this.reload();
	}
});

Ext.app.Page = function(config){
	this.totalCount=config.totalCount?config.totalCount:1;
	this.pageNo=config.pageNo?config.pageNo:1;
	this.pageSize=config.pageSize?config.pageSize:1;
};






/**
 * 跳往指定页
 */
Ext.app.Page.goPage		= function(node,pNo){
	var tmpIdx=pNo;
	if(tmpIdx>this.totalCount){
		tmpIdx=this.totalCount;
	}
	this.pageNo=tmpIdx;
};


Ext.app.Page.prototype={
	
	
	
	/**
	 * 获取分页HTML脚本
	 */
	getPageHTML : function(){
		var pageInfo=[
		     "<span type='pagectrl'>",
		     "<span class='page-span-1' title='上一页'>",
             	"<a href='#' ptype='prepage'>|<<</a>",
             "</span>",
             "<span class='page-span-2'>第"+this.pageNo+"/"+this.totalCount+"页"
        ];
		/*
		var startIdx=this.pageNo>3?(this.pageNo-3):1;
		if(startIdx!=1){
			pageInfo=pageInfo.concat("... ,");
		}
		
	 	for(var i=startIdx,j=0;i<=this.totalCount;i++,j++){
	 		
			if(i==this.pageNo){
				pageInfo=pageInfo.concat(i+" ,");
			}else{
				pageInfo=pageInfo.concat("<a href='#' ptype='num' pno='"+i+"'>"+i+"</a> ,");
			}
			if(j==5){
				pageInfo=pageInfo.concat("... ,");
				break;
			}
		}
		*/
	 	pageInfo=pageInfo.concat( [
		     "</span>",
             "<span class='page-span-1' title='下一页'>",
             	"<a href='#' ptype='nextpage'>>>| </a>",
             "</span>",
             "</span>"
        ]);
		return pageInfo.join('');
	},
	nextPage : function(node){
		var tmpIdx=this.pageNo+1;
		if(tmpIdx>this.totalCount){
			tmpIdx=this.totalCount;
		}
		this.pageNo=tmpIdx;
	},
	/**
	 * 前一页
	 */
	prePage : function(node){
		var tmpIdx=this.pageNo-1;
		if(tmpIdx<1){
			tmpIdx=1;
		}
		this.pageNo=tmpIdx;
	}
		
}